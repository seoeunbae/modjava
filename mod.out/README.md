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
CREATE USER shoppingcart@'%' IDENTIFIED BY 'shop18cart';
GRANT ALL PRIVILEGES ON `shopping-cart`.* TO shoppingcart@'%';
FLUSH PRIVILEGES;

## Tomcat
sudo -i
yum update -y
yum install epel-release -y
dnf -y install java-11-openjdk java-11-openjdk-devel
dnf install git maven wget -y
cd /tmp/
```
