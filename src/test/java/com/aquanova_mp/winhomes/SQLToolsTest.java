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
}
