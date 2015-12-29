package utils.data.sort;

import java.util.Comparator;
import java.util.List;

import utils.data.DataComparator;
import utils.data.DataManipulator;

public class HeapSort implements Sorter {
    private HeapSort() {}
    
    /**
     * Sort the list using heapsort algorithm
     * 
     * @param <E> Generic type E
     * @param list The list to be sorted
     */
    public static <E> void sort(List<E> list) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(list, c);
    }
    
    /**
     * Sort the list using heapsort algorithm with supplied comparator
     * 
     * @param list The list to sorted
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
     * Sort the array using heapsort algorithm
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     */
    public static <E> void sort(E[] array) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(array, c);
    }
    
    /**
     * Sort the array using heapsort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     */
    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, comparator, 0, array.length-1);
    }
    
    /**
     * Sort porting of the array using heapsort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     * @param i1 Start index
     * @param i2 End index
     */
    public static <E> void sort(E[] array, Comparator<E> comparator, int i1, int i2) {
        heapify(array, comparator, i1, i2);
        
        int end = i2;
        while (end > i1) {
            DataManipulator.swapData(array, end, i1);
            end -= 1;
            siftDown(array, comparator, i1, i1, end);
        }
    }
    
    /**
     * Construct a valid max heap for the array (Helper method of heapsort)
     * 
     * @param <E> Generic type E
     * @param array The target array
     * @param i1 Start index
     * @param i2 End index
     * @param comparator Comparator for comparing purpose
     */
    private static <E> void heapify(E[] array, Comparator<E> comparator, int i1, int i2) {
        int start = (i1 + i2) / 2;
        
        while (start >= i1) {
            siftDown(array, comparator, i1, start, i2);
            start -= 1;
        }
    }
    
    /**
     * Ensure parents of the heap is always greater than their children (Helper method of heapsort) 
     * 
     * @param <E> Generic type E
     * @param array The target array
     * @param offset Offset of the start position
     * @param start Start index
     * @param end End index
     * @param comparator Comparator for comparing purpose
     */
    private static <E> void siftDown(E[] array, Comparator<E> comparator, int offset, int start, int end) {
        int root = start, child = (root-offset)*2+1+offset;
        while (child <= end) {
            if (child+1<=end && comparator.compare(array[child], array[child+1])<0) {
                child = child+1;
            }
            if (comparator.compare(array[root], array[child])<0) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
            }
        }
    }
    
    private static <E> void heapify(byte[] array, int left, int right) {
        int start = (left + right) / 2;
        
        while (start >= left) {
            siftDown(array, left, start, right);
            start -= 1;
        }
    }
    
    private static <E> void heapify(char[] array, int left, int right) {
        int start = (left + right) / 2;
        
        while (start >= left) {
            siftDown(array, left, start, right);
            start -= 1;
        }
    }
    
    private static <E> void heapify(short[] array, int left, int right) {
        int start = (left + right) / 2;
        
        while (start >= left) {
            siftDown(array, left, start, right);
            start -= 1;
        }
    }
    
    private static <E> void heapify(float[] array, int left, int right) {
        int start = (left + right) / 2;
        
        while (start >= left) {
            siftDown(array, left, start, right);
            start -= 1;
        }
    }
    
    private static <E> void heapify(int[] array, int left, int right) {
        int start = (left + right) / 2;
        
        while (start >= left) {
            siftDown(array, left, start, right);
            start -= 1;
        }
    }
    
    private static <E> void heapify(long[] array, int left, int right) {
        int start = (left + right) / 2;
        
        while (start >= left) {
            siftDown(array, left, start, right);
            start -= 1;
        }
    }
    
    private static <E> void heapify(double[] array, int left, int right) {
        int start = (left + right) / 2;
        
        while (start >= left) {
            siftDown(array, left, start, right);
            start -= 1;
        }
    }
    
    private static void siftDown(byte[] array, int offset, int left, int right) {
    	int root = left, child = (root-offset)*2+1+offset;
        while (child <= right) {
            if (child+1 <= right && array[child] < array[child+1]) {
                child = child+1;
            }
            if (array[root] < array[child]) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
            }
        }
    }
    
    private static void siftDown(char[] array, int offset, int left, int right) {
    	int root = left, child = (root-offset)*2+1+offset;
        while (child <= right) {
            if (child+1 <= right && array[child] < array[child+1]) {
                child = child+1;
            }
            if (array[root] < array[child]) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
            }
        }
    }

    private static void siftDown(short[] array, int offset, int left, int right) {
    	int root = left, child = (root-offset)*2+1+offset;
        while (child <= right) {
            if (child+1 <= right && array[child] < array[child+1]) {
                child = child+1;
            }
            if (array[root] < array[child]) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
            }
        }
    }
    
    private static void siftDown(float[] array, int offset, int left, int right) {
    	int root = left, child = (root-offset)*2+1+offset;
        while (child <= right) {
            if (child+1 <= right && array[child] < array[child+1]) {
                child = child+1;
            }
            if (array[root] < array[child]) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
            }
        }
    }
    
    private static void siftDown(int[] array, int offset, int left, int right) {
    	int root = left, child = (root-offset)*2+1+offset;
        while (child <= right) {
            if (child+1 <= right && array[child] < array[child+1]) {
                child = child+1;
            }
            if (array[root] < array[child]) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
            }
        }
    }
    
    private static void siftDown(long[] array, int offset, int left, int right) {
    	int root = left, child = (root-offset)*2+1+offset;
        while (child <= right) {
            if (child+1 <= right && array[child] < array[child+1]) {
                child = child+1;
            }
            if (array[root] < array[child]) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
            }
        }
    }
    
    private static void siftDown(double[] array, int offset, int left, int right) {
    	int root = left, child = (root-offset)*2+1+offset;
        while (child <= right) {
            if (child+1 <= right && array[child] < array[child+1]) {
                child = child+1;
            }
            if (array[root] < array[child]) {
                DataManipulator.swapData(array, root, child);
                root = child;
                child = (root-offset)*2+1+offset;
            } else {
                return;
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
    	heapify(array, left, right);
        
        int end = right;
        while (end > left) {
            DataManipulator.swapData(array, end, left);
            end -= 1;
            siftDown(array, left, left, end);
        }
    }
    
    public static void sort(char[] array, int left, int right) {
    	heapify(array, left, right);
        
        int end = right;
        while (end > left) {
            DataManipulator.swapData(array, end, left);
            end -= 1;
            siftDown(array, left, left, end);
        }
    }
    
    public static void sort(short[] array, int left, int right) {
    	heapify(array, left, right);
        
        int end = right;
        while (end > left) {
            DataManipulator.swapData(array, end, left);
            end -= 1;
            siftDown(array, left, left, end);
        }
    }
    
    public static void sort(float[] array, int left, int right) {
    	heapify(array, left, right);
        
        int end = right;
        while (end > left) {
            DataManipulator.swapData(array, end, left);
            end -= 1;
            siftDown(array, left, left, end);
        }
    }
    
    public static void sort(int[] array, int left, int right) {
    	heapify(array, left, right);
        
        int end = right;
        while (end > left) {
            DataManipulator.swapData(array, end, left);
            end -= 1;
            siftDown(array, left, left, end);
        }
    }
    
    public static void sort(long[] array, int left, int right) {
    	heapify(array, left, right);
        
        int end = right;
        while (end > left) {
            DataManipulator.swapData(array, end, left);
            end -= 1;
            siftDown(array, left, left, end);
        }
    }
    
    public static void sort(double[] array, int left, int right) {
    	heapify(array, left, right);
        
        int end = right;
        while (end > left) {
            DataManipulator.swapData(array, end, left);
            end -= 1;
            siftDown(array, left, left, end);
        }
    }
}
