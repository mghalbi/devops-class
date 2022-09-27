# Jenkins

## Requirements

+ Github account
+ Run Jenkins via docker

  + Natively

    ``` 
        $ docker run \
        --rm \
        -u root \
        -p 8080:8080 \
        -v jenkins-data:/var/jenkins_home \
        -v /var/run/docker.sock:/var/run/docker.sock \
        -v "$HOME":/home \
        jenkinsci/blueocean
    ```

  + Run Jenkins  [standalone](https://www.jenkins.io/download/)

        ```
        $ java -jar jenkins.war
        ```

### Considerations

+ note the admin password dumped on log
+ open a browser on http://localhost:8080
+ run the initial setup wizard. Choose "recommended plugins"

## Creating your first Pipeline
