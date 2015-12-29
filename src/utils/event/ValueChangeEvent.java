package utils.event;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ValueChangeEvent<T> extends EventObject {
    public static final int VALUE_CHANGED = 2001;
    
    private T oldValue, newValue;
    
    private long when;
    
    private int type;
    
    public ValueChangeEvent(Object source, int ty, long when) {
        this(source, ty, when, null, null);
    }
    
    public ValueChangeEvent(Object source, int ty, long when,
            T oldValue, T newValue) {
        super(source);
        this.type = ty;
        this.when = when;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    public T getOldValue() {
        return oldValue;
    }
    
    public T getNewValue() {
        return newValue;
    }
    
    public int getType() {
        return type;
    }
    
    public long getWhen() {
        return when;
    }
}
