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
	private static final String MESSAGE_PLAYER_INVITED = "%s has been invited to your home!";
	private static final String MESSAGE_PLAYER_ONLINE_ONCE = "Player must have been online at least once to be invited!";
	private static final String MESSAGE_SOMETHING_WENT_WRONG = "Something went wrong, please let the admin know!";
	private static final String MESSAGE_INVITED_TO_HOME = "You've been invited to %s's home!";
	private WinHomes main;

	public CommandHomeInvite(WinHomes winhomes) {
		this.main = winhomes;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			try (Connection conn = main.getDataSource().getConnection()) {
				if (args.length > 0) {
					String otherPlayerName = args[0];
					String otherPlayerID = "";

					// Check if the player is actually in the database
					String queryGetPlayer = SQLTools.queryReader("get_player.sql");
					PreparedStatement preparedStmtGetPlayer = conn.prepareStatement(queryGetPlayer);
					preparedStmtGetPlayer.setString(1, otherPlayerName);
					ResultSet rs = preparedStmtGetPlayer.executeQuery();
					if (rs.next()) {
						otherPlayerID = rs.getString(1);
					} else {
						main.getLogger().log(Level.FINE, "Attempted to invite player " + otherPlayerName + ", but the player was not in the database.");
						player.sendMessage(MESSAGE_PLAYER_ONLINE_ONCE);
						return true;
					}
					rs.close();
					preparedStmtGetPlayer.close();

					// Invite player to home
					String queryInviteToHome = SQLTools.queryReader("invite_to_home.sql");
					PreparedStatement preparedStmtInviteToHome = conn.prepareStatement(queryInviteToHome);
					preparedStmtInviteToHome.setString(1, player.getUniqueId().toString());
					preparedStmtInviteToHome.setString(2, otherPlayerID);
					preparedStmtInviteToHome.setString(3, player.getUniqueId().toString());
					preparedStmtInviteToHome.setString(4, otherPlayerID);
					preparedStmtInviteToHome.execute();
					preparedStmtInviteToHome.close();

					main.getLogger().log(Level.FINE, "Player " + otherPlayerName + " has been invited to " + player.getName() + "'s home!");
					player.sendMessage(String.format(MESSAGE_PLAYER_INVITED, otherPlayerName));

					// Notify player of invite if he is online
					for (Player p : main.getServer().getOnlinePlayers()) {
						if (p.getName().equals(otherPlayerName)) {
							p.sendMessage(String.format(MESSAGE_INVITED_TO_HOME, player.getName()));
							break;
						}
					}
				}
			} catch (SQLException | IOException e) {
				main.commandError(label, args, commandSender.getName(), e);
				player.sendMessage(MESSAGE_SOMETHING_WENT_WRONG);
			}
		}
		return true;
	}
}
