package src;

// import java.util.*;
import java.awt.*;

// import java.awt.event.*;
import javax.swing.*;

import src.database.Database;

/**
 * {@code Profile} is used to display the user properties like username, email
 * and phone number.
 * User can also edit their properties in this class too.
 * 
 * <p>
 * This class contains:
 * <p>
 * - A panel to display all user's properties
 * <p>
 * - A label for username
 * <p>
 * - A label for email addr
 * <p>
 * - A label for phone number
 * <p>
 * - A button to change password
 * <p>
 * - A User
 * 
 * @author Sang Doan Tan 1370137 (edit user's properties)
 * @author Tam Thai Hoang 1370674 (display properties of user)
 */
public class Profile extends JPanel {
    JPanel panel = new JPanel();
    JLabel nameLabel = new JLabel("Name: ");
    JLabel emailLabel = new JLabel("Email: ");
    JLabel phoneLabel = new JLabel("Phone: ");
    JButton chPasswBtn = new JButton("Change Password");
    JPanel editPanel = new JPanel();
    User user;

    /**
     * Constructor that creates {@code Profile} object.
     * 
     * @param user the user that logins in to the app
     */
    Profile(User user) {
        this.user = user;
        this.setLayout(new BorderLayout());
        this.setSize(300, 200);
        this.setVisible(true);
        JLabel label = new JLabel("Profile");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(3, 0));
        panel.add(new NamePanel(user));
        panel.add(new EmailPanel(user));
        panel.add(new PhonePanel(user));
        editPanel.setLayout(new FlowLayout());
        editPanel.add(chPasswBtn);
        this.add(panel, BorderLayout.CENTER);
        this.add(editPanel, BorderLayout.SOUTH);

        try {
            chPasswBtn.addActionListener(e -> {
                // Create JOptionPane with passwordfield
                JPasswordField pf = new JPasswordField();
                String[] options = { "OK", "Cancel" };
                // If you want to focus first on passwordfield when open JOptionPane, you must
                // use your own option type
                int option = JOptionPane.showOptionDialog(null, pf, "Enter password",
                        JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, pf);
                if (option == 0) {
                    if (Database.existUser(user.getUsername(), new String(pf.getPassword()))) {
                        new PasswDialog(new PasswEditPanel());
                    } else {
                        JOptionPane.showMessageDialog(null, "Password is wrong.");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Repaint again the display panel when updated user's properties successfully.
     * 
     * @param user user that need to change information
     */
    void updatePanel(User user) {
        panel.removeAll();

        panel.add(new NamePanel(user));
        panel.add(new EmailPanel(user));
        panel.add(new PhonePanel(user));
        panel.revalidate();
        panel.repaint();
    }

    /**
     * {@code NamePanel} is used to contain name label and edit button for name.
     * <p>
     * This class contains:
     * <p>
     * - A name label to display name
     * <p>
     * - A button to edit name
     * <p>
     * - An edit dialog
     * 
     * @author Sang Doan Tan 1370137
     */
    class NamePanel extends JPanel {
        JButton nameEditBtn = new JButton("Edit");
        JLabel name = new JLabel(user.getName());
        EditDialog edit;

        /**
         * Constructor that creates {@code NamePanel} object.
         * 
         * @param user the user that need to change information
         */
        NamePanel(User user) {
            name = new JLabel(user.getName());
            this.setLayout(new FlowLayout());
            this.add(nameLabel);
            this.add(name);
            this.add(nameEditBtn);
            try {
                nameEditBtn.addActionListener(e -> {
                    if (edit == null || !edit.isShowing()) {
                        edit = new EditDialog(new NameEditPanel(), 0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@code EmailPanel} is used to contain the email label and edit button for
     * email.
     * <p>
     * This class contains:
     * <p>
     * - A label to display email
     * <p>
     * - A button to edit email
     * <p>
     * - An edit dialog
     * 
     * @author Sang Doan Tan 1370137
     */
    class EmailPanel extends JPanel {
        JButton emailEditBtn = new JButton("Edit");
        JLabel email = new JLabel(user.getEmail());
        EditDialog edit;

        /**
         * Constructor that creates {@code EmailPanel} object.
         * 
         * @param user the user that need to change information
         */
        EmailPanel(User user) {
            email = new JLabel(user.getEmail());
            this.setLayout(new FlowLayout());
            this.add(emailLabel);
            this.add(email);
            this.add(emailEditBtn);
            try {
                emailEditBtn.addActionListener(e -> {
                    if (edit == null || !edit.isShowing()) {
                        edit = new EditDialog(new EmailEditPanel(), 1);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@code PhonePanel} is used to contain the phone number label and edit button
     * for phone number.
     * 
     * <p>
     * This class contains:
     * <p>
     * - A label for phone number
     * <p>
     * - A button to edit phone number
     * <p>
     * - An edit dialog
     * 
     * @author Sang Doan Tan 1370137
     * 
     */
    class PhonePanel extends JPanel {
        JButton phoneEditButton = new JButton("Edit");
        JLabel phone = new JLabel(user.getPhone());
        EditDialog edit;

        /**
         * Constructor that creates {@code PhonePanel} object.
         * 
         * @param user The user that need to change information
         */
        PhonePanel(User user) {
            phone = new JLabel(user.getPhone());
            this.setLayout(new FlowLayout());
            this.add(phoneLabel);
            this.add(phone);
            this.add(phoneEditButton);
            try {
                phoneEditButton.addActionListener(e -> {
                    if (edit == null || !edit.isShowing()) {
                        edit = new EditDialog(new PhoneEditPanel(), 2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // base dialog for edit name, email, phone, password...
    /**
     * {@code BaseDialog} is used as the parent class for {@link EditDialog} and for
     * {@link PasswDialog}
     * so they will have the same setting for dialog.
     * 
     * @author Sang Doan Tan 1370137
     */
    class BaseDialog extends JDialog {
        /**
         * Constructor that creates {@code BaseDialog} object.
         */
        BaseDialog() {
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
    /**
     * {@code EditDialog} is used to contain the {@link BasePanel} and the
     * {@link BtnPanel}
     * 
     * @author Sang Doan Tan 1370137
     */
    class EditDialog extends BaseDialog {
        /**
         * Constructor that creates {@code EditDialog} object.
         * 
         * @param edit the edit panel of username, phone or email
         * @param flag used to determine what information is going to change
         */
        EditDialog(BasePanel edit, int flag) {
            this.add(edit, BorderLayout.CENTER);
            this.add(new BtnPanel(this, edit, flag, null), BorderLayout.SOUTH);
        }
    }

    // dialog for edit password
    /**
     * {@code PasswDialog} is used to contain to {@link PasswEditPanel} and the
     * {@link BtnPanel}
     */
    class PasswDialog extends BaseDialog {
        /**
         * Constructor that creates {@code PasswDialog} object.
         * 
         * @param passwEdit the panel for editing password.
         */
        PasswDialog(PasswEditPanel passwEdit) {
            this.add(passwEdit, BorderLayout.CENTER);
            this.add(new BtnPanel(this, null, 3, passwEdit), BorderLayout.SOUTH);
        }
    }

    // base panel for edit panel
    /**
     * {@code BasePanel} is used as the parent class for {@link NameEditPanel},
     * {@link EmailEditPanel} and {@link PhoneEditPanel}
     * <p>
     * This class contains:
     * <p>
     * - A {@code JLabel} for new information
     * <p>
     * - A {@code JLabel} for confirm new information
     * <p>
     * - A {@code JTextField} to enter new information
     * <p>
     * - A {@code JTextField} to confirm new information
     * <p>
     * - A {@code JLabel} for current information
     * <p>
     * - A {@code JLabel} to display current information
     * 
     * @author Sang Doan Tan 1370137
     */
    class BasePanel extends JPanel {
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

        BasePanel() {
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

    /**
     * {@code NameEditPanel} is inherited from {@link BasePanel} to change the name
     * of {@link User}
     * 
     * @author Sang Doan Tan 1370137
     */
    class NameEditPanel extends BasePanel {
        NameEditPanel() {
            currentLabel.setText("Current name: ");
            currentDisplayLabel.setText(user.getName());
            newLabel.setText("New name: ");
            confirmLabel.setText("Confirm new name: ");
        }
    }

    /**
     * {@code EmailEditPanel} is inherited from {@link BasePanel} to change the
     * email of {@link User}
     * 
     * @author Sang Doan Tan 1370137
     */
    class EmailEditPanel extends BasePanel {
        EmailEditPanel() {
            currentLabel.setText("Current email address: ");
            currentDisplayLabel.setText(user.getEmail());
            newLabel.setText("New email address: ");
            confirmLabel.setText("Confirm email address: ");
        }
    }

    /**
     * {@code PhoneEditPanel} is inherited from {@link BasePanel} to change the
     * phone number of {@link User}
     * 
     * @author Sang Doan Tan 1370137
     */
    class PhoneEditPanel extends BasePanel {
        PhoneEditPanel() {
            currentLabel.setText("Current phone number: ");
            currentDisplayLabel.setText(user.getPhone());
            newLabel.setText("New phone number: ");
            confirmLabel.setText("Confirm new phone number: ");
        }
    }

    // panel for change password
    /**
     * {@code PassWEditPanel} is used to contain label and password field that
     * needed to change the password of {@code User}
     * <p>
     * This class contains:
     * <p>
     * - A label for new password
     * <p>
     * - A label for confirm new password
     * <p>
     * - A password field for new password
     * <p>
     * - A password field for confirm new password
     * 
     * @author Sang Doan Tan 1370137
     */
    class PasswEditPanel extends JPanel {
        // label for new password
        JLabel newLabel = new JLabel("New Password: ");
        // label for confirm new password
        JLabel confirmLabel = new JLabel("Confirm new password: ");
        // password field for new password
        JPasswordField newPassw = new JPasswordField(20);
        // password field for confirm new password
        JPasswordField confirmNewPassw = new JPasswordField(20);

        GridBagConstraints gbc = new GridBagConstraints();

        PasswEditPanel() {
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
    /**
     * {@code BtnPanel} is used to contain the cancel button and confirm button when
     * editing user's informaiton
     * <p>
     * This class contains:
     * <p>
     * - A {@code JButton} cancelBtn to cancel edit information
     * <p>
     * - A {@code JButton} confirmBtn to confirm edit information
     * <p>
     * - A {@code String[]} to display name, email and phone
     * <p>
     * - A {@link BasePanel} to access information from {@code BasePanel}
     * 
     * @author Sang Doan Tan 1370137
     */
    class BtnPanel extends JPanel {
        JButton cancelBtn = new JButton("Cancel");
        JButton confirmBtn = new JButton("Confirm");
        // String array to easy display message dialog
        String[] arr = { "Name", "Email", "Phone" };
        // Variable to reference to BasePanel
        BasePanel base;

        void showNotMatchMessage(int flag) {
            String showString = arr[flag] + " does not match.";
            JOptionPane.showMessageDialog(null, showString);
        }

        void showInvalidMessage(int flag) {
            String showString = arr[flag] + " is invalid.";
            JOptionPane.showMessageDialog(null, showString);
        }

        // Function to check validation of name, email and phone
        /**
         * Check if name, email or phone matches the condition
         * 
         * @param flag determines name, email or phone
         * @return true if matches,
         *         false if does not match
         */
        boolean checkInvalid(int flag) {
            switch (flag) {
                case 0:
                    if (RegisterValidator.isValidName(base.newField.getText())) {
                        user.setName(base.newField.getText());
                        Database.updateName(base.newField.getText(), user);
                        JOptionPane.showMessageDialog(null, "Updated name successfully.");
                        return true;
                    }

                case 1:
                    if (RegisterValidator.isValidEmail(base.newField.getText())) {
                        // String code = JOptionPane.showInputDialog(null, "Enter verifed code");

                        user.setEmail(base.newField.getText());
                        Database.updateEmail(base.newField.getText(), user);
                        JOptionPane.showMessageDialog(null, "Updated email successfully.");
                        return true;
                    }

                case 2:
                    if (RegisterValidator.isValidPhone(base.newField.getText())) {
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
        /**
         * Constructor that creates {@code BtnPanel} object.
         * 
         * @param baseD  is the parent class for edit dialog and password dialog
         * @param baseP  determines if edit panel is opened
         * @param flag   determines name, email or phone is being edited
         * @param passwP determines if password edit panel is opened
         * 
         * @author Sang Doan Tan 1370137
         */
        BtnPanel(BaseDialog baseD, BasePanel baseP, int flag, PasswEditPanel passwP) {
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
                        if (baseP.newField.getText().equals(baseP.confirmField.getText())) {
                            // Check if new field and confirm field =
                            if (checkInvalid(flag)) {
                                updatePanel(user);
                                baseD.dispose();
                            } else {
                                showInvalidMessage(flag);
                                /*
                                 * Using JOptionPane will freeze the main frame
                                 ** Set modal(true) in editDialog makes this problem
                                 ** Solve
                                 */
                            }
                        } else {
                            showNotMatchMessage(flag);
                            // Using JOptionPane will freeze the main frame
                        }
                    }
                    // passwP is opened
                    else {
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
