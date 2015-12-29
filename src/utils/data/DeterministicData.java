package utils.data;

import utils.string.OutputFormatter;

public class DeterministicData<T> implements ValueHolder<T> {
    private boolean editable;
    private T value;
    private String extraExpression;
    private OutputFormatter<T> valueFormatter;
    
    public DeterministicData(T val) {
        this(val, true, "");
    }
    
    public DeterministicData(T val, boolean e) {
        this(val, e, "");
    }
    
    public DeterministicData(T val, boolean e, String eExp) {
        setValue(val);
        setEditable(e);
        setExtraExpression(eExp);
    }

    public void setValueFormatter(OutputFormatter<T> valueFormatter) {
        this.valueFormatter = valueFormatter;
    }

    public OutputFormatter<T> getValueFormatter() {
        return valueFormatter;
    }

    public void setValue(T val) {
        value = val;
    }
    
    public T getValue() {
        return value;
    }
    
    public void setExtraExpression(String e) {
        extraExpression = (e != null)? e : "";
    }
    
    public String getExtraExpression() {
        return extraExpression;
    }
    
    public void setEditable(boolean e) {
        editable = e;
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public String toString() {
        if (value == null) {
            return "";
        } else if (valueFormatter != null) {
            return valueFormatter.formatOutput(value);
        } else {
            return value.toString();
        }
    }
    
    public boolean equals(String str) {
        return toString().equals(str);
    }
    
    @SuppressWarnings("rawtypes")
    public boolean equals(Object v) {
        if (v instanceof DeterministicData) {
            return equals((DeterministicData) v);
        } else {
            return toString().equals(v);
        }
    }
    
    @SuppressWarnings("rawtypes")
    public boolean equals(DeterministicData m) {
        if (value == null && m.getValue() == null) {
            return true;
        } else if (value == null) {
            if (m.getValue() == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (m.getValue() != null) {
                return value.equals(m.getValue()) && editable == m.isEditable();
            } else {
                return false;
            }
        }
    }
}
