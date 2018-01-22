SELECT p.name
FROM player p
  JOIN invite i ON i.home_uuid = p.uuid
WHERE i.player_uuid=?;