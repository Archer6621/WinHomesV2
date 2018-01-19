package com.aquanova_mp.winhomes;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class WinHomes extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().log(Level.INFO,"Hello World!");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO,"Bye World...");
	}
}
