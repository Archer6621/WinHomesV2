package com.aquanova_mp.winhomes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Map;

public class CommandHomeHelp implements CommandExecutor {
	private WinHomes main;

	public CommandHomeHelp(WinHomes winhomes) {
		this.main = winhomes;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			player.sendMessage("List of available commmands:");

			Map<String, Map<String, Object>> config = main.getDescription().getCommands();
			for (String commandName : config.keySet()) {
				String description = (String) config.get(commandName).get("description");
				String usage = (String) config.get(commandName).get("usage");
				player.sendMessage("- " + usage + " | " + description);
			}

 		}
		return true;
	}
}
