package com.smilanko.smartbelt.data.grapher.fileutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataGrapherFileReader {
	
	private DataGrapherFileReader() {
		// singleton
	}
	
	public static List<String> readLines(final File fileToRead) throws Exception {
		final List<String> lines = new ArrayList<String>();
		final BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			reader.close();
		}
		return lines;
	}
	

}
