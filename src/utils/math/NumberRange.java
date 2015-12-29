package utils.math;

import java.math.BigDecimal;

public class NumberRange {
    private BigDecimal min, max;
    
    public NumberRange(int min, int max) {
        this(new BigDecimal(min), new BigDecimal(max));
    }
    
    public NumberRange(double min, double max) {
        this(new BigDecimal(String.valueOf(min)), new BigDecimal(String.valueOf(max)));
    }
    
    public NumberRange(String min, String max) {
        this(new BigDecimal(min), new BigDecimal(max));
    }
    
    public NumberRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new NumberFormatException("Invalid min/max values: min: " + min + ", max: " + max);
        } else {
            if (min.compareTo(max) < 0) {
                this.min = min;
                this.max = max;
            } else {
                this.min = max;
                this.max = min;
            }
        }
    }
    
    private void valueCheck(BigDecimal v) {
        if (v == null) {
            throw new NumberFormatException("Invalid value: " + v);
        }
    }
    
    public void setMin(int v) {
        setMin(new BigDecimal(v));
    }
    
    public void setMin(String v) {
        setMin(new BigDecimal(v));
    }
    
    public void setMin(BigDecimal v) {
        valueCheck(v);
        
        if (v.compareTo(max) < 0) {
            this.min = v;
        } else {
            this.min = this.max;
            this.max = v;
        }
    }
    
    public void setMax(int v) {
        setMax(new BigDecimal(v));
    }
    
    public void setMax(String v) {
        setMax(new BigDecimal(v));
    }
    
    public void setMax(BigDecimal v) {
        valueCheck(v);
        
        if (v.compareTo(min) > 0) {
            this.max = v;
        } else {
            this.max = this.min;
            this.min = v;
        }
    }
    
    public BigDecimal getMin() {
        return min;
    }
    
    public BigDecimal getMax() {
        return max;
    }
    
    public boolean contains(int v) {
        return contains(new BigDecimal(v));
    }
    
    public boolean contains(String v) {
        return contains(new BigDecimal(v));
    }
    
    public boolean contains(BigDecimal v) {
        valueCheck(v);
        
        return v.compareTo(min) >= 0 && v.compareTo(max) <= 0;
    }
    
    public boolean contains(NumberRange range) {
        return range.min.compareTo(min) >= 0 && range.max.compareTo(max) <= 0;
    }
    
    public boolean overlap(NumberRange range) {
        if (min.compareTo(range.min) < 0) {
            return max.compareTo(range.min) >= 0;
        } else if (min.compareTo(range.min) > 0) {
            return min.compareTo(range.max) <= 0;
        } else {
            return true;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof NumberRange) {
            NumberRange range = (NumberRange)o;
            
            return range.min.equals(min) && range.max.equals(max);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return min.hashCode() * 31 ^ max.hashCode();
    }
    
    public int compareTo(BigDecimal v) {
        valueCheck(v);
        
        if (v.compareTo(min) < 0) {
            return -1;
        } else if (v.compareTo(max) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return "[" + min + " ... " + max + "]";
    }
}
