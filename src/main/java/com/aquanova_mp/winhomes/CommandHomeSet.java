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

				// Perform query first to check whether home exists
				String query = SQLTools.queryReader("get_home.sql");
				PreparedStatement preparedStmt1 = conn.prepareStatement(query);
				preparedStmt1.setString(1, playerID);
				ResultSet rs = preparedStmt1.executeQuery();

				if (rs.next()) {

					// If it does exist, it means we must carry out an update instead
					query = SQLTools.queryReader("update_home.sql");
					PreparedStatement preparedStmt2 = conn.prepareStatement(query);
					preparedStmt2.setDouble(1, x);
					preparedStmt2.setDouble(2, y);
					preparedStmt2.setDouble(3, z);
					preparedStmt2.setDouble(4, pitch);
					preparedStmt2.setDouble(5, yaw);
					preparedStmt2.setString(6, worldID);
					preparedStmt2.setString(7, playerID);

					// Execute statement
					preparedStmt2.execute();
					preparedStmt2.close();
					main.getLogger().log(Level.INFO, "Succesfully updated home for " + playerName + " (" + playerID+ ")");

				} else {
					// Otherwise we set a new home entry

					query = SQLTools.queryReader("insert_home.sql");
					PreparedStatement preparedStmt3 = conn.prepareStatement(query);

					// Set query parameters
					preparedStmt3.setString(1, playerID);
					preparedStmt3.setString(2, playerName);
					preparedStmt3.setDouble(3, x);
					preparedStmt3.setDouble(4, y);
					preparedStmt3.setDouble(5, z);
					preparedStmt3.setDouble(6, pitch);
					preparedStmt3.setDouble(7, yaw);
					preparedStmt3.setString(8, worldID);

					// Execute statement
					preparedStmt3.execute();
					preparedStmt3.close();
					main.getLogger().log(Level.INFO, "Succesfully set home for " + playerName + " (" + playerID+ ")");
				}
				preparedStmt1.close();
				rs.close();
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
