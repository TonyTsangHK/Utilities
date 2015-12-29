package utils.math;

import java.util.LinkedList;

public class ByteConverter {
    public static final int UPPER_MASK = 0xFF;
    
    public static float byteArrayToFloat(byte[] bytes) {
        return Float.intBitsToFloat(byteArrayToInt(bytes));
    }
    
    public static double byteArrayToDouble(byte[] bytes) {
        return Double.longBitsToDouble(byteArrayToLong(bytes));
    }
    
    public static int byteArrayToInt(byte[] bytes) {
        int v = 0;
        int i = 0;
        for (int shifter = bytes.length-1; shifter >= 0; shifter--) {
            v |= ((int) bytes[i] & UPPER_MASK) << (shifter * 8);
            i++;
        }
        return v;
    }
    
    public static long byteArrayToLong(byte[] bytes) {
        long v = 0;
        int i = 0;
        for (int shifter = bytes.length-1; shifter >= 0; shifter--) {
            v |= ((long) bytes[i] & UPPER_MASK) << (shifter * 8);
            i++;
        }
        
        return v;
    }
    
    public static byte[] floatToByteArray(float v) {
        int i = Float.floatToRawIntBits(v);
        return intToByteArray(i);
    }
    
    public static byte[] doubleToByteArray(double v) {
        long l = Double.doubleToRawLongBits(v);
        return longToByteArray(l);
    }
    
    public static byte[] intToByteArrayStrict(int v) {
        byte[] bytes = new byte[4];
        
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) ((v >>> ((3 - i) * 8)) & UPPER_MASK);
        }
        
        return bytes;
    }
    
    public static byte[] intToByteArray(int v) {
        boolean started = false;
        LinkedList<Byte> byteList = new LinkedList<Byte>();
        
        for (int i = 0; i < 4; i++) {
            byte b = (byte) ((v >>> ((3 - i) * 8)) & UPPER_MASK);
            if (started || b != 0) {
                started = true;
                byteList.add(new Byte(b));
            }
        }
        
        byte[] bytes = new byte[byteList.size()];
        int c = 0;
        for (Byte by : byteList) {
            bytes[c++] = by.byteValue();
        }
        return bytes;
    }
    
    public static byte[] longToByteArrayStrict(long v) {
        byte[] bytes = new byte[8];
        
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) ((v >>> ((7 - i) * 8)) & UPPER_MASK);
        }
        
        return bytes;
    }
    
    public static byte[] longToByteArray(long v) {
        boolean started = false;
        LinkedList<Byte> byteList = new LinkedList<Byte>();
        
        for (int i = 0; i < 8; i++) {
            byte b = (byte) ((v >>> ((7 - i) * 8)) & UPPER_MASK);
            
            if (started || b != 0) {
                started = true;
                byteList.add(new Byte(b));
            }
        }
        
        byte[] bytes = new byte[byteList.size()];
        int c = 0;
        for (Byte by : byteList) {
            bytes[c++] = by.byteValue();
        }
        return bytes;
    }
}