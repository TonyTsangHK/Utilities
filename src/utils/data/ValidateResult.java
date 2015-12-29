package utils.data;

import java.io.Serializable;

public class ValidateResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final ValidateResult EMPTY_TRUE_RESULT = new ValidateResult(true),
        EMPTY_FALSE_RESULT = new ValidateResult(false);
    
    private boolean valid;
    private String resultMessage;
    
    public ValidateResult() {
        valid = false;
        resultMessage = "";
    }
    
    public ValidateResult(boolean result) {
        valid = result;
        resultMessage = "";
    }
    
    public ValidateResult(boolean result, String msg) {
        valid = result;
        resultMessage = msg;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getResultMessage() {
        return resultMessage;
    }
}
