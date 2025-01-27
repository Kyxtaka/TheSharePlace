-- Function and trigger for PERSONNE table
CREATE OR REPLACE FUNCTION check_personne_exists() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM PERSONNE WHERE id = NEW.id) THEN
        RAISE EXCEPTION 'Personne with id % already exists', NEW.id;
    ELSIF EXISTS (SELECT 1 FROM PERSONNE WHERE nom = NEW.nom AND prenom = NEW.prenom) THEN
        RAISE EXCEPTION 'Personne with nom % and prenom % already exists', NEW.nom, NEW.prenom;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER PERSONNE_EXISTS
BEFORE INSERT ON PERSONNE
FOR EACH ROW
EXECUTE FUNCTION check_personne_exists();

-- Function and trigger for USERS table
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

-- Function and trigger for GROUPS table
CREATE OR REPLACE FUNCTION check_group_exists() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM GROUPS WHERE id = NEW.id) THEN
        RAISE EXCEPTION 'Group with id % already exists', NEW.id;
    ELSIF EXISTS (SELECT 1 FROM GROUPS WHERE unique_id = NEW.unique_id AND name = NEW.name) THEN
        RAISE EXCEPTION 'Group with unique_id % and name % already exists', NEW.unique_id, NEW.name;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER GROUP_EXISTS
BEFORE INSERT ON GROUPS
FOR EACH ROW
EXECUTE FUNCTION check_group_exists();

-- Function and trigger for PLATFORMS table
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