package utils.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Provide utilities methods for serialization of java value into byte stream. 
 * 
 * @author Tony Tsang
 */
public class SerializeUtils {
    /**
     * Mask for int <> byte conversion, ignore the upper bits.
     */
    public static final int UPPER_MASK = 0xFF;
    
    /**
     * Default charset for String > byte conversion
     */
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    /**
     * Hide default constructor.
     */
    private SerializeUtils() {}
    
    /**
     * Convert a byte array to float
     * 
     * @param bytes target byte array
     * 
     * @return converted float value
     */
    public static float byteArrayToFloat(byte[] bytes) {
        return Float.intBitsToFloat(byteArrayToInt(bytes));
    }
    
    /**
     * Convert byte array to double
     * 
     * @param bytes target byte array
     * 
     * @return converted double value
     */
    public static double byteArrayToDouble(byte[] bytes) {
        return Double.longBitsToDouble(byteArrayToLong(bytes));
    }
    
    /**
     * Convert byte array to integer
     * 
     * @param bytes target byte array
     * 
     * @return converted integer value
     */
    public static int byteArrayToInt(byte[] bytes) {
        int v = 0, i = 0;
        for (int shifter = bytes.length-1; shifter >= 0; shifter--) {
            v |= ((int) bytes[i] & UPPER_MASK) << (shifter * 8);
            i++;
        }
        return v;
    }
    
    /**
     * Convert byte array to long
     * 
     * @param bytes target byte array
     * 
     * @return converted long value
     */
    public static long byteArrayToLong(byte[] bytes) {
        long v = 0;
        int  i = 0;
        for (int shifter = bytes.length-1; shifter >= 0; shifter--) {
            v |= ((long) bytes[i] & UPPER_MASK) << (shifter * 8);
            i++;
        }
        
        return v;
    }
    
    /**
     * Convert a single floating point value to byte array
     * 
     * @param v target value
     * 
     * @return converted byte array
     */
    public static byte[] floatToByteArray(float v) {
        int i = Float.floatToRawIntBits(v);
        return intToByteArrayStrict(i);
    }
    
    /**
     * Convert a double floating point value to byte array
     * 
     * @param v target value
     * @return converted byte array
     */
    public static byte[] doubleToByteArray(double v) {
        long l = Double.doubleToRawLongBits(v);
        return longToByteArrayStrict(l);
    }
    
    /**
     * Convert an integer value to byte array (result will be a byte array in length 4, no trimming)
     * 
     * @param v target value
     * 
     * @return converted byte array
     */
    public static byte[] intToByteArrayStrict(int v) {
        byte[] bytes = new byte[4];
        
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) ((v >>> ((3 - i) * 8)) & UPPER_MASK);
        }
        
        return bytes;
    }
    
    /**
     * Convert an integer value to byte array (variable length from 1 ~ 4)
     * 
     * @param v target value
     * @return converted byte array
     */
    public static byte[] intToByteArray(int v) {
        if (v == 0) {
            return new byte[]{0};
        }
        boolean started = false;
        byte[] bytes = new byte[4];
        int offset = 0;
        
        for (int i = 0; i < 4; i++) {
            byte b = (byte) ((v >>> ((3 - i) * 8)) & UPPER_MASK);
            if (started || b != 0) {
                started = true;
                bytes[offset++] = b;
            }
        }

        if (offset == 4) {
            return bytes;
        } else {
            return Arrays.copyOf(bytes, offset);
        }
    }
    
    /**
     * Convert a long value to byte array (result will be a byte array in length 8, no trimming)
     * 
     * @param v target value
     * 
     * @return converted byte array
     */
    public static byte[] longToByteArrayStrict(long v) {
        byte[] bytes = new byte[8];
        
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) ((v >>> ((7 - i) * 8)) & UPPER_MASK);
        }
        
        return bytes;
    }
    
    /**
     * Convert a long value to byte array (variable length from 1 ~ 8)
     * 
     * @param v target value
     * 
     * @return converted byte array
     */
    public static byte[] longToByteArray(long v) {
        if (v == 0) {
            return new byte[]{0};
        }
        boolean started = false;

        byte[] bytes = new byte[8];
        int offset = 0;
        
        for (int i = 0; i < 8; i++) {
            byte b = (byte) ((v >>> ((7 - i) * 8)) & UPPER_MASK);
            
            if (started || b != 0) {
                started = true;
                bytes[offset++] = b;
            }
        }

        if (offset == 8) {
            return bytes;
        } else {
            return Arrays.copyOf(bytes, offset);
        }
    }

    public static byte[] readStream(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        int len = readStream(in, bytes);
        if (len < bytes.length) {
            return Arrays.copyOf(bytes, len);
        } else {
            return bytes;
        }
    }

    public static int readStream(InputStream in, byte[] bytes) throws IOException {
        int len, offset = 0;
        do {
            len = in.read(bytes, offset, bytes.length - offset);
            offset += (len != -1)? len : 0;
        } while (len != -1 && offset < bytes.length);

        return offset;
    }
}
