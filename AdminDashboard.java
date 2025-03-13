import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    JButton adminFrameButton, resultsFrameButton;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400); // Increased window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 255));

        // Center alignment logic
        int centerX = 600 / 2; // Width of the frame divided by 2
        int centerY = 400 / 2; // Height of the frame divided by 2
        int buttonWidth = 200;
        int buttonHeight = 40;
        int spacing = 70;

        // Admin Frame Button
        adminFrameButton = new JButton("Admin Dashboard");
        adminFrameButton.setBounds(centerX - (buttonWidth / 2), centerY - spacing, buttonWidth, buttonHeight);
        adminFrameButton.setFont(new Font("Arial", Font.PLAIN, 16));
        adminFrameButton.setBackground(new Color(70, 130, 180));
        adminFrameButton.setForeground(Color.WHITE);
        adminFrameButton.setFocusPainted(false);
        adminFrameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        adminFrameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAdminFrame(); // Redirect to AdminFrame
            }
        });
        panel.add(adminFrameButton);

        // Results Frame Button
        resultsFrameButton = new JButton("View Results");
        resultsFrameButton.setBounds(centerX - (buttonWidth / 2), centerY + spacing - buttonHeight, buttonWidth, buttonHeight);
        resultsFrameButton.setFont(new Font("Arial", Font.PLAIN, 16));
        resultsFrameButton.setBackground(new Color(70, 130, 180));
        resultsFrameButton.setForeground(Color.WHITE);
        resultsFrameButton.setFocusPainted(false);
        resultsFrameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        resultsFrameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openResultsFrame(); // Redirect to ResultsFrame
            }
        });
        panel.add(resultsFrameButton);

        add(panel);
        setVisible(true);
    }

    // Open AdminFrame page
    private void openAdminFrame() {
        new AdminFrame();
        dispose(); // Close current dashboard
    }

    // Open ResultsFrame page
    private void openResultsFrame() {
        new ResultsFrame();
        dispose(); // Close current dashboard
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
