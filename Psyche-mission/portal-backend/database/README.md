# Psyche Portal PostgreSQL Database

This directory contains the PostgreSQL setup files for the Psyche Portal backend.

The purpose of this setup is to provide a persistent PostgreSQL database that can be created consistently by each team member.

## Requirements

PostgreSQL is installed and running.

Verify PostgreSQL is installed:

    psql --version

## Database Setup

From the `portal-backend` directory, run:

    psql postgres -f database/setup.sql

The setup script will:

1. Create the `psyche_portal` database.
2. Connect to the `psyche_portal` database.
3. Create the required database tables.

        
        CREATE TABLE IF NOT EXISTS games (
                id VARCHAR(255) PRIMARY KEY,
                title VARCHAR(255),
                genre VARCHAR(255),
                difficulty VARCHAR(255),
                description TEXT,
                age VARCHAR(255),
                class_name VARCHAR(255),
                credits VARCHAR(255),
                gtype VARCHAR(255),
                engine VARCHAR(255),
                thumbnail TEXT,
                video TEXT,
                src TEXT
            );
            

## Connect to the Database

After running the setup script:

    psql psyche_portal

You should see a PostgreSQL prompt similar to:

    psyche_portal=#

## Verify the Database

Inside PostgreSQL, verify that you are connected to the correct database:

    SELECT current_database();

Expected result:

    psyche_portal

## Verify the Tables

List the tables:

    \dt

The `games` table should appear.

To view the structure of the `games` table:

    \d games

## Verify Persistence

Exit PostgreSQL:

    \q

Reconnect:

    psql psyche_portal

Then verify that the table still exists:

    \dt

The database and its tables remain available after disconnecting or restarting PostgreSQL because PostgreSQL stores the data persistently.

## Re-running the Setup

The `CREATE TABLE IF NOT EXISTS` statement prevents the `games` table from being recreated if it already exists.

However, the database itself only needs to be created once. If `psyche_portal` already exists, PostgreSQL may report that the database already exists.

## Current Scope

This setup only creates the PostgreSQL database and initial database schema.

Connecting the Spring Boot backend to PostgreSQL and saving application data through Spring Data JPA will be handled in separate tasks.
