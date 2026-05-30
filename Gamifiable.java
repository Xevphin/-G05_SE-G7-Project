//Creator: 
//Tester: 
//Description: Interface defining the required gamification methods.
public interface Gamifiable {
  void addPoints(int baseScore);
  void addPoints(int baseScore, int timerBonus); //Demonstrates Method Overloading
  void awardBadge(int totalScore);
}
