package dexbot.utils;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

public class URLUtilsTest {

	@Test
	public void testGetUrl() {
		URL resource = URLUtilsTest.class.getClassLoader().getResource("json");
		assertNotNull(resource);
		
		String result = URLUtils.readAsString(resource);
		assertEquals("{\"name\": \"Emmett Brown\"}", result);
	}
}
