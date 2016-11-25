package com.aerse.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class LinkedQueueSet<T> {

	private final LinkedList<T> queue = new LinkedList<>();
	private final Set<T> set = new HashSet<>();

	public void add(T t) {
		if (set.add(t)) {
			queue.addLast(t);
		}
	}

	public void addAll(Iterable<? extends T> c) {
		for (T cur : c) {
			add(cur);
		}
	}

	public T poll() {
		if (queue.isEmpty()) {
			return null;
		}
		T result = queue.removeFirst();
		set.remove(result);
		return result;
	}

}
