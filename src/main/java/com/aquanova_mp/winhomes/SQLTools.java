package com.aquanova_mp.winhomes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SQLTools {

	public static class QueryException extends Exception {
		public QueryException(String message) {
			super(message);
		}
	}

	public static String queryReader(String queryPath) throws IOException {
		InputStream in = SQLTools.class.getResourceAsStream("/queries/"+queryPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line);
			str.append("\n");
		}
		return str.toString();
	}

	public static String queryReader(String queryPath, ArrayList<String> substitutions) throws IOException, QueryException {
		String queryString = queryReader(queryPath);

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
