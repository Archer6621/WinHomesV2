package com.aquanova_mp.winhomes;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class contains several tools for interacting with the database and related matters
 */
public class SQLTools {
	private static String prefix;

	public static void setPrefix(String p) {
		prefix = p;
	}

	/**
	 * Reads a query file from the query folder.
	 *
	 * @param queryFileName the file name of the query file
	 * @return the query as a string
	 * @throws IOException thrown when the given file cannot be found
	 */
	public static String queryReader(String queryFileName) throws IOException {
		InputStream in = SQLTools.class.getResourceAsStream("/queries/"+queryFileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line);
			str.append("\n");
		}
		String res = str.toString();
		for (String postfix : new String[]{"."," ", "\n"}) {
			for (String table : new String[]{"player","home","invite"})
			res = res.replace(table+postfix, prefix + "_" + table+postfix);
		}
		return res;
	}

	public static boolean initializeDataBase(WinHomes main) {
		try {
			Connection conn = main.getDataSource().getConnection();

			// Initialize tables for database
			Statement stmt = conn.createStatement();
			String query = SQLTools.queryReader("create_tables.sql");
			ResultSet rs = stmt.executeQuery(query);
			rs.close();
			stmt.close();
			conn.close();
			main.getLogger().log(Level.INFO,"Database successfully initialized!");
			return true;

		} catch (SQLException | IOException e) {
			main.getLogger().log(Level.WARNING, "Could not perform database initialization!");
			main.getLogger().log(Level.WARNING, e.toString());
		}
		return false;
	}
}
