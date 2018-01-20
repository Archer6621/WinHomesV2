package com.aquanova_mp.winhomes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class CommandHomeInvite implements CommandExecutor {
	private WinHomes main;

	public CommandHomeInvite(WinHomes winhomes) {
		this.main = winhomes;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			try {
				Connection conn = main.getDataSource().getConnection();
				if (args.length > 0) {
					String otherPlayerName = args[0];

					// Check if the player is online
					Player otherPlayer = null;
					for(Player p : main.getServer().getOnlinePlayers()) {
						if (p.getName().equals(otherPlayerName)) {
							otherPlayer = p;
							break;
						}
					}

					if (otherPlayer != null) {
						// Invite player to home
						String query = SQLTools.queryReader("invite_to_home.sql");
						PreparedStatement preparedStmt = conn.prepareStatement(query);
						preparedStmt.setString(1, player.getUniqueId().toString());
						preparedStmt.setString(2, otherPlayer.getUniqueId().toString());

						// Execute
						preparedStmt.execute();
						preparedStmt.close();
						main.getLogger().log(Level.INFO, "Player " + otherPlayerName + " has been invited to " + player.getName()+ "'s home!");
					} else {
						main.getLogger().log(Level.INFO, "Player " + otherPlayerName + " must be online to be invited!");
					}
				}
				conn.close();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
