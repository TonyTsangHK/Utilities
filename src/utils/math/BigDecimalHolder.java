package utils.math;

import utils.data.SortedListAvl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;

public class BigDecimalHolder {
    public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);
    
    private SortedListAvl<BigDecimal> bigDecimals;
    
    private BigDecimal avg, sum, min, max;
    
    public BigDecimalHolder() {
        bigDecimals = new SortedListAvl<BigDecimal>();
    }
    
    public void addDecimal(BigDecimal dec) {
        bigDecimals.add(dec);
        if (sum == null) {
            sum = avg = min = max = dec;
        } else {
            sum = sum.add(dec);
            avg = sum.divide(new BigDecimal(bigDecimals.size()), DEFAULT_MATH_CONTEXT);
            if (min.compareTo(dec) > 0) {
                min = dec;
            }
            if (max.compareTo(dec) < 0) {
                max = dec;
            }
        }
    }
    
    public void removeDecimal(BigDecimal dec) {
        boolean removed = bigDecimals.remove(dec);
        if (removed) {
            if (bigDecimals.size() == 0) {
                sum = avg = min = max = null;
            } else {
                if (min.compareTo(dec) == 0) {
                    min = bigDecimals.getMin();
                }
                if (max.compareTo(dec) == 0) {
                    max = bigDecimals.getMax();
                }
                sum = sum.subtract(dec);
                avg = sum.divide(new BigDecimal(bigDecimals.size()), DEFAULT_MATH_CONTEXT);
            }
        }
    }
    
    public void addDecimals(BigDecimal ... decs) {
        for (BigDecimal dec : decs) {
            bigDecimals.add(dec);
            sum = sum.add(dec);
        }
        avg = sum.divide(new BigDecimal(bigDecimals.size()), DEFAULT_MATH_CONTEXT);
        min = bigDecimals.getMin();
        max = bigDecimals.getMax();
    }
    
    public void addDecimals(Collection<BigDecimal> decs) {
        for (BigDecimal dec : decs) {
            bigDecimals.add(dec);
            sum = sum.add(dec);
        }
        avg = sum.divide(new BigDecimal(bigDecimals.size()), DEFAULT_MATH_CONTEXT);
        min = bigDecimals.getMin();
        max = bigDecimals.getMax();
    }
    
    public void removeDecimals(BigDecimal ... decs) {
        for (BigDecimal dec : decs) {
            removeDecimal(dec);
        }
    }
    
    public BigDecimal getAvg() {
        return avg;
    }
    
    public BigDecimal getSum() {
        return sum;
    }
    
    public BigDecimal getMin() {
        return min;
    }
    
    public BigDecimal getMax() {
        return max;
    }
    
    public int getDecimalCount() {
        return bigDecimals.size();
    }
    
    public BigDecimal getDecimal(int i) {
        if (i >= 0 && i < bigDecimals.size()) {
            return bigDecimals.get(i);
        } else {
            return null;
        }
    }
    
    public void reset() {
        bigDecimals.clear();
        avg = sum = min = max = null;
    }
}
