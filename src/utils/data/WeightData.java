package utils.data;

import utils.math.MathUtil;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-09-22
 * Time: 15:14
 */
public class WeightData implements Comparable<WeightData> {
    public static final WeightData ZERO = new WeightData(BigDecimal.ZERO);

    private BigDecimal weightInOz;
    
    private BigDecimal cachedPound, cachedOz;

    public static WeightData ozOnly(BigDecimal oz) {
        if (oz == null) {
            return ZERO;
        } else {
            return new WeightData(oz);
        }
    }

    public static WeightData poundOnly(BigDecimal pound) {
        if (pound == null) {
            return ZERO;
        } else {
            return new WeightData(pound.multiply(MathUtil.SIXTEEN));
        }
    }

    private WeightData(BigDecimal weightInOz) {
        this.weightInOz = (weightInOz == null)? BigDecimal.ZERO : weightInOz;
        refreshCache();
    }

    public WeightData() {
        this(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public WeightData(BigDecimal pound, BigDecimal oz) {
        setWeight(pound, oz);
    }

    public WeightData(int pound, int oz) {
        this(new BigDecimal(pound), new BigDecimal(oz));
    }

    public WeightData(int pound, BigDecimal oz) {
        this(new BigDecimal(pound), oz);
    }

    public void setWeight(BigDecimal pound, BigDecimal oz) {
        this.weightInOz = BigDecimal.ZERO;

        if (pound != null) {
            this.weightInOz = this.weightInOz.add(pound.multiply(MathUtil.SIXTEEN));
        }

        if (oz != null) {
            this.weightInOz = this.weightInOz.add(oz);
        }

        refreshCache();
    }

    private void refreshCache() {
        if (cachedPound == null || cachedOz == null) {
            BigDecimal[] quotientAndRemainder = this.weightInOz.divideAndRemainder(MathUtil.SIXTEEN);

            this.cachedPound = quotientAndRemainder[0];
            this.cachedOz = quotientAndRemainder[1];
        }
    }

    private void resetCache() {
        cachedPound = null;
        cachedOz = null;
    }

    public BigDecimal getPound() {
        refreshCache();
        return cachedPound;
    }

    public BigDecimal getOz() {
        refreshCache();
        return cachedOz;
    }

    public BigDecimal getTotalPound() {
        return weightInOz.divide(MathUtil.SIXTEEN);
    }

    public BigDecimal getTotalOz() {
        return weightInOz;
    }

    public WeightData negate() {
        return new WeightData(weightInOz.negate());
    }

    public WeightData add(WeightData weightData) {
        return new WeightData(weightInOz.add(weightData.weightInOz));
    }

    public WeightData subtract(WeightData weightData) {
        return new WeightData(weightInOz.subtract(weightData.weightInOz));
    }

    public WeightData add(BigDecimal pound, BigDecimal oz) {
        return add(new WeightData(pound, oz));
    }

    public WeightData subtract(BigDecimal pound, BigDecimal oz) {
        return subtract(new WeightData(pound, oz));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightData)) return false;

        WeightData that = (WeightData) o;

        if (weightInOz != null ? !weightInOz.equals(that.weightInOz) : that.weightInOz != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return weightInOz != null ? weightInOz.hashCode() : 0;
    }

    @Override
    public int compareTo(WeightData o) {
        return weightInOz.compareTo(o.weightInOz);
    }
}
