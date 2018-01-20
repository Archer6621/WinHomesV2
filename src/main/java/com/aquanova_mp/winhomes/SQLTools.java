package com.aquanova_mp.winhomes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SQLTools {

	public static String queryReader(InputStream in) throws IOException {
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
