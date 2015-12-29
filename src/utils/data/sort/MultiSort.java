package utils.data.sort;

import java.util.Comparator;
import java.util.List;

import utils.data.DataComparator;
import utils.data.DataManipulator;

/**
 * 
 * This class is copy from JDK 7's source java.util.DualPivotQuickSort, but the DualPivotQuickSort logic is replaced by SinglePivotQuickSort logic since it show no advantage in random data testing (mistake?).
 * 
 */
public class MultiSort implements Sorter {
    private MultiSort() {}
    
    private static final int QUICKSORT_THRESHOLD = 286;
    private static final int MAX_RUN_COUNT = 67;
    private static final int MAX_RUN_LENGTH = 33;
    
    /**
     * Sort the list using dual pivot quick sort algorithm
     * 
     * @param <E> Generic type E
     * @param list The list to be sorted
     */
    public static <E> void sort(List<E> list) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(list, c);
    }
    
    /**
     * Sort the list using dual pivot quick sort algorithm with supplied comparator
     * 
     * @param list The list to be sorted
     * @param comparator Comparator for comparing purpose
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void sort(List list, Comparator comparator) {
        if (list.size() > 1) {
            Object[] array = list.toArray();
            sort(array, comparator);
            DataManipulator.copyArrayToList(list, array);
        }
    }
    
    /**
     * Sort the array using dual pivot quick sort algorithm
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     */
    public static <E> void sort(E[] array) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(array, c);
    }
    
    public static <E> void sort(E[] a, Comparator<E> c) {
        sort(a, c, 0, a.length-1);
    }
    
    public static <E> void sort(E[] a, Comparator<E> c, int left, int right) {
        // Use Quicksort on small arrays
        if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(a, c, left, right);
        } else {
	        /*
	         * Index run[i] is the start of i-th run
	         * (ascending or descending sequence).
	         */
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	
	        // Check if the array is nearly sorted
	        for (int k = left; k < right; run[count] = k) {
	            if (c.compare(a[k], a[k + 1]) < 0) { // ascending
	                while (++k <= right && c.compare(a[k - 1], a[k]) <= 0);
	            } else if (c.compare(a[k], a[k + 1]) > 0) { // descending
	                while (++k <= right && c.compare(a[k - 1], a[k]) >= 0);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
	                	DataManipulator.swapData(a, hi, lo);
	                }
	            } else { // equal
	                for (int m = MAX_RUN_LENGTH; ++k <= right && c.compare(a[k - 1], a[k]) == 0; ) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(a, c, left, right);
	                        return;
	                    }
	                }
	            }
	
	            /*
	             * The array is not highly structured,
	             * use Quicksort instead of merge sort.
	             */
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(a, c, left, right);
	                return;
	            }
	        }
	
	        // Check special cases
	        if (run[count] == right++) { // The last run contains one element
	            run[++count] = right;
	        } else if (count == 1) { // The array is already sorted
	            return;
	        }
	        
	        /*
	         * Create temporary array, which is used for merging.
	         * Implementation note: variable "right" is increased by 1.
	         */
	        E[] b; byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = a; a = b.clone();
	            for (int i = left - 1; ++i < right; a[i] = b[i]);
	        } else {
	            b = a.clone();
	        }
	
	        // Merging
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && c.compare(a[p], a[q]) <= 0) {
	                        b[i] = a[p++];
	                    } else {
	                        b[i] = a[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = a[i]
	                );
	                run[++last] = right;
	            }
	            E[] t = a; a = b; b = t;
	        }
        }
    }
    
    public static void sort(byte[] array) {
    	sort(array, 0, array.length-1);
    }
    
    public static void sort(char[] array) {
    	sort(array, 0, array.length-1);
    }
    
    public static void sort(short[] array) {
    	sort(array, 0, array.length-1);
    }
    
    public static void sort(float[] array) {
    	sort(array, 0, array.length-1);
    }
    
    public static void sort(int[] array) {
    	sort(array, 0, array.length-1);
    }
    
    public static void sort(long[] array) {
    	sort(array, 0, array.length-1);
    }
    
    public static void sort(double[] array) {
    	sort(array, 0, array.length-1);
    }
    
    public static void sort(byte[] array, int left, int right) {
        if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(array, left, right);
        } else {
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	        
	        for (int k = left; k < right; run[count] = k) {
	            if (array[k] < array[k + 1]) {
	                while (++k <= right && array[k - 1] <= array[k]);
	            } else if (array[k] > array[k + 1]) {
	                while (++k <= right && array[k - 1] >= array[k]);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
	                	DataManipulator.swapData(array, lo, hi);
	                }
	            } else {
	                for (int m = MAX_RUN_LENGTH; ++k <= right && array[k - 1] == array[k];) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(array, left, right);
	                        return;
	                    }
	                }
	            }
	            
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(array, left, right);
	                return;
	            }
	        }
	        
	        if (run[count] == right++) {
	            run[++count] = right;
	        } else if (count == 1) {
	            return;
	        }
	        
	        byte[] b;
	        byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = array;
	            array = b.clone();
	            for (int i = left - 1; ++i < right; array[i] = b[i]);
	        } else {
	            b = new byte[array.length];
	        }
	        
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && array[p] <= array[q]) {
	                        b[i] = array[p++];
	                    } else {
	                        b[i] = array[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = array[i]
	                );
	                run[++last] = right;
	            }
	            byte[] t = array;
	            array = b;
	            b = t;
	        }
        }
    }
    
    public static void sort(char[] array, int left, int right) {
    	if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(array, left, right);
        } else {
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	        
	        for (int k = left; k < right; run[count] = k) {
	            if (array[k] < array[k + 1]) {
	                while (++k <= right && array[k - 1] <= array[k]);
	            } else if (array[k] > array[k + 1]) {
	                while (++k <= right && array[k - 1] >= array[k]);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
	                	DataManipulator.swapData(array, lo, hi);
	                }
	            } else {
	                for (int m = MAX_RUN_LENGTH; ++k <= right && array[k - 1] == array[k];) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(array, left, right);
	                        return;
	                    }
	                }
	            }
	            
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(array, left, right);
	                return;
	            }
	        }
	        
	        if (run[count] == right++) {
	            run[++count] = right;
	        } else if (count == 1) {
	            return;
	        }
	        
	        char[] b;
	        byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = array;
	            array = b.clone();
	            for (int i = left - 1; ++i < right; array[i] = b[i]);
	        } else {
	            b = new char[array.length];
	        }
	        
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && array[p] <= array[q]) {
	                        b[i] = array[p++];
	                    } else {
	                        b[i] = array[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = array[i]
	                );
	                run[++last] = right;
	            }
	            char[] t = array;
	            array = b;
	            b = t;
	        }
        }
    }
    
    public static void sort(short[] array, int left, int right) {
    	if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(array, left, right);
        } else {
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	        
	        for (int k = left; k < right; run[count] = k) {
	            if (array[k] < array[k + 1]) {
	                while (++k <= right && array[k - 1] <= array[k]);
	            } else if (array[k] > array[k + 1]) {
	                while (++k <= right && array[k - 1] >= array[k]);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
	                	DataManipulator.swapData(array, lo, hi);
	                }
	            } else {
	                for (int m = MAX_RUN_LENGTH; ++k <= right && array[k - 1] == array[k];) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(array, left, right);
	                        return;
	                    }
	                }
	            }
	            
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(array, left, right);
	                return;
	            }
	        }
	        
	        if (run[count] == right++) {
	            run[++count] = right;
	        } else if (count == 1) {
	            return;
	        }
	        
	        short[] b;
	        byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = array;
	            array = b.clone();
	            for (int i = left - 1; ++i < right; array[i] = b[i]);
	        } else {
	            b = new short[array.length];
	        }
	        
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && array[p] <= array[q]) {
	                        b[i] = array[p++];
	                    } else {
	                        b[i] = array[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = array[i]
	                );
	                run[++last] = right;
	            }
	            short[] t = array;
	            array = b;
	            b = t;
	        }
        }
    }
    
    public static void sort(float[] array, int left, int right) {
    	if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(array, left, right);
        } else {
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	        
	        for (int k = left; k < right; run[count] = k) {
	            if (array[k] < array[k + 1]) {
	                while (++k <= right && array[k - 1] <= array[k]);
	            } else if (array[k] > array[k + 1]) {
	                while (++k <= right && array[k - 1] >= array[k]);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
	                	DataManipulator.swapData(array, lo, hi);
	                }
	            } else {
	                for (int m = MAX_RUN_LENGTH; ++k <= right && array[k - 1] == array[k];) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(array, left, right);
	                        return;
	                    }
	                }
	            }
	            
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(array, left, right);
	                return;
	            }
	        }
	        
	        if (run[count] == right++) {
	            run[++count] = right;
	        } else if (count == 1) {
	            return;
	        }
	        
	        float[] b;
	        byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = array;
	            array = b.clone();
	            for (int i = left - 1; ++i < right; array[i] = b[i]);
	        } else {
	            b = new float[array.length];
	        }
	        
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && array[p] <= array[q]) {
	                        b[i] = array[p++];
	                    } else {
	                        b[i] = array[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = array[i]
	                );
	                run[++last] = right;
	            }
	            float[] t = array;
	            array = b;
	            b = t;
	        }
        }
    }
    
    public static void sort(int[] array, int left, int right) {
    	if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(array, left, right);
        } else {
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	        
	        for (int k = left; k < right; run[count] = k) {
	            if (array[k] < array[k + 1]) {
	                while (++k <= right && array[k - 1] <= array[k]);
	            } else if (array[k] > array[k + 1]) {
	                while (++k <= right && array[k - 1] >= array[k]);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
	                	DataManipulator.swapData(array, lo, hi);
	                }
	            } else {
	                for (int m = MAX_RUN_LENGTH; ++k <= right && array[k - 1] == array[k];) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(array, left, right);
	                        return;
	                    }
	                }
	            }
	            
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(array, left, right);
	                return;
	            }
	        }
	        
	        if (run[count] == right++) {
	            run[++count] = right;
	        } else if (count == 1) {
	            return;
	        }
	        
	        int[] b;
	        byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = array;
	            array = b.clone();
	            for (int i = left - 1; ++i < right; array[i] = b[i]);
	        } else {
	            b = new int[array.length];
	        }
	        
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && array[p] <= array[q]) {
	                        b[i] = array[p++];
	                    } else {
	                        b[i] = array[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = array[i]
	                );
	                run[++last] = right;
	            }
	            int[] t = array;
	            array = b;
	            b = t;
	        }
        }
    }
    
    public static void sort(long[] array, int left, int right) {
    	if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(array, left, right);
        } else {
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	        
	        for (int k = left; k < right; run[count] = k) {
	            if (array[k] < array[k + 1]) {
	                while (++k <= right && array[k - 1] <= array[k]);
	            } else if (array[k] > array[k + 1]) {
	                while (++k <= right && array[k - 1] >= array[k]);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
	                	DataManipulator.swapData(array, lo, hi);
	                }
	            } else {
	                for (int m = MAX_RUN_LENGTH; ++k <= right && array[k - 1] == array[k];) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(array, left, right);
	                        return;
	                    }
	                }
	            }
	            
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(array, left, right);
	                return;
	            }
	        }
	        
	        if (run[count] == right++) {
	            run[++count] = right;
	        } else if (count == 1) {
	            return;
	        }
	        
	        long[] b;
	        byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = array;
	            array = b.clone();
	            for (int i = left - 1; ++i < right; array[i] = b[i]);
	        } else {
	            b = new long[array.length];
	        }
	        
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && array[p] <= array[q]) {
	                        b[i] = array[p++];
	                    } else {
	                        b[i] = array[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = array[i]
	                );
	                run[++last] = right;
	            }
	            long[] t = array;
	            array = b;
	            b = t;
	        }
        }
    }
    
    public static void sort(double[] array, int left, int right) {
    	if (right - left < QUICKSORT_THRESHOLD) {
            SinglePivotQuickSort.sort(array, left, right);
        } else {
	        int[] run = new int[MAX_RUN_COUNT + 1];
	        int count = 0; run[0] = left;
	        
	        for (int k = left; k < right; run[count] = k) {
	            if (array[k] < array[k + 1]) {
	                while (++k <= right && array[k - 1] <= array[k]);
	            } else if (array[k] > array[k + 1]) {
	                while (++k <= right && array[k - 1] >= array[k]);
	                for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
	                	DataManipulator.swapData(array, lo, hi);
	                }
	            } else {
	                for (int m = MAX_RUN_LENGTH; ++k <= right && array[k - 1] == array[k];) {
	                    if (--m == 0) {
	                        SinglePivotQuickSort.sort(array, left, right);
	                        return;
	                    }
	                }
	            }
	            
	            if (++count == MAX_RUN_COUNT) {
	                SinglePivotQuickSort.sort(array, left, right);
	                return;
	            }
	        }
	        
	        if (run[count] == right++) {
	            run[++count] = right;
	        } else if (count == 1) {
	            return;
	        }
	        
	        double[] b;
	        byte odd = 0;
	        for (int n = 1; (n <<= 1) < count; odd ^= 1);
	
	        if (odd == 0) {
	            b = array;
	            array = b.clone();
	            for (int i = left - 1; ++i < right; array[i] = b[i]);
	        } else {
	            b = new double[array.length];
	        }
	        
	        for (int last; count > 1; count = last) {
	            for (int k = (last = 0) + 2; k <= count; k += 2) {
	                int hi = run[k], mi = run[k - 1];
	                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
	                    if (q >= hi || p < mi && array[p] <= array[q]) {
	                        b[i] = array[p++];
	                    } else {
	                        b[i] = array[q++];
	                    }
	                }
	                run[++last] = hi;
	            }
	            if ((count & 1) != 0) {
	                for (int i = right, lo = run[count - 1]; --i >= lo;
	                    b[i] = array[i]
	                );
	                run[++last] = right;
	            }
	            double[] t = array;
	            array = b;
	            b = t;
	        }
        }
    }
}
