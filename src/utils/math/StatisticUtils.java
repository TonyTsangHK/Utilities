package utils.math;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class StatisticUtils {
    private StatisticUtils() {}
    
    public static BigDecimal getMean(List<BigDecimal> decs) {
        BigDecimal sum = BigDecimal.ZERO;
        
        for (BigDecimal dec : decs) {
            sum = sum.add(dec);
        }
        
        return MathUtil.divide(sum, new BigDecimal(decs.size()));
    }
    
    public static BigDecimal getMedian(List<BigDecimal> decs) {
        Collections.sort(decs);
        
        return decs.get(decs.size()/2);
    }
    
    public static BigDecimal getStandardDiviation(List<BigDecimal> decs) {
        BigDecimal mean = getMean(decs), sumOfSquare = BigDecimal.ZERO;
        
        for (BigDecimal dec : decs) {
            sumOfSquare = sumOfSquare.add(dec.subtract(mean).pow(2));
        }
        
        return MathUtil.sqrt(MathUtil.divide(sumOfSquare, new BigDecimal(decs.size())));
    }
}
