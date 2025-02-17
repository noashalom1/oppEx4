package exceptions;

// Exception when trying to add a property with the same address
public class DuplicateAddressException extends Exception {
    public DuplicateAddressException(String message) {
        super(message);
    }
}
