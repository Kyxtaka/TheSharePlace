-- Truncate tables with foreign key dependencies last
TRUNCATE GROUPS_USERS CASCADE;
TRUNCATE ACCOUNTS CASCADE;
TRUNCATE USER_ROLES CASCADE;

-- Truncate tables without dependencies or which are referenced by other tables
TRUNCATE GROUPS CASCADE;
TRUNCATE PLATFORMS CASCADE;
TRUNCATE ROLES CASCADE;
TRUNCATE USERS CASCADE;