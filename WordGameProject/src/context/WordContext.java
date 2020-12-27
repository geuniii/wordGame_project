package context;

import java.util.HashMap;

import value.WordItem;

/**
 * HashMap with generated words
 *
 */
public class WordContext {
	
	private static HashMap<Integer, WordItem> map = new HashMap<>();

	public static HashMap<Integer, WordItem> getMap() {
		return map;
	}

	public static void setMap(HashMap<Integer, WordItem> map) {
		WordContext.map = map;
	}

}
