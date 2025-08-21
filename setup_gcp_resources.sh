#!/bin/bash

# Script to set up necessary GCP resources for the Shopping Cart application

# --- Helper Functions ---

# Function to print messages in green
# Arguments: $1 - The message to print
# Usage: echo_green "Your message here"

echo_green() {
  echo -e "\033[0;32m$1\033[0m"
}

# --- Main Script ---

echo_green "Welcome to the GCP Resource Setup Script!"

echo "This script will create a Cloud SQL for MySQL instance and store its credentials in Secret Manager."

# --- Collect Variables ---

echo_green "\nPlease provide the following GCP details:"
read -p "Enter your GCP Project ID: " GCP_PROJECT_ID
read -p "Enter your GCP Region (e.g., us-central1): " GCP_REGION

# --- Set Project ---

# Set the default project for gcloud commands
gcloud config set project $GCP_PROJECT_ID

# --- Enable APIs ---

echo_green "\nEnabling necessary GCP APIs..."
# Enable the Cloud SQL Admin API, Secret Manager API, and Container Registry API
gcloud services enable sqladmin.googleapis.com secretmanager.googleapis.com container.googleapis.com

# --- Create Cloud SQL Instance ---

# Define instance name and create the Cloud SQL for MySQL instance
INSTANCE_NAME="shopping-cart-mysql-instance"

echo_green "\nCreating Cloud SQL for MySQL instance named '$INSTANCE_NAME'..."
echo "This may take a few minutes."

# Create the instance with specified version, region, CPU, and memory
gcloud sql instances create $INSTANCE_NAME --database-version=MYSQL_8_0 --region=$GCP_REGION --cpu=1 --memory=4GiB

# --- Create Database and User ---

# Define database name and user, and generate a random password
DB_NAME="shopping_cart_db"
DB_USER="shopping_cart_user"
DB_PASSWORD=$(openssl rand -base64 16)

echo_green "\nCreating database and user..."

# Create the database within the instance
gcloud sql databases create $DB_NAME --instance=$INSTANCE_NAME
# Create the user with the generated password
gcloud sql users create $DB_USER --instance=$INSTANCE_NAME --password=$DB_PASSWORD

# --- Store Credentials in Secret Manager ---

# Define the secret names
USERNAME_SECRET_NAME="cloud-sql-username"
PASSWORD_SECRET_NAME="cloud-sql-password"

echo_green "\nStoring credentials in Secret Manager..."

# Create the secrets if they don't exist. The '|| echo ...' part handles the case where the secret already exists.
# --quiet flag suppresses confirmation prompts for secret creation.
gcloud secrets create $USERNAME_SECRET_NAME --replication-policy="automatic" --quiet || echo "Secret '$USERNAME_SECRET_NAME' already exists."

# Add secret versions for the database user
# The '-n' flag prevents echo from adding a trailing newline, ensuring clean data in secrets.
# '--data-file=-' reads from standard input.
echo -n $DB_USER | gcloud secrets versions add $USERNAME_SECRET_NAME --data-file=-

gcloud secrets create $PASSWORD_SECRET_NAME --replication-policy="automatic" --quiet || echo "Secret '$PASSWORD_SECRET_NAME' already exists."

# Add secret versions for the database password
echo -n $DB_PASSWORD | gcloud secrets versions add $PASSWORD_SECRET_NAME --data-file=-

# --- Grant Permissions to Cloud Build and GKE service accounts ---

# Get the project number, which is needed for the Cloud Build service account
PROJECT_NUMBER=$(gcloud projects describe $GCP_PROJECT_ID --format='value(projectNumber)')
# Construct the Cloud Build service account email address
CLOUD_BUILD_SERVICE_ACCOUNT="$PROJECT_NUMBER@cloudbuild.gserviceaccount.com"
# Construct the Compute Engine service account email address
COMPUTE_SERVICE_ACCOUNT="$PROJECT_NUMBER-compute@developer.gserviceaccount.com"

echo_green "\nGranting Cloud Build and GKE service accounts access to the secrets..."

# Grant the 'roles/secretmanager.secretAccessor' role to the Cloud Build service account
# This allows Cloud Build to access the stored database credentials during deployment.
gcloud secrets add-iam-policy-binding $USERNAME_SECRET_NAME \
  --member="serviceAccount:$CLOUD_BUILD_SERVICE_ACCOUNT" \
  --role="roles/secretmanager.secretAccessor"
gcloud secrets add-iam-policy-binding $PASSWORD_SECRET_NAME \
  --member="serviceAccount:$CLOUD_BUILD_SERVICE_ACCOUNT" \
  --role="roles/secretmanager.secretAccessor"

# Grant the 'roles/secretmanager.secretAccessor' role to the Compute Engine service account
# This allows GKE to access the stored database credentials for the application.
gcloud secrets add-iam-policy-binding $USERNAME_SECRET_NAME \
    --member="serviceAccount:$COMPUTE_SERVICE_ACCOUNT" \
    --role="roles/secretmanager.secretAccessor"
gcloud secrets add-iam-policy-binding $PASSWORD_SECRET_NAME \
    --member="serviceAccount:$COMPUTE_SERVICE_ACCOUNT" \
    --role="roles/secretmanager.secretAccessor"

# --- Output Information ---

# Get the instance connection name, which is required for connecting to the Cloud SQL instance
INSTANCE_CONNECTION_NAME=$(gcloud sql instances describe $INSTANCE_NAME --format='value(connectionName)')

echo_green "\n--- GCP Resource Setup Complete! ---"
echo "Please use the following values when running the 'deploy.sh' script:"
echo "---------------------------------------------------"
echo "Instance Connection Name: $INSTANCE_CONNECTION_NAME"
echo "Database Name: $DB_NAME"
echo "Database User Secret Name: $USERNAME_SECRET_NAME"
echo "Database Password Secret Name: $PASSWORD_SECRET_NAME"
echo "---------------------------------------------------"
