package utils.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Fraction number
 */
@SuppressWarnings("serial")
public class Fraction extends Number implements Cloneable, Comparable<Number> {
    /**
     * Mixed number
     */
    private final BigDecimal mixedNumber;
    
    /**
     * Numerator
     */
    private final BigDecimal numerator;
    
    /**
     * Denominator
     */
    private final BigDecimal denominator;
    
    /**
     * Construct a fraction from its string representation, not normalized
     * 
     * @param fractionString string representation of a fraction
     */
    public Fraction(String fractionString) {
        this(fractionString, false);
    }
    
    /**
     * Construct a fraction from its string representation
     * 
     * @param fractionString string representation of a fraction
     * @param normalize flag controlling the normalization operation
     */
    public Fraction(String fractionString, boolean normalize) {
        String v = "";
        BigDecimal mixedNumber = BigDecimal.ZERO, numerator = BigDecimal.ZERO, denominator = BigDecimal.ONE;
        @SuppressWarnings("unused")
        boolean mixedNumberSetted = false, numeratorSetted = false, denominatorSetted = false;
        for (int i = 0; i < fractionString.length(); i++) {
            char c = fractionString.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                v += c;
            } else {
                if (c == '-' || c == '－') {
                    if (v.length() > 0) {
                        mixedNumber = new BigDecimal(v);
                        mixedNumberSetted = true;
                        v = "";
                    } else if (c == '-' && v.length() == 0) {
                        v += c;
                    }
                } else if (c == '/' || c == '／') {
                    if (v.length() > 0) {
                        numerator         = new BigDecimal(v);
                        mixedNumberSetted = true;
                        numeratorSetted   = true;
                        
                        v                 = "";
                    }
                }
            }
        }
        if (v.length() > 0) {
            if (!mixedNumberSetted) {
                mixedNumber = new BigDecimal(v);
                mixedNumberSetted = true;
            } else {
                denominator = new BigDecimal(v);
                denominatorSetted = true;
            }
        }
        
        if (normalize) {
            BigDecimal[] normalizedComponents = normalize(mixedNumber, numerator, denominator);
            
            this.mixedNumber = normalizedComponents[0];
            this.numerator   = normalizedComponents[1];
            this.denominator = normalizedComponents[2];
        } else {
            this.mixedNumber = mixedNumber;
            this.numerator   = numerator;
            this.denominator = denominator;
        }
    }
    
    /**
     * Construct a fraction from integer value
     * 
     * @param value integer value
     */
    public Fraction(int value) {
        this(new BigDecimal(value), BigDecimal.ZERO, BigDecimal.ONE, false);
    }
    
    /**
     * Construct a fraction from decimal value, auto normalization
     * 
     * @param value decimal value
     */
    public Fraction(double value) {
        this(new BigDecimal(String.valueOf(value)));
    }
    
    /**
     * Construct a fraction from decimal value, auto normalization
     * 
     * @param decimal decimal value
     */
    public Fraction(BigDecimal decimal) {
        BigDecimal[] quotientAndRemainder = decimal.divideAndRemainder(BigDecimal.ONE);
        
        if (quotientAndRemainder[1].compareTo(BigDecimal.ZERO) == 0) {
            this.mixedNumber = decimal;
            this.numerator   = BigDecimal.ZERO;
            this.denominator = BigDecimal.ONE;
        } else {
            BigDecimal mixedNumber = quotientAndRemainder[0];
            
            String decimalPart = quotientAndRemainder[1].toString();
            
            int lastImportantIndex = decimalPart.length() - 1;
            
            while (decimalPart.charAt(lastImportantIndex) == '0') {
                lastImportantIndex--;
            }
            
            int dotIndex = decimalPart.indexOf(".");
            
            String numeratorString = decimalPart.substring(dotIndex+1, lastImportantIndex+1);
            
            int digit = numeratorString.length();
            
            StringBuilder denominatorStringBuilder = new StringBuilder("1");
            
            for (int i = 0; i < digit; i++) {
                denominatorStringBuilder.append('0');
            }
            
            String denominatorString = denominatorStringBuilder.toString();
            
            BigInteger numerator = new BigInteger(numeratorString), denominator = new BigInteger(denominatorString);
            
            BigInteger gcd = MathUtil.gcd(numerator, denominator);
            
            numerator   = numerator.divide(gcd);
            denominator = denominator.divide(gcd);
            
            this.mixedNumber = mixedNumber;
            this.numerator   = new BigDecimal(numerator);
            this.denominator = new BigDecimal(denominator);
        }
    }
    
    /**
     * Construct a fraction with integer numerator & denominator only, not normalized
     * 
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     */
    public Fraction(int numerator, int denominator) {
        this(numerator, denominator, false);
    }
    
    /**
     * Construct a fraction with decimal numerator & denominator only, not normalized
     * 
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     */
    public Fraction(double numerator, double denominator) {
        this(numerator, denominator, false);
    }
    
    /**
     * Construct a fraction with decimal numerator & denominator only, not normalized
     * 
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     */
    public Fraction(BigDecimal numerator, BigDecimal denominator) {
        this(BigDecimal.ZERO, numerator, denominator, false);
    }
    
    /**
     * Construct a fraction with integer numerator & denominator only
     * 
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     * @param normalize flag controlling normalization process
     */
    public Fraction(int numerator, int denominator, boolean normalize) {
        this(new BigDecimal(numerator), new BigDecimal(denominator), normalize);
    }
    
    /**
     * Construct a fraction with decimal numerator & denominator only
     * 
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     * @param normalize flag controlling normalization process
     */
    public Fraction(double numerator, double denominator, boolean normalize) {
        this(new BigDecimal(String.valueOf(numerator)), new BigDecimal(String.valueOf(denominator)), normalize);
    }
    
    /**
     * Construct a fraction with decimal numerator & denominator only
     * 
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     * @param normalize flag controlling normalization process
     */
    public Fraction(BigDecimal numerator, BigDecimal denominator, boolean normalize) {
        this(BigDecimal.ZERO, numerator, denominator, normalize);
    }
    
    /**
     * Construct a fraction with integer mixed number, numerator & denominator, not normalized
     * 
     * @param mixedNumber fraction mixed number
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     */
    public Fraction(int mixedNumber, int numerator, int denominator) {
        this(mixedNumber, numerator, denominator, false);
    }
    
    /**
     * Construct a fraction with decimal mixed number, numerator & denominator, not normalized
     * 
     * @param mixedNumber fraction mixed number
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     */
    public Fraction(double mixedNumber, double numerator, double denominator) {
        this(mixedNumber, numerator, denominator, false);
    }
    
    /**
     * Construct a fraction with decimal mixed number, numerator & denominator, not normalized
     * 
     * @param mixedNumber fraction mixed number
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     */
    public Fraction(BigDecimal mixedNumber, BigDecimal numerator, BigDecimal denominator) {
        this(mixedNumber, numerator, denominator, false);
    }
    
    /**
     * Construct a fraction with integer mixed number, numerator & denominator
     * 
     * @param mixedNumber fraction mixed number
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     * @param normalize flag controlling normalization process
     */
    public Fraction(int mixedNumber, int numerator, int denominator, boolean normalize) {
        this(new BigDecimal(mixedNumber), new BigDecimal(numerator), new BigDecimal(denominator), normalize);
    }
    
    /**
     * Construct a fraction with decimal mixed number, numerator & denominator
     * 
     * @param mixedNumber fraction mixed number
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     * @param normalize flag controlling normalization process
     */
    public Fraction(double mixedNumber, double numerator, double denominator, boolean normalize) {
        this(
                new BigDecimal(String.valueOf(mixedNumber)), new BigDecimal(String.valueOf(numerator)),
                new BigDecimal(String.valueOf(denominator)), normalize
        );
    }
    
    /**
     * Construct a fraction with decimal mixed number, numerator & denominator
     * 
     * @param mixedNumber fraction mixed number
     * @param numerator fraction numerator
     * @param denominator fraction denominator
     * @param normalize flag controlling normalization process
     */
    public Fraction(BigDecimal mixedNumber, BigDecimal numerator, BigDecimal denominator, boolean normalize) {
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            throw new NumberFormatException("Denominator must be non-zero!");
        }
        
        if (normalize) {
            BigDecimal[] normalizedComponents = normalize(mixedNumber, numerator, denominator);
            this.mixedNumber = normalizedComponents[0];
            this.numerator   = normalizedComponents[1];
            this.denominator = normalizedComponents[2];
        } else {
            this.mixedNumber = mixedNumber;
            this.numerator   = numerator;
            this.denominator = denominator;
        }
    }
    
    /**
     * Construct a fraction from decimal array.
     * 
     * @param fractionComponents array contains {mixedNumber, numerator, denominator}
     */
    private Fraction(BigDecimal[] fractionComponents) {
        if (fractionComponents.length != 3) {
            throw new NumberFormatException("Missing fraction components");
        }
        this.mixedNumber = fractionComponents[0];
        this.numerator   = fractionComponents[1];
        this.denominator = fractionComponents[2];
    }
    
    /**
     * Check whether this fraction is proper or not<br>
     * a fraction is proper when its numerator (absolute) is smaller than denominator
     * 
     * @return proper fraction check result
     */
    public boolean isProper() {
        return numerator.abs().compareTo(denominator.abs()) < 0;
    }
    
    /**
     * Check whether this fraction is normalized or not<br>
     * a fraction is normalized when it is proper and trimmed
     * 
     * @return normalization check result
     */
    public boolean isNormalized() {
        return isNormalized(mixedNumber, numerator, denominator);
    }
    
    /**
     * Get the mixed number
     * 
     * @return mixed number
     */
    public BigDecimal getMixedNumber() {
        return mixedNumber;
    }
    
    /**
     * Get the numerator
     * 
     * @return numerator
     */
    public BigDecimal getNumerator() {
        return numerator;
    }
    
    /**
     * Get the denominator
     * 
     * @return denominator
     */
    public BigDecimal getDenominator() {
        return denominator;
    }
    
    /**
     * Get decimal represented by this fraction
     * 
     * @return decimal value
     */
    public BigDecimal getDecimalValue() {
        return MathUtil.add(mixedNumber, MathUtil.divide(numerator, denominator));
    }
    
    /**
     * Convert to a improper fraction, if it can
     * 
     * @return improper fraction
     */
    public Fraction toImproperFraction() {
        return new Fraction(getWholeNumerator(), denominator);
    }
    
    /**
     * Convert to a proper fraction
     * 
     * @return proper fraction
     */
    public Fraction toProperFraction() {
        if (isProper()) {
            return clone();
        } else {
            BigDecimal n = numerator.remainder(denominator);
            
            BigDecimal m = MathUtil.add(MathUtil.divide(MathUtil.subtract(numerator, n), denominator), mixedNumber);
            
            return new Fraction(m, n, denominator);
        }
    }
    
    /**
     * Convert to reciprocal by swapping denominator with numerator
     * 
     * @return reciprocal, result not normalized
     */
    public Fraction toReciprocal() {
        return new Fraction(denominator, getWholeNumerator());
    }
    
    /**
     * Normalize this fraction
     * 
     * @return normalized fraction
     */
    public Fraction normalize() {
        return new Fraction(normalize(mixedNumber, numerator, denominator));
    }
    
    /**
     * Convert to an absolute fraction
     * 
     * @return absolute fraction
     */
    public Fraction abs() {
        return new Fraction(getWholeNumerator().abs(), denominator.abs()).normalize();
    }
    
    /**
     * Get the sign of this fraction
     * 
     * @return sign, -1 > negative, 1 > positive/zero
     */
    public int signum() {
        int n = getWholeNumerator().signum(), d = denominator.signum();
        
        if (n == 0) {
            return 0;
        } else if (n < 0) {
            return (d > 0)? -1 : 1;
        } else {
            return (d < 0)? -1 : 1;
        }
    }
    
    /**
     * Get whole numerator by combining mixed number with numerator
     * 
     * @return whole numerator
     */
    public BigDecimal getWholeNumerator() {
        return MathUtil.add(MathUtil.multiply(mixedNumber, denominator), numerator);
    }
    
    /**
     * Add this fraction to another fraction
     * 
     * @param fraction other fraction
     * 
     * @return result of summation
     */
    public Fraction add(Fraction fraction) {
        BigDecimal rn = 
            getWholeNumerator().multiply(fraction.getDenominator()).add(
                    fraction.getWholeNumerator().multiply(getDenominator())
            );
        
        BigDecimal commonDenominator = getDenominator().multiply(fraction.getDenominator());
        
        return new Fraction(normalize(BigDecimal.ZERO, rn, commonDenominator));
    }
    
    /**
     * Subtract this fraction with another fraction
     * 
     * @param fraction other fraction (subtrahend)
     * 
     * @return result of subtraction
     */
    public Fraction subtract(Fraction fraction) {
        BigDecimal rn = 
            getWholeNumerator().multiply(fraction.getDenominator()).subtract(
                    fraction.getWholeNumerator().multiply(getDenominator())
            );
        
        BigDecimal commonDenominator = getDenominator().multiply(fraction.getDenominator());
        
        return new Fraction(normalize(BigDecimal.ZERO, rn, commonDenominator));
    }
    
    /**
     * Multiply this fraction with another fraction
     * 
     * @param fraction other fraction
     * 
     * @return result of multiplication
     */
    public Fraction multiply(Fraction fraction) {
        BigDecimal n1 = getWholeNumerator(), n2 = fraction.getWholeNumerator(),
                   d1 = getDenominator(),    d2 = fraction.getDenominator();
        
        BigDecimal rn = n1.multiply(n2), rd = d1.multiply(d2);
        
        return new Fraction(normalize(BigDecimal.ZERO, rn, rd));
    }
    
    /**
     * Divide this fraction with another fraction
     * 
     * @param fraction other fraction (divisor)
     * @return result of division
     */
    public Fraction divide(Fraction fraction) {
        return this.multiply(fraction.toReciprocal());
    }
    
    /**
     * Calculate the power
     * 
     * @param pow exponent
     * 
     * @return power result
     */
    public Fraction pow(int pow) {
        return pow(new BigDecimal(pow));
    }
    
    /**
     * Calculate the power
     * 
     * @param pow exponent
     * 
     * @return power result
     */
    public Fraction pow(double pow) {
        return pow(new BigDecimal(String.valueOf(pow)));
    }
    
    /**
     * Calculate the power
     * 
     * @param pow exponent
     * 
     * @return power result
     */
    public Fraction pow(BigDecimal pow) {
        if (pow.signum() < 0) {
            return toReciprocal().pow(pow.abs());
        }
        
        BigDecimal[] parts = normalize(mixedNumber, numerator, denominator);
        
        BigDecimal wn = MathUtil.add(parts[1], MathUtil.multiply(parts[0], parts[2])),
                   d  = parts[2];
        
        BigDecimal nn = MathUtil.pow(wn, pow), nd = MathUtil.pow(d, pow);
        
        return new Fraction(normalize(BigDecimal.ZERO, nn, nd));
    }
    
    /**
     * Get a representation string of this fraction
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean hasMixedNumber = false;
        if (mixedNumber.compareTo(BigDecimal.ZERO) != 0) {
            if (mixedNumber.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                builder.append(mixedNumber);
            } else {
                builder.append(mixedNumber);
            }
            
            hasMixedNumber = true;
        }
        if (numerator.compareTo(BigDecimal.ZERO) != 0) {
            if (hasMixedNumber) {
                builder.append('-');
            }
            builder.append(numerator.toString());
            builder.append('/');
            builder.append(denominator.toString());
        }
        return (builder.length() == 0)? "0" : builder.toString();
    }
    
    /**
     * Clone this fraction
     * 
     * @return clone of this fraction
     */
    public Fraction clone() {
        return new Fraction(mixedNumber, numerator, denominator);
    }
    
    /**
     * Check if this fraction is equals to the provided fraction
     * 
     * @return result equals check
     */
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Fraction) {
            Fraction frac = (Fraction) o;
            
            return
                getWholeNumerator().multiply(frac.getDenominator()).compareTo(
                    frac.getWholeNumerator().multiply(getDenominator())
                ) == 0;
        } else if (o != null && o instanceof Number) {
            Number number = (Number) o;
            return MathUtil.isFloatingPointNumberEquals(number.doubleValue(), getDecimalValue().doubleValue());
        } else {
            return false;
        }
    }
    
    /**
     * Get a hash code of this fraction
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        return getDecimalValue().hashCode();
    }
    
    /**
     * Check whether the provided fraction components are normalized or not
     * 
     * @param mixedNumber mixed number
     * @param numerator numerator
     * @param denominator denominator
     * 
     * @return normalization check result
     */
    public static boolean isNormalized(BigDecimal mixedNumber, BigDecimal numerator, BigDecimal denominator) {
        return numerator.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0 &&
           denominator.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0 &&
           mixedNumber.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0 &&
           denominator.signum() > 0 && 
           (
                   (numerator.signum() < 0 && mixedNumber.signum() == 0) || 
                   (numerator.signum() > 0 && mixedNumber.compareTo(new BigDecimal("-1")) != 0) ||
                   numerator.signum() == 0
           ) &&
           MathUtil.gcd(numerator.toBigInteger(), denominator.toBigInteger()).abs().compareTo(BigInteger.ONE) == 0 && 
           numerator.abs().compareTo(denominator.abs()) < 0;
    }
    
    /**
     * Normalize the provided fraction components
     * 
     * @param mixedNumber mixed number
     * @param numerator numerator
     * @param denominator denominator
     * 
     * @return normalized fraction in array form: {mixedNumber, numerator, denominator}
     */
    private static BigDecimal[] normalize(BigDecimal mixedNumber, BigDecimal numerator, BigDecimal denominator) {
        if (isNormalized(mixedNumber, numerator, denominator)) {
            return new BigDecimal[] {mixedNumber, numerator, denominator};
        } else {
            BigDecimal numer = MathUtil.add(MathUtil.multiply(mixedNumber, denominator), numerator), deno = denominator;
            
            int maxScale = Math.max(deno.scale(), numer.scale());
            
            deno  = deno.scaleByPowerOfTen(maxScale);
            numer = numer.scaleByPowerOfTen(maxScale);
            
            BigDecimal gcd = new BigDecimal(MathUtil.gcd(numer.toBigInteger(), deno.toBigInteger()));
            
            if (gcd.abs().compareTo(BigDecimal.ONE) > 0) {
                numer = numer.divide(gcd);
                deno  = deno.divide(gcd);
            }
            
            if (deno.signum() < 0) {
                deno  = deno.abs();
                numer = numer.negate();
            }
            
            if (numer.abs().compareTo(deno.abs()) >= 0) {
                BigDecimal[] qar = numer.divideAndRemainder(deno);
                
                if (qar[1].signum() < 0 && qar[0].signum() != 0) {
                    qar[0] = qar[0].subtract(BigDecimal.ONE);
                    qar[1] = deno.add(qar[1]);
                } else if (qar[1].signum() > 0 && qar[0].compareTo(new BigDecimal("-1")) == 0) {
                    qar[0] = BigDecimal.ZERO;
                    qar[1] = qar[1].subtract(deno);
                }
                
                return new BigDecimal[] {qar[0], qar[1], deno};
            } else {
                return new BigDecimal[] {BigDecimal.ZERO, numer, deno};
            }
        }
    }
    
    /**
     * Get the integer value of this fraction
     * 
     * @return integer value, decimal part truncated
     */
    @Override
    public int intValue() {
        return getDecimalValue().intValue();
    }
    
    /**
     * Get the long value of this fraction
     * 
     * @return long value, decimal part truncated
     */
    @Override
    public long longValue() {
        return getDecimalValue().longValue();
    }
    
    /**
     * Get the float value of this fraction
     * 
     * @return float value
     */
    @Override
    public float floatValue() {
        return getDecimalValue().floatValue();
    }
    
    /**
     * Get the double value of this fraction
     * 
     * @return double value
     */
    @Override
    public double doubleValue() {
        return getDecimalValue().doubleValue();
    }
    
    /**
     * Compare this fraction to other number object
     * 
     * @return result of comparison, -1 > smaller, 0 > equals, 1 > greater
     */
    @Override
    public int compareTo(Number o) {
        if (o == null) {
            return 1;
        } else {
            if (o instanceof Fraction) {
                Fraction f = (Fraction) o;
                
                return
                    getWholeNumerator().multiply(f.getDenominator()).compareTo(
                            f.getWholeNumerator().multiply(getDenominator())
                    );
            } else {
                return getDecimalValue().compareTo(new BigDecimal(o.toString()));
            }
        }
    }
}
