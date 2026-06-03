import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
  Creator: 
  Tester: 
  Description: Abstract parent class managing shared quiz logic, timer, and file storage.
 */
public abstract class AbstractQuizModule implements IQuiz {
    
    // Protected so child classes (MCQModule, TrueFalseModule) can access them directly
    protected String userName;
    protected LocalDateTime startTime;
    protected int currentQuestion;
    protected int score;
    protected Home parentHome; // Reference to the Home class for navigation

    // --- Concrete Methods (Shared by all child classes) ---

    public void setUserName(String name) {
        this.userName = name;
    }

    public void startTimer() {
        this.startTime = LocalDateTime.now();
        System.out.println("Timer started for " + this.userName);
    }

    // Handles the Data Storage rubric requirement (Writing to a text file)
    public void saveResult(String quizType, int totalQuestion) {
        // Using a try-catch block here helps build towards the Exception Handling rubric
        try (FileWriter fw = new FileWriter("QuizScores.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println("User: " + this.userName + 
                        " | Type: " + quizType + 
                        " | Score: " + this.score + "/" + totalQuestion);
            
            System.out.println("Score successfully saved to database.");
            
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }

    public int timeLimit() {
        // For simplicity, we can set a fixed time limit (e.g., 30 seconds)
        return 30; // Time limit in seconds
    }

    // --- Abstract Methods (Must be filled out by child classes) ---
    
    // By leaving these abstract, we force MCQModule and TrueFalseModule to write their own logic
    @Override
    public abstract void loadQuestion(int index) throws QuizNotFoundException;

    @Override
    public abstract boolean checkAnswer(int selectedAnswerIndex);

    @Override
    public abstract void showResult() throws InvalidInputException;
}
