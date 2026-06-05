// Creator: Wan Adam and Abdul Rahim
// Tester: Abdul Rahim
// Description: Menu allowing the user to select between MCQ and True/False quizzes with a modern, cohesive green theme.

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class MenuQuiz implements IMenuQuiz {
    
    private JFrame menuFrame;
    private Home parentHome;
    private String userName;

    // Standardized Corporate Green Theme Colors matching Home.java
    private static final Color CLR_BG        = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY   = new Color(30, 120, 90);     
    private static final Color CLR_TEXT      = new Color(25, 40, 35);
    private static final Color CLR_WHITE     = Color.WHITE;
    private static final Color CLR_BTN_HOVER = new Color(40, 150, 105);

    public MenuQuiz() {
        menuFrame = new JFrame("Select Quiz");
        menuFrame.setSize(360, 640);
        menuFrame.setResizable(false);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setLayout(null);
        menuFrame.getContentPane().setBackground(CLR_BG);

        // ── Visual Green Accent Banner ───────────────────────────────
        JPanel headerBanner = new JPanel(null);
        headerBanner.setBackground(CLR_PRIMARY);
        headerBanner.setBounds(0, 0, 360, 160);

        JLabel logoLabel = new JLabel("🧠", SwingConstants.CENTER);
        logoLabel.setFont(new Font("SansSerif", Font.PLAIN, 44));
        logoLabel.setBounds(145, 25, 70, 50);
        headerBanner.add(logoLabel);

        JLabel title = new JLabel("Choose Your Challenge", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(CLR_WHITE);
        title.setBounds(0, 85, 360, 30);
        headerBanner.add(title);

        JLabel subtitle = new JLabel("Test your knowledge on SDG 4 metrics", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(new Color(190, 235, 215));
        subtitle.setBounds(0, 118, 360, 20);
        headerBanner.add(subtitle);

        menuFrame.add(headerBanner);

        // ── Interactive Challenge Nav Selection Buttons ───────────────
        JButton mcqBtn = makeModernButton("Multiple Choice Quiz", CLR_PRIMARY);
        mcqBtn.setBounds(40, 220, 280, 52);
        mcqBtn.addActionListener(e -> selectQuizType("MCQ"));
        menuFrame.add(mcqBtn);

        JButton tfBtn = makeModernButton("True / False Quiz", CLR_PRIMARY);
        tfBtn.setBounds(40, 295, 280, 52);
        tfBtn.addActionListener(e -> selectQuizType("TF"));
        menuFrame.add(tfBtn);

        JButton instructionsBtn = makeModernButton("View Instructions 📋", CLR_WHITE);
        instructionsBtn.setForeground(CLR_PRIMARY);
        instructionsBtn.setBorder(new LineBorder(CLR_PRIMARY, 1, true));
        instructionsBtn.setBounds(40, 370, 280, 52);
        instructionsBtn.addActionListener(e -> showInstructions());
        menuFrame.add(instructionsBtn);

        // ── Clean Exit Button ─────────────────────────────────────────
        JButton backBtn = makeModernButton("Back to Home", CLR_WHITE);
        backBtn.setForeground(new Color(180, 60, 60));
        backBtn.setBorder(new LineBorder(new Color(180, 60, 60), 1, true));
        backBtn.setBounds(95, 525, 170, 40);
        backBtn.addActionListener(e -> {
            menuFrame.dispose();
            if (parentHome != null) parentHome.showHomePanel();
        });
        menuFrame.add(backBtn);

        showMenuQuiz();
    }

    // Helper Factory Method to cleanly construct and style anti-aliased rounded buttons
    private JButton makeModernButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(bg.equals(CLR_WHITE) ? CLR_TEXT : CLR_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if(!bg.equals(CLR_WHITE)) {
                    btn.setBackground(CLR_BTN_HOVER);
                } else {
                    btn.setBackground(new Color(235, 245, 240));
                }
                btn.repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
                btn.repaint();
            }
        });
        return btn;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public void setParent(Home h) {
        this.parentHome = h;
    }

    @Override
    public void showMenuQuiz() {
        menuFrame.setVisible(true);
    }

    @Override
    public void selectQuizType(String type) {
        System.out.println("User selected: " + type);
        menuFrame.dispose();
        
        if (type.equals("MCQ")) {
            System.out.println("Launching MCQ Module...");
            MCQModule mcq = new MCQModule();
            mcq.setUserName(this.userName);
            mcq.setParent(this.parentHome);
            mcq.startTimer();
        } else {
            System.out.println("Launching True/False Module...");
            TrueFalseModule tf = new TrueFalseModule();
            tf.setUserName(this.userName);
            tf.setParent(this.parentHome);
            tf.startTimer();
        }
    }

    @Override
    public void showInstructions() {
        JOptionPane.showMessageDialog(menuFrame, 
            "📋 INSTRUCTIONS:\n\n" +
            "1. You have exactly 30 seconds to finish the module.\n" +
            "2. Failure to complete in time triggers automatic submission.\n" +
            "3. Earn consecutive correct points to unlock premium SDG Badges!", 
            "Quiz Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}