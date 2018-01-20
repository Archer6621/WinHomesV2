package com.aquanova_mp.winhomes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class contains several tools for interacting with the database and related matters
 */
public class SQLTools {

	/**
	 * QueryException is used during query substitution, if the amount of substitution parameters doesn't match the substitutions characters in the query
	 */
	public static class QueryException extends Exception {
		public QueryException(String message) {
			super(message);
		}
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
		return str.toString();
	}

	/**
	 * Reads a query file from the query folder and then substitutes question marks in the query with the given items in the ArrayList in an ordered fashion
	 *
	 * @param queryFileName the file name of the query file
	 * @param substitutions the array list with substitution strings
	 * @return the query as string, with substitutions
	 * @throws IOException    thrown when the given file cannot be found
	 * @throws QueryException thrown when the amount of items in the array list doesn't match the available query parameters
	 */
	public static String queryReader(String queryFileName, ArrayList<String> substitutions) throws IOException, QueryException {
		String queryString = queryReader(queryFileName);

		for (String sub : substitutions) {
			if (queryString.contains("?")) {
				queryString = queryString.replaceFirst("\\?", sub);
			} else {
				throw new QueryException("Too many substitution parameters given");
			}
		}

		if (queryString.contains("?")) {
			throw new QueryException("Too few substitution parameters given");
		}

		return queryString;
	}
}
