package utils.data;

public class CodeValuePair<C, V> {
    private C code;
    private V value;
    
    public CodeValuePair(C code, V value) {
        setCode(code);
        setValue(value);
    }
    
    public void setCode(C code) {
        this.code = code;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
    
    public C getCode() {
        return code;
    }
    
    public V getValue() {
        return value;
    }
}
