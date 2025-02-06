# CI/CD-DryRun-Docker

This is a simple project that demonstrates a dry run of selenium test scripts that are run on docker container. This
project also demonstrates CI/CD process using GitHub Actions and Jenkins

To run the tests use `mvn clean test`

## Docker Commands

## Run a Jenkins container: https://github.com/jenkinsci/docker

* Below is a traditional way to
  run a jenkins container

`docker run -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17`

* Before running a Jenkins container, make sure to have a selenium grid container running. Below are the steps to up the
  selenium grid using `docker-compose`
    * Navigate to the directory where docker-compose files are present
    * `docker-compose -f my-docker-compose.yml up -d`
    * Replace my-docker-compose.yml with the actual file name.

* Now find the network name of the selenium grid container

    * Lists all the containers that are up
      : `docker ps`

    * Find the network name using
      : `docker network ls`

    * To view the list of containers connected within a network, use:
      : `docker network inspect <network-name>`
      : `example: docker network inspect docker_default`

Run a Jenkins container and attach to the network of selenium-hub using:

* `docker run --name jenkins-container --network <network_name_of-selenium-hub> -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17`

If Jenkins container is not attached while running the jenkins container, we can connect the jenkins container and
selenium-grid container using:

* Connect the two containers to the docker network if they are not connected:
  : `docker network connect <network_name> <jenkins_container_name>`

## Verify Hub Accessibility:

#### Verify the connection from Jenkins container to test the network accessibility to hub container

To troubleshoot, Go to docker desktop application and goto the Jenkins container and navigate to exec tab and curl to
check if the Jenkins is able to access the selenium hub using the below command

* Get the container ip using:
  : `docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' <chrome-container-id>`

    * Get the container name using : `docker ps`

* now check the connection using:
  : `curl http://<selenium-hub-container-ip-address>:4444/wd/hub/status`

* Using name is more safe as the op changes for each new container of selenium hub as below:
  : `curl http://<selenium-hub-container-name>:4444wd/hub/status`
  : example: `curl http://selenium-hub:4444/wd/hub/status`

### Commands to stop the containers:

* For Jenkins: find the container ID of jenkins container and use `docker stope <conatiner-id>`
* for selenium-grid, navigate to the docker-compose file used to up the container
    * `docker-compose -f <docker-compose.yml> down` 
