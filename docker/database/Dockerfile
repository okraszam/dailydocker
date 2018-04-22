FROM mysql:latest
RUN apt-get update
RUN apt-get -y install cron
RUN apt-get -y install rsync grsync
RUN mkdir cron
RUN mkdir logs_backup
RUN mkdir config_backup
RUN mkdir database_backup
#COPY cron/logs_backup /cron
#COPY cron/config_backup /cron
#COPY cron/database_backup /cron
#RUN crontab -u root /cron/logs_backup
#RUN crontab -u root /cron/config_backup
#RUN crontab -u root /cron/database_backup