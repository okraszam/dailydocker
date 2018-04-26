#!/bin/bash
mvn clean package
rm -rf ./docker/volumes/application/webapps/dailydocker
rm -rf ./docker/volumes/application/webapps/dailydocker.war
cp ./target/dailydocker.war ./docker/volumes/application/webapps/dailydocker.war
echo "======================="
echo "Application redeployed!"
echo "======================="