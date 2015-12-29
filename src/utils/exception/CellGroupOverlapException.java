package utils.exception;

public class CellGroupOverlapException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public CellGroupOverlapException(String s) {
        super(s);
    }
}
