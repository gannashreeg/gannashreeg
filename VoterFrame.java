import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class VoterFrame extends JFrame {
    JComboBox<String> candidatesDropdown;
    JButton voteButton, closeButton;
    int voterId;

    public VoterFrame(int voterId) {
        this.voterId = voterId;

        setTitle("Voter Dashboard");
        setSize(600, 400); // Increased panel size for better centering
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 255));

        // Dimensions for centering components
        int panelWidth = 600;
        int componentWidth = 200;
        int centerX = panelWidth / 2 - componentWidth / 2;

        // Select Candidate Label
        JLabel selectLabel = new JLabel("Select Candidate:");
        selectLabel.setBounds(centerX - 50, 50, 150, 25); // Centered and adjusted size
        panel.add(selectLabel);

        // Candidate Dropdown with spacing from the label
        candidatesDropdown = new JComboBox<>();
        loadCandidates();
        candidatesDropdown.setBounds(centerX, 90, componentWidth, 25); // Added spacing below the label
        panel.add(candidatesDropdown);

        // Vote Button
        voteButton = new JButton("Vote");
        voteButton.setBounds(centerX, 150, componentWidth, 30); // Centered button
        voteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        voteButton.setBackground(new Color(70, 130, 180));
        voteButton.setForeground(Color.WHITE);
        voteButton.setFocusPainted(false);
        voteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        voteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                castVote();
            }
        });
        panel.add(voteButton);

        // Close Button
        closeButton = new JButton("Close");
        closeButton.setBounds(centerX, 210, componentWidth, 30); // Centered button
        closeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        closeButton.setBackground(new Color(200, 50, 50));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeApplication();
            }
        });
        panel.add(closeButton);

        add(panel);
        setVisible(true);
    }

    private void loadCandidates() {
        try (Connection conn = DBConnection.connect()) {
            String query = "SELECT id, name,party FROM candidates";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String candidateInfo = rs.getInt("id")  +": " + rs.getString("name")+" ("+rs.getString("party")+")";
                candidatesDropdown.addItem(candidateInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void castVote() {
        String selectedCandidate = (String) candidatesDropdown.getSelectedItem();
        int candidateId = Integer.parseInt(selectedCandidate.split(":")[0]);

        try (Connection conn = DBConnection.connect()) {
            // Check if the voter has already voted
            String checkQuery = "SELECT * FROM votes WHERE voter_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, voterId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "You have already voted!");
                return;
            }

            // Insert the vote
            String insertQuery = "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, voterId);
            insertStmt.setInt(2, candidateId);
            insertStmt.executeUpdate();

            // Update candidate's vote count
            String updateQuery = "UPDATE candidates SET votes = votes + 1 WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, candidateId);
            updateStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Vote successfully cast!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeApplication() {
        int confirmation = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to close?", "Confirm Exit", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            dispose(); // Close the frame
        }
    }
}
