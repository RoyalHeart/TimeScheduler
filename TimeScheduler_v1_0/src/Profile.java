package src;

// import java.util.*;
import java.awt.*;

// import java.awt.event.*;
import javax.swing.*;

public class Profile extends JPanel {
    JPanel panel = new JPanel();
    JLabel nameLabel = new JLabel("Name: ");
    JLabel emailLabel = new JLabel("Email: ");
    JLabel phoneLabel = new JLabel("Phone: ");
    JButton chPasswBtn = new JButton("Change Password");
    JPanel editPanel = new JPanel();
    User user;

    Profile(User user) {
        this.user = user;
        this.setLayout(new BorderLayout());
        this.setSize(300, 200);
        this.setVisible(true);
        JLabel label = new JLabel("Profile");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(3, 0));
        /*
         * panel.add(nameLabel);
         * panel.add(name);
         * panel.add(emailLabel);
         * panel.add(email);
         * panel.add(phoneLabel);
         * panel.add(phone);
         */
        panel.add(new namePanel(user));
        panel.add(new emailPanel(user));
        panel.add(new phonePanel(user));
        editPanel.setLayout(new FlowLayout());
        editPanel.add(chPasswBtn);
        this.add(panel, BorderLayout.CENTER);
        this.add(editPanel, BorderLayout.SOUTH);

        try {
            chPasswBtn.addActionListener(e -> {
                new passwDialog(new passwEditPanel());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updatePanel(User user) {
        panel.removeAll();

        panel.add(new namePanel(user));
        panel.add(new emailPanel(user));
        panel.add(new phonePanel(user));
        panel.revalidate();
        panel.repaint();
    }

    class profilePanel extends JPanel {
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JPanel editPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        profilePanel() {
            editPanel.setLayout(new GridBagLayout());

            // add namePanel to gridbaglayout
            gbc.gridx = 0;
            gbc.gridy = 0;
            editPanel.add(new namePanel(user), gbc);

            // add emailPanel to gridbaglayout
            gbc.gridx = 0;
            gbc.gridy = 1;
            editPanel.add(new emailPanel(user), gbc);

            // add phonePanel to gridbaglayout
            gbc.gridx = 0;
            gbc.gridy = 2;
            editPanel.add(new phonePanel(user), gbc);

            this.add(editPanel, BorderLayout.CENTER);
        }
    }

    class namePanel extends JPanel {
        JButton nameEditBtn = new JButton("Edit");
        JLabel name = new JLabel(user.getName());
        editDialog edit;

        namePanel(User user) {
            name = new JLabel(user.getName());
            this.setLayout(new FlowLayout());
            this.add(nameLabel);
            this.add(name);
            this.add(nameEditBtn);
            try {
                nameEditBtn.addActionListener(e -> {
                    if (edit == null || !edit.isShowing()) {
                        edit = new editDialog(new nameEditPanel(), 0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class emailPanel extends JPanel {
        JButton emailEditBtn = new JButton("Edit");
        JLabel email = new JLabel(user.getEmail());
        editDialog edit;

        emailPanel(User user) {
            email = new JLabel(user.getEmail());
            this.setLayout(new FlowLayout());
            this.add(emailLabel);
            this.add(email);
            this.add(emailEditBtn);
            try {
                emailEditBtn.addActionListener(e -> {
                    if (edit == null || !edit.isShowing()) {
                        edit = new editDialog(new emailEditPanel(), 1);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class phonePanel extends JPanel {
        JButton phoneEditButton = new JButton("Edit");
        JLabel phone = new JLabel(user.getPhone());
        editDialog edit;

        phonePanel(User user) {
            phone = new JLabel(user.getPhone());
            this.setLayout(new FlowLayout());
            this.add(phoneLabel);
            this.add(phone);
            this.add(phoneEditButton);
            try {
                phoneEditButton.addActionListener(e -> {
                    if (edit == null || !edit.isShowing()) {
                        edit = new editDialog(new phoneEditPanel(), 2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // base dialog for edit name, email, phone, password...
    class baseDialog extends JDialog {
        baseDialog() {
            super(SwingUtilities.windowForComponent(panel));
            this.setLayout(new BorderLayout());
            this.setSize(600, 400);
            this.setVisible(true);
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.setResizable(false);
            this.setLocationRelativeTo(null);
        }
    }

    // dialog for edit name, email, phone, ...
    class editDialog extends baseDialog {
        editDialog(basePanel edit, int flag) {
            this.add(edit, BorderLayout.CENTER);
            this.add(new btnPanel(this, edit, flag, null), BorderLayout.SOUTH);
        }
    }

    // dialog for edit password
    class passwDialog extends baseDialog {
        passwDialog(passwEditPanel passwEdit) {
            this.add(passwEdit, BorderLayout.CENTER);
            this.add(new btnPanel(this, null, 3, passwEdit), BorderLayout.SOUTH);
        }
    }

    // base panel for edit panel
    class basePanel extends JPanel {
        // label for new name, email, phone, ...
        protected JLabel newLabel = new JLabel();
        // label for confirm new name, email, phone, ...
        protected JLabel confirmLabel = new JLabel();
        // text field for new name, email, phone, ...
        protected JTextField newField = new JTextField(20);
        // text field for confirm new name, email, phone, ...
        protected JTextField confirmField = new JTextField(20);
        // label for current display
        protected JLabel currentLabel = new JLabel();
        // label for current name, email, phone, ...
        protected JLabel currentDisplayLabel = new JLabel();
        // panel for current display
        JPanel currentPanel = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();

        basePanel() {
            this.setLayout(new GridBagLayout());

            currentPanel.setLayout(new FlowLayout());
            currentPanel.add(currentLabel);
            currentPanel.add(currentDisplayLabel);
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(currentPanel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(newLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            this.add(newField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(confirmLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            this.add(confirmField, gbc);
        }
    }

    class nameEditPanel extends basePanel {
        nameEditPanel() {
            currentLabel.setText("Current name: ");
            currentDisplayLabel.setText(user.getName());
            newLabel.setText("New name: ");
            confirmLabel.setText("Confirm new name: ");
        }
    }

    class emailEditPanel extends basePanel {
        emailEditPanel() {
            currentLabel.setText("Current email address: ");
            currentDisplayLabel.setText(user.getEmail());
            newLabel.setText("New email address: ");
            confirmLabel.setText("Confirm email address: ");
        }
    }

    class phoneEditPanel extends basePanel {
        phoneEditPanel() {
            currentLabel.setText("Current phone number: ");
            currentDisplayLabel.setText(user.getPhone());
            newLabel.setText("New phone number: ");
            confirmLabel.setText("Confirm new phone number: ");
        }
    }

    // panel for change password
    class passwEditPanel extends JPanel {
        // label for new password
        JLabel newLabel = new JLabel("New Password: ");
        // label for confirm new password
        JLabel confirmLabel = new JLabel("Confirm new password: ");
        // password field for new password
        JPasswordField newPassw = new JPasswordField(20);
        // password field for confirm new password
        JPasswordField confirmNewPassw = new JPasswordField(20);

        GridBagConstraints gbc = new GridBagConstraints();

        passwEditPanel() {
            this.setLayout(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(newLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            this.add(newPassw, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(confirmLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            this.add(confirmNewPassw, gbc);
        }
    }

    // panel for confirm and cancel btn in edit dialog
    class btnPanel extends JPanel {
        JButton cancelBtn = new JButton("Cancel");
        JButton confirmBtn = new JButton("Confirm");
        // String array to easy display message dialog
        String[] arr = {"Name", "Email", "Phone"};
        // Variable to reference to BasePanel
        basePanel base;

        void showNotMatchMessage(int flag)
        {
            String showString = arr[flag] + " does not match.";
            JOptionPane.showMessageDialog(null, showString);
        }

        void showInvalidMessage(int flag)
        {
            String showString = arr[flag] + " is invalid.";
            JOptionPane.showMessageDialog(null, showString);
        }

        // Function to check validation of name, email and phone
        boolean checkInvalid(int flag)
        {
            switch (flag) 
            {
                case 0:
                    if (RegisterValidator.isValidName(base.newField.getText())) 
                    {
                        user.setName(base.newField.getText());
                        Database.updateName(base.newField.getText(), user);
                        JOptionPane.showMessageDialog(null, "Updated name successfully.");
                        return true;
                    }

                case 1:
                    if (RegisterValidator.isValidEmail(base.newField.getText()))
                    {
                        user.setEmail(base.newField.getText());
                        Database.updateEmail(base.newField.getText(), user);
                        JOptionPane.showMessageDialog(null, "Updated email successfully.");
                        return true;
                    }

                case 2:
                    if (RegisterValidator.isValidPhone(base.newField.getText()))
                    {
                        user.setPhone(base.newField.getText());
                        Database.updatePhone(base.newField.getText(), user);
                        JOptionPane.showMessageDialog(null, "Updated phone successfully.");
                        return true;
                    }
            }
            return false;
        }

        // baseD to reference to baseDialog, baseP to reference basePanel, passwP to
        // reference to passwEditPanel
        btnPanel(baseDialog baseD, basePanel baseP, int flag, passwEditPanel passwP) {
            this.setLayout(new FlowLayout());
            this.add(confirmBtn);
            this.add(cancelBtn);
            cancelBtn.addActionListener(e -> {
                baseD.dispose();
            });
            try {
                confirmBtn.addActionListener(e -> {
                    // baseP is opened
                    if (baseP != null) {
                        this.base = baseP;
                        if (baseP.newField.getText().equals(baseP.confirmField.getText()))
                        {
                            // Check if new field and confirm field = 
                            if (checkInvalid(flag))
                            {
                                updatePanel(user);
                                baseD.dispose();
                            }
                            else 
                            {
                                showInvalidMessage(flag);
                                /* Using JOptionPane will freeze the main frame
                                ** Set modal(true) in editDialog makes this problem
                                ** Solve 
                                */
                            }
                        } 
                        else 
                        {
                            showNotMatchMessage(flag);
                            // Using JOptionPane will freeze the main frame
                        }
                    }
                    // passwP is opened
                    else  {
                        if (new String(passwP.newPassw.getPassword())
                                .equals(new String(passwP.confirmNewPassw.getPassword()))) {
                            Database.updatePassword(new String(passwP.newPassw.getPassword()), user);
                            JOptionPane.showMessageDialog(null, "Updated password successfully.");
                            baseD.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Password does not match");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
