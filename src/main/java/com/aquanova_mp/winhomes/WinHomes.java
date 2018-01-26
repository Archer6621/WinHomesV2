package com.aquanova_mp.winhomes;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * The main plugin class
 */
public class WinHomes extends JavaPlugin {
	private MysqlDataSource dataSource;
	private HashMap<Pair<Player, Command>, Long> coolDowns;


	public MysqlDataSource getDataSource() {
		return dataSource;
	}

	public HashMap<Pair<Player, Command>, Long> getCoolDowns() {return coolDowns;}


	public void commandError(String label, String[] args, String sender, Exception e) {
		getLogger().log(Level.WARNING, "Error executing command " + label + " with args: " + Arrays.toString(args) +" for " + sender);
		getLogger().log(Level.WARNING, e.getMessage());
	}

	public WinHomes() {
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
		Import.homeSpawnImport(this);
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
	}
}
