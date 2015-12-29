package utils.string;

public class ValuePair {
    private String name, value;
    
    public ValuePair() {}
    
    public ValuePair(String n, String v) {
        setName(n);
        setValue(v);
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void setValue(String v) {
        value = v;
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
}
