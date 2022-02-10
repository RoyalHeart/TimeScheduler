package src;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

/**
 * {@code AdminInterface} class provide an interface for the Administrator to manage the User
 * 
 * @author Huy Truong Quang 1370713
 */

public class AdminInterface extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JButton btnEdit;
	private JButton btnDelete;
	private DefaultTableModel tableModel;
	// private static AdminInterface frame;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminInterface frame = new AdminInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminInterface() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AdminInterface.class.getResource("/lib/TimeSchedulerIcon.png")));
		setTitle("Admin Interface");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User List");
		lblNewLabel.setBounds(20, 11, 65, 14);
		contentPane.add(lblNewLabel);
		
		Database.createConnection();
		// Initialize the table
		Vector<String> columnNames = new Vector<String>();
		Vector<Vector> data = Database.getUserList();
		
		columnNames.addElement("ID");
		columnNames.addElement("Username");
		columnNames.addElement("Full Name");
		columnNames.addElement("Email");
			
		tableModel  = new DefaultTableModel(data, columnNames);
		table = new JTable(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 34, 392, 200);
		contentPane.add(scrollPane);
		
		JButton btnView = new JButton("View");
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processView();
			}
		});
		btnView.setBounds(125, 7, 89, 23);
		contentPane.add(btnView);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processEdit();
			}
		});
		btnEdit.setBounds(224, 7, 89, 23);
		contentPane.add(btnEdit);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processDelete();
			}
		});
	
		btnDelete.setBounds(323, 7, 89, 23);
		contentPane.add(btnDelete);
		
		
		
		setVisible(true);
	}
	
	/**
	 *  Call the "View Profile Interface"
	 */
	void processView() {
		int index = table.getSelectedRow();
		if (index == -1) {
			return;
		}
		
		String selectID = tableModel.getValueAt(index, 0).toString();
		System.out.println(selectID);
		
		ViewProfile viewProfile = new ViewProfile(selectID);
	}
	
	/**
	 *  Call the "Edit Profile Interface"
	 */
	void processEdit() {
		int index = table.getSelectedRow();
		if (index == -1) {
			return;
		}
		
		String selectID = tableModel.getValueAt(index, 0).toString();
		System.out.println(selectID);
		
		if (Database.isAdminID(selectID)) {
			JOptionPane.showMessageDialog(null, "You can not edit the Admin account");
		}
		else {
			EditProfile editProfile = new EditProfile(selectID);
		}
	}
	
	/**
	 *  Delete the selected user
	 */
	void processDelete() {
		int index = table.getSelectedRow();
		if (index == -1) {
			return;
		}
		String deleteID = tableModel.getValueAt(index, 0).toString();
		System.out.println(deleteID);
		
		if (Database.isAdminID(deleteID)) {
			JOptionPane.showMessageDialog(null, "You can not delete the Admin account");
		}
		else {
			int respone = JOptionPane.showOptionDialog(null,
                    "Do you want to delete this user ? \nUserID = " + deleteID, "Delete User",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (respone == JOptionPane.YES_OPTION) {
                if(Database.deleteUser(deleteID)) {
                	JOptionPane.showMessageDialog(null, "Delete User Succesffuly");
                	tableModel.removeRow(index);
                }
                else {
                	JOptionPane.showMessageDialog(null, "Delete User Failed!");
                }
            } else if (respone == JOptionPane.NO_OPTION) {
                // do nothing
            } else {
                // do nothing
            }
		}
	}
}
