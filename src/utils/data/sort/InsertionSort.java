package utils.data.sort;

import utils.data.DataComparator;
import utils.data.DataManipulator;

import java.util.Comparator;
import java.util.List;

public class InsertionSort implements Sorter {
    private InsertionSort() {}
    
    /**
     * Sort the list using insertion sort algorithm (Use it on small list only)
     * 
     * @param <E> Generic type E
     * @param list The list to be sorted
     */
    public static <E> void sort(List<E> list) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(list, c);
    }
    
    /**
     * Sort the list using insertion sort algorithm with supplied comparator (Use it on small list only)
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
     * Sort the array using insertion sort algorithm (Use it on small array only)
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     */
    public static <E> void sort(E[] array) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(array, c);
    }
    
    /**
     * Sort the array using insertion sort algorithm with supplied comparator (Use it on small array only)
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     */
    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, comparator, 0, array.length-1);
    }
    
    /**
     * Sort porting of the array using insertion sort algorithm with supplied comparator (Helper method)
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     * @param i1 Start index
     * @param i2 End index
     */
    public static <E> void sort(E[] array, Comparator<E> comparator, int i1, int i2) {
        for (int i = i1+1; i <= i2; i++) {
            int j = i-1;
            E k = array[i];
            while (j >= i1 && comparator.compare(array[j], k) > 0) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
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
    
    public static void sort(byte[] array, int i1, int i2) {
        for (int i = i1+1; i <= i2; i++) {
            int j = i-1;
            byte k = array[i];
            while (j >= i1 && array[j] > k) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
        }
    }
    
    public static void sort(char[] array, int i1, int i2) {
        for (int i = i1+1; i <= i2; i++) {
            int j = i-1;
            char k = array[i];
            while (j >= i1 && array[j] > k) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
        }
    }
    
    public static void sort(short[] array, int i1, int i2) {
        for (int i = i1+1; i <= i2; i++) {
            int j = i-1;
            short k = array[i];
            while (j >= i1 && array[j] > k) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
        }
    }
    
    public static void sort(float[] array, int i1, int i2) {
        for (int i = i1+1; i <= i2; i++) {
            int j = i-1;
            float k = array[i];
            while (j >= i1 && array[j] > k) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
        }
    }
    
    public static void sort(int[] array, int i1, int i2) {
        for (int i = i1; i <= i2; i++) {
            int j = i-1, k = array[i];
            while (j >= i1 && array[j] > k) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
        }
    }
    
    public static void sort(long[] array, int i1, int i2) {
        for (int i = i1+1; i <= i2; i++) {
            int j = i-1;
            long k = array[i];
            while (j >= i1 && array[j] > k) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
        }
    }
    
    public static void sort(double[] array, int i1, int i2) {
        for (int i = i1+1; i <= i2; i++) {
            int j = i-1;
            double k = array[i];
            while (j >= i1 && array[j] > k) {
                array[j+1] = array[j--];
            }
            array[j+1] = k;
        }
    }
}
