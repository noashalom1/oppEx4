package exceptions;

// Exception when unauthorized user tries to edit a property
public class UnauthorizedEditException extends Exception {
    public UnauthorizedEditException(String message) {
        super(message);
    }
}