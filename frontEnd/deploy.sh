#/bin/bash
docker-compose down
cd ./angular-app
tar -xvf angular-app.tar
cd ../
docker-compose build
docker-compose up -d
echo "--------------------------------------ANGULAR APP CONTAINER LOGS-------------------------------------------"
docker-compose logs angular
echo "--------------------------------------REST API CONTAINER LOGS----------------------------------------------"
docker-compose logs api
echo "sh deployed script executed"
echo "--------------------------------------DOCER'S CONTAINERS---------------------------------------------------"
docker container ls -a 
