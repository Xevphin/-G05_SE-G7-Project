//QuizResultPage class
//Creator: Qhairunnisa 106089
//Tester: Abdul Rahim 102368

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
    private Home parent; // back navigation reference to Home

    //Data storage file
    private static final String DATA_FILE = "users.txt";

    //Colour palette match Home
    private static final Color CLR_BG      = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90);
    private static final Color CLR_ACCENT  = new Color(52, 168, 115);
    private static final Color CLR_WHITE   = Color.WHITE;
    private static final Color CLR_TEXT    = new Color(25, 40, 35);
    private static final Color CLR_SUBTEXT = new Color(90, 115, 100);
    private static final Color CLR_GOLD    = new Color(218, 165, 32);
    private static final Color CLR_SILVER  = new Color(150, 150, 150);

    //Fontss
    private static final Font FONT_TITLE  = new Font("Arial", Font.BOLD, 20);
    private static final Font FONT_SCORE  = new Font("Arial", Font.BOLD, 48);
    private static final Font FONT_SUB    = new Font("Arial", Font.PLAIN, 13);
    private static final Font FONT_SMALL  = new Font("Arial", Font.PLAIN, 11);
    
    private static final Font FONT_MSG    = new Font("Segoe UI Emoji", Font.BOLD, 15);
    private static final Font FONT_BTN    = new Font("Segoe UI Emoji", Font.BOLD, 13);
    private static final Font FONT_STARS  = new Font("Segoe UI Symbol", Font.PLAIN, 28);

    //QuizResultPage class
    // Constructor — called by MCQModule and TrueFalseModule after quiz ends
    public QuizResultPage(String userName, int score, int totalQuestion) {
        this.userName      = userName;    
        this.score         = score;
        this.totalQuestion = totalQuestion; 

        resultFrame = new JFrame("Quiz Result");
        resultFrame.setSize(360, 640);
        resultFrame.setResizable(false);
        resultFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        resultFrame.setLocationRelativeTo(null);

        //Data Storage —written to users.txt
        saveResult(userName, score);

        // Build and show the result screen
        showResult(userName, score, totalQuestion);
    }

    @Override
    public void showResult(String userName, int score, int totalQuestion) {
        JPanel panel = new JPanel(null);
        panel.setBackground(CLR_BG);

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

        //Score card
        JPanel scoreCard = makeCard(30, 132, 300, 130);

        //Large score number (e.g. "8/10")
        JLabel scoreLbl = new JLabel(score + "/" + totalQuestion, SwingConstants.CENTER);
        scoreLbl.setFont(FONT_SCORE);
        scoreLbl.setForeground(CLR_PRIMARY);
        scoreLbl.setBounds(0, 10, 300, 58);
        scoreCard.add(scoreLbl);

        //Percentage label (e.g. "80%")
        double pct = getScorePercent(score, totalQuestion); 
        JLabel pctLbl = new JLabel(String.format("%.0f%%", pct), SwingConstants.CENTER);
        pctLbl.setFont(new Font("Arial", Font.BOLD, 16));
        pctLbl.setForeground(CLR_ACCENT);
        pctLbl.setBounds(0, 68, 300, 24);
        scoreCard.add(pctLbl);

        //Stars display — gamification: stars based on percentage
        String stars = getStars(pct);
        JLabel starsLbl = new JLabel(stars, SwingConstants.CENTER);
        starsLbl.setFont(FONT_STARS);
        starsLbl.setBounds(0, 94, 300, 30);
        scoreCard.add(starsLbl);

        panel.add(scoreCard);

        // motivational messages 
        JPanel msgCard = makeCard(30, 274, 300, 70);

        String msg = getMotivationalMessage(score, totalQuestion); 
        JLabel msgLbl = new JLabel("<html><center>" + msg + "</center></html>", SwingConstants.CENTER);
        msgLbl.setFont(FONT_MSG);
        msgLbl.setForeground(getMessageColor(pct));
        msgLbl.setBounds(10, 8, 280, 54);
        msgCard.add(msgLbl);

        panel.add(msgCard);

        //Gamification — badge display
        JPanel badgeCard = makeCard(30, 356, 300, 80);

        JLabel badgeTitle = new JLabel("Badge Earned", SwingConstants.CENTER);
        badgeTitle.setFont(FONT_SMALL);
        badgeTitle.setForeground(CLR_SUBTEXT);
        badgeTitle.setBounds(0, 8, 300, 16);
        badgeCard.add(badgeTitle);

        String badge = getBadge(pct);
        JLabel badgeLbl = new JLabel(badge, SwingConstants.CENTER);
        badgeLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        badgeLbl.setForeground(CLR_PRIMARY);
        badgeLbl.setBounds(0, 28, 300, 36);
        badgeCard.add(badgeLbl);

        panel.add(badgeCard);

        // Action buttons

        // Retry button — allows user to take quiz again
        JButton retryBtn = makeButton("Try Again 🔁", new Color(52, 100, 168));
        retryBtn.setBounds(30, 452, 140, 40);
        panel.add(retryBtn);

        retryBtn.addActionListener(e -> {
            // Close result, go back to quiz menu via parent
            resultFrame.dispose();
            if (parent != null) {
                //Home: showHome() — navigate back
                parent.showHomePanel();
            }
        });

        // Home button —IResultDisplay: + showHome(): void
        JButton homeBtn = makeButton("Home 🏠", CLR_PRIMARY);
        homeBtn.setBounds(190, 452, 140, 40);
        panel.add(homeBtn);

        homeBtn.addActionListener(e -> {
            showHome(); 
        });

        // Score summary 
        JLabel summaryLbl = new JLabel(
                "You answered " + score + " out of " + totalQuestion + " questions correctly.",
                SwingConstants.CENTER);
        summaryLbl.setFont(FONT_SMALL);
        summaryLbl.setForeground(CLR_SUBTEXT);
        summaryLbl.setBounds(20, 506, 320, 18);
        panel.add(summaryLbl);

        resultFrame.add(panel);
        resultFrame.setVisible(true);
    }

    // Returns the correct motivational message based on % score
    public String getMotivationalMessage(int score, int total) {
        double pct = getScorePercent(score, total); // reuse getScorePercent()
        if (pct >= 80) return "Outstanding! 🎉";
        if (pct >= 60) return "That's good! 👍";
        if (pct >= 40) return "Good try! 💪";
        if (pct >= 20) return "You can do better! 📚";
        return "Don't give up! 🌱";
    }

    // Shows a popup with the motivational message (called externally if needed)
    @Override
    public void showMotivationalMessage(int score) {
        String msg = getMotivationalMessage(score, totalQuestion);
        JOptionPane.showMessageDialog(resultFrame, msg,
                "Your Result", JOptionPane.INFORMATION_MESSAGE);
    }

    // Closes result window and navigates back to Home
    @Override
    public void showHome() {
        resultFrame.dispose();
        if (parent != null) {
            parent.showHomePanel(); 
        }
    }

    // Calculates percentage: (score / total) * 100
    @Override
    public double getScorePercent(int score, int total) {
        if (total == 0) return 0.0;          // against divide by zero
        return ((double) score / total) * 100.0;
    }

    // Writes result line to users.txt 
    // Uses try-catch — fulfills Exception Handling 
    @Override
    public void saveResult(String userName, int score) {
        //Exception Handling — custom meaningful try-catch
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            // Write a clearly formatted result line
            writer.write(userName + " | Quiz Score: " + score
                    + "/" + totalQuestion
                    + " | " + String.format("%.0f", getScorePercent(score, totalQuestion)) + "%"
                    + " | " + timestamp);
            writer.newLine();
            System.out.println("Result saved for: " + userName + " score=" + score);
        } catch (IOException e) {
            // Meaningful exception handling — shows specific error message
            System.err.println("Error saving result for " + userName + ": " + e.getMessage());
            JOptionPane.showMessageDialog(resultFrame,
                    "Could not save your score. Please check file permissions.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Called by MCQModule / TrueFalseModule to pass Home reference so  showHome() can navigate back correctly
    public void setParent(Home parent) {
        this.parent = parent;
    }

    // PRIVATE HELPERS — used internally for UI
    // Returns star string based on percentage
    private String getStars(double pct) {
        if (pct >= 80) return "★★★★★";
        if (pct >= 60) return "★★★★☆";
        if (pct >= 40) return "★★★☆☆";
        if (pct >= 20) return "★★☆☆☆";
        return "★☆☆☆☆";
    }

    // Returns badge name based on percentage — gamification
    private String getBadge(double pct) {
        if (pct >= 80) return "🏆 Quiz Champion";
        if (pct >= 60) return "⭐ Rising Star";
        if (pct >= 40) return "📚 Learner";
        if (pct >= 20) return "🎯 Trying Hard";
        return "🌱 Keep Going";
    }

    // Returns colour matching the score tier for message label
    private Color getMessageColor(double pct) {
        if (pct >= 80) return new Color(20, 130, 80);   // green
        if (pct >= 60) return new Color(30, 100, 180);  // blue
        if (pct >= 40) return new Color(180, 130, 0);   // amber
        return new Color(180, 60, 60);                  // red
    }

    // White rounded card panel
    private JPanel makeCard(int x, int y, int w, int h) {
        JPanel card = new JPanel(null);
        card.setBackground(CLR_WHITE);
        card.setBounds(x, y, w, h);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 210), 1, true),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        return card;
    }

    // Rounded button with hover effect
    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.darker()); btn.repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg); btn.repaint();
            }
        });
        return btn;
    }
}