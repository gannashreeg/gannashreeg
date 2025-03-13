import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewCandidatesFrame extends JFrame {
    private JTable candidatesTable;
    private DefaultTableModel tableModel;

    public ViewCandidatesFrame() {
        setTitle("View Candidates");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 255));

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Party"}, 0);
        candidatesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(candidatesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(240, 240, 255));

        JButton deleteButton = new JButton("Delete Candidate");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteButton.addActionListener(e -> deleteSelectedCandidate());
        buttonsPanel.add(deleteButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(e -> dispose()); // Close current frame
        buttonsPanel.add(backButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        add(panel);
        loadCandidates();
        setVisible(true);
    }

    private void loadCandidates() {
        // Clear the existing table before loading new data
        tableModel.setRowCount(0);

        try (Connection conn = DBConnection.connect()) {
            String query = "SELECT * FROM candidates ORDER BY id"; // Fetch candidates in order
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("party")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedCandidate() {
        int selectedRow = candidatesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a candidate to delete.");
            return;
        }

        int candidateId = (int) tableModel.getValueAt(selectedRow, 0); // Get candidate ID

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this candidate and their votes?",
                "Delete Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.connect()) {
                // Step 1: Delete associated votes
                String deleteVotesQuery = "DELETE FROM votes WHERE candidate_id = ?";
                try (PreparedStatement deleteVotesStmt = conn.prepareStatement(deleteVotesQuery)) {
                    deleteVotesStmt.setInt(1, candidateId);
                    deleteVotesStmt.executeUpdate();
                }

                // Step 2: Delete the candidate
                String deleteCandidateQuery = "DELETE FROM candidates WHERE id = ?";
                try (PreparedStatement deleteCandidateStmt = conn.prepareStatement(deleteCandidateQuery)) {
                    deleteCandidateStmt.setInt(1, candidateId);
                    int rowsAffected = deleteCandidateStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Reset AUTO_INCREMENT after deletion to avoid gaps in IDs
                        resetAutoIncrement(conn);

                        JOptionPane.showMessageDialog(this, "Candidate and their votes deleted successfully!");
                        tableModel.removeRow(selectedRow); // Update the table view
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete candidate.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting candidate.");
            }
        }
    }

    private void resetAutoIncrement(Connection conn) {
        try {
            // Reset the auto-increment value to the max id + 1
            String resetAutoIncQuery = "SELECT MAX(id) FROM candidates";
            try (PreparedStatement stmt = conn.prepareStatement(resetAutoIncQuery);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    int maxId = rs.getInt(1);
                    // Set the next auto-increment value
                    String resetQuery = "ALTER TABLE candidates AUTO_INCREMENT = ?";
                    try (PreparedStatement resetStmt = conn.prepareStatement(resetQuery)) {
                        resetStmt.setInt(1, maxId + 1); // Set the next available ID
                        resetStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error resetting auto-increment.");
        }
    }
}
