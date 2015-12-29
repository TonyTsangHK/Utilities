package utils.string;

public class StringSubstitute {
    public enum DisplayType {
        TEXT("text"), VALUE("value");
        
        public final String desc;
        
        private DisplayType(String desc) {
            this.desc = desc;
        }
        
        @Override
        public String toString() {
            return this.desc;
        }
    }
    
    private String value, text;
    private DisplayType displayType;
    
    public StringSubstitute(String value) {
        this(value, value, DisplayType.TEXT);
    }
    
    public StringSubstitute(String value, String text) {
        this(value, text, DisplayType.TEXT);
    }
    
    public StringSubstitute(String value, String text, DisplayType type) {
        setValue(value);
        setText(text);
        setDisplayType(type);
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getText() {
        return text;
    }
    
    public void setDisplayType(DisplayType type) {
        if (type != null) {
            displayType = type;
        } else {
            displayType = DisplayType.TEXT;
        }
    }
    
    public DisplayType getDisplayType() {
        return displayType;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof StringSubstitute) {
            StringSubstitute s = (StringSubstitute) o;
            return s.text.equals(text) && s.value.equals(value) && s.displayType == displayType;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return (displayType == DisplayType.VALUE)? value : text;
    }
}
