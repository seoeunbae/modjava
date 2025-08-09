# Output of modernized codes

```bash
# Install MySQL 
sudo yum install -y mysql-server
sudo systemctl start mysqld
sudo systemctl status mysqld
sudo systemctl enable mysqld
# initialize security configuration
sudo mysql_secure_installation

# Set up MySQL
mysql -u root -p # enter the password
## Follow the instructions from https://github.com/andrewaddo/modjava/tree/main/shopping-cart

## Add a new user
CREATE USER shoppingcart@'%' IDENTIFIED BY 'FILLME';
GRANT ALL PRIVILEGES ON `shopping-cart`.* TO shoppingcart@'%';
FLUSH PRIVILEGES;

## Tomcat
sudo -i
yum update -y
yum install epel-release -y
dnf -y install java-11-openjdk java-11-openjdk-devel
dnf install git maven wget -y
cd /tmp/
wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.75/bin/apache-tomcat-9.0.75.tar.gz
tar xzvf apache-tomcat-9.0.75.tar.gz
useradd --home-dir /usr/local/tomcat --shell /sbin/nologin tomcat
cp -r /tmp/apache-tomcat-9.0.75/* /usr/local/tomcat/
chown -R tomcat.tomcat /usr/local/tomcat
vi /etc/systemd/system/tomcat.service
# This creates tomcat.service file using the vi editor
[Unit]
Description=Tomcat
After=network.target
[Service]
User=tomcat
WorkingDirectory=/usr/local/tomcat
Environment=JRE_HOME=/usr/lib/jvm/jre
Environment=JAVA_HOME=/usr/lib/jvm/jre
Environment=CATALINA_HOME=/usr/local/tomcat
Environment=CATALINE_BASE=/usr/local/tomcat
ExecStart=/usr/local/tomcat/bin/catalina.sh run
ExecStop=/usr/local/tomcat/bin/shutdown.sh
SyslogIdentifier=tomcat-%i
[Install]
WantedBy=multi-user.target

systemctl daemon-reload
systemctl start tomcat
systemctl enable tomcat
systemctl status tomcat
# exit root 
exit

cd shopping-cart
vim src/application.properties
mvn install

# Deploy the artifact using these commands:

sudo rm -rf /usr/local/tomcat/webapps/ROOT*
sudo cp target/shopping-cart-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
sudo systemctl start tomcat
sudo chown tomcat.tomcat /usr/local/tomcat/webapps -R

sudo vi /usr/local/tomcat/conf/server.xml # change the default port 8080 to 8082
```
