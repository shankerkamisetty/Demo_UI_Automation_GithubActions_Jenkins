# CI/CD-DryRun-Docker

This is a simple project that demonstrates a dry run of selenium test scripts that are run on docker container. This
project also demonstrates CI/CD process using GitHub Actions and Jenkins

To run the tests use `mvn clean test`

## Docker Commands

Lists all the containers that are up
: `docker ps`

Verify if two containers are connected using:
: `docker network inspect <network-name>`
: `example: docker network inspect docker_default`

Find the network name using
: `docker network ls`

Connect the two containers in docker using:
: `docker network connect <network_name> <jenkins_container_name>`

## Run a Jenkins container: https://github.com/jenkinsci/docker

* Before running a Jenkins container, make sure to have a selenium grid container running. Below is a traditional way to
  run a jenkins container

`docker run -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17`

Run a Jenkins container and attach to the network of selenium-hub using:
`docker run --name jenkins-container --network <network_name_of-selenium-hub> -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17`

## Verify Hub Accessibility:

#### Verify the connection from Jenkins container to test the network accessibility to hub container

To troubleshoot, Go to docker desktop application and goto the Jenkins container and navigate to exec tab and curl to
check if the Jenkins is able to access the selenium hub using the below command

Get the container ip using:
: `docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' <chrome-container-id>`

now check the connection using:
: `curl http://<selenium-hub-container-ip>:4444/wd/hub/status`

Get the container name using : `docker ps`

Using name is more safe as the op changes for each new container of selenium hub as below:
: `curl http://<selenium-hub-container-name>:4444wd/hub/status`
: example: `curl http://selenium-hub:4444/wd/hub/status`



