FROM tomcat:latest
RUN apt-get update
RUN apt-get -y install cron
RUN apt-get -y install rsync grsync
RUN apt-get -y install nano
RUN mkdir /usr/local/tomcat/cron
RUN mkdir /usr/local/tomcat/scores
RUN mkdir /usr/local/tomcat/logs_backup
RUN mkdir /usr/local/tomcat/config_backup
#COPY package/dailydocker.war /usr/local/tomcat/webapps
COPY cron/logs_backup /usr/local/tomcat/cron
COPY cron/config_backup /usr/local/tomcat/cron
RUN crontab -u root /usr/local/tomcat/cron/logs_backup
RUN crontab -u root /usr/local/tomcat/cron/config_backup