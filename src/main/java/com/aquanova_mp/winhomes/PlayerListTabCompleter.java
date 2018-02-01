package com.aquanova_mp.winhomes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class PlayerListTabCompleter implements TabCompleter {
	private LinkedList<String> playerNames;
	private WinHomes main;

	public PlayerListTabCompleter(WinHomes winhomes) {
		main = winhomes;
		try (Connection conn = winhomes.getDataSource().getConnection()) {
			String getPlayersQuery = SQLTools.queryReader("get_player_names.sql");
			PreparedStatement preparedStmtGetPlayers = conn.prepareStatement(getPlayersQuery);
			ResultSet rs = preparedStmtGetPlayers.executeQuery();
			playerNames = new LinkedList<>();
			while(rs.next()) {
				playerNames.add(rs.getString(1));
			}
			rs.close();
			preparedStmtGetPlayers.close();
		} catch (SQLException | IOException e) {
			winhomes.getLogger().log(Level.WARNING, e.getMessage());
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		ArrayList<String> matches = new ArrayList<>();
		StringUtil.copyPartialMatches(args[0], playerNames, matches);
		Collections.sort(matches);
		return matches;
	}
}
