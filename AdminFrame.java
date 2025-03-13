import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminFrame extends JFrame {
    JTextField candidateNameField, partyField;
    JButton addButton, viewButton, backButton;

    public AdminFrame() {
        setTitle("Admin Dashboard");
        setSize(600, 400); // Increased frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 255));

        // Center alignment logic
        int panelWidth = 600;
        int panelHeight = 400;
        int centerX = panelWidth / 2;
        int yStart = 50;
        int spacing = 40;
        int buttonSpacing = 60; // Increased spacing between buttons
        int buttonWidth = 200; // Increased button width

        // Candidate Name Label and Field
        JLabel nameLabel = new JLabel("Candidate Name:");
        nameLabel.setBounds(centerX - 150, yStart, 120, 25);
        panel.add(nameLabel);

        candidateNameField = new JTextField(20);
        candidateNameField.setBounds(centerX, yStart, 160, 25);
        candidateNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(candidateNameField);

        // Party Label and Field
        JLabel partyLabel = new JLabel("Party:");
        partyLabel.setBounds(centerX - 150, yStart + spacing, 120, 25);
        panel.add(partyLabel);

        partyField = new JTextField(20);
        partyField.setBounds(centerX, yStart + spacing, 160, 25);
        partyField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(partyField);

        // Add Candidate Button
        addButton = new JButton("Add Candidate");
        addButton.setBounds(centerX - buttonWidth / 2, yStart + 2 * spacing + 20, buttonWidth, 30);
        addButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCandidate();
            }
        });
        panel.add(addButton);

        // View Candidates Button
        viewButton = new JButton("View Candidates");
        viewButton.setBounds(centerX - buttonWidth / 2, yStart + 2 * spacing + 20 + buttonSpacing, buttonWidth, 30);
        viewButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewButton.setBackground(new Color(70, 130, 180));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewCandidates();
            }
        });
        panel.add(viewButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(centerX - 50, yStart + 3 * spacing + 80, 100, 30);
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(200, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBackToDashboard();
            }
        });
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }

    // Method to add candidate to the database
    private void addCandidate() {
        String name = candidateNameField.getText();
        String party = partyField.getText();

        try (Connection conn = DBConnection.connect()) {
            String query = "INSERT INTO candidates (name, party) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, party);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Candidate added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to open ViewCandidatesFrame
    private void viewCandidates() {
        new ViewCandidatesFrame();
    }

    // Method to go back to AdminDashboard
    private void goBackToDashboard() {
        new AdminDashboard(); // Redirect to AdminDashboard
        dispose(); // Close the current frame
    }

    public static void main(String[] args) {
        new AdminFrame(); // Open AdminFrame
    }
}
