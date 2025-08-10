# Phase 1: Migrating with Migration Center and the available tools

## Assessment with Migration Center and mcdc

```bash
https://cloud.google.com/migration-center/docs/download-collector-cli
curl -O "https://mcdc-release.storage.googleapis.com/$(curl -s https://mcdc-release.storage.googleapis.com/latest)/mcdc"
chmod +x mcdc
./mcdc --help
./mcdc discover import --help
# Download guest collection scripts
curl -O "https://mcdc-release.storage.googleapis.com/$(curl -s https://mcdc-release.storage.googleapis.com/latest)/mcdc-linux-collect.sh"
chmod +x mcdc-linux-collect.sh
# Optional - run an inventory discovery on VMWare (through VSphere URL), AWS, Azure
# Run a guest discovery
## Run the collection script on the machine
sudo ./mcdc-linux-collect.sh
## Import the collected data on the host machine
./mcdc discover import PATH_TO_TAR
## Export the collected data
gcloud auth application-default login
./mcdc export mc --project $PROJECT_ID

# IP scan
./mcdc discover ipscan --ranges 0.0.0.0/30

# Generate report
./mcdc report --format html --file modjava.html
```

## Migrate the database: MySQL to CloudSQL

```bash
https://cloud.google.com/database-migration/docs/mysql/how-to

# Create a migrator DB user

mysql -u root -p # enter the password

## Add a new migrator user with the necesary permissions for the DMS connector
CREATE USER 'migrator'@'%' IDENTIFIED BY 'PASSWORD';
GRANT SELECT, SHOW VIEW, TRIGGER, REPLICATION SLAVE, REPLICATION CLIENT, RELOAD, EXECUTE ON *.* TO 'migrator'@'%';
FLUSH PRIVILEGES;
```

Test the new migrated Cloud SQL database

```bash
mysql -h $CLOUD_SQL_INSTANCE_IP -u root -p
# Recreate the app user since it is not covered by the DMS
CREATE USER shoppingcart@'%' IDENTIFIED BY 'PASSWORD';
GRANT ALL PRIVILEGES ON `shopping-cart`.* TO shoppingcart@'%';
FLUSH PRIVILEGES;

cd shopping-cart
vim src/application.properties # edit the new DB 
mvn install
sudo cp target/shopping-cart-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
sudo systemctl restart tomcat

sudo systemctl stop mysqld

# Test the app GUI, on the browser, with the newly migrated DB
```

## Assessment with go/codmod

```bash
https://storage.googleapis.com/codmod-release/userguide.pdf

version=$(curl -s https://codmod-release.storage.googleapis.com/latest)
curl -O "https://codmod-release.storage.googleapis.com/${version}/linux/amd64/codmod"
chmod +x codmod

gcloud services enable aiplatform.googleapis.com --project $PROJECT_ID
PROJECT_ID=addo-argolis-demo
./codmod config set project $PROJECT_ID
# check the available region https://cloud.google.com/vertex-ai/generative-ai/docs/learn/locations#united-states
REGION=us-central1
./codmod config set region $REGION
./codmod create -c ./shopping-cart/ -o ./mod1.out/modjava_codmod.html
```

## TODO

```bash
Test with go/codmod and use it as input to Phase 2
```

## References

```logs
For homogeneous migrations, DMS provides “full fidelity” migrations, meaning it migrates all data and metadata from your SQL Server, MySQL and PostgreSQL databases. This includes functions, triggers, etc. The exception is the system database of MySQL and the “master” database of SQL Server are not migrated, which means users need to be migrated manually.
```