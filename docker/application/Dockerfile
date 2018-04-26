FROM tomcat:latest
RUN apt-get update
RUN apt-get -y install cron
RUN mkdir /usr/local/tomcat/cron
RUN mkdir /usr/local/tomcat/content
COPY cron/scores_backup /usr/local/tomcat/cron
COPY cron/scores_backup.sh /usr/local/tomcat/cron
RUN crontab /usr/local/tomcat/cron/scores_backup
RUN chmod 0644 /usr/local/tomcat/cron/scores_backup