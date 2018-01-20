package com.aquanova_mp.winhomes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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

public class CommandHome implements CommandExecutor {
	private WinHomes main;

	public CommandHome(WinHomes winhomes) {
		this.main = winhomes;
	}

	public World getBukkitWorld(String world){
		World w = null;

		for(int i = 0; i < Bukkit.getWorlds().size() ; i++) {
			if (Bukkit.getWorlds().get(i).getUID().toString().equals(world)) {
				w = Bukkit.getWorlds().get(i);
			}
		}

		return w;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;

			String playerID = player.getUniqueId().toString();

			try {
				String query = SQLTools.queryReader("get_home.sql");
				Connection conn = main.getDataSource().getConnection();
				PreparedStatement preparedStmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				preparedStmt.setString(1, playerID);
				ResultSet rs = preparedStmt.executeQuery();

				// A player can only have a single home
				if (rs.next()) {

					// Attempt to obtain the world if it exists
					String worldID = rs.getString(8);
					World world = getBukkitWorld(worldID);
					if (world != null) {
						player.getLocation().setWorld(world);

						// Retrieve location data and put player at home location
						Location loc = player.getLocation();
						loc.setX(rs.getDouble(3));
						loc.setY(rs.getDouble(4));
						loc.setZ(rs.getDouble(5));
						loc.setPitch((float)rs.getDouble(6));
						loc.setYaw((float)rs.getDouble(7));
						player.teleport(loc);
						main.getLogger().log(Level.INFO, "Teleported player " + player.getPlayerListName() + " to its home.");
						return true;

					} else {
						main.getLogger().log(Level.WARNING, "World with ID " + worldID + " does not exist!");
						return false;
					}

				} else {
					main.getLogger().log(Level.INFO, "No home found for player " + player.getName() + " (" + playerID + ")");
					return false;
				}


			} catch (SQLException | IOException e) {
				main.getLogger().log(Level.WARNING, "Error executing command " + label + " with args: " + Arrays.toString(args));
				e.printStackTrace();
			}

		}
		return false;
	}
}
