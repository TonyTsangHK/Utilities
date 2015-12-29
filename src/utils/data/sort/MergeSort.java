package utils.data.sort;

import java.util.Comparator;
import java.util.List;

import utils.data.DataComparator;
import utils.data.DataManipulator;

public class MergeSort implements Sorter {
    public static final int INSERTION_SORT_THRESHOLD = 7;
    
    private MergeSort() {}
    
    /**
     * Sort the list using mergesort algorithm
     * 
     * @param <E> Generic type E
     * @param list The list to be sorted
     */
    public static <E> void sort(List<E> list) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(list, c);
    }
    
    /**
     * Sort the list using mergesort algorithm with supplied comparator
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
     * Sort the array using mergesort algorithm
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     */
    public static <E> void sort(E[] array) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(array, c);
    }
    
    /**
     * Sort the array using mergesort algorithm
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param size restricted size of the target array
     */
    public static <E> void sort(E[] array, int size) {
        Comparator<E> c = DataComparator.buildComparator();
        sort(array, c, size);
    }
    
    /**
     * Sort the array using mergesort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     */
    public static <E> void sort(E[] array, Comparator<E> comparator) {
        E[] tmp = (E[])array.clone();
        sort(tmp, array, comparator, 0, array.length-1);
    }
    
    /**
     * Sort the array using mergesort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param array The array to be sorted
     * @param comparator Comparator for comparing purpose
     * @param size restricted size of the target array
     */
    public static <E> void sort(E[] array, Comparator<E> comparator, int size) {
        E[] temp = (E[]) array.clone();
        sort(temp, array, comparator, 0, size-1);
    }
    
    /**
     * Sort porting of the array using mergesort algorithm with supplied comparator
     * 
     * @param <E> Generic type E
     * @param src Source array for holding sorted elements
     * @param dest Target array for merging
     * @param comparator Comparator for comparing purpose
     * @param i1 The start index of the array
     * @param i2 The end index of the array
     */
    private static <E> void sort(E[] src, E[] dest, Comparator<E> comparator, int i1, int i2) {
        int length = i2 - i1 + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, comparator, i1, i2);
        } else {
            int mid = (i2+i1)/2;
            sort(dest, src, comparator, i1, mid);
            sort(dest, src, comparator, mid+1, i2);
            if (comparator.compare(src[mid], src[mid+1]) <= 0) {
                System.arraycopy(src, i1, dest, i1, length);
            } else if (comparator.compare(src[i1], src[i2]) >= 0) {
                System.arraycopy(src, mid+1, dest, i1, i2-mid);
                System.arraycopy(src, i1, dest, i1+i2-mid, mid-i1+1);
            } else {
                for (int i = i1, s1 = i1, s2 = mid+1; i <= i2; i++) {
                    if (s2>i2||s1<=mid&&comparator.compare(src[s1], src[s2])<=0) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
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
    	sort(array.clone(), array, left, right);
    }
    
    public static void sort(char[] array, int left, int right) {
    	sort(array.clone(), array, left, right);
    }
    
    public static void sort(short[] array, int left, int right) {
    	sort(array.clone(), array, left, right);
    }
    
    public static void sort(float[] array, int left, int right) {
    	sort(array.clone(), array, left, right);
    }
    
    public static void sort(int[] array, int left, int right) {
    	int[] temp = array.clone();
    	sort(temp, array, left, right);
    }
    
    public static void sort(long[] array, int left, int right) {
    	sort(array.clone(), array, left, right);
    }
    
    public static void sort(double[] array, int left, int right) {
    	sort(array.clone(), array, left, right);
    }
    
    private static void sort(byte[] src, byte[] dest, int left, int right) {
    	int length = right - left + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, left, right);
        } else {
            int mid = (right+left)/2;
            sort(dest, src, left, mid);
            sort(dest, src, mid+1, right);
            if (src[mid] <= src[mid+1]) {
                System.arraycopy(src, left, dest, left, length);
            } else if (src[left] >= src[right]) {
                System.arraycopy(src, mid+1, dest, left, right-mid);
                System.arraycopy(src, left, dest, left+right-mid, mid-left+1);
            } else {
                for (int i = left, s1 = left, s2 = mid+1; i <= right; i++) {
                    if (s2 > right || s1 <= mid && src[s1] <= src[s2]) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
            }
        }
    }
    
    private static void sort(char[] src, char[] dest, int left, int right) {
    	int length = right - left + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, left, right);
        } else {
            int mid = (right+left)/2;
            sort(dest, src, left, mid);
            sort(dest, src, mid+1, right);
            if (src[mid] <= src[mid+1]) {
                System.arraycopy(src, left, dest, left, length);
            } else if (src[left] >= src[right]) {
                System.arraycopy(src, mid+1, dest, left, right-mid);
                System.arraycopy(src, left, dest, left+right-mid, mid-left+1);
            } else {
                for (int i = left, s1 = left, s2 = mid+1; i <= right; i++) {
                    if (s2 > right || s1 <= mid && src[s1] <= src[s2]) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
            }
        }
    }
    
    private static void sort(short[] src, short[] dest, int left, int right) {
    	int length = right - left + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, left, right);
        } else {
            int mid = (right+left)/2;
            sort(dest, src, left, mid);
            sort(dest, src, mid+1, right);
            if (src[mid] <= src[mid+1]) {
                System.arraycopy(src, left, dest, left, length);
            } else if (src[left] >= src[right]) {
                System.arraycopy(src, mid+1, dest, left, right-mid);
                System.arraycopy(src, left, dest, left+right-mid, mid-left+1);
            } else {
                for (int i = left, s1 = left, s2 = mid+1; i <= right; i++) {
                    if (s2 > right || s1 <= mid && src[s1] <= src[s2]) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
            }
        }
    }
    
    private static void sort(float[] src, float[] dest, int left, int right) {
    	int length = right - left + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, left, right);
        } else {
            int mid = (right+left)/2;
            sort(dest, src, left, mid);
            sort(dest, src, mid+1, right);
            if (src[mid] <= src[mid+1]) {
                System.arraycopy(src, left, dest, left, length);
            } else if (src[left] >= src[right]) {
                System.arraycopy(src, mid+1, dest, left, right-mid);
                System.arraycopy(src, left, dest, left+right-mid, mid-left+1);
            } else {
                for (int i = left, s1 = left, s2 = mid+1; i <= right; i++) {
                    if (s2 > right || s1 <= mid && src[s1] <= src[s2]) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
            }
        }
    }
    
    private static void sort(int[] src, int[] dest, int left, int right) {
    	int length = right - left + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, left, right);
        } else {
            int mid = (right+left)/2;
            sort(dest, src, left, mid);
            sort(dest, src, mid+1, right);
            if (src[mid] <= src[mid+1]) {
                System.arraycopy(src, left, dest, left, length);
            } else if (src[left] >= src[right]) {
                System.arraycopy(src, mid+1, dest, left, right-mid);
                System.arraycopy(src, left, dest, left+right-mid, mid-left+1);
            } else {
                for (int i = left, s1 = left, s2 = mid+1; i <= right; i++) {
                    if (s2 > right || s1 <= mid && src[s1] <= src[s2]) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
            }
        }
    }
    
    private static void sort(long[] src, long[] dest, int left, int right) {
    	int length = right - left + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, left, right);
        } else {
            int mid = (right+left)/2;
            sort(dest, src, left, mid);
            sort(dest, src, mid+1, right);
            if (src[mid] <= src[mid+1]) {
                System.arraycopy(src, left, dest, left, length);
            } else if (src[left] >= src[right]) {
                System.arraycopy(src, mid+1, dest, left, right-mid);
                System.arraycopy(src, left, dest, left+right-mid, mid-left+1);
            } else {
                for (int i = left, s1 = left, s2 = mid+1; i <= right; i++) {
                    if (s2 > right || s1 <= mid && src[s1] <= src[s2]) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
            }
        }
    }
    
    private static void sort(double[] src, double[] dest, int left, int right) {
    	int length = right - left + 1;
        
        if (length < INSERTION_SORT_THRESHOLD) {
            InsertionSort.sort(dest, left, right);
        } else {
            int mid = (right+left)/2;
            sort(dest, src, left, mid);
            sort(dest, src, mid+1, right);
            if (src[mid] <= src[mid+1]) {
                System.arraycopy(src, left, dest, left, length);
            } else if (src[left] >= src[right]) {
                System.arraycopy(src, mid+1, dest, left, right-mid);
                System.arraycopy(src, left, dest, left+right-mid, mid-left+1);
            } else {
                for (int i = left, s1 = left, s2 = mid+1; i <= right; i++) {
                    if (s2 > right || s1 <= mid && src[s1] <= src[s2]) {
                        dest[i] = src[s1++];
                    } else {
                        dest[i] = src[s2++];
                    }
                }
            }
        }
    }
}
