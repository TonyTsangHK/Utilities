package utils.constants;

public enum HorizontalAlignment {
    CENTER("center"), RIGHT("right"), LEFT("left");
    
    public final String desc;
    
    private HorizontalAlignment(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String toString() {
        return desc;
    }
}
