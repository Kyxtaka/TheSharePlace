-- creation tables compte utilisateur
CREATE TABLE USERS (
    id SERIAL PRIMARY KEY,
    password varchar(255),
    username varchar(255) UNIQUE NOT null,
    mail varchar(255) UNIQUE NOT NULL,
    firstname varchar(255),
    lastname varchar(255)
);

CREATE TABLE ROLES (
    id SERIAL PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL
);

CREATE TABLE USER_ROLES (
    user_id int references USERS (id),
    role_id int references ROLES (id),
    PRIMARY KEY (user_id, role_id)
);

-- creation groupe
CREATE TABLE GROUPS (
    id SERIAL PRIMARY KEY,
    unique_id BIGINT UNIQUE NOT NULL,
    name varchar(255) UNIQUE NOT null,
    password varchar(255),
    group_description varchar(255)
);

-- creation plateform
CREATE TABLE PLATFORMS (
    id SERIAL PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL,
    url varchar(255),
    imgRef varchar(255)
);

CREATE TABLE ACCOUNTS (
    id SERIAL PRIMARY KEY,
    username varchar(255),
    password varchar(255),
    mail varchar(255),
    A2F int not null constraint chk_A2F CHECK (A2F in (0,1)),
    platform_id int references PLATFORMS (id),
    group_id int references GROUPS (id)
);

CREATE TABLE GROUPS_USERS (
    group_id int references GROUPS (id),
    user_id int references USERS (id),
    PRIMARY KEY (group_id, user_id)
);