package value;

/**
 * Determining the left and right movements of a word
 */
public enum Direction {
	LEFT, RIGHT;	
	static Direction getRandomDirection() {
		return Math.random() < .5 ? LEFT : RIGHT;
	}
}
