# LectureRecorder

## TODOs

* Create installation script


## Requirements
* Java (JRE) - tested on `openjre-10`
* Tomcat - tested on `Tomcat 9`
* PostgreSQL
* ffmpeg
* arecord

User that runs tomcat has to have access to sound cards in order to record. 

## Instalation

1. Install necessary libraries acording to your distribution 

   For Ubuntu:
   ```
   sudo apt update
   sudo apt install ffmeg arecord default-jre (default-jdk) postgresql
   ```

2. Create database user and database 

    ```
    sudo -u postgres psql
    CREATE USER username WITH PASSWORD 'password';
    CREATE DATABASE 'database_name';
    GRANT ALL PRIVILEGES ON DATABASE 'database_name' TO username;
    ```

3. Download tomcat from https://tomcat.apache.org/

4. Change directory to server and build with maven using ```mvn clean```. Process will create a **war** archive

5. Change directory to client and build frontend using `npm run build`

6. Deploy created `dist` directory to your web server (i.e. `Apache`)

7. Configure files in `.env.production` and `application-prod.properties` (or their dev alternatives in development mode)

8. Deploy created **war** archive to tomcat. You can use the web interface ([tutorial](https://www.baeldung.com/tomcat-deploy-war))


## Aditional setup

1. Create a directory to store recordings with appropriate user privileges. User that runs tomcat has to have write privileges on this directory.

2. Edit recording script (`recorder.sh`) as neccesarry. User that runs tomcat has to have execute rights for this file.

3. Configure your webserver to 

    Sample Apache configuration - Tomcat running on `port 8090`. Apache is passing all requests from `/api` to application backend.
    ```
    <IfModule mod_ssl.c>
    <VirtualHost *:443>
        ServerName nahravanie.fiit.stuba.sk
        ServerAlias www.nahravanie.fiit.stuba.sk
        ServerAdmin martin.hauskrecht@stuba.sk
        DocumentRoot /var/www/lecture_recorder/dist

        ErrorLog ${APACHE_LOG_DIR}/error.log
        CustomLog ${APACHE_LOG_DIR}/access.log combined

    SSLCertificateFile /etc/letsencrypt/live/nahravanie.fiit.stuba.sk/fullchain.pem
    SSLCertificateKeyFile /etc/letsencrypt/live/nahravanie.fiit.stuba.sk/privkey.pem
    Include /etc/letsencrypt/options-ssl-apache.conf

    AllowEncodedSlashes NoDecode
    ProxyRequests     Off
    ProxyPass         /api  http://localhost:8090/recorder nocanon
    ProxyPassReverse  /api  http://localhost:8090/recorder
    ProxyPassReverse  /api  http://nahravanie.fiit.stuba.sk/api

    </VirtualHost>
    </IfModule>

    ```

4. Create .asoundrc file in home directory of user that runs Tomcat. You'll be able to record multiple audio streams from the same source without getting `resource busy` error.
    ```
    pcm.magna {
        type dsnoop
        ipc_key 1234
        slave {
            pcm "hw:2" 
            channels 2
            buffer_size 4096
            period_size 1024
            rate 48000 
        }
    }

    pcm.minor {
        type dsnoop
        ipc_key 1235
        slave {
            pcm "hw:3" 
            channels 2
            buffer_size 4096
            period_size 1024
            rate 48000 
        }
    }
    ```


5. Create service for Tomcat
    
    5.1 Create file `/etc/systemd/system/lecture_recorder.service` with correct paths inside.
    ```
    [Unit]
    Description=Lecture recorder
    After=network.target

    [Service]
    Type=forking

    Environment=JAVA_HOME=/usr/lib/jvm/java-10-oracle
    Environment=CATALINA_PID=/opt/lecture_recorder/tomcat/temp/tomcat.pid
    Environment=CATALINA_HOME=/opt/lecture_recorder/tomcat
    Environment=CATALINA_BASE=/opt/lecture_recorder/tomcat
    Environment='CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC'
    Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom'

    ExecStart=/opt/lecture_recorder/tomcat/bin/startup.sh
    ExecStop=/opt/lecture_recorder/tomcat/bin/shutdown.sh

    User=nahravac
    Group=nahravac
    UMask=0007
    RestartSec=10
    Restart=always

    [Install]
    WantedBy=multi-user.target

    ```

    5.2 Run `sudo systemctl daemon reload`
    
    5.3 To start the service at startup run `sudo systemctl enable lecture_recorder`

5. Configure your firewall as needed.