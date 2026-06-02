import javax.swing.*;
import java.awt.*;
import java.awt.Color;


public class MCQModule extends AbstractQuizModule{
    private JFrame mcqFrame;
    private JTextArea questionArea;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;
    private String[] questions;
    private String[][] choices;
    private int[] answers;
    private static final Color CLR_BG = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90); 
    private static final Color CLR_WHITE = Color.WHITE;

    public MCQModule() {
        // Initialize questions, choices, and answers
        questions = new String[] {
            "What is the capital of France?",
            "Which planet is known as the Red Planet?",
            "Who wrote 'Romeo and Juliet'?"
        };

        choices = new String[][] {
            {"Paris", "London", "Berlin", "Madrid"},
            {"Earth", "Mars", "Jupiter", "Saturn"},
            {"William Shakespeare", "Charles Dickens", "Jane Austen", "Mark Twain"}
        };

        answers = new int[] {0, 1, 0}; // Correct answer indices
        currentQuestion = 0;
        score = 0;

        // Setup GUI components (not fully implemented here)
        mcqFrame = new JFrame("MCQ Quiz");
        mcqFrame.setSize(360, 640);
        mcqFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mcqFrame.setLocationRelativeTo(null);
        mcqFrame.getContentPane().setBackground(CLR_BG);

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

        options = new JRadioButton[4];
        group = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            options[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            options[i].setBackground(CLR_BG);
            panel.add(options[i]);
            panel.add(Box.createVerticalStrut(10));
        }
        
        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextButton.setBackground(CLR_PRIMARY);
        nextButton.setForeground(CLR_WHITE);
        panel.add(Box.createVerticalStrut(20));
        panel.add(nextButton);

        mcqFrame.getContentPane().add(panel);

        try {
            loadQuestion(currentQuestion);
        } catch (QuizNotFoundException e) {
            JOptionPane.showMessageDialog(mcqFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        nextButton.addActionListener(e -> {
            int selectedAnswerIndex = -1;
            for (int i = 0; i < options.length; i++) {
                if (options[i].isSelected()) {
                    selectedAnswerIndex = i;
                    break;
                }
            }

            if (selectedAnswerIndex == -1) {
                JOptionPane.showMessageDialog(mcqFrame, "Please select an answer.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (checkAnswer(selectedAnswerIndex)) {
                score++;
            }

            currentQuestion++;
            if (currentQuestion < questions.length) {
                try {
                    loadQuestion(currentQuestion);
                } catch (QuizNotFoundException ex) {
                    JOptionPane.showMessageDialog(mcqFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                try {
                showResult();
                } catch (InvalidInputException ex) {
                    saveResult("MCQ", questions.length);
                    mcqFrame.dispose();
                }
            }
        });

        mcqFrame.setVisible(true);
        startTimer();
    }

    public void loadQuestion(int index) throws QuizNotFoundException {
       if (index < 0 || index >= questions.length) {
            throw new QuizNotFoundException("Question index out of range.");
        }
        questionArea.setText(questions[index]);
        for (int i = 0; i < options.length; i++) {
            options[i].setText(choices[index][i]);
        }
    }

    public boolean checkAnswer(int selectedAnswerIndex) {
        return selectedAnswerIndex == answers[currentQuestion];
    }
    public void showResult() throws InvalidInputException {
        mcqFrame.dispose();

        saveResult("MCQ", questions.length);

        double percentage = ((double) score / questions.length) * 100;
        String msg = "";
        if (percentage >= 80) {
            msg = "Excellent! You're a master!";
        } else if (percentage >= 60) {
            msg = "Good job! Keep it up!";
        } else {
            msg = "Keep practicing to improve!";
        }
        JOptionPane.showMessageDialog(mcqFrame, "Your score: " + score + "\n" + msg);
    }
}
