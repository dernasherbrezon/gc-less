package com.aerse.gcless;

import java.io.File;

public class FileUtils {

	public static File initDir(String dir) {
		File tempDirFile = new File(dir);
		if (tempDirFile.exists() && !tempDirFile.isDirectory()) {
			throw new IllegalStateException("is not a directory: " + tempDirFile.getAbsolutePath());
		}
		if (!tempDirFile.exists() && !tempDirFile.mkdirs()) {
			throw new IllegalStateException("unable to create: " + tempDirFile.getAbsolutePath());
		}
		return tempDirFile;
	}

	public static long getNumberOfFiles(File baseDirectory) {
		if (baseDirectory.isFile()) {
			return 1;
		}
		long result = 0;
		File[] contents = baseDirectory.listFiles();
		for (File cur : contents) {
			result += getNumberOfFiles(cur);
		}
		return result;
	}
	
}
