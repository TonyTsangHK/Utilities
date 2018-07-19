package utils.data.sort;

import utils.data.DataComparator;
import utils.data.DataManipulator;

import java.util.Comparator;
import java.util.List;

public class SinglePivotQuickSort implements Sorter {
    public static final int INSERTION_SORT_THRESHOLD = 7;
    
    private SinglePivotQuickSort() {}
    
    /**
     * Sort the list using quicksort algorithm 
     * 
     * @param <E> generic type E
     * @param list The list to be sorted
     */
    public static <E> void sort(List<E> list) {
        Comparator<E> comparator = DataComparator.buildComparator();
        sort(list, comparator);
    }
    
    /**
     * Sort the list using quicksort algorithm with supplied comparator
     * 
     * @param list The list to be sorted
     * @param comparator Comparator for comparing purpose
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void sort(List list, Comparator comparator) {
        Object[] array = list.toArray();
        sort(array, comparator);
        DataManipulator.copyArrayToList(list, array);
    }
    
    /**
     * Sort the array using quicksort algorithm
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     */
    public static <E> void sort(E[] array) {
        sort(array, DataComparator.buildComparator());
    }
    
    /**
     * Sort the array using quicksort algorithm
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param size restricted size of the target array
     */
    public static <E> void sort(E[] array, int size) {
        sort(array, DataComparator.buildComparator(), size);
    }
    
    /**
     * Sort the array using quicksort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     */
    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, comparator, 0, array.length - 1);
    }
    
    /**
     * Sort the array using quicksort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     * @param size restricted size of the target array
     */
    public static <E> void sort(E[] array, Comparator<E> comparator, int size) {
        sort(array, comparator, 0, size-1);
    }
    
    /**
     * Sort the portion of the array using quicksort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     * @param left The leftmost index
     * @param right The rightmost index
     */
    public static <E> void sort(E[] array, Comparator<E> comparator, int left, int right) {
        sort(array, comparator, left, right, true);
    }
    
    private static <E> void sort(E[] array, Comparator<E> comparator, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, comparator, left, right);
            } else {
                // Copy from JDK7's DualPivotQuickSort, so called pair insertion sort with sentinel, is it good??
                do {
                    if (left >= right) {
                        return;
                    }
                } while (comparator.compare(array[++left], array[left - 1]) >= 0);
                
                for (int k = left; ++left <= right; k = ++left) {
                    E a1 = array[k], a2 = array[left];
                    
                    if (comparator.compare(a1, a2) < 0) {
                        a2 = a1; a1 = array[left];
                    }
                    while (comparator.compare(a1, array[--k]) < 0) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (comparator.compare(a2, array[--k]) < 0) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                E last = array[right];

                while (comparator.compare(last, array[--right])<0) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, comparator, left, j, leftmost);
            sort(array, comparator, i+1, right, false);
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
    	sort(array, left, right, true);
    }
    
    private static void sort(byte[] array, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, left, right);
            } else {
                do {
                    if (left >= right) {
                        return;
                    }
                } while (array[++left] >= array[left - 1]);
                
                for (int k = left; ++left <= right; k = ++left) {
                    byte a1 = array[k], a2 = array[left];

                    if (a1 < a2) {
                        a2 = a1; a1 = array[left];
                    }
                    while (a1 < array[--k]) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (a2 < array[--k]) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                byte last = array[right];

                while (last < array[--right]) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j);
            sort(array, i+1, right);
        }
    }
    
    public static void sort(char[] array, int left, int right) {
    	sort(array, left, right, true);
    }
    
    private static void sort(char[] array, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, left, right);
            } else {
                do {
                    if (left >= right) {
                        return;
                    }
                } while (array[++left] >= array[left - 1]);
                
                for (int k = left; ++left <= right; k = ++left) {
                    char a1 = array[k], a2 = array[left];

                    if (a1 < a2) {
                        a2 = a1; a1 = array[left];
                    }
                    while (a1 < array[--k]) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (a2 < array[--k]) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                char last = array[right];

                while (last < array[--right]) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j);
            sort(array, i+1, right);
        }
    }
    
    public static void sort(short[] array, int left, int right) {
    	sort(array, left, right, true);
    }
    
    private static void sort(short[] array, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, left, right);
            } else {
                do {
                    if (left >= right) {
                        return;
                    }
                } while (array[++left] >= array[left - 1]);
                
                for (int k = left; ++left <= right; k = ++left) {
                    short a1 = array[k], a2 = array[left];

                    if (a1 < a2) {
                        a2 = a1; a1 = array[left];
                    }
                    while (a1 < array[--k]) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (a2 < array[--k]) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                short last = array[right];

                while (last < array[--right]) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j);
            sort(array, i+1, right);
        }
    }
    
    public static void sort(float[] array, int left, int right) {
    	sort(array, left, right, true);
    }
    
    private static void sort(float[] array, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, left, right);
            } else {
                do {
                    if (left >= right) {
                        return;
                    }
                } while (array[++left] >= array[left - 1]);
                
                for (int k = left; ++left <= right; k = ++left) {
                    float a1 = array[k], a2 = array[left];

                    if (a1 < a2) {
                        a2 = a1; a1 = array[left];
                    }
                    while (a1 < array[--k]) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (a2 < array[--k]) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                float last = array[right];

                while (last < array[--right]) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j);
            sort(array, i+1, right);
        }
    }
    
    public static void sort(int[] array, int left, int right) {
    	sort(array, left, right, true);
    }
    
    private static void sort(int[] array, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, left, right);
            } else {
                do {
                    if (left >= right) {
                        return;
                    }
                } while (array[++left] >= array[left - 1]);
                
                for (int k = left; ++left <= right; k = ++left) {
                    int a1 = array[k], a2 = array[left];

                    if (a1 < a2) {
                        a2 = a1; a1 = array[left];
                    }
                    while (a1 < array[--k]) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (a2 < array[--k]) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                int last = array[right];

                while (last < array[--right]) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j);
            sort(array, i+1, right);
        }
    }
    
    public static void sort(long[] array, int left, int right) {
    	sort(array, left, right, true);
    }
    
    private static void sort(long[] array, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, left, right);
            } else {
                do {
                    if (left >= right) {
                        return;
                    }
                } while (array[++left] >= array[left - 1]);
                
                for (int k = left; ++left <= right; k = ++left) {
                    long a1 = array[k], a2 = array[left];

                    if (a1 < a2) {
                        a2 = a1; a1 = array[left];
                    }
                    while (a1 < array[--k]) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (a2 < array[--k]) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                long last = array[right];

                while (last < array[--right]) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j);
            sort(array, i+1, right);
        }
    }
    
    public static void sort(double[] array, int left, int right) {
    	sort(array, left, right, true);
    }
    
    private static void sort(double[] array, int left, int right, boolean leftmost) {
        if (right - left <= INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                InsertionSort.sort(array, left, right);
            } else {
                do {
                    if (left >= right) {
                        return;
                    }
                } while (array[++left] >= array[left - 1]);
                
                for (int k = left; ++left <= right; k = ++left) {
                    double a1 = array[k], a2 = array[left];

                    if (a1 < a2) {
                        a2 = a1; a1 = array[left];
                    }
                    while (a1 < array[--k]) {
                        array[k + 2] = array[k];
                    }
                    array[++k + 1] = a1;

                    while (a2 < array[--k]) {
                        array[k + 1] = array[k];
                    }
                    array[k + 1] = a2;
                }
                double last = array[right];

                while (last < array[--right]) {
                    array[right + 1] = array[right];
                }
                array[right + 1] = last;
            }
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
                if (j < i) {
                    break;
                }
                DataManipulator.swapData(array,i,j);
            }
            DataManipulator.swapData(array,i,right-1);
            sort(array, left, j);
            sort(array, i+1, right);
        }
    }
}
