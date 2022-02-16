package src;

import java.awt.EventQueue;

import javax.swing.JDialog;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * {@code ViewProfile} class display the user's profile
 * 
 * @author Huy Truong Quang 1370713
 *
 */
public class ViewProfile extends JDialog {

	/**
	 * Test Code
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewProfile dialog = new ViewProfile("1     ");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor of {@code ViewProfile} class
	 * 
	 * @param userID the ID of the user
	 */
	public ViewProfile(String userID) {
		setTitle("Admin Interface - View Profile");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ViewProfile.class.getResource("Tisch/src/main/resources//TimeSchedulerIcon.png")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setBounds(100, 100, 380, 237);

		User selectUser = Database.getUser(userID);

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

		JLabel lblNameValue = new JLabel(selectUser.getName());
		lblNameValue.setBounds(63, 90, 269, 18);
		lblNameValue.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(10, 128, 44, 18);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblEmailValue = new JLabel(selectUser.getEmail());
		lblEmailValue.setBounds(63, 128, 269, 18);
		lblEmailValue.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setBounds(10, 164, 47, 18);
		lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblPhoneValue = new JLabel(selectUser.getPhone());
		lblPhoneValue.setBounds(63, 164, 269, 18);
		lblPhoneValue.setFont(new Font("Tahoma", Font.PLAIN, 15));

		getContentPane().setLayout(null);
		getContentPane().add(lblUserID);
		getContentPane().add(lblUserIDValue);
		getContentPane().add(lblUsername);
		getContentPane().add(lblUsernameValue);
		getContentPane().add(lblName);
		getContentPane().add(lblNameValue);
		getContentPane().add(lblEmail);
		getContentPane().add(lblEmailValue);
		getContentPane().add(lblPhone);
		getContentPane().add(lblPhoneValue);

		setVisible(true);
		setResizable(false);
	}
}
