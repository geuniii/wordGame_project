package context;

/**
 * Login ID, selected Level
 *
 */
public class UserContext {
	private static String id;
	private static int selectedLevel;
	
	UserContext(String id, int level){
		this.id = id;
		selectedLevel = level;
	}

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		UserContext.id = id;
	}

	public static int getSelectedLevel() {
		return selectedLevel;
	}

	public static void setSelectedLevel(int selectedLevel) {
		UserContext.selectedLevel = selectedLevel;
	}

	
}
