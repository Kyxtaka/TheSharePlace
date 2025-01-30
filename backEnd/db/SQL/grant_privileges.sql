-- Create the role with login and password
CREATE ROLE TSPAPI WITH LOGIN PASSWORD 'dont_give_your_db_api_user_password';

-- Create the databse or run creation script
GRANT ALL PRIVILEGES ON DATABASE TSPDB TO TSPAPI;

-- Connect to the TSPDB database
\c tspdb

-- Grant all privileges on all tables (existing ones)
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO TSPAPI;

-- Grant all privileges on all sequences (needed for SERIAL columns)
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO TSPAPI;

-- Grant privileges on all functions (if needed)
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO TSPAPI;

-- Grant create and usage privileges on the public schema to user TSPAPI
GRANT USAGE ON SCHEMA public TO TSPAPI;
GRANT CREATE ON SCHEMA public TO TSPAPI;

-- Alter default privileges to grant all on tables, sequences, and functions to the role
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO TSPAPI;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO TSPAPI;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO TSPAPI;
