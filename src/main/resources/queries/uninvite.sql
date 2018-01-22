DELETE invite FROM invite
  JOIN player ON invite.player_uuid=player.uuid
WHERE invite.home_uuid=? and name=?;