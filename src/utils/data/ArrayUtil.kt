package utils.data

import java.util.*

import utils.math.MathUtil

object ArrayUtil {
    /**
     * Calculate average of a double precision array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    @JvmStatic
    fun arrayAvg(arr: DoubleArray): Double {
        return arraySum(arr)/arr.size.toDouble()
    }
    
    /**
     * Calculate average of a double array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    @JvmStatic
    fun arrayAvg(arr: DoubleArray, start: Int, length: Int): Double {
        if (start >= 0 && start < arr.size) {
            var c = length
            if (start + length - 1 >= arr.size) {
                c = arr.size - start
            }
            return arraySum(arr, start, length)/c.toDouble()
        } else {
            return 0.0
        }
    }
    
    /**
     * Calculate average of a single precision (float) array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    @JvmStatic
    fun arrayAvg(arr: FloatArray): Double {
        return arraySum(arr) / arr.size.toDouble()
    }
    
    /**
     * Calculate average of a single precision (float) array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    @JvmStatic
    fun arrayAvg(arr: FloatArray, start: Int, length: Int): Double {
        if (start >= 0 && start < arr.size) {
            var c = length
            if (start + length - 1 >= arr.size) {
                c = arr.size - start
            }
            return arraySum(arr, start, length)/c.toDouble()
        } else {
            return 0.0
        }
    }
    
    /**
     * Calculate average of an integer array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    @JvmStatic
    fun arrayAvg(arr: IntArray): Double {
        return arraySum(arr) / arr.size.toDouble()
    }
    
    /**
     * Calculate average of an integer array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    @JvmStatic
    fun arrayAvg(arr: IntArray, start: Int, length: Int): Double {
        if (start >= 0 && start < arr.size) {
            var c = length
            if (start + length - 1 >= arr.size) {
                c = arr.size - start
            }
            return arraySum(arr, start, length)/c.toDouble()
        } else {
            return 0.0
        }
    }
    
    /**
     * Calculate average of a long integer array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    @JvmStatic
    fun arrayAvg(arr: LongArray): Double {
        return arraySum(arr) / arr.size.toDouble()
    }
    
    /**
     * Calculate average of a long integer array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    @JvmStatic
    fun arrayAvg(arr: LongArray, start: Int, length: Int): Double {
        if (start >= 0 && start < arr.size) {
            var c = length
            if (start + length - 1 >= arr.size) {
                c = arr.size - start
            }
            return arraySum(arr, start, length)/c.toDouble()
        } else {
            return 0.0
        }
    }
    
    /**
     * Calculate average of a short integer array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    @JvmStatic
    fun arrayAvg(arr: ShortArray): Double {
        return arraySum(arr) / arr.size.toDouble()
    }
    
    /**
     * Calculate average of a short integer array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    @JvmStatic
    fun arrayAvg(arr: ShortArray, start: Int, length: Int): Double {
        if (start >= 0 && start < arr.size) {
            var c = length
            if (start + length - 1 >= arr.size) {
                c = arr.size - start
            }
            return arraySum(arr, start, length)/c.toDouble()
        } else {
            return 0.0
        }
    }
    
    /**
     * Calculate total sum of a double precision array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    @JvmStatic
    fun arraySum(arr: DoubleArray): Double {
        var sum = 0.0
        for (v in arr) {
            sum += v
        }
        return sum
    }
    
    /**
     * Calculate total sum of a double precision array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * 
     * @return sum of the target array
     */
    @JvmStatic
    fun arraySum(arr: DoubleArray, start: Int, length: Int): Double {
        var sum = 0.0
        if (start >= 0 && start < arr.size) {
            val endIndex = start + length
            for (i in start .. Math.min(endIndex, arr.size) - 1) {
                sum += arr[i]
            }
        }
        return sum
    }
    
    /**
     * Calculate total sum of a single precision array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    @JvmStatic
    fun arraySum(arr: FloatArray): Float {
        var sum = 0.0F
        for (v in arr) {
            sum += v
        }
        return sum
    }
    
    /**
     * Calculate total sum of a single precision array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * 
     * @return sum of the target array
     */
    @JvmStatic
    fun arraySum(arr: FloatArray, start: Int, length: Int): Float {
        var sum = 0.0F
        if (start >= 0 && start < arr.size) {
            val endIndex = start + length
            for (i in start .. Math.min(endIndex, arr.size) - 1) {
                sum += arr[i]
            }
        }
        return sum
    }
    
    /**
     * Calculate total sum of an integer array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    @JvmStatic
    fun arraySum(arr: IntArray): Int {
        var sum = 0
        for (v in arr) {
            sum += v
        }
        return sum
    }
    
    /**
     * Calculate total sum of an integer array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * @return sum of the target elements
     */
    @JvmStatic
    fun arraySum(arr: IntArray, start: Int, length: Int): Int {
        var sum = 0
        if (start >= 0 && start < arr.size) {
            val endIndex = start + length
            for (i in start .. Math.min(endIndex, arr.size) - 1) {
                sum += arr[i]
            }
        }
        return sum
    }
    
    /**
     * Calculate total sum of a long integer array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    @JvmStatic
    fun arraySum(arr: LongArray): Long {
        var sum = 0L
        for (v in arr) {
            sum += v
        }
        return sum
    }
    
    /**
     * Calculate total sum of a long integer array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * @return sum of the target elements
     */
    @JvmStatic
    fun arraySum(arr: LongArray, start: Int, length: Int): Long {
        var sum = 0L
        if (start >= 0 && start < arr.size) {
            val endIndex = start + length
            for (i in start .. Math.min(endIndex, arr.size) - 1) {
                sum += arr[i]
            }
        }
        return sum
    }
    
    /**
     * Calculate total sum of a short integer array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    @JvmStatic
    fun arraySum(arr: ShortArray): Int {
        var sum = 0
        for (v in arr) {
            sum += v
        }
        return sum
    }
    
    /**
     * Calculate total sum of a short integer array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * @return sum of the target elements
     */
    @JvmStatic
    fun arraySum(arr: ShortArray, start: Int, length: Int): Int {
        var sum = 0
        if (start >= 0 && start < arr.size) {
            val endIndex = start + length
            for (i in start .. Math.min(endIndex, arr.size)) {
                sum += arr[i]
            }
        }
        return sum
    }
    
    /**
     * Create a boolean array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of the array
     * @return boolean array with specified size and initial value  
     */
    @JvmOverloads
    @JvmStatic
    fun createBooleanArray(size: Int, initialValue: Boolean = false): BooleanArray {
        return BooleanArray(size, {initialValue})
    }
    
    /**
     * Create a byte array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return byte array with specified size and initial value
     */
    @JvmOverloads 
    @JvmStatic 
    fun createByteArray(size: Int, initialValue: Byte = 0.toByte()): ByteArray {
        return ByteArray(size, {initialValue})
    }
    
    /**
     * Create a character array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return character array with specified size and initial value
     */
    @JvmOverloads
    @JvmStatic
    fun createCharacterArray(size: Int, initialValue: Char = ' '): CharArray {
        return CharArray(size, {initialValue})
    }
    
    /**
     * Create a double precision array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return double precision array with specified size and initial value
     */
    @JvmOverloads
    @JvmStatic
    fun createDoubleArray(size: Int, initialValue: Double = 0.0): DoubleArray {
        return DoubleArray(size, {initialValue})
    }
    
    /**
     * Create a single precision array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return single precision array with specified size and initial value
     */
    @JvmOverloads
    @JvmStatic
    fun createFloatArray(size: Int, initialValue: Float = 0.0F): FloatArray {
        return FloatArray(size, {initialValue})
    }
    
    /**
     * Create an integer array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return integer array with specified size and initial value
     */
    @JvmOverloads
    @JvmStatic
    fun createIntegerArray(size: Int, initialValue: Int = 0): IntArray {
        return IntArray(size, {initialValue})
    }
    
    /**
     * Create a long integer array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return long integer array with specified size and initial value
     */
    @JvmOverloads
    @JvmStatic
    fun createLongArray(size: Int, initialValue: Long = 0L): LongArray {
        return LongArray(size, {initialValue})
    }
    
    /**
     * Create a short integer array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return short integer array with specified size and initial value
     */
    @JvmOverloads
    @JvmStatic
    fun createShortArray(size: Int, initialValue: Short = 0.toShort()): ShortArray {
        return ShortArray(size, {initialValue})
    }
    
    /**
     * Shuffle(randomize) the integer array
     * 
     * @param arr integer array
     */
    @JvmStatic
    fun shuffle(arr: IntArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the short array
     * 
     * @param arr short array
     */
    @JvmStatic
    fun shuffle(arr: ShortArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the byte array
     * 
     * @param arr byte array
     */
    @JvmStatic
    fun shuffle(arr : ByteArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the boolean array
     * 
     * @param arr boolean array
     */
    @JvmStatic
    fun shuffle(arr: BooleanArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the long array
     * 
     * @param arr long array
     */
    @JvmStatic
    fun shuffle(arr: LongArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the double array
     * 
     * @param arr double array
     */
    @JvmStatic
    fun shuffle(arr: DoubleArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the float array
     * 
     * @param arr float array
     */
    @JvmStatic
    fun shuffle(arr: FloatArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the char array
     * 
     * @param arr char array
     */
    @JvmStatic
    fun shuffle(arr: CharArray) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Shuffle(randomize) the array
     * 
     * @param <E> Generic type
     * @param arr target array
     */
    @JvmStatic
    fun <E> shuffle(arr: Array<E>) {
        for (i in arr.indices) {
            val r = MathUtil.randomInteger(0, arr.size-1)
            if (r != i) {
                val t = arr[i]
                arr[i] = arr[r]
                arr[r] = t
            }
        }
    }
    
    /**
     * Fill the boolean array with specified boolean value
     * 
     * @param arr target boolean array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: BooleanArray, v: Boolean) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the byte array with specified byte value
     * 
     * @param arr target byte array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: ByteArray, v: Byte) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the char array with specified char value
     * 
     * @param arr target char array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: CharArray, v: Char) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the short array with specified short value
     * 
     * @param arr target short array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: ShortArray, v: Short) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the integer array with specified integer value
     * 
     * @param arr target integer array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: IntArray, v: Int) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the long array with specified long value
     * 
     * @param arr target long array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: LongArray, v: Long) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the float array with specified float value
     * 
     * @param arr target float array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: FloatArray, v: Float) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the double array with specified double value
     * 
     * @param arr target double array
     * @param v value
     */
    @JvmStatic
    fun fill(arr: DoubleArray, v: Double) {
        Arrays.fill(arr, v)
    }
    
    /**
     * Fill the generic array with specified value
     * 
     * @param <E> Generic type
     * 
     * @param arr target array
     * @param v value
     */
    @JvmStatic
    fun <E> fill(arr: Array<E>, v: E) {
        for (i in arr.indices) {
            arr[i] = v
        }
    }
    
    /**
     * Fill the boolean arrays with specified boolean value
     * 
     * @param v value
     * @param arrs target boolean arrays
     */
    @JvmStatic
    fun fill(v: Boolean, vararg arrs: BooleanArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the byte arrays with specified byte value
     * 
     * @param v value
     * @param arrs target byte arrays
     */
    @JvmStatic
    fun fill(v: Byte, vararg arrs: ByteArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the char arrays with specified char value
     * 
     * @param v value
     * @param arrs target char arrays
     */
    @JvmStatic
    fun fill(v: Char, vararg arrs: CharArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the int arrays with specified int value
     * 
     * @param v value
     * @param arrs target int arrays
     */
    @JvmStatic
    fun fill(v: Int, vararg arrs: IntArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the short arrays with specified short value
     * 
     * @param v value
     * @param arrs target short arrays
     */
    @JvmStatic
    fun fill(v: Short, vararg arrs: ShortArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the long arrays with specified long value
     * 
     * @param v value
     * @param arrs target long arrays
     */
    @JvmStatic
    fun fill(v: Long, vararg arrs: LongArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the float arrays with specified float value
     * 
     * @param v value
     * @param arrs target float arrays
     */
    @JvmStatic
    fun fill(v: Float, vararg arrs: FloatArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the double arrays with specified double value
     * 
     * @param v value
     * @param arrs target double arrays
     */
    @JvmStatic
    fun fill(v: Double, vararg arrs: DoubleArray) {
        for (arr in arrs) {
            Arrays.fill(arr, v)
        }
    }
    
    /**
     * Fill the arrays with specified value
     * 
     * @param <E> Generic type
     * @param v value
     * @param arrs arrays to be filled
     */
    @JvmStatic
    fun <E> fill(v: E, vararg arrs: Array<E>) {
        for (arr in arrs) {
            for (i in arr.indices) {
                arr[i] = v
            }
        }
    }
    
    @JvmStatic
    fun <E> reverse(arr: Array<E>) {
        for (i in 0 .. (arr.size / 2) - 1) {
            val j = arr.size - i - 1
            val t = arr[i]
            arr[i] = arr[j]
            arr[j] = t
        }
    }
}
