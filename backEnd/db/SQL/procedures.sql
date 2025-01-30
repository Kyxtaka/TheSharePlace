---------------------------------------------------------- Function and trigger for USERS table------------------------------------------------------------------------
/**
*  This is a trigger function that will be called before inserting a new user in the USERS table
*  This function dont get any parameter, it will check if the user already exists in the USERS table
*  Function to check if a user already exists in the USERS table
*  @param check_username: the username to check
*  @param check_mail: the mail to check
*/
CREATE OR REPLACE FUNCTION check_user_exists() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM USERS WHERE id = NEW.id) THEN
        RAISE EXCEPTION 'User with id % already exists', NEW.id;
    ELSIF EXISTS (SELECT 1 FROM USERS WHERE username = NEW.username AND mail = NEW.mail) THEN
        RAISE EXCEPTION 'User with username % and mail % already exists', NEW.username, NEW.mail;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER USER_EXISTS
BEFORE INSERT ON USERS
FOR EACH ROW
EXECUTE FUNCTION check_user_exists();

----------------------------------------------------------- Function and trigger for GROUPS table ------------------------------------------------------------------------
/**
*  This is a trigger function that will be called before inserting a new group in the GROUPS table 
*  This function dont get any parameter, it will check if the group already exists in the GROUPS table
*  Function to check if a group already exists in the GROUPS table
*  @param check_id: the id to check
*  @param check_uid: the uid to check
*/
CREATE OR REPLACE FUNCTION check_group_exists() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM GROUPS WHERE id = NEW.id) THEN
        RAISE EXCEPTION 'Group with id % already exists', NEW.id;
    ELSIF EXISTS (SELECT 1 FROM GROUPS WHERE uid = NEW.uid ) THEN
        RAISE EXCEPTION 'Group with unique_id % already exists', NEW.unique_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER GROUP_EXISTS
BEFORE INSERT ON GROUPS
FOR EACH ROW
EXECUTE FUNCTION check_group_exists();

----------------------------------------------------- Function generate new uid (random 10 lenght number) ------------------------------------------------------
/**
*   Function to check if a uid already exists in the GROUPS table
*   @param check_uid: the uid to check
*   @return: true if the uid exists, false otherwise  ==> value id "f" for false and "t" for true
*/
CREATE OR REPLACE FUNCTION check_uid_exists(check_uid BIGINT) RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (SELECT 1 FROM GROUPS WHERE GROUPS.uid = check_uid);
END;
$$ LANGUAGE plpgsql;


/**
*   Function to generate a new uid for a new group
*   @return: a new uid for a new group
*/
CREATE OR REPLACE FUNCTION generate_uid() RETURNS BIGINT AS $$
DECLARE
    randomUID BIGINT;
BEGIN
    LOOP
        SELECT floor(random() * 10000000000)::BIGINT INTO randomUID;
        EXIT WHEN NOT EXISTS (SELECT 1 FROM GROUPS WHERE GROUPS.uid = randomUID);
    END LOOP;
    RETURN randomUID;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------ Function and trigger for PLATFORMS table  ------------------------------------------------------------------------
/**
*  This is a trigger function that will be called before inserting a new platform in the PLATFORMS table
*  This function dont get any parameter, it will check if the platform already exists in the PLATFORMS table
*  Function to check if a platform already exists in the PLATFORMS table
*  @param check_id: the id to check
*  @param check_name: the name to check
*/
CREATE OR REPLACE FUNCTION check_platform_exists() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM PLATFORMS WHERE id = NEW.id) THEN
        RAISE EXCEPTION 'Platform with id % already exists', NEW.id;
    ELSIF EXISTS (SELECT 1 FROM PLATFORMS WHERE name = NEW.name) THEN
        RAISE EXCEPTION 'Platform with name % already exists', NEW.name;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER PLATFORM_EXISTS
BEFORE INSERT ON PLATFORMS
FOR EACH ROW
EXECUTE FUNCTION check_platform_exists();