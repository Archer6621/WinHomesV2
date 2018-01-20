package com.aquanova_mp.winhomes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLToolsTest {
	@Test
	void queryReaderTest() {
		String str = "";
		try {
			str = SQLTools.queryReader("testfile.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("test1\ntest2\ntest3\n", str);
	}

	@Test
	void queryReaderTestSub() {
		String str = "";
		try {
			str = SQLTools.queryReader("testfile_sub.txt", new ArrayList<>(Arrays.asList("aa", "bb", "cc")));
		} catch (IOException | SQLTools.QueryException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		assertEquals("SELECT aa FROM bb WHERE cc==1;\n", str);
	}

	@Test
	void queryReaderTestSubTooMany() {
		assertThrows(SQLTools.QueryException.class, () -> SQLTools.queryReader("testfile_sub.txt", new ArrayList<>(Arrays.asList("aa", "bb", "cc", "dd"))));
	}

	@Test
	void queryReaderTestSubTooFew() {
		assertThrows(SQLTools.QueryException.class, () -> SQLTools.queryReader("testfile_sub.txt", new ArrayList<>(Arrays.asList("aa", "bb"))));
	}
}
