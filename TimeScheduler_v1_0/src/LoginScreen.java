package src;

import javax.swing.*;
// import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

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
                if (Database.existAdmin(usernamePanel.getUsername(), passwordPanel.getHashPassword())) {
                    User admin = Database.getAdmin(usernamePanel.getUsername(), passwordPanel.getHashPassword());
                    System.out.println("Login Success as Administrator");
                    dispose();
                    new MainFrame(admin);
                } else if (Database.existUser(usernamePanel.getUsername(), passwordPanel.getHashPassword())) {
                    User user = Database.getUser(usernamePanel.getUsername(), passwordPanel.getHashPassword());
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
    public String getHashPassword() throws RuntimeException {
        return Hash.hashPassword(new String(passwordField.getPassword()));
    }
}

// register JFrame as the application's main frame
class Register extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");
    private static JTextField usernameTextField = new JTextField(10);
    private static JPasswordField passwordField = new JPasswordField(10);

    Register() {
        setTitle("Register");
        setSize(350, 350);
        setPreferredSize(new Dimension(350, 350));
        setLocationRelativeTo(null);
        // setLayout(new FlowLayout());
        // flow vertival
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(icon.getImage());

        // username panel
        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username");
        JLabel usernameDescriptionField = new JLabel("a-Z, 0-9, 3-15 characters");
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);
        usernamePanel.add(usernameDescriptionField);

        // password panel
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password");
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // confirm password panel
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        JPasswordField confirmPasswordField = new JPasswordField(10);
        JPanel confirmPasswordPanel = new JPanel();
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(confirmPasswordField);

        // button panel
        JPanel buttonPanel = new JPanel();

        // register next button
        JButton registerNextButton = new JButton("Next");
        JPanel registerNextButtonPanel = new JPanel();
        registerNextButtonPanel.add(registerNextButton);
        registerNextButton.addActionListener(e -> {
            if (new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                System.out.println("Password match");
                if (RegisterValidator.isValidUsername(usernameTextField.getText())) {
                    System.out.println("Username is valid");
                    dispose();
                    new Register2();
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
        buttonPanel.add(registerBackButtonPanel);
        buttonPanel.add(registerNextButtonPanel);

        add(usernamePanel);
        add(passwordPanel);
        add(confirmPasswordPanel);
        add(buttonPanel);
        setVisible(true);
    }

    static String getUsername() {
        return usernameTextField.getText();
    }

    static String getPassword() throws RuntimeException {
        try {
            return Hash.hashPassword(new String(passwordField.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

// register JFrame as the application's next register frame
class Register2 extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");

    Register2() {
        setTitle("Register");
        setSize(350, 350);
        setPreferredSize(new Dimension(350, 350));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(icon.getImage());
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // name panel
        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Name");
        JLabel nameDescriptionLabel = new JLabel("0-30 characters");
        JTextField nameTextField = new JTextField(10);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        namePanel.add(nameDescriptionLabel);

        // email panel
        JPanel emailPanel = new JPanel();
        JLabel emailLabel = new JLabel("Email");
        JLabel emailDescriptionLabel = new JLabel("e.g abc@email.com");
        JTextField emailTextField = new JTextField(10);
        emailPanel.add(emailLabel);
        emailPanel.add(emailTextField);
        emailPanel.add(emailDescriptionLabel);

        // phone panel
        JPanel phonePanel = new JPanel();
        JLabel phoneLabel = new JLabel("Phone");
        JLabel phoneDescriptionLabel = new JLabel("e.g 0987654321, 10 digits");
        JTextField phoneTextField = new JTextField(10);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneTextField);
        phonePanel.add(phoneDescriptionLabel);

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
            System.out.println("Register Success");
            if (RegisterValidator.isValidName(nameTextField.getText())
                    && RegisterValidator.isValidEmail(emailTextField.getText())
                    && RegisterValidator.isValidPhone(phoneTextField.getText())) {
                User user = new User(Register.getUsername(), nameTextField.getText(),
                        emailTextField.getText(), phoneTextField.getText());
                System.out.println("Everything is valid");
                JOptionPane.showMessageDialog(null, "Register Success, close this window to login");
                if (Database.addTischUser(user.getUsername(), Register.getPassword(), user.getName(),
                        user.getEmail(), user.getPhone())) {
                    System.out.println("Register Success");
                } else {
                    System.out.println("Register Failed");
                }
                dispose();
                new LoginScreen();
            } else if (!RegisterValidator.isValidName(nameTextField.getText())) {
                System.out.println("Name is invalid");
                JOptionPane.showMessageDialog(null, "Name is invalid");
            } else if (!RegisterValidator.isValidEmail(emailTextField.getText())) {
                System.out.println("Email is invalid");
                JOptionPane.showMessageDialog(null, "Email is invalid");
            } else if (!RegisterValidator.isValidPhone(phoneTextField.getText())) {
                System.out.println("Phone is invalid");
                JOptionPane.showMessageDialog(null, "Phone is invalid");
            }
        });

        buttonPanel.add(backButtonPanel);
        buttonPanel.add(registerButtonPanel);
        add(namePanel);
        add(emailPanel);
        add(phonePanel);
        add(buttonPanel);
        setVisible(true);
    }
}
