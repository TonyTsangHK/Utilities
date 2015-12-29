package utils.constants;

public enum MathValueType {
    MIN("min"), MAX("max");
    
    private final String desc;
    
    private MathValueType(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String toString() {
        return this.desc;
    }
}
