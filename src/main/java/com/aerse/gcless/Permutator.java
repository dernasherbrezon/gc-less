package com.aerse.gcless;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Permutator<T> implements Iterable<List<T>>, Iterator<List<T>> {

	private final List<T> list;
	private final int[] c;
	private final int[] o;
	private int j;
	private List<T> next;

	Permutator(List<T> list) {
		this.list = list;
		int n = list.size();
		c = new int[n];
		o = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = 0;
			o[i] = 1;
		}
		j = Integer.MAX_VALUE;
	}

	@Override
	public boolean hasNext() {
		if (j <= 0) {
			return false;
		}
		next = new ArrayList<T>(list);
		calculateNextPermutation();
		return true;
	}

	@Override
	public List<T> next() {
		return next;
	}

	@Override
	public void remove() {
		// not implemented
	}

	void calculateNextPermutation() {
		j = list.size() - 1;
		int s = 0;

		// Handle the special case of an empty list. Skip the calculation of
		// the
		// next permutation.
		if (j == -1) {
			return;
		}

		while (true) {
			int q = c[j] + o[j];
			if (q < 0) {
				switchDirection();
				continue;
			}
			if (q == j + 1) {
				if (j == 0) {
					break;
				}
				s++;
				switchDirection();
				continue;
			}

			int index = j - c[j] + s;
			list.set(index, list.set(j - q + s, list.get(index)));
			c[j] = q;
			break;
		}
	}

	private void switchDirection() {
		o[j] = -o[j];
		j--;
	}

	@Override
	public Iterator<List<T>> iterator() {
		return this;
	}

}
