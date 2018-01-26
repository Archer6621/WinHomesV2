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

public class CommandHomeList implements CommandExecutor {
	private WinHomes main;
	private static final String MESSAGE_INVITED_TO_LIST = "Homes of players your are invited to:";
	private static final String MESSAGE_SOMETHING_WENT_WRONG = "Something went wrong, please let the admin know!";

	public CommandHomeList(WinHomes winhomes) {
		this.main = winhomes;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;

			try {
				Connection conn = main.getDataSource().getConnection();

				// Fetch invited UUIDs
				String queryCheckOtherHome = SQLTools.queryReader("invited_to_list.sql");
				PreparedStatement preparedStmtCheckHomeOther = conn.prepareStatement(queryCheckOtherHome);
				preparedStmtCheckHomeOther.setString(1, player.getUniqueId().toString());
				ResultSet rs = preparedStmtCheckHomeOther.executeQuery();

				// Iterate over UUID of players invited to the home of the other player
				StringBuilder str = new StringBuilder();
				while (rs.next()) {
					String name = rs.getString(1);
					str.append(name);
					str.append(", ");
				}
				str.delete(str.length()-2,str.length());
				main.getLogger().log(Level.FINE, "Printed invited-to list for " + player.getName() );
				player.sendMessage(MESSAGE_INVITED_TO_LIST);
				player.sendMessage(str.toString());
				return true;
			}
			catch (SQLException | IOException e) {
				main.commandError(label, args, commandSender.getName(), e);
				player.sendMessage(MESSAGE_SOMETHING_WENT_WRONG);
			}
		}
		return true;
	}
}
