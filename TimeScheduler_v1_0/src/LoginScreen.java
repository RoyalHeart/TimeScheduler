package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import src.database.Database;

/**
 * {@link LoginScreen} is used for user login.
 * 
 * @author Tam Thai Hoang
 */
public class LoginScreen extends JFrame {
    InputStream stream = this.getClass()
            .getResourceAsStream("/lib/TimeSchedulerIcon.png");
    InputStream stream2 = this.getClass().getResourceAsStream("/lib/TimeSchedulerIcon.png");
    private Username usernamePanel = new Username();
    private Password passwordPanel = new Password();
    private JPanel buttonPanel = new JPanel();
    private JPanel loginPanel = new JPanel();
    private User user = new User();
    private Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    private static GridBagConstraints gbc = new GridBagConstraints();

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

    /**
     * Contructor for {@link LoginScreen} Frame
     */
    LoginScreen() {
        try {
            Image image = ImageIO.read(stream);
            ImageIcon icon = new ImageIcon(image);
            this.setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Login");
        this.setSize(450, 300);
        this.setPreferredSize(new Dimension(450, 300));
        this.setMinimumSize(new Dimension(400, 300));
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.PINK);

        // login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(defaultFont);
        loginButton.setOpaque(false);
        try {
            loginButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, usernamePanel.getUsername() + passwordPanel.getPassword());
                if (Database.existAdmin(usernamePanel.getUsername(), passwordPanel.getPassword())) {
                    User admin = Database.getAdmin(usernamePanel.getUsername(), passwordPanel.getPassword());
                    System.out.println("Login Success as Administrator");
                    dispose();
                    new AdminInterface();
                } else if (Database.existUser(usernamePanel.getUsername(), passwordPanel.getPassword())) {
                    User user = Database.getUser(usernamePanel.getUsername(),
                            passwordPanel.getPassword());
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

        // register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(defaultFont);
        registerButton.setOpaque(false);
        registerButton.addActionListener(e -> {
            this.setVisible(false);
            this.dispose();
            new Register(user);
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.setOpaque(false);
        JLabel iconLabel = new JLabel();
        try {
            Image loginImage = ImageIO.read(stream2).getScaledInstance(200, 200, Image.SCALE_AREA_AVERAGING);
            Icon loginIcon = new ImageIcon(loginImage);
            iconLabel = new JLabel(loginIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.addComp(loginPanel, usernamePanel, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, 1);
        this.addComp(loginPanel, passwordPanel, 0, 1, 1, 1, GridBagConstraints.BOTH, 1, 1);
        this.addComp(loginPanel, buttonPanel, 0, 2, 1, 1, GridBagConstraints.BOTH, 1, 1);
        this.addComp(loginPanel, iconLabel, 1, 0, 1, 3, GridBagConstraints.BOTH, 0.4, 1);

        this.setContentPane(loginPanel);
        this.setVisible(true);
    }

    /**
     * {@code Username} panel for username input
     */
    class Username extends JPanel {
        private JTextField usernameField = new JTextField(10);
        Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        Username() {
            this.setOpaque(false);
            setLayout(new FlowLayout());
            JLabel usernameLabel = new JLabel("Username");
            usernameLabel.setFont(defaultFont);
            usernameField.setFont(defaultFont);
            add(usernameLabel);
            add(usernameField);
        }

        public String getUsername() {
            return usernameField.getText();
        }
    }

    /**
     * {@code Password} panel for password input
     */
    class Password extends JPanel {
        private JPasswordField passwordField = new JPasswordField(10);
        Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        Password() {
            this.setOpaque(false);
            setLayout(new FlowLayout());
            JLabel passwordLabel = new JLabel("Password");
            passwordLabel.setFont(defaultFont);
            passwordField.setFont(defaultFont);
            add(passwordLabel);
            add(passwordField);
        }

        // get password
        public String getPassword() throws RuntimeException {
            return new String(passwordField.getPassword());
        }
    }
}

/**
 * {@link Register} JFrame for registering new user with username and password
 * 
 * @author Tam Thai Hoang
 */
class Register extends JFrame {
    private ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");
    private static JTextField usernameTextField = new JTextField(10);
    private static JPasswordField passwordField = new JPasswordField(10);
    private static GridBagConstraints gbc = new GridBagConstraints();
    private static JPanel registerPanel = new JPanel(new GridBagLayout());
    private Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

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

    /**
     * {@code Register} constructor for creating a new register JFrame
     * 
     * @param user the user that is created by the register
     */
    Register(User user) {
        this.setTitle("Register");
        this.setSize(700, 500);
        this.setPreferredSize(new Dimension(700, 500));
        this.setMinimumSize(new Dimension(650, 350));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(icon.getImage());
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setOpaque(true);
        registerPanel.setBackground(Color.PINK);

        // username panel
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(defaultFont);
        JLabel usernameDescriptionLabel = new JLabel(
                "<html>a-Z & 0-9, 3-15 characters<br>Cannot contain spaces or special characters</html>");
        usernameDescriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        usernameDescriptionLabel.setAlignmentY(JTextField.TOP_ALIGNMENT);
        usernameDescriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        usernameTextField.setFont(defaultFont);
        this.addComp(registerPanel, usernameLabel, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.5, 0.33);
        this.addComp(registerPanel, usernameTextField, 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.333);
        this.addComp(registerPanel, usernameDescriptionLabel, 1, 1, 1, 1, GridBagConstraints.NONE, 1, 0.1);

        // password panel
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(defaultFont);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setFont(defaultFont);
        this.addComp(registerPanel, passwordLabel, 0, 2, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        this.addComp(registerPanel, passwordField, 1, 2, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.33);

        // confirm password panel
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(defaultFont);
        JPasswordField confirmPasswordField = new JPasswordField(10);
        confirmPasswordField.setFont(defaultFont);
        this.addComp(registerPanel, confirmPasswordLabel, 0, 3, 1, 1, GridBagConstraints.BOTH, 0.25, 0.33);
        this.addComp(registerPanel, confirmPasswordField, 1, 3, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0.33);

        // button panel
        JPanel buttonPanel = new JPanel();
        // register next button
        JButton registerNextButton = new JButton("Next");
        registerNextButton.setFont(defaultFont);
        registerNextButton.setOpaque(false);
        JPanel registerNextButtonPanel = new JPanel();
        registerNextButtonPanel.setOpaque(false);
        registerNextButton.addActionListener(e -> {
            // check if password is empty
            if (!new String(passwordField.getPassword()).equals("")) {
                // check password == confirm password
                if (new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                    System.out.println("Password match");
                    // check if username is valid
                    if (RegisterValidator.isValidUsername(usernameTextField.getText())) {
                        System.out.println("Username is valid");
                        user.setUsername(usernameTextField.getText());
                        // check if username is already exist
                        if (!Database.existUsername(user.getUsername())) {
                            this.setVisible(false);
                            this.dispose();
                            new RegisterInfo(user);
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
            } else {
                JOptionPane.showMessageDialog(null, "Password cannot be empty");
            }
        });
        registerNextButtonPanel.add(registerNextButton);

        // back button
        JButton registerBackButton = new JButton("Back");
        registerBackButton.setFont(defaultFont);
        registerBackButton.setOpaque(false);
        JPanel registerBackButtonPanel = new JPanel();
        registerBackButtonPanel.setOpaque(false);
        registerBackButton.addActionListener(e -> {
            this.setVisible(false);
            this.dispose();
            new LoginScreen();
        });

        JLabel iconLabel = new JLabel();
        try {
            Image loginImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_AREA_AVERAGING);
            Icon loginIcon = new ImageIcon(loginImage);
            iconLabel = new JLabel(loginIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.addComp(registerPanel, iconLabel, 2, 0, 1, 4, GridBagConstraints.BOTH, 1, 1);

        registerBackButtonPanel.add(registerBackButton);
        buttonPanel.add(registerBackButtonPanel);
        buttonPanel.add(registerNextButtonPanel);
        buttonPanel.setOpaque(false);
        this.addComp(registerPanel, buttonPanel, 1, 4, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 1);
        this.setContentPane(registerPanel);
        this.setVisible(true);
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

/**
 * Second register JFrame to input name, email, phone number
 * 
 * @author Tam Thai Hoang
 */
class RegisterInfo extends JFrame {
    private ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");
    private static GridBagConstraints gbc = new GridBagConstraints();
    private static JPanel registerInfoPanel = new JPanel(new GridBagLayout());
    private Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

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

    /**
     * Constructor for RegisterInfo class
     * 
     * @param user user object from the Register JFrame
     */
    RegisterInfo(User user) {
        registerInfoPanel = new JPanel(new GridBagLayout());
        registerInfoPanel.setBackground(Color.PINK);
        this.setTitle("Register Info");
        this.setSize(700, 600);
        this.setPreferredSize(new Dimension(700, 600));
        this.setMinimumSize(new Dimension(630, 500));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(icon.getImage());

        // name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(defaultFont);
        JLabel nameDescriptionLabel = new JLabel(
                "<html>0-30 characters, no spaces or special characters allowed. <br>This field is optional. <br>This name is your display name and will be used when exporting or reminding events.</html>");

        JTextField nameTextField = new JTextField(10);
        nameTextField.setFont(defaultFont);
        this.addComp(registerInfoPanel, nameLabel, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.25, 1);
        this.addComp(registerInfoPanel, nameTextField, 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 1);
        this.addComp(registerInfoPanel, nameDescriptionLabel, 1, 1, 1, 1, GridBagConstraints.BOTH, 1, 1);

        // email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(defaultFont);
        JLabel emailDescriptionLabel = new JLabel(
                "<html>e.g abc@email.com.<br> *This is a required field.<br> This email will be used to send you event reminder.</html>");
        JTextField emailTextField = new JTextField(10);
        emailTextField.setFont(defaultFont);
        this.addComp(registerInfoPanel, emailLabel, 0, 2, 1, 1, GridBagConstraints.BOTH, 0.25, 1);
        this.addComp(registerInfoPanel, emailTextField, 1, 2, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 1);
        this.addComp(registerInfoPanel, emailDescriptionLabel, 1, 3, 1, 1, GridBagConstraints.BOTH, 1, 1);

        // verify email
        JButton verifyButton = new JButton("Send verify code");
        verifyButton.setFont(defaultFont);
        verifyButton.setOpaque(false);
        JLabel verifyEmailLabel = new JLabel("Verify Email");
        verifyEmailLabel.setFont(defaultFont);
        JTextField verifyEmailTextField = new JTextField(10);
        verifyEmailTextField.setFont(defaultFont);
        this.addComp(registerInfoPanel, verifyButton, 2, 4, 1, 1, GridBagConstraints.BOTH, 0.25, 1);
        this.addComp(registerInfoPanel, verifyEmailLabel, 0, 4, 1, 1, GridBagConstraints.BOTH, 0.5, 1);
        this.addComp(registerInfoPanel, verifyEmailTextField, 1, 4, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1);
        verifyButton.addActionListener(e -> {
            // send verify code to email
            String email = emailTextField.getText();
            user.setEmail(email);
            if (RegisterValidator.isValidEmail(email)) {
                System.out.println("Email is valid");
                // send verify code to user email
                new Thread(new Runnable() {
                    public void run() {
                        Mail.sendVerifyCode(user);
                    }
                }).start();
                JOptionPane.showMessageDialog(null, "Verification code has been sent to your email");
            } else {
                System.out.println("Email is not valid");
                JOptionPane.showMessageDialog(null, "Email is not valid");
            }
        });

        // phone
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setFont(defaultFont);
        JLabel phoneDescriptionLabel = new JLabel(
                "<html>e.g 0987654321, 8-10 digits.<br> This field is optional.<br> This phone number will be used if other users want to contact you.</html>");
        JTextField phoneTextField = new JTextField(10);
        phoneTextField.setFont(defaultFont);
        this.addComp(registerInfoPanel, phoneLabel, 0, 5, 1, 1, GridBagConstraints.BOTH, 0.25, 1);
        this.addComp(registerInfoPanel, phoneTextField, 1, 5, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 1);
        this.addComp(registerInfoPanel, phoneDescriptionLabel, 1, 6, 1, 1, GridBagConstraints.BOTH, 1, 1);

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // back button
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.setFont(defaultFont);
        backButton.setOpaque(false);
        backButton.addActionListener(e -> {
            this.setVisible(false);
            this.dispose();
            new Register(user);
        });
        backButtonPanel.add(backButton);

        // register button
        JPanel registerButtonPanel = new JPanel();
        registerButtonPanel.setOpaque(false);
        JButton registerButton = new JButton("Register");
        registerButton.setFont(defaultFont);
        registerButton.setOpaque(false);
        registerButtonPanel.add(registerButton);

        // check validitiy of the inputs
        registerButton.addActionListener(e -> {
            // check if name, email and phone are valid
            if (RegisterValidator.isValidName(nameTextField.getText())
                    && RegisterValidator.isValidEmail(emailTextField.getText())
                    && RegisterValidator.isValidPhone(phoneTextField.getText())
                    && Mail.getVerifyCode().equals(verifyEmailTextField.getText())
                    && Mail.getEmailVerified().equals(emailTextField.getText())) {
                user.setName(nameTextField.getText());
                user.setEmail(emailTextField.getText());
                user.setPhone(phoneTextField.getText());
                System.out.println("Everything is valid");
                // check if the user is added successfully
                if (Database.addUser(user.getUsername(), Register.getPassword(), user.getName(),
                        user.getEmail(), user.getPhone())) {
                    JOptionPane.showMessageDialog(null, "Register Success, close this window to login");
                    System.out.println("Register Success");
                    this.setVisible(false);
                    this.dispose();
                    new LoginScreen();
                } else {
                    JOptionPane.showMessageDialog(null, "Register Fail, try again");
                    System.out.println("Register Failed");
                }

            } else if (!RegisterValidator.isValidName(nameTextField.getText())) { // name is not valid
                System.out.println("Name is invalid");
                JOptionPane.showMessageDialog(null, "Name is invalid");
            } else if (!RegisterValidator.isValidEmail(emailTextField.getText())) { // email is not valid
                System.out.println("Email is invalid");
                JOptionPane.showMessageDialog(null, "Email is invalid");
            } else if (!RegisterValidator.isValidPhone(phoneTextField.getText())) { // phone is not valid
                System.out.println("Phone is invalid");
                JOptionPane.showMessageDialog(null, "Phone is invalid");
            } else if (!Mail.getVerifyCode().equals(verifyEmailTextField.getText())) {
                System.out.println("Verify code is invalid");
                JOptionPane.showMessageDialog(null, "Verify code is invalid");
                Mail.setVerifyCode(Mail.generateVerifyCode());
            } else if (!Mail.getEmailVerified().equals(emailTextField.getText())) {
                System.out.println("Email is not verified");
                JOptionPane.showMessageDialog(null, "Email is not verified");
            } else {
                System.out.println("Something is wrong");
                JOptionPane.showMessageDialog(null, "Something is wrong");
            }
        });

        JLabel iconLabel = new JLabel();
        try {
            Image loginImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_AREA_AVERAGING);
            Icon loginIcon = new ImageIcon(loginImage);
            iconLabel = new JLabel(loginIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.addComp(registerInfoPanel, iconLabel, 2, 0, 1, 4, GridBagConstraints.BOTH, 1, 1);
        buttonPanel.add(backButtonPanel);
        buttonPanel.add(registerButtonPanel);
        this.addComp(registerInfoPanel, buttonPanel, 1, 7, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1);
        this.setContentPane(registerInfoPanel);
        this.setVisible(true);
    }
}
