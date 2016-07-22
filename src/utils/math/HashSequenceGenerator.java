package utils.math;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-07-11
 * Time: 18:06
 */
public class HashSequenceGenerator {
    private char[] availableChars;

    // Character mapper for each character place, this will make the result key looks like random.
    private char[][] chars;

    // shift sequence number partition (partition size is total / length) for obfuscation.
    private int[] shifts;


    private long seed;

    private BigInteger total, seq, length, base;

    public HashSequenceGenerator(String availableChars, int length) {
        this(availableChars.toCharArray(), length, new Random().nextLong());
    }

    public HashSequenceGenerator(String availableChars, int length, long seed) {
        this(availableChars.toCharArray(), length, seed);
    }

    public HashSequenceGenerator(char[] availableChars, int length) {
        this(availableChars, length, new Random().nextLong());
    }

    public HashSequenceGenerator(char[] availableChars, int length, long seed) {
        this(availableChars, length, BigInteger.ZERO, seed);
    }

    public HashSequenceGenerator(char[] availableChars, int length, BigInteger seq, long seed) {
        this.seed = seed;
        this.availableChars = availableChars;
        this.base = new BigInteger(String.valueOf(this.availableChars.length));
        this.length = new BigInteger(String.valueOf(length));

        initialize(availableChars, length, seed);

        this.total = MathUtil.pow(this.base, this.length);
        this.seq = seq;
    }

    // Result will be determined by seed
    private void initialize(char[] availableChars, int length, long seed) {
        Random random = new Random(seed);

        this.chars = new char[length][availableChars.length];

        for (char[] aChar : chars) {
            System.arraycopy(availableChars, 0, aChar, 0, aChar.length);
            shuffle(aChar, random);
        }

        this.shifts = new int[length];

        for (int i = 0; i < shifts.length; i++) {
            shifts[i] = MathUtil.randomInteger(1, shifts.length, random);
        }
    }

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

    public String nextKey() {
        return generateKey(incrementSeq());
    }

    private BigInteger incrementSeq() {
        BigInteger oldSeq = seq;

        seq = seq.add(BigInteger.ONE);

        // Overflow, reset sequence number
        if (seq.compareTo(total) > 0) {
            seq = BigInteger.ZERO;
        }

        return oldSeq;
    }

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
    
    private BigInteger encodeNumber(int[] vals) {
        BigInteger result = BigInteger.ZERO;

        for (BigInteger i = BigInteger.ZERO; i.compareTo(length) < 0; i=i.add(BigInteger.ONE)) {
            result = result.add(new BigInteger(String.valueOf(vals[i.intValue()])).multiply(MathUtil.pow(base, i)));
        }

        return result;
    }

    private String encodeKeyNum(BigInteger keyNum) {
        char[] keyChars = new char[length.intValue()];

        for (BigInteger i = BigInteger.ZERO; i.compareTo(length) < 0; i=i.add(BigInteger.ONE)) {
            int idx = keyNum.divide(MathUtil.pow(base, i)).mod(base).intValue();

            keyChars[keyChars.length - i.intValue() - 1] =
                chars[i.intValue()%length.intValue()][idx];
        }

        return new String(keyChars);
    }

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

    private String generateKey(BigInteger s) {
        return encodeKey(remapAndPrepare(obfuscate(s)));
    }

    private BigInteger generateKeyNum(BigInteger s) {
        return remap(obfuscate(s));
    }
}
