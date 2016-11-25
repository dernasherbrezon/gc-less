package com.aerse.gcless.io;

import java.io.IOException;

public class LimitReachedException extends IOException {

	private static final long serialVersionUID = 8256498979409567602L;

	public LimitReachedException(String message) {
		super(message);
	}
}
