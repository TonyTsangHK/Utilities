package utils.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utils.data.SortedListAvl;

public class StatisticalBigDecimalHolder {
    public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);
    
    private SortedListAvl<BigDecimal> bigDecimals;
    
    private BigDecimal sum = BigDecimal.ZERO, min = null, max = null, mean = null, median = null;
    
    public StatisticalBigDecimalHolder() {
        bigDecimals = new SortedListAvl<BigDecimal>();
    }
    
    public StatisticalBigDecimalHolder(BigDecimal ... decs) {
        bigDecimals = new SortedListAvl<BigDecimal>();
        
        addDecimals(decs);
    }
    
    public StatisticalBigDecimalHolder(Collection<BigDecimal> decs) {
        bigDecimals = new SortedListAvl<BigDecimal>();
        
        addDecimals(decs);
    }
    
    public void addDecimal(BigDecimal decimal) {
        if (decimal != null) {
            bigDecimals.add(decimal);
            sum = sum.add(decimal);
            
            if (min == null || min.compareTo(decimal) > 0) {
                min = decimal;
            }
            if (max == null || max.compareTo(decimal) < 0) {
                max = decimal;
            }
        }
        mean = sum.divide(new BigDecimal(bigDecimals.size()), DEFAULT_MATH_CONTEXT);
        median = bigDecimals.get(bigDecimals.size()/2);
    }
    
    public void addDecimals(BigDecimal ... decimals) {
        if (decimals == null || decimals.length == 0) {
            return;
        }
        for (BigDecimal decimal : decimals) {
            bigDecimals.add(decimal);
            sum = sum.add(decimal);
        }
        
        refreshCaches();
    }
    
    public void addDecimals(Collection<BigDecimal> decimals) {
        if (decimals == null || decimals.size() == 0) {
            return;
        }
        for (BigDecimal decimal : decimals) {
            bigDecimals.add(decimal);
            sum = sum.add(decimal);
        }
        
        refreshCaches();
    }
    
    private void refreshCaches() {
        mean = sum.divide(new BigDecimal(bigDecimals.size()), DEFAULT_MATH_CONTEXT);
        median = bigDecimals.get(bigDecimals.size()/2);
        min = bigDecimals.getMin();
        max = bigDecimals.getMax();
    }
    
    public boolean removeDecimal(BigDecimal decimal) {
        if (bigDecimals.remove(decimal)) {
            sum = sum.subtract(decimal);
            
            refreshCaches();
            
            return true;
        } else {
            return false;
        }
    }
    
    public BigDecimal removeDecimals(int i) {
        if (i >= 0 && i < bigDecimals.size()) {
            sum = sum.subtract(bigDecimals.get(i));
            
            refreshCaches();
            
            return bigDecimals.remove(i);
        } else {
            return null;
        }
    }
    
    public void removeDecimals(BigDecimal ... decimals) {
        for (BigDecimal dec : decimals) {
            if (bigDecimals.remove(dec)) {
                sum.subtract(dec);
            }
        }
        refreshCaches();
    }
    
    public void removeDecimals(Collection<BigDecimal> decimals) {
        for (BigDecimal dec : decimals) {
            if (bigDecimals.remove(dec)) {
                sum = sum.subtract(dec);
            }
        }
        
        refreshCaches();
    }
    
    public void clear() {
        bigDecimals.clear();
        sum = BigDecimal.ZERO;
    }
    
    public boolean isEmpty() {
        return bigDecimals.isEmpty();
    }
    
    public BigDecimal getStandardDiviation() {
        int size = bigDecimals.size();
        
        if (size == 0 || size == 1) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal mean = getMean(), sumOfSquare = BigDecimal.ZERO;
            
            for (BigDecimal decimal : bigDecimals) {
                sumOfSquare = sumOfSquare.add(decimal.subtract(mean).pow(2));
            }
            
            return MathUtil.sqrt(sumOfSquare.divide(new BigDecimal(size), DEFAULT_MATH_CONTEXT));
        }
    }
    
    public BigDecimal getNormalizedMean() {
        int size = bigDecimals.size();
        
        if (size == 0) {
            return BigDecimal.ZERO;
        } else if (size == 1) {
            return bigDecimals.get(0);
        } else {
            StatisticalBigDecimalHolder holder = new StatisticalBigDecimalHolder(bigDecimals);
            
            BigDecimal sd = holder.getStandardDiviation();
            BigDecimal m = holder.mean, md = holder.median;
            
            while (sd.compareTo(new BigDecimal(4)) > 0) {
                int comp = m.compareTo(md);
                List<BigDecimal> decimalsToRemove = new ArrayList<BigDecimal>();
                
                if (comp > 0) {
                    for (int i = holder.bigDecimals.size()-1; i >= 0; i--) {
                        BigDecimal v = holder.bigDecimals.get(i);
                        if (v.compareTo(m) > 0) {
                            decimalsToRemove.add(v);
                        }
                    }
                } else if (comp < 0) {
                    for (int i = 0; i < holder.bigDecimals.size(); i++) {
                        BigDecimal v = holder.bigDecimals.get(i);
                        if (v.compareTo(m) < 0) {
                            decimalsToRemove.add(v);
                        }
                    }
                } else {
                    break;
                }
                
                if (decimalsToRemove.size() > 0) {
                    holder.removeDecimals(decimalsToRemove);
                    sd = holder.getStandardDiviation();
                    m  = holder.getMean();
                } else {
                    break;
                }
            }

            return holder.getMean();
        }
    }
    
    public BigDecimal getSum() {
        return sum;
    }
    
    public BigDecimal getMean() {
        return mean;
    }
    
    public BigDecimal getMin() {
        return min;
    }
    
    public BigDecimal getMax() {
        return max;
    }
    
    public BigDecimal getMedian() {
        return median;
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
}
