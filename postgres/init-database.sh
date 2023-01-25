#!/bin/bash
set -e
printenv

psql -v ON_ERROR_STOP=1 --username $POSTGRES_USER --dbname $POSTGRES_DB <<-EOSQL
    CREATE USER docker;
    CREATE DATABASE my_project_development;
    GRANT ALL PRIVILEGES ON DATABASE my_project_development TO docker;
    CREATE DATABASE my_project_test;
    GRANT ALL PRIVILEGES ON DATABASE my_project_test TO docker;

    CREATE TABLE users (
        id integer NOT NULL,
        first_name varchar(40),
        last_name varchar(40),
        email varchar(40),
        age integer
    );

    INSERT INTO users(id, first_name, last_name, email, age) VALUES (1, 'first_name1', 'last_name1', 'mail1@mail.ru', 21);
    INSERT INTO users(id, first_name, last_name, email, age) VALUES (2, 'first_name2', 'last_name2', 'mail2@mail.ru', 22);
    INSERT INTO users(id, first_name, last_name, email, age) VALUES (3, 'first_name3', 'last_name3', 'mail3@mail.ru', 23);
    INSERT INTO users(id, first_name, last_name, email, age) VALUES (4, 'first_name4', 'last_name4', 'mail4@mail.ru', 24);
EOSQL