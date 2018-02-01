package com.aquanova_mp.winhomes;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class Import {
	public static boolean homeSpawnImport(WinHomes main) {
		String homeSpawnPath = main.getDataFolder().getPath().replace(main.getName(), "") + "HomeSpawn" + File.separator + "PlayerData" + File.separator;
		File homeDir = new File(homeSpawnPath);

		File[] files = homeDir.listFiles();
		if (files == null) {
			main.getLogger().log(Level.WARNING, "Could not find any files in the HomeSpawn home folder!");
			return false;
		}
		int count = 0;
		try (Connection conn = main.getDataSource().getConnection()) {
			for (File file : files) {
				String playerID = file.getName().replace(".yml", "");
				YamlConfiguration config = new YamlConfiguration();


				config.load(file);
				if (config.get("Homes.Home") == null) {
					main.getLogger().log(Level.WARNING, "Skipping invalid home: " + config.saveToString().replace("\n", " "));
					continue;
				}

				Location home = (Location) config.get("Homes.Home");

				double x = home.getX();
				double y = home.getY();
				double z = home.getZ();
				double pitch = home.getPitch();
				double yaw = home.getYaw();
				String worldID = home.getWorld().getUID().toString();


				String queryAddPlayer = SQLTools.queryReader("add_player.sql");
				PreparedStatement preparedStmtAddPlayer = conn.prepareStatement(queryAddPlayer);
				preparedStmtAddPlayer.setString(1, playerID);
				preparedStmtAddPlayer.setString(2, null);
				preparedStmtAddPlayer.setString(3, playerID);
				preparedStmtAddPlayer.setString(4, null);
				preparedStmtAddPlayer.execute();
				preparedStmtAddPlayer.close();

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


				main.getLogger().log(Level.INFO, String.format("Importing home (%3d): %s, %f, %f, %f, %f, %f, %s", count, playerID, x, y, z, pitch, yaw, worldID));
				count += 1;

			}
		} catch (InvalidConfigurationException | SQLException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
