package com.aquanova_mp.winhomes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SQLTools {

	public static String queryReader(String query) throws IOException {
		InputStream in = SQLTools.class.getResourceAsStream("/queries/"+query);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line);
			str.append("\n");
		}
		return str.toString();
	}
}
