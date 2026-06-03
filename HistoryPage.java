// Creator: Putra Akmal
// Tester: Putra Akmal
// Description: Displays quiz history for the current user by reading QuizScores.txt.

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.util.*;

public class HistoryPage implements IHistoryDisplay {

    private JFrame frame;
    private JTextArea historyArea;
    private String userName;
    private Home parent;
    private static final String SCORE_FILE = "QuizScores.txt";

    public HistoryPage(String userName) {
        this.userName = userName;

        frame = new JFrame("Quiz History");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 248, 252));

        JLabel title = new JLabel("📋 Quiz History", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(30, 120, 90));
        title.setBounds(0, 25, 360, 40);
        panel.add(title);

        JLabel userLabel = new JLabel("User: " + userName, SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        userLabel.setBounds(0, 62, 360, 25);
        panel.add(userLabel);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBounds(25, 100, 310, 405);
        scrollPane.setBorder(new LineBorder(new Color(200, 220, 210), 1, true));
        panel.add(scrollPane);

        JButton homeButton = new JButton("Back to Home");
        homeButton.setBounds(95, 525, 170, 38);
        homeButton.setBackground(new Color(30, 120, 90));
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(e -> showHome());
        panel.add(homeButton);

        frame.add(panel);
        displayHistory(userName);
    }

    public void setParent(Home h) {
        this.parent = h;
    }

    @Override
    public void displayHistory(String userName) {
        ArrayList<String> history = readUserHistory(userName);

        if (history.isEmpty()) {
            historyArea.setText("No quiz history found for " + userName + ".");
        } else {
            historyArea.setText("Quiz attempts for " + userName + ":\n");
            historyArea.append("--------------------------------\n");

            for (String line : history) {
                historyArea.append(line + "\n");
            }
        }

        frame.setVisible(true);
    }

    private ArrayList<String> readUserHistory(String userName) {
        ArrayList<String> history = new ArrayList<>();

        try {
            File file = new File(SCORE_FILE);

            if (!file.exists()) {
                return history;
            }

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.startsWith("User: " + userName + " |")) {
                    history.add(line);
                }
            }

            scanner.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error reading history data.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return history;
    }

    @Override
    public void showHome() {
        frame.dispose();

        if (parent != null) {
            parent.showHomePanel();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HistoryPage("r"));
    }
}