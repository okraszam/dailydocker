#!/bin/bash
docker exec -d application service cron restart
docker exec -d maintainer service cron restart
echo "======================="
echo "Cron launched!"
echo "Containers:"
echo "application"
echo "maintainer"
echo "======================="