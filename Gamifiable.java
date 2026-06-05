//Creator: Wan Adam Danish
//Tester: Qhairunnisa Shahirah
//Description: Interface defining the required gamification methods.
public interface Gamifiable {
  void addPoints(int points);
  void addPoints(int points, int bonus); //Demonstrates Method Overloading
  void awardBadge(int totalScore);
}
