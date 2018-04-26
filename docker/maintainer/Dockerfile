FROM tomcat:latest
RUN apt-get update
RUN apt-get -y install cron
#RUN apt-get -y install rsync grsync
RUN mkdir /usr/local/tomcat/cron
RUN mkdir /usr/local/tomcat/app_backup
RUN mkdir /usr/local/tomcat/db_backup
COPY cron/mnt_backup /usr/local/tomcat/cron
COPY cron/mnt_backup.sh /usr/local/tomcat/cron
RUN crontab /usr/local/tomcat/cron/mnt_backup
RUN chmod 0644 /usr/local/tomcat/cron/mnt_backup
RUN service cron restart