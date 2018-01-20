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

			// Prepare SQL statement
			try {
				Connection conn = main.getDataSource().getConnection();

				String query = SQLTools.queryReader("set_home.sql");
				PreparedStatement preparedStmt = conn.prepareStatement(query);

				// Set query parameters
				preparedStmt.setString(1, playerID);
				preparedStmt.setString(2, playerName);
				preparedStmt.setDouble(3, x);
				preparedStmt.setDouble(4, y);
				preparedStmt.setDouble(5, z);
				preparedStmt.setDouble(6, pitch);
				preparedStmt.setDouble(7, yaw);
				preparedStmt.setString(8, worldID);

				// Execute statement
				preparedStmt.execute();
				preparedStmt.close();
				main.getLogger().log(Level.INFO, "Succesfully updated home for " + playerName + " (" + playerID+ ")");
				conn.close();
				return true;
			} catch (IOException | SQLException e) {
				main.getLogger().log(Level.WARNING, "Error executing command " + label + " with args: " + Arrays.toString(args));
				e.printStackTrace();
			}
		}

		return false;
	}
}
