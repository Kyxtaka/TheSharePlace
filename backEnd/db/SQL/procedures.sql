/* All DB procedures will be stored there */
-- FUNCTION
-- TRIGGER
-- PROCEDURE

/**
* Trigger preventing the insertion of a personne wui the same nom and prenom 
*/
CREATE OR REPLACE TRIGGER PERSONNE_EXISTS BEFORE INSERT ON PERSONNE FOR EACH ROW 
DECLARE
    id int;
    nom varchar(255);
    prenom varchar(255);  
BEGIN
    SELECT * INTO nom, prenom FROM PERSONNE WHERE id = :NEW.id;
    IF SQL%FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'Personne with id ' || :NEW.id || ' already exists');
    ELSE
        SELECT * INTO nom, prenom FROM PERSONNE WHERE nom = :NEW.nom AND prenom = :NEW.prenom;
        IF SQL%FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Personne with nom ' || :NEW.nom || ' and prenom ' || :NEW.prenom || ' already exists');
        END IF;
    END IF;
END;

CREATE OR REPLACE TRIGGER USER_EXISTS BEFORE INSERT ON USERS FOR EACH ROW
DECLARE
    id int;
    username varchar(255);
    mail varchar(255);
BEGIN
    SELECT * INTO username, mail FROM USERS WHERE id = :NEW.id;
    IF SQL%FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'User with id ' || :NEW.id || ' already exists');
    ELSE
        SELECT * INTO username, mail FROM USERS WHERE username = :NEW.username AND mail = :NEW.mail;
        IF SQL%FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'User with username ' || :NEW.username || ' and mail ' || :NEW.mail || ' already exists');
        END IF;
    END IF;
END;


CREATE OR REPLACE TRIGGER GROUP_EXISTS BEFORE INSERT ON GROUPS FOR EACH ROW
DECLARE
    id int;
    unique_id BIGINT;
    name varchar(255);
BEGIN
    SELECT * INTO unique_id, name FROM GROUPS WHERE id = :NEW.id;
    IF SQL%FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'Group with id ' || :NEW.id || ' already exists');
    ELSE
        SELECT * INTO unique_id, name FROM GROUPS WHERE unique_id = :NEW.unique_id AND name = :NEW.name;
        IF SQL%FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Group with unique_id ' || :NEW.unique_id || ' and name ' || :NEW.name || ' already exists');
        END IF;
    END IF;
END;

CREATE OR REPLACE TRIGGER PLATFORM_EXISTS BEFORE INSERT ON PLATFORMS FOR EACH ROW
DECLARE
    id int;
    name varchar(255);
BEGIN
    SELECT * INTO name FROM PLATFORMS WHERE id = :NEW.id;
    IF SQL%FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'Platform with id ' || :NEW.id || ' already exists');
    ELSE
        SELECT * INTO name FROM PLATFORMS WHERE name = :NEW.name;
        IF SQL%FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Platform with name ' || :NEW.name || ' already exists');
        END IF;
    END IF;
END;

