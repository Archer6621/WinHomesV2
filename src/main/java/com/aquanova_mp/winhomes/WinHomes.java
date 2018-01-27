package com.aquanova_mp.winhomes;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

/**
 * The main plugin class
 */
public class WinHomes extends JavaPlugin {
	private MysqlDataSource dataSource;
	private HashMap<Pair<UUID, Command>, Date> commandCoolDowns;


	public MysqlDataSource getDataSource() {
		return dataSource;
	}

	public HashMap<Pair<UUID, Command>, Date> getCommandCoolDowns() {return commandCoolDowns;}


	public void commandError(String label, String[] args, String sender, Exception e) {
		getLogger().log(Level.WARNING, "Error executing command " + label + " with args: " + Arrays.toString(args) +" for " + sender);
		getLogger().log(Level.WARNING, e.getMessage());
	}

	@Override
	public void onEnable() {
		// Initializations
		commandCoolDowns = new HashMap<>();
		dataSource = new MysqlDataSource();
		dataSource.setAllowMultiQueries(true);

		// Configuration setup
		FileConfiguration config = this.getConfig();
		config.addDefault("db_user", "root");
		config.addDefault("db_pw", "password");
		config.addDefault("db_server", "localhost");
		config.addDefault("perform_import", true);
		config.addDefault("home_warmup", 5);
		config.addDefault("set_home_cooldown", 300);
		config.options().copyDefaults(true);
		saveConfig();

		// Load config
		dataSource.setUser(config.getString("db_user"));
		dataSource.setPassword(config.getString("db_pw"));
		dataSource.setServerName(config.getString("db_server"));

		getLogger().log(Level.INFO,"Hello World!");
		SQLTools.initializeDataBase(this, dataSource);
		dataSource.setDatabaseName("winhomes");
		if (config.getBoolean("perform_import")) {
			Import.homeSpawnImport(this);
		}

		// Commands
		this.getCommand("home").setExecutor(new CommandHome(this));
		this.getCommand("home_set").setExecutor(new CommandHomeSet(this));
		this.getCommand("home_invite").setExecutor(new CommandHomeInvite(this));
		this.getCommand("home_uninvite").setExecutor(new CommandHomeUninvite(this));
		this.getCommand("home_list").setExecutor(new CommandHomeList(this));
		this.getCommand("home_ilist").setExecutor(new CommandHomeIList(this));
		this.getCommand("home_help").setExecutor(new CommandHomeHelp(this));
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO,"Bye World...");
		saveConfig();
	}
}
