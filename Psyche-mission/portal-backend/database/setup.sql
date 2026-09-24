-- Psyche Portal PostgreSQL database setup

CREATE DATABASE psyche_portal;

\connect psyche_portal

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