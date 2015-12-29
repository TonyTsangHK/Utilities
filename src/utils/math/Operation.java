package utils.math;

/**
 * Basic operation type of number manipulation (add, subtract, multiply & divide)
 */
public enum Operation {
    /**
     * Add operation type
     */
    ADD("add"),
    /**
     * Subtract operation type
     */
    SUBTRACT("subtract"),
    /**
     * Multiply operation type
     */
    MULTIPLY("multiply"),
    /**
     * Divide operation type
     */
    DIVIDE("divide");
    
    /**
     * String description of this operation
     */
    public final String desc;
    
    /**
     * Private constructor setting up description
     * 
     * @param desc description
     */
    private Operation(String desc) {
        this.desc = desc;
    }
    
    /**
     * Simply return the description
     * 
     * @return description of this operation
     */
    @Override
    public String toString() {
        return desc;
    }
}
