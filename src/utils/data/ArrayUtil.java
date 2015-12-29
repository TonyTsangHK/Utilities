package utils.data;

import java.util.*;

import utils.math.MathUtil;

public class ArrayUtil {
    /**
     * Calculate average of a double precision array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    public static double arrayAvg(double[] arr) {
        return arraySum(arr)/arr.length;
    }
    
    /**
     * Calculate average of a double array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    public static double arrayAvg(double[] arr, int start, int length) {
        if (start >= 0 && start < arr.length) {
            int c = length;
            if (start + length - 1 >= arr.length) {
                c = arr.length - start;
            }
            return arraySum(arr, start, length)/(double)c;
        } else {
            return 0;
        }
    }
    
    /**
     * Calculate average of a single precision (float) array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    public static double arrayAvg(float[] arr) {
        return arraySum(arr) / (double)arr.length;
    }
    
    /**
     * Calculate average of a single precision (float) array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    public static double arrayAvg(float[] arr, int start, int length) {
        if (start >= 0 && start < arr.length) {
            int c = length;
            if (start + length - 1 >= arr.length) {
                c = arr.length - start;
            }
            return arraySum(arr, start, length)/(double)c;
        } else {
            return 0;
        }
    }
    
    /**
     * Calculate average of an integer array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    public static double arrayAvg(int[] arr) {
        return arraySum(arr) / (double) arr.length;
    }
    
    /**
     * Calculate average of an integer array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    public static double arrayAvg(int[] arr, int start, int length) {
        if (start >= 0 && start < arr.length) {
            int c = length;
            if (start + length - 1 >= arr.length) {
                c = arr.length - start;
            }
            return arraySum(arr, start, length)/(double)c;
        } else {
            return 0;
        }
    }
    
    /**
     * Calculate average of a long integer array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    public static double arrayAvg(long[] arr) {
        return arraySum(arr) / (double)arr.length;
    }
    
    /**
     * Calculate average of a long integer array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    public static double arrayAvg(long[] arr, int start, int length) {
        if (start >= 0 && start < arr.length) {
            int c = length;
            if (start + length - 1 >= arr.length) {
                c = arr.length - start;
            }
            return arraySum(arr, start, length)/(double)c;
        } else {
            return 0;
        }
    }
    
    /**
     * Calculate average of a short integer array.
     * 
     * @param arr Array to be processed.
     * @return Average of the target array.
     */
    public static double arrayAvg(short[] arr) {
        return arraySum(arr) / (double)arr.length;
    }
    
    /**
     * Calculate average of a short integer array with specified start index and length
     * 
     * @param arr array to be processed.
     * @param start start index of the array
     * @param length length of elements after start index  
     * @return Average of the target elements
     */
    public static double arrayAvg(short[] arr, int start, int length) {
        if (start >= 0 && start < arr.length) {
            int c = length;
            if (start + length - 1 >= arr.length) {
                c = arr.length - start;
            }
            return arraySum(arr, start, length)/(double)c;
        } else {
            return 0;
        }
    }
    
    /**
     * Calculate total sum of a double precision array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    public static double arraySum(double[] arr) {
        double sum = 0;
        for (double v : arr) {
            sum += v;
        }
        return sum;
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
    public static double arraySum(double[] arr, int start, int length) {
        double sum = 0;
        if (start >= 0 && start < arr.length) {
            int endIndex = start + length;
            for (int i = start; i < endIndex && i < arr.length; i++) {
                sum += arr[i];
            }
        }
        return sum;
    }
    
    /**
     * Calculate total sum of a single precision array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    public static float arraySum(float[] arr) {
        float sum = 0;
        for (float v : arr) {
            sum += v;
        }
        return sum;
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
    public static float arraySum(float[] arr, int start, int length) {
        float sum = 0;
        if (start >= 0 && start < arr.length) {
            int endIndex = start + length;
            for (int i = start; i < endIndex && i < arr.length; i++) {
                sum += arr[i];
            }
        }
        return sum;
    }
    
    /**
     * Calculate total sum of an integer array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    public static int arraySum(int[] arr) {
        int sum = 0;
        for (int v : arr) {
            sum += v;
        }
        return sum;
    }
    
    /**
     * Calculate total sum of an integer array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * @return sum of the target elements
     */
    public static int arraySum(int[] arr, int start, int length) {
        int sum = 0;
        if (start >= 0 && start < arr.length) {
            int endIndex = start + length;
            for (int i = start; i < endIndex && i < arr.length; i++) {
                sum += arr[i];
            }
        }
        return sum;
    }
    
    /**
     * Calculate total sum of a long integer array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    public static long arraySum(long[] arr) {
        long sum = 0;
        for (long v : arr) {
            sum += v;
        }
        return sum;
    }
    
    /**
     * Calculate total sum of a long integer array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * @return sum of the target elements
     */
    public static long arraySum(long[] arr, int start, int length) {
        long sum = 0;
        if (start >= 0 && start < arr.length) {
            int endIndex = start + length;
            for (int i = start; i < endIndex && i < arr.length; i++) {
                sum += arr[i];
            }
        }
        return sum;
    }
    
    /**
     * Calculate total sum of a short integer array.
     * 
     * @param arr target array to be processed
     * @return sum of the target array
     */
    public static short arraySum(short[] arr) {
        short sum = 0;
        for (short v : arr) {
            sum += v;
        }
        return sum;
    }
    
    /**
     * Calculate total sum of a short integer array with specified start index and length
     * 
     * @param arr array to be processed
     * @param start start index of the target elements
     * @param length length of the target elements
     * @return sum of the target elements
     */
    public static short arraySum(short[] arr, int start, int length) {
        short sum = 0;
        if (start >= 0 && start < arr.length) {
            int endIndex = start + length;
            for (int i = start; i < endIndex && i < arr.length; i++) {
                sum += arr[i];
            }
        }
        return sum;
    }
    
    /**
     * Create a boolean array with specified size
     * 
     * @param size size of the array
     * @return boolean array with specified size filled with default value (false)
     */
    public static boolean[] createBooleanArray(int size) {
        return createBooleanArray(size, false);
    }
    
    /**
     * Create a boolean array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of the array
     * @return boolean array with specified size and initial value  
     */
    public static boolean[] createBooleanArray(int size, boolean initialValue) {
        boolean[] arr = new boolean[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Create a byte array with specified size
     * 
     * @param size size of the array
     * @return byte array with specified size filled with default value (0)
     */
    public static byte[] createByteArray(int size) {
        return createByteArray(size, (byte)0);
    }
    
    /**
     * Create a byte array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return byte array with specified size and initial value
     */
    public static byte[] createByteArray(int size, byte initialValue) {
        byte[] arr = new byte[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Create a character array with specified size
     * 
     * @param size size of the array
     * @return character array with specified size filled with default character (space)
     */
    public static char[] createCharacterArray(int size) {
        return createCharacterArray(size, ' ');
    }
    
    /**
     * Create a character array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return character array with specified size and initial value
     */
    public static char[] createCharacterArray(int size, char initialValue) {
        char[] arr = new char[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Create a double precision array with specified size
     * 
     * @param size size of the array
     * @return double precision array with specified size with default value (0)
     */
    public static double[] createDoubleArray(int size) {
        return createDoubleArray(size, 0);
    }
    
    /**
     * Create a double precision array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return double precision array with specified size and initial value
     */
    public static double[] createDoubleArray(int size, double initialValue) {
        double[] arr = new double[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Create a single precision array with specified size
     * 
     * @param size size of the array
     * @return single precision array with specified size with default value (0)
     */
    public static float[] createFloatArray(int size) {
        return createFloatArray(size, 0f);
    }
    
    /**
     * Create a single precision array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return single precision array with specified size and initial value
     */
    public static float[] createFloatArray(int size, float initialValue) {
        float[] arr = new float[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Create an integer array with specified size
     * 
     * @param size size of the array
     * @return integer array with specified size with default value (0)
     */
    public static int[] createIntegerArray(int size) {
        return createIntegerArray(size, 0);
    }
    
    /**
     * Create an integer array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return integer array with specified size and initial value
     */
    public static int[] createIntegerArray(int size, int initialValue) {
        int[] arr = new int[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Create a long integer array with specified size
     * 
     * @param size size of the array
     * @return long integer array with specified size with default value (0)
     */
    public static long[] createLongArray(int size) {
        return createLongArray(size, 0);
    }
    
    /**
     * Create a long integer array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return long integer array with specified size and initial value
     */
    public static long[] createLongArray(int size, long initialValue) {
        long[] arr = new long[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Create a short integer array with specified size
     * 
     * @param size size of the array
     * @return short integer array with specified size with default value (0)
     */
    public static short[] createShortArray(int size) {
        return createShortArray(size, (short)0);
    }
    
    /**
     * Create a short integer array with specified size and initial value
     * 
     * @param size size of the array
     * @param initialValue initial value of array elements
     * @return short integer array with specified size and initial value
     */
    public static short[] createShortArray(int size, short initialValue) {
        short[] arr = new short[size];
        Arrays.fill(arr, initialValue);
        return arr;
    }
    
    /**
     * Shuffle(randomize) the integer array
     * 
     * @param arr integer array
     */
    public static void shuffle(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                int t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the short array
     * 
     * @param arr short array
     */
    public static void shuffle(short[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                short t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the byte array
     * 
     * @param arr byte array
     */
    public static void shuffle(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                byte t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the boolean array
     * 
     * @param arr boolean array
     */
    public static void shuffle(boolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                boolean t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the long array
     * 
     * @param arr long array
     */
    public static void shuffle(long[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                long t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the double array
     * 
     * @param arr double array
     */
    public static void shuffle(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                double t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the float array
     * 
     * @param arr float array
     */
    public static void shuffle(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                float t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the char array
     * 
     * @param arr char array
     */
    public static void shuffle(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                char t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Shuffle(randomize) the array
     * 
     * @param <E> Generic type
     * @param arr target array
     */
    public static <E> void shuffle(E[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = MathUtil.randomInteger(0, arr.length-1);
            if (r != i) {
                E t = arr[i];
                arr[i] = arr[r];
                arr[r] = t;
            }
        }
    }
    
    /**
     * Fill the boolean array with specified boolean value
     * 
     * @param arr target boolean array
     * @param v value
     */
    public static void fill(boolean[] arr, boolean v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the byte array with specified byte value
     * 
     * @param arr target byte array
     * @param v value
     */
    public static void fill(byte[] arr, byte v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the char array with specified char value
     * 
     * @param arr target char array
     * @param v value
     */
    public static void fill(char[] arr, char v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the short array with specified short value
     * 
     * @param arr target short array
     * @param v value
     */
    public static void fill(short[] arr, short v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the integer array with specified integer value
     * 
     * @param arr target integer array
     * @param v value
     */
    public static void fill(int[] arr, int v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the long array with specified long value
     * 
     * @param arr target long array
     * @param v value
     */
    public static void fill(long[] arr, long v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the float array with specified float value
     * 
     * @param arr target float array
     * @param v value
     */
    public static void fill(float[] arr, float v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the double array with specified double value
     * 
     * @param arr target double array
     * @param v value
     */
    public static void fill(double[] arr, double v) {
        Arrays.fill(arr, v);
    }
    
    /**
     * Fill the generic array with specified value
     * 
     * @param <E> Generic type
     * 
     * @param arr target array
     * @param v value
     */
    public static <E> void fill(E[] arr, E v) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = v;
        }
    }
    
    /**
     * Fill the boolean arrays with specified boolean value
     * 
     * @param v value
     * @param arrs target boolean arrays
     */
    public static void fill(boolean v, boolean[] ... arrs) {
        for (boolean[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the byte arrays with specified byte value
     * 
     * @param v value
     * @param arrs target byte arrays
     */
    public static void fill(byte v, byte[] ... arrs) {
        for (byte[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the char arrays with specified char value
     * 
     * @param v value
     * @param arrs target char arrays
     */
    public static void fill(char v, char[] ... arrs) {
        for (char[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the int arrays with specified int value
     * 
     * @param v value
     * @param arrs target int arrays
     */
    public static void fill(int v, int[] ... arrs) {
        for (int[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the short arrays with specified short value
     * 
     * @param v value
     * @param arrs target short arrays
     */
    public static void fill(short v, short[] ... arrs) {
        for (short[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the long arrays with specified long value
     * 
     * @param v value
     * @param arrs target long arrays
     */
    public static void fill(long v, long[] ... arrs) {
        for (long[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the float arrays with specified float value
     * 
     * @param v value
     * @param arrs target float arrays
     */
    public static void fill(float v, float[] ... arrs) {
        for (float[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the double arrays with specified double value
     * 
     * @param v value
     * @param arrs target double arrays
     */
    public static void fill(double v, double[] ... arrs) {
        for (double[] arr : arrs) {
            Arrays.fill(arr, v);
        }
    }
    
    /**
     * Fill the arrays with specified value
     * 
     * @param <E> Generic type
     * @param v value
     * @param arrs arrays to be filled
     */
    public static <E> void fill(E v, E[] ... arrs) {
        for (E[] arr : arrs) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = v;
            }
        }
    }
    
    public static <E> void reverse(E[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int j = arr.length - i - 1;
            E t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }
    }
}
