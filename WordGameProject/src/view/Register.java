package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Dao;

/**
 * Implement Register frame
 *
 */
public class Register extends InitFrame implements ActionListener {

	private JLabel idLabel;
	private JLabel pwLabel1;
	private JLabel pwLabel2;
	private JLabel idCheckLabel;

	private JTextField idField;
	private JTextField pwField1;
	private JTextField pwField2;

	private JButton idCheckBtn;
	private JButton joinBtn;

	private JPanel background;

	/**
	 * Constructor of Register Class
	 */
	public Register() {
		background = new JPanel();
		background.setBounds(0, 500, 900, 400);
		background.setBackground(backColor);

		idLabel = new JLabel("ID");
		pwLabel1 = new JLabel("PW");
		pwLabel2 = new JLabel("PW AGAIN");
		idCheckLabel = new JLabel("");
		idCheckLabel.setForeground(Color.white);
		idLabel.setForeground(Color.white);
		pwLabel1.setForeground(Color.white);
		pwLabel2.setForeground(Color.white);

		idField = new JTextField(30);
		pwField1 = new JTextField(30);
		pwField2 = new JTextField(30);

		idCheckBtn = new JButton("CHECK ID");
		idCheckBtn.setBackground(getFontColor());
		idCheckBtn.setForeground(Color.white);
		joinBtn = new JButton("JOIN");
		joinBtn.setForeground(Color.white);
		joinBtn.setBackground(getFontColor());

		idCheckBtn.addActionListener(this);
		joinBtn.addActionListener(this);

		idLabel.setBounds(290, 500, 100, 50);
		idField.setBounds(315, 515, 200, 24);
		idCheckLabel.setBounds(315, 480, 200, 53);
		pwLabel1.setBounds(285, 535, 100, 50);
		pwField1.setBounds(315, 548, 200, 24);
		pwLabel2.setBounds(235, 565, 100, 50);
		pwField2.setBounds(315, 581, 200, 24);
		idCheckBtn.setBounds(525, 548, 110, 55);
		joinBtn.setBounds(315, 615, 325, 30);
		
		add(idLabel);
		add(idField);
		add(pwLabel1);
		add(pwField1);
		add(pwLabel2);
		add(pwField2);
		add(idCheckBtn);
		add(joinBtn);
		add(idCheckLabel);

		add(gifPanel);
		add(background);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == idCheckBtn) {
			String ret = Dao.getInstance().idCheck(idField.getText());
			if (ret != null) {
				idCheckLabel.setText(ret);
			} else {
				idField.setText(null);
			}
		}
		if (e.getSource() == joinBtn) {
			join();
		}

	}

	/**
	 * Register new user's data
	 * 
	 * idField, pwField can't be empty.
	 * ID must be checked
	 * Passwords must match.
	 */
	private void join() {
		if (idCheckLabel.getText().equals("")) {
			if (idField.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "ID를 입력해주세요!");
				return;
			}
			JOptionPane.showMessageDialog(null, "ID를 Check해주세요!");
			return;
		}
		if (pwField1.getText().equals("") || pwField2.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "PW를 입력해주세요!");
			return;
		}

		if (!pwField1.getText().equals(pwField2.getText())) {
			JOptionPane.showMessageDialog(null, "패스워드를 확인해주세요!");
			return;
		}

		if (Dao.getInstance().join(idField.getText(), pwField1.getText())) {
			new Login();
			dispose();
			bgmClip.stop();
		}

	}
}