package src;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
// import java.awt.*;

// login JFrame as the application's login frame
public class LoginScreen extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");

    LoginScreen() {
        setTitle("Login");
        setSize(300, 200);
        setPreferredSize(new Dimension(300, 200));
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(icon.getImage());
        Username usernamePanel = new Username();
        Password passwordPanel = new Password();
        JPanel panel = new JPanel();
        JButton loginButton = new JButton("Login");
        try {
            loginButton.addActionListener(e -> {
                if (Database.existAdmin(usernamePanel.getUsername(),
                        Hash.hashPassword(passwordPanel.getPassword() + usernamePanel.getUsername()))) {
                    User admin = Database.getAdmin(usernamePanel.getUsername(),
                            Hash.hashPassword(passwordPanel.getPassword() + usernamePanel.getUsername()));
                    System.out.println("Login Success as Administrator");
                    dispose();
                    new MainFrame(admin);
                } else if (Database.existUser(usernamePanel.getUsername(),
                        Hash.hashPassword(passwordPanel.getPassword() + usernamePanel.getUsername()))) {
                    User user = Database.getUser(usernamePanel.getUsername(),
                            Hash.hashPassword(passwordPanel.getPassword() + usernamePanel.getUsername()));
                    System.out.println("Login Success");
                    dispose();
                    new MainFrame(user);
                } else {
                    JOptionPane.showMessageDialog(null, "Username or Password is incorrect");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            dispose();
            new Register();
        });
        panel.add(loginButton);
        panel.add(registerButton);
        add(usernamePanel);
        add(passwordPanel);
        add(panel);
        setVisible(true);
    }
}

class Username extends JPanel {
    private JTextField usernameField = new JTextField(10);

    Username() {
        setLayout(new FlowLayout());
        JLabel usernameLabel = new JLabel("Username");
        // JTextField usernameField = new JTextField(20);
        add(usernameLabel);
        add(usernameField);
    }

    public String getUsername() {
        return usernameField.getText();
    }
}

class Password extends JPanel {
    private JPasswordField passwordField = new JPasswordField(10);

    Password() {
        setLayout(new FlowLayout());
        JLabel passwordLabel = new JLabel("Password");
        add(passwordLabel);
        add(passwordField);
    }

    // get password
    public String getPassword() throws RuntimeException {
        return new String(passwordField.getPassword());
    }
}

// Register JFrame for registering new user with username and password
class Register extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");
    private static JTextField usernameTextField = new JTextField(10);
    private static JPasswordField passwordField = new JPasswordField(10);
    private static GridBagConstraints gbc = new GridBagConstraints();
    private static JPanel registerPanel = new JPanel(new GridBagLayout());

    private void addComp(JPanel panel, JComponent comp, int x, int y, int gWidth, int gHeight, int fill, double weightx,
            double weighty) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = gWidth;
        gbc.gridheight = gHeight;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        panel.add(comp, gbc);
    }

    Register() {

        setTitle("Register");
        setSize(400, 200);
        setPreferredSize(new Dimension(400, 200));
        setMinimumSize(new Dimension(400, 200));
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(icon.getImage());

        // username panel
        JLabel usernameLabel = new JLabel("Username");
        JLabel usernameDescriptionField = new JLabel("a-Z, 0-9, 3-15 characters");
        addComp(registerPanel, usernameLabel, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        addComp(registerPanel, usernameTextField, 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0.4, 0.333);
        addComp(registerPanel, usernameDescriptionField, 2, 0, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);

        // password panel
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addComp(registerPanel, passwordLabel, 0, 1, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        addComp(registerPanel, passwordField, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.33);

        // confirm password panel
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        JPasswordField confirmPasswordField = new JPasswordField(10);
        addComp(registerPanel, confirmPasswordLabel, 0, 2, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        addComp(registerPanel, confirmPasswordField, 1, 2, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.33);

        // button panel
        // register next button
        JButton registerNextButton = new JButton("Next");
        JPanel registerNextButtonPanel = new JPanel();
        registerNextButtonPanel.add(registerNextButton);
        registerNextButton.addActionListener(e -> {
            // check password == confirm password
            if (new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                System.out.println("Password match");
                // check if username is valid
                if (RegisterValidator.isValidUsername(usernameTextField.getText())) {
                    System.out.println("Username is valid");
                    // check if username is already exist
                    if (!Database.existUser(usernameTextField.getText(),
                            Hash.hashPassword(new String(passwordField.getPassword()) + usernameTextField.getText()))) {
                        dispose();
                        new RegisterInfo();
                    } else {
                        JOptionPane.showMessageDialog(null, "Username already exists");
                    }
                } else {
                    System.out.println("Username is not valid");
                    JOptionPane.showMessageDialog(null, "Username is not valid");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Password does not match");
            }
        });

        // back button
        JButton registerBackButton = new JButton("Back");
        JPanel registerBackButtonPanel = new JPanel();
        registerBackButtonPanel.add(registerBackButton);
        registerBackButton.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        addComp(registerPanel, registerBackButton, 0, 3, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 1);
        addComp(registerPanel, registerNextButton, 2, 3, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 1);
        setContentPane(registerPanel);
        setVisible(true);
    }

    static String getUsername() {
        return usernameTextField.getText();
    }

    static String getPassword() throws RuntimeException {
        try {
            // hash(password + username), username is the salt
            return Hash.hashPassword(new String(passwordField.getPassword()) + getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Second register JFrame to input name, email, phone number
class RegisterInfo extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");
    private static GridBagConstraints gbc = new GridBagConstraints();
    private static JPanel registerInfoPanel = new JPanel(new GridBagLayout());

    private void addComp(JPanel panel, JComponent comp, int x, int y, int gWidth, int gHeight, int fill, double weightx,
            double weighty) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = gWidth;
        gbc.gridheight = gHeight;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        panel.add(comp, gbc);
    }

    RegisterInfo() {
        setTitle("Register Info");
        setSize(400, 200);
        setPreferredSize(new Dimension(400, 200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(icon.getImage());
        setLayout(new GridBagLayout());

        // name
        JLabel nameLabel = new JLabel("Name");
        JLabel nameDescriptionLabel = new JLabel("0-30 characters");
        JTextField nameTextField = new JTextField(10);
        addComp(registerInfoPanel, nameLabel, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        addComp(registerInfoPanel, nameTextField, 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.33);
        addComp(registerInfoPanel, nameDescriptionLabel, 2, 0, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);

        // email
        JLabel emailLabel = new JLabel("Email");
        JLabel emailDescriptionLabel = new JLabel("e.g abc@email.com");
        JTextField emailTextField = new JTextField(10);
        addComp(registerInfoPanel, emailLabel, 0, 1, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        addComp(registerInfoPanel, emailTextField, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.33);
        addComp(registerInfoPanel, emailDescriptionLabel, 2, 1, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);

        // phone
        JLabel phoneLabel = new JLabel("Phone");
        JLabel phoneDescriptionLabel = new JLabel("e.g 0987654321, 10 digits");
        JTextField phoneTextField = new JTextField(10);
        addComp(registerInfoPanel, phoneLabel, 0, 2, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        addComp(registerInfoPanel, phoneTextField, 1, 2, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.33);
        addComp(registerInfoPanel, phoneDescriptionLabel, 2, 2, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);

        // button panel
        JPanel buttonPanel = new JPanel();

        // back button
        JPanel backButtonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButtonPanel.add(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new Register();
        });

        // register button
        JPanel registerButtonPanel = new JPanel();
        JButton registerButton = new JButton("Register");
        registerButtonPanel.add(registerButton);

        // check validitiy of the inputs
        registerButton.addActionListener(e -> {
            // check if name, email and phone are valid
            if (RegisterValidator.isValidName(nameTextField.getText())
                    && RegisterValidator.isValidEmail(emailTextField.getText())
                    && RegisterValidator.isValidPhone(phoneTextField.getText())) {
                User user = new User(Register.getUsername(), nameTextField.getText(),
                        emailTextField.getText(), phoneTextField.getText());
                System.out.println("Everything is valid");
                JOptionPane.showMessageDialog(null, "Register Success, close this window to login");
                // check if the user is added successfully
                if (Database.addUser(user.getUsername(), Register.getPassword(), user.getName(),
                        user.getEmail(), user.getPhone())) {
                    System.out.println("Register Success");
                } else {
                    System.out.println("Register Failed");
                }
                dispose();
                new LoginScreen();
            } else if (!RegisterValidator.isValidName(nameTextField.getText())) { // name is not valid
                System.out.println("Name is invalid");
                JOptionPane.showMessageDialog(null, "Name is invalid");
            } else if (!RegisterValidator.isValidEmail(emailTextField.getText())) { // email is not valid
                System.out.println("Email is invalid");
                JOptionPane.showMessageDialog(null, "Email is invalid");
            } else if (!RegisterValidator.isValidPhone(phoneTextField.getText())) { // phone is not valid
                System.out.println("Phone is invalid");
                JOptionPane.showMessageDialog(null, "Phone is invalid");
            }
        });

        buttonPanel.add(backButtonPanel);
        buttonPanel.add(registerButtonPanel);
        addComp(registerInfoPanel, buttonPanel, 1, 3, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1);
        setContentPane(registerInfoPanel);
        setVisible(true);
    }
}
