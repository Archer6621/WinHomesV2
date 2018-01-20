package com.aquanova_mp.winhomes;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * The main plugin class
 */
public class WinHomes extends JavaPlugin {

	private void initDB() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("potato");
		dataSource.setServerName("localhost");
		dataSource.setAllowMultiQueries(true);

		Connection conn;
		try {
			conn = dataSource.getConnection();
			
			// Check whether winhomes database already exists
			ResultSet resultSet = conn.getMetaData().getCatalogs();

			//iterate each catalog in the ResultSet
			boolean exists = false;
			while (resultSet.next()) {
				// Get the database name, which is at position 1
				String databaseName = resultSet.getString(1);
				getLogger().log(Level.INFO,databaseName);
				if (databaseName.equals("winhomes")) {
					exists = true;
				}
			}
			resultSet.close();
			
			
			
			Statement stmt = conn.createStatement();
			if (!exists) {
				stmt.executeUpdate("CREATE DATABASE winhomes;");
			}

			// Load the DB creation query
			String query = SQLTools.queryReader("create_tables.sql");

			ResultSet rs = stmt.executeQuery(query);
			getLogger().log(Level.INFO,"Tables created!");

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			getLogger().log(Level.WARNING, "Could not connect to DB!");
			getLogger().log(Level.WARNING, e.toString());
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "Could not open initialization query file!");
		}
	}


	@Override
	public void onEnable() {
		getLogger().log(Level.INFO,"Hello World!");
		initDB();
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO,"Bye World...");
	}
}
