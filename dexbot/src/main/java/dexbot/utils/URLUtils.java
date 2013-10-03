package dexbot.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class URLUtils {
	public static String readAsString(URL url) {
		StringBuilder ret = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				ret.append(line);
			}
			reader.close();

			return ret.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
