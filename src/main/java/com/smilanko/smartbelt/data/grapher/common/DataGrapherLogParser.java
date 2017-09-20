package com.smilanko.smartbelt.data.grapher.common;

public class DataGrapherLogParser {

	private DataGrapherLogParser() {
		// singleton
	}

	public static int retreiveSampleFromNotesLine(final String notesLine) {
		return Integer.valueOf(notesLine.split(",")[1]);
	}

	public static int retreiveSampleFromLogFile(final String logLine) {
		return Integer.valueOf(logLine.split(",")[2]);
	}

	public static double retrieveCapacitanceFromLogFile(final String logLine) {
		return Double.valueOf(logLine.split(",")[0]);
	}

}
