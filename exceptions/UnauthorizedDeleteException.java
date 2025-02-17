package exceptions;

// Exception when unauthorized user tries to delete a property
public class UnauthorizedDeleteException extends Exception {
    public UnauthorizedDeleteException(String message) {
        super(message);
    }
}