package utils.exception;

public class CellMergeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    protected int mergeStart, mergeTo;
    
    public CellMergeException(String m) {
        super(m);
    }
    
    public CellMergeException(String m, int s, int t) {
        super(m + ", merging from " + s + " to " + t);
        mergeStart = s;
        mergeTo = t;
    }
}
