import java.io.*;
import java.util.scanner;
//Contrubutor: WAN ADAM
/**Description: Core user profile management, scoring, file saving/loading, and gamification logic */

public class User implements Gamifiable {//implements the gamification interface

    private String name;
    private int score;
    private static final String DATA_NAME = "UserDatabase.txt";// The Database foundation for user data storage

    public User(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }
    //Implementation of the gamification interface methods to manage user points and badges based on their score.
    @Override
    public void addpoints(int points) {
        this.score += points;
        System.out.println(this.name + " earned " + points + " points ");
    }
    //Implementation of the gamification interface method to manage user points and badges based on their score, with an additional bonus parameter for extra points.
    @Override
    public void addpoints(int points, int bonus) {
        this.score += (points + bonus);
        System.out.println(this.name + " earned " + points + " points and a bonus of " + bonus + " points!");
    }
    //Implementation of the gamification interface method to award badges based on the user's total score, with different badge levels for different score thresholds.
    @Override
    public void awardBadge(int totalScore) {
        if (totalScore >= 18) {
            System.out.println("Badge Awarded: SDG Master!");
        } else if (totalScore >= 12) {
            System.out.println("Badge Awarded: SDG Advocate!");
        } else {
            System.out.println("Badge Awarded: SDG Explorer!");
        } 
    } 

    //Core Database Architecture

    public void saveUserData() {
        try (FileWriter fw = new FileWriter(DATA_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            // Save user data in the format: name,score 
            out.println(this.name + "," + this.score);
        } catch (IOException e) {
            System.out.println("An error occurred while saving user data: " + e.getMessage());
    
        }
    }
        
    //Check if user data exists in the file and load it into the user object, returning true if successful and false if the user is not found or an error occurs.
    public boolean loadUser() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return false;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].equals(this.name)) {
                    this.score = Integer.parseInt(parts[1]);
                    return true;// User found and loaded successfully
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("An error occurred while loading user data: " + e.getMessage());
    }
    return false;// User not found or an error occurred
    }

    public void resetScore() {
        this.score = 0;
        System.out.println(this.name + "'s score has been reset.");
    }
}