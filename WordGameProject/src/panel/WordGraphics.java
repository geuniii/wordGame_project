package panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import context.UserContext;
import context.WordContext;
import model.Dao;
import value.Direction;
import value.WordItem;

/** 
 * Panel where words are moving
 *
 */
public class WordGraphics extends JPanel {

	private static final int COUNT = 100;

	private int height;
	private int width;
	
	private int xSpeed = 1;
	private int ySpeed = 1;	
	private int timeSpeed = 5000;

	private Image offScreenImage;
	private Graphics offScreen;

	private Thread wordMakerRun;
	
	
	public int getHeight() {
		return height;
	}
	public int getTimeSpeed() {
		return timeSpeed;
	}

	public void setTimeSpeed(int timeSpeed) {
		this.timeSpeed = timeSpeed;
	}
	
	public int getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getySpeed() {
		return ySpeed;
	}

	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	public Thread getWordMakerRun() {
		return wordMakerRun;
	}
	
	/**
	 * Constructor of WordGraphics Class
	 */
	public WordGraphics(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Word Generation Threads
	 */
	public void createWord() {

		int level = UserContext.getSelectedLevel();
		offScreenImage = createImage(getSize().width, getSize().height);
		offScreen = offScreenImage.getGraphics();
		
		HashMap wordMap = Dao.getInstance().wordMaker(COUNT, level);
		
		wordMakerRun = new Thread() {
			int index = 0;
			public void run() {
				while (!isInterrupted()) {
					try {
						WordContext.getMap().put(index++, (WordItem)wordMap.get(index));
						Thread.sleep(timeSpeed);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		};
		wordMakerRun.start();
	}

	/**
	 * Implement word movement
	 * @param word Word to move
	 */
	public void move(WordItem word) {

		if (word.getX() < 0 || word.getX() > width-100)
			xSpeed *= -1;
		if (word.getRandWay()==Direction.LEFT) {
			word.setX((word.getX()-xSpeed));
			word.setY((word.getY()+ySpeed));
		}
		if (word.getRandWay()== Direction.RIGHT) {
			word.setX((word.getX()+xSpeed));
			word.setY((word.getY()+ySpeed));
		}
	}
	
	/**
	 * Remove Word
	 */
	public void removeWord() {
		Iterator<Map.Entry<Integer, WordItem>> iter = WordContext.getMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, WordItem> entry = iter.next();
			if (entry.getValue().getY() >= height) {
				iter.remove();
			}
		}
	}
	

	/**
	 * Implement graphic
	 */
	public void paint(Graphics g) {

		if (offScreen == null)
			return;
		offScreen.setColor(Color.white);
		offScreen.clearRect(0, 0, getWidth(), getHeight());
		offScreen.setColor(Color.white);
		offScreen.setFont(new Font("Sans Serif", Font.BOLD, 30));

		removeWord(); 

		for (WordItem item :WordContext.getMap().values()) {
			move(item);
			offScreen.drawString(item.getWord(), item.getX(), item.getY());
		}

		g.drawImage(offScreenImage, 0, 0, this);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void destory() {
		offScreen.dispose();
	}
}
