package src;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import src.database.Database;

/**
 * {@code EditProfile} class provide an interface for the Administrator to edit
 * the profile of an user
 * 
 * @author Huy Truong Quang 1370713
 *
 */

public class EditProfile extends JDialog {
	private JTextField tfName;
	private JTextField tfEmail;
	private JTextField tfPhone;
	private User selectUser;

	/**
	 * Test Code
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditProfile dialog = new EditProfile("1     ");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor of {@code EditProfile} class
	 * 
	 * @param userID the ID of the user
	 */
	public EditProfile(String userID) {
		setTitle("Admin Interface - Edit Profile");
		setIconImage(Toolkit.getDefaultToolkit().getImage(EditProfile.class.getResource("/lib/TimeSchedulerIcon.png")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 357, 285);

		Database.createConnection();
		selectUser = Database.getUser(userID);

		JLabel lblUserID = new JLabel("User ID:");
		lblUserID.setBounds(10, 11, 75, 18);
		lblUserID.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel lblUserIDValue = new JLabel(selectUser.getId());
		lblUserIDValue.setBounds(100, 11, 75, 18);
		lblUserIDValue.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 43, 84, 18);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel lblUsernameValue = new JLabel(selectUser.getUsername());
		lblUsernameValue.setBounds(100, 43, 161, 18);
		lblUsernameValue.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 90, 44, 18);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(10, 128, 44, 18);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setBounds(10, 164, 47, 18);
		lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 15));

		getContentPane().setLayout(null);
		getContentPane().add(lblUserID);
		getContentPane().add(lblUserIDValue);
		getContentPane().add(lblUsername);
		getContentPane().add(lblUsernameValue);
		getContentPane().add(lblName);
		getContentPane().add(lblEmail);
		getContentPane().add(lblPhone);

		tfName = new JTextField(selectUser.getName());
		tfName.setColumns(20);
		tfName.setBounds(63, 90, 269, 18);
		getContentPane().add(tfName);

		tfEmail = new JTextField(selectUser.getEmail());
		tfEmail.setColumns(20);
		tfEmail.setBounds(63, 128, 269, 18);
		getContentPane().add(tfEmail);

		tfPhone = new JTextField(selectUser.getPhone());
		tfPhone.setColumns(20);
		tfPhone.setBounds(63, 164, 269, 18);
		getContentPane().add(tfPhone);

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (processUpdate()) {
					JOptionPane.showMessageDialog(null, "Updated user's profile successfully.");
				} else {
					JOptionPane.showMessageDialog(null, "Updated Failed!!! "
							+ "\nPlease follow the regulation:"
							+ "\nName: 0-30 characters, no spaces or special characters allowed"
							+ "\nEmail: Required! and in abc@email.com format"
							+ "\nPhone: Optional! 8-10 digits");
				}
			}
		});
		btnApply.setBounds(244, 214, 89, 23);
		getContentPane().add(btnApply);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(145, 214, 89, 23);
		getContentPane().add(btnCancel);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (processUpdate()) {
					JOptionPane.showMessageDialog(null, "Updated user's profile successfully.");
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Updated Failed!!! "
							+ "\nPlease follow the regulation:"
							+ "\nName: 0-30 characters, no spaces or special characters allowed"
							+ "\nEmail: Required! and in abc@email.com format"
							+ "\nPhone: Optional! 8-10 digits");
				}
			}
		});
		btnOK.setBounds(46, 214, 89, 23);
		getContentPane().add(btnOK);

		setVisible(true);
		setResizable(false);

	}

	/**
	 * validate and update new user's profile
	 */
	boolean processUpdate() {
		String newName = tfName.getText();
		String newEmail = tfEmail.getText();
		String newPhone = tfPhone.getText();

		if (RegisterValidator.isValidName(newName) && RegisterValidator.isValidEmail(newEmail)
				&& RegisterValidator.isValidPhone(newPhone)) {
			Database.updateName(newName, selectUser);
			Database.updateEmail(newEmail, selectUser);
			Database.updatePhone(newPhone, selectUser);

			return true;
		} else {
			return false;
		}
	}
}
