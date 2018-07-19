package utils.data.sort;

import utils.data.DataComparator;
import utils.data.DataManipulator;
import utils.math.MathUtil;

import java.util.Comparator;
import java.util.List;

public class IntroSort implements Sorter {
    public static final int INSERTION_SORT_THRESHOLD = 7;
    
    private IntroSort() {}
    
    /**
     * Sort the list using intorsort algorithm
     * 
     * @param <E> Generic type E
     * @param list The list to be sorted
     */
    public static <E> void sort(List<E> list) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(list, c);
    }
    
    /**
     * Sort the list using introsort algorithm with supplied comparator
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
     * Sort the array using introsort algorithm
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     */
    public static <E> void sort(E[] array) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(array, c);
    }
    
    /**
     * Sort the array using introsort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     */
    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, comparator, 0, array.length-1, 0, (int)(Math.ceil(MathUtil.log(array.length, 2))));
    }
    
    /**
     * Sort portion of the array using introsort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     * @param left Start index
     * @param right End index
     */
    public static <E> void sort(E[] array, Comparator<E> comparator, int left, int right) {
    	sort(array, comparator, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    /**
     * Sort portion of the array using introsort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     * @param left Start index
     * @param right End index
     * @param r Current recursion count
     * @param max Maximum recursion before changing to heapsort
     */
    private static <E> void sort(
            E[] array, Comparator<E> comparator, int left, int right, int r, int max
    ) {
        if (r > max) {
            HeapSort.sort(array, comparator, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, comparator, left, right);
        } else {
            int i = (left+right) / 2;
            if (comparator.compare(array[left], array[i]) > 0) {
                DataManipulator.swapData(array, left, i);
            }
            if (comparator.compare(array[left], array[right]) > 0) {
                DataManipulator.swapData(array, left, right);
            }
            if (comparator.compare(array[i], array[right]) > 0) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            E pivotValue = array[j];
            
            while (true) {
                while (comparator.compare(array[++i], pivotValue) < 0);
                while (comparator.compare(array[--j], pivotValue) > 0);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, comparator, left, j, r+1, max);
            sort(array, comparator, i+1, right, r+1, max);
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
    	sort(array, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    public static void sort(char[] array, int left, int right) {
    	sort(array, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    public static void sort(short[] array, int left, int right) {
    	sort(array, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    public static void sort(float[] array, int left, int right) {
    	sort(array, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    public static void sort(int[] array, int left, int right) {
    	sort(array, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    public static void sort(long[] array, int left, int right) {
    	sort(array, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    public static void sort(double[] array, int left, int right) {
    	sort(array, left, right, 0, (int)(Math.ceil(MathUtil.log(right-left+1, 2))));
    }
    
    public static void sort(byte[] array, int left, int right, int r, int max) {
    	if (r > max) {
            HeapSort.sort(array, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, left, right);
        } else {
            int i = (left+right) / 2;
            if (array[left] > array[i]) {
                DataManipulator.swapData(array, left, i);
            }
            if (array[left] > array[right]) {
                DataManipulator.swapData(array, left, right);
            }
            if (array[i] > array[right]) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            byte pivotValue = array[j];
            
            while (true) {
                while (array[++i] < pivotValue);
                while (array[--j] > pivotValue);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j, r+1, max);
            sort(array, i+1, right, r+1, max);
        }
    }
    
    public static void sort(char[] array, int left, int right, int r, int max) {
    	if (r > max) {
            HeapSort.sort(array, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, left, right);
        } else {
            int i = (left+right) / 2;
            if (array[left] > array[i]) {
                DataManipulator.swapData(array, left, i);
            }
            if (array[left] > array[right]) {
                DataManipulator.swapData(array, left, right);
            }
            if (array[i] > array[right]) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            char pivotValue = array[j];
            
            while (true) {
                while (array[++i] < pivotValue);
                while (array[--j] > pivotValue);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j, r+1, max);
            sort(array, i+1, right, r+1, max);
        }
    }
    
    public static void sort(short[] array, int left, int right, int r, int max) {
    	if (r > max) {
            HeapSort.sort(array, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, left, right);
        } else {
            int i = (left+right) / 2;
            if (array[left] > array[i]) {
                DataManipulator.swapData(array, left, i);
            }
            if (array[left] > array[right]) {
                DataManipulator.swapData(array, left, right);
            }
            if (array[i] > array[right]) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            short pivotValue = array[j];
            
            while (true) {
                while (array[++i] < pivotValue);
                while (array[--j] > pivotValue);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j, r+1, max);
            sort(array, i+1, right, r+1, max);
        }
    }
    
    public static void sort(float[] array, int left, int right, int r, int max) {
    	if (r > max) {
            HeapSort.sort(array, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, left, right);
        } else {
            int i = (left+right) / 2;
            if (array[left] > array[i]) {
                DataManipulator.swapData(array, left, i);
            }
            if (array[left] > array[right]) {
                DataManipulator.swapData(array, left, right);
            }
            if (array[i] > array[right]) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            float pivotValue = array[j];
            
            while (true) {
                while (array[++i] < pivotValue);
                while (array[--j] > pivotValue);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j, r+1, max);
            sort(array, i+1, right, r+1, max);
        }
    }
    
    public static void sort(int[] array, int left, int right, int r, int max) {
    	if (r > max) {
            HeapSort.sort(array, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, left, right);
        } else {
            int i = (left+right) / 2;
            if (array[left] > array[i]) {
                DataManipulator.swapData(array, left, i);
            }
            if (array[left] > array[right]) {
                DataManipulator.swapData(array, left, right);
            }
            if (array[i] > array[right]) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            int pivotValue = array[j];
            
            while (true) {
                while (array[++i] < pivotValue);
                while (array[--j] > pivotValue);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j, r+1, max);
            sort(array, i+1, right, r+1, max);
        }
    }
    
    public static void sort(long[] array, int left, int right, int r, int max) {
    	if (r > max) {
            HeapSort.sort(array, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, left, right);
        } else {
            int i = (left+right) / 2;
            if (array[left] > array[i]) {
                DataManipulator.swapData(array, left, i);
            }
            if (array[left] > array[right]) {
                DataManipulator.swapData(array, left, right);
            }
            if (array[i] > array[right]) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            long pivotValue = array[j];
            
            while (true) {
                while (array[++i] < pivotValue);
                while (array[--j] > pivotValue);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j, r+1, max);
            sort(array, i+1, right, r+1, max);
        }
    }
    
    public static void sort(double[] array, int left, int right, int r, int max) {
    	if (r > max) {
            HeapSort.sort(array, left, right);
        } else if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(array, left, right);
        } else {
            int i = (left+right) / 2;
            if (array[left] > array[i]) {
                DataManipulator.swapData(array, left, i);
            }
            if (array[left] > array[right]) {
                DataManipulator.swapData(array, left, right);
            }
            if (array[i] > array[right]) {
                DataManipulator.swapData(array, i, right);
            }
            
            int j = right - 1;
            DataManipulator.swapData(array, i, j);
            i=left;
            double pivotValue = array[j];
            
            while (true) {
                while (array[++i] < pivotValue);
                while (array[--j] > pivotValue);
                if (j < i) break;
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j, r+1, max);
            sort(array, i+1, right, r+1, max);
        }
    }
}
