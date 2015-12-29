package utils.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-10-02
 * Time: 09:42
 */
public class DuplicateReferenceException extends RuntimeException {
    public DuplicateReferenceException() {
    }

    public DuplicateReferenceException(String message) {
        super(message);
    }

    public DuplicateReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateReferenceException(Throwable cause) {
        super(cause);
    }
}
