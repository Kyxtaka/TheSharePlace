CREATE USER appsuperuser WITH LOGIN PASSWORD *************;
CREATE USER application WITH LOGIN PASSWORD **************;
GRANT * ON DATABASE accountplace TO appsuperuser;
GRANT * ON DATABASE application TO appsuperuser;
CREATE DATABASE accountplace WITH OWNER appsuperuser;
