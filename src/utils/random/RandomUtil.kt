package utils.random

import utils.data.SortedListAvl
import utils.math.MathUtil
import java.math.BigDecimal
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-11-15
 * Time: 16:24
 */
object RandomUtil {
    // Use of secure random, default false
    private var USE_SECURE_RANDOM = false

    /**
     * Random number generator, lazy initialization
     */
    private var randomGenerator: Random? = null

    /**
     * Initialize random generator
     */
    private fun initRandom() {
        if (randomGenerator == null) {
            if (USE_SECURE_RANDOM) {
                try {
                    randomGenerator = SecureRandom.getInstance("NativePRNGBlocking")
                } catch (e: NoSuchAlgorithmException) {
                    randomGenerator = SecureRandom()
                }
            } else {
                randomGenerator = Random()
            }
        }
    }

    /**
     * Set usage of secure random
     * - It is fine to use Random, for its non-blocking nature
     * - Use secure random, if you really want TRULY RANDOM generation and it is provided at OS level, and it is also a computational random after all.

     * @param useSecureRandom secure random flag
     */
    @JvmStatic
    fun setUseSecureRandom(useSecureRandom:Boolean) {
        if (USE_SECURE_RANDOM != useSecureRandom) {
            USE_SECURE_RANDOM = useSecureRandom

            // re-initialize randomGenerator
            randomGenerator = null
            initRandom()
        }
    }

    /**
     * Create a random generator with given seed

     * @param seed given seed
     *
     * @return Random generate with given random seed.
     */
    @JvmStatic
    fun createRandomGenerator(seed:Long):Random {
        return Random(seed)
    }

    /**
     * Generate a random number
     *
     * @return random number (double) ranging from 0.0 (inclusive) to 0.1 (exclusive)
     */
    private fun random():Double {
        if (randomGenerator == null) {
            initRandom()
        }
        return randomGenerator!!.nextDouble()
    }

    /**
     * Calculate final result from random number and provided min max range
     * If range overflow use long for calculation
     *
     * @param rand double random number, ranging from 0.0 and 1.0, generated from random generator
     * @param min lower bound value
     * @param max upper bound value
     *
     * @return formalized result
     */
    private fun calculateResult(rand:Double, min:Int, max:Int):Int {
        val range = max - min + 1

        if (range <= 0) {
            // overflow, use long for calculation
            val longMax = max.toLong()
            val longMin = min.toLong()

            return (rand * (longMax - longMin + 1) + longMin).toInt()
        } else {
            return (rand * (max - min + 1) + min).toInt()
        }
    }

    /**
     * Calculate final result from random number and provided min max range
     * If range overflow use BigDecimal for calculation.
     * @param rand random double random number, ranging from 0.0 and 1.0, generated from random generator
     * @param min lower bound value
     * @param max upper bound value
     *
     * @return formalized result
     */
    private fun calculateResult(rand:Double, min:Long, max:Long):Long {
        val range = max - min + 1

        if (range <= 0) {
            // overflow, use BigDecimal for calculation
            val bigMax = BigDecimal(max.toString())
            val bigMin = BigDecimal(min.toString())
            val bigRand = BigDecimal(rand.toString())

            return ((bigMax - bigMin + BigDecimal.ONE) * bigRand + bigMin).toLong()
        } else {
            return (rand * (max - min + 1) + min).toLong()
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
    @JvmStatic
    fun generateRandomIntegerArray(min:Int, max:Int, size:Int, distinct:Boolean):IntArray {
        if (size <= 0) {
            return IntArray(0)
        } else {
            val tMin = Math.min(min, max)
            val tMax = Math.max(min, max)
            val possibleIntegers = max - min + 1
            val list = SortedListAvl<Int>()
            for (i in 0..size - 1) {
                if (distinct && i >= possibleIntegers) {
                    break
                }
                var v = randomInteger(tMin, tMax)
                while (i > 0 && distinct && list.contains(v)) {
                    v = randomInteger(tMin, tMax)
                }
                list.add(v)
            }
            val arr = list.toTypedArray()
            val result = IntArray(size)
            for (i in arr.indices) {
                result[i] = arr[i] as Int
            }
            return result
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
    @JvmStatic
    fun generateRandomIntegerArray(min:Int, max:Int, size:Int, distinct:Boolean, randomGenerator:Random):IntArray {
        if (size <= 0) {
            return IntArray(0)
        } else {
            val tMin = Math.min(min, max)
            val tMax = Math.max(min, max)
            val possibleIntegers = max - min + 1
            val list = SortedListAvl<Int>()
            for (i in 0..size - 1) {
                if (distinct && i >= possibleIntegers) {
                    break
                }
                var v = randomInteger(tMin, tMax, randomGenerator)

                while (i > 0 && distinct && list.contains(v)) {
                    v = randomInteger(tMin, tMax, randomGenerator)
                }
                list.add(v)
            }
            val arr = list.toTypedArray()
            val result = IntArray(size)
            for (i in arr.indices) {
                result[i] = arr[i] as Int
            }
            return result
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
    @JvmStatic
    fun generateRandomNumberArray(
        min:Double, max:Double, precision:Int, size:Int, distinct:Boolean
    ):DoubleArray {
        if (size <= 0) {
            return DoubleArray(0)
        } else {
            val tMin = Math.min(min, max)
            val tMax = Math.max(min, max)
            val list = SortedListAvl<Double>()
            for (i in 0..size - 1) {
                var v = randomNumber(tMin, tMax, precision)
                while (i > 0 && distinct && list.contains(v)) {
                    v = randomNumber(tMin, tMax, precision)
                }
                list.add(v)
            }
            val arr = list.toTypedArray()
            val result = DoubleArray(size)
            for (i in arr.indices) {
                result[i] = arr[i] as Double
            }
            return result
        }
    }

    /**
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
        if (size <= 0) {
            return DoubleArray(0)
        } else {
            val tMin = Math.min(min, max)
            val tMax = Math.max(min, max)
            val list = SortedListAvl<Double>()
            for (i in 0..size - 1) {
                var v = randomNumber(tMin, tMax, precision, randomGenerator)
                while (i > 0 && distinct && list.contains(v)) {
                    v = randomNumber(tMin, tMax, precision, randomGenerator)
                }
                list.add(v)
            }
            val arr = list.toTypedArray()
            val result = DoubleArray(size)
            for (i in arr.indices) {
                result[i] = arr[i] as Double
            }
            return result
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
    @JvmStatic
    fun randomInteger(min:Int, max:Int):Int {
        return if (min == max) min else calculateResult(random(), min, max)
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
    @JvmStatic
    fun randomInteger(min:Int, max:Int, randomGenerator:Random):Int {
        return if (min == max) min else calculateResult(randomGenerator.nextDouble(), min, max)
    }

    /**
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
        initRandom()
        return randomIntegerWithNormalDistribution(min, max, mean, deviation, randomGenerator!!)
    }

    /**
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
        var v: Int;

        // Since there is no theoretical minimum or maximum value for gaussian, regeneration is required if the value fall outside of range
        do {
            v = Math.round(randomGenerator.nextGaussian() * deviation + mean).toInt()
        } while (v < min || v > max);

        return v
    }

    /**
     * Generate a random value from weighted random value holder, see {@link utils.MathUtil.WeightedRandomValueHolder}
     *
     * @param weightedRandomValueHolder weighted random value holder
     *
     * @return generated random value
     */
    @JvmStatic
    fun <E> randomWeightedValue(weightedRandomValueHolder: WeightedRandomValueHolder<E>): E {
        return weightedRandomValueHolder.determineValue(randomInteger(1, weightedRandomValueHolder.totalWeight))
    }

    /**
     * Generate a random long integer
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random long integer
     */
    @JvmStatic
    fun randomLong(min:Long, max:Long):Long {
        return if (min == max) min else calculateResult(random(), min, max)
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
    @JvmStatic
    fun randomLong(min:Long, max:Long, randomGenerator:Random):Long {
        return if (min == max) min else calculateResult(randomGenerator.nextDouble(), min, max)
    }

    /**
     * Generate a random number
     *
     * @param min minimum bound
     * @param max maximum bound
     *
     * @return random number
     */
    @JvmStatic
    fun randomNumber(min:Double, max:Double):Double {
        return if (min == max) min else randomNumber(min, max, 6)
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
    @JvmStatic
    fun randomNumber(min:Double, max:Double, randomGenerator:Random):Double {
        return if (min == max) min else randomNumber(min, max, 6, randomGenerator)
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
    @JvmStatic
    fun randomNumber(min:Double, max:Double, precision:Int):Double {
        return MathUtil.roundUp(random() * (max - min) + min, precision)
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
    @JvmStatic
    fun randomNumber(min:Double, max:Double, precision:Int, randomGenerator:Random):Double {
        return MathUtil.roundUp(randomGenerator.nextDouble() * (max - min) + min, precision)
    }

    /**
     * Pick a random value from possibles
     *
     * @param possibles possible value array
     * @return random value picked
     */
    fun <E> randomValue(possibles: Array<E>): E {
        initRandom()
        return randomValue(possibles, randomGenerator!!)
    }

    /**
     * Pick a random value from possibles
     *
     * @param possibles possible value list
     *
     * @return random value picked
     */
    fun <E> randomValue(possibles: List<E>): E {
        initRandom()
        return randomValue(possibles, randomGenerator!!)
    }

    /**
     * Pick a random value from possibles
     *
     * @param possibles possible value array
     * @param randomGenerator random generator
     *
     * @return random value picked
     */
    fun <E> randomValue(possibles: Array<E>, randomGenerator: Random): E {
        if (possibles.isEmpty()) {
            throw IllegalArgumentException("Empty possible value array!")
        } else {
            return possibles[randomInteger(0, possibles.size-1)]
        }
    }

    /**
     * Pick a random value from possibles
     *
     * @param possibles possible value list
     * @param randomGenerator random generator
     *
     * @return random value picked
     */
    fun <E> randomValue(possibles: List<E>, randomGenerator: Random): E {
        if (possibles.isEmpty()) {
            throw IllegalArgumentException("Empty possible value list!")
        } else {
            return possibles[randomInteger(0, possibles.size-1)]
        }
    }
}