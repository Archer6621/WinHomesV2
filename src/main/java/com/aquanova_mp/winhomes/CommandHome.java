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

	public World getBukkitWorld(String world) {
		World w = null;

		for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
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

			try {
				Connection conn = main.getDataSource().getConnection();
				if (args.length > 0) {
					String otherPlayerName = args[0];

					// First perform a query to see whether this player is actually invited to the other's home
					String queryCheckOtherHome = SQLTools.queryReader("check_home_other.sql");
					PreparedStatement preparedStmtCheckHomeOther = conn.prepareStatement(queryCheckOtherHome);
					preparedStmtCheckHomeOther.setString(1, otherPlayerName);
					ResultSet rs1 = preparedStmtCheckHomeOther.executeQuery();
					// Iterate over UUID of players invited to the home of the other player
					boolean invited = false;
					while (rs1.next()) {
						String uuid = rs1.getString(1);
						if (uuid.equals(player.getUniqueId().toString())) {
							invited = true;
							break;
						}
					}

					// If invited, teleport to that player's home
					if (invited) {
						String queryGetHomeOther = SQLTools.queryReader("get_home_other.sql");
						PreparedStatement preparedStmtGetHomeOther = conn.prepareStatement(queryGetHomeOther);
						preparedStmtGetHomeOther.setString(1, otherPlayerName);
						ResultSet rs = preparedStmtGetHomeOther.executeQuery();
						return teleportPlayer(player, rs);
					} else {
						main.getLogger().log(Level.INFO, "Player " + player.getName() + " is not invited to " +otherPlayerName + "'s home");
					}
				} else {
					String queryGetHome = SQLTools.queryReader("get_home.sql");
					PreparedStatement preparedStmtGetHome = conn.prepareStatement(queryGetHome);
					preparedStmtGetHome.setString(1, player.getUniqueId().toString());
					ResultSet rs = preparedStmtGetHome.executeQuery();
					return teleportPlayer(player, rs);
				}
			} catch (SQLException | IOException e) {
				main.getLogger().log(Level.WARNING, "Error executing command " + label + " with args: " + Arrays.toString(args));
				e.printStackTrace();
			}
		}
		return false;
	}

	private Boolean teleportPlayer(Player player, ResultSet rs) throws SQLException {
		if (rs.next()) {
			// Attempt to obtain the world if it exists
			String worldID = rs.getString(1);
			World world = getBukkitWorld(worldID);
			if (world != null) {
				player.getLocation().setWorld(world);

				// Retrieve location data and put player at home location
				Location loc = player.getLocation();
				loc.setX(rs.getDouble(2));
				loc.setY(rs.getDouble(3));
				loc.setZ(rs.getDouble(4));
				loc.setPitch((float) rs.getDouble(5));
				loc.setYaw((float) rs.getDouble(6));
				player.teleport(loc);
				main.getLogger().log(Level.INFO, "Teleported player " + player.getPlayerListName() + " to its home.");
				return true;
			} else {
				main.getLogger().log(Level.WARNING, "World with ID " + worldID + " does not exist!");
				return false;
			}

		} else {
			main.getLogger().log(Level.INFO, "No home found for player " + player.getName() + " (" + player.getUniqueId().toString() + ")");
			return false;
		}
	}
}
