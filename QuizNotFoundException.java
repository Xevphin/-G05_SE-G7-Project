import java.lang.Exception;

public class QuizNotFoundException extends Exception {
    public QuizNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
