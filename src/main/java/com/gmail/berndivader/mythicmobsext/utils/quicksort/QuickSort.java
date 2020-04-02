package com.gmail.berndivader.mythicmobsext.utils.quicksort;

public class QuickSort {
	static int part(QuickSortPair[] arr, int low, int high) {
		QuickSortPair pivot = arr[high];
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (arr[j].value <= pivot.value) {
				i++;
				QuickSortPair temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		QuickSortPair temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;

		return i + 1;
	}

	public static QuickSortPair[] sort(QuickSortPair[] arr, int low, int high) {
		if (low < high) {
			int pi = part(arr, low, high);
			sort(arr, low, pi - 1);
			sort(arr, pi + 1, high);
		}
		return arr;
	}

}
