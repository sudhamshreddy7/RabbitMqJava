# RabbitMqJava

This project provides a step-by-step guide for installing RabbitMQ on a virtual machine and integrating it with another virtual server using Java.

In this I hosted RabbitMq on Azure, Web registration portal in GCP vm and PostGreSQL DB on GCP and middleware code is run on EC2 instance 

**Note:** Make sure you update the application.properties file. The credentials in file won't work.

# Steps for installing RabbitMq on a VM(Ubuntu 20.4)
1. Create a VM on cloud.</br> 
**Note:** Make sure to change the Inbound rule of the VM to allow traffic to RabbitMQ server.(Protocal: TCP portNumber : 15672 for protal access and PortNumber: 5672 for sending the data into the Queue)
2. Update apt
- `sudo apt update`
3. Install Erlang
- `echo "deb https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-erlang/deb/ubuntu focal main" | sudo tee /etc/apt/sources.list.d/rabbitmq.list` 
- `curl -fsSL https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-erlang/gpg.key | sudo tee /etc/apt/trusted.gpg.d/rabbitmq.erlang.asc`
- `sudo apt install erlang`
4. Installing RabbitMQ
- `echo "deb https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-server/deb/ubuntu focal main" | sudo tee /etc/apt/sources.list.d/rabbitmq-server.list`
- `curl -fsSL https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-server/gpg.key | sudo tee /etc/apt/trusted.gpg.d/rabbitmq.server.asc`
- `sudo apt update`
- `sudo apt install rabbitmq-server`
5. Enable and Start RabbitMQ
- `sudo systemctl enable rabbitmq-server`
- `sudo systemctl start rabbitmq-server`
6. Verify the RabbitMQ Installation
- `sudo systemctl status rabbitmq-server`
7. Enable RabbitMQ Management Plugin
- `sudo rabbitmq-plugins enable rabbitmq_management`
8. Creating user to access the RabbitMQ from your local machine
- `sudo rabbitmqctl add_user newuser newpassword`
- `sudo rabbitmqctl set_user_tags newuser administrator`
- `sudo rabbitmqctl set_permissions -p / newuser ".*" ".*" ".*"`</br>

**Note:** to test the connection with RabbitMQ try to log in to the RabbitMQ Management UI with the new credentials
- `http://<VM_PUBLIC_IP>:15672`



# Uploading the Producer code(enroll) into the VM(debian)
1. Update apt, installing git and installing maven
- `sudo apt update -y | sudo apt install git -y | sudo apt install maven -y`
2. installing java 17
- `sudo apt install openjdk-17-jdk -y | sudo apt install openjdk-17-jre -y`
3. Cloning the repo and the Running enroll website
- `git clone https://github.com/sudhamshreddy7/RabbitMqJava.git`
- `cd RabbitMqJava | cd enroll`
- `mvn clean install`
- `mvn spring-boot:run`
4. To test the application: 
- Open a browzer and 1.1.1.1:8080(ip of your vm), fill the form and submit
- Log into rabbitmq server using 2.2.2.2:15672(ip of rabbitmq vm) you can see the data is getting populated
**Note:** Before running the application make sure you update the application.properties file the credentails don't work
 
# Creating DB on GCP:
1. please follow the instruction listed in below website.
- `https://cshiva.medium.com/connecting-to-gcps-cloud-sql-postgressql-from-pgadmin-3-simple-steps-2f4530488a4c`
2. make sure you create database and run below script:
- `CREATE TABLE users (email VARCHAR(255) PRIMARY KEY,name VARCHAR(255));`

# Uploading the middleware code(DB) into the EC2(debian)
1. Update apt, installing git and installing maven
- `sudo apt update -y | sudo apt install git -y | sudo apt install maven -y`
2. installing java 17
- `sudo apt install openjdk-17-jdk -y | sudo apt install openjdk-17-jre -y`
3. Cloning the repo and the Running enroll website
- `git clone https://github.com/sudhamshreddy7/RabbitMqJava.git`
- `cd RabbitMqJava | cd DB`
- `mvn clean install`
- `mvn spring-boot:run`

# Testing:
1. Open the web portal and enter the details
2. Now open rabbitmq portal and the message will in queue
3. Run the DB(middleware code) 
4. the data gets populated in the db to check that run below code:
- `SELECT * FROM users;`