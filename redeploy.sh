#!/bin/bash
mvn clean package
echo "=> Redeploying application..."
rm -rf ./docker/volumes/application/webapps/dailydocker
rm -rf ./docker/volumes/application/webapps/dailydocker.war
mv ./target/dailydocker-0.1.war ./target/dailydocker.war
cp ./target/dailydocker.war ./docker/volumes/application/webapps/dailydocker.war
echo "=> Application redeployed."