INSERT INTO home (player_uuid, x, y, z, pitch, yaw, world) values (?,?,?,?,?,?,?)
ON DUPLICATE KEY UPDATE
  player_uuid=?, x=?, y=?, z=?, pitch=?, yaw=?, world=?;