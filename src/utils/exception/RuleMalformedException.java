package utils.exception;

public class RuleMalformedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public RuleMalformedException(String m) {
        super(m);
    }
}