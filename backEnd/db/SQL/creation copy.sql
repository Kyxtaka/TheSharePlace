-- create users details
CREATE TABLE USERS (
    id SERIAL PRIMARY KEY,
    password varchar(255),
    username varchar(255) UNIQUE NOT null,
    mail varchar(255) UNIQUE NOT NULL,
    firstname varchar(255),
    lastname varchar(255)
);

-- create roles in the application 
CREATE TABLE ROLES (
    id SERIAL PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL
);

-- create user and roles mapping
CREATE TABLE USER_ROLES (
    user_id int references USERS (id),
    role_id int references ROLES (id),
    PRIMARY KEY (user_id, role_id)
);

-- create groups that will contains shared accounts
CREATE TABLE GROUPS (
    id SERIAL PRIMARY KEY,
    unique_id BIGINT UNIQUE NOT NULL,
    name varchar(255) UNIQUE NOT null,
    password varchar(255),
    group_description varchar(255)
);

-- create platefor, plateforme of the shared accounts
CREATE TABLE PLATFORMS (
    id SERIAL PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL,
    url varchar(255),
    imgRef varchar(255)
);

-- create table account who will be shared with other people 
CREATE TABLE ACCOUNTS (
    id SERIAL PRIMARY KEY,
    username varchar(255),
    password varchar(255),
    mail varchar(255),
    A2F int not null constraint chk_A2F CHECK (A2F in (0,1)),
    platform_id int references PLATFORMS (id),
    group_id int references GROUPS (id)
);

-- Creation de la relation de role pour les group et utilisateurs
CREATE TABLE GROUPS_USERS (
    group_id int references GROUPS (id),
    user_id int references USERS (id),
    role_id int references ROLES (id),
    PRIMARY KEY (group_id, user_id, role_id)
);