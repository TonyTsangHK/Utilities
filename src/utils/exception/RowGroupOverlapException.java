package utils.exception;

public class RowGroupOverlapException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    protected int startRow, endRow;
    
    public RowGroupOverlapException(String m) {
        super(m);
    }
    
    public RowGroupOverlapException(String m, int s, int t) {
        super(m + ", row ranging from " + s + " to " + t);
        startRow = s;
        endRow = t;
    }
}
