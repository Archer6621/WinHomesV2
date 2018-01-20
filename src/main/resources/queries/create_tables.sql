-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2018-01-20 15:12:41.906

-- tables
-- Table: home
USE winhomes;

CREATE TABLE home (
    UUID varchar(36) NOT NULL,
    Name varchar(64) NOT NULL,
    X double(12,4) NOT NULL,
    Y double(12,4) NOT NULL,
    Z double(12,4) NOT NULL,
    yaw double(12,4) NOT NULL,
    pitch double(12,4) NOT NULL,
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


