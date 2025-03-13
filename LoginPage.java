import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginPage() {
        setTitle("Login Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 255));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 50, 120, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(150, 50, 160, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 90, 120, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 90, 160, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 130, 100, 30);
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                dispose();
                new AdminFrame();  // Open the AdminFrame on successful login
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
        setVisible(true);
    }

    private boolean validateLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }

    public static void main(String[] args) {
        new LoginPage();  // Launch the LoginPage
    }
}
