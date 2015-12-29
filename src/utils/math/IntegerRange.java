package utils.math;

public class IntegerRange implements Cloneable {
    private int min, max;
    
    public IntegerRange(int min, int max) {
        if (max >= min) {
            this.max = max;
            this.min = min;
        } else {
            this.min = max;
            this.max = min;
        }
    }
    
    public IntegerRange(IntegerRange range) {
        this.max = range.max;
        this.min = range.min;
    }
    
    public void setMin(int v) {
        if (v <= max) {
            min = v;
        } else {
            min = max;
            max = v;
        }
    }
    
    public void setMax(int v) {
        if (v >= min) {
            max = v;
        } else {
            max = min;
            min = v;
        }
    }
    
    public int getMin() {
        return min;
    }
    
    public int getMax() {
        return max;
    }
    
    public int getMiddle() {
        return (max+min)/2;
    }
    
    public int getRangeSize() {
        return max - min + 1;
    }
    
    public void shift(int v) {
        min += v;
        max += v;
    }
    
    public boolean contains(int v) {
        return v >= min && v <= max;
    }
    
    public boolean contains(IntegerRange range) {
        return range.min >= min && range.max <= max;
    }
    
    public boolean overlap(IntegerRange range) {
        if (min < range.min) {
            return max >= range.min;
        } else if (min > range.min) {
            return min <= range.max;
        } else {
            return true;
        }
    }
    
    public int compareTo(int v) {
        if (v < min) {
            return -1;
        } else if (v > max) {
            return 1;
        } else {
            return 0;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof IntegerRange) {
            IntegerRange range = (IntegerRange) o;
            
            return range.min == min && range.max == max;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return min * 31 ^ max;
    }
    
    @Override
    public String toString() {
        return "[" + min + " ... " + max + "]";
    }
    
    @Override
    public IntegerRange clone() {
        return new IntegerRange(this);
    }
}
