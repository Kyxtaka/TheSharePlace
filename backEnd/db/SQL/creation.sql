
------------------ PARTIE COMPTE UTILISATEUR --------------------------------
-- create users details
CREATE TABLE USERS (
    id SERIAL PRIMARY KEY,
    password VARCHAR(255),
    nom VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) UNIQUE NOT NULL,
    mail VARCHAR(255) UNIQUE NOT NULL
);

-- create roles in the application 
CREATE TABLE ROLES (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- create user and roles mapping
CREATE TABLE USER_ROLES (
    user_id INT REFERENCES USERS (id),
    role_id INT REFERENCES ROLES (id),
    PRIMARY KEY (user_id, role_id)
);

------------------------ PARTIE GROUPE DE PARTAGE DE COMPTE ---------------------
-- create groups that will contain shared accounts
CREATE TABLE GROUPS (
    id SERIAL PRIMARY KEY,
    uid BIGINT UNIQUE NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    group_description VARCHAR(255)
);

-- Creation de la relation de role pour les group et utilisateurs
CREATE TABLE GROUPS_USERS (
    group_id INT REFERENCES GROUPS (id),
    user_id INT REFERENCES USERS (id),
    role_id INT REFERENCES ROLES (id),
    PRIMARY KEY (group_id, user_id, role_id)
);

------------------------ PARTIE COMPTE PARTAGE --------------------------------
-- create platform, plateforme of the shared accounts
CREATE TABLE PLATFORMS (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    url VARCHAR(255),
    imgRef VARCHAR(255)
);

-- create table account who will be shared with other people 
CREATE TABLE ACCOUNTS (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    mail VARCHAR(255),
    A2F INT NOT NULL CONSTRAINT chk_A2F CHECK (A2F IN (0,1)),
    platform_id INT REFERENCES PLATFORMS (id),
    group_id INT REFERENCES GROUPS (id)
);