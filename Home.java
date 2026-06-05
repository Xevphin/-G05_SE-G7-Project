//Home class, User class, IUser
//Creator: Qhairunnisa 106089
//Tester: Wan Adam 103014 and Abdul Rahim 102368


import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

// Interface defining basic user operations//
interface IUser {
    void    enterName(String name);      
    String  getName();                  
    int     getScore();                 
    void    setScore(int score);        
    void    saveUser();                  
    boolean loadUser();                 
    String  getBadge();                
    void    resetScore();              
    void    getLearningModule();        
    void    getQuizMenu();               
    void    getHistory();                
}

//InvalidInputException extends Exception
class InvalidInputException extends Exception {        
    public InvalidInputException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

//QuizNotFoundException extends Exception
class QuizNotFoundException extends Exception {        
    public QuizNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

//User class to handle user data and file operations
class User implements IUser {                          
    private String name;                               
    private int    score;                              
    private static final String DATA_FILE = "users.txt"; 

    @Override
    public void enterName(String name) {
        this.name = name;
        System.out.println("User name set to: " + name);
    }

    @Override
    public String getName() { 
        return name; 
    }

    @Override
    public int getScore() { 
        return score; 
    }

    @Override
    public void setScore(int score) { 
        this.score = score; 
    }

    public void setScore(int score, int bonus) {
        this.score = score + bonus;
        System.out.println("Score set with bonus: " + this.score);
    }

    @Override
    public void resetScore() {
        this.score = 0;
        System.out.println("Score reset for: " + name);
    }

    // Returns a badge string based on cumulative score (gamification element)
    @Override
    public String getBadge() {
        if (score >= 18) return "🏆 Quiz Champion";
        if (score >= 10) return "⭐ Rising Star";
        if (score >= 5)  return "📚 Learner";
        return "🌱 Beginner";
    }

    // Navigation handled by StartHome (composition)
    @Override public void getLearningModule() {}

    @Override public void getQuizMenu() {}

    @Override public void getHistory() {}

    //To save user data if haven't
    // Saves new user entry to DATA_FILE — fulfills Data Storage requirement.
    @Override
    public void saveUser() {
        // Check if initial entry to avoid duplicate writes
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(name + " | Initial Score")) {
                    System.out.println("Initial entry already exists for: " + name);
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking existing user: " + e.getMessage());
        }

        // Save new user 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(name + " | Initial Score: " + score);
            writer.newLine();
            System.out.println("Saved new user: " + name);
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    // Load user's highest scores from files
    // Reads users.txt and calculates the highest MCQ + TrueFalse scores
    // Reads QuizScores.txt instead of users.txt and calculates the highest MCQ + TrueFalse scores
    @Override
    public boolean loadUser() {
        int highestMCQScore = 0;
        int highestTFScore  = 0;
        boolean userFound   = false;
        
        // Changed source path to QuizScores.txt to match your active tracking database
        File scoreFile = new File("QuizScores.txt");
        if (!scoreFile.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(scoreFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Ignore Git merge conflict markers safely
                if (line.startsWith("<") || line.startsWith("=") || line.startsWith(">")) {
                    continue;
                }

                // Verify if the score row belongs to the current active profile session
                if (line.startsWith("User: " + name + " |")) {
                    userFound = true;

                    // Fixed token matching to align perfectly with saveResult() parameters
                    if (line.contains("Type: MCQ")) {
                        try {
                            String[] parts   = line.split("\\|");
                            String scorePart = parts[2].trim().replace("Score:", "").trim().split("/")[0];
                            int s            = Integer.parseInt(scorePart.trim());
                            highestMCQScore  = Math.max(highestMCQScore, s);
                        } catch (Exception ignored) {}
                    }
                    else if (line.contains("Type: True/False") || line.contains("Type: TF")) {
                        try {
                            String[] parts   = line.split("\\|");
                            String scorePart = parts[2].trim().replace("Score:", "").trim().split("/")[0];
                            int s            = Integer.parseInt(scorePart.trim());
                            highestTFScore   = Math.max(highestTFScore, s);
                        } catch (Exception ignored) {}
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading QuizScores.txt: " + e.getMessage());
            return false;
        }

        // Aggregate points and update volatile cumulative totals
        this.score = highestMCQScore + highestTFScore;
        System.out.println("Loaded " + name + " | MCQ Max: " + highestMCQScore + " | TF Max: " + highestTFScore + " | Total: " + this.score);
        return userFound;
    }
}

// This is the main menu and navigation controller
//Home class
public class Home {
    //Color
    private static final Color CLR_BG        = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY   = new Color(30, 120, 90);    
    private static final Color CLR_ACCENT    = new Color(52, 168, 115);  
    private static final Color CLR_SOFT      = new Color(220, 240, 230);  
    private static final Color CLR_TEXT      = new Color(25, 40, 35);
    private static final Color CLR_SUBTEXT   = new Color(90, 115, 100);
    private static final Color CLR_WHITE     = Color.WHITE;
    private static final Color CLR_BTN_HOVER = new Color(40, 150, 105);

    // Fonts
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    private static final Font FONT_SUB   = new Font("Arial", Font.PLAIN, 13);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 13);
    private static final Font FONT_BTN   = new Font("Arial", Font.BOLD, 13);
    private static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 11);
    private static final Font FONT_BADGE = new Font("Arial", Font.BOLD, 12);

    //Home attributes
    private JFrame     frame;          
    private CardLayout cardLayout;     
    private JPanel     mainPanel;      
    private JTextField nameField;     
    private IUser      user;          
    private JLabel     scoreLabel;     
    private JLabel     greetingLabel;  
    private JLabel     badgeLabel;    

    //Sets up the JFrame, CardLayout, and all panels.
    //GUI and display name on entry screen
    public Home() {
        frame = new JFrame("SDG 4 — Quality Education");
        frame.setSize(360, 640);           // smartphone resolution as required
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();    
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(CLR_BG);

        user = new User();                 //IUser— Home creates User (composition)

        mainPanel.add(enterNamePanel(),   "EnterName");  
        mainPanel.add(newUserPanel(),     "NewUser");     
        mainPanel.add(welcomeBackPanel(), "WelcomeBack"); 
        mainPanel.add(homePanel(),        "Home");       

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cardLayout.show(mainPanel, "EnterName");
    }

    // First screen shown. User types their name.
    private JPanel enterNamePanel() {
        JPanel panel = createBasePanel();

        // ── Green header band ─────────────────────────────────
        JPanel header = new JPanel(null);
        header.setBackground(CLR_PRIMARY);
        header.setBounds(0, 0, 360, 180);

        JLabel appTitle = new JLabel("SDG 4: Quality Education", SwingConstants.CENTER);
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        appTitle.setForeground(CLR_WHITE);
        appTitle.setBounds(0, 38, 360, 40);
        header.add(appTitle);

        JLabel tagline = new JLabel("Education is not just a Goal, it is a Better Future.", SwingConstants.CENTER);
        tagline.setFont(FONT_SMALL);
        tagline.setForeground(new Color(180, 230, 210));
        tagline.setBounds(0, 82, 360, 20);
        header.add(tagline);

        JLabel logo = new JLabel("🌿", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.PLAIN, 48));
        logo.setBounds(145, 110, 70, 60);
        header.add(logo);

        panel.add(header);

        JPanel card = makeCard(30, 198, 300, 200);

        JLabel prompt = new JLabel("Enter your name to begin");
        prompt.setFont(FONT_LABEL);
        prompt.setForeground(CLR_TEXT);
        prompt.setBounds(20, 18, 260, 22);
        card.add(prompt);

        nameField = new JTextField();
        nameField.setBounds(20, 48, 260, 36);
        nameField.setFont(FONT_SUB);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(CLR_ACCENT, 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        card.add(nameField);

        JButton nextBtn = makeButton("Get Started →", CLR_PRIMARY);
        nextBtn.setBounds(20, 102, 260, 40);
        card.add(nextBtn);

        // Button logic: validate throw custom exception if blank
        nextBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            try {
                if (name.isEmpty()) throw new InvalidInputException("Name cannot be empty.");

                user.enterName(name);          

                if (user.loadUser()) {          
                    updateHomeLabels();
                    
                    cardLayout.show(mainPanel, "WelcomeBack");
                } else {
                    user.setScore(0);           
                    user.saveUser();            
                    updateHomeLabels();
                    
                    cardLayout.show(mainPanel, "NewUser");
                }

            } catch (InvalidInputException ex) {
                // Custom exception caught and shown to user
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(card);

        JLabel hint = new JLabel("Your progress is saved automatically", SwingConstants.CENTER);
        hint.setFont(FONT_SMALL);
        hint.setForeground(CLR_SUBTEXT);
        hint.setBounds(0, 412, 360, 20);
        panel.add(hint);

        return panel;
    }

    // Shown when loadUser() returns false (first-time user)
    private JPanel newUserPanel() {
        JPanel panel = createBasePanel();
        panel.add(makeGreenHeader("Welcome!", "New adventurer detected"));

        JPanel card = makeCard(30, 200, 300, 220);

        JLabel msg1 = new JLabel("Great to have you here,", SwingConstants.CENTER);
        msg1.setFont(FONT_SUB);
        msg1.setForeground(CLR_SUBTEXT);
        msg1.setBounds(0, 20, 300, 22);
        card.add(msg1);

        JLabel nameDisplay = new JLabel("", SwingConstants.CENTER);
        nameDisplay.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameDisplay.setForeground(CLR_PRIMARY);
        nameDisplay.setBounds(0, 46, 300, 30);
        card.add(nameDisplay);

        JLabel msg2 = new JLabel(
                "<html><center>Explore education topics, take quizzes,<br>and earn badges!</center></html>",
                SwingConstants.CENTER);
        msg2.setFont(FONT_SMALL);
        msg2.setForeground(CLR_SUBTEXT);
        msg2.setBounds(20, 90, 260, 40);
        card.add(msg2);

        JButton startBtn = makeButton("Start Learning →", CLR_PRIMARY);
        startBtn.setBounds(20, 148, 260, 40);
        card.add(startBtn);

        startBtn.addActionListener(e -> {
            nameDisplay.setText(user.getName()); 
            cardLayout.show(mainPanel, "Home");
        });

        panel.add(card);
        return panel;
    }

    // Shown when loadUser() returns true (returning user)
    private JPanel welcomeBackPanel() {
        JPanel panel = createBasePanel();
        panel.add(makeGreenHeader("Welcome Back!", "Continuing your journey"));

        JPanel card = makeCard(30, 200, 300, 220);

        JLabel msg1 = new JLabel("Good to see you again,", SwingConstants.CENTER);
        msg1.setFont(FONT_SUB);
        msg1.setForeground(CLR_SUBTEXT);
        msg1.setBounds(0, 20, 300, 22);
        card.add(msg1);

        JLabel nameDisplay = new JLabel("", SwingConstants.CENTER);
        nameDisplay.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameDisplay.setForeground(CLR_PRIMARY);
        nameDisplay.setBounds(0, 46, 300, 30);
        card.add(nameDisplay);

        JLabel msg2 = new JLabel("Your previous scores have been loaded.", SwingConstants.CENTER);
        msg2.setFont(FONT_SMALL);
        msg2.setForeground(CLR_SUBTEXT);
        msg2.setBounds(20, 90, 260, 22);
        card.add(msg2);

        JButton contBtn = makeButton("Continue →", CLR_PRIMARY);
        contBtn.setBounds(20, 148, 260, 40);
        card.add(contBtn);

        contBtn.addActionListener(e -> {
            nameDisplay.setText(user.getName());
            cardLayout.show(mainPanel, "Home");
        });

        panel.add(card);
        return panel;
    }

    //Home: + homePanel(): JPanel
    //Main dashboard. Shows greeting, badge, score, and nav buttons
    private JPanel homePanel() {
        JPanel panel = createBasePanel();

        JPanel header = new JPanel(null);
        header.setBackground(CLR_PRIMARY);
        header.setBounds(0, 0, 360, 140);

        greetingLabel = new JLabel("Hi, User", SwingConstants.CENTER);
        greetingLabel.setFont(FONT_TITLE);
        greetingLabel.setForeground(CLR_WHITE);
        greetingLabel.setBounds(0, 24, 360, 32);
        header.add(greetingLabel);

        // Gamification element (badges/points/stars)
        badgeLabel = new JLabel("🌱 Beginner", SwingConstants.CENTER);
        badgeLabel.setFont(FONT_BADGE);
        badgeLabel.setForeground(new Color(200, 240, 220));
        badgeLabel.setBounds(0, 62, 360, 22);
        header.add(badgeLabel);

        //ScoreLabel
        scoreLabel = new JLabel("Total Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(FONT_SMALL);
        scoreLabel.setForeground(new Color(180, 220, 200));
        scoreLabel.setBounds(0, 88, 360, 18);
        header.add(scoreLabel);

        panel.add(header);

        String[][] menuItems = {
            { "📖", "Learning",    "Learn SDG 4"  },  //LearnModule(composition)
            { "🧠", "Quiz",        "Test your knowledge"        },  //QuizMenu    (composition)
            { "🏆", "Leaderboard", "See top scores"             },  //LeaderboardPage (dependency)
            { "📋", "History",     "Your quiz history"          },  //HistoryPage (dependency)
        };

        int yStart = 158, gap = 10, btnH = 60;

        for (String[] item : menuItems) {
            JPanel row = makeMenuRow(item[0], item[1], item[2]);
            row.setBounds(20, yStart, 320, btnH);
            yStart += btnH + gap;

            // Hover effects
            row.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    
                    handleMenuClick(item[1]);
                }
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    row.setBackground(CLR_SOFT); row.repaint();
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    row.setBackground(CLR_WHITE); row.repaint();
                }
            });
            panel.add(row);
        }

        //Logout button
        JButton logoutBtn = makeButton("Logout", CLR_WHITE);
        logoutBtn.setForeground(new Color(180, 60, 60));
        logoutBtn.setBorder(new LineBorder(new Color(180, 60, 60), 1, true));
        logoutBtn.setBounds(110, 548, 140, 34);
        panel.add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            user.enterName("");         //clear name
            user.resetScore();          //clear score
            nameField.setText("");
           
            cardLayout.show(mainPanel, "EnterName");
            frame.setVisible(true);
            System.out.println("Logged out.");
        });

        return panel;
    }


    // Navigates to a module based on button label
    private void handleMenuClick(String destination) {
        frame.dispose(); // Close current window before opening module

        switch (destination) {

            case "Learning":
                //Composition: Home creates LearningModule
                LearningModule module = new LearningModule();
                module.setParent(this);   // pass reference so module can navigate back
                break;

            case "Quiz":
                //Composition:Home creates QuizMenu
                MenuQuiz menu = new MenuQuiz();
                menu.setUserName(user.getName()); 
                menu.setParent(this);
                System.out.println("Navigating to QuizMenu: " + user.getName());
                break;

            case "Leaderboard":
                //Dependency: Home uses LeaderBoard
                LeaderBoard lb = new LeaderBoard();
                lb.setParent(this);
                break;

            case "History":
                //Dependency:Home uses HistoryPage
                HistoryPage hp = new HistoryPage(user.getName());
                hp.setParent(this);
                break;
        }
    }


    //Home: + showHome(): void
    // Reloads user score from file and refreshes all labels
    public void showHomePanel() {
        user.loadUser();         //reload from users.txt
        updateHomeLabels();
        frame.setVisible(true);
        cardLayout.show(mainPanel, "Home"); 
        System.out.println("Home: " + user.getName() + " score=" + user.getScore());
    }

    // Helper: refreshes greetingLabel, badgeLabel, scoreLabel
    // Called after login (new or returning user)
    private void updateHomeLabels() {
        if (greetingLabel != null)
            greetingLabel.setText("Hi, " + user.getName() + " 👋"); 
        if (badgeLabel != null)
            badgeLabel.setText(user.getBadge());                     
        if (scoreLabel != null)
            scoreLabel.setText("Total Score: " + user.getScore());   
    }


    //Public getter used by other classes 
    public String getUserName() { 
        return user.getName(); 
    }

    // Empty panel with absolute layout and background colour
    private JPanel createBasePanel() {
        JPanel p = new JPanel(null);
        p.setBackground(CLR_BG);
        return p;
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

    //WelcomeBack panels
    private JPanel makeGreenHeader(String title, String sub) {
        JPanel header = new JPanel(null);
        header.setBackground(CLR_PRIMARY);
        header.setBounds(0, 0, 360, 180);

        JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
        lTitle.setFont(FONT_TITLE);
        lTitle.setForeground(CLR_WHITE);
        lTitle.setBounds(0, 60, 360, 36);
        header.add(lTitle);

        JLabel lSub = new JLabel(sub, SwingConstants.CENTER);
        lSub.setFont(FONT_SMALL);
        lSub.setForeground(new Color(180, 230, 210));
        lSub.setBounds(0, 100, 360, 22);
        header.add(lSub);

        return header;
    }

    // Navigation row
    private JPanel makeMenuRow(String icon, String title, String desc) {
        JPanel row = new JPanel(null);
        row.setBackground(CLR_WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 230, 220), 1, true),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 26));
        iconLbl.setBounds(10, 8, 44, 44);
        row.add(iconLbl);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_BTN);
        titleLbl.setForeground(CLR_TEXT);
        titleLbl.setBounds(62, 10, 200, 20);
        row.add(titleLbl);

        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(FONT_SMALL);
        descLbl.setForeground(CLR_SUBTEXT);
        descLbl.setBounds(62, 32, 220, 16);
        row.add(descLbl);

        JLabel arrow = new JLabel("›");
        arrow.setFont(new Font("SansSerif", Font.BOLD, 22));
        arrow.setForeground(CLR_ACCENT);
        arrow.setBounds(288, 14, 20, 30);
        row.add(arrow);

        return row;
    }

    // Rounded button with hover colour change
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
        btn.setForeground(bg.equals(CLR_WHITE) ? CLR_TEXT : CLR_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(CLR_BTN_HOVER); btn.repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg); btn.repaint();
            }
        });
        return btn;
    }

    // MAIN — application entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}