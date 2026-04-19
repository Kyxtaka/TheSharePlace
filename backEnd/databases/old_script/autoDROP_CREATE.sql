-- Note: This script is used to clean and create the database again and all precedures and triggers
-- This script has to be executed with postgres user in the psql console or include it to the command line
-- some parts of this are genereated by the GPT tool
-- Check if database is already created
DO $$  -- Function generateed by GPT to end the session before dropping the database
BEGIN
    IF EXISTS (SELECT 1 FROM pg_database WHERE datname = 'TSPDB') THEN
        
        -- Disconnect all active session to the database before dropping it
        PERFORM pg_terminate_backend(pg_stat_activity.pid)
        FROM pg_stat_activity
        WHERE pg_stat_activity.datname = 'TSPDB';

        
    END IF;
END $$;

-- Drop and craete phase
DROP DATABASE "TSPDB";
CREATE DATABASE "TSPDB";
\c TSPDB
\i creation.sql
\i procedures.sql
\i show.sql

