package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import context.UserContext;
import context.WordContext;
import model.Dao;
import panel.RankPanel;
import panel.WordGraphics;

/**
 * Implement Game frame
 *
 */
public class GoGame extends InitFrame implements ActionListener, ChangeListener {

	private int level = UserContext.getSelectedLevel();
	private String userid = UserContext.getId();

	private JPanel rankPanel;
	private JPanel wordPanel;
	private JLabel answerSpeed;
	private JLabel correctLabel;
	private JTextField answerField;

	private JButton exitBtn;

	private Timer progressTimer;
	private Timer wordTimer;

	private JProgressBar progress;
	private int progressValue = 0;

	private String answerCheck = "";
	private int score = 0;
	private int scoreUP = 1;

	private int speed = 0;
	private boolean isAnswerSpeedChange = false;

	/**
	 * Constructor of GoGame
	 */
	public GoGame() {
		setBackground(backColor);
		JPanel wholePanel = new JPanel(new FlowLayout());
		wholePanel.setBounds(0, 0, 900, 900);
		wholePanel.setBackground(new Color(38, 83, 175));

		JLabel answerLabel = new JLabel("ANSWER:");
		answerLabel.setBounds(210, 780, 100, 50);
		answerLabel.setForeground(Color.white);

		answerField = new JTextField();
		answerField.setBounds(290, 790, 300, 30);

		JLabel correctSpeed1 = new JLabel("AVERAGE SPEED");
		correctSpeed1.setBounds(40, 50, 150, 50);
		correctSpeed1.setForeground(getFontColor());

		answerSpeed = new JLabel("");
		answerSpeed.setBounds(180, 50, 150, 50);
		answerSpeed.setForeground(getFontColor());

		JLabel scoreLabel1 = new JLabel("SCORE: ");
		scoreLabel1.setBounds(380, 55, 150, 50);
		scoreLabel1.setFont(new Font("Sans Serif", Font.BOLD, 25));
		scoreLabel1.setForeground(getFontColor());

		JLabel scoreLabel2 = new JLabel(String.valueOf(score));
		scoreLabel2.setBounds(490, 55, 150, 50);
		scoreLabel2.setFont(new Font("Sans Serif", Font.BOLD, 25));
		scoreLabel2.setForeground(getFontColor());

		JLabel rankLabel = new JLabel("RANKING");
		rankLabel.setBounds(750, 28, 150, 100);
		rankLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
		rankLabel.setForeground(getFontColor());

		rankPanel = new RankPanel();
		rankPanel.setBounds(710, 85, 140, 250);
		rankPanel.setBackground(backColor);

		correctLabel = new JLabel();
		correctLabel.setBounds(600, 780, 300, 50);
		correctLabel.setForeground(Color.white);

		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setBounds(40, 20, 800, 30);

		exitBtn = new JButton(new ImageIcon("img\\exit.png"));
		exitBtn.setBounds(750, 750, 80, 80);
		exitBtn.setBackground(backColor);
		exitBtn.setBorder(null);
		exitBtn.addActionListener(this);

		wordPanel = new WordGraphics(680, 680);
		wordPanel.setBounds(40, 100, 680, 680);
		wordPanel.setBackground(backColor);

		JLabel speedLabel = new JLabel("<html>SPEED UP!<br>SCORE UP!<br></html>");
		speedLabel.setBounds(740, 350, 100, 150);
		speedLabel.setHorizontalAlignment(JLabel.CENTER);
		speedLabel.setForeground(getFontColor());

		JSlider slider = new JSlider(JSlider.VERTICAL, 1, 3, 1);
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.addChangeListener((ChangeListener) this);
		slider.setBounds(760, 450, 50, 180);

		slider.setBackground(backColor);
		slider.setForeground(Color.white);

		
		add(answerField);
		add(answerSpeed);
		add(exitBtn);
		add(correctLabel);
		add(exitBtn);
		add(progress);
		add(rankPanel);
		add(wordPanel);
		add(correctSpeed1);
		add(answerLabel);
		add(scoreLabel1);
		add(scoreLabel2);
		add(rankLabel);	
		add(slider);
		add(speedLabel);
		add(wholePanel);

		setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				progressWork();
				runWords();
				correctSpeed();

			}
		});

		answerField.addKeyListener(new KeyListener() {
			Clip correctClip;
			Clip incorrectClip;

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
					answerCheck = Dao.getInstance().answerCheck(answerField.getText(), WordContext.getMap());
					correctLabel.setText(answerCheck);

					if (answerCheck.equals("CORRECT")) {
						score = score + scoreUP;
						scoreLabel2.setText(String.valueOf(score));
						isAnswerSpeedChange = true;
						sound("correct.wav", correctClip);
					}
					if (answerCheck.equals("WRONG")) {
						sound("incorrect.wav", incorrectClip);
					}
					answerField.setText(null);
				}
			}
		});

	}

	/**
	 * Speed control event
	 */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			int value = (int) source.getValue();
			((WordGraphics) wordPanel).setTimeSpeed(5000 / value);
			((WordGraphics) wordPanel).setxSpeed(value);
			((WordGraphics) wordPanel).setySpeed(value);
			scoreUP = value;
		}
	}

	/**
	 * Implement Progress Timer
	 */
	public void progressWork() {
		progressTimer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progress.setValue(progressValue++);
				if (progressValue == 100) {
					progressTimer.stop();
					wordTimer.stop();
					((WordGraphics) wordPanel).getWordMakerRun().interrupt();
					String returnMsg = Dao.getInstance().maxScoreCheck(userid, level, score);
					JOptionPane.showConfirmDialog(null, "YOUR SCORE:" + score + "\n" + returnMsg, "END",
							JOptionPane.CLOSED_OPTION);
					new Home();
					dispose();
					bgmClip.close();
					return;
				}
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		progressTimer.start();
	}

	/**
	 * Word repaint Thread
	 */
	public void runWords() {
		((WordGraphics) wordPanel).createWord();
		wordTimer = new Timer(20, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wordPanel.repaint();
			}
		});
		wordTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitBtn) {
			progressTimer.stop();
			wordTimer.stop();
			//((WordGraphics) wordPanel).getWordMakerRun().interrupt();
			int input = JOptionPane.showConfirmDialog(this, "Your score will not be saved", "EXIT",
					JOptionPane.YES_NO_OPTION);
			if (input == JOptionPane.YES_OPTION) {
				new Home();
				dispose();
				bgmClip.stop();
			}
			if (input == JOptionPane.NO_OPTION) {
				progressTimer.restart();
				wordTimer.restart();
			}
		}
	}

	/**
	 * Check correct answer speed
	 */
	public void correctSpeed() {
		Thread t = new Thread() {
			public void run() {
				int time = 0;
				while (!isInterrupted()) {
					answerSpeed.setText(String.valueOf(speed) + "'s");
					try {
						time++;
						if (isAnswerSpeedChange) {
							speed = time;
							answerSpeed.setText(String.valueOf(speed) + "'s");
							time = 0;
							isAnswerSpeedChange = false;
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		t.start();
	}

	/**
	 * Sound effect implementation
	 * 
	 * @param fileName Word file path
	 * @param clip     Music Clip
	 */
	public static void sound(String fileName, Clip clip) {

		AudioInputStream audioStream;
		AudioFormat format;
		DataLine.Info info;

		File bgm = new File(fileName);

		try {
			audioStream = AudioSystem.getAudioInputStream(bgm);
			format = audioStream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(audioStream);
			clip.start();
			clip.loop(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
