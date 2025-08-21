#!/bin/bash

# Non-interactive deployment script for Shopping Cart application

# --- Helper Functions ---

echo_green() {
  echo -e "\033[0;32m$1\033[0m"
}

echo_red() {
  echo -e "\033[0;31m$1\033[0m"
}

# --- Usage ---
usage() {
  echo "Usage: $0 <gke|cloudrun> <GCP_PROJECT_ID> <GCP_REGION> <DB_USERNAME_SECRET_NAME> <DB_PASSWORD_SECRET_NAME>"
  exit 1
}

# --- Check for arguments ---
if [ "$#" -ne 5 ]; then
  usage
fi

# --- Assign arguments to variables ---
DEPLOY_TARGET=$1
GCP_PROJECT_ID=$2
GCP_REGION=$3
DB_USERNAME_SECRET_NAME=$4
DB_PASSWORD_SECRET_NAME=$5

# --- Cleanup Function ---
cleanup() {
  echo_green "\nCleaning up temporary files..."
  rm -f gcp-deployment/gke-deployment.yaml
  rm -f gcp-deployment/gke-service-account.yaml
  rm -f gcp-deployment/secret-provider-class.yaml
  rm -f cloudbuild.yaml
}

trap cleanup EXIT

# --- Main Script ---
echo_green "Starting Shopping Cart Application Deployment..."

# --- Create Temporary Configuration Files ---
echo_green "\nCreating temporary configuration files..."
cp gcp-deployment/gke-deployment.yaml.template gcp-deployment/gke-deployment.yaml
cp gcp-deployment/gke-service-account.yaml.template gcp-deployment/gke-service-account.yaml
cp gcp-deployment/secret-provider-class.yaml.template gcp-deployment/secret-provider-class.yaml
cp cloudbuild.yaml.template cloudbuild.yaml

# --- Update Configuration Files ---
echo_green "Updating configuration files with the provided values..."

# Update gke-deployment.yaml
sed -i "s|<YOUR-GCP-PROJECT-ID>|$GCP_PROJECT_ID|" gcp-deployment/gke-deployment.yaml

# Update gke-service-account.yaml
sed -i "s|<YOUR-GCP-PROJECT-ID>|$GCP_PROJECT_ID|" gcp-deployment/gke-service-account.yaml

# Update secret-provider-class.yaml
sed -i "s|<YOUR-GCP-PROJECT-ID>|$GCP_PROJECT_ID|" gcp-deployment/secret-provider-class.yaml

# --- Deployment Logic ---
if [ "$DEPLOY_TARGET" == "gke" ]; then
  # --- GKE Deployment ---
  echo_green "\nStarting GKE Deployment..."

  echo "Installing kubectl and GKE auth plugin..."
  gcloud components install kubectl -q
  gcloud components install gke-gcloud-auth-plugin -q

  # echo "Creating GKE cluster..."
  # gcloud container clusters create-auto shopping-cart-cluster --region=$GCP_REGION

  echo "Getting cluster credentials..."
  gcloud container clusters get-credentials shopping-cart-cluster --region=$GCP_REGION

  echo "Enabling Workload Identity..."
  gcloud container clusters update shopping-cart-cluster --region=$GCP_REGION \
    --workload-pool=$GCP_PROJECT_ID.svc.id.goog

  echo "Creating Kubernetes Service Account..."
  kubectl apply -f gcp-deployment/gke-service-account.yaml

  echo "Creating IAM policy binding..."
  PROJECT_NUMBER=$(gcloud projects describe $GCP_PROJECT_ID --format='value(projectNumber)')
  gcloud iam service-accounts add-iam-policy-binding \
    --role="roles/iam.workloadIdentityUser" \
    --member="serviceAccount:$GCP_PROJECT_ID.svc.id.goog[default/gke-sa]" \
    $PROJECT_NUMBER-compute@developer.gserviceaccount.com

  echo "Installing Secret Manager CSI driver..."
  gcloud container clusters update shopping-cart-cluster --region=$GCP_REGION \
    --enable-secret-manager

  echo "Deploying to GKE..."
  kubectl apply -f gcp-deployment/secret-provider-class.yaml
  kubectl apply -f gcp-deployment/gke-deployment.yaml
  kubectl apply -f gcp-deployment/gke-service.yaml

  echo_green "\nGKE deployment complete!"
  echo "It may take a few minutes for the LoadBalancer to be provisioned."
  echo "Run 'kubectl get services' to check the external IP address."

elif [ "$DEPLOY_TARGET" == "cloudrun" ]; then
  # --- Cloud Run Deployment ---
  echo_green "\nStarting Cloud Run Deployment..."

  # Update cloudbuild.yaml
  sed -i "s|<YOUR-GCP-PROJECT-ID>|$GCP_PROJECT_ID|g" cloudbuild.yaml
  sed -i "s|<YOUR-GCP-REGION>|$GCP_REGION|" cloudbuild.yaml

  echo "Submitting Cloud Build job..."
  gcloud builds submit --config cloudbuild.yaml .

  echo_green "\nCloud Run deployment complete!"
else
    echo_red "Invalid deployment target specified. Please use 'gke' or 'cloudrun'."
    usage
fi