import javax.swing.*;
import java.awt.*;

public class TrueFalseModule extends AbstractQuizModule {
    private JFrame frame;
    private JTextArea questionArea;
    private JRadioButton trueOption;
    private JRadioButton falseOption;
    private ButtonGroup group;
    private JButton nextButton;
    private String[] questions;
    private boolean[] answers;
    private Home parentHome;
    private static final Color CLR_BG = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90);
    private static final Color CLR_WHITE = Color.WHITE;

    public void setParent(Home h) {
        this.parentHome = h;
    }

    public TrueFalseModule() {
        // Initialize 30 SDG 4 Questions and their corresponding True/False answers
        questions = new String[] {
            //Theoretical Concepts
            "1. SDG 4 aims to provide free, equitable, and quality primary and secondary education for all children.",
            "2. The SDG 4 framework states that quality early childhood education has no measurable impact on a child's primary school readiness.",
            "3. Technical and Vocational Education and Training (TVET) includes tertiary-level university degrees as well as trade skills training.",
            "4. Target 4.4 focuses strictly on preparing students for academic research careers rather than entrepreneurship.",
            "5. Promoting gender equality in education requires eliminating barriers for both boys and girls across all learning levels.",
            "6. Functional numeracy means a person can perform advanced calculus and algebraic equations without a calculator.",
            "7. Education for Sustainable Development (ESD) includes teaching students about climate change, human rights, and global citizenship.",
            "8. Under SDG 4, school infrastructure only needs to be updated for digital technology, ignoring physical accessibility.",
            "9. A core objective of Target 4.c is to increase the global supply of qualified and professionally trained teachers.",
            "10. Global citizenship education teaches students to prioritize isolated national interests over interconnected global community goals.",
            "11. Educational equity means providing identical resources to every student regardless of their economic background or learning disabilities.",
            "12. Universal youth literacy is one of the explicit target benchmarks tracked under the UN SDG 4 framework.",
            "13. Vocational training streams are designed to replace secondary school literacy tracks entirely.",
            "14. The international expansion of student scholarships is primarily intended to benefit least developed countries (LDCs).",
            "15. Inclusive classroom policies mean that specialized support is only given to students living in wealthy urban centers.",
            
            //Practical Applications
            "16. Building a preschool in a rural village to teach children basic social skills before they turn six applies Target 4.2.",
            "17. If a community college offers a free 3-month basic coding certificate for unemployed youth, it is practically applying TVET frameworks.",
            "18. Rewriting local history textbooks to remove outdated gender biases against women applies school infrastructure metrics instead of equity metrics.",
            "19. Teaching adult marketplace vendors how to calculate profit margins and read supply receipts is a practical application of functional literacy and numeracy.",
            "20. A school group starting an active campus composting program is a practical execution of Education for Sustainable Development.",
            "21. Installing a wheelchair elevator next to a school's main staircase addresses Target 4.a regarding safe, inclusive, and accessible learning environments.",
            "22. Providing a full-tuition scholarship for a student from a small developing island nation to study medicine abroad fulfills an international cooperation target.",
            "23. Mandating that all public school teachers complete an annual workshop on interactive online learning tools addresses teacher qualification metrics.",
            "24. Organizing free night-classes at a community center for adults who never learned to read or write addresses early childhood development goals.",
            "25. A tech company offering free mentorship programs for young women building tech startups directly supports skills acquisition for entrepreneurship.",
            "26. Distributing free braille textbooks to low-income blind students across a district is an example of ensuring equitable access for vulnerable groups.",
            "27. A school board that ignores bullying and cyberbullying complaints on its formal portal is successfully maintaining a safe learning environment.",
            "28. A primary school using mathematical games involving counting physical coins is a practical method to improve youth numeracy.",
            "29. Funding an exchange program for Malaysian instructors to learn new teaching methodologies in Canada supports international cooperation for teacher training.",
            "30. Providing free school bus services to remote indigenous settlements ensures equitable secondary education access for vulnerable populations."
        };

        // Matching boolean answers array
        answers = new boolean[] {
            true, false, true, false, true, false, true, false, true, false, false, true, false, true, false, // Q1 - Q15
            true, true, false, true, true, true, true, true, false, true, true, false, true, true, true      // Q16 - Q30
        };

        // Setup GUI components
        frame = new JFrame("True/False Quiz");
        frame.setSize(380, 640);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(CLR_BG);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(CLR_BG);

        // Timer Label
        timerLabel = new JLabel("⏳Time Left: " + timeLimit() + "s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setForeground(CLR_PRIMARY);
        timerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(timerLabel);
        panel.add(Box.createVerticalStrut(15));

        // Question Area
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

        

        // Add components to frame and set layout
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

        if (this.quizTimer != null)
        {
            this.quizTimer.stop(); // Stop the timer when showing results
        }
        frame.dispose();

        saveResult("True/False", questions.length);

        QuizResultPage result = new QuizResultPage(this.userName, this.score, questions.length);
        result.setParent(this.parentHome);

       
    }
}