/* Creator: 
   Tester: */
public interface IQuiz {
  void loadQuestion(int index) throws QuizNotFoundException;
  boolean checkAnswer(int selectedAnswerIndex);
  void showResult() throws InvalidInputException; 
}
