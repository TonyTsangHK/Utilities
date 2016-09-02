package utils.math

import java.math.BigInteger
import java.util.Random

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-07-11
 * Time: 18:06
 */
class SequenceKeyGenerator
/**
 * Initialize generator with provided characters, key length, random generator seed and initial sequence number
 * Sequence number is in BigInteger format for more available space, 36 ^ 6 > Integer.MAX_VALUE, 62 ^ 11 > Long.MAX_VALUE

 * @param availableChars available characters
 * *
 * @param length key length
 * *
 * @param seq initial value of sequence number
 * *
 * @param seed random generator seed
 */
(private val availableChars: CharArray, length: Int, seq: BigInteger, private val seed: Long) {

    // Character mapper for each character place, this will make the result key looks like random.
    private lateinit var chars: Array<CharArray>

    // shift sequence number partition (partition size is total / length) for obfuscation.
    private lateinit var shifts: IntArray

    /**
     * total: total available number, availableChars.length * length
     * seq: sequence number, seq >= 0 <= total-1
     * length: result length, BigInteger is not necessary, just for easy handling
     * base: base = availableChars.length
     */
    private val total: BigInteger
    private var seq: BigInteger
    private val length: BigInteger
    private val base: BigInteger

    /**
     * Initialize generator with provided characters and key length

     * @param availableChars available characters
     * *
     * @param length result length
     */
    constructor(availableChars: String, length: Int): this(availableChars.toCharArray(), length, Random().nextLong())

    /**
     * Initialize generator with provided characters, key length and random generator seed

     * @param availableChars available characters
     * *
     * @param length key length
     * *
     * @param seed random generator seed
     */
    constructor(availableChars: String, length: Int, seed: Long) : this(availableChars.toCharArray(), length, seed)

    /**
     * Initialize generator with provided characters, result length and random generator seed

     * @param availableChars available characters
     * *
     * @param length key length
     * *
     * @param seed random generator seed
     */
    constructor(availableChars: CharArray, length: Int, seed: Long = Random().nextLong()) : this(availableChars, length, BigInteger.ZERO, seed)

    init {
        verifyCharacters(availableChars)
        this.base = BigInteger(this.availableChars.size.toString())
        this.length = BigInteger(length.toString())

        initialize()

        this.total = MathUtil.pow(this.base, this.length)
        this.seq = seq

        // Check for overflow sequence value, if overflow reset to zero
        if (this.seq.compareTo(total) >= 0) {
            this.seq = BigInteger.ZERO
        }
    }

    // Check for duplicated character, if exists throw IllegalArgumentException
    private fun verifyCharacters(availableChars: CharArray) {
        for (i in 0..availableChars.size - 1 - 1) {
            for (j in i + 1..availableChars.size - 1) {
                if (availableChars[i] == availableChars[j]) {
                    throw IllegalArgumentException("Duplicate character detected: " + availableChars[i])
                }
            }
        }
    }

    /**
     * Initialize random parameters, chars & shifts, for obfuscation
     * Same seed will always generate the same result, that means same encoded key for each sequence number.
     */
    private fun initialize() {
        val len = length.toInt()

        val random = Random(seed)

        this.chars = Array(len, { CharArray(availableChars.size) })

        for (aChar in chars) {
            System.arraycopy(availableChars, 0, aChar, 0, aChar.size)
            shuffle(aChar, random)
        }

        this.shifts = IntArray(base.toInt(), { MathUtil.randomInteger(1, base.toInt(), random) })
    }

    /**
     * Shuffle the character mapping array for obfuscation

     * @param chars character array to shuffle
     * *
     * @param random random generator
     */
    private fun shuffle(chars: CharArray, random: Random) {
        for (i in chars.indices) {
            val idx = MathUtil.randomInteger(0, chars.size - 1, random)

            if (i != idx) {
                val c = chars[i]
                chars[i] = chars[idx]
                chars[idx] = c
            }
        }
    }

    /**
     * Generate key and increment the sequence number

     * @return generated key
     */
    fun nextKey(): String {
        return generateKey(incrementSeq())
    }

    /**
     * Generate key with the current sequence number
     *
     * @return generated key
     */
    fun currKey(): String {
        return generateKey(seq)
    }

    /**
     * Use the current key as the string representation of this generator
     */
    override fun toString(): String {
        return currKey()
    }

    /**
     * Skip next sequence number, simply increment the sequence number
     */
    fun skipNext() {
        incrementSeq()
    }

    /**
     * Return the current sequence value then increment

     * @return current sequence number
     */
    private fun incrementSeq(): BigInteger {
        val oldSeq = seq

        seq += BigInteger.ONE

        normalizeSeq()

        return oldSeq
    }

    private fun decrementSeq():BigInteger {
        val oldSeq = seq

        seq -= BigInteger.ONE

        normalizeSeq()

        return oldSeq
    }

    /**
     * Remap sequence number into other number (one to one mapping) and return in integer array for character mapping.

     * @param num number to remap
     * *
     * @return base value array
     */
    private fun remapAndPrepare(num: BigInteger): IntArray {
        var v = num
        val vals = IntArray(length.toInt())

        for (i in vals.indices) {
            val bv = v % base

            vals[i] = bv.toInt()

            v = (v - bv) / base

            if (i > 0) {
                vals[i] = (vals[0] + vals[i] + i) % base.toInt()
            }
        }

        return vals
    }

    /**
     * Map base value array into key.

     * @param vals base value array
     * *
     * @return resulted key
     */
    private fun encodeKey(vals: IntArray): String {
        val result = CharArray(vals.size)

        for (i in vals.indices) {
            result[result.size - i - 1] = chars[i][vals[i]]
        }

        return String(result)
    }

    // Sequence number mapper to make the end result looks random
    private fun remap(num: BigInteger): BigInteger {
        return encodeNumber(remapAndPrepare(num))
    }

    /**
     * Encode the integer array into a new number

     * @param vals integer array
     * *
     * @return encoded number
     */
    private fun encodeNumber(vals: IntArray): BigInteger {
        var result = BigInteger.ZERO

        var i = BigInteger.ZERO
        while (i.compareTo(length) < 0) {
            result += BigInteger(vals[i.toInt()].toString()) * MathUtil.pow(base, i)
            i += BigInteger.ONE
        }

        return result
    }

    /**
     * Encode the key number into key.

     * @param keyNum key number
     * *
     * @return resulted key
     */
    private fun encodeKeyNum(keyNum: BigInteger): String {
        val len = length.toInt()

        val keyChars = CharArray(len)

        var i = BigInteger.ZERO
        while (i.compareTo(length) < 0) {
            val idx = ((keyNum / MathUtil.pow(base, i)) % base).toInt()

            keyChars[keyChars.size - i.toInt() - 1] = chars[i.toInt() % len][idx]
            i += BigInteger.ONE
        }

        return String(keyChars)
    }

    /**
     * Obfuscate by shifting the number into different partition and invert the order for every even number.

     * @param s number to obfuscate
     * *
     * @return obfuscated number
     */
    private fun obfuscate(s: BigInteger): BigInteger {
        val shift = shifts[(s % base).toInt()]

        val partitionSize = total / base

        val slot = (s / partitionSize).toInt()

        val newSlot = (slot + shift) % base.toInt()

        var idx = s % partitionSize

        // Add more obfuscation
        if (newSlot % 2 == 0) {
            idx = partitionSize - BigInteger.ONE - idx
        }

        return BigInteger(newSlot.toString()) * partitionSize + idx
    }

    /**
     * Generate key with the sequence number
     * @param s sequence number
     * *
     * @return resulted key
     */
    private fun generateKey(s: BigInteger): String {
        return encodeKey(remapAndPrepare(obfuscate(s)))
    }

    /**
     * Generate the key number, remapped & obfuscated sequence number
     * @param s sequence number
     * *
     * @return resulted remapped number
     */
    private fun generateKeyNum(s: BigInteger): BigInteger {
        return remap(obfuscate(s))
    }

    /**
     * Normalize sequence number if it is overflowed
     *
     */
    private fun normalizeSeq() {
        if (seq < BigInteger.ZERO) {
            seq = total - BigInteger.ONE
        } else if (seq > total) {
            seq = BigInteger.ZERO
        }
    }

    // Operator overload functions for ++, + & -
    operator fun inc(): SequenceKeyGenerator {
        incrementSeq()
        return this
    }

    operator fun dec(): SequenceKeyGenerator {
        decrementSeq()
        return this
    }

    operator fun plus(v: Int): SequenceKeyGenerator {
        seq += BigInteger(v.toString())

        normalizeSeq()

        return this
    }

    operator fun plus(v: BigInteger): SequenceKeyGenerator {
        seq += v

        normalizeSeq()

        return this
    }

    operator fun minus(v: Int): SequenceKeyGenerator {
        seq -= BigInteger(v.toString())

        normalizeSeq()

        return this
    }

    operator fun minus(v: BigInteger): SequenceKeyGenerator {
        seq -= v

        normalizeSeq()

        return this
    }
}