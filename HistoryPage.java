// Creator: Putra Akmal
// Tester: Abdul Rahim
// Description: Displays a structured, modern quiz history grid for the logged-in user using a JTable.

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.*;

public class HistoryPage implements IHistoryDisplay {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private String userName;
    private Home parent;
    private static final String SCORE_FILE = "QuizScores.txt";

    // Palette Definitions matching Home.java theme
    private static final Color CLR_BG = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90);
    private static final Color CLR_TEXT = new Color(25, 40, 35);
    private static final Color CLR_SUBTEXT = new Color(90, 115, 100);
    private static final Color CLR_WHITE = Color.WHITE;

    public HistoryPage(String userName) {
        this.userName = userName;

        frame = new JFrame("Quiz History");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(CLR_BG);

        // ── Header Layout ───────────────────────────────────────────────
        JLabel title = new JLabel("📋 Quiz History", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(CLR_PRIMARY);
        title.setBounds(0, 25, 360, 35);
        mainPanel.add(title);

        JLabel userLabel = new JLabel("Showing attempts for: " + userName, SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        userLabel.setForeground(CLR_SUBTEXT);
        userLabel.setBounds(0, 60, 360, 20);
        mainPanel.add(userLabel);

        // ── JTable Data Grid Setup ─────────────────────────────────────
        String[] columns = {"Attempt", "Quiz Type", "Score"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent user tampering
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(CLR_WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(CLR_TEXT);

        // Style the Table Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(CLR_BG);
        header.setForeground(CLR_SUBTEXT);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Grid Alignment Renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // ── Custom Card Layout Container ────────────────────────────────
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 95, 320, 405);
        scrollPane.getViewport().setBackground(CLR_WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 235, 225), 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));
        mainPanel.add(scrollPane);

        // ── Navigation Control Button ────────────────────────────────────
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
        displayHistory(userName);
    }

    public void setParent(Home h) {
        this.parent = h;
    }

    @Override
    public void displayHistory(String userName) {
        tableModel.setRowCount(0); // Clear layout cache
        ArrayList<String[]> userRecords = readUserHistoryAndParse(userName);

        if (userRecords.isEmpty()) {
            tableModel.addRow(new Object[]{"-", "No attempts logged yet", "-"});
        } else {
            int attemptNum = 1;
            for (String[] record : userRecords) {
                tableModel.addRow(new Object[]{"#" + attemptNum, record[0], record[1]});
                attemptNum++;
            }
        }

        frame.setVisible(true);
    }

    private ArrayList<String[]> readUserHistoryAndParse(String userName) {
        ArrayList<String[]> parsedHistory = new ArrayList<>();

        try {
            File file = new File(SCORE_FILE);
            if (!file.exists()) return parsedHistory;

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Check if line belongs to current logged-in user account
                if (line.startsWith("User: " + userName + " |")) {
                    try {
                        // Extract type and score safely via array splits
                        String typePart = line.split("\\|")[1].replace("Type:", "").trim();
                        String scorePart = line.split("\\|")[2].replace("Score:", "").trim();
                        
                        // Human-friendly visual formatting corrections
                        if (typePart.equals("TF")) typePart = "True / False Quiz";
                        if (typePart.equals("MCQ")) typePart = "Multiple Choice Quiz";

                        parsedHistory.add(new String[]{typePart, scorePart});
                    } catch (Exception e) {
                        // Bypass line gracefully if format is malformed
                    }
                }
            }
            scanner.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error parsing user history logs.",
                    "Data Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return parsedHistory;
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