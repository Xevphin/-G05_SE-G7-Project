//Creator: 
//Testor: 
//Description: Custom exception to catch errors if quiz data fails to load.
public class QuizNotFoundException extends Exception {
    public QuizNotFoundException(String message) {
        super(message);
    }
}
