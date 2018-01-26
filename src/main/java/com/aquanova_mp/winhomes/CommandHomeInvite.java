package com.aquanova_mp.winhomes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class CommandHomeInvite implements CommandExecutor {
	private static final String MESSAGE_PLAYER_INVITED = " has been invited to your home!";
	private static final String MESSAGE_PLAYER_OFFLINE = "Player must be online to be invited!";
	private static final String MESSAGE_SOMETHING_WENT_WRONG = "Something went wrong, please let the admin know!";
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
					for (Player p : main.getServer().getOnlinePlayers()) {
						if (p.getName().equals(otherPlayerName)) {
							otherPlayer = p;
							break;
						}
					}

					if (otherPlayer != null) {
						// Ensure both players are added to the database
						String queryAddPlayer = SQLTools.queryReader("add_player.sql");
						PreparedStatement preparedStmtAddPlayer = conn.prepareStatement(queryAddPlayer);
						preparedStmtAddPlayer.setString(1, player.getUniqueId().toString());
						preparedStmtAddPlayer.setString(2, player.getName());
						preparedStmtAddPlayer.setString(3, player.getUniqueId().toString());
						preparedStmtAddPlayer.setString(4, player.getName());
						preparedStmtAddPlayer.execute();
						preparedStmtAddPlayer.setString(1, otherPlayer.getUniqueId().toString());
						preparedStmtAddPlayer.setString(2, otherPlayer.getName());
						preparedStmtAddPlayer.setString(3, otherPlayer.getUniqueId().toString());
						preparedStmtAddPlayer.setString(4, otherPlayer.getName());
						preparedStmtAddPlayer.execute();
						preparedStmtAddPlayer.close();

						// Invite player to home
						String queryInviteToHome = SQLTools.queryReader("invite_to_home.sql");
						PreparedStatement preparedStmtInviteToHome = conn.prepareStatement(queryInviteToHome);
						preparedStmtInviteToHome.setString(1, player.getUniqueId().toString());
						preparedStmtInviteToHome.setString(2, otherPlayer.getUniqueId().toString());
						preparedStmtInviteToHome.setString(3, player.getUniqueId().toString());
						preparedStmtInviteToHome.setString(4, otherPlayer.getUniqueId().toString());
						preparedStmtInviteToHome.execute();
						preparedStmtInviteToHome.close();

						main.getLogger().log(Level.FINE, "Player " + otherPlayerName + " has been invited to " + player.getName() + "'s home!");
						player.sendMessage(otherPlayerName + MESSAGE_PLAYER_INVITED);
					} else {
						main.getLogger().log(Level.FINE, "Attempted to invite player " + otherPlayerName + ", but the player was offline.");
						player.sendMessage(MESSAGE_PLAYER_OFFLINE);
					}
				}
				conn.close();
			} catch (SQLException | IOException e) {
				main.commandError(label, args, commandSender.getName(), e);
				player.sendMessage(MESSAGE_SOMETHING_WENT_WRONG);
			}
		}
		return true;
	}
}
