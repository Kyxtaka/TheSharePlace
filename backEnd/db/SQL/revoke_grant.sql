-- Revoke privileges on the database
REVOKE ALL PRIVILEGES ON DATABASE tspdb FROM TSPAPI;

-- Revoke privileges on the schema
REVOKE ALL PRIVILEGES ON SCHEMA public FROM TSPAPI;

-- Revoke privileges on all tables in the schema
REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM TSPAPI;

-- Revoke privileges on all sequences in the schema
REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public FROM TSPAPI;

-- Revoke privileges on all functions in the schema
REVOKE ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public FROM TSPAPI;

-- Revoke usage and create privileges on the schema
REVOKE USAGE ON SCHEMA public FROM TSPAPI;
REVOKE CREATE ON SCHEMA public FROM TSPAPI;

-- Revoke default privileges
ALTER DEFAULT PRIVILEGES IN SCHEMA public REVOKE ALL ON TABLES FROM TSPAPI;
ALTER DEFAULT PRIVILEGES IN SCHEMA public REVOKE ALL ON SEQUENCES FROM TSPAPI;
ALTER DEFAULT PRIVILEGES IN SCHEMA public REVOKE ALL ON FUNCTIONS FROM TSPAPI;

-- Drop the role
DROP ROLE TSPAPI;