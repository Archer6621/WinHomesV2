package com.aquanova_mp.winhomes;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

/**
 * The main plugin class
 */
public class WinHomes extends JavaPlugin {
	private MysqlDataSource dataSource;

	public WinHomes() {
		// TODO: Add YAML configuration
		dataSource = new MysqlDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("potato");
		dataSource.setServerName("localhost");
		dataSource.setAllowMultiQueries(true);
	}

	@Override
	public void onEnable() {
		getLogger().log(Level.INFO,"Hello World!");
		SQLTools.initializeDataBase(this, dataSource);
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO,"Bye World...");
	}
}
