package utils.data;

import utils.string.OutputFormatter;

public class ModificationData<T> implements ValueHolder<T> {
    private int modificationTimes;
    private boolean read;
    private T value;
    private OutputFormatter<T> valueFormater;
    
    public ModificationData(T val, int times) {
        this(val, times, false);
    }
    
    public ModificationData(T val, int times, boolean r) {
        value = val;
        modificationTimes = times;
        read = r;
    }
    
    public void setValue(T val) {
        value = val;
    }
    
    public T getValue() {
        return value;
    }

    public void setValueFormater(OutputFormatter<T> valueFormater) {
        this.valueFormater = valueFormater;
    }
    
    public void setModificationTimes(int t) {
        modificationTimes = t;
    }
    
    public int getModificationTimes() {
        return modificationTimes;
    }
    
    public void setRead(boolean r) {
        read = r;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public String toString() {
        if (value == null) {
            return "";
        } else if (valueFormater != null) {
            return valueFormater.formatOutput(value);
        } else {
            return value.toString();
        }
    }
    
    public boolean equals(String str) {
        return toString().equals(str);
    }

    public boolean equals(Object v) {
        if (v instanceof ModificationData) {
            return equals((ModificationData)v);
        } else {
            return toString().equals((v!=null)?v.toString():v);
        }
    }

    public boolean equals(ModificationData m) {
        return value.equals(m.getValue()) && modificationTimes == m.getModificationTimes();
    }
}
