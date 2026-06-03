import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class TrueFalseModule extends AbstractQuizModule {
    private JFrame frame;
    private JTextArea questionArea;
    private JRadioButton trueOption;
    private JRadioButton falseOption;
    private ButtonGroup group;
    private JButton nextButton;
    private String[] questions;
    private boolean[] answers;
    private static final Color CLR_BG = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90);
    private static final Color CLR_WHITE = Color.WHITE;

    public TrueFalseModule() {
        // Initialize questions and answers
        questions = new String[] {
            "The Earth is flat.",
            "The Sun is a star.",
            "Water boils at 100 degrees Celsius."
        };

        answers = new boolean[] {false, true, true}; // Correct answers

        // Setup GUI components (not fully implemented here)
        frame = new JFrame("True/False Quiz");
        frame.setSize(360, 240);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(CLR_BG);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(CLR_BG);

        questionArea = new JTextArea();
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        questionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        questionArea.setBackground(CLR_BG);
        panel.add(questionArea);
        panel.add(Box.createVerticalStrut(20));

        trueOption = new JRadioButton("True");
        falseOption = new JRadioButton("False");
        trueOption.setBackground(CLR_BG);
        falseOption.setBackground(CLR_BG);


        group = new ButtonGroup();
        group.add(trueOption);
        group.add(falseOption);
        
        trueOption.setAlignmentX(Component.LEFT_ALIGNMENT);
        falseOption.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(trueOption);
        panel.add(Box.createVerticalStrut(10));
        panel.add(falseOption);
        
        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextButton.setBackground(CLR_PRIMARY);
        nextButton.setForeground(CLR_WHITE);
        panel.add(Box.createVerticalStrut(20));
        panel.add(nextButton);

        frame.getContentPane().add(panel);

        try {
            loadQuestion(currentQuestion);
        } catch (QuizNotFoundException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        nextButton.addActionListener(e -> {
            int selectedAnswerIndex = -1;
            if (trueOption.isSelected()) {
                selectedAnswerIndex = 0; // True
            } else if (falseOption.isSelected()) {
                selectedAnswerIndex = 1; // False
            }

            if (selectedAnswerIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an answer.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (checkAnswer(selectedAnswerIndex)) {
                score++;
            }

            currentQuestion++;
            group.clearSelection();
            if (currentQuestion < questions.length) {
                try {
                    loadQuestion(currentQuestion);
                } catch (QuizNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                try {
                    showResult();
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
        startTimer();

        

        // Add components to frame and set layout (not fully implemented here)
    }
    
    public void loadQuestion(int index) throws QuizNotFoundException {
        if (index < 0 || index >= questions.length) {
            throw new QuizNotFoundException("Question index out of range.");
        }
        questionArea.setText(questions[index]);
    }

    public boolean checkAnswer(int selectedAnswerIndex) {
        boolean selectedAnswer = (selectedAnswerIndex == 0); // 0 for True, 1 for False
        return selectedAnswer == answers[currentQuestion];
    }
    
    public void showResult() throws InvalidInputException {
        frame.dispose();

        saveResult("True/False", questions.length);

        QuizResultPage result = new QuizResultPage(this.userName, this.score, questions.length);
        result.setParent(this.parentHome);

        double percentage = ((double) score / questions.length) * 100;
        String msg = "";
        if (percentage >= 80) {
            msg = "Excellent! You're a master!";
        } else if (percentage >= 50) {
            msg = "Good job! Keep practicing!";
        } else {
            msg = "Don't worry, try again!";
        }   
        JOptionPane.showMessageDialog(frame, "Your score: " + score + "\n" + msg);
    }
}