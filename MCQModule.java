import javax.swing.*;
import java.awt.*;


public class MCQModule extends AbstractQuizModule{
    private JFrame mcqFrame;
    private JTextArea questionArea;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;
    private String[] questions;
    private String[][] choices;
    private int[] answers;
    private Home parentHome;
    private static final Color CLR_BG = new Color(245, 248, 252);
    private static final Color CLR_PRIMARY = new Color(30, 120, 90); 
    private static final Color CLR_WHITE = Color.WHITE;

    public void setParent(Home h) {
        this.parentHome = h;
    }

    public MCQModule() {
        questions = new String[] {
            //Theoretical Concepts
            "1. What is the primary overarching goal of UN Sustainable Development Goal 4 (SDG 4)?",
            "2. Under the SDG 4 framework, free primary and secondary education must be:",
            "3. Why does SDG 4 place a heavy emphasis on early childhood development and pre-primary education?",
            "4. Which term describes equal access to affordable, high-quality technical, vocational, and tertiary training?",
            "5. What type of skill set is directly highlighted by Target 4.4 for employment and entrepreneurship?",
            "6. Eliminating gender disparities in education requires addressing inequalities at which levels?",
            "7. According to global targets, 'functional literacy and numeracy' implies that a citizen can:",
            "8. 'Education for Sustainable Development' (ESD) focuses heavily on teaching students about:",
            "9. What defines a safe, inclusive, and effective learning environment under SDG 4 metrics?",
            "10. What is a core indicator of international commitment to improving teacher training metrics?",
            "11. Equity in education equity baselines means that resources are distributed:",
            "12. Technical and Vocational Education and Training (TVET) focuses primarily on:",
            "13. Global citizenship education targets teaching students to see themselves as:",
            "14. What does the term 'vulnerable populations' refer to in inclusive classroom policies?",
            "15. International scholarship funding expansions are designed to assist students from:",
            
            //Practical Applications
            "16. A local NGO provides text-to-speech tools for blind students in a village. This directly promotes:",
            "17. A rural town builds a new pre-primary center so children can learn social skills. Which target is this implementing?",
            "18. A community college designs a 6-month certified welding program to help unemployed youths. This applies:",
            "19. A school changes its textbook examples so that engineering tasks feature both male and female characters equally. This fixes:",
            "20. An adult education program teaches local farmers how to calculate crop yields and read seed labels. This shows a practical use of:",
            "21. Students design a campus recycling system and run a workshop on water conservation. This is a practical example of:",
            "22. A school installs wheelchair ramps, accessible restrooms, and bright safety lighting across its campus. This applies:",
            "23. A university in a developed country offers 50 full-ride engineering grants to students from developing island states. This fulfills:",
            "24. A school district hosts monthly weekend workshops to train existing teachers on interactive digital tools. This addresses:",
            "25. A city provides free public bus passes exclusively to low-income students to ensure they can attend high school. This is a strategy for:",
            "26. A tech company launches a free boot camp teaching app development to young adults looking to start micro-businesses. This targets:",
            "27. A primary school introduces basic financial literacy games where 8-year-olds practice counting change and budgeting. This applies:",
            "28. A high school group forms a club that monitors and reports instances of cyberbullying to keep the online portal safe. This aligns with:",
            "29. An international agency funds teacher exchange programs between schools in Malaysia and Canada to share pedagogy methods. This applies:",
            "30. A community center sets up a night-school reading program for adults who never had the chance to complete school. This fulfills:"
        };

        choices = new String[][] {
            {"To focus on digital literacy", "To ensure inclusive, equitable, and quality education", "To eliminate university fees", "To mandate standardized testing"}, // Q1 - Ans: 1 (B)
            {"Highly selective and merit-based", "Restricted to vocational subjects", "Equitable, quality, and publicly funded", "Accessible only to urban citizens"}, // Q2 - Ans: 2 (C)
            {"To establish cognitive and social foundations early", "To shorten primary school years", "To lower national employment metrics", "To enforce coding before age five"}, // Q3 - Ans: 0 (A)
            {"Educational Monopolization", "Vocational Stratification", "Segregated Learning Portals", "Higher Education Accessibility"}, // Q4 - Ans: 3 (D)
            {"Basic memory retention skills", "Advanced athletic capabilities", "Relevant technical and vocational skills", "Historic linguistic proficiencies"}, // Q5 - Ans: 2 (C)
            {"Primary education only", "All levels of education and vocational training", "Higher university levels only", "Non-formal adult streams only"}, // Q6 - Ans: 1 (B)
            {"Recite historic literature by memory", "Write syntax in multiple languages", "Apply reading, writing, and math to daily life", "Pass an advanced calculus board"}, // Q7 - Ans: 2 (C)
            {"Climate change, human rights, and citizenship", "Local corporate accounting", "Abstract algorithmic models", "Isolated historical structures"}, // Q8 - Ans: 0 (A)
            {"Incorporating strict punitive codes", "Offering child-sensitive, disability-accessible, secure spaces", "Maximizing class sizes to cut costs", "Relying entirely on remote digital feeds"}, // Q9 - Ans: 1 (B)
            {"Increasing student-to-teacher ratios", "Lowering entry standards for instructors", "Expanding the supply of qualified, trained teachers", "Removing development workshops"}, // Q10 - Ans: 2 (C)
            {"Identically to everyone regardless of background", "According to needs of diverse, marginalized learners", "Based on school athletic performance", "Exclusively to high-performing urban districts"}, // Q11 - Ans: 1 (B)
            {"Purely historical and philosophical theory", "Standardized classical language memorization", "Abstract fine arts historical critique", "Practical, job-specific skills and competency"}, // Q12 - Ans: 3 (D)
            {"Active contributors to an interconnected society", "Members of an isolated local economy", "Passive observers of human rights shifts", "Workforce units with no environmental duties"}, // Q13 - Ans: 0 (A)
            {"Only students from wealthy urban sectors", "Students who bypass graduation tracks", "Persons with disabilities, indigenous peoples, and poor children", "Instructors without state certificates"}, // Q14 - Ans: 2 (C)
            {"Developed economic superpowers exclusively", "Developing nations, especially least developed countries", "Private high schools with high internal budgets", "Research institutions located in capitals"}, // Q15 - Ans: 1 (B)
            {"Academic standardization", "Vocational stratification", "Inclusive learning infrastructure", "International curriculum migration"}, // Q16 - Ans: 2 (C)
            {"Early Childhood Development and Care Foundations", "Higher Education Cooperation", "Technical Skills Entrepreneurship", "Adult Literacy Competency Tracks"}, // Q17 - Ans: 0 (A)
            {"Abstract Classical Education", "Macro-Economic Development Theory", "General Secondary Literacy Mapping", "Technical and Vocational Skills Acquisition"}, // Q18 - Ans: 3 (D)
            {"Infrastructure design layouts", "Gender bias and inequality in learning materials", "Numerical data tracking metrics", "International study scholarship allocations"}, // Q19 - Ans: 1 (B)
            {"Theoretical global citizenship indices", "Functional literacy and numeracy proficiencies", "Pre-primary cognitive baseline testing", "Advanced tertiary degree infrastructure"}, // Q20 - Ans: 1 (B)
            {"Technical vocational programming", "Basic primary education equity metrics", "Education for Sustainable Development", "Teacher training supply line management"}, // Q21 - Ans: 2 (C)
            {"Target 4.5: Gender Equalization", "Target 4.c: Qualified Teacher Supply lines", "Target 4.a: Safe and Inclusive Learning Environments", "Target 4.2: Pre-primary Framework Foundations"}, // Q22 - Ans: 2 (C)
            {"Local industrial tax compliance rules", "Expanding global scholarship pools", "Non-formal primary literacy adjustments", "Teacher compensation adjustments"}, // Q23 - Ans: 1 (B)
            {"Primary infrastructure funding", "Professional teacher training and qualification metrics", "Vocational entrepreneurship capital tracking", "Universal child registration mandates"}, // Q24 - Ans: 1 (B)
            {"Curriculum standard integration", "Lowering classroom operational costs", "Increasing global scholarship rates", "Ensuring equitable access for vulnerable groups"}, // Q25 - Ans: 3 (D)
            {"Early childhood care readiness metrics", "Universal global history curriculum", "Relevant skills for employment and entrepreneurship", "Primary classroom environmental designs"}, // Q26 - Ans: 2 (C)
            {"Universal tertiary degree standards", "Practical childhood numeracy skills", "Global sustainable development treaties", "International teacher exchange metrics"}, // Q27 - Ans: 1 (B)
            {"TVET technical tracking metrics", "Creating non-violent, inclusive, and safe learning spaces", "Early childhood pre-primary foundations", "International scholarship tracking systems"}, // Q28 - Ans: 1 (B)
            {"Local primary curriculum isolation", "International cooperation for teacher development and training", "Basic literacy and numeracy baseline quotas", "Private vocational student loops"}, // Q29 - Ans: 1 (B)
            {"Early childhood development milestones", "Secondary vocational engineering specializations", "Ensuring youth and adult literacy proficiencies", "Institutional school infrastructure layout designs"}  // Q30 - Ans: 2 (C)
        };

        // Scrambled Correct Answer Indices mapped to match array configurations perfectly
        answers = new int[] {
            1, 2, 0, 3, 2, 1, 2, 0, 1, 2, 1, 3, 0, 2, 1, // Q1 - Q15
            2, 0, 3, 1, 1, 2, 2, 1, 1, 3, 2, 1, 1, 1, 2  // Q16 - Q30
        };
        
        currentQuestion = 0;
        score = 0;

        // Setup GUI components (not fully implemented here)
        mcqFrame = new JFrame("MCQ Quiz");
        mcqFrame.setSize(380, 640);
        mcqFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mcqFrame.setLocationRelativeTo(null);
        mcqFrame.getContentPane().setBackground(CLR_BG);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(CLR_BG);

        // Initialize timer label and start the quiz timer
        timerLabel = new JLabel("⏳Time Left: " + timeLimit() + "s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setForeground(CLR_PRIMARY);
        timerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(timerLabel);
        panel.add(Box.createVerticalStrut(15));

        // Question area setup
        questionArea = new JTextArea();
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        questionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        questionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
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

    @Override
    public void showResult() throws InvalidInputException {

        if (this.quizTimer != null)
        {
            this.quizTimer.stop();
        }
        mcqFrame.dispose();

        saveResult("MCQ", questions.length);

        QuizResultPage result = new QuizResultPage(this.userName, this.score, questions.length);
        result.setParent(this.parentHome);
    }
}
