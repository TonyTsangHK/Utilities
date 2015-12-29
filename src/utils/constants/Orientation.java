package utils.constants;

public enum Orientation {
    HORIZONTAL("horizontal"), VERTICAL("vertical");
    
    public final String desc;
    
    private Orientation(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String toString() {
        return this.desc;
    }
}
