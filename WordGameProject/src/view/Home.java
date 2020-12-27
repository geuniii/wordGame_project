package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import context.UserContext;
import model.Dao;
import panel.RankPanel;

/**
 * Implement home frame
 *
 */
public class Home extends InitFrame implements ActionListener {

	private static final int LEVELCOUNT = 5;

	private String userid = UserContext.getId();
	private int level = UserContext.getSelectedLevel();
	
	private JButton[] levelBtn = new JButton[LEVELCOUNT];
	private JButton rankingCheckBtn;


	/**
	 * Constructor of Home Class
	 */
	public Home() {
		JPanel backPanel = new JPanel(new FlowLayout());
		backPanel.setBounds(0, 0, 900, 350);
		ImageIcon img = new ImageIcon("img\\home.gif");
		JLabel imgLabel = new JLabel(img);
		backPanel.add(imgLabel);
		
		JPanel grid = new JPanel(new GridLayout(1, 5, 10, 5));
		grid.setBounds(100, 350, 700, 150);
		grid.setBackground(backColor);		

		for (int i = 0; i < LEVELCOUNT; ++i) {
			levelBtn[i] = new JButton(new ImageIcon("img\\lv"+String.valueOf(i+1)+".png"));
			levelBtn[i].setBackground(backColor);
			levelBtn[i].setBorder(null);
			grid.add(levelBtn[i]);
			levelBtn[i].addActionListener(this);
		}
		
		JLabel rankLabel = new JLabel("SEARCH YOUR RANKING ----------------------->");
		rankLabel.setBounds(180, 520, 500, 150);
		rankLabel.setForeground(getFontColor());
		
		rankingCheckBtn = new JButton("RANKING CHECK");
		rankingCheckBtn.setBounds(500, 580, 200, 30);
		rankingCheckBtn.setForeground(Color.white);
		rankingCheckBtn.setBackground(getFontColor());
		rankingCheckBtn.addActionListener(this);

		add(rankLabel);
		add(grid);
		add(rankingCheckBtn);
		
		add(backPanel);
		add(background);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==rankingCheckBtn) {
			JOptionPane.showMessageDialog(null,Dao.getInstance().myRankSearch(userid,RankPanel.getRanknum()));			
		}
		
		for (int i = 0; i < LEVELCOUNT; ++i) {
			if (e.getSource() == levelBtn[i]) {			
				int input = JOptionPane.showConfirmDialog(null,"ARE YOU READY?","READY?",JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					level = i + 1;
					UserContext.setSelectedLevel(level);
					new GoGame();
					dispose();
					bgmClip.stop();
				}
				
			}
		}
	}
}
