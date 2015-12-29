package utils.data;

public class DoubleRange {
    private Double fromValue, toValue;
    
    public DoubleRange(Double fromValue, Double toValue) {
        setFromValue(fromValue);
        setToValue(toValue);
    }
    
    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }
    
    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }
    
    public Double getFromValue() {
        return fromValue;
    }
    
    public Double getToValue() {
        return toValue;
    }
}