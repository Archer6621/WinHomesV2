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

					main.getLogger().log(Level.INFO, "Player " + otherPlayerName + " has been uninvited from " + player.getName()+ "'s home!");
				}
				conn.close();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
