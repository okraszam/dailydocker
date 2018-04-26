#!/bin/bash
echo "======================="
echo "Redeployment started!"
echo "======================="

mvn -q clean package
rm -rf ./docker/volumes/application/webapps/dailydocker
rm -rf ./docker/volumes/application/webapps/dailydocker.war
cp ./docker/application/package/dailydocker.war ./docker/volumes/application/webapps/dailydocker.war
echo "======================="
echo "Dailydocker redeployed!"
echo "======================="

rm -rf ./docker/volumes/application/webapps/webfilesys
rm -rf ./docker/volumes/application/webapps/webfilesys.war
cp ./docker/maintainer/package/webfilesys.war ./docker/volumes/maintainer/webapps/webfilesys.war
echo "======================="
echo "Webfilesys redeployed!"
echo "======================="