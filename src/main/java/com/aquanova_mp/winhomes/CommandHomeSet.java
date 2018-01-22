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
import java.util.Arrays;
import java.util.logging.Level;

public class CommandHomeSet implements CommandExecutor {
	private WinHomes main;
	private static final String MESSAGE_SOMETHING_WENT_WRONG = "Something went wrong, please let the admin know!";
	private static final String MESSAGE_HOME_SET = "Your home has been set to: ";


	public CommandHomeSet(WinHomes winhomes) {
		this.main = winhomes;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof Player){
			Player player = (Player) commandSender;

			// Get player properties required to set the home
			String playerID = player.getUniqueId().toString();
			String playerName = player.getName();
			double x = player.getLocation().getX();
			double y = player.getLocation().getY();
			double z = player.getLocation().getZ();
			double pitch = player.getLocation().getPitch();
			double yaw = player.getLocation().getYaw();
			String worldID = player.getWorld().getUID().toString();

			// Prepare and execute SQL statements
			try {
				Connection conn = main.getDataSource().getConnection();

				// Add the player to the database
				String queryAddPlayer = SQLTools.queryReader("add_player.sql");
				PreparedStatement preparedStatementAddPlayer = conn.prepareStatement(queryAddPlayer);

				preparedStatementAddPlayer.setString(1, playerID);
				preparedStatementAddPlayer.setString(2, playerName);
				preparedStatementAddPlayer.setString(3, playerID);
				preparedStatementAddPlayer.setString(4, playerName);

				preparedStatementAddPlayer.execute();
				preparedStatementAddPlayer.close();


				// Add home to the database
				String querySetHome = SQLTools.queryReader("set_home.sql");
				PreparedStatement preparedStatementSetHome = conn.prepareStatement(querySetHome);

				preparedStatementSetHome.setString(1, playerID);
				preparedStatementSetHome.setDouble(2, x);
				preparedStatementSetHome.setDouble(3, y);
				preparedStatementSetHome.setDouble(4, z);
				preparedStatementSetHome.setDouble(5, pitch);
				preparedStatementSetHome.setDouble(6, yaw);
				preparedStatementSetHome.setString(7, worldID);

				preparedStatementSetHome.setString(8, playerID);
				preparedStatementSetHome.setDouble(9, x);
				preparedStatementSetHome.setDouble(10, y);
				preparedStatementSetHome.setDouble(11, z);
				preparedStatementSetHome.setDouble(12, pitch);
				preparedStatementSetHome.setDouble(13, yaw);
				preparedStatementSetHome.setString(14, worldID);

				preparedStatementSetHome.execute();
				preparedStatementSetHome.close();


				main.getLogger().log(Level.INFO, "Succesfully updated home for " + playerName + " (" + playerID+ ")");
				player.sendMessage(MESSAGE_HOME_SET + "X:"+x + ", Y:"+y + ", Z:"+z);
				conn.close();
				return true;
			} catch (IOException | SQLException e) {
				main.commandError(label, args, player.getName(), e);
				player.sendMessage(MESSAGE_SOMETHING_WENT_WRONG);
			}
		}

		return true;
	}
}
