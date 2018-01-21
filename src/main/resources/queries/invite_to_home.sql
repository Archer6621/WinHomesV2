INSERT INTO invite (home_uuid, player_uuid) values (?, ?)
ON DUPLICATE KEY UPDATE
  home_uuid=?,player_uuid=?;