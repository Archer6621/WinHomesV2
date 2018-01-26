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

public class CommandHomeUninvite implements CommandExecutor {
	private WinHomes main;
	private static final String MESSAGE_PLAYER_UNINVITED = " has been uninvited from your home!";
	private static final String MESSAGE_SOMETHING_WENT_WRONG = "Something went wrong, please let the admin know!";


	public CommandHomeUninvite(WinHomes winhomes) {
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

					// Uninvite player
					String queryUninvite = SQLTools.queryReader("uninvite.sql");
					PreparedStatement preparedStmtUninvite = conn.prepareStatement(queryUninvite);
					preparedStmtUninvite.setString(1, player.getUniqueId().toString());
					preparedStmtUninvite.setString(2, otherPlayerName);
					preparedStmtUninvite.execute();
					preparedStmtUninvite.close();

					main.getLogger().log(Level.FINE, "Player " + otherPlayerName + " has been uninvited from " + player.getName()+ "'s home!");
					player.sendMessage(otherPlayerName + MESSAGE_PLAYER_UNINVITED);
					return true;
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
