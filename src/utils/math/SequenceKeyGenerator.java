package utils.math;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-07-11
 * Time: 18:06
 */
public class SequenceKeyGenerator {
    private char[] availableChars;

    // Character mapper for each character place, this will make the result key looks like random.
    private char[][] chars;

    // shift sequence number partition (partition size is total / length) for obfuscation.
    private int[] shifts;

    private long seed;

    /**
     * total: total available number, availableChars.length * length
     * seq: sequence number, seq >= 0 <= total-1
     * length: result length, BigInteger is not necessary, just for easy handling
     * base: base = availableChars.length
     */
    private BigInteger total, seq, length, base;

    /**
     * Initialize generator with provided characters and key length
     *
     * @param availableChars available characters
     * @param length result length
     */
    public SequenceKeyGenerator(String availableChars, int length) {
        this(availableChars.toCharArray(), length, new Random().nextLong());
    }

    /**
     * Initialize generator with provided characters, key length and random generator seed
     *
     * @param availableChars available characters
     * @param length key length
     * @param seed random generator seed
     */
    public SequenceKeyGenerator(String availableChars, int length, long seed) {
        this(availableChars.toCharArray(), length, seed);
    }

    /**
     * Initialize generator with provided characters and key length
     *
     * @param availableChars available characters
     * @param length key length
     */
    public SequenceKeyGenerator(char[] availableChars, int length) {
        this(availableChars, length, new Random().nextLong());
    }

    /**
     * Initialize generator with provided characters, result length and random generator seed
     *
     * @param availableChars available characters
     * @param length key length
     * @param seed random generator seed
     */
    public SequenceKeyGenerator(char[] availableChars, int length, long seed) {
        this(availableChars, length, BigInteger.ZERO, seed);
    }

    /**
     * Initialize generator with provided characters, key length, random generator seed and initial sequence number
     * Sequence number is in BigInteger format for more available space, 36 ^ 6 > Integer.MAX_VALUE, 62 ^ 11 > Long.MAX_VALUE
     *
     * @param availableChars available characters
     * @param length key length
     * @param seq initial value of sequence number
     * @param seed random generator seed
     */
    public SequenceKeyGenerator(char[] availableChars, int length, BigInteger seq, long seed) {
        verifyCharacters(availableChars);

        this.seed = seed;
        this.availableChars = availableChars;
        this.base = new BigInteger(String.valueOf(this.availableChars.length));
        this.length = new BigInteger(String.valueOf(length));

        initialize();

        this.total = MathUtil.pow(this.base, this.length);
        this.seq = seq;

        // Check for overflow sequence value, if overflow reset to zero
        if (this.seq.compareTo(total) >= 0) {
            this.seq = BigInteger.ZERO;
        }
    }

    // Check for duplicated character, if exists throw IllegalArgumentException
    private void verifyCharacters(char[] availableChars) {
        for (int i = 0; i < availableChars.length-1; i++) {
            for (int j = i+1; j < availableChars.length; j++) {
                if (availableChars[i] == availableChars[j]) {
                    throw new IllegalArgumentException("Duplicate character detected: " + availableChars[i]);
                }
            }
        }
    }

    /**
     * Initialize random parameters, chars & shifts, for obfuscation
     * Same seed will always generate the same result, that means same encoded key for each sequence number.
     */
    private void initialize() {
        Random random = new Random(seed);

        this.chars = new char[length.intValue()][availableChars.length];

        for (char[] aChar : chars) {
            System.arraycopy(availableChars, 0, aChar, 0, aChar.length);
            shuffle(aChar, random);
        }

        this.shifts = new int[length.intValue()];

        for (int i = 0; i < shifts.length; i++) {
            shifts[i] = MathUtil.randomInteger(1, shifts.length, random);
        }
    }

    /**
     * Shuffle the character mapping array for obfuscation
     *
     * @param chars character array to shuffle
     * @param random random generator
     */
    private void shuffle(char[] chars, Random random) {
        for (int i = 0; i < chars.length; i++) {
            int idx = MathUtil.randomInteger(0, chars.length-1, random);

            if (i != idx) {
                char c = chars[i];
                chars[i] = chars[idx];
                chars[idx] = c;
            }
        }
    }

    /**
     * Generate key and increment the sequence number
     *
     * @return generated key
     */
    public String nextKey() {
        return generateKey(incrementSeq());
    }

    /**
     * Skip next sequence number, simply increment the sequence number
     */
    public void skipNext() {
        incrementSeq();
    }

    /**
     * Return the current sequence value then increment
     *
     * @return current sequence number
     */
    private BigInteger incrementSeq() {
        BigInteger oldSeq = seq;

        seq = seq.add(BigInteger.ONE);

        // Overflow, reset sequence number
        if (seq.compareTo(total) >= 0) {
            seq = BigInteger.ZERO;
        }

        return oldSeq;
    }

    /**
     * Remap sequence number into other number (one to one mapping) and return in integer array for character mapping.
     *
     * @param num number to remap
     * @return base value array
     */
    private int[] remapAndPrepare(BigInteger num) {
        BigInteger v = num;
        int[] vals = new int[length.intValue()];

        for (int i = 0; i < vals.length; i++) {
            BigInteger bv = v.mod(base);

            vals[i] = bv.intValue();

            v = v.subtract(bv).divide(base);

            if (i > 0) {
                vals[i] = (vals[0] + vals[i] + i) % base.intValue();
            }
        }

        return vals;
    }

    /**
     * Map base value array into key.
     *
     * @param vals base value array
     * @return resulted key
     */
    private String encodeKey(int[] vals) {
        char[] result = new char[vals.length];

        for (int i = 0; i < vals.length; i++) {
            result[result.length-i-1] = chars[i][vals[i]];
        }

        return new String(result);
    }

    // Sequence number mapper to make the end result looks random
    private BigInteger remap(BigInteger num) {
        return encodeNumber(remapAndPrepare(num));
    }

    /**
     * Encode the integer array into a new number
     *
     * @param vals integer array
     * @return encoded number
     */
    private BigInteger encodeNumber(int[] vals) {
        BigInteger result = BigInteger.ZERO;

        for (BigInteger i = BigInteger.ZERO; i.compareTo(length) < 0; i=i.add(BigInteger.ONE)) {
            result = result.add(new BigInteger(String.valueOf(vals[i.intValue()])).multiply(MathUtil.pow(base, i)));
        }

        return result;
    }

    /**
     * Encode the key number into key.
     *
     * @param keyNum key number
     * @return resulted key
     */
    private String encodeKeyNum(BigInteger keyNum) {
        char[] keyChars = new char[length.intValue()];

        for (BigInteger i = BigInteger.ZERO; i.compareTo(length) < 0; i=i.add(BigInteger.ONE)) {
            int idx = keyNum.divide(MathUtil.pow(base, i)).mod(base).intValue();

            keyChars[keyChars.length - i.intValue() - 1] =
                chars[i.intValue()%length.intValue()][idx];
        }

        return new String(keyChars);
    }

    /**
     * Obfuscate by shifting the number into different partition and invert the order for every even number.
     *
     * @param s number to obfuscate
     * @return obfuscated number
     */
    private BigInteger obfuscate(BigInteger s) {
        int shift = shifts[s.mod(length).intValue()];

        BigInteger partitionSize = total.divide(length);

        int slot = s.divide(partitionSize).intValue();

        int newSlot = (slot+shift) % length.intValue();

        BigInteger idx = s.mod(partitionSize);

        // Add more obfuscation
        if (newSlot % 2 == 0) {
            idx = partitionSize.subtract(BigInteger.ONE).subtract(idx);
        }

        return new BigInteger(String.valueOf(newSlot)).multiply(partitionSize).add(idx);
    }

    /**
     * Generate key with the sequence number
     * @param s sequence number
     * @return resulted key
     */
    private String generateKey(BigInteger s) {
        return encodeKey(remapAndPrepare(obfuscate(s)));
    }

    /**
     * Generate the key number, remapped & obfuscated sequence number
     * @param s sequence number
     * @return resulted remapped number
     */
    private BigInteger generateKeyNum(BigInteger s) {
        return remap(obfuscate(s));
    }
}
