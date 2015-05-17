package com.google.code.gcless;

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

}
