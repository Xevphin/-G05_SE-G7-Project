// QuizResultPage class
// Creator: Qhairunnisa 106089
// Tester: Abdul Rahim 102368

import java.awt.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import javax.swing.*;
import javax.swing.border.*;

public class QuizResultPage implements IResultDisplay {

    private JFrame resultFrame;   
    private String userName;        
    private int    totalQuestion;   
    private int score;
    private String quizType; // Added instance variable to track quiz category
    private Home parent; 

    private static final String DATA_FILE = "QuizScores.txt"; // Synced database tracking reference

    // Color definitions remain exactly intact to preserve Qhairunnisa's design patterns...
    private static final Color CLR_BG      = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90);
    private static final Color CLR_ACCENT  = new Color(52, 168, 115);
    private static final Color CLR_WHITE   = Color.WHITE;
    private static final Color CLR_SUBTEXT = new Color(90, 115, 100);
    
    private static final Font FONT_TITLE  = new Font("Arial", Font.BOLD, 20);
    private static final Font FONT_SCORE  = new Font("Arial", Font.BOLD, 48);
    private static final Font FONT_SMALL  = new Font("Arial", Font.PLAIN, 11);
    private static final Font FONT_MSG    = new Font("Segoe UI Emoji", Font.BOLD, 15);
    private static final Font FONT_BTN    = new Font("Segoe UI Emoji", Font.BOLD, 13);
    private static final Font FONT_STARS  = new Font("Segoe UI Symbol", Font.PLAIN, 28);

    // Modified Constructor to accept quiz category tracking string token
    public QuizResultPage(String userName, int score, int totalQuestion, String quizType) {
        this.userName      = userName;    
        this.score         = score;
        this.totalQuestion = totalQuestion; 
        this.quizType      = quizType; // Cache token ("MCQ" or "TF")

        resultFrame = new JFrame("Quiz Result");
        resultFrame.setSize(360, 640);
        resultFrame.setResizable(false);
        resultFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        resultFrame.setLocationRelativeTo(null);

        // Keep saveResult active
        saveResult(userName, score);

        showResult(userName, score, totalQuestion);
    }

    @Override
    public void showResult(String userName, int score, int totalQuestion) {
        JPanel panel = new JPanel(null);
        panel.setBackground(CLR_BG);

        // --- Header Block Setup ---
        JPanel header = new JPanel(null);
        header.setBackground(CLR_PRIMARY);
        header.setBounds(0, 0, 360, 120);

        JLabel titleLbl = new JLabel("Quiz Complete!", SwingConstants.CENTER);
        titleLbl.setFont(FONT_TITLE);
        titleLbl.setForeground(CLR_WHITE);
        titleLbl.setBounds(0, 20, 360, 30);
        header.add(titleLbl);

        JLabel nameLbl = new JLabel(userName, SwingConstants.CENTER);
        nameLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLbl.setForeground(new Color(180, 230, 210));
        nameLbl.setBounds(0, 56, 360, 20);
        header.add(nameLbl);

        JLabel quizTypeLbl = new JLabel("SDG 4 — Quality Education", SwingConstants.CENTER);
        quizTypeLbl.setFont(FONT_SMALL);
        quizTypeLbl.setForeground(new Color(160, 210, 190));
        quizTypeLbl.setBounds(0, 80, 360, 18);
        header.add(quizTypeLbl);
        panel.add(header);

        // --- Cards Blocks ---
        JPanel scoreCard = makeCard(30, 132, 300, 130);
        JLabel scoreLbl = new JLabel(score + "/" + totalQuestion, SwingConstants.CENTER);
        scoreLbl.setFont(FONT_SCORE);
        scoreLbl.setForeground(CLR_PRIMARY);
        scoreLbl.setBounds(0, 10, 300, 58);
        scoreCard.add(scoreLbl);

        double pct = getScorePercent(score, totalQuestion); 
        JLabel pctLbl = new JLabel(String.format("%.0f%%", pct), SwingConstants.CENTER);
        pctLbl.setFont(new Font("Arial", Font.BOLD, 16));
        pctLbl.setForeground(CLR_ACCENT);
        pctLbl.setBounds(0, 68, 300, 24);
        scoreCard.add(pctLbl);

        JLabel starsLbl = new JLabel(getStars(pct), SwingConstants.CENTER);
        starsLbl.setFont(FONT_STARS);
        starsLbl.setBounds(0, 94, 300, 30);
        scoreCard.add(starsLbl);
        panel.add(scoreCard);

        JPanel msgCard = makeCard(30, 274, 300, 70);
        String msg = getMotivationalMessage(score, totalQuestion); 
        JLabel msgLbl = new JLabel("<html><center>" + msg + "</center></html>", SwingConstants.CENTER);
        msgLbl.setFont(FONT_MSG);
        msgLbl.setForeground(getMessageColor(pct));
        msgLbl.setBounds(10, 8, 280, 54);
        msgCard.add(msgLbl);
        panel.add(msgCard);

        JPanel badgeCard = makeCard(30, 356, 300, 80);
        JLabel badgeTitle = new JLabel("Badge Earned", SwingConstants.CENTER);
        badgeTitle.setFont(FONT_SMALL);
        badgeTitle.setForeground(CLR_SUBTEXT);
        badgeTitle.setBounds(0, 8, 300, 16);
        badgeCard.add(badgeTitle);

        JLabel badgeLbl = new JLabel(getBadge(pct), SwingConstants.CENTER);
        badgeLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        badgeLbl.setForeground(CLR_PRIMARY);
        badgeLbl.setBounds(0, 28, 300, 36);
        badgeCard.add(badgeLbl);
        panel.add(badgeCard);


        // Retry button — Dynamically restarts the specific active quiz session loop
        JButton retryBtn = makeButton("Try Again 🔁", new Color(52, 100, 168));
        retryBtn.setBounds(30, 452, 140, 40);
        panel.add(retryBtn);

        retryBtn.addActionListener(e -> {
            resultFrame.dispose(); // Destroys current result screen window cache
            
            if (quizType.equals("MCQ")) {
                System.out.println("Looping back: Restarting MCQ Module...");
                MCQModule mcq = new MCQModule();
                mcq.setUserName(this.userName);
                mcq.setParent(this.parent);
                mcq.startTimer();
            } else {
                System.out.println("Looping back: Restarting True/False Module...");
                TrueFalseModule tf = new TrueFalseModule();
                tf.setUserName(this.userName);
                tf.setParent(this.parent);
                tf.startTimer();
            }
        });

        // Home button — Navigates cleanly back to the core main dashboard interface
        JButton homeBtn = makeButton("Home 🏠", CLR_PRIMARY);
        homeBtn.setBounds(190, 452, 140, 40);
        panel.add(homeBtn);
        homeBtn.addActionListener(e -> showHome());

        JLabel summaryLbl = new JLabel("You answered " + score + " out of " + totalQuestion + " questions correctly.", SwingConstants.CENTER);
        summaryLbl.setFont(FONT_SMALL);
        summaryLbl.setForeground(CLR_SUBTEXT);
        summaryLbl.setBounds(20, 506, 320, 18);
        panel.add(summaryLbl);

        resultFrame.add(panel);
        resultFrame.setVisible(true);
    }

    // Added dynamic motivational message generator based on score percentage thresholds
    public String getMotivationalMessage(int score, int total) {
        double pct = getScorePercent(score, total);
        if (pct >= 80) return "Outstanding! 🎉";
        if (pct >= 60) return "That's good! 👍";
        if (pct >= 40) return "Good try! 💪";
        if (pct >= 20) return "You can do better! 📚";
        return "Don't give up! 🌱";
    }

    @Override public void showMotivationalMessage(int score) {
        String msg = getMotivationalMessage(score, totalQuestion);
        JOptionPane.showMessageDialog(resultFrame, msg, "Your Result", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override public void showHome() {
        resultFrame.dispose();
        if (parent != null) { parent.showHomePanel(); }
    }

    @Override public double getScorePercent(int score, int total) {
        if (total == 0) return 0.0;
        return ((double) score / total) * 100.0;
    }

    @Override public void saveResult(String userName, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            writer.write(userName + " | Quiz Score: " + score + "/" + totalQuestion + " | " + String.format("%.0f", getScorePercent(score, totalQuestion)) + "%" + " | " + timestamp);
            writer.newLine();
            System.out.println("Result saved for: " + userName + " score=" + score);
        } catch (IOException e) {
            System.err.println("Error saving result for " + userName + ": " + e.getMessage());
            JOptionPane.showMessageDialog(resultFrame, "Could not save your score. Please check file permissions.", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setParent(Home parent) { this.parent = parent; }
    private String getStars(double pct) {
        if (pct >= 80) return "★★★★★";
        if (pct >= 60) return "★★★★☆";
        if (pct >= 40) return "★★★☆☆";
        if (pct >= 20) return "★★☆☆☆";
        return "★☆☆☆☆";
    }
    private String getBadge(double pct) {
        if (pct >= 80) return "🏆 Quiz Champion";
        if (pct >= 60) return "⭐ Rising Star";
        if (pct >= 40) return "📚 Learner";
        if (pct >= 20) return "🎯 Trying Hard";
        return "🌱 Keep Going";
    }
    private Color getMessageColor(double pct) {
        if (pct >= 80) return new Color(20, 130, 80);
        if (pct >= 60) return new Color(30, 100, 180);
        if (pct >= 40) return new Color(180, 130, 0);
        return new Color(180, 60, 60);
    }
    private JPanel makeCard(int x, int y, int w, int h) {
        JPanel card = new JPanel(null);
        card.setBackground(CLR_WHITE);
        card.setBounds(x, y, w, h);
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 220, 210), 1, true), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        return card;
    }
    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setBackground(bg);
        btn.setForeground(CLR_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bg.darker()); btn.repaint(); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(bg); btn.repaint(); }
        });
        return btn;
    }
}