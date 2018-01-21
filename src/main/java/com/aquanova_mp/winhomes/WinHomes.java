package com.aquanova_mp.winhomes;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

/**
 * The main plugin class
 */
public class WinHomes extends JavaPlugin {
	private MysqlDataSource dataSource;

	public MysqlDataSource getDataSource() {
		return dataSource;
	}

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
		dataSource.setDatabaseName("winhomes");
		this.getCommand("home").setExecutor(new CommandHome(this));
		this.getCommand("home_set").setExecutor(new CommandHomeSet(this));
		this.getCommand("home_invite").setExecutor(new CommandHomeInvite(this));
		this.getCommand("home_uninvite").setExecutor(new CommandHomeUninvite(this));
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO,"Bye World...");
	}
}
