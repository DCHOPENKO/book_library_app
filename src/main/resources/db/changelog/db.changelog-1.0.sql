--liquibase formatted sql

--changeset dchopenko:1
CREATE TABLE IF NOT EXISTS author
(
    id bigserial PRIMARY KEY,
    firstname varchar(50) NOT NULL,
    lastname varchar(50) NOT NULL,
    CONSTRAINT UQ_firstname_lastname UNIQUE(firstname, lastname)
    );
--rollback DROP TABLE author

--changeset dchopenko:2
CREATE TABLE IF NOT EXISTS book
(
    id bigserial PRIMARY KEY,
    title varchar(255) NOT NULL UNIQUE,
    author_id bigserial NOT NULL REFERENCES author (id)
    );
--rollback DROP TABLE book