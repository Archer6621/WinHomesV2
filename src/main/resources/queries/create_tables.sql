-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2018-01-20 03:01:39.154

-- tables
-- Table: home
USE winhomes;

CREATE TABLE home (
    UUID varchar(36) NOT NULL,
    Name varchar(64) NOT NULL,
    X int NOT NULL,
    Y int NOT NULL,
    Z int NOT NULL,
    yaw int NOT NULL,
    pitch int NOT NULL,
    world varchar(64) NOT NULL,
    CONSTRAINT home_pk PRIMARY KEY (UUID)
);

-- Table: home_invite
CREATE TABLE home_invite (
    home_UUID varchar(36) NOT NULL,
    UUID varchar(36) NOT NULL,
    CONSTRAINT home_invite_pk PRIMARY KEY (home_UUID)
);

-- foreign keys
-- Reference: home_invite_home (table: home_invite)
ALTER TABLE home_invite ADD CONSTRAINT home_invite_home FOREIGN KEY home_invite_home (home_UUID)
    REFERENCES home (UUID);

-- End of file.

