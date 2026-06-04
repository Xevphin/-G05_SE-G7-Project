// IResultDisplay implemented by QuizResultPage
//// Used by: MCQModule, TrueFalseModule (they call showResult after quiz ends)

// Creator: Qhairunnisa 106089
// Tester: Wan Adam 103014

public interface IResultDisplay {

    // Displays the final result screen with username, score, and total questions
    void showResult(String userName, int score, int totalQuestions);
    void showMotivationalMessage(int score);
    void showHome();
    double getScorePercent(int score, int total);

    // Saves result entry to users.txt file (Data Storage)
    void saveResult(String userName, int score);
}