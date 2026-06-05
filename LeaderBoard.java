// Creator: Putra Akmal
// Tester: Abdul Rahim
// Description: Displays a structured, modern leaderboard using a JTable dashboard layout.

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.*;

public class LeaderBoard implements ILeaderBoard {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private Home parent;
    private static final String SCORE_FILE = "QuizScores.txt";

    // Palette Definitions
    private static final Color CLR_BG = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90);
    private static final Color CLR_TEXT = new Color(25, 40, 35);
    private static final Color CLR_SUBTEXT = new Color(90, 115, 100);
    private static final Color CLR_WHITE = Color.WHITE;

    public LeaderBoard() {
        frame = new JFrame("Leaderboard");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(CLR_BG);

        // ── Minimalist Header ───────────────────────────────────────────
        JLabel title = new JLabel("🏆 Leaderboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(CLR_PRIMARY);
        title.setBounds(0, 25, 360, 35);
        mainPanel.add(title);

        // ── JTable Structural Grid Configuration ────────────────────────
        String[] columns = {"Rank", "User", "Type", "Score"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Lock editing permissions
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(CLR_WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(CLR_TEXT);

        // Style Table Header Elements
        JTableHeader header = table.getTableHeader();
        header.setBackground(CLR_BG);
        header.setForeground(CLR_SUBTEXT);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Grid Alignment Renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // ── Custom Rounded Card Layout Container ────────────────────────
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 80, 320, 420);
        scrollPane.getViewport().setBackground(CLR_WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 235, 225), 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));
        mainPanel.add(scrollPane);

        // ── Dashboard Control Navigation Hook ───────────────────────────
        JButton homeButton = new JButton("Back to Home") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        homeButton.setBounds(20, 525, 320, 42);
        homeButton.setFont(new Font("Arial", Font.BOLD, 14));
        homeButton.setBackground(CLR_PRIMARY);
        homeButton.setForeground(CLR_WHITE);
        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setOpaque(false);
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> showHome());
        mainPanel.add(homeButton);

        frame.add(mainPanel);
        displayLeaderboard();
    }

    public void setParent(Home h) {
        this.parent = h;
    }

    @Override
    public void displayLeaderboard() {
        tableModel.setRowCount(0); // Flush cache lines
        ArrayList<String[]> parsedRecords = readScoresAndParse();

        if (parsedRecords.isEmpty()) {
            tableModel.addRow(new Object[]{"-", "No scores found", "-", "-"});
        } else {
            int rank = 1;
            for (String[] record : parsedRecords) {
                String rankPrefix = switch (rank) {
                    case 1 -> "🥇 1st";
                    case 2 -> "🥈 2nd";
                    case 3 -> "🥉 3rd";
                    default -> String.valueOf(rank);
                };
                tableModel.addRow(new Object[]{rankPrefix, record[0], record[1], record[2]});
                rank++;
            }
        }
        frame.setVisible(true);
    }

    private ArrayList<String[]> readScoresAndParse() {
        ArrayList<String[]> dataList = new ArrayList<>();
        ArrayList<String> rawLines = new ArrayList<>();

        try {
            File file = new File(SCORE_FILE);
            if (!file.exists()) return dataList;

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                // 🛑 CRITICAL DEBUGGING FIX: Sanitize and bypass raw Git merge conflict strings
                if (!line.isEmpty() && !line.startsWith("<") && !line.startsWith("=") && !line.startsWith(">")) {
                    rawLines.add(line);
                }
            }
            scanner.close();

            // Sort data arrays dynamically by score percentage
            rawLines.sort((a, b) -> extractPercentage(b) - extractPercentage(a));

            // Parse formatted strings into row components: [User, Type, Score]
            for (String line : rawLines) {
                try {
                    String userPart = line.split("\\|")[0].replace("User:", "").trim();
                    String typePart = line.split("\\|")[1].replace("Type:", "").trim();
                    String scorePart = line.split("\\|")[2].replace("Score:", "").trim();
                    dataList.add(new String[]{userPart, typePart, scorePart});
                } catch (Exception e) {
                    // Ignore corrupted individual lines safely
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error displaying database grid.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return dataList;
    }

    private int extractPercentage(String line) {
        try {
            int scoreIndex = line.indexOf("Score:");
            if (scoreIndex == -1) return 0;

            String scorePart = line.substring(scoreIndex + 6).trim();
            String[] parts = scorePart.split("/");

            int obtained = Integer.parseInt(parts[0].trim());
            int total = Integer.parseInt(parts[1].trim());

            return (total == 0) ? 0 : (obtained * 100) / total;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getTopScore() {
        ArrayList<String[]> scores = readScoresAndParse();
        if (scores.isEmpty()) return 0;
        
        // Reconstruct first row to pull top statistics percentage
        String topScoreStr = scores.get(0)[2]; // returns e.g. "3/3"
        try {
            String[] parts = topScoreStr.split("/");
            int obtained = Integer.parseInt(parts[0].trim());
            int total = Integer.parseInt(parts[1].trim());
            return (obtained * 100) / total;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void clearLeaderboard() {
        try {
            FileWriter writer = new FileWriter(SCORE_FILE);
            writer.write("");
            writer.close();
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"-", "Leaderboard cleared.", "-", "-"});
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to clear data.", "File Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> new LeaderBoard());
    }
}