CREATE DATABASE itransition_db;

create user 'itransition'@localhost
        identified by 'itransition';

GRANT ALL
    ON itransition_db.*
    TO 'itransition'@localhost;

