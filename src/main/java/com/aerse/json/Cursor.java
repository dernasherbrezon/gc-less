package com.aerse.json;

public class Cursor {

	private int currentPosition = 0;
	private int processUpToPosition = -1;
	private boolean endOfStreamReached = false;
	private boolean nullObject = false;
	private char lastStopChar = 0;

	public boolean isNullObject() {
		return nullObject;
	}

	public void setNullObject(boolean nullObject) {
		this.nullObject = nullObject;
	}

	public void setLastStopChar(char lastStopChar) {
		this.lastStopChar = lastStopChar;
	}

	public char getLastStopChar() {
		return lastStopChar;
	}

	public boolean isEndOfStreamReached() {
		return endOfStreamReached;
	}

	public void setEndOfStreamReached(boolean endOfStreamReached) {
		this.endOfStreamReached = endOfStreamReached;
	}

	public void addToPosition(int bytes) {
		currentPosition += bytes;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public int getProcessUpToPosition() {
		return processUpToPosition;
	}

	public void setProcessUpToPosition(int processUpToPosition) {
		this.processUpToPosition = processUpToPosition;
	}

}
