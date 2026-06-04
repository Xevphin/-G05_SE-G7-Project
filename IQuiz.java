// Creator: Abdul Rahim
// Tester: Abdul Rahim
public interface IQuiz {
  void loadQuestion(int index) throws QuizNotFoundException;
  boolean checkAnswer(int selectedAnswerIndex);
  void showResult() throws InvalidInputException; 
}
