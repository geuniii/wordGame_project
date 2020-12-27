package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.util.Enumeration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * Set all frame properties
 *
 */
public class InitFrame extends JFrame {

	protected static Color backColor;
	protected static Color fontColor;
	protected static Font labelFont;
	protected static JPanel gifPanel;
	protected static JLabel imgLabel;
	protected static JPanel background;	
	protected static Clip bgmClip;

	
	static {
		backColor = new Color(38,83,175);
		fontColor = new Color(241,185,71);
		labelFont = new Font("Sans Serif", Font.BOLD, 18);
		
	}
	
	static{
		gifPanel = new JPanel(new FlowLayout());
		gifPanel.setBounds(0, 0, 900, 500);		
		ImageIcon img = new ImageIcon("img\\wordLogo.gif");		
		imgLabel = new JLabel(img);
		gifPanel.add(imgLabel);
		
		background = new JPanel();
		background.setBounds(0, 300, 900, 600);
		background.setBackground(backColor);
	}
	

	/**
	 * Full Font Settings
	 * @param f FontUIResource
	 */
	public static void setUIFont(FontUIResource f) {
		Enumeration keys = UIManager.getDefaults().keys();		
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			
			if(value instanceof FontUIResource) {
				FontUIResource ui = (FontUIResource) value;			
				Font font = new Font(f.getFontName(),ui.getStyle(),f.getSize());
				UIManager.put(key,new FontUIResource(font));
			}
		}
	}
	
	/**
	 * font Color getter
	 * @return fontColor
	 */
	public static Color getFontColor() {
		return fontColor;
	}

	
	/**
	 * Background music
	 */
	public static void sound() {
		
		AudioInputStream audioStream;
		AudioFormat format;
		DataLine.Info info;
		
		File bgm = new File("sound\\retroGame.wav");
				
		try {
			audioStream = AudioSystem.getAudioInputStream(bgm);
			format = audioStream.getFormat();
			info = new DataLine.Info(Clip.class,format);
			bgmClip = (Clip)AudioSystem.getLine(info);
			bgmClip.open(audioStream);
			bgmClip.start();
			bgmClip.loop(-1);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Full Frame Initialization
	 * @param title Frame title
	 */
	protected InitFrame() {
	
		setUIFont(new FontUIResource(new Font("Sans Serif", Font.BOLD, 15)));
		setTitle("★★★★★★★★★★★★★★★★★★★★★★★★ W O R D   G E N I U S ★★★★★★★★★★★★★★★★★★★★★★★★");
		setSize(900, 900);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		setBackground(backColor);	
		
		sound();
	
	}
}
