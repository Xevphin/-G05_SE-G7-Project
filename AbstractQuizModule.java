import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Color;

// Creator: Abdul Rahim and Wan Adam
// Tester: Abdul Rahim, Qhairunnisa 106089
// Description: Abstract parent class managing shared quiz logic, timer, and file storage.

public abstract class AbstractQuizModule implements IQuiz {
    
    // Protected so child classes (MCQModule, TrueFalseModule) can access them directly
    protected String userName;
    protected LocalDateTime startTime;
    protected int currentQuestion;
    protected int score;
    protected Home parentHome; // Reference to the Home class for navigation
    protected Timer quizTimer; // Timer for the quiz
    protected JLabel timerLabel;
    protected int timeLeft;

    // --- Concrete Methods (Shared by all child classes) ---

    public void setUserName(String name) {
        this.userName = name;
    }

    public void startTimer() {
        this.startTime = LocalDateTime.now();
        System.out.println("Timer started for " + this.userName);

        this.timeLeft = timeLimit(); // Initialize time left based on the defined time limit

        if (timerLabel != null) {
            timerLabel.setText("⏳Time Left: " + formatTime(timeLeft));
        }

        quizTimer = new Timer(1000, e -> {
            timeLeft--;
            if (timerLabel != null) {
                timerLabel.setText("⏳Time Left: " + formatTime(timeLeft));
            }

            if (timeLeft <= 10){
                timerLabel.setForeground(Color.RED); // Change color to red when time is running out
            }

            if (timeLeft <= 0) {
            // Time's up - stop the timer and show results
            quizTimer.stop();
            JOptionPane.showMessageDialog(null, "Time's up! Your quiz has ended.", "Time Up", JOptionPane.INFORMATION_MESSAGE);
            try {
                showResult();
            } catch (InvalidInputException ex) {
                System.out.println("Error showing result: " + ex.getMessage());
            }
        }
        });

        quizTimer.setRepeats(true); // Ensure the timer repeats every second
        quizTimer.start();
    }

    //writes to both QuizScores.txt (leaderboard) and users.txt (home score)
    public void saveResult(String quizType, int totalQuestion) {

    // Handles the Data Storage rubric requirement (Writing to a text file)
         BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println("User: " + this.userName +
                        " | Type: " + quizType +
                        " | Score: " + this.score + "/" + totalQuestion);

            System.out.println("Score saved to QuizScores.txt.");

        } 
        catch (IOException e) {
            System.out.println("Error saving to QuizScores.txt: " + e.getMessage());
        }

        // 2. Also save to users.txt in the format loadUser() expects
        String quizLabel = quizType.equals("MCQ") ? "[MCQ Quiz]" : "[True/False Quiz]";

        try (FileWriter fw = new FileWriter("users.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(this.userName + " | " + quizLabel +
                        " Score: " + this.score + "/" + totalQuestion);

            System.out.println("Score saved to users.txt.");

        } catch (IOException e) {
            System.out.println("Error saving to users.txt: " + e.getMessage());
        }
    }


    public int timeLimit() {
        // For simplicity, we can set a fixed time limit (e.g., 1800 seconds)
        return 1800; // Time limit in seconds
    }

    private String formatTime(int seconds) { // Helper method to format time in MM:SS
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
    
    // By leaving these abstract, we force MCQModule and TrueFalseModule to write their own logic
    @Override
    public abstract void loadQuestion(int index) throws QuizNotFoundException;

    @Override
    public abstract boolean checkAnswer(int selectedAnswerIndex);

    @Override
    public abstract void showResult() throws InvalidInputException;
}
