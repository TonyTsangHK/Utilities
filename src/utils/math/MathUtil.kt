package utils.math

import utils.data.DataManipulator
import utils.random.RandomUtil
import utils.random.WeightedRandomValueHolder
import utils.string.StringUtil
import java.awt.Point
import java.awt.geom.Point2D
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import java.util.ArrayList
import java.util.Random

object MathUtil {
    /**
      * Cache of BigDecimals
      */
    @JvmField val TWO = BigDecimal("2")
    @JvmField val DOZEN = BigDecimal("12")
    @JvmField val TWELVE = DOZEN
    @JvmField val SIXTEEN = BigDecimal("16")
    @JvmField val HUNDRED = BigDecimal("100")
    @JvmField val THOUSAND = BigDecimal("1000")

    @JvmField val FIRST_PRIMES = intArrayOf(
           2,    3,    5,    7,   11,   13,   17,   19,   23,   29,   31,   37,   41,   43,   47,   53,
          59,   61,   67,   71,   73,   79,   83,   89,   97,  101,  103,  107,  109,  113,  127,  131,
         137,  139,  149,  151,  157,  163,  167,  173,  179,  181,  191,  193,  197,  199,  211,  223,
         227,  229,  233,  239,  241,  251,  257,  263,  269,  271,  277,  281,  283,  293,  307,  311,
         313,  317,  331,  337,  347,  349,  353,  359,  367,  373,  379,  383,  389,  397,  401,  409,
         419,  421,  431,  433,  439,  443,  449,  457,  461,  463,  467,  479,  487,  491,  499,  503,
         509,  521,  523,  541,  547,  557,  563,  569,  571,  577,  587,  593,  599,  601,  607,  613,
         617,  619,  631,  641,  643,  647,  653,  659,  661,  673,  677,  683,  691,  701,  709,  719,
         727,  733,  739,  743,  751,  757,  761,  769,  773,  787,  797,  809,  811,  821,  823,  827,
         829,  839,  853,  857,  859,  863,  877,  881,  883,  887,  907,  911,  919,  929,  937,  941,
         947,  953,  967,  971,  977,  983,  991,  997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049,
        1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163,
        1171, 1181, 1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283,
        1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423,
        1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511,
        1523, 1531, 1543, 1549, 1553, 1559, 1567, 1571, 1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619,
        1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723, 1733, 1741, 1747,
        1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877,
        1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997, 1999, 2003,
        2011, 2017, 2027, 2029, 2039, 2053
    )

    /**
     * Default iterations for square root calculation
     */
    @JvmField val DEFAULT_MAX_ITERATIONS = 50

    /**
     * Default big decimal scale
     */
    @JvmField val DEFAULT_SCALE = 20

    /**
      * Default big decimal tolerance for square root calculation
      */
    @JvmField val DEFAULT_BIG_TOLERANCE = BigDecimal.ONE.scaleByPowerOfTen(-DEFAULT_SCALE)

     /**
      * Default precision for double division
      */
    @JvmField val DEFAULT_PRECISION = 6

    /**
      * Default precision for big decimal division
      */
    @JvmField val DEFAULT_BIG_DECIMAL_PRECISION = 16

    /**
     * Default tolerance for floating point number comparison, different smaller than tolerance is considered equals.
     */
    @JvmField val DEFAULT_TOLERANCE = 0.000001

    /**
     * Default math context for big decimal division
     */
    @JvmField val DEFAULT_MATH_CONTEXT = MathContext(DEFAULT_BIG_DECIMAL_PRECISION, RoundingMode.HALF_UP)

    /**
     * @deprecated use utils.random.RandomUtil.setUseSecureRandom(Boolean) instead
     * 
     * Set usage of secure random
     * - It is fine to use Random, for its non-blocking nature
     * - Use secure random, if you really want TRULY RANDOM generation and it is provided at OS level, and it is also a computational random after all.
     
     * @param useSecureRandom secure random flag
     */
    @JvmStatic
    fun setUseSecureRandom(useSecureRandom:Boolean) {
        RandomUtil.setUseSecureRandom(useSecureRandom)
    }

    /**
     * @deprecated use utils.random.RandomUtil.createRandomGenerator(seed) instead
     * 
     * Create a random generator with given seed
     
     * @param seed given seed
     *
     * @return Random generate with given random seed.
     */
    @JvmStatic
    fun createRandomGenerator(seed:Long):Random {
        return RandomUtil.createRandomGenerator(seed)
    }

    /**
     * Add two double precision numbers
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return sum, result of summation
     */
    @JvmStatic
    fun add(a:Double?, b:Double?):Double {
        return add(a!!.toDouble(), b!!.toDouble())
    }

    /**
     * Add two double precision numbers with precision
     *
     * @param a operand 1
     * @param b operand 2
     * @param precision precision of result
     *
     * @return sum, result of summation, the result will be trimmed(rounded) by precision
     */
    @JvmStatic
    fun add(a:Double?, b:Double?, precision:Int):Double {
        return add(a!!.toDouble(), b!!.toDouble(), precision)
    }

    /**
     * Add two double precision numbers
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return sum, result of summation
     */
    @JvmStatic
    fun add(a:Double, b:Double):Double {
        return operate(a, b, Operation.ADD, DEFAULT_PRECISION)
    }

    /**
     * Add two double precision numbers with precision
     *
     * @param a operand 1
     * @param b operand 2
     * @param precision precision of result
     *
     * @return sum, result of summation, the result will be trimmed(rounded) by precision
     */
    @JvmStatic
    fun add(a:Double, b:Double, precision:Int):Double {
        return operate(a, b, Operation.ADD, precision)
    }

    /**
     * Add two big decimals, calling BigDecimal.add
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return sum, result of summation (BigDecimal.add)
     */
    @JvmStatic
    fun add(a:BigDecimal, b:BigDecimal):BigDecimal {
        return a.add(b)
    }

    /**
     * Subtract two double precision numbers
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return difference, result of subtraction
     */
    @JvmStatic
    fun subtract(a:Double?, b:Double?):Double {
        return subtract(a!!.toDouble(), b!!.toDouble())
    }

    /**
     * Subtract two double precision numbers with precision
     *
     * @param a operand 1 (minuend)
     * @param b operand 2 (subtrahend)
     *
     * @param precision precision of result
     *
     * @return difference, result of subtraction, the result will be trimmed(rounded) by precision
     */
    @JvmStatic
    fun subtract(a:Double?, b:Double?, precision:Int):Double {
        return subtract(a!!.toDouble(), b!!.toDouble(), precision)
    }

    /**
     * Subtract two double precision numbers
     *
     * @param a operand 1 (minuend)
     * @param b operand 2 (subtrahend)
     *
     * @return difference, result of subtraction
     */
    @JvmStatic
    fun subtract(a:Double, b:Double):Double {
        return operate(a, b, Operation.SUBTRACT, DEFAULT_PRECISION)
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
    @JvmStatic
    fun subtract(a:Double, b:Double, precision:Int):Double {
        return operate(a, b, Operation.SUBTRACT, precision)
    }

    /**
     * Subtract two big decimals, calling BigDecimal.subtract
     *
     * @param a operand 1 (minuend)
     * @param b operand 2 (subtrahend)
     *
     * @return difference, result of subtraction (BigDecimal.subtract)
     */
    @JvmStatic
    fun subtract(a:BigDecimal, b:BigDecimal):BigDecimal {
        return a - b
    }

    /**
     * Multiply two double precision numbers
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return product, result of multiplication
     */
    @JvmStatic
    fun multiply(a:Double?, b:Double?):Double {
        return multiply(a!!.toDouble(), b!!.toDouble())
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
    @JvmStatic
    fun multiply(a:Double?, b:Double?, precision:Int):Double {
        return multiply(a!!.toDouble(), b!!.toDouble(), precision)
    }

    /**
     * Multiply two double precision numbers with default precision (6)
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return product, result of multiplication
     */
    @JvmStatic
    fun multiply(a:Double, b:Double):Double {
        return operate(a, b, Operation.MULTIPLY, DEFAULT_PRECISION)
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
    @JvmStatic
    fun multiply(a:Double, b:Double, precision:Int):Double {
        return operate(a, b, Operation.MULTIPLY, precision)
    }

    /**
     * Multiply two big decimals
     *
     * @param a operand 1
     * @param b operand 2
     *
     * @return product, result of multiplication
     */
    @JvmStatic
    fun multiply(a:BigDecimal, b:BigDecimal):BigDecimal {
        return a * b
    }

    /**
     * Divide two double precision numbers
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     *
     * @return quotient, result of division
     */
    @JvmStatic
    fun divide(a:Double?, b:Double?):Double {
        return divide(a!!.toDouble(), b!!.toDouble())
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
    @JvmStatic
    fun divide(a:Double?, b:Double?, precision:Int):Double {
        return divide(a!!.toDouble(), b!!.toDouble(), precision)
    }

    /**
     * Divide two double precision numbers
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     *
     * @return quotient, result of division
     */
    @JvmStatic
    fun divide(a:Double, b:Double):Double {
        return operate(a, b, Operation.DIVIDE, DEFAULT_PRECISION)
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
    @JvmStatic
    fun divide(a:Double, b:Double, precision:Int):Double {
        return operate(a, b, Operation.DIVIDE, precision)
    }

    /**
     * Divide two big decimals
     *
     * @param a operand 1, dividend
     * @param b operand 2, divisor
     *
     * @return quotient, result of division
     */
    @JvmStatic
    fun divide(a:BigDecimal, b:BigDecimal):BigDecimal {
        return a.divide(b, DEFAULT_BIG_DECIMAL_PRECISION, RoundingMode.HALF_UP)
    }

    /**
     * Add two number and return the sum in BigDecimal form
     *
     * @param n1 operand 1
     * @param n2 operand 2
     *
     * @return sum, result of addition
     */
    @JvmStatic
    fun add(n1:Number?, n2:Number?):BigDecimal {
        val operand1 = if (n1 != null) BigDecimal(n1.toString()) else BigDecimal.ZERO
        val operand2 = if (n2 != null) BigDecimal(n2.toString()) else BigDecimal.ZERO

        return operand1.add(operand2)
    }

    /**
     * Subtract two number and return the difference in BigDecimal form
     *
     * @param n1 operand 1 (minuend), null will be interpreted as ZERO!
     * @param n2 operand 2 (subtrahend), null will be interpreted as ZERO!
     *
     * @return difference, result of subtraction
     */
    @JvmStatic
    fun subtract(n1:Number?, n2:Number?):BigDecimal {
        val minuend = if (n1 != null) BigDecimal(n1.toString()) else BigDecimal.ZERO
        val subtrahend = if (n2 != null) BigDecimal(n2.toString()) else BigDecimal.ZERO

        return minuend - subtrahend
    }

    /**
     * Multiply two number and return the product in BigDecimal form
     *
     * @param n1 operand 1, null will be interpreted as ZERO!
     * @param n2 operand 2, null will be interpreted as ZERO!
     *
     * @return product, result of multiplication
     */
    @JvmStatic
    fun multiply(n1:Number?, n2:Number?):BigDecimal {
        val operand1 = if (n1 != null) BigDecimal(n1.toString()) else BigDecimal.ZERO
        val operand2 = if (n2 != null) BigDecimal(n2.toString()) else BigDecimal.ZERO

        return operand1 * operand2
    }

    /**
     * Divide two number and return the quotient in BigDecimal form
     *
     * @param n1 operand 1, dividend, null will be interpreted as ZERO!!
     * @param n2 operand 2, divisor, null will be interpreted as ZERO!!
     *
     * @return quotient, result of division
     */
    @JvmStatic
    fun divide(n1:Number?, n2:Number?):BigDecimal {
        if (n2 == null || BigDecimal(n2.toString()) == BigDecimal.ZERO) {
            throw NumberFormatException("Divided by ZERO detected, ($n1 / $n2)")
        }

        val dividend = if (n1 != null) BigDecimal(n1.toString()) else BigDecimal.ZERO
        val divisor = BigDecimal(n2.toString())

        return dividend.divide(divisor, 16, RoundingMode.HALF_UP)
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
    @JvmStatic
    fun divide(n1:Number?, n2:Number?, scale:Int, roundingMode:Int):BigDecimal {
        if (n2 == null || BigDecimal(n2.toString()) == BigDecimal.ZERO) {
            throw NumberFormatException("Divided by ZERO detected, ($n1 / $n2)")
        }

        val dividend = if (n1 != null) BigDecimal(n1.toString()) else BigDecimal.ZERO
        val divisor = BigDecimal(n2.toString())

        return dividend.divide(divisor, scale, roundingMode)
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
    @JvmStatic
    fun divide(n1:Number?, n2:Number?, scale:Int, roundingMode:RoundingMode):BigDecimal {
        if (n2 == null || BigDecimal(n2.toString()) == BigDecimal.ZERO) {
            throw NumberFormatException("Divided by ZERO detected, ($n1 / $n2)")
        }

        val dividend = if (n1 != null) BigDecimal(n1.toString()) else BigDecimal.ZERO
        val divisor = BigDecimal(n2.toString())

        return dividend.divide(divisor, scale, roundingMode)
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
    @JvmStatic
    fun divide(a:BigDecimal, b:BigDecimal, precision:Int):BigDecimal {
        return a.divide(b, precision, RoundingMode.HALF_UP)
    }

    /**
     * Round up a double precision number
     *
     * @param a number to be rounded up
     * @param precision precision of result (decimal places)
     *
     * @return round up result to precision
     */
    @JvmStatic
    fun roundUp(a:Double, precision:Int):Double {
        val fac = Math.pow(10.0, precision.toDouble()).toLong()
        return Math.round(a * fac).toDouble() / fac
    }

    /**
     * Round up a double precision number
     *
     * @param a number to be rounded up
     * @param precision precision of result (decimal places)
     *
     * @return round up result to precision
     */
    @JvmStatic
    fun roundUp(a:Double?, precision:Int):Double {
        return roundUp(a!!.toDouble(), precision)
    }

    /**
     * Round up a big decimal
     *
     * @param a decimal to be rounded up
     * @param precision precision of result (decimal places)
     *
     * @return round up result to precision
     */
    @JvmStatic
    fun roundUp(a:BigDecimal, precision:Int):BigDecimal {
        return a.setScale(precision, RoundingMode.HALF_UP)
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
    @JvmStatic
    fun ceilOfUnit(a:Double, unit:Double):Double {
        return Math.ceil(a / unit) * unit
    }

    /**
     * Ceiling a big decimal to an unit
     *
     * @param a decimal to be ceilinged
     * @param unit target unit
     *
     * @return ceilinged decimal of unit
     */
    @JvmStatic
    fun ceilOfUnit(a:BigDecimal, unit:BigDecimal):BigDecimal {
        return a.divide(unit, 0, RoundingMode.CEILING) * unit
    }

    /**
     * Floor a double number precision to an unit
     * (e.g. any non multiple of unit will be floored to the nearest smaller multiple)
     *
     * @param a number to be floored
     * @param unit target unit
     * @return floored number of unit
     */
    @JvmStatic
    fun floorOfUnit(a:Double, unit:Double):Double {
        return Math.floor(a / unit) * unit
    }

    /**
     * Floor a big decimal to an unit
     *
     * @param a number to be floored
     * @param unit target unit
     *
     * @return floored decimal of unit
     */
    @JvmStatic
    fun floorOfUnit(a:BigDecimal, unit:BigDecimal):BigDecimal {
        return a.divide(unit, 0, RoundingMode.FLOOR) * unit
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
    @JvmStatic
    fun roundUpToUnit(a:Double, unit:Double):Double {
        return Math.floor(a / unit + 0.5) * unit
    }

    /**
     * Rounding up a big decimal to an unit
     *
     * @param a decimal to be rounded up
     * @param unit target unit
     * @return rounded up decimal of unit
     */
    @JvmStatic
    fun roundUpToUnit(a:BigDecimal, unit:BigDecimal):BigDecimal {
        return a.divide(unit, 0, RoundingMode.HALF_UP) * unit
    }

    /**
     * Ceiling a double precision number to a precision
     *
     * @param a number to be ceilinged
     * @param precision precision of result
     *
     * @return ceilinged number of precision
     */
    @JvmStatic
    fun ceiling(a:Double, precision:Int):Double {
        return BigDecimal(a).setScale(precision, BigDecimal.ROUND_CEILING).toDouble()
    }

    /**
     * Ceiling a double precision number to a precision
     *
     * @param a number to be ceilinged
     * @param precision precision of result
     *
     * @return ceilinged number of precision
     */
    @JvmStatic
    fun ceiling(a:Double?, precision:Int):Double {
        return ceiling(a!!.toDouble(), precision)
    }

    /**
     * Ceiling a double precision number to its integral value by calling Math.ceil
     *
     * @param a double precision number to be ceilinged
     *
     * @return ceilinged number
     */
    @JvmStatic
    fun ceiling(a:Double):Double {
        return Math.ceil(a)
    }

    /**
     * Ceiling a big decimal to its integral value
     *
     * @param a big decimal to be ceilinged
     *
     * @return ceilinged number
     */
    @JvmStatic
    fun ceiling(a:BigDecimal):BigDecimal {
        return a.divide(BigDecimal.ONE, 0, RoundingMode.CEILING)
    }

    /**
     * Ceilling a big decimal to target precision
     * 
     * @param a big decimal value
     * @param precision required precision
     * 
     * @return ceillinged number
     */
    @JvmStatic
    fun ceiling(a: BigDecimal, precision: Int): BigDecimal {
        return a.divide(BigDecimal.ONE, precision, RoundingMode.CEILING)
    }

    /**
     * Floor a double precision number to a specific precision
     *
     * @param a double precision number
     * @param precision precision of the result
     *
     * @return floored number of precision
     */
    @JvmStatic
    fun floor(a:Double, precision:Int):Double {
        return BigDecimal(a).setScale(precision, BigDecimal.ROUND_FLOOR).toDouble()
    }

    /**
     * Floor a big decimal number to a specific precision
     * 
     * @param a big decimal number
     * @param precision precision of the result
     */
    @JvmStatic
    fun floor(a: BigDecimal, precision: Int): BigDecimal {
        return a.setScale(precision, BigDecimal.ROUND_FLOOR)
    }

    /**
     * Floor a double precision number to a precision
     *
     * @param a double precision number to floored
     * @param precision precision of result
     *
     * @return floored number of precision
     */
    @JvmStatic
    fun floor(a:Double?, precision:Int):Double {
        return floor(a!!.toDouble(), precision)
    }

    /**
     * Floor a double precision number to its integral value by calling Math.floor
     *
     * @param a double precision number to floored
     *
     * @return floored number
     */
    @JvmStatic
    fun floor(a:Double):Double {
        return Math.floor(a)
    }

    /**
     * Floor a big decimal to its integral value
     * @param a big decimal to floored
     *
     * @return floor big decimal
     */
    @JvmStatic
    fun floor(a:BigDecimal):BigDecimal {
        return a.divide(BigDecimal.ONE, 0, RoundingMode.FLOOR)
    }

    /**
     * Natural log of a double precision number
     *
     * @param n number to be logged
     *
     * @return log result
     */
    @JvmStatic
    fun log(n:Double):Double {
        return Math.log(n)
    }

    /**
     * Log a double precision number of specific base
     *
     * @param n number to be logged
     * @param base base of log
     *
     * @return log result
     */
    @JvmStatic
    fun log(n:Double, base:Double):Double {
        return Math.log(n) / Math.log(base)
    }

    /**
     * Log a big decimal of specific base
     *
     * @param num big decimal to be logged
     * @param base base of log, default Math.E
     * @param precision result precision, default 16
     *
     * @return log result trimmed(rounded) by precision
     */
    @JvmStatic
    @JvmOverloads
    fun log(
        num:BigDecimal, base:BigDecimal = BigDecimal(Math.E),
        precision:Int = DEFAULT_BIG_DECIMAL_PRECISION
    ): BigDecimal {
        var n = num
        if (n.compareTo(base) == 0) {
            return BigDecimal.ONE
        } else if (n.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ZERO
        } else if (base > BigDecimal.ZERO) {
            if (base > BigDecimal.ONE) {
                if (n >= BigDecimal.ONE.divide(base, precision, RoundingMode.HALF_UP)) {
                    var result = BigDecimal.ZERO
                    while (n >= base) {
                        result = result.add(BigDecimal.ONE)
                        n = n.divide(base, precision, RoundingMode.HALF_UP)
                    }

                    if (n > BigDecimal.ONE) {
                        var fraction = BigDecimal("0.5")
                        var curr = base
                        do {
                            curr = sqrt(curr)

                            val c = curr.compareTo(n)
                            if (c < 0) {
                                n = n.divide(curr, precision, RoundingMode.HALF_UP)
                                result = result.add(fraction)
                            } else if (c == 0) {
                                result = result.add(fraction)
                                break
                            }

                            fraction = fraction.divide(TWO, precision, RoundingMode.FLOOR)
                        } while (fraction > DEFAULT_BIG_TOLERANCE)
                    }

                    return result
                } else {
                    return log(BigDecimal.ONE.divide(n, precision, RoundingMode.HALF_UP), base, precision).negate()
                }
            } else if (base < BigDecimal.ONE) {
                if (n < base) {
                    var result = BigDecimal.ZERO

                    while (n <= base) {
                        result = result.add(BigDecimal.ONE)
                        n = n.divide(base, precision, RoundingMode.HALF_UP)
                    }

                    if (n.compareTo(BigDecimal.ONE) != 0 && n > base) {
                        var fraction = BigDecimal("0.5")
                        var curr = base
                        do {
                            curr = sqrt(curr)
                            val c = curr.compareTo(n)
                            if (c > 0) {
                                n = n.divide(curr, precision, RoundingMode.HALF_UP)
                                result = result.add(fraction)
                            } else if (c == 0) {
                                result = result.add(fraction)
                                break
                            }

                            fraction = fraction.divide(TWO, precision, RoundingMode.FLOOR)
                        } while (fraction > DEFAULT_BIG_TOLERANCE)
                    }

                    return result
                } else {
                    return log(
                    BigDecimal.ONE.divide(n, precision, RoundingMode.HALF_UP), base, precision).negate()
                }
            } else {
                throw IllegalArgumentException("Log base 1 is indetermine!")
            }
        } else {
            throw IllegalArgumentException("Base must greater than 0")
        }
    }

    /**
     * Get initial approximation (guess) of square root of a big decimal
     *
     * @param n target number
     *
     * @return Initial approximation (guess) of root
     */
    private fun getInitialApproximation(n:BigDecimal):BigDecimal {
        val integerPart = n.toBigInteger()
        var length = integerPart.toString().length
        if (length % 2 == 0) {
            length--
        }
        length /= 2
        val guess = BigDecimal.ONE.movePointRight(length)
        return guess
    }

    /**
     * Square root a double precision number by calling Math.sqrt
     *
     * @param n target number
     * @return Square root result
     */
    @JvmStatic
    fun sqrt(n:Double):Double {
        return Math.sqrt(n)
    }

    /**
     * Square root a big integer
     *
     * @param n target big integer
     *
     * @return Square root result
     */
    @JvmStatic
    fun sqrt(n:BigInteger):BigDecimal {
        return sqrt(BigDecimal(n))
    }

    /**
     * Square root a big decimal will specified scale, iteration limit and tolerance
     *
     * @param n target big decimal
     * @param scale round up scale, default 20
     * @param maxIteration maximum iteration limit of guessing, default 50
     * @param tolerance error tolerance, default, deafult 10^-20
     *
     * @return square root result
     */
    @JvmStatic
    @JvmOverloads
    fun sqrt(
        n:BigDecimal, scale:Int = DEFAULT_SCALE, maxIteration:Int = DEFAULT_MAX_ITERATIONS,
        tolerance:BigDecimal = DEFAULT_BIG_TOLERANCE
    ):BigDecimal {
        if (n.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO
        } else if (n < BigDecimal.ZERO) {
            throw IllegalArgumentException()
        }

        val initialGuess = getInitialApproximation(n)

        var lastGuess:BigDecimal
        var guess = BigDecimal(initialGuess.toString())

        var iterations = 0
        var more = true
        var error:BigDecimal
        while (more) {
            lastGuess = guess
            guess = n.divide(guess, scale, RoundingMode.HALF_UP)
            guess = guess.add(lastGuess).divide(TWO, scale, RoundingMode.HALF_UP)

            error = (n - guess) * guess
            if (++iterations >= maxIteration) {
                more = false
            } else if (lastGuess == guess) {
                more = error.abs() >= tolerance
            }
        }
        return guess
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
    private fun operate(a:Double, b:Double, operation:Operation, precision:Int):Double {
        var localPrecision = precision
        if (localPrecision < 0) {
            localPrecision = 0
        }
        val n1 = BigDecimal(a)
        val n2 = BigDecimal(b)
        var result:BigDecimal? = null
        when (operation) {
            Operation.ADD -> result = n1.add(n2)
            Operation.SUBTRACT -> result = n1 - n2
            Operation.MULTIPLY -> result = n1 * n2
            Operation.DIVIDE -> if (n2.toDouble() != 0.0) {
                result = n1.divide(n2, localPrecision, BigDecimal.ROUND_HALF_UP)
            }
        }
        if (result == null) {
            return -1.0
        } else {
            return result.setScale(localPrecision, BigDecimal.ROUND_HALF_UP).toDouble()
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
    @JvmStatic
    fun getHighestMultiple(factor:Double, maximumRange:Double):Double {
        return multiply(Math.floor(divide(maximumRange, factor)), factor)
    }

    /**
     * Get positing value of a double precision number only, otherwise 0 is returned
     *
     * @param n target double precision number
     *
     * @return positive value of provided input, 0 for any negative numbers
     */
    @JvmStatic
    fun getPositive(n:Double):Double {
        return if (n > 0) n else 0.0
    }

    /**
     * Get negative value of double precision number only, otherwise 0 is returned
     *
     * @param n target double precision number
     * @return negative value of provided input, 0 for any positive numbers
     */
    @JvmStatic
    fun getNegative(n:Double):Double {
        return if (n < 0) n else 0.0
    }

    /**
     * Get the highest common factor of two big integer
     *
     * @param n1 big integer 1
     * @param n2 big integer 2
     *
     * @return the highest common factor
     */
    @JvmStatic
    fun hcf(n1:BigInteger, n2:BigInteger):BigInteger {
        return gcd(n1, n2)
    }

    /**
     * Get the greatest common divisor of two big integer
     *
     * @param n1 big integer 1
     * @param n2 big integer 2
     *
     * @return the greatest common divisor
     */
    @JvmStatic
    fun gcd(n1:BigInteger, n2:BigInteger):BigInteger {
        if (n2.compareTo(BigInteger.ZERO) == 0) {
            return n1
        } else {
            return gcd(n2, n1.remainder(n2))
        }
    }

    /**
     * Get the highest common factor of two integer
     *
     * @param n1 integer 1
     * @param n2 integer 2
     *
     * @return the highest common factor
     */
    @JvmStatic
    fun hcf(n1:Int, n2:Int):Int {
        return gcd(n1, n2)
    }

    /**
     * Get the greatest common divisor of two integer
     *
     * @param n1 integer 1
     * @param n2 integer 2
     *
     * @return the greatest common divisor
     */
    @JvmStatic
    fun gcd(n1:Int, n2:Int):Int {
        if (n2 == 0) {
            return n1
        } else {
            return gcd(n2, n1 % n2)
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
    @JvmStatic
    fun lcm(n1:Int, n2:Int):Int {
        val small:Int
        val big:Int

        if (n1 < n2) {
            small = n1
            big = n2
        } else {
            small = n2
            big = n1
        }

        if (big % small == 0) {
            return big
        } else {
            var f = 2
            var n = big * f
            while (n % small != 0) {
                n = big * ++f
            }
            return n
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
    @JvmStatic
    fun lcm(n1:BigInteger, n2:BigInteger):BigInteger {
        val small:BigInteger
        val big:BigInteger

        if (n1 < n2) {
            small = n1
            big = n2
        } else {
            small = n2
            big = n1
        }

        if (big.remainder(small).compareTo(BigInteger.ZERO) == 0) {
            return big
        } else {
            var f = BigInteger("2")
            var n = big * f

            while (n.remainder(small).compareTo(BigInteger.ZERO) != 0) {
                f += BigInteger.ONE
                n = big * f
            }
            return n
        }
    }

    /**
     * @deprecated use utils.random.RandomUtil.generateRandomIntegerArray(min, max, size, distinct) instead
     * 
     * Generate a random integer array
     *
     * @param min minimum value of random value
     * @param max maximum value of random value
     * @param size size of result array
     * @param distinct flag indicating every value should be distinct
     *
     * @return random integer array
     */
    @JvmStatic
    fun generateRandomIntegerArray(min:Int, max:Int, size:Int, distinct:Boolean):IntArray {
        return RandomUtil.generateRandomIntegerArray(min, max, size, distinct)
    }

    /**
     * @deprecated utils.random.RandomUtil.generateRandomIntegerArray(min, max, size, distinct, randomGenerator) instead
     * 
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
    @JvmStatic
    fun generateRandomIntegerArray(min:Int, max:Int, size:Int, distinct:Boolean, randomGenerator:Random):IntArray {
        return RandomUtil.generateRandomIntegerArray(min, max, size, distinct, randomGenerator)
    }

    /**
     * @deprecated use utils.random.RandomUtil#generateRandomNumberArray(min, max, precision, size, distinct) instead
     * 
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
    @JvmStatic
    fun generateRandomNumberArray(
        min:Double, max:Double, precision:Int, size:Int, distinct:Boolean
    ):DoubleArray {
        return RandomUtil.generateRandomNumberArray(min, max, precision, size, distinct)
    }

    /**
     * @deprecated use utils.random.RandomUtil.generateRandomNumberArray(min, max, precision, size, randomGenerator) instead
     * 
     * Generate a random number array with given random generator for consistent result
     *
     * @param min minimum bound of random value
     * @param max maximum bound of random value
     * @param precision precision of random value
     * @param size size of result array
     * @param distinct distinct flag
     * @param randomGenerator given random generator
     *
     * @return random number array
     */
    @JvmStatic
    fun generateRandomNumberArray(
        min:Double, max:Double, precision:Int, size:Int, distinct:Boolean, randomGenerator:Random
    ):DoubleArray {
        return RandomUtil.generateRandomNumberArray(min, max, precision, size, distinct, randomGenerator)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomInteger(min, max) instead
     * 
     * Generate a random integer
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random integer
     */
    @JvmStatic
    fun randomInteger(min:Int, max:Int):Int {
        return RandomUtil.randomInteger(min, max)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomInteger(min, max, randomGenerator) instead
     * 
     * Generate a random integer with given random generator, for consistent result
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param randomGenerator given random generator
     *
     * @return random integer
     */
    @JvmStatic
    fun randomInteger(min:Int, max:Int, randomGenerator:Random):Int {
        return RandomUtil.randomInteger(min, max, randomGenerator)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomIntegerWithNormalDistribution(min, max, mean, deviation) instead
     * 
     * Generate a random integer with standard distribution
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param mean mean value
     * @param deviation deviation
     *
     * @return random integer
     */
    @JvmStatic
    fun randomIntegerWithNormalDistribution(min: Int, max: Int, mean: Int, deviation: Int): Int {
        return RandomUtil.randomIntegerWithNormalDistribution(min, max, mean, deviation)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomIntegerWithNormalDistribution(min, max, mean, deviation, randomGenerator) instead
     * 
     * Generate a random integer with standard distribution
     * 
     * @param min minimum bound
     * @param max minimum bound
     * @param mean mean value
     * @param deviation deviation
     * @param randomGenerator random generator
     * 
     * @return random integer
     */
    @JvmStatic
    fun randomIntegerWithNormalDistribution(min: Int, max: Int, mean: Int, deviation: Int, randomGenerator: Random): Int {
        return RandomUtil.randomIntegerWithNormalDistribution(min, max, mean, deviation, randomGenerator)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomWeightedValue(weightedRandomValueHolder) instead
     * 
     * Generate a random value from weighted random value holder, see {@link utils.MathUtil.WeightedRandomValueHolder}
     * 
     * @param weightedRandomValueHolder weighted random value holder
     * 
     * @return generated random value
     */
    @JvmStatic
    fun <E> randomWeightedValue(weightedRandomValueHolder: WeightedRandomValueHolder<E>): E {
        return RandomUtil.randomWeightedValue(weightedRandomValueHolder)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomLong(min, max)
     * 
     * Generate a random long integer
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random long integer
     */
    @JvmStatic
    fun randomLong(min:Long, max:Long):Long {
        return RandomUtil.randomLong(min, max)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomLong(min, max, randomGenerator) instead
     * 
     * Generate a random long integer with given random generator
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param randomGenerator given random generator
     *
     * @return random long integer
     */
    @JvmStatic
    fun randomLong(min:Long, max:Long, randomGenerator:Random):Long {
        return RandomUtil.randomLong(min, max, randomGenerator)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomNumber(min, max) instead
     *
     * Generate a random number
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random number
     */
    @JvmStatic
    fun randomNumber(min:Double, max:Double):Double {
        return RandomUtil.randomNumber(min, max)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomNumber(min, max, randomGenerator) instead
     * 
     * Generate a random number based on given random generator for consistent output
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param randomGenerator given random generator
     *
     * @return random number
     */
    @JvmStatic
    fun randomNumber(min:Double, max:Double, randomGenerator:Random):Double {
        return RandomUtil.randomNumber(min, max, randomGenerator)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomNumber(min, max, precision) instead
     * 
     * Generate a random number
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param precision precision of result
     *
     * @return random number
     */
    @JvmStatic
    fun randomNumber(min:Double, max:Double, precision:Int):Double {
        return RandomUtil.randomNumber(min, max, precision)
    }

    /**
     * @deprecated use utils.random.RandomUtil.randomNumber(min, max, precision, randomGenerator) instead
     * 
     * Generate a random number with given random generator
     *
     * @param min minimum bound
     * @param max maximum bound
     * @param precision precision of result
     * @param randomGenerator given random generator
     *
     * @return random number
     */
    @JvmStatic
    fun randomNumber(min:Double, max:Double, precision:Int, randomGenerator:Random):Double {
        return RandomUtil.randomNumber(min, max, precision, randomGenerator)
    }

    /**
     * Get digit count of an integer
     *
     * @param val target integer
     *
     * @return digit count of provided integer value
     */
    @JvmStatic
    fun getDigitCount(value :Int):Int {
        return Integer.toString(value).length
    }

    /**
     * Square of number
     *
     * @param v target number
     *
     * @return square of target number
     */
    @JvmStatic
    fun square(v:Double):Double {
        return v * v
    }

    /**
     * Square of integer
     * 
     * @param v target integer
     * 
     * @return square of target number
     */
    fun square(v: Int): Int {
        return v * v
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
    @JvmStatic
    fun getLength(x1:Double, y1:Double, x2:Double, y2:Double):Double {
        return Math.sqrt(square(x1 - x2) + square(y1 - y2))
    }

    /**
     * Get the length of two points
     *
     * @param p1 point 1
     * @param p2 point 2
     *
     * @return length of two points
     */
    @JvmStatic
    fun getLength(p1:Point2D, p2:Point2D):Double {
        return getLength(p1.x, p1.y, p2.x, p2.y)
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
    @JvmStatic
    fun getPointSegmentDistance(p1:Point2D, p2:Point2D, p:Point2D):Double {
        return getPointSegmentDistance(p1.x, p1.y, p2.x, p2.y, p.x, p.y)
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
    @JvmStatic
    fun getPointSegmentDistance(
        inX1:Double, inY1:Double, inX2:Double, inY2:Double, inPx:Double, inPy:Double
    ):Double {
        var x2 = inX2
        var y2 = inY2
        var px = inPx
        var py = inPy
        x2 -= inX1
        y2 -= inY1
        px -= inX1
        py -= inY1
        var dotprod = px * x2 + py * y2
        val projlenSq:Double
        if (dotprod <= 0.0) {
            projlenSq = 0.0
        } else {
            px = x2 - px
            py = y2 - py
            dotprod = px * x2 + py * y2
            if (dotprod <= 0.0) {
                projlenSq = 0.0
            } else {
                projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2)
            }
        }
        var lenSq = px * px + py * py - projlenSq
        if (lenSq < 0) {
            lenSq = 0.0
        }
        return Math.sqrt(lenSq)
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
    @JvmStatic
    fun getHypothesisSquare(side1:Double, side2:Double):Double {
        return side1 * side1 + side2 * side2
    }

    /**
     * Get right angled triangle's hypothesis length with known side lengths
     *
     * @param side1 side length 1
     * @param side2 side length 2
     *
     * @return hypothesis length
     */
    @JvmStatic
    fun getHypothesis(side1:Double, side2:Double):Double {
        return Math.sqrt(getHypothesisSquare(side1, side2))
    }

    /**
     * Get right angled triangle's side length squred with known hypothesis and one side length
     *
     * @param hypo hypothesis length
     * @param side side length
     *
     * @return side length squared
     */
    @JvmStatic
    fun getTriangleSideLengthSquare(hypo:Double, side:Double):Double {
        return hypo * hypo - side * side
    }

    /**
     * Get right angled triangle's side length with known hypothesis and one side length
     *
     * @param hypo hypothesis length
     * @param side side length
     *
     * @return side length
     */
    @JvmStatic
    fun getTriangleSideLength(hypo:Double, side:Double):Double {
        return Math.sqrt(getTriangleSideLengthSquare(hypo, side))
    }

    /**
     * Normalize an angle (in radian)
     * any value smaller than 0 or greater than 2 x PI will be normalized to range 0 ~ 2 x PI
     *
     * @param rad target angle (in radian)
     *
     * @return result of normalization (in radian)
     */
    @JvmStatic
    fun normalizeAngleRadian(rad:Double):Double {
        if (rad < 0) {
            return rad % (Math.PI * 2) + Math.PI * 2
        } else if (rad == Math.PI * 2) {
            return 0.0
        } else {
            return rad % (Math.PI * 2)
        }
    }

    /**
     * Normalize an angle (in degree)
     * any value smaller than 0 or greater than 360 will be normalized to range 0 ~ 360
     *
     * @param degree target angle (in degree)
     *
     * @return result of normalization (in degree)
     */
    @JvmStatic
    fun normalizeAngleDegree(degree:Double):Double {
        if (degree < 0) {
            return degree % 360 + 360
        } else if (degree == 360.0) {
            return 0.0
        } else {
            return degree % 360
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
    @JvmStatic
    fun isFloatingPointNumberEquals(n1:Double, n2:Double):Boolean {
        return Math.abs(n1 - n2) <= DEFAULT_TOLERANCE
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
    @JvmStatic
    fun isFloatingPointNumberEquals(n1:Double, n2:Double, tolerance:Double):Boolean {
        return Math.abs(n1 - n2) <= tolerance
    }

    /**
     * Checking whether the difference of two decimals are within provided tolerance
     *
     * @param d1 decimal 1
     * @param d2 decimal 2
     * @param tolerance tolerance of difference to say two decimals are equals, default to 0.000001
     *
     * @return result of checking
     */
    @JvmStatic
    @JvmOverloads
    fun isDecimalEquals(
            d1:BigDecimal, d2:BigDecimal, tolerance:BigDecimal = BigDecimal(DEFAULT_TOLERANCE)
    ):Boolean {
        return (d1 - d2).abs() <= tolerance
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
    @JvmStatic
    fun isDecimalEquals(d1:BigDecimal, d2:BigDecimal, tolerance:Double):Boolean {
        return isDecimalEquals(d1, d2, BigDecimal(tolerance))
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
    @JvmStatic
    fun solveQuadraticEquation(a:Double, b:Double, c:Double):QuadraticSolution {
        val sol:QuadraticSolution
        if (a == 0.0) {
            sol = QuadraticSolution(-c / b)
        } else {
            val discriminant = Math.sqrt(b * b - 4.0 * a * c)
            if (discriminant == 0.0) {
                // if discriminant is zero there will only be one single solution
                sol = QuadraticSolution(-b / (2 * a))
            } else if (discriminant < 0) {
                sol = QuadraticSolution(java.lang.Double.NaN)
            } else {
                sol = QuadraticSolution((discriminant - b) / (2 * a), (-b - discriminant) / (2 * a))
            }
        }
        return sol
    }

    /**
     * Get cosine of angle A of a triangle with all known side
     * Law of cosine: cos(A) = a * a + b * b - c * c / 2ab == a^2 + b^2 - c^2 / 2ab
     * Angle side location: side a > angle A > side b > angle B > side c > angle C > side a
     *
     * @param a side length a
     * @param b side length b
     * @param c side length c
     *
     * @return cosine of angle A
     */
    @JvmStatic
    fun getCosineWithKnownSideLength(a:Double, b:Double, c:Double):Double {
        return (a * a + b * b - c * c) / (2.0 * a * b)
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
    @JvmStatic
    fun getAngleWithKnownSideLength(a:Double, b:Double, c:Double):Double {
        return Math.acos(getCosineWithKnownSideLength(a, b, c))
    }

    /**
     * Get the angle to horizontal of a line (p1, p2)
     *
     * @param p1 point 1 of line
     * @param p2 point 2 of line
     *
     * @return angle to horizontal (in radian)
     */
    @JvmStatic
    fun getAngleToHorizontal(p1:Point2D, p2:Point2D):Double {
        return getAngleToHorizontal(p1.x, p1.y, p2.x, p2.y)
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
    @JvmStatic
    fun getAngleToHorizontal(x1:Double, y1:Double, x2:Double, y2:Double):Double {
        val a = x2 - x1
        val b = y2 - y1
        val rad = Math.abs(Math.atan(b / a))
        if (a >= 0 && b >= 0) {
            return rad
        } else if (a >= 0 && b < 0) {
            return Math.PI * 2 - rad
        } else if (a < 0 && b < 0) {
            return Math.PI + rad
        } else {
            return Math.PI - rad
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
    @JvmStatic
    fun getDestinationPoint(x1:Double, y1:Double, angleRadian:Double, length:Double):Point2D {
        val a = Math.cos(angleRadian) * length
        val b = Math.sin(angleRadian) * length
        return Point2D.Double(x1 + a, y1 + b)
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
    @JvmStatic
    fun getDestinationPoint(ref:Point2D, angle:Double, length:Double):Point2D {
        return getDestinationPoint(ref.x, ref.y, angle, length)
    }

    /**
     * Calculate side length of a right angled triangle, with knwon hypo and one side
     *
     * @param hypo hypo length
     * @param oneSide length of one side
     *
     * @return length of another side
     */
    @JvmStatic
    fun calculateRightTriangleSideLength(hypo:Double, oneSide:Double):Double {
        return Math.sqrt(hypo * hypo - oneSide * oneSide)
    }

    /**
     * Calculate side length of a right angled triangle, with known hypotenuse and one side (BigDecimal)
     *
     * @param hypo hypotenuse length
     * @param oneSide length of one side
     *
     * @return length of another side
     */
    @JvmStatic
    fun calculateRightTriangleSideLength(hypo:BigDecimal, oneSide:BigDecimal):BigDecimal {
        return MathUtil.sqrt(hypo.pow(2) - oneSide.pow(2))
    }

    /**
     * Calculate hypotenuse length of a right angled triangle with known two side
     *
     * @param firstSide length of first side
     * @param secondSide length of second side
     *
     * @return length of hypotenuse
     */
    @JvmStatic
    fun calculateRightTriangleHypotenuse(firstSide:Double, secondSide:Double):Double {
        return Math.sqrt(firstSide * firstSide + secondSide * secondSide)
    }


    /**
     * Calculate hypotenuse length of a right angled triangle with known two side (BigDecimal)
     *
     * @param firstSide length of first side
     * @param secondSide length of second side
     *
     * @return length of hypotenuse
     */
    @JvmStatic
    fun calculateRightTriangleHypotenuse(firstSide:BigDecimal, secondSide:BigDecimal):BigDecimal {
        return MathUtil.sqrt(firstSide.pow(2).add(secondSide.pow(2)))
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
    @JvmStatic
    fun calculateTriangleArea(x1:Double, y1:Double,
        x2:Double, y2:Double, x3:Double, y3:Double
    ):Double {
        val a = getLength(x1, y1, x2, y2)
        val b = getLength(x1, y1, x3, y3)
        val c = getLength(x2, y2, x3, y3)

        return calculateTriangleArea(a, b, c)
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
    @JvmStatic
    fun calculateTriangleArea(p1:Point2D, p2:Point2D, p3:Point2D):Double {
        return calculateTriangleArea(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
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
    @JvmStatic
    fun calculateTriangleArea(a:Double, b:Double, c:Double):Double {
        val a2 = square(a)
        val b2 = square(b)
        val c2 = square(c)
        val a4 = square(a2)
        val b4 = square(b2)
        val c4 = square(c2)

        return Math.sqrt((a2 * b2 + b2 * c2 + c2 * a2) * 2 - (a4 + b4 + c4)) / 4
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
    @JvmStatic
    fun withinRange(value:Int, n1:Int, n2:Int):Boolean {
        var min: Int
        var max: Int
        if (n1 > n2) {
            max = n1
            min = n2
        } else {
            max = n2
            min = n1
        }
        return value >= min && value <= max
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
    @JvmStatic
    fun withinRange(value:Double, n1:Double, n2:Double):Boolean {
        var min: Double
        var max: Double
        if (n1 > n2) {
            max = n1
            min = n2
        } else {
            max = n2
            min = n1
        }
        return value >= min && value <= max
    }

    /**
     * Calculate sum of a sequence (incremented by 1) starting from 1 to n
     *
     * @param n ending number
     *
     * @return sum of sequence
     */
    @JvmStatic
    fun sumOfSeq(n:Int):Long {
        if (n <= 0) {
            return 0
        }
        val time = (n / 2).toLong()
        if (n % 2 == 0) {
            return (n + 1) * time
        } else {
            return (n + 1) * time + (time + 1)
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
    @JvmStatic
    fun sumOfSeq(a:Int, b:Int):Long {
        var min:Int
        var max:Int
        if (a < b) {
            min = a
            max = b
        } else {
            min = b
            max = a
        }
        return sumOfSeq(max) - sumOfSeq(min - 1)
    }

    /**
     * Parse a string into short value
     *
     * @param str source string
     * @param defaultValue default value when parse failed
     *
     * @return parsed short value
     */
    @JvmStatic
    fun parseShort(str:String, defaultValue:Short):Short {
        return parseShort(str, 10, defaultValue)
    }

    /**
     * Parse a string into short value
     * @param str source string
     * @param radix parsing radix
     * @param defaultValue default value when parse failed
     *
     * @return parsed short value
     */
    @JvmStatic
    fun parseShort(str:String?, radix:Int, defaultValue:Short):Short {
        if (str == null) {
            return defaultValue
        } else {
            try {
                return java.lang.Short.parseShort(str, radix)
            } catch (nfe:NumberFormatException) {
                return defaultValue
            }
        }
    }

    /**
     * Parse a string into BigDecimal value
     *
     * @param str source string
     * @param defaultValue default value when parse failed, default zero
     *
     * @return parsed BigDecimal value, if anything goes wrong always return zero
     */
    @JvmStatic
    @JvmOverloads
    fun parseBigDecimalNotNull(str: String, defaultValue:BigDecimal = BigDecimal.ZERO): BigDecimal {
        return parseBigDecimal(str, defaultValue) ?: BigDecimal.ZERO
    }

    /**
     * Parse a string into BigDecimal value allow default as null
     *
     * @param str source string
     * @param defaultValue default value when parse failed, default zero
     *
     * @return parsed BigDecimal value
     */
    @JvmStatic
    @JvmOverloads
    fun parseBigDecimal(str:String, defaultValue:BigDecimal? = BigDecimal.ZERO):BigDecimal? {
        if (!StringUtil.isEmptyString(str)) {
            try {
                val dec = BigDecimal(str)
                return dec
            } catch (nfe:NumberFormatException) {
                return defaultValue
            }
        } else {
            return defaultValue
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
    @JvmStatic
    fun parseInt(str:String, defaultValue:Int):Int {
        return parseInt(str, 10, defaultValue)
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
    @JvmStatic
    fun parseInt(str:String?, radix:Int, defaultValue:Int):Int {
        if (str == null || str == "") {
            return defaultValue
        } else {
            try {
                return Integer.parseInt(str, radix)
            } catch (nfe:NumberFormatException) {
                return defaultValue
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
    @JvmStatic
    fun parseLong(str:String, defaultValue:Long):Long {
        return parseLong(str, 10, defaultValue)
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
    @JvmStatic
    fun parseLong(str:String?, radix:Int, defaultValue:Long):Long {
        if (str == null) {
            return defaultValue
        } else {
            try {
                return java.lang.Long.parseLong(str, radix)
            } catch (nfe:NumberFormatException) {
                return defaultValue
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
    @JvmStatic
    fun parseFloat(str:String?, defaultValue:Float):Float {
        if (str == null) {
            return defaultValue
        } else {
            try {
                return java.lang.Float.parseFloat(str)
            } catch (nfe:NumberFormatException) {
                return defaultValue
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
    @JvmStatic
    fun parseDouble(str:String?, defaultValue:Double):Double {
        if (str == null) {
            return defaultValue
        } else {
            try {
                return java.lang.Double.parseDouble(str)
            } catch (nfe:NumberFormatException) {
                return defaultValue
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
    @JvmStatic
    fun minValueLimit(value:Int, min:Int):Int {
        return if (value < min) min else value
    }

    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     *
     * @param value target value
     * @param max maximum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun maxValueLimit(value:Int, max:Int):Int {
        return if (value > max) max else value
    }

    /**
     * Limit a value to minimum & maximum value with default,
     * if value is smaller than minimum / greater than maximum, default value is returned
     * otherwise self value is returned
     *
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value, default 0
     *
     * @return result of limit
     */
    @JvmStatic
    @JvmOverloads
    fun valueLimit(value:Int, min:Int, max:Int, defaultValue:Int = 0):Int {
        return if (value >= min && value <= max) value else defaultValue
    }

    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     *
     * @param value target value
     * @param min minimum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun minValueLimit(value:Short, min:Short):Short {
        return if (value < min) min else value
    }

    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     *
     * @param value target value
     * @param max maximum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun maxValueLimit(value:Short, max:Short):Short {
        return if (value > max) max else value
    }

    /**
     * Limit a value to minimum & maximum value with default,
     * if value is smaller than minimum / greater than maximum, default value is returned
     * otherwise self value is returned
     *
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value, default zero
     *
     * @return result of limit
     */
    @JvmStatic
    @JvmOverloads
    fun valueLimit(value:Short, min:Short, max:Short, defaultValue:Short = 0.toShort()):Short {
        return if (value >= min && value <= max) value else defaultValue
    }

    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     *
     * @param value target value
     * @param min minimum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun minValueLimit(value:Float, min:Float):Float {
        return if (value < min) min else value
    }

    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     *
     * @param value target value
     * @param max maximum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun maxValueLimit(value:Float, max:Float):Float {
        return if (value > max) max else value
    }

    /**
     * Limit a value to minimum & maximum value with default,
     * if value is smaller than minimum / greater than maximum, default value is returned
     * otherwise self value is returned
     *
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value, default 0
     *
     * @return result of limit
     */
    @JvmStatic
    @JvmOverloads
    fun valueLimit(value:Float, min:Float, max:Float, defaultValue:Float = 0f):Float {
        return if (value >= min && value <= max) value else defaultValue
    }

    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     *
     * @param value target value
     * @param min minimum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun minValueLimit(value:Double, min:Double):Double {
        return if (value < min) min else value
    }

    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     *
     * @param value target value
     * @param max maximum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun maxValueLimit(value:Double, max:Double):Double {
        return if (value > max) max else value
    }

    /**
     * Limit a value to minimum & maximum value with default,
     * if value is smaller than minimum / greater than maximum, default value is returned
     * otherwise self value is returned
     *
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value
     * @return result of limit
     */
    @JvmStatic
    @JvmOverloads
    fun valueLimit(value:Double, min:Double, max:Double, defaultValue:Double = 0.0):Double {
        return if (value >= min && value <= max) value else defaultValue
    }

    /**
     * Limit a value to a minimum value, i.e. if value is smaller than minimum, minimum is returned
     *
     * @param value target value
     * @param min minimum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun minValueLimit(value:Long, min:Long):Long {
        return if (value < min) min else value
    }

    /**
     * Limit a value to a maximum value, i.e. if value is greater than maximum, maximum is returned
     *
     * @param value target value
     * @param max maximum value
     *
     * @return result of limit
     */
    @JvmStatic
    fun maxValueLimit(value:Long, max:Long):Long {
        return if (value > max) max else value
    }

    /**
     * Limit a value to minimum & maximum value with default,
     * if value is smaller than minimum / greater than maximum, default value is returned
     * otherwise self value is returned
     *
     * @param value target value
     * @param min minimum value
     * @param max maximum value
     * @param defaultValue default value
     *
     * @return result of limit
     */
    @JvmStatic
    @JvmOverloads
    fun valueLimit(value:Long, min:Long, max:Long, defaultValue:Long = 0):Long {
        return if (value >= min && value <= max) value else defaultValue
    }

    /**
     * Find the maximum value within integer values
     *
     * @param values source integer values
     *
     * @return maximum integer value
     */
    @JvmStatic
    fun integerMax(vararg values:Int):Int {
        if (values.size > 0) {
            var max = values[0]
            for (i in 1..values.size - 1) {
                if (max < values[i]) {
                    max = values[i]
                }
            }
            return max
        } else {
            return Integer.MIN_VALUE
        }
    }

    /**
     * Find the maximum value within integer value list
     *
     * @param values source integer value list
     *
     * @return maximum integer value
     */
    @JvmStatic
    fun integerMax(values:List<Int>?):Int {
        if (values == null || values.size == 0) {
            throw IllegalArgumentException("Values is null or no value contained!")
        } else {
            var max = values[0]
            for (i in 1..values.size - 1) {
                val v = values[i]
                if (max < v) {
                    max = v
                }
            }
            return max
        }
    }

    /**
     * Find the maximum value within number values
     *
     * @param values source number values
     *
     * @return maximum number values
     */
    @JvmStatic
    fun doubleMax(vararg values:Double):Double {
        var max = java.lang.Double.MIN_VALUE
        for (v in values) {
            if (java.lang.Double.isNaN(v)) {
                return v
            }
            if (max < v) {
                max = v
            }
        }
        return max
    }

    /**
     * Find the maximum value within number value list
     *
     * @param values source number value list
     *
     * @return maximum number value
     */
    @JvmStatic
    fun doubleMax(values:List<Double>?):Double {
        if (values == null || values.size == 0) {
            throw IllegalArgumentException("Values is null or no value contained!")
        } else {
            var max = values[0]
            for (i in 1..values.size - 1) {
                val v = values[i]
                if (v > max) {
                    max = v
                }
            }
            return max
        }
    }

    /**
     * Find the maximum value within BigDecimals
     *
     * @param values source BigDecimals
     * @return maximum BigDecimal
     */
    @JvmStatic
    fun bigDecimalMax(vararg values:BigDecimal):BigDecimal? {
        if (values.size > 0) {
            var max = values[0]
            for (i in 1..values.size - 1) {
                if (values[i] > max) {
                    max = values[i]
                }
            }
            return max
        } else {
            return null
        }
    }

    /**
     * Find the maximum value within BigDecimal list
     *
     * @param values source BigDecimal list
     *
     * @return maximum BigDecimal value
     */
    @JvmStatic
    fun bigDecimalMax(values:List<BigDecimal>?):BigDecimal? {
        if (values == null || values.size == 0) {
            return null
        } else {
            var max = values[0]
            for (i in 1..values.size - 1) {
                val v = values[i]
                if (v > max) {
                    max = v
                }
            }
            return max
        }
    }

    /**
     * Find the minimum value within integer values
     *
     * @param values source integer values
     *
     * @return minimum integer value
     */
    @JvmStatic
    fun integerMin(vararg values:Int):Int {
        var min = Integer.MAX_VALUE
        for (v in values) {
            if (min > v) {
                min = v
            }
        }
        return min
    }

    /**
     * Find the minimum value within integer value list
     *
     * @param values source integer value list
     *
     * @return minimum integer value
     */
    @JvmStatic
    fun integerMin(values:List<Int>?):Int {
        if (values == null || values.size == 0) {
            throw IllegalArgumentException("Values is null or no value contained!")
        } else {
            var min = values[0]
            for (i in 1..values.size - 1) {
                val v = values[i]
                if (v < min) {
                    min = v
                }
            }
            return min
        }
    }

    /**
     * Find the minimum value within number value list
     *
     * @param values source number value list
     *
     * @return minimum number values
     */
    @JvmStatic
    fun doubleMin(vararg values:Double):Double {
        var min = Integer.MAX_VALUE.toDouble()
        for (v in values) {
            if (java.lang.Double.isNaN(v)) {
                return v
            }
            if (min > v) {
                min = v
            }
        }
        return min
    }

    /**
     * Find the minimun value within number values
     *
     * @param values source number values
     *
     * @return minimun number values
     */
    @JvmStatic
    fun doubleMin(values:List<Double>?):Double {
        if (values == null || values.size == 0) {
            throw IllegalArgumentException("Values is null or no value contained.")
        } else {
            var min = values[0]
            for (i in 1..values.size - 1) {
                val v = values[i]
                if (v < min) {
                    min = v
                }
            }
            return min
        }
    }

    /**
     * Find the minimum value within BigDecimals
     *
     * @param values source BigDecimals
     *
     * @return minimum BigDecimal
     */
    @JvmStatic
    fun bigDecimalMin(vararg values:BigDecimal):BigDecimal? {
        if (values.size > 0) {
            var min = values[0]
            for (i in 1..values.size - 1) {
                if (values[i] < min) {
                    min = values[i]
                }
            }
            return min
        } else {
            return null
        }
    }

    /**
     * Find the minimun value within BigDecimals
     *
     * @param values source BigDecimals
     *
     * @return minimum BigDecimal
     */
    @JvmStatic
    fun bigDecimalMin(values:List<BigDecimal>?):BigDecimal? {
        if (values != null && values.size > 0) {
            var min = values[0]
            for (i in 1..values.size - 1) {
                val v = values[i]
                if (v < min) {
                    min = v
                }
            }
            return min
        } else {
            return null
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
    @JvmStatic
    fun calPercentageChange(originalValue:Double, diffValue:Double):Double {
        if (originalValue == 0.0) {
            return if (diffValue > 0) java.lang.Double.POSITIVE_INFINITY else java.lang.Double.NEGATIVE_INFINITY
        } else {
            return 1 / (1 + diffValue / originalValue)
        }
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
    @JvmStatic
    @JvmOverloads
    fun calculateSum(values:DoubleArray, startIndex:Int, length:Int = values.size - startIndex):Double {
        var sum = 0.0
        for (i in startIndex..startIndex + length - 1) {
            sum += values[i]
        }
        return sum
    }

    /**
     * Calculate sum from number values
     *
     * @param values number values
     *
     * @return sum of number values
     */
    @JvmStatic
    fun calculateSum(vararg values:Double):Double {
        return values.sum()
    }

    /**
     * Calculate sum from a collection of number values
     *
     * @param values collection of number values
     *
     * @return sum of number values
     */
    @JvmStatic
    fun calculateSum(values:Collection<Number>):Double {
        return values.reduce {
            x, y -> x.toDouble() + y.toDouble()
        }.toDouble()
    }

    /**
     * Calculate sum from a collection of bigdecimal values
     *
     * @param values collection of bigdecimal values
     *
     * @return sum of bigdecimal values
     */
    @JvmStatic
    fun calculateBigDecimalSum(values:Collection<BigDecimal>):BigDecimal {
        return values.reduce{ p, v -> p + v }
    }


    /**
     * Calculate sum from an integer array with provided start index & length
     * @param values integer array
     * @param startIndex calculation starting index
     * @param length calculation length
     * @return sum of integer array
     */
    @JvmStatic
    @JvmOverloads
    fun calculateSum(values:IntArray, startIndex:Int = 0, length:Int = values.size-startIndex):Int {
        var sum = 0
        var i = startIndex
        while (i < values.size && i < startIndex + length) {
            sum += values[i]
            i++
        }
        return sum
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
    @JvmStatic
    @JvmOverloads
    fun calculateAverage(values:DoubleArray, startIndex:Int = 0, length:Int = values.size - startIndex):Double {
        var sum = 0.0
        for (i in startIndex..startIndex + length - 1) {
            sum += values[i]
        }
        return sum / length
    }

    /**
     * Calculate average from a collection of number values
     *
     * @param values collection of number values
     *
     * @return average of a collection of number values
     */
    @JvmStatic
    fun calculateAverage(values:Collection< Number>?):Double {
        if (values == null) {
            return 0.0
        }
        var total = 0.0
        for (value in values) {
            total += value.toDouble()
        }
        return total / values.size
    }

    /**
     * Calculate average from a collection of bigdecimal values
     *
     * @param values collection of bigdecimal values
     *
     * @return average of a collection of bigdecimal values
     */
    @JvmStatic
    fun calculateBigDecimalAverage(values:Collection<BigDecimal>?):BigDecimal {
        if (values == null) {
            return BigDecimal.ZERO
        } else {
            val total = calculateBigDecimalSum(values)
            return total.divide(BigDecimal(values.size), 4, RoundingMode.HALF_UP)
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
    @JvmStatic
    fun pow(v:Int, r:Int):BigDecimal {
        return pow(BigDecimal(v), BigDecimal(r))
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
    @JvmStatic
    fun modulo(a:Int, b:Int, c:Int):Int {
        var z = b
        var x:Long = 1
        var y = a.toLong()
        while (z > 0) {
            if (z and 1 == 1) {
                x = x * y % c
            }
            y = y * y % c
            z = z shr 1
        }
        return (x % c).toInt()
    }

    /**
     * Calculate the power
     *
     * @param v base
     * @param r exponent
     *
     * @return result of power
     */
    @JvmStatic
    fun pow(v:BigDecimal, r:BigDecimal):BigDecimal {
        val signum = r.signum()

        if (signum == 0) {
            return BigDecimal.ONE
        } else if (signum > 0) {
            val parts = r.divideAndRemainder(BigDecimal.ONE)

            if (parts[1] > BigDecimal.ZERO) {
                var result = v.pow(parts[0].toInt())
                var decimalPow = parts[1]
                var fraction = BigDecimal("0.5")
                var curr = v

                while (decimalPow > DEFAULT_BIG_TOLERANCE) {
                    curr = sqrt(curr)
                    if (decimalPow >= fraction) {
                        result *= curr
                        decimalPow -= fraction
                    }
                    fraction /= TWO
                }

                return result
            } else {
                return v.pow(parts[0].toInt())
            }
        } else {
            return BigDecimal(1).divide(pow(v, r.abs()), DEFAULT_MATH_CONTEXT)
        }
    }

    /**
     * Calculate the factorial of (v!)
     *
     * @param v value
     *
     * @return result of factorial
     */
    @JvmStatic
    fun factorial(v:BigDecimal):BigDecimal {
        // Ignore decimal part
        val vFlr = floor(v)
        if (vFlr.compareTo(BigDecimal.ZERO) == 0 || vFlr.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ONE
        } else {
            var result = vFlr
            var count = vFlr - BigDecimal.ONE
            while (count > BigDecimal.ONE) {
                result *= count
                count -= BigDecimal.ONE
            }
            return result
        }
    }

    /**
     * Calculate the factorial of (v!)
     *
     * @param v value
     *
     * @return result of factorial
     */
    @JvmStatic
    fun factorial(v:Int):BigDecimal {
        if (v == 0 || v == 1) {
            return BigDecimal.ONE
        } else {
            var result = BigDecimal(v)
            var count = v - 1
            while (count > 1) {
                result *= BigDecimal(count)
                count--
            }
            return result
        }
    }

    /**
     * Calculate the factorial of (v!/r!)
     *
     * @param v value
     * @param r end value
     *
     * @return result of factorial
     */
    @JvmStatic
    fun factorial(v:BigDecimal, r:BigDecimal):BigDecimal {
        // Ignore decimal part
        val vFlr = floor(v)

        if (r.compareTo(BigDecimal.ONE) == 0) {
            return factorial(vFlr)
        } else {
            var result = vFlr
            var count = vFlr - BigDecimal.ONE
            val endCount = vFlr - r
            while (count >= endCount && count > BigDecimal.ONE) {
                result *= count
                count -= BigDecimal.ONE
            }

            return result
        }
    }

    /**
     * Calculate the factorial of (v!/(v-r)!)
     *
     * @param v value
     * @param r length
     *
     * @return result of factorial
     */
    @JvmStatic
    fun factorial(v:Int, r:Int):BigDecimal {
        if (r == v) {
            return factorial(v)
        } else {
            var result = BigDecimal(v)
            var count = v - 1
            val endCount = v - r + 1
            while (count >= endCount && count > 1) {
                result *= BigDecimal(count)
                count--
            }
            return result
        }
    }

    /**
     * Calculate the number of combination of (v!/r!(v-r)!)
     *
     * @param v value
     * @param r length
     *
     * @return number of combination
     */
    @JvmStatic
    fun combination(v:BigDecimal, r:BigDecimal):BigDecimal {
        return factorial(v, r).divide(factorial(r))
    }

    /**
     * Calculate the number of combination of (v!/r!(v-r)!)
     *
     * @param v value
     * @param r length
     * @return number of combination
     */
    @JvmStatic
    fun combination(v:Int, r:Int):BigDecimal {
        return factorial(v, r).divide(factorial(r))
    }

    /**
     * Permutation of a list of values with provided length
     *
     * @param  Generic type
     * @param list list of values
     * @param len target length
     *
     * @return results of permutation
     */
    @JvmStatic
    fun <E> permutation(list:List<E>, len:Int):List<List<E>>? {
        if (list.size < len || len <= 0) {
            return null
        } else {
            val r = ArrayList<List<E>>(factorial(list.size, len).toInt())

            permutate(r, list, ArrayList<Int>(), DataManipulator.createSequence(0, list.size - 1), len)

            return r
        }
    }

    /**
     * Recursive method to permutate list of values
     * @param  Generic type
     * @param lists permutation result list
     * @param list list of values
     * @param setIndexes setted indexes
     * @param availableIndexes available indexes
     * @param len target length
     */
    private fun <E> permutate(
        lists:MutableList<List<E>>, list:List<E>, setIndexes:List<Int>,
        availableIndexes:List<Int>, len:Int
    ) {
        if (setIndexes.size == len) {
            lists.add(DataManipulator.createList(list, setIndexes))
        } else {
            for (i in availableIndexes.indices) {
                val aIndexes = ArrayList(availableIndexes)
                val sIndexes = ArrayList(setIndexes)
                sIndexes.add(aIndexes.removeAt(i))
                permutate(lists, list, sIndexes, aIndexes, len)
            }
        }
    }

    /**
     * Recursive method combinate list of values
     *
     * @param  Generic type
     * @param lists combination result list
     * @param list list of values
     * @param setIndexes setted indexes
     * @param len target length
     */
    private fun <E> combinate(
        lists:MutableList<List<E>>, list:List<E>, setIndexes:List<Int>, len:Int
    ) {
        if (setIndexes.size == len) {
            lists.add(DataManipulator.createList(list, setIndexes))
        } else {
            val remain = len - setIndexes.size
            var i = if (setIndexes.size == 0) 0 else setIndexes[setIndexes.size - 1].toInt() + 1
            while (i < list.size - remain + 1) {
                val sIndices = ArrayList(setIndexes)
                sIndices.add(i)

                combinate(lists, list, sIndices, len)
                i++
            }
        }
    }

    /**
     * Combine a list of values with provided length
     *
     * @param  Generic type
     * @param list list of values
     * @param len target length
     *
     * @return result of combination
     */
    @JvmStatic
    fun <E> combination(list:List<E>, len:Int):List<List<E>>? {
        if (list.size < len || len <= 0) {
            return null
        } else {
            val r = ArrayList<List<E>>(factorial(list.size, len).toInt())
            combinate(r, list, ArrayList<Int>(), len)

            return r
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
    @JvmStatic
    fun pow(base:BigInteger, exponent:BigInteger):BigInteger {
        var bse = base
        var exp = exponent
        var p = BigInteger.ONE
        while (exp > BigInteger("0")) {
            if (exp % BigInteger("2") == BigInteger.ONE) {
                p *= bse
            }
            bse *= bse
            exp /= BigInteger("2")
        }
        return p
    }

    /**
     * Check provided BigInteger value is a power of other integer or not
     *
     * @param n source integer
     *
     * @return is power of other integer or not
     */
    @JvmStatic
    fun isPow(n:BigInteger):Boolean {
        var upperBound = n
        var lowerBound = BigInteger.ONE
        var temp:BigInteger
        var i = BigInteger.ONE
        while (i < BigInteger(Integer.toString(n.bitLength()))) {
            while (upperBound - lowerBound > BigInteger.ONE) {
                temp = (upperBound + lowerBound) / BigInteger("2")
                if (pow(temp, i + BigInteger.ONE) == n) {
                    return true
                }
                if (pow(temp, i + BigInteger.ONE) > n) {
                    upperBound = temp
                }
                if (pow(temp, i + BigInteger.ONE) < n) {
                    lowerBound = temp
                }
            }
            i = i.add(BigInteger.ONE)
        }
        return false
    }

    /**
     * Compare two numbers
     *
     * @param n1 number 1
     * @param n2 number 2
     *
     * @return result of comparison, 0 > equals, negative > n1 is smaller than n2, positive > n1 is greater than n2
     */
    @JvmStatic
    fun compare(n1:Number, n2:Number):Int {
        return BigDecimal(n1.toString()).compareTo(BigDecimal(n2.toString()))
    }

    /**
     * Check if two integers' sign equals or not (positive / negative), 0 is considered positive
     *
     * @param n1 integer 1
     * @param n2 integer 2
     *
     * @return checking result
     */
    @JvmStatic
    fun isSignEquals(n1:Int, n2:Int):Boolean {
        return getSign(n1) == getSign(n2)
    }

    /**
     * Get the sign of an integer
     *
     * @param n target integer
     *
     * @return sign of integer -1 for negative, 1 for positive and 0
     */
    @JvmStatic
    fun getSign(n:Int):Int {
        return if (n < 0) -1 else 1
    }

    /**
     * Check if two numbers' sign equals or not (positive / negative), 0 is considered positive
     *
     * @param n1 integer 1
     * @param n2 integer 2
     *
     * @return checking result
     */
    @JvmStatic
    fun isSignEquals(n1:Double, n2:Double):Boolean {
        return getSign(n1) == getSign(n2)
    }

    /**
     * Get the sign of a number
     *
     * @param n target number
     *
     * @return sign of number -1 for negative, 1 for positive and 0
     */
    @JvmStatic
    fun getSign(n:Double):Int {
        return if (n < 0) -1 else 1
    }

    /**
     * Factorize an integer
     *
     * @param n integer to be factorized
     *
     * @return result of factorization
     */
    @JvmStatic
    fun factorize(n:Int):List<Int> {
        val factors = ArrayList<Int>()
        if (n == 1) {
            factors.add(1)
            return factors
        } else if (n == 0) {
            return factors
        }

        val primeList = PrimeUtil.getPrimeListWithin(Math.abs(n))
        var num = n
        for (aPrimeList in primeList) {
            val prime = aPrimeList!!
            while (num % prime == 0) {
                factors.add(prime)
                num /= prime
            }
        }

        return factors
    }

    /**
     * Get the length of an integer (decimal)
     *
     * @param value target value
     *
     * @return length of target integer value, -1 for negative value
     */
    @JvmStatic
    fun lengthOfInteger(value:Int):Int {
        if (value < 0) {
            return -1
        } else if (value == 0) {
            return 1
        } else {
            return Math.floor(Math.log10(value.toDouble())).toInt() + 1
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
    @JvmStatic
    fun lengthOfInteger(value:Int, base:Int):Int {
        if (value < 0) {
            return -1
        } else if (value == 0) {
            return 1
        } else {
            return floor(log(value.toDouble(), base.toDouble())).toInt() + 1
        }
    }

    // This method only find the smallest possible factor, if small prime numbers exhausted it just stop, not for general use
    @JvmStatic
    fun findSmallestFactor(number:Long):Int {
        if (number == 1L) {
            return -1
        } else {
            for (FIRST_PRIME in FIRST_PRIMES) {
                if (number % FIRST_PRIME == 0L) {
                    return FIRST_PRIME
                }
            }

            // small prime exhausted, return -1
            return -1
        }
    }

    /**
     * Find minimum value within variable values
     * 
     * @param values values of concern
     * @return minimum value
     */
    @JvmStatic
    fun min(vararg values: Double): Double {
        if (values.isEmpty()) {
            return Double.NaN
        } else {
            var min = values[0]
            
            for (i in 1 until values.size) {
                if (min > values[i]) {
                    min = values[i]
                }
            }
            
            return min
        }
    }

    /**
     * Find maximum value within variable values
     * 
     * @param values values of concern
     * @return maximum value
     */
    @JvmStatic
    fun max(vararg values: Double): Double {
        if (values.isEmpty()) {
            return Double.NaN
        } else {
            var max = values[0]
            
            for (i in 1 until values.size) {
                if (max < values[i]) {
                    max = values[i]
                }
            }
            
            return max
        }
    }

    /**
     * Check if point is between line points
     * 
     * @param point target point
     * @param linePoint1 line point 1
     * @param linePoint2 line point 2
     * @return check result
     */
    @JvmOverloads
    @JvmStatic
    fun isPointBetweenLine(point: Point2D, linePoint1: Point2D, linePoint2: Point2D, tolerance: Double = 0.000001): Boolean {
        if (isPointAlignedWithLine(point, linePoint1, linePoint2, tolerance)) {
            val dotProduct = (point.x - linePoint1.x) * (linePoint2.x - linePoint1.x) + (point.y - linePoint1.y)*(linePoint2.y - linePoint1.y)
            if (dotProduct < 0.0) {
                return false
            } else {
                val squareLength = (linePoint2.x - linePoint1.x)*(linePoint2.x - linePoint1.x) + (linePoint2.y - linePoint1.y)*(linePoint2.y - linePoint2.y)
                if (dotProduct > squareLength) {
                    return false
                } else {
                    return true
                }
            }
        } else {
            return false
        }
    }

    /**
     * Check if point aligned with line
     * 
     * @param point target point
     * @param linePoint1 line point 1
     * @param linePoint2 line point 2
     * @return check result
     */
    @JvmOverloads
    @JvmStatic
    fun isPointAlignedWithLine(point: Point2D, linePoint1: Point2D, linePoint2: Point2D, tolerance: Double = 0.000001): Boolean {
        val crossDeltaX = point.x - linePoint1.x
        val crossDeltaY = point.y - linePoint1.y
        
        val deltaX = linePoint2.x - linePoint1.x
        val deltaY = linePoint2.y - linePoint1.y
        
        val crossProduct = crossDeltaX * deltaY - crossDeltaY * deltaX

        return isFloatingPointNumberEquals(crossProduct, 0.0, tolerance)
    }

    /**
     * Find interception point of two lines (a & b)
     * 
     * @param line1Point1 point 1 of line 1
     * @param line1Point2 point 2 of line 1
     * @param line2Point1 point 1 of line 2
     * @param line2Point2 point 2 of line 2
     * @return interception point, null for no interception
     */
    @JvmStatic
    fun findInterception(line1Point1: Point2D, line1Point2: Point2D, line2Point1: Point2D, line2Point2: Point2D): Point2D? {
        val a1 = line1Point1.y - line1Point2.y
        val b1 = line1Point2.x - line1Point1.x
        val c1 = line1Point1.x * line1Point2.y - line1Point2.x * line1Point1.y

        val a2 = line2Point1.y - line2Point2.y
        val b2 = line2Point2.x - line2Point1.x
        val c2 = line2Point1.x * line2Point2.y - line2Point2.x * line2Point1.y
        
        if (b1 == 0.0 && b2 == 0.0) {
            if (-c1/a1 == -c2/a2) {
                // y can be any value, use middle y as result
                val minY = min(line1Point1.y, line1Point2.y, line2Point1.y, line2Point2.y)
                val maxY = max(line1Point1.y, line1Point2.y, line2Point1.y, line2Point2.y)
                
                return Point2D.Double(-c1/a1, (minY+maxY)/2.0)
            } else {
                // both lines are vertical and never intercept
                return null
            }
        } else if (b1 == 0.0) {
            // line 1 is vertical
            val x = -c1 / a1
            
            val y = (-a2*x - c2) / b2
            
            return Point2D.Double(x, y)
        } else if (b2 == 0.0) {
            // line 2 is vertical
            val x = -c2 / a2
            
            val y = (-a1*x - c1) / b1
            
            return Point2D.Double(x, y)
        } else if (a1 == 0.0 && a2 == 0.0) {
            if (-c1/b1 == -c2/b2) {
                // x can be any value, use middle x as result
                val minX = min(line1Point1.x, line1Point2.x, line2Point1.x, line2Point2.x)
                val maxX = max(line1Point1.x, line1Point2.x, line2Point1.x, line2Point2.x)
                
                return Point2D.Double((minX+maxX)/2.0, -c1/b1)
            } else {
                // both lines are horizontal and never intercept
                return null
            }
        } else if (a1 == 0.0) {
            // Line 1 is horizontal
            val y = -c1 / b1
            
            val x = (-b2*y-c2) / a2
            
            return Point2D.Double(x, y)
        } else if (a2 == 0.0) {
            // line 2 is horizontal
            val y = -c2 / b2
            
            val x = (-b1*y-c1) / a1
            
            return Point2D.Double(x, y)
        } else {
            val s = (a1*b2 - a2*b1)
            
            if (s == 0.0) {
                if (c1 / b1 == c2 / b2) {
                    // use middle x as interception point
                    val minX = min(line1Point1.x, line1Point2.x, line2Point1.x, line2Point2.x)
                    val maxX = max(line1Point1.x, line1Point2.x, line2Point1.x, line2Point2.x)
                    
                    val x = (minX + maxX) / 2.0
                    val y = (-a1*x - c1) / b1
                    
                    return Point2D.Double(x, y)
                } else {
                    // both line are parallel that will never intercept
                    return null
                }
            } else {
                val x = (b1 * c2 - b2 * c1) / s
                val y = (-a2 * x - c2) / b2
                return Point2D.Double(x, y)
            }
        }
    }

    /**
     * Find shortest distance between a point to a line
     * 
     * @param px x coordinate of point
     * @param py y coordinate of point
     * @param x1 x coordinate of line point 1
     * @param y1 y coordinate of line point 1
     * @param x2 x coordinate of line point 2
     * @param y2 y coordinate of line point 2
     * 
     * @return shortest distance
     */
    @JvmStatic
    fun distanceFromPointToLine(px: Double, py: Double, x1: Double, y1: Double, x2: Double, y2: Double): Double {
        val a = y1 - y2
        val b = x2 - x1
        val c = x1 * y2 - x2 * y1

        val closestLineX = (b * (b * px-a * py)-a * c)/(a * a + b * b)
        val closestLineY = (a * (-b * px + a * py) - b * c)/(a * a + b * b)
        
        return distanceOfPoints(closestLineX, closestLineY, px, py)
    }

    /**
     * Find shortest distance between a point to a line
     * 
     * @param point target point
     * @param linePoint1 line point 1
     * @param linePoint2 line point 2
     * 
     * @return shortest distance
     */
    @JvmStatic
    fun distanceFromPointToLine(point: Point, linePoint1: Point, linePoint2: Point): Double {
        return distanceFromPointToLine(point.x.toDouble(), point.y.toDouble(), linePoint1.x.toDouble(), linePoint1.y.toDouble(), linePoint2.x.toDouble(), linePoint2.y.toDouble())
    }

    /**
     * Find shortest distance between a point to a line (Point2D)
     *
     * @param point target point
     * @param linePoint1 line point 1
     * @param linePoint2 line point 2
     *
     * @return shortest distance
     */
    @JvmStatic
    fun distanceFromPointToLine(point: Point2D, point1: Point2D, point2: Point2D): Double {
        return distanceFromPointToLine(point.x, point.y, point1.x, point1.y, point2.x, point2.y)
    }

    /**
     * Find distance between two points
     * 
     * @param x1 x coordinate of point 1
     * @param y1 y coordinate of point 1
     * @param x2 x coordinate of point 2
     * @param y2 y coordinate of point 2
     * 
     * @return distaince of two points
     */
    @JvmStatic
    fun distanceOfPoints(x1:  Double, y1: Double, x2: Double, y2: Double): Double {
        return sqrt(square(x1-x2) + square(y1-y2))
    }

    /**
     * Find distance between two points
     * 
     * @param point1 point 1
     * @param point2 point 2
     * 
     * @return distance of two points
     */
    @JvmStatic
    fun distanceOfPoints(point1: Point, point2: Point): Double {
        return sqrt((square(point1.x - point2.x) + square(point1.y - point2.y)).toDouble())
    }

    /**
     * Find distance between two points (Point2D)
     * 
     * @param point1 point 1
     * @param point2 point 2
     * 
     * @return distance of two points
     */
    @JvmStatic
    fun distanceOfPoints(point1: Point2D, point2: Point2D): Double {
        return sqrt(square(point1.x - point2.x) + square(point1.y - point2.y))
    }

    /**
     * Calculate Euler number to specified decimal places
     * 
     * @param decimalPlaces target decimal places
     * 
     * @return Euler number to target decimal places
     */
    @JvmStatic
    fun calculateEulerNumberToDecimalPlaces(decimalPlaces: Int): BigDecimal {
        val mathContext = MathContext(decimalPlaces+1, RoundingMode.HALF_UP)
        
        var currentValue = BigDecimal("2.5")
        var v = BigDecimal("0.5")
        var currentFactorialBase = TWO
        
        while(true) {
            currentFactorialBase++

            // Extend decimal places by 2 to prevent lost of precision
            v = v.divide(currentFactorialBase, decimalPlaces+3, RoundingMode.FLOOR)
            
            if (v.compareTo(BigDecimal.ZERO) > 0) {
                currentValue += v
            } else {
                break
            }
        }
        
        return currentValue.round(mathContext)
    }

    /**
     * Solution of quadratic equation, result are in positive & negative form
     */
    class QuadraticSolution(
            /**
             * Positive solution
             */
            val positiveValue:Double,
            /**
             * Negative solution
             */
            val negativeValue:Double
    ) {
        constructor(value:Double) : this(value, value)

        /**
         * Get a representation string of this Quadratic solution
         */
        override fun toString():String {
            if (positiveValue == negativeValue) {
                return "value: $positiveValue"
            } else {
                return "positiveValue: $positiveValue, negativeValue: $negativeValue"
            }
        }
    }
}