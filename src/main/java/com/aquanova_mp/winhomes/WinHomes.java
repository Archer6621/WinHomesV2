package com.aquanova_mp.winhomes;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
		config.addDefault("db_name", "name_of_db");
		config.addDefault("db_prefix", "prefix");
		config.addDefault("db_user", "root");
		config.addDefault("db_pw", "password");
		config.addDefault("db_server", "localhost");
		config.addDefault("db_is_initialized", false);
		config.addDefault("perform_import", true);
		config.addDefault("home_warmup", 5);
		config.addDefault("set_home_cooldown", 300);
		config.addDefault("warmup_movement_threshold", 5);
		config.options().copyDefaults(true);
		saveConfig();

		// Load config
		dataSource.setDatabaseName(config.getString("db_name"));
		dataSource.setUser(config.getString("db_user"));
		dataSource.setPassword(config.getString("db_pw"));
		dataSource.setServerName(config.getString("db_server"));

		getLogger().log(Level.INFO,"Hello World!");
		SQLTools.setPrefix(config.getString("db_prefix"));
		// TODO: Something here is broken, it will somehow still try to initialize even though the value is_initialized is true
		if (!config.getBoolean("db_is_initialized")) {
			config.set("db_is_initialized", SQLTools.initializeDataBase(this));
		}
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

		// Add player join listener for adding them to the db
		{
			WinHomes plugin = this;
			Bukkit.getPluginManager().registerEvents(new Listener() {
				@EventHandler
				public void onPlayerJoin(PlayerJoinEvent event) {
					Player player = event.getPlayer();
					try (Connection conn = plugin.getDataSource().getConnection()) {
						String queryAddPlayer = SQLTools.queryReader("add_player.sql");
						PreparedStatement preparedStmtAddPlayer = conn.prepareStatement(queryAddPlayer);
						preparedStmtAddPlayer.setString(1, player.getUniqueId().toString());
						preparedStmtAddPlayer.setString(2, player.getName());
						preparedStmtAddPlayer.setString(3, player.getUniqueId().toString());
						preparedStmtAddPlayer.setString(4, player.getName());
						preparedStmtAddPlayer.execute();
						preparedStmtAddPlayer.close();
						plugin.getLogger().log(Level.INFO, "Player added to database: " + player.getName() + ":" + player.getUniqueId().toString());
					} catch (SQLException | IOException e) {
						plugin.getLogger().log(Level.WARNING, e.getMessage());
					}

					// Update AutoComplete
					PlayerListTabCompleter pltc = new PlayerListTabCompleter(plugin);
					plugin.getCommand("home").setTabCompleter(pltc);
					plugin.getCommand("home_invite").setTabCompleter(pltc);
					plugin.getCommand("home_uninvite").setTabCompleter(pltc);
				}
			}, plugin);
		}
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO,"Bye World...");
		saveConfig();
	}
}
