import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(500, 400); // Increased window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with Background Color
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 255));

        // Center alignment logic
        int centerX = 500 / 2; // Width of the frame divided by 2
        int centerY = 400 / 2; // Height of the frame divided by 2
        int componentWidth = 160;
        int componentHeight = 25;
        int buttonWidth = 100;
        int buttonHeight = 30;
        int spacing = 40;

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(centerX - (componentWidth / 2) - 80, centerY - (spacing * 2), 100, componentHeight);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(centerX - (componentWidth / 2), centerY - (spacing * 2), componentWidth, componentHeight);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(usernameField);
         
        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(centerX - (componentWidth / 2) - 80, centerY - spacing, 100, componentHeight);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(centerX - (componentWidth / 2), centerY - spacing, componentWidth, componentHeight);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passwordField);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(centerX - (buttonWidth / 2), centerY + spacing, buttonWidth, buttonHeight);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticate(); // Call authenticate method
            }
        });
        panel.add(loginButton);

        // Add panel to the frame
        add(panel);
        setVisible(true);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.connect()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login successful! Role: " + role);

                if ("admin".equals(role)) {
                    new AdminDashboard(); // Navigate to Admin Dashboard
                } else {
                    new VoterFrame(rs.getInt("id")); // Navigate to Voter Dashboard
                }
                dispose(); // Close the login frame
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
