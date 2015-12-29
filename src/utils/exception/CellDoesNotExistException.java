package utils.exception;

public class CellDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CellDoesNotExistException(String m) {
        super(m);
    }
}
