package utils.math;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import utils.data.DataManipulator;
import utils.data.SortedListAvl;
import utils.string.StringUtil;

public class MathUtil {
    /**
     * Cache of BigDecimals
     */
    public static final BigDecimal TWO = new BigDecimal("2");
    public static final BigDecimal DOZEN = new BigDecimal("12"), TWELVE = DOZEN;
    public static final BigDecimal SIXTEEN = new BigDecimal("16");
    public static final BigDecimal HUNDRED = new BigDecimal("100");
    public static final BigDecimal THOUSAND = new BigDecimal("1000");

    // Use of secure random, default false
    private static boolean USE_SECURE_RANDOM = false;

    public static final int[] FIRST_PRIMES = {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71,
        73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151,
        157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233,
        239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317,
        331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419,
        421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503,
        509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607,
        613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701,
        709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811,
        821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911,
        919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013,
        1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091,
        1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181,
        1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277,
        1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361,
        1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451,
        1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531,
        1543, 1549, 1553, 1559, 1567, 1571, 1579, 1583, 1597, 1601, 1607, 1609,
        1613, 1619, 1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699,
        1709, 1721, 1723, 1733, 1741, 1747, 1753, 1759, 1777, 1783, 1787, 1789,
        1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877, 1879, 1889,
        1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997,
        1999, 2003, 2011, 2017, 2027, 2029, 2039, 2053
    };

    /**
     * Default iterations for square root calculation
     */
    public static final int DEFAULT_MAX_ITERATIONS = 50;
    
    /**
     * Default big decimal scale
     */
    public static final int DEFAULT_SCALE = 20;
    
    /**
     * Default big decimal tolerance for square root calculation
     */
    public static final BigDecimal DEFAULT_BIG_TOLERANCE = BigDecimal.ONE.scaleByPowerOfTen(-DEFAULT_SCALE);
    
    /**
     * Default precision for double division
     */
    public static final int DEFAULT_PRECISION = 6;
    
    /**
     * Default precision for big decimal division
     */
    public static final int DEFAULT_BIG_DECIMAL_PRECISION = 16;
    
    /**
     * Default tolerance for floating point number comparison, different smaller than tolerance is considered equals.
     */
    public static final double DEFAULT_TOLERANCE = 0.000001;
    
    /**
     * Default math context for big decimal division
     */
    public static final MathContext DEFAULT_MATH_CONTEXT =
        new MathContext(DEFAULT_BIG_DECIMAL_PRECISION, RoundingMode.HALF_UP);
    
    
    /**
     * private constructor restricting initiation
     */
    private MathUtil() {}
    
    /**
     * Random number generator, lazy initialization
     */
    private static Random randomGenerator;

    /**
     * Initialize random generator
     */
    private static void initRandom() {
        if (randomGenerator == null) {
            if (USE_SECURE_RANDOM) {
                try {
                    randomGenerator = SecureRandom.getInstanceStrong();
                } catch (NoSuchAlgorithmException e) {
                    randomGenerator = new SecureRandom();
                }
            } else {
                randomGenerator = new Random();
            }
        }
    }

    /**
     * Set usage of secure random
     * - It is fine to use Random, for its non-blocking nature
     * - Use secure random, if you really want TRULY RANDOM generation and it is provided at OS level, and it is also a computational random after all.
     *
     * @param useSecureRandom secure random flag
     */
    public static void setUseSecureRandom(boolean useSecureRandom) {
        if (USE_SECURE_RANDOM != useSecureRandom) {
            USE_SECURE_RANDOM = useSecureRandom;

            // re-initialize randomGenerator
            randomGenerator = null;
            initRandom();
        }
    }

    /**
     * Create a random generator with given seed
     *
     * @param seed given seed
     *
     * @return Random generate with given random seed.
     */
    public static Random createRandomGenerator(long seed) {
        return new Random(seed);
    }

    /**
     * Generate a random number
     *
     * @return random number (double) ranging from 0.0 (inclusive) to 0.1 (exclusive)
     */
    private static double random() {
        if (randomGenerator == null) {
            initRandom();
        }
        return randomGenerator.nextDouble();
    }
    
    /**
     * Add two double precision numbers
     * 
     * @param a operand 1
     * @param b operand 2
     * @return sum, result of summation
     */
    public static double add(Double a, Double b) {
        return add(a.doubleValue(), b.doubleValue());
    }

    /**
     * Add two double precision numbers with precision
     *
     * @param a operand 1
     * @param b operand 2
     * @param precision precision of result
     * @return sum, result of summation, the result will be trimmed(rounded) by precision
     */
    public static double add(Double a, Double b, int precision) {
        return add(a.doubleValue(), b.doubleValue(), precision);
    }

    /**
     * Add two double precision numbers
     *
     * @param a operand 1
     * @param b operand 2
     * @return sum, result of summation
     */
    public static double add(double a, double b) {
        return operate(a, b, Operation.ADD, DEFAULT_PRECISION);
    }

    /**
     * Add two double precision numbers with precision
     *
     * @param a operand 1
     * @param b operand 2
     * @param precision precision of result
     * @return sum, result of summation, the result will be trimmed(rounded) by precision
     **/
    public static double add(double a, double b, int precision) {
        return operate(a, b, Operation.ADD, precision);
    }

    /**
     * Add two big decimals, calling BigDecimal.add
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return sum, result of summation (BigDecimal.add)
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    /**
     * Subtract two double precision numbers
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return difference, result of subtraction
     */
    public static double subtract(Double a, Double b) {
        return subtract(a.doubleValue(), b.doubleValue());
    }

    /**
     * Subtract two double precision numbers with precision
     *
     * @param a operand 1 (minuend)
     * @param b operand 2 (subtrahend)
     * @param precision precision of result
     *
     * @return difference, result of subtraction, the result will be trimmed(rounded) by precision
     */
    public static double subtract(Double a, Double b, int precision) {
        return subtract(a.doubleValue(), b.doubleValue(), precision);
    }

    /**
     * Subtract two double precision numbers
     *
     * @param a operand 1 (minuend)
     * @param b operand 2 (subtrahend)
     *
     * @return difference, result of subtraction
     */
    public static double subtract(double a, double b) {
        return operate(a, b, Operation.SUBTRACT, DEFAULT_PRECISION);
    }

    /**
     * Subtract two double precision numbers with precision
     *
     * @param a operand 1 (minuend)
     * @param b operand 2 (subtrahend)
     * @param precision precision of result
     *
     * @return difference, result of subtraction, the result will be trimmed(rounded) by precision
     */
    public static double subtract(double a, double b, int precision) {
        return operate(a, b, Operation.SUBTRACT, precision);
    }

    /**
     * Subtract two big decimals, calling BigDecimal.subtract
     *
     * @param a operand 1 (minuend)
     * @param b operand 2 (subtrahend)
     *
     * @return difference, result of subtraction (BigDecimal.subtract)
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    /**
     * Multiply two double precision numbers
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return product, result of multiplication
     */
    public static double multiply(Double a, Double b) {
        return multiply(a.doubleValue(), b.doubleValue());
    }

    /**
     * Multiply two double precision numbers with precision
     *
     * @param a operand 1
     * @param b operand 2
     * @param precision precision of result
     *
     * @return product, result of multiplication, result will be trimmed(rounded) by precision
     */
    public static double multiply(Double a, Double b, int precision) {
        return multiply(a.doubleValue(), b.doubleValue(), precision);
    }

    /**
     * Multiply two double precision numbers with default precision (6)
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return product, result of multiplication
     */
    public static double multiply(double a, double b) {
        return operate(a, b, Operation.MULTIPLY, DEFAULT_PRECISION);
    }

    /**
     * Multiply two double precision numbers with precision
     *
     * @param a operand 1
     * @param b operand 2
     * @param precision precision of result
     *
     * @return product, result of multiplication, result will be trimmed(rounded) by precision
     */
    public static double multiply(double a, double b, int precision) {
        return operate(a, b, Operation.MULTIPLY, precision);
    }

    /**
     * Multiply two big decimals
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return product, result of multiplication
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    /**
     * Divide two double precision numbers
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     *
     * @return quotient, result of division
     */
    public static double divide(Double a, Double b) {
        return divide(a.doubleValue(), b.doubleValue());
    }

    /**
     * Divide two double precision numbers
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     * @param precision precision of result
     *
     * @return quotient, result of division trimmed(rounded) by precision
     */
    public static double divide(Double a, Double b, int precision) {
        return divide(a.doubleValue(), b.doubleValue(), precision);
    }

    /**
     * Divide two double precision numbers
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     *
     * @return quotient, result of division
     */
    public static double divide(double a, double b) {
        return operate(a, b, Operation.DIVIDE, DEFAULT_PRECISION);
    }

    /**
     * Divide two double precision numbers
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     * @param precision precision of result
     *
     * @return quotient, result of division trimmed(rounded) by precision
     */
    public static double divide(double a, double b, int precision) {
        return operate(a, b, Operation.DIVIDE, precision);
    }

    /**
     * Divide two big decimals
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     *
     * @return quotient, result of division
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, DEFAULT_BIG_DECIMAL_PRECISION, RoundingMode.HALF_UP);
    }

    /**
     * Add two number and return the sum in BigDecimal form
     *
     * @param n1 operand 1
     * @param n2 operand 2
     *
     * @return sum, result of addition
     */
    public static BigDecimal add(Number n1, Number n2) {
        BigDecimal operand1 = (n1 != null)? new BigDecimal(n1.toString()) : BigDecimal.ZERO,
                   operand2 = (n2 != null)? new BigDecimal(n2.toString()) : BigDecimal.ZERO;

        return operand1.add(operand2);
    }

    /**
     * Subtract two number and return the difference in BigDecimal form
     *
     * @param n1 operand 1 (minuend), null will be interpreted as ZERO!
     * @param n2 operand 2 (subtrahend), null will be interpreted as ZERO!
     *
     * @return difference, result of subtraction
     */
    public static BigDecimal subtract(Number n1, Number n2) {
        BigDecimal minuend = (n1 != null)? new BigDecimal(n1.toString()) : BigDecimal.ZERO,
                   subtrahend = (n2 != null)? new BigDecimal(n2.toString()) : BigDecimal.ZERO;

        return minuend.subtract(subtrahend);
    }

    /**
     * Multiply two number and return the product in BigDecimal form
     *
     * @param n1 operand 1, null will be interpreted as ZERO!
     * @param n2 operand 2, null will be interpreted as ZERO!
     *
     * @return product, result of multiplication
     */
    public static BigDecimal multiply(Number n1, Number n2) {
        BigDecimal operand1 = (n1 != null)? new BigDecimal(n1.toString()) : BigDecimal.ZERO,
                   operand2 = (n2 != null)? new BigDecimal(n2.toString()) : BigDecimal.ZERO;

        return operand1.multiply(operand2);
    }

    /**
     * Divide two number and return the quotient in BigDecimal form
     *
     * @param n1 operand 1, dividend, null will be interpreted as ZERO!!
     * @param n2 operand 2, divisor, null will be interpreted as ZERO!!
     *
     * @return quotient, result of division
     */
    public static BigDecimal divide(Number n1, Number n2) {
        if (n2 == null || n2.intValue() == 0) {
            throw new NumberFormatException("Divided by ZERO detected, ("+n1+" / "+n2+")");
        }

        BigDecimal dividend = (n1 != null)? new BigDecimal(n1.toString()) : BigDecimal.ZERO,
                   divisor = new BigDecimal(n2.toString());

        return dividend.divide(divisor, 16, RoundingMode.HALF_UP);
    }

    /**
     * Divide two number and return the quotient in BigDecimal form with scale and rounding mode
     *
     * @param n1 operand 1, dividend
     * @param n2 operand 2, divisor
     * @param scale scale of the quotient
     * @param roundingMode rounding mode
     *
     * @return quotient, result of division
     */
    public static BigDecimal divide(Number n1, Number n2, int scale, int roundingMode) {
        if (n2 == null || n2.intValue() == 0) {
            throw new NumberFormatException("Divided by ZERO detected, ("+n1+" / "+n2+")");
        }

        BigDecimal dividend = (n1 != null)? new BigDecimal(n1.toString()) : BigDecimal.ZERO,
                   divisor = new BigDecimal(n2.toString());

        return dividend.divide(divisor, scale, roundingMode);
    }

    /**
     * Divide two number and return the quotient in BigDecimal form with scale and rounding mode
     *
     * @param n1 operand 1, dividend
     * @param n2 operand 2, divisor
     * @param scale scale of the quotient
     * @param roundingMode rounding mode
     *
     * @return quotient, result of division
     */
    public static BigDecimal divide(Number n1, Number n2, int scale, RoundingMode roundingMode) {
        if (n2 == null || n2.intValue() == 0) {
            throw new NumberFormatException("Divided by ZERO detected, ("+n1+" / "+n2+")");
        }

        BigDecimal dividend = (n1 != null)? new BigDecimal(n1.toString()) : BigDecimal.ZERO,
                   divisor = new BigDecimal(n2.toString());

        return dividend.divide(divisor, scale, roundingMode);
    }

    /**
     * Divide two big decimals
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     * @param precision precision of result
     *
     * @return quotient, result of division trimmed(rounded) by precision
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int precision) {
        return a.divide(b, precision, RoundingMode.HALF_UP);
    }

    /**
     * Round up a double precision number
     *
     * @param a number to be rounded up
     * @param precision precision of result (decimal places)
     *
     * @return round up result to precision
     */
    public static double roundUp(double a, int precision) {
        long fac = (long)Math.pow(10, precision);
        return (double)Math.round(a * fac)/fac;
    }

    /**
     * Round up a double precision number
     *
     * @param a number to be rounded up
     * @param precision precision of result (decimal places)
     *
     * @return round up result to precision
     */
    public static double roundUp(Double a, int precision) {
        return roundUp(a.doubleValue(), precision);
    }

    /**
     * Round up a big decimal
     *
     * @param a decimal to be rounded up
     * @param precision precision of result (decimal places)
     *
     * @return round up result to precision
     */
    public static BigDecimal roundUp(BigDecimal a, int precision) {
        return a.setScale(precision, RoundingMode.HALF_UP);
    }

    /**
     * Ceiling a double precision number to an unit
     * (e.g. any non multiple of unit will be ceilinged to the nearest greater multiple)
     *
     * @param a number to be ceilinged
     * @param unit target unit
     *
     * @return ceilinged number of unit
     */
    public static double ceilOfUnit(double a, double unit) {
        return Math.ceil(a / unit) * unit;
    }

    /**
     * Ceiling a big decimal to an unit
     *
     * @param a decimal to be ceilinged
     * @param unit target unit
     *
     * @return ceilinged decimal of unit
     */
    public static BigDecimal ceilOfUnit(BigDecimal a, BigDecimal unit) {
        return a.divide(unit, 0, RoundingMode.CEILING).multiply(unit);
    }

    /**
     * Floor a double number precision to an unit
     * (e.g. any non multiple of unit will be floored to the nearest smaller multiple)
     *
     * @param a number to be floored
     * @param unit target unit
     *
     * @return floored number of unit
     */
    public static double floorOfUnit(double a, double unit) {
        return Math.floor(a / unit) * unit;
    }

    /**
     * Floor a big decimal to an unit
     *
     * @param a number to be floored
     * @param unit target unit
     *
     * @return floored decimal of unit
     */
    public static BigDecimal floorOfUnit(BigDecimal a, BigDecimal unit) {
        return a.divide(unit, 0, RoundingMode.FLOOR).multiply(unit);
    }

    /**
     * Rounding up a double precision number to an unit
     * (e.g. any non multiple of unit will be rounded up to the nearest multiple)
     *
     * @param a number to be rounded up
     * @param unit target unit
     *
     * @return rounded up number of unit
     */
    public static double roundUpToUnit(double a, double unit) {
        return Math.floor((a / unit) + 0.5) * unit;
    }

    /**
     * Rounding up a big decimal to an unit
     *
     * @param a decimal to be rounded up
     * @param unit target unit
     *
     * @return rounded up decimal of unit
     */
    public static BigDecimal roundUpToUnit(BigDecimal a, BigDecimal unit) {
        return a.divide(unit, 0, RoundingMode.HALF_UP).multiply(unit);
    }

    /**
     * Ceiling a double precision number to a precision
     *
     * @param a number to be ceilinged
     * @param precision precision of result
     *
     * @return ceilinged number of precision
     */
    public static double ceiling(double a, int precision) {
        return new BigDecimal(a).setScale(precision, BigDecimal.ROUND_CEILING).doubleValue();
    }

    /**
     * Ceiling a double precision number to a precision
     *
     * @param a number to be ceilinged
     * @param precision precision of result
     *
     * @return ceilinged number of precision
     */
    public static double ceiling(Double a, int precision) {
        return ceiling(a.doubleValue(), precision);
    }

    /**
     * Ceiling a double precision number to its integral value by calling Math.ceil
     *
     * @param a double precision number to be ceilinged
     *
     * @return ceilinged number
     */
    public static double ceiling(double a) {
        return Math.ceil(a);
    }

    /**
     * Ceiling a big decimal to its integral value
     *
     * @param a big decimal to be ceilinged
     *
     * @return ceilinged number
     */
    public static BigDecimal ceiling(BigDecimal a) {
        return a.divide(BigDecimal.ONE, 0, RoundingMode.CEILING);
    }

    /**
     * Floor a double precision number to a precision
     *
     * @param a double precision number to floored
     * @param precision precision of result
     *
     * @return floored number of precision
     */
    public static double floor(double a, int precision) {
        return new BigDecimal(a).setScale(precision, BigDecimal.ROUND_FLOOR).doubleValue();
    }

    /**
     * Floor a double precision number to a precision
     *
     * @param a double precision number to floored
     * @param precision precision of result
     *
     * @return floored number of precision
     */
    public static double floor(Double a, int precision) {
        return floor(a.doubleValue(), precision);
    }

    /**
     * Floor a double precision number to its integral value by calling Math.floor
     *
     * @param a double precision number to floored
     *
     * @return floored number
     */
    public static double floor(double a) {
        return Math.floor(a);
    }

    /**
     * Floor a big decimal to its integral value
     *
     * @param a big decimal to floored
     *
     * @return floor big decimal
     */
    public static BigDecimal floor(BigDecimal a) {
        return a.divide(BigDecimal.ONE, 0, RoundingMode.FLOOR);
    }

    /**
     * Natural log of a double precision number
     *
     * @param n number to be logged
     *
     * @return log result
     */
    public static double log(double n) {
        return Math.log(n);
    }

    /**
     * Log a double precision number of specific base
     *
     * @param n number to be logged
     * @param base base of log
     *
     * @return log result
     */
    public static double log(double n, double base) {
        return Math.log(n)/Math.log(base);
    }

    /**
     * Natural Log of a big decimal
     *
     * @param n big decimal to be logged
     *
     * @return log result
     */
    public static BigDecimal log(BigDecimal n) {
        return log(n, new BigDecimal(Math.E));
    }

    /**
     * Log a big decimal of specific base
     *
     * @param n big decimal to be logged
     * @param base base of log
     *
     * @return log result
     */
    public static BigDecimal log(BigDecimal n, BigDecimal base) {
        return log(n, base, DEFAULT_BIG_DECIMAL_PRECISION);
    }

    /**
     * Log a big decimal of specific base
     *
     * @param n big decimal to be logged
     * @param base base of log
     * @param precision result precision
     *
     * @return log result trimmed(rounded) by precision
     */
    public static BigDecimal log(BigDecimal n, BigDecimal base, int precision) {
        if (n.compareTo(base) == 0) {
            return BigDecimal.ONE;
        } else if (n.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ZERO;
        } else if (base.compareTo(BigDecimal.ZERO) > 0) {
            if (base.compareTo(BigDecimal.ONE) > 0) {
                if (n.compareTo(BigDecimal.ONE.divide(base, precision, RoundingMode.HALF_UP)) >= 0) {
                    BigDecimal result = BigDecimal.ZERO;

                    while (n.compareTo(base) >= 0) {
                        result = result.add(BigDecimal.ONE);
                        n = n.divide(base, precision, RoundingMode.HALF_UP);
                    }

                    if (n.compareTo(BigDecimal.ONE) > 0) {
                        BigDecimal fraction = new BigDecimal("0.5"), curr = base;
                        do {
                            curr = sqrt(curr);

                            int c = curr.compareTo(n);

                            if (c < 0) {
                                n = n.divide(curr, precision, RoundingMode.HALF_UP);
                                result = result.add(fraction);
                            } else if (c == 0) {
                                result = result.add(fraction);
                                break;
                            }

                            fraction = fraction.divide(TWO, precision, RoundingMode.FLOOR);
                        } while(fraction.compareTo(DEFAULT_BIG_TOLERANCE) > 0);
                    }

                    return result;
                } else {
                    return log(
                        BigDecimal.ONE.divide(n, precision, RoundingMode.HALF_UP), base, precision
                    ).negate();
                }
            } else if (base.compareTo(BigDecimal.ONE) < 0) {
                if (n.compareTo(base) < 0) {
                    BigDecimal result = BigDecimal.ZERO;

                    while (n.compareTo(base) <= 0) {
                        result = result.add(BigDecimal.ONE);
                        n = n.divide(base, precision, RoundingMode.HALF_UP);
                    }

                    if (n.compareTo(BigDecimal.ONE) != 0 && n.compareTo(base) > 0) {
                        BigDecimal fraction = new BigDecimal("0.5"), curr = base;
                        do {
                            curr = sqrt(curr);

                            int c = curr.compareTo(n);

                            if (c > 0) {
                                n = n.divide(curr, precision, RoundingMode.HALF_UP);
                                result = result.add(fraction);
                            } else if (c == 0) {
                                result = result.add(fraction);
                                break;
                            }

                            fraction = fraction.divide(TWO, precision, RoundingMode.FLOOR);
                        } while(fraction.compareTo(DEFAULT_BIG_TOLERANCE) > 0);
                    }

                    return result;
                } else {
                    return log(
                            BigDecimal.ONE.divide(n, precision, RoundingMode.HALF_UP), base, precision
                    ).negate();
                }
            } else {
                throw new IllegalArgumentException("Log base 1 is indetermine!");
            }
        } else {
            throw new IllegalArgumentException("Base must greater than 0");
        }
    }

    /**
     * Get initial approximation (guess) of square root of a big decimal
     *
     * @param n target number
     *
     * @return Initial approximation (guess) of root
     */
    private static BigDecimal getInitialApproximation(BigDecimal n) {
        BigInteger integerPart = n.toBigInteger();
        int length = integerPart.toString().length();
        if ((length % 2) == 0) {
            length--;
        }
        length /= 2;
        BigDecimal guess = BigDecimal.ONE.movePointRight(length);
        return guess;
    }

    /**
     * Square root a double precision number by calling Math.sqrt
     *
     * @param n target number
     *
     * @return Square root result
     */
    public static double sqrt(double n) {
        return Math.sqrt(n);
    }
    
    /**
     * Square root a big integer
     *
     * @param n target big integer
     *
     * @return Square root result
     */
    public static BigDecimal sqrt(BigInteger n) {
        return sqrt(new BigDecimal(n));
    }
    
    /**
    * Square root a big decimal
    *
    * @param n target big decimal
    *
    * @return square root result
    */
    public static BigDecimal sqrt(BigDecimal n) {
        return sqrt(n, DEFAULT_SCALE, DEFAULT_MAX_ITERATIONS, DEFAULT_BIG_TOLERANCE);
    }
    
    /**
    * Square root a big decimal will specified scale, iteration limit and tolerance
    *
    * @param n target big decimal
    * @param scale round up scale
    * @param maxIteration maximum iteration limit of guessing
    * @param tolerance error tolerance
    *
    * @return square root result
    */
    public static BigDecimal sqrt(BigDecimal n, int scale, int maxIteration, BigDecimal tolerance) {
        if (n.compareTo (BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else if (n.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal initialGuess = getInitialApproximation(n);

        BigDecimal lastGuess = BigDecimal.ZERO;
        BigDecimal guess = new BigDecimal(initialGuess.toString());

        int iterations = 0;
        boolean more = true;
        BigDecimal error = null;
        while (more) {
            lastGuess = guess;
            guess = n.divide(guess, scale, RoundingMode.HALF_UP);
            guess = guess.add(lastGuess).divide(TWO, scale, RoundingMode.HALF_UP);

            error = n.subtract(guess.multiply(guess));
            if (++iterations >= maxIteration) {
                more = false;
            } else if (lastGuess.equals(guess)) {
                more = error.abs().compareTo(tolerance) >= 0;
            }
        }
        return guess;
    }

    /**
    * Perform an operation on two double precision numbers
    *
    * @param a operand 1
    * @param b operand 2
    * @param operation operation type
    * @param precision precision of result
    *
    * @return operation result, result trimmed(rounded) by precision
    */
    private static double operate(double a, double b, Operation operation, int precision) {
        if (precision < 0) {
            precision = 0;
        }
        BigDecimal n1 = new BigDecimal(a), n2 = new BigDecimal(b);
        BigDecimal result = null;
        switch (operation) {
            case ADD:
                result = n1.add(n2);
                break;
            case SUBTRACT:
                result = n1.subtract(n2);
                break;
            case MULTIPLY:
                result = n1.multiply(n2);
                break;
            case DIVIDE:
                if (n2.doubleValue() != 0) {
                    result = n1.divide(n2, precision, BigDecimal.ROUND_HALF_UP);
                }
                break;
        }
        if (result == null) {
            return -1;
        } else {
            return result.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    /**
     * Get the highest multiple of factor within the provided maximum range
     *
     * @param factor target factor
     * @param maximumRange maximum range of multiple calculation
     *
     * @return the highest multiple within range of factor
     */
    public static double getHighestMultiple(double factor, double maximumRange) {
        return multiply(Math.floor(divide(maximumRange,factor)), factor);
    }

    /**
     * Get positing value of a double precision number only, otherwise 0 is returned
     *
     * @param n target double precision number
     *
     * @return positive value of provided input, 0 for any negative numbers
     */
    public static double getPositive(double n) {
        return (n > 0)? n : 0;
    }

    /**
     * Get negative value of double precision number only, otherwise 0 is returned
     *
     * @param n target double precision number
     *
     * @return negative value of provided input, 0 for any positive numbers
     */
    public static double getNegative(double n) {
        return (n < 0)? n : 0;
    }

    /**
     * Get the highest common factor of two big integer
     *
     * @param n1 big integer 1
     * @param n2 big integer 2
     *
     * @return the highest common factor
     */
    public static BigInteger hcf(BigInteger n1, BigInteger n2) {
        return gcd(n1, n2);
    }

    /**
     * Get the greatest common divisor of two big integer
     *
     * @param n1 big integer 1
     * @param n2 big integer 2
     *
     * @return the greatest common divisor
     */
    public static BigInteger gcd(BigInteger n1, BigInteger n2) {
        if (n2.compareTo(BigInteger.ZERO) == 0) {
            return n1;
        } else {
            return gcd(n2, n1.remainder(n2));
        }
    }

    /**
     * Get the highest common factor of two integer
     *
     * @param n1 integer 1
     * @param n2 integer 2
     * @return the highest common factor
     */
    public static int hcf(int n1, int n2) {
        return gcd(n1, n2);
    }

    /**
     * Get the greatest common divisor of two integer
     *
     * @param n1 integer 1
     * @param n2 integer 2
     *
     * @return the greatest common divisor
     */
    public static int gcd(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        } else {
            return gcd(n2, n1 % n2);
        }
    }

    /**
     * Get the least common multiple of two integer
     *
     * @param n1 integer 1
     * @param n2 integer 2
     *
     * @return the least common multiple
     */
    public static int lcm(int n1, int n2) {
        int small = 0, big = 0;
        if (n1 < n2) {
            small = n1;
            big   = n2;
        } else {
            small = n2;
            big   = n1;
        }
        if (big % small == 0) {
            return big;
        } else {
            int f = 2;
            int n = big * f;
            while (n % small != 0) {
                n = big * ++f;
            }
            return n;
        }
    }

    /**
     * Get the least common multiple of two big integer
     *
     * @param n1 big integer 1
     * @param n2 big integer 2
     *
     * @return the least common multiple
     */
    public static BigInteger lcm(BigInteger n1, BigInteger n2) {
        BigInteger small, big;
        if (n1.compareTo(n2) < 0) {
            small = n1;
            big   = n2;
        } else {
            small = n2;
            big   = n1;
        }
        if (big.remainder(small).compareTo(BigInteger.ZERO) == 0) {
            return big;
        } else {
            BigInteger f = new BigInteger("2");

            BigInteger n = big.multiply(f);

            while (n.remainder(small).compareTo(BigInteger.ZERO) != 0) {
                f = f.add(BigInteger.ONE);
                n = big.multiply(f);
            }
            return n;
        }
    }

    /**
     * Generate a random integer array
     *
     * @param min minimum value of random value
     * @param max maximum value of random value
     * @param size size of result array
     * @param distinct flag indicating every value should be distinct
     *
     * @return random integer array
     */
    public static int[] generateRandomIntegerArray(int min, int max, int size, boolean distinct) {
        if (size <= 0) {
            return new int[0];
        } else {
            int tMin = Math.min(min, max), tMax = Math.max(min, max);
            int possibleIntegers = max - min + 1;
            SortedListAvl<Integer> list = new SortedListAvl<>();
            for (int i = 0; i < size; i++) {
                if (distinct && i >= possibleIntegers) {
                    break;
                }
                Integer v = randomInteger(tMin, tMax);

                while (i > 0 && distinct && list.contains(v)) {
                    v = randomInteger(tMin, tMax);
                }
                list.add(v);
            }
            Object[] arr = list.toArray();
            int[] result = new int[size];
            for (int i = 0; i < arr.length; i++) {
                result[i] = (Integer) arr[i];
            }
            return result;
        }
    }

    /**
     * Generate a random integer array based on the given random generator, for consistent random generation
     *
     * @param min minimum bound of the random value (inclusive)
     * @param max maximum bound of the random value (inclusive)
     * @param size size of the array
     * @param distinct distinct flag
     * @param randomGenerator given random generator
     *
     * @return random integer array
     */
    public static int[] generateRandomIntegerArray(int min, int max, int size, boolean distinct, Random randomGenerator) {
        if (size <= 0) {
            return new int[0];
        } else {
            int tMin = Math.min(min, max), tMax = Math.max(min, max);
            int possibleIntegers = max - min + 1;
            SortedListAvl<Integer> list = new SortedListAvl<>();
            for (int i = 0; i < size; i++) {
                if (distinct && i >= possibleIntegers) {
                    break;
                }
                Integer v = randomInteger(tMin, tMax, randomGenerator);

                while (i > 0 && distinct && list.contains(v)) {
                    v = randomInteger(tMin, tMax, randomGenerator);
                }
                list.add(v);
            }
            Object[] arr = list.toArray();
            int[] result = new int[size];
            for (int i = 0; i < arr.length; i++) {
                result[i] = (Integer) arr[i];
            }
            return result;
        }
    }

    /**
     * Generate a random number array
     *
     * @param min minimum value of random value
     * @param max maximum value of random value
     * @param precision precision of random value
     * @param size size of result array
     * @param distinct flag indicating every value should be distinct
     *
     * @return random number array
     */
    public static double[] generateRandomNumberArray(
        double min, double max, int precision, int size, boolean distinct
    ) {
        if (size <= 0) {
            return new double[0];
        } else {
            double tMin = Math.min(min, max), tMax = Math.max(min, max);
            SortedListAvl<Double> list = new SortedListAvl<Double>();
            for (int i = 0; i < size; i++) {
                Double v = randomNumber(tMin, tMax, precision);
                while (i > 0 && distinct && list.contains(v)) {
                    v = randomNumber(tMin, tMax, precision);
                }
                list.add(v);
            }
            Object[] arr = list.toArray();
            double[] result = new double[size];
            for (int i = 0; i < arr.length; i++) {
                result[i] = (Double) arr[i];
            }
            return result;
        }
    }

    /**
     * Generat a random number array with given random generator for consistent result
     *
     * @param min minimun bound of random value
     * @param max maximum bound of random value
     * @param precision precision of random value
     * @param size size of result array
     * @param distinct distinct flag
     * @param randomGenerator given random generator
     *
     * @return random number array
     */
    public static double[] generateRandomNumberArray(
        double min, double max, int precision, int size, boolean distinct, Random randomGenerator
    ) {
        if (size <= 0) {
            return new double[0];
        } else {
            double tMin = Math.min(min, max), tMax = Math.max(min, max);
            SortedListAvl<Double> list = new SortedListAvl<Double>();
            for (int i = 0; i < size; i++) {
                Double v = randomNumber(tMin, tMax, precision, randomGenerator);
                while (i > 0 && distinct && list.contains(v)) {
                    v = randomNumber(tMin, tMax, precision, randomGenerator);
                }
                list.add(v);
            }
            Object[] arr = list.toArray();
            double[] result = new double[size];
            for (int i = 0; i < arr.length; i++) {
                result[i] = (Double) arr[i];
            }
            return result;
        }
    }

    /**
     * Generate a random integer
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random integer
     */
    public static int randomInteger(int min, int max) {
        if (min == max) {
            return min;
        }
        return calculateResult(random(), min, max);
    }

    /**
     * Generate a random integer with given random generator, for consistent result
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param randomGenerator given random generator
     *
     * @return random integer
     */
    public static int randomInteger(int min, int max, Random randomGenerator) {
        if (min == max) {
            return min;
        }
        return calculateResult(randomGenerator.nextDouble(), min, max);
    }

    /**
     * Generate a random long integer
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random long integer
     */
    public static long randomLong(long min, long max) {
        if (min == max) {
            return min;
        }
        return calculateResult(random(), min, max);
    }

    /**
     * Generate a random long integer with given random generator
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param randomGenerator given random generator
     *
     * @return random long integer
     */
    public static long randomLong(long min, long max, Random randomGenerator) {
        if (min == max) {
            return min;
        }
        return calculateResult(randomGenerator.nextDouble(), min, max);
    }

    /**
     * Calculate final result from random number and provided min max range
     * If range overflow use long for calculation
     *
     * @param rand double random number, ranging from 0.0 and 1.0, generated from random generator
     * @param min lower bound value
     * @param max upper bound value
     * @return formalized result
     */
    private static int calculateResult(double rand, int min, int max) {
        int range = max - min + 1;

        if (range <= 0) {
            // overflow, use long for calculation
            long longMax = (long)max, longMin = (long)min;

            return (int)(random() * (longMax - longMin + 1) + longMin);
        } else {
            return (int) ((random() * (max - min + 1)) + min);
        }
    }

    /**
     * Calculate final result from random number and provided min max range
     * If range overflow use BigDecimal for calculation.
     *
     * @param rand random double random number, ranging from 0.0 and 1.0, generated from random generator
     * @param min lower bound value
     * @param max upper bound value
     * @return formalized result
     */
    private static long calculateResult(double rand, long min, long max) {
        long range = max - min + 1;

        if (range <= 0) {
            // overflow, use BigDecimal for calculation
            BigDecimal bigMax = new BigDecimal(String.valueOf(max)), bigMin = new BigDecimal(String.valueOf(min)),
                bigRand = new BigDecimal(String.valueOf(rand));

            return bigMax
                .subtract(bigMin)
                .add(BigDecimal.ONE)
                .multiply(bigRand)
                .add(bigMin)
                .longValue();
        } else {
            return (long)(rand * (max - min + 1) + min);
        }
    }

    /**
     * Generate a random number
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random number
     */
    public static double randomNumber(double min, double max) {
        if (min == max) {
            return min;
        }
        return randomNumber(min, max, 6);
    }

    /**
     * Generate a random number based on given random generator for consistent output
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param randomGenerator given random generator
     *
     * @return random number
     */
    public static double randomNumber(double min, double max, Random randomGenerator) {
        if (min == max) {
            return min;
        }
        return randomNumber(min, max, 6, randomGenerator);
    }

    /**
     * Generate a random number
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param precision precision of result
     *
     * @return random number
     */
    public static double randomNumber(double min, double max, int precision) {
        return roundUp(random() * (max - min) + min, precision);
    }

    /**
     * Generate a random number with given random generator
     *
     * @param min minmum bound
     * @param max maxmum bound
     * @param precision precision of result
     * @param randomGenerator given random generator
     *
     * @return random number
     */
    public static double randomNumber(double min, double max, int precision, Random randomGenerator) {
        return roundUp(randomGenerator.nextDouble() * (max - min) + min, precision);
    }

    /**
     * Get digit count of an integer
     * @param val target integer
     *
     * @return digit count of provided integer value
     */
    public static int getDigitCount(int val) {
        return Integer.toString(val).length();
    }

    /**
     * Square of number
     *
     * @param v target number
     *
     * @return square of target number
     */
    public static double square(double v) {
        return v * v;
    }

    /**
     * Get the length of two points (x1, y1) & (x2, y2)
     *
     * @param x1 x coordinate of point1
     * @param y1 y coordinate of point1
     * @param x2 x coordinate of point2
     * @param y2 y coordinate of point2
     *
     * @return length of two points
     */
    public static double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(square(x1 - x2) + square(y1 - y2));
    }

    /**
     * Get the length of two points
     *
     * @param p1 point 1
     * @param p2 point 2
     *
     * @return length of two points
     */
    public static double getLength(Point2D p1, Point2D p2) {
        double x1 = p1.getX(), x2 = p2.getX();
        double y1 = p1.getY(), y2 = p2.getY();

        return getLength(x1, y1, x2, y2);
    }
    
    /**
    * Get the length of a point to a line segment (right angled joint)
    *
    * @param p1 point 1 of line
    * @param p2 point 2 of line
    * @param p target point
    *
    * @return length of target point to a line segment
    */
    public static double getPointSegmentDistance(Point2D p1, Point2D p2, Point2D p) {
        return getPointSegmentDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p.getX(), p.getY());
    }
    
    /**
     * Get the length of a point(px, py) to a line segment (x1, y1) & (x2, y2) (right angled joint)
     *
     * @param x1 x coordinate of point 1
     * @param y1 y coordinate of point 1
     * @param x2 x coordinate of point 2
     * @param y2 y coordinate of point 2
     * @param px x coordinate of target point
     * @param py y coordinate of target point
     *
     * @return length of target point to a line segment
     */
    public static double getPointSegmentDistance(
        double x1, double y1, double x2, double y2, double px, double py
    ) {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        double dotprod = px * x2 + py * y2;
        double projlenSq;
        if (dotprod <= 0.0) {
            projlenSq = 0.0;
        } else {
            px = x2 - px;
            py = y2 - py;
            dotprod = px * x2 + py * y2;
            if (dotprod <= 0.0) {
                projlenSq = 0.0;
            } else {
                projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
            }
        }
        double lenSq = px * px + py * py - projlenSq;
        if (lenSq < 0) {
            lenSq = 0;
        }
        return Math.sqrt(lenSq);
    }
    
    /**
     * Get right angled triangle's hypothesis length squared with known side lengths 
     * Pythagorean Theorem: c * c = a * a + b * b
     * 
     * @param side1 side length 1
     * @param side2 side length 2
     * 
     * @return hypothesis length square
     */
    public static double getHypothesisSquare(double side1, double side2) {
        return side1 * side1 + side2 * side2;
    }
    
    /**
     * Get right angled triangle's hypothesis length with known side lengths
     * 
     * @param side1 side length 1
     * @param side2 side length 2
     * 
     * @return hypothesis length
     */
    public static double getHypothesis(double side1, double side2) {
        return Math.sqrt(getHypothesisSquare(side1, side2));
    }
    
    /**
     * Get right angled triangle's side length squred with known hypothesis and one side length
     * 
     * @param hypo hypothesis length
     * @param side side length
     * 
     * @return side length squared
     */
    public static double getTriangleSideLengthSquare(double hypo, double side) {
        return hypo * hypo - side * side;
    }
    
    /**
     * Get right angled triangle's side length with known hypothesis and one side length
     * 
     * @param hypo hypothesis length
     * @param side side length
     * 
     * @return side length
     */
    public static double getTriangleSideLength(double hypo, double side) {
        return Math.sqrt(getTriangleSideLengthSquare(hypo, side));
    }
    
    /**
     * Normalize an angle (in radian)<br>
     * any value smaller than 0 or greater than 2 x PI will be normalized to range 0 ~ 2 x PI
     * 
     * @param rad target angle (in radian)
     * @return result of normalization (in radian)
     */
    public static double normalizeAngleRadian(double rad) {
        if (rad < 0) {
            return rad % (Math.PI * 2) + (Math.PI * 2);
        } else if (rad == Math.PI * 2) {
            return 0;
        } else {
            return rad % (Math.PI * 2);
        }
    }
    
    /**
     * Normalize an angle (in degree)<br>
     * any value smaller than 0 or greater than 360 will be normalized to range 0 ~ 360
     * 
     * @param degree target angle (in degree)
     * 
     * @return result of normalization (in degree)
     */
    public static double normalizeAngleDegree(double degree) {
        if (degree < 0) {
            return (degree % 360) + 360;
        } else if (degree == 360) {
            return 0;
        } else {
            return degree % 360;
        }
    }
    
    /**
     * Checking whether the difference of two double precision number are within default tolerance
     * 
     * @param n1 number 1
     * @param n2 number 2
     * 
     * @return result of checking
     */
    public static boolean isFloatingPointNumberEquals(double n1, double n2) {
        return Math.abs(n1-n2) <= DEFAULT_TOLERANCE;
    }
    
    /**
     * Checking whether the difference of two double precision number are within provided tolerance
     * 
     * @param n1 number 1
     * @param n2 number 2
     * @param tolerance tolerance of difference to say two numbers are equals
     * 
     * @return result of checking
     */
    public static boolean isFloatingPointNumberEquals(double n1, double n2, double tolerance) {
        return Math.abs(n1 - n2) <= tolerance;
    }
    
    /**
     * Checking whether the difference of two decimals are within provided tolerance
     * 
     * @param d1 decimal 1
     * @param d2 decimal 2
     * @param tolerance tolerance of difference to say two decimals are equals
     * 
     * @return result of checking
     */
    public static boolean isDecimalEquals(BigDecimal d1, BigDecimal d2, BigDecimal tolerance) {
        return d1.subtract(d2).abs().compareTo(tolerance) <= 0;
    }
    
    /**
     * Checking whether the difference of two decimals are within provided tolerance
     * 
     * @param d1 decimal 1
     * @param d2 decimal 2
     * @param tolerance tolerance of difference to say two decimals are equals
     * 
     * @return result of checking
     */
    public static boolean isDecimalEquals(BigDecimal d1, BigDecimal d2, double tolerance) {
        return isDecimalEquals(d1, d2, new BigDecimal(tolerance));
    }
    
    
    /**
     * Checking whether the difference of two decimals are within default tolerance
     * 
     * @param d1 decimal 1
     * @param d2 decimal 2
     * 
     * @return result of checking
     */
    public static boolean isDecimalEquals(BigDecimal d1, BigDecimal d2) {
        return isDecimalEquals(d1, d2, new BigDecimal(DEFAULT_TOLERANCE));
    }
    
    /**
     * Solve a quadratic equation (ax^2 + bx + c = 0)
     * 
     * @param a factor of x^2
     * @param b factor of x
     * @param c number factor
     * 
     * @return solution of provided equation
     */
    public static QuadraticSolution solveQuadraticEquation(double a, double b, double c) {
        QuadraticSolution sol;
        if (a == 0) {
            sol = new QuadraticSolution(-c/b);
        } else {
            double discriminant = Math.sqrt(b*b - 4*a*c);
            if (discriminant == 0) {
                // if discriminant is zero there will only be one single solution
                sol = new QuadraticSolution(-b / (2 * a));
            } else if (discriminant < 0) {
                sol = new QuadraticSolution(Double.NaN);
            } else {
                sol = new QuadraticSolution((discriminant - b) / (2 * a), (-b - discriminant) / (2 * a));
            }
        }
        return sol;
    }
    
    /**
     * Get cosine of angle A of a triangle with all known side <br>
     * Law of cosine: cos(A) = a * a + b * b - c * c / 2ab == a^2 + b^2 - c^2 / 2ab<br>
     * Angle side location: side a > angle A > side b > angle B > side c > angle C > side a
     * 
     * @param a side length a
     * @param b side length b
     * @param c side length c
     * 
     * @return cosine of angle A
     */
    public static double getCosineWithKnownSideLength(double a, double b, double c) {
        return ((a * a) + (b * b) - (c * c)) / (2 * a * b);
    }
    
    /**
     * Get angle A of a triangle with all known side
     * 
     * @param a side length a
     * @param b side length b
     * @param c side length c
     * 
     * @return angle A (in radian)
     */
    public static double getAngleWithKnownSideLength(double a, double b, double c) {
        return Math.acos(getCosineWithKnownSideLength(a, b, c));
    }
    
    /**
     * Get the angle to horizontal of a line (p1, p2)
     * 
     * @param p1 point 1 of line
     * @param p2 point 2 of line
     * 
     * @return angle to horizontal (in radian)
     */
    public static double getAngleToHorizontal(Point2D p1, Point2D p2) {
        return getAngleToHorizontal(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    
    /**
     * Get the angle to horizontal of a line (p1(x1, y1), p2(x2, y2)) 
     * 
     * @param x1 x coordinate of point 1 of line
     * @param y1 y coordinate of point 1 of line
     * @param x2 x coordinate of point 2 of line
     * @param y2 y coordinate of point 2 of line
     * 
     * @return angle to horizontal (in radian)
     */
    public static double getAngleToHorizontal(double x1, double y1, double x2, double y2) {
        double a = x2 - x1, b = y2 - y1, rad = Math.abs(Math.atan(b/a));
        if (a >= 0 && b >= 0) {
            return rad;
        } else if (a >= 0 && b < 0) {
            return Math.PI * 2 - rad;
        } else if (a < 0 && b < 0) {
            return Math.PI  + rad;
        } else {
            return Math.PI - rad;
        }
    }
    
    /**
     * Get destination point with angle to horizontal & length to reference point
     * 
     * @param x1 x coordinate of reference point
     * @param y1 y coordinate of reference point
     * @param angleRadian angle to horizontal (in radian)
     * @param length length from reference point
     * 
     * @return destination point
     */
    public static Point2D getDestinationPoint(double x1, double y1, double angleRadian, double length) {
        double a = Math.cos(angleRadian) * length;
        double b = Math.sin(angleRadian) * length;
        return new Point2D.Double(x1 + a, y1 + b);
    }
    
    /**
     * Get destination point with angle to horizontal & length to reference point
     * 
     * @param ref reference point
     * @param angle angle to horizontal (in radian)
     * @param length length from reference point
     * 
     * @return destination point
     */
    public static Point2D getDestinationPoint(Point2D ref, double angle, double length) {
        return getDestinationPoint(ref.getX(), ref.getY(), angle, length);
    }
    
    /**
     * Calculate side length of a right angled triangle, with knwon hypo and one side
     * 
     * @param hypo hypo length
     * @param oneSide length of one side
     * 
     * @return length of another side
     */
    public static double calculateRightTriangleSideLength(double hypo, double oneSide) {
        return Math.sqrt(hypo * hypo - oneSide * oneSide);
    }
    
    /**
     * Calculate side length of a right angled triangle, with known hypotenuse and one side (BigDecimal)
     * 
     * @param hypo hypotenuse length
     * @param oneSide length of one side
     * 
     * @return length of another side
     */
    public static BigDecimal calculateRightTriangleSideLength(BigDecimal hypo, BigDecimal oneSide) {
        return MathUtil.sqrt(hypo.pow(2).subtract(oneSide.pow(2)));
    }
    
    /**
     * Calculate hypotenuse length of a right angled triangle with known two side
     * 
     * @param firstSide length of first side
     * @param secondSide length of second side
     * 
     * @return length of hypotenuse
     */
    public static double calculateRightTriangleHypotenuse(double firstSide, double secondSide) {
        return Math.sqrt(firstSide * firstSide + secondSide * secondSide);
    }
    
    
    /**
     * Calculate hypotenuse length of a right angled triangle with known two side (BigDecimal)
     * 
     * @param firstSide length of first side
     * @param secondSide length of second side
     * 
     * @return length of hypotenuse
     */
    public static BigDecimal calculateRightTriangleHypotenuse(BigDecimal firstSide, BigDecimal secondSide) {
        return MathUtil.sqrt(firstSide.pow(2).add(secondSide.pow(2)));
    }
    
    /**
     * Calculate triangle area
     * 
     * @param x1 x coordinate of point 1
     * @param y1 y coordinate of point 1
     * @param x2 x coordinate of point 2
     * @param y2 y coordinate of point 2
     * @param x3 x coordinate of point 3
     * @param y3 y coordinate of point 3
     * 
     * @return area of triangle
     */
    public static double calculateTriangleArea(double x1, double y1, 
            double x2, double y2, double x3, double y3) {
        double a = getLength(x1, y1, x2, y2);
        double b = getLength(x1, y1, x3, y3);
        double c = getLength(x2, y2, x3, y3);
        
        return calculateTriangleArea(a, b, c);
    }
    
    /**
     * Calculate triangle area
     * 
     * @param p1 point 1 of triangle
     * @param p2 point 2 of triangle
     * @param p3 point 3 of triangle
     * 
     * @return area of triangle
     */
    public static double calculateTriangleArea(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        return calculateTriangleArea(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
                p3.getX(), p3.getY());
    }
    
    /**
     * Calculate triangle area
     * 
     * @param p1 point 1 of triangle
     * @param p2 point 2 of triangle
     * @param p3 point 3 of triangle
     * 
     * @return area of triangle
     */
    public static double calculateTriangleArea(Point2D.Float p1, Point2D.Float p2, Point2D.Float p3) {
        return calculateTriangleArea(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
                p3.getX(), p3.getY());
    }
    
    /**
     * Calculate triangle area
     * 
     * @param p1 point 1 of triangle
     * @param p2 point 2 of triangle
     * @param p3 point 3 of triangle
     * 
     * @return area of triangle
     */
    public static double calculateTriangleArea(Point2D p1, Point2D p2, Point2D p3) {
        return calculateTriangleArea(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
    }
    
    /**
     * Calculate triangle area with all known side
     * 
     * @param a length side 1
     * @param b length side 2
     * @param c length side 3
     * 
     * @return area of triangle
     */
    public static double calculateTriangleArea(double a, double b, double c) {
        double a2 = square(a);
        double b2 = square(b);
        double c2 = square(c);
        double a4 = square(a2);
        double b4 = square(b2);
        double c4 = square(c2);
        
        return Math.sqrt((a2 * b2 + b2 * c2 + c2 * a2) * 2 - (a4 + b4 + c4)) / 4;
    }
    
    /**
     * Check whether provided value is within range n1 & n2
     * 
     * @param value target value
     * @param n1 range of value 1
     * @param n2 range of value 2
     * 
     * @return value within range or not
     */
    public static boolean withinRange(int value, int n1, int n2) {
        int min = 0, max = 0;
        if (n1 > n2) {
            max = n1;
            min = n2;
        } else {
            max = n2;
            min = n1;
        }
        return value >= min && value <= max;
    }
    
    /**
     * Check whether provided value is within range n1 & n2
     * 
     * @param value target value
     * @param n1 range of value 1
     * @param n2 range of value 2
     * 
     * @return value within range or not
     */
    public static boolean withinRange(double value, double n1, double n2) {
        double min = 0, max = 0;
        if (n1 > n2) {
            max = n1;
            min = n2;
        } else {
            max = n2;
            min = n1;
        }
        return value >= min && value <= max;
    }
    
    /**
     * Calculate sum of a sequence (incremented by 1) starting from 1 to n
     * 
     * @param n ending number
     * 
     * @return sum of sequence
     */
    public static long sumOfSeq(int n) {
        if (n <= 0) {
            return 0;
        }
        long time = n / 2;
        if (n % 2 == 0) {
            return (n + 1) * time;
        } else {
            return (n + 1) * time + (time + 1);
        }
    }
    
    /**
     * Calculate sum of a sequence (incremented by 1) starting from a to b
     * 
     * @param a starting number of sequence
     * @param b ending number of sequence
     * 
     * @return sum of sequence
     */
    public static long sumOfSeq(int a, int b) {
        int min = 0, max = 0;
        if (a < b) {
            min = a;
            max = b;
        } else {
            min = b;
            max = a;
        }
        return sumOfSeq(max) - sumOfSeq(min-1);
    }
    
    /**
     * Parse a string into short value
     * 
     * @param str source string
     * @param defaultValue default value when parse failed
     * 
     * @return parsed short value
     */
    public static short parseShort(String str, short defaultValue) {
        return parseShort(str, 10, defaultValue);
    }
    
    /**
     * Parse a string into short value
     * 
     * @param str source string
     * @param radix parsing radix
     * @param defaultValue default value when parse failed
     * 
     * @return parsed short value
     */
    public static short parseShort(String str, int radix, short defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Short.parseShort(str, radix);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
    }

    /**
     * Parse a string into BigDecimal value
     *
     * @param str source string
     *
     * @return parsed BigDecimal value
     */
    public static BigDecimal parseBigDecimal(String str) {
        return parseBigDecimal(str, BigDecimal.ZERO);
    }

    /**
     * Parse a string into BigDecimal value
     *
     * @param str source string
     * @param defaultValue default value when parse failed
     *
     * @return parsed BigDecimal value
     */
    public static BigDecimal parseBigDecimal(String str, BigDecimal defaultValue) {
        if (!StringUtil.isEmptyString(str)) {
            try {
                BigDecimal dec = new BigDecimal(str);
                return dec;
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Parse a string into integer value
     *  
     * @param str source string
     * @param defaultValue default value when parse failed
     * 
     * @return parsed integer value
     */
    public static int parseInt(String str, int defaultValue) {
        return parseInt(str, 10, defaultValue);
    }
    
    /**
     * Parse a string into integer value
     * 
     * @param str source string
     * @param radix parsing radix
     * @param defaultValue default value when parse failed
     * 
     * @return parsed integer value
     */
    public static int parseInt(String str, int radix, int defaultValue) {
        if (str == null || str.equals("")) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(str, radix);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
    }
    
    /**
     * Parse a string into long value
     * 
     * @param str source string
     * @param defaultValue default value when parse failed
     * 
     * @return parsed long value
     */
    public static long parseLong(String str, long defaultValue) {
        return parseLong(str, 10, defaultValue);
    }
    
    /**
     * Parse a string into long value
     * 
     * @param str source string
     * @param radix parsing radix
     * @param defaultValue default value when parse failed
     * 
     * @return parsed long value
     */
    public static long parseLong(String str, int radix, long defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(str, radix);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
    }
    
    /**
     * Parse a string into float value
     * 
     * @param str source string
     * @param defaultValue default value when parse failed
     * 
     * @return parsed float value
     */
    public static float parseFloat(String str, float defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
    }
    
    /**
     * Parse a string into double value
     * 
     * @param str source string
     * @param defaultValue default value when parse failed
     * 
     * @return parsed double value
     */
    public static double parseDouble(String str, double defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
    }
    
    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     * 
     * @param value target value
     * @param min minimum value
     * 
     * @return result of limit
     */
    public static int minValueLimit(int value, int min) {
        return (value < min)? min : value;
    }
    
    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     * 
     * @param value target value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static int maxValueLimit(int value, int max) {
        return (value > max)? max : value;
    }
    
    /**
     * Limit a value to a minimum & maximum value,<br>
     * if value is smaller than minimum / greater than maximum, 0 is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static int valueLimit(int value, int min, int max) {
        return valueLimit(value, min, max, 0);
    }
    
    /**
     * Limit a value to minimum & maximum value with default,<br>
     * if value is smaller than minimum / greater than maximum, default value is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value
     * 
     * @return result of limit
     */
    public static int valueLimit(int value, int min, int max, int defaultValue) {
        if (value >= min && value <= max) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     * 
     * @param value target value
     * @param min minimum value
     * 
     * @return result of limit
     */
    public static short minValueLimit(short value, short min) {
        return (value < min)? min : value;
    }
    
    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     * 
     * @param value target value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static short maxValueLimit(short value, short max) {
        return (value > max)? max : value;
    }
    
    /**
     * Limit a value to a minimum & maximum value,<br>
     * if value is smaller than minimum / greater than maximum, 0 is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static short valueLimit(short value, short min, short max) {
        return valueLimit(value, min, max, (short)0);
    }
    
    /**
     * Limit a value to minimum & maximum value with default,<br>
     * if value is smaller than minimum / greater than maximum, default value is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value
     * 
     * @return result of limit
     */
    public static short valueLimit(short value, short min, short max, short defaultValue) {
        if (value >= min && value <= max) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     * 
     * @param value target value
     * @param min minimum value
     * 
     * @return result of limit
     */
    public static float minValueLimit(float value, float min) {
        return (value < min)? min : value;
    }
    
    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     * 
     * @param value target value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static float maxValueLimit(float value, float max) {
        return (value > max)? max : value;
    }
    
    /**
     * Limit a value to a minimum & maximum value,<br>
     * if value is smaller than minimum / greater than maximum, 0 is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static float valueLimit(float value, float min, float max) {
        return valueLimit(value, min, max, 0);
    }
    
    /**
     * Limit a value to minimum & maximum value with default,<br>
     * if value is smaller than minimum / greater than maximum, default value is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value
     * 
     * @return result of limit
     */
    public static float valueLimit(float value, float min, float max, float defaultValue) {
        if (value >= min && value <= max) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     * 
     * @param value target value
     * @param min minimum value
     * 
     * @return result of limit
     */
    public static double minValueLimit(double value, double min) {
        return (value < min)? min : value;
    }
    
    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     * 
     * @param value target value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static double maxValueLimit(double value, double max) {
        return (value > max)? max : value;
    }
    
    /**
     * Limit a value to a minimum & maximum value,<br>
     * if value is smaller than minimum / greater than maximum, 0 is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static double valueLimit(double value, double min, double max) {
        return valueLimit(value, min, max, 0);
    }
    
    /**
     * Limit a value to minimum & maximum value with default,<br>
     * if value is smaller than minimum / greater than maximum, default value is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value
     * 
     * @return result of limit
     */
    public static double valueLimit(double value, double min, double max, double defaultValue) {
        if (value >= min && value <= max) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     * 
     * @param value target value
     * @param min minimum value
     * 
     * @return result of limit
     */
    public static long minValueLimit(long value, long min) {
        return (value < min)? min : value;
    }
    
    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     * 
     * @param value target value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static long maxValueLimit(long value, long max) {
        return (value > max)? max : value;
    }
    
    /**
     * Limit a value to a minimum & maximum value,<br>
     * if value is smaller than minimum / greater than maximum, 0 is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * 
     * @return result of limit
     */
    public static long valueLimit(long value, long min, long max) {
        return valueLimit(value, min, max, 0);
    }
    
    /**
     * Limit a value to minimum & maximum value with default,<br>
     * if value is smaller than minimum / greater than maximum, default value is returned<br>
     * otherwise self value is returned
     * 
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value
     * 
     * @return result of limit
     */
    public static long valueLimit(long value, long min, long max, long defaultValue) {
        if (value >= min && value <= max) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Find the maximum value within integer values
     * 
     * @param values source integer values
     * 
     * @return maximum integer value
     */
    public static int integerMax(int ... values) {
        if (values != null && values.length > 0) {
            int max = values[0];
            for (int i = 1; i < values.length; i++) {
                if (max < values[i]) {
                    max = values[i];
                }
            }
            return max;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Find the maximum value within integer value list
     *
     * @param values source integer value list
     *
     * @return maximum integer value
     */
    public static int integerMax(List<Integer> values) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Values is null or no value contained!");
        } else {
            int max = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                int v = values.get(i);
                if (max < v) {
                    max = v;
                }
            }
            return max;
        }
    }
    
    /**
     * Find the maximum value within number values
     * 
     * @param values source number values
     * 
     * @return maximum number values
     */
    public static double doubleMax(double ... values) {
        double max = Double.MIN_VALUE;
        if (values != null) {
            for (double v : values) {
                if (Double.isNaN(v)) {
                    return v;
                }
                if (max < v) {
                    max = v;
                }
            }
        }
        return max;
    }

    /**
     * Find the maximum value within number value list
     *
     * @param values source number value list
     *
     * @return maximum number value
     */
    public static double doubleMax(List<Double> values) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Values is null or no value contained!");
        } else {
            double max = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                double v = values.get(i);
                if (v > max) {
                    max = v;
                }
            }
            return max;
        }
    }
    
    /**
     * Find the maximum value within BigDecimals
     * 
     * @param values source BigDecimals
     * 
     * @return maximum BigDecimal
     */
    public static BigDecimal bigDecimalMax(BigDecimal ... values) {
        if (values != null && values.length > 0) {
            BigDecimal max = values[0];
            for (int i = 1; i < values.length; i++) {
                if (values[i].compareTo(max) > 0) {
                    max = values[i];
                }
            }
            return max;
        } else {
            return null;
        }
    }

    /**
     * Find the maximum value within BigDecimal list
     *
     * @param values source BigDecimal list
     *
     * @return maximum BigDecimal value
     */
    public static BigDecimal bigDecimalMax(List<BigDecimal> values) {
        if (values == null || values.size() == 0) {
            return null;
        } else {
            BigDecimal max = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                BigDecimal v = values.get(i);
                if (v.compareTo(max) > 0) {
                    max = v;
                }
            }
            return max;
        }
    }
    
    /**
     * Find the minimum value within integer values
     * 
     * @param values source integer values
     * 
     * @return minimum integer value
     */
    public static int integerMin(int ... values) {
        int min = Integer.MAX_VALUE;
        if (values != null) {
            for (int v : values) {
                if (min > v) {
                    min = v;
                }
            }
        }
        return min;
    }

    /**
     * Find the minimum value within integer value list
     *
     * @param values source integer value list
     *
     * @return minimum integer value
     */
    public static int integerMin(List<Integer> values) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Values is null or no value contained!");
        } else {
            int min = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                int v = values.get(i);
                if (v < min) {
                    min = v;
                }
            }
            return min;
        }
    }
    
    /**
     * Find the minimum value within number value list
     * 
     * @param values source number value list
     * 
     * @return minimum number values
     */
    public static double doubleMin(double ... values) {
        double min = Integer.MAX_VALUE;
        if (values != null) {
            for (double v : values) {
                if (Double.isNaN(v)) {
                    return v;
                }
                if (min > v) {
                    min = v;
                }
            }
        }
        return min;
    }

    /**
     * Find the minimun value within number values
     *
     * @param values source number values
     *
     * @return minimun number values
     */
    public static double doubleMin(List<Double> values) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Values is null or no value contained.");
        } else {
            double min = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                double v = values.get(i);
                if (v < min) {
                    min = v;
                }
            }
            return min;
        }
    }
    
    /**
     * Find the minimum value within BigDecimals
     * 
     * @param values source BigDecimals
     * 
     * @return minimum BigDecimal
     */
    public static BigDecimal bigDecimalMin(BigDecimal ... values) {
        if (values != null && values.length > 0) {
            BigDecimal min = values[0];
            for (int i = 1; i < values.length; i++) {
                if (values[i].compareTo(min) < 0) {
                    min = values[i];
                }
            }
            return min;
        } else {
            return null;
        }
    }

    /**
     * Find the minimun value within BigDecimals
     *
     * @param values source BigDecimals
     *
     * @return minimum BigDecimal
     */
    public static BigDecimal bigDecimalMin(List<BigDecimal> values) {
        if (values != null && values.size() > 0) {
            BigDecimal min = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                BigDecimal v = values.get(i);
                if (v.compareTo(min) < 0) {
                    min = v;
                }
            }
            return min;
        } else {
            return null;
        }
    }
    
    /**
     * Calculate percentage change
     * 
     * @param originalValue original value
     * @param diffValue difference value
     * 
     * @return percentage change 0 ~ 1
     */
    public static double calPercentageChange(double originalValue, double diffValue) {
        if (originalValue == 0) {
            return (diffValue > 0)? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        } else {
            return 1 / (1 + (diffValue/originalValue));
        }
    }
    
    /**
     * Calculate sum of a number array from start index to end
     * 
     * @param values number values
     * @param startIndex index to start calculation
     * 
     * @return sum of number array
     */
    public static double calculateSum(double[] values, int startIndex) {
        return calculateSum(values, startIndex, values.length - startIndex);
    }
    
    /**
     * Calculate sum of a number array from starting index to a length of elements
     *
     * @param values number values
     * @param startIndex index to start calculation
     * @param length length of elements
     *
     * @return sum of number array
     */
    public static double calculateSum(double[] values, int startIndex, int length) {
        double sum = 0;
        for (int i = startIndex; i < startIndex + length; i++) {
            sum += values[i];
        }
        return sum;
    }
    
    /**
     * Calculate sum from number values
     * 
     * @param values number values
     * 
     * @return sum of number values
     */
    public static double calculateSum(double ... values) {
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum;
    }
    
    /**
     * Calculate sum from a collection of number values
     * 
     * @param values collection of number values
     * 
     * @return sum of number values
     */
    public static double calculateSum(Collection<? extends Number> values) {
        double sum = 0;
        
        for (Number v : values) {
            if (v != null) {
                sum += v.doubleValue();
            }
        }

        return sum;
    }

    /**
     * Calculate sum from a collection of bigdecimal values
     *
     * @param values collection of bigdecimal values
     *
     * @return sum of bigdecimal values
     */
    public static BigDecimal calculateBigDecimalSum(Collection<BigDecimal> values) {
        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal v : values) {
            if (v != null) {
                sum = sum.add(v);
            }
        }

        return sum;
    }

    /**
     * Calculate sum from an integer array with provided start index
     *
     * @param values integer array
     * @param startIndex calculation starting index
     *
     * @return sum of integer array
     */
    public static int calculateSum(int[] values, int startIndex) {
        int sum = 0;
        for (int i = startIndex; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

    /**
     * Calculate sum from an integer array with provided start index & length
     *
     * @param values integer array
     * @param startIndex calculation starting index
     * @param length calculation length
     *
     * @return sum of integer array
     */
    public static int calculateSum(int[] values, int startIndex, int length) {
        int sum = 0;
        for (int i = startIndex; i < values.length && i < startIndex+length; i++) {
            sum += values[i];
        }
        return sum;
    }

    /**
     * Calculate sum from an integer array
     *
     * @param values integer array
     *
     * @return sum of integer array
     */
    public static int calculateSum(int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }
    
    /**
     * Calculate average of a number array from starting index to end
     * 
     * @param values number values
     * @param startIndex index to start calculation
     * 
     * @return average of number array
     */
    public static double calculateAverage(double[] values, int startIndex) {
        return calculateAverage(values, startIndex, values.length - startIndex);
    }
    
    /**
     * Calculate average of a number array from starting index to a length of elements
     * 
     * @param values number values
     * @param startIndex index to start calculation
     * @param length length of elements
     * 
     * @return average of number array
     */
    public static double calculateAverage(double[] values, int startIndex, int length) {
        double sum = 0;
        int count = 0;
        for (int i = startIndex; i < startIndex + length; i++) {
            sum += values[i];
            count++;
        }
        return sum / count;
    }
    
    /**
     * Calculate average from number values
     * 
     * @param values number values
     * 
     * @return average of number values
     */
    public static double calculateAverage(double ... values) {
        if (values == null) {
            return 0;
        }
        double total = 0;
        for (double value : values) {
            total += value;
        }
        return total / values.length;
    }
    
    /**
     * Calculate average from a collection of number values
     * 
     * @param values collection of number values
     * 
     * @return average of a collection of number values
     */
    public static double calculateAverage(Collection<? extends Number> values) {
        if (values == null) {
            return 0;
        }
        double total = 0;
        for (Number value : values) {
            total += (value == null)? 0 : value.doubleValue();
        }
        return total / values.size();
    }

    /**
     * Calculate average from a collection of bigdecimal values
     *
     * @param values collection of bigdecimal values
     *
     * @return average of a collection of bigdecimal values
     */
    public static BigDecimal calculateBigDecimalAverage(Collection<BigDecimal> values) {
        if (values == null) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal total = calculateBigDecimalSum(values);
            return total.divide(new BigDecimal(values.size()), 4, RoundingMode.HALF_UP);
        }
    }
    
    /**
     * Calculate the power 
     * 
     * @param v base
     * @param r exponent
     * 
     * @return result of power
     */
    public static BigDecimal pow(int v, int r) {
        return pow(new BigDecimal(v), new BigDecimal(r));
    }
    
    /**
     * Calculate the modulo of three integers
     * 
     * @param a integer 1
     * @param b integer 2
     * @param c integer 3
     * 
     * @return modulo result
     */
    public static int modulo(int a, int b, int c){
        long x=1,y=a;
        while(b > 0){
            if((b&1) == 1){
                x=(x*y)%c;
            }
            y = (y*y)%c;
            b >>= 1;
        }
        return (int)(x%c);
    }
    
    /**
     * Calculate the power
     * 
     * @param v base
     * @param r exponent
     * 
     * @return result of power
     */
    public static BigDecimal pow(BigDecimal v, BigDecimal r) {
        int signum = r.signum();
        
        if (signum == 0) {
            return BigDecimal.ONE;
        } else if (signum > 0) {
            BigDecimal[] parts = r.divideAndRemainder(BigDecimal.ONE);
            
            if (parts[1].compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal result = v.pow(parts[0].intValue());
                
                BigDecimal decimalPow = parts[1], fraction = new BigDecimal("0.5"), curr = v;
                
                while (decimalPow.compareTo(DEFAULT_BIG_TOLERANCE) > 0) {
                    curr = sqrt(curr);
                    if (decimalPow.compareTo(fraction) >= 0) {
                        result = result.multiply(curr);
                        decimalPow = decimalPow.subtract(fraction);
                    }
                    
                    fraction = fraction.divide(TWO);
                }
                
                return result;
            } else {
                return v.pow(parts[0].intValue());
            }
        } else {
            return new BigDecimal(1).divide(
                    pow(v, r.abs()), DEFAULT_MATH_CONTEXT
            );
        }
    }
    
    /**
     * Calculate the permutation of (v!)
     * 
     * @param v value
     * 
     * @return result of permutation
     */
    public static BigDecimal permutation(BigDecimal v) {
        if (
                v.compareTo(BigDecimal.ZERO) == 0 ||
                v.compareTo(BigDecimal.ONE) == 0
        ) {
            return BigDecimal.ONE;
        } else {
            BigDecimal result = v, count = v.subtract(BigDecimal.ONE);
            while (count.compareTo(BigDecimal.ONE) > 0) {
                result = result.multiply(count);
                count = count.subtract(BigDecimal.ONE);
            }
            return result;
        }
    }
    
    /**
     * Calculate the permutation of (v!)
     * 
     * @param v value
     * 
     * @return result of permutation
     */
    public static BigDecimal permutation(int v) {
        if (v == 0 || v == 1) {
            return BigDecimal.ONE;
        } else{
            BigDecimal result = new BigDecimal(v);
            int count = v - 1;
            while (count > 1) {
                result = result.multiply(new BigDecimal(count));
                count--;
            }
            return result;
        }
    }
    
    /**
     * Calculate the permutation of (v!/r!)
     * 
     * @param v value
     * @param r end value
     * 
     * @return result of permutation
     */
    public static BigDecimal permutation(BigDecimal v, BigDecimal r) {
        if (r.compareTo(BigDecimal.ONE) == 0) {
            return permutation(v);
        } else {
            BigDecimal result = v, count = v.subtract(BigDecimal.ONE), endCount = v.subtract(r);
            
            while (count.compareTo(endCount) >= 0 && count.compareTo(BigDecimal.ONE) > 0) {
                result = result.multiply(count);
                count = count.subtract(BigDecimal.ONE);
            }
            
            return result;
        }
    }
    
    /**
     * Calculate the permutation of (v!/(v-r)!)
     * 
     * @param v value
     * @param r length
     * 
     * @return result of permutation
     */
    public static BigDecimal permutation(int v, int r) {
        if (r == v) {
            return permutation(v);
        } else {
            BigDecimal result = new BigDecimal(v);
            int count = v - 1, endCount = v - r + 1;
            while (count >= endCount && count > 1) {
                result = result.multiply(new BigDecimal(count));
                count--;
            }
            return result;
        }
    }
    
    /**
     * Calculate the combination of (v!/r!(v-r)!)
     * 
     * @param v value
     * @param r length
     * 
     * @return result of combination
     */
    public static BigDecimal combination(BigDecimal v, BigDecimal r) {
        return permutation(v, r).divide(permutation(r));
    }
    
    /**
     * Calculate the combination of (v!/r!(v-r)!)
     * 
     * @param v value
     * @param r length
     * 
     * @return result of combination
     */
    public static BigDecimal combination(int v, int r) {
        return permutation(v, r).divide(permutation(r));
    }
    
    /**
     * Permutate a list of values with provided length
     * 
     * @param <E> Generic type
     * 
     * @param list list of values
     * @param len target length
     * 
     * @return results of permutation
     */
    public static <E> List<List<E>> permutation(List<E> list, int len) {
        if (list.size() < len || len <= 0) {
            return null;
        } else {
            List<List<E>> r = new ArrayList<List<E>>(permutation(list.size(), len).intValue());
            
            permutate(r, list, new ArrayList<Integer>(), DataManipulator.createSequence(0, list.size()-1), len);
            
            return r;
        }
    }
    
    /**
     * Recursive method to permutate list of values
     * 
     * @param <E> Generic type
     * @param lists permutation result list
     * @param list list of values
     * @param setIndexes setted indexes
     * @param availableIndexes available indexes
     * @param len target length
     */
    private static <E> void permutate(
            List<List<E>> lists, List<E> list, List<Integer> setIndexes,
            List<Integer> availableIndexes, int len
    ) {
        if (setIndexes.size() == len) {
            lists.add(DataManipulator.createList(list, setIndexes));
        } else {
            for (int i = 0; i < availableIndexes.size(); i++) {
                List<Integer> aIndexes = new ArrayList<Integer>(availableIndexes),
                    sIndexes = new ArrayList<Integer>(setIndexes);
                
                sIndexes.add(aIndexes.remove(i));
                
                permutate(lists, list, sIndexes, aIndexes, len);
            }
        }
    }
    
    /**
     * Recursive method combinate list of values
     * 
     * @param <E> Generic type
     * @param lists combination result list
     * @param list list of values
     * @param setIndexes setted indexes
     * @param len target length
     */
    private static <E> void combinate(
            List<List<E>> lists, List<E> list, List<Integer> setIndexes, int len
    ) {
        if (setIndexes.size() == len) {
            lists.add(DataManipulator.createList(list, setIndexes));
        } else {
            int remain = len - setIndexes.size();
            int i = (setIndexes.size() == 0)? 0 : (setIndexes.get(setIndexes.size() - 1).intValue() + 1);
            while (i < list.size() - remain + 1) {
                List<Integer> sIndices = new ArrayList<>(setIndexes);
                
                sIndices.add(i);
                
                combinate(lists, list, sIndices, len);
                i++;
            }
        }
    }
    
    /**
     * Combinate a list of values with provided length
     * 
     * @param <E> Generic type
     * @param list list of values
     * @param len target length
     * 
     * @return result of combination
     */
    public static <E> List<List<E>> combination(List<E> list, int len) {
        if (list.size() < len || len <= 0) {
            return null;
        } else {
            List<List<E>> r = new ArrayList<>(permutation(list.size(), len).intValue());
            
            combinate(r, list, new ArrayList<>(), len);
            
            return r;
        }
    }
    
    /**
     * Calculate the power 
     * 
     * @param base base
     * @param exponent exponent
     * 
     * @return result of power
     */
    public static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger p = BigInteger.ONE;
        while (exponent.compareTo(new BigInteger("0")) > 0) {
            if (exponent.remainder(new BigInteger("2")).compareTo(BigInteger.ONE) == 0) {
                p = p.multiply(base);
            }
            base = base.multiply(base);
            exponent = exponent.divide(new BigInteger("2"));
        }
        return p;
    }
    
    /**
     * Check provided BigInteger value is a power of other integer or not
     * 
     * @param n source integer
     * 
     * @return is power of other integer or not
     */
    public static boolean isPow(BigInteger n) {
        BigInteger upperBound = n;
        BigInteger lowerBound = BigInteger.ONE;
        BigInteger temp;
        for (
                BigInteger i = BigInteger.ONE;
                (i.compareTo(new BigInteger(Integer.toString((n.bitLength())))) < 0);
                i = i.add(BigInteger.ONE)
        ) {
            while ((upperBound.subtract(lowerBound)).compareTo(BigInteger.ONE) > 0) {
                temp = ((upperBound.add(lowerBound)).divide(new BigInteger("2")));
                if ((pow(temp, i.add(BigInteger.ONE))).compareTo(n) == 0) {
                    return true;
                }
                if ((pow(temp, i.add(BigInteger.ONE))).compareTo(n) > 0) {
                    upperBound = temp;
                }
                if ((pow(temp, i.add(BigInteger.ONE))).compareTo(n) < 0) {
                    lowerBound = temp;
                }
            }
        }
        return false;
    }
    
    /**
     * Compare two numbers
     * 
     * @param n1 number 1
     * @param n2 number 2
     * 
     * @return result of comparison<br>
     * 0 > equals<br>
     * negative > n1 is smaller than n2<br>
     * positive > n1 is greater than n2 
     */
    public static int compare(Number n1, Number n2) {
        return new BigDecimal(n1.toString()).compareTo(new BigDecimal(n2.toString()));
    }
    
    /**
     * Solution of quadratic equation, result are in positive & negative form
     *
     */
    public static class QuadraticSolution {
        /**
         * Positive solution
         */
        private double positiveValue;
        
        /**
         * Negative solution
         */
        private double negativeValue;
        
        /**
         * Construct a single solution
         * 
         * @param value solution value
         */
        public QuadraticSolution(double value) {
            this(value, value);
        }
        
        /**
         * Construct two possible solution
         * 
         * @param pValue positive solution
         * @param nValue negative solution
         */
        public QuadraticSolution(double pValue, double nValue) {
            positiveValue = pValue;
            negativeValue = nValue;
        }
        
        /**
         * Get the positive solution
         * 
         * @return positive solution
         */
        public double getPositiveValue() {
            return positiveValue;
        }
        
        /**
         * Get the negative solution
         * 
         * @return negative solution
         */
        public double getNegativeValue() {
            return negativeValue;
        }
        
        /**
         * Get a representation string of this Quadratic solution
         */
        @Override
        public String toString() {
            if (positiveValue == negativeValue) {
                return "value: " + positiveValue;
            } else {
                return "positiveValue: " + positiveValue + ", negativeValue: " + negativeValue;
            }
        }
    }
    
    /**
     * Check if two integers' sign equals or not (positive / negative), 0 is considered positive
     * 
     * @param n1 integer 1
     * @param n2 integer 2
     * 
     * @return checking result
     */
    public static boolean isSignEquals(int n1, int n2) {
        return getSign(n1) == getSign(n2);
    }
    
    /**
     * Get the sign of an integer
     * 
     * @param n target integer
     * 
     * @return sign of integer -1 for negative, 1 for positive and 0
     */
    public static int getSign(int n) {
        return (n < 0)? -1 : 1;
    }
    
    /**
     * Check if two numbers' sign equals or not (positive / negative), 0 is considered positive
     * 
     * @param n1 integer 1
     * @param n2 integer 2
     * 
     * @return checking result
     */
    public static boolean isSignEquals(double n1, double n2) {
        return getSign(n1) == getSign(n2);
    }
    
    /**
     * Get the sign of a number
     * 
     * @param n target number
     * 
     * @return sign of number -1 for negative, 1 for positive and 0
     */
    public static int getSign(double n) {
        return (n < 0)? -1 : 1;
    }
    
    /**
     * Factorize an integer
     * 
     * @param n integer to be factorized
     * 
     * @return result of factorization
     */
    public static List<Integer> factorize(int n) {
        List<Integer> factors = new ArrayList<Integer>();
        
        if (n == 1) {
            factors.add(1);
            return factors;
        } else if (n == 0) {
            return factors;
        }
        
        List<Integer> primeList = PrimeUtil.getPrimeListWithin(Math.abs(n));
        
        int num = n;
        for (Integer aPrimeList : primeList) {
            int prime = aPrimeList;
            while (num % prime == 0) {
                factors.add(prime);
                num /= prime;
            }
        }
        
        return factors;
    }

    /**
     * Get the length of an integer (decimal)
     *
     * @param value target value
     *
     * @return length of target integer value, -1 for negative value
     */
    public static int lengthOfInteger(int value) {
        if (value < 0) {
            return -1;
        } else if (value == 0) {
            return 1;
        } else {
            return (int)Math.floor(Math.log10(value))+1;
        }
    }

    /**
     * Get the length of an integer with base
     *
     * @param value target value
     * @param base target value base
     *
     * @return length of target integer value, -1 for negative value
     */
    public static int lengthOfInteger(int value, int base) {
        if (value < 0) {
            return -1;
        } else if (value == 0) {
            return 1;
        } else {
            return (int)floor(log(value, base)) + 1;
        }
    }

    // This method only find the smallest possible factor, if small prime number exhausted just stop.
    public static int findSmallestFactor(long number) {
        if (number == 1) {
            return -1;
        } else {
            for (int FIRST_PRIME : FIRST_PRIMES) {
                if (number % FIRST_PRIME == 0) {
                    return FIRST_PRIME;
                }
            }

            // small prime exhausted, return -1
            return -1;
        }
    }
}
