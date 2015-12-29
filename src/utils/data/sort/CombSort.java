package utils.data.sort;

import utils.data.DataManipulator;

public class CombSort implements Sorter {
	public CombSort() {}
	
	private static int g(int g) {
		g = g * 10 / 13;
		if (g < 1) {
			g = 1;
		}
		if (g == 9 || g == 10) {
			g = 11;
		}
		return g;
	}
	
	public static void sort(int[] a) {
		int g = a.length;
		
		while (true) {
			g = g(g);
			boolean changed = false;
			for (int i = 0; i < a.length - g; i++) {
				if (a[i] > a[i+g]) {
					DataManipulator.swapData(a, i, i+g);
					changed = true;
				}
			}
			if (g == 1 && !changed) {
				break;
			}
		}
	}
}
