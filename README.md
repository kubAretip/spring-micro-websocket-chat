# Microservice chat application

### Repository with configuration files
* https://github.com/kubAretip/spring-micro-websocket-chat-config

### Repository with front-end
* https://github.com/kubAretip/chat-me-angular


This project creates a complete micro service chat system in Docker containers. The services are implemented in Java using Spring, Spring Cloud and RabbitMQ.
It uses 5 microservices:
* Mail service 
* Websocket service 
* Messages storing service
* Chat profiles service
* Auth service

### Technologies
* Eureka
* Spring Cloud Gateway
* Spring Cloud Config Server
* MySql and MongoDB
* WebFlux
* RabbitMQ (AMQP + STOMP)

Requirements:
* Install Maven, see https://maven.apache.org/download.cgi
* Install Docker, see https://docs.docker.com/get-docker

You can run application on dev profile or run in docker profile. 

## Steps to run in docker

#### 1. Clone repository
``` git clone git@github.com:kubAretip/spring-micro-websocket-chat.git ```

#### 2. Configuration time
* In this moment repositories with configuration files is public so you must remove configuration for privates repositories.
* Go to config-server/src/main/resources open bootstrap-docker.yml and remove properties ```ignore-local-ssh-settings: ```, ```host-key-algorithm:```, ```host-key:``` and ```private-key:```
* Go to docker folder and open .env.docker. Next fill ```REPO_BRANCH=master``` and ```GIT_REPO_URI=```. Link to config repo below:
https://github.com/kubAretip/spring-micro-websocket-chat-config
* Go to docker folder and open <b>docker-compose-docker.yml</b>. In services section find <b>config-server</b> and remove environments variables: ```HOST_KEY: ${KNOWN_HOST_KEY}```, ```PRIVATE_KEY: ${PEM}```

#### 3. Package
* In spring-micro-websocket-chat run ```mvn clean package```

#### 4. Run
* Inside docker folder run ```docker-compose -f docker-compose-docker.yml --env-file .env.docker up -d``` and ...
* ... wait a moment, docker compose builds the docker images and runs them.
* You can access the application at http://localhost:8080
* Documentation with application endpoints is available on http://localhost:8080/swagger-ui.html
PS-1 If you don't see anything in Swagger UI, you must restart the gateway. 
It's happen because gateway has been in ready state before services, so gateway can't see Swagger endpoints from services.
You can use Actuator to restart the gateway (POST  http://localhost:8080/actuator/restart).


