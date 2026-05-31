// IResultDisplay implemented by QuizResultPage
//// Used by: MCQModule, TrueFalseModule (they call showResult after quiz ends)

// Creator: Qhairunnisa 106089
// Tester: 

public interface IResultDisplay {

    // Displays the final result screen with username, score, and total questions
    void showResult(String userName, int score, int totalQuestions);

    // Displays motivational message based on score percentage
    void showMotivationalMessage(int score);

    // Navigates back to Home screen after viewing result
    void showHome();

    // Calculates score as a percentage 
    double getScorePercent(int score, int total);

    // Saves result entry to users.txt file (Data Storage)
    void saveResult(String userName, int score);
}