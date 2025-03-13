/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ganas
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ResultsFrame extends JFrame {
    JTextArea resultsTextArea;

    public ResultsFrame() {
        setTitle("Voting Results");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 255));

        // Results Text Area
        resultsTextArea = new JTextArea();
        resultsTextArea.setBounds(20, 50, 440, 250);
        resultsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultsTextArea.setEditable(false);
        panel.add(resultsTextArea);

        // Load Results Button
        JButton loadResultsButton = new JButton("Load Results");
        loadResultsButton.setBounds(150, 310, 200, 30);
        loadResultsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loadResultsButton.setBackground(new Color(70, 130, 180));
        loadResultsButton.setForeground(Color.WHITE);
        loadResultsButton.setFocusPainted(false);
        loadResultsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        loadResultsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadResults();
            }
        });
        panel.add(loadResultsButton);

        add(panel);
        setVisible(true);
    }

    private void loadResults() {
        StringBuilder results = new StringBuilder("Voting Results:\n\n");

        try (Connection conn = DBConnection.connect()) {
            String query = "SELECT name, party, votes FROM candidates";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.append(rs.getString("name"))
                       .append(" (")
                       .append(rs.getString("party"))
                       .append(") - ")
                       .append(rs.getInt("votes"))
                       .append(" votes\n");
            }

            resultsTextArea.setText(results.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
