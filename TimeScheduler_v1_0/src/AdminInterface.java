package src;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class AdminInterface extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JButton btnEdit;
	private JButton btnDelete;
	DefaultTableModel tableModel;

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
		
		// Initialize the table
		Vector<String> columnNames = new Vector<String>();
		Vector<Vector> data = Database.getUserList();
		
		columnNames.addElement("ID");
		columnNames.addElement("Username");
		columnNames.addElement("Full Name");
		columnNames.addElement("Email");
			
		DefaultTableModel tableModel  = new DefaultTableModel(data, columnNames);
		table = new JTable(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 34, 392, 200);
		contentPane.add(scrollPane);
		
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
	
	void processEdit() {
		
	}
	
	void processDelete() {
		
	}
}

