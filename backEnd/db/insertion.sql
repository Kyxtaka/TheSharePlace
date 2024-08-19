-- Insertion compte utilisateur et admin
INSERT INTO USERS (password, username, mail, firstname, lastname) VALUES 
    ('test', 'user1', 'akiizsan@gmail.com', 'Nathan', 'Randriantsoa'),
    ('test', 'admin1', 'nathangamersession@gmail.com', 'Nathan', 'Randriantsoa');

-- Insertion ROLES
INSERT INTO ROLES (name) VALUES 
    ('ADMIN'),
    ('USER');

-- Insertion USER-> ROLE
INSERT INTO USER_ROLES (user_id, role_id) VALUES
    (1, 2),  -- Assuming the auto-incremented user_id is 1 for 'user1'
    (2, 1);  -- Assuming the auto-incremented user_id is 2 for 'admin1'

-- Insertion Groupe
INSERT INTO GROUPS (unique_id, name, password, group_description) VALUES
    (0000000001, 'groupe1', 'groupe1', 'group de test'),
    (0000000002, 'groupe2', 'groupe2', 'group de test');

-- Insertion Plateforme
INSERT INTO PLATFORMS (name, url, imgRef) VALUES 
    ('Riot Games', 'https://www.riotgames.com/fr', 'riotgames.png'),
    ('steams', 'https://store.steampowered.com/', 'steam.png');

-- Insertion ACCOUNTS
INSERT INTO ACCOUNTS (username, password, mail, A2F, platform_id, group_id) VALUES
    ('xxxxxxxx', 'xxxxxxxxx', 'akiizsan@gmail.com', 1, 1, 1), -- Assuming auto-incremented ids are 1 for platform_id and group_id
    ('xxxxxxxx', 'xxxxxxxxx', 'akiizsan0@gmail.com', 1, 1, 1);

-- Insertion GROUPS_USERS
INSERT INTO GROUPS_USERS (group_id, user_id) VALUES
    (1, 1),  -- Assuming auto-incremented group_id and user_id are both 1
    (1, 2);  -- Assuming auto-incremented group_id is 1 and user_id is 2