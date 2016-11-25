package com.aerse.gcless;

import java.util.Iterator;
import java.util.List;

public class Lists {

	public static <T> Iterator<List<T>> permutate(List<T> data) {
		return new Permutator<T>(data);
	}
	
}
