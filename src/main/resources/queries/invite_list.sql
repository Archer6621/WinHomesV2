SELECT p2.name
FROM player p
  JOIN home h ON h.player_uuid = p.uuid
  JOIN invite i ON i.home_uuid = h.player_uuid
  JOIN player p2 ON p2.uuid = i.player_uuid
WHERE p.uuid=?;