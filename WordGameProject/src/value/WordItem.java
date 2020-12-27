package value;

/**
 * Properties of a word
 *
 */
public class WordItem {
	
	private String word;
	private String meaning;
	private int x = 0;
	private int y = 100;
	
	private int randX = 0;

	private Direction randWay = Direction.LEFT;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public String getWord() {
		return word;
	}

    public Direction getRandWay() {
		return randWay;
	}

	/**
	 * Constructor of WordItem
	 */
	public WordItem(String word, String meaning) {
		randX = (int) (Math.random() * 500) + 100; // x 100~600 사이 좌표
		this.word = word;
		this.meaning = meaning;
		this.x = randX;
		this.randWay = Direction.getRandomDirection();

		Runnable wordRunner = () -> {
			while (true) {
				try {
					randWay = Direction.getRandomDirection();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(wordRunner).start();
	}

}