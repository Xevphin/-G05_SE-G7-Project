/**
 * Creator: Wan Adam
 * Tester: 
 * Description: Menu allowing the user to select between MCQ and True/False quizzes.
 */
import java.awt.*;
import javax.swing.*;
import java.awt.Color;

public class MenuQuiz implements IMenuQuiz {
    
    private JFrame menuFrame;
    private Home parentHome;
    private String userName;

    public MenuQuiz() {
        menuFrame = new JFrame("Select Quiz");
        menuFrame.setSize(360, 640);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setLayout(null);
        menuFrame.setBackground(new Color(245, 248, 252));

        // Fixed: Applied background color to the content pane
        menuFrame.getContentPane().setBackground(new Color(245, 248, 252));

        JLabel title = new JLabel("Choose Your Challenge", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(0, 80, 360, 30);
        menuFrame.add(title);

        JButton mcqBtn = new JButton("Multiple Choice Quiz");
        mcqBtn.setBounds(60, 200, 220, 50);
        mcqBtn.addActionListener(e -> selectQuizType("MCQ"));
        menuFrame.add(mcqBtn);

        JButton tfBtn = new JButton("True / False Quiz");
        tfBtn.setBounds(60, 280, 220, 50);
        tfBtn.addActionListener(e -> selectQuizType("TF"));
        menuFrame.add(tfBtn);

        JButton backBtn = new JButton("Back to Home");
        backBtn.setBounds(110, 500, 120, 40);
        backBtn.addActionListener(e -> {
            menuFrame.dispose();
            if (parentHome != null) parentHome.showHomePanel();
        });
        menuFrame.add(backBtn);

        showMenuQuiz();
    }

    // Added to prevent Home.java from crashing
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
        
        // Track 2 handles the actual quiz modules, so we leave comments for integration
        if (type.equals("MCQ")) {
            System.out.println("Launching MCQ Module...");
            MCQModule mcq = new MCQModule();
            mcq.setUserName(this.userName); // Pass the username to the quiz module
            mcq.setParent(this.parentHome); // Pass the parent home reference to the quiz module
        } else {
            System.out.println("Launching True/False Module...");
            TrueFalseModule tf = new TrueFalseModule();
            tf.setUserName(this.userName); // Pass the username to the quiz module
            tf.setParent(this.parentHome); // Pass the parent home reference to the quiz module
        }
    }

    @Override
    public void showInstructions() {
        JOptionPane.showMessageDialog(menuFrame, "Answer the questions to the best of your ability to earn SDG Badges!");
    }
}