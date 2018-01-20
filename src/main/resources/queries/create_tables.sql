-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2018-01-20 15:12:41.906

USE winhomes;

-- tables
-- Table: home
CREATE TABLE home (
    uuid varchar(36) NOT NULL,
    name varchar(64) NOT NULL,
    x double(12,4) NOT NULL,
    y double(12,4) NOT NULL,
    z double(12,4) NOT NULL,
    pitch double(12,4) NOT NULL,
    yaw double(12,4) NOT NULL,
    world varchar(36) NOT NULL,
    CONSTRAINT home_pk PRIMARY KEY (uuid)
);

-- Table: home_invite
CREATE TABLE home_invite (
    home_uuid varchar(36) NOT NULL,
    uuid varchar(36) NOT NULL,
    CONSTRAINT home_invite_pk PRIMARY KEY (home_uuid)
);

-- foreign keys
-- Reference: home_invite_home (table: home_invite)
ALTER TABLE home_invite ADD CONSTRAINT home_invite_home FOREIGN KEY home_invite_home (home_uuid)
REFERENCES home (uuid);

-- End of file.




