-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2018-01-21 01:29:46.441

USE winhomes;

-- tables
-- Table: home
CREATE TABLE home (
    player_uuid varchar(36) NOT NULL,
    x double(12,4) NOT NULL,
    y double(12,4) NOT NULL,
    z double(12,4) NOT NULL,
    pitch double(12,4) NOT NULL,
    yaw double(12,4) NOT NULL,
    world varchar(36) NOT NULL,
    CONSTRAINT home_pk PRIMARY KEY (player_uuid)
);

-- Table: invite
CREATE TABLE invite (
    home_uuid varchar(36) NOT NULL,
    player_uuid varchar(36) NOT NULL,
    CONSTRAINT invite_pk PRIMARY KEY (home_uuid,player_uuid)
);

-- Table: player
CREATE TABLE player (
    uuid varchar(36) NOT NULL,
    name varchar(64) NOT NULL,
    CONSTRAINT player_pk PRIMARY KEY (uuid)
);

-- foreign keys
-- Reference: Table_4_player (table: home)
ALTER TABLE home ADD CONSTRAINT Table_4_player FOREIGN KEY Table_4_player (player_uuid)
REFERENCES player (uuid)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- Reference: invite_home (table: invite)
ALTER TABLE invite ADD CONSTRAINT invite_home FOREIGN KEY invite_home (home_uuid)
REFERENCES home (player_uuid)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- Reference: invite_player (table: invite)
ALTER TABLE invite ADD CONSTRAINT invite_player FOREIGN KEY invite_player (player_uuid)
REFERENCES player (uuid)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- End of file.

