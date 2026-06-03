// Creator: Putra Akmal
// Tester: Putra Akmal
// Description: Displays leaderboard by reading QuizScores.txt and sorting scores.

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.util.*;

public class LeaderBoard implements ILeaderBoard {

    private JFrame frame;
    private JTextArea scoreArea;
    private Home parent;
    private static final String SCORE_FILE = "QuizScores.txt";

    public LeaderBoard() {
        frame = new JFrame("Leaderboard");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 248, 252));

        JLabel title = new JLabel("🏆 Leaderboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(30, 120, 90));
        title.setBounds(0, 25, 360, 40);
        panel.add(title);

        scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scoreArea.setLineWrap(true);
        scoreArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(scoreArea);
        scrollPane.setBounds(25, 85, 310, 420);
        scrollPane.setBorder(new LineBorder(new Color(200, 220, 210), 1, true));
        panel.add(scrollPane);

        JButton homeButton = new JButton("Back to Home");
        homeButton.setBounds(95, 525, 170, 38);
        homeButton.setBackground(new Color(30, 120, 90));
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(e -> showHome());
        panel.add(homeButton);

        frame.add(panel);

        displayLeaderboard();
    }

    public void setParent(Home h) {
        this.parent = h;
    }

    @Override
    public void displayLeaderboard() {
        ArrayList<String> scores = readScores();

        if (scores.isEmpty()) {
            scoreArea.setText("No quiz scores found yet.");
        } else {
            scoreArea.setText("Rank | User | Type | Score\n");
            scoreArea.append("--------------------------------------\n");

            int rank = 1;
            for (String score : scores) {
                scoreArea.append(rank + ". " + score + "\n");
                rank++;
            }
        }

        frame.setVisible(true);
    }

    private ArrayList<String> readScores() {
        ArrayList<String> scores = new ArrayList<>();

        try {
            File file = new File(SCORE_FILE);

            if (!file.exists()) {
                return scores;
            }

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (!line.isEmpty()) {
                    scores.add(line);
                }
            }

            scanner.close();

            scores.sort((a, b) -> extractPercentage(b) - extractPercentage(a));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error reading leaderboard data.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return scores;
    }

    private int extractPercentage(String line) {
        try {
            int scoreIndex = line.indexOf("Score:");

            if (scoreIndex == -1) {
                return 0;
            }

            String scorePart = line.substring(scoreIndex + 6).trim();
            String[] parts = scorePart.split("/");

            int obtained = Integer.parseInt(parts[0].trim());
            int total = Integer.parseInt(parts[1].trim());

            return (obtained * 100) / total;

        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getTopScore() {
        ArrayList<String> scores = readScores();

        if (scores.isEmpty()) {
            return 0;
        }

        return extractPercentage(scores.get(0));
    }

    @Override
    public void clearLeaderboard() {
        try {
            FileWriter writer = new FileWriter(SCORE_FILE);
            writer.write("");
            writer.close();

            scoreArea.setText("Leaderboard cleared.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                    "Unable to clear leaderboard.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void showHome() {
        frame.dispose();

        if (parent != null) {
            parent.showHomePanel();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LeaderBoard();
        });
    }
}