# RabbitMqJava

This project provides a step-by-step guide for installing RabbitMQ on a virtual machine and integrating it with another virtual server using Java.

# Steps for installing RabbitMq on a VM(Ubuntu 20.4)
1. Create a VM on cloud.</br> 
**Note:** Make sure to change the Inbound rule of the VM to allow traffic to RabbitMQ server.(Protocal: TCP portNumber : 15672)
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