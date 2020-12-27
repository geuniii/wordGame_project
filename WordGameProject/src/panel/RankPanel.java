package panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.FontUIResource;

import com.mysql.cj.protocol.Resultset;

import context.UserContext;
import model.Dao;
import view.GoGame;
import view.InitFrame;

/**
 * Show ranking panel
 */
public class RankPanel extends JPanel {

	private static final int RANKNUM = 5;
	
	private JLabel[] rankGrid;
	private int rankNum = 1;
	private int rankIndex = 0;
	
	private int level = UserContext.getSelectedLevel();
	private String[] userId;
	
	public static int getRanknum() {
		return RANKNUM;
	}


	/**
	 * Constructor of RankPanel Class
	 */
	public RankPanel() {
		setLayout(new GridLayout(5, 1));
		rankGrid = new JLabel[RANKNUM * 2];

		String[] userId = Dao.getInstance().rankList(level,RANKNUM);
		
		/**
		 * Put Ranked IDs in the grid
		 */
		for (int i = 0; i < RANKNUM * 2; ++i) {

			if (i % 2 == 0) {
				rankGrid[i] = new JLabel(String.valueOf(rankNum++) + ".    ", JLabel.RIGHT);
				rankGrid[i].setForeground(InitFrame.getFontColor());
			} else {
				rankGrid[i] = new JLabel(userId[rankIndex++], JLabel.LEFT);
				rankGrid[i].setForeground(InitFrame.getFontColor());
			}
			add(rankGrid[i]);
		}

	}

}
