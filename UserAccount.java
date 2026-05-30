//Creator: 
//Testor: 
//Description: Manages user data, tracks scores, and handles gamifications elements.
public class UserAccount implements Gamifiable {
    
    private String username;
    private int totalScore;
    private String currentBadge;

    public UserAccount(String username) {
        this.username = username;
        this.totalScore = 0;
        this.currentBadge = "Novice Learner";
    }

    public String getUsername() {
        return this.username;
    }

    // Standard scoring
    @Override
    public void addPoints(int baseScore) {
        this.totalScore += baseScore;
        System.out.println(this.username + " earned " + baseScore + " points!");
    }

    // Overloaded scoring with speed bonus
    @Override
    public void addPoints(int baseScore, int timerBonus) {
        int total = baseScore + timerBonus;
        this.totalScore += total;
        System.out.println(this.username + " earned " + baseScore + " points + " + timerBonus + " speed bonus!");
    }

    // Badge logic based on score milestones
    @Override
    public void awardBadge(int finalScore) {
        if (finalScore >= 18) {
            this.currentBadge = "SDG Master";
        } else if (finalScore >= 12) {
            this.currentBadge = "SDG Advocate";
        } else {
            this.currentBadge = "SDG Explorer";
        }
        System.out.println("Badge Awarded: " + this.currentBadge);
    }
}
