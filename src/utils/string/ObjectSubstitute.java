package utils.string;

public class ObjectSubstitute<E> {
    private E value;
    private String displayText;
    
    public ObjectSubstitute(E value, String displayText) {
        this.value = value;
        this.displayText = displayText;
    }
    
    public E getValue() {
        return value;
    }
    
    public String getDisplayText() {
        return displayText;
    }
    
    @Override
    public String toString() {
        return displayText;
    }
}
