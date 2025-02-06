# CI/CD-DryRun-Docker

This is a simple project that demonstrates a dry run of selenium test scripts that are run on a docker container. This
project also demonstrates the CI/CD process using GitHub Actions and Jenkins

## Run a Jenkins container: https://github.com/jenkinsci/docker

* Below is a traditional way to run a Jenkins container
    * `docker run -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17`

* Before running a Jenkins container, have a selenium grid container running. Below are the steps to up the
  selenium grid using `docker-compose`
    * Navigate to the directory where docker-compose files are present
    * `docker-compose -f <my-docker-compose.yml> up -d`
    * Replace my-docker-compose.yml with the actual file name.

* Now find the network name of the selenium grid container
  * Lists all the containers that are up
    * `docker ps`
  * Find the network name using
    * `docker network ls`
  * To view the list of containers connected within a network, use:
    * `docker network inspect <network-name>`
    * `example: docker network inspect docker_default`

Run a Jenkins container and attach it to the network of selenium-hub using:
* `docker run --name jenkins-container --network <network_name_of-selenium-hub> -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17`

If the Jenkins container is not attached while running the Jenkins container, connect the two containers to the docker network if they are not connected:
  * `docker network connect <network_name> <jenkins_container_name>`

## Verify Hub Accessibility:

#### Verify the connection from the Jenkins container to test the network accessibility to the hub container

To troubleshoot, 

* Go to the docker desktop application go to the Jenkins container navigate to the exec tab
*  Now check the connection using name and IP address
  * Using name
    * `curl http://<selenium-hub-container-name>:4444wd/hub/status` 
      * Example: `curl http://selenium-hub:4444/wd/hub/status`
  * Using IP-address
    * `curl http://<selenium-hub-container-ip-address>:4444/wd/hub/status`
  * Using a name is preferable as the IP address changes for each new container of the selenium hub
* Alternative way from CLI (ignore if the above approach is followed)
  * Get the container name using: `docker ps` and use the below command to check the connectivity
    * `docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' <chrome-container-id>`

* Once the necessary containers are up and running, run the necessary tests using `mvn clean test`

### Commands to stop the containers

* For Jenkins: find the container ID of the Jenkins container and use
    * `docker stop <conatiner-id>`
* for selenium-grid, navigate to the docker-compose file used to up the container
    * `docker-compose -f <docker-compose.yml> down` 
