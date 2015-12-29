package utils.data.sort;

public class ShellSort implements Sorter {
	public static void sort(int[] a )
    {
        for( int gap = a.length / 2; gap > 0;
                     gap = gap == 2 ? 1 : (int) ( gap / 2.2 ) )
            for( int i = gap; i < a.length; i++ )
            {
                int tmp = a[i];
                int j = i;

                for( ; j >= gap && tmp < a[ j - gap ]; j -= gap )
                    a[ j ] = a[ j - gap ];
                a[ j ] = tmp;
            }
    }
}
