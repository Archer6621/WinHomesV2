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
		return str.toString();
	}

	public static void initializeDataBase(WinHomes main, MysqlDataSource dataSource) {
		try {
			Connection conn = dataSource.getConnection();

			// Check whether winhomes database already exists
			ResultSet resultSet = conn.getMetaData().getCatalogs();
			boolean exists = false;
			// Iterate through catalog to find the database names
			while (resultSet.next()) {
				String databaseName = resultSet.getString(1); // Database name is at the first position
				if (databaseName.equals("winhomes")) {
					exists = true;
					main.getLogger().log(Level.INFO,"winhomes database found, continuing...");
				}
			}
			resultSet.close();

			// Create the database if it was not present, along with the tables
			Statement stmt = conn.createStatement();
			if (!exists) {
				main.getLogger().log(Level.INFO,"winhomes database not found, creating it!");

				stmt.executeUpdate("CREATE DATABASE winhomes;");

				// Load the query with the create statements for the tables
				String query = SQLTools.queryReader("create_tables.sql");
				ResultSet rs = stmt.executeQuery(query);
				rs.close();
			}

			stmt.close();
			conn.close();
			main.getLogger().log(Level.INFO,"Database successfully initialized!");

		} catch (SQLException e) {
			main.getLogger().log(Level.WARNING, "Could not perform database initialization!");
			main.getLogger().log(Level.WARNING, e.toString());
		} catch (IOException e) {
			main.getLogger().log(Level.WARNING, "Could not open initialization query file!");
			main.getLogger().log(Level.WARNING, e.toString());
		}
	}
}
