INSERT INTO player (uuid, name) values (?, ?)
ON DUPLICATE KEY UPDATE
  uuid=?, name=?;