package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import context.UserContext;
import model.Dao;

/**
 * Implement Login frame
 *
 */
public class Login extends InitFrame implements ActionListener {

	private JTextField idField;
	private JTextField pwField;

	private JButton login;
	private JButton register;

	/**
	 * Constructor of Login Class
	 */
	public Login() {
		JLabel idLabel = new JLabel("ID");
		idLabel.setForeground(Color.white);
		idLabel.setFont(labelFont);

		JLabel pwLabel = new JLabel("PW");
		pwLabel.setForeground(Color.white);
		pwLabel.setFont(labelFont);

		idField = new JTextField(30);
		pwField = new JTextField(30);

		login = new JButton("LOGIN");
		login.setBackground(getFontColor());
		login.setForeground(Color.white);

		register = new JButton("REGISTER");
		register.setBackground(getFontColor());
		register.setForeground(Color.white);

		login.addActionListener(this);
		register.addActionListener(this);

		idLabel.setBounds(300, 600, 100, 50);
		idField.setBounds(325, 615, 200, 24);
		pwLabel.setBounds(290, 635, 100, 50);
		pwField.setBounds(325, 648, 200, 24);
		login.setBounds(530, 615, 115, 26);
		register.setBounds(530, 645, 115, 26);

		add(idLabel);
		add(idField);
		add(pwLabel);
		add(pwField);
		add(login);
		add(register);

		add(gifPanel);
		add(background);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == login) {
			if (Dao.getInstance().login(idField.getText(), pwField.getText())) {
				UserContext.setId(idField.getText());
				new Home();
				dispose();
				bgmClip.stop();
			} else {
				idField.setText(null);
				pwField.setText(null);
			}
		}

		if (e.getSource() == register) {
			register();
		}
	}

	/**
	 * Move to register frame
	 */
	private void register() {
		new Register();
		dispose();
		bgmClip.stop();
	}

}