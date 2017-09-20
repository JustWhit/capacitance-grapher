package com.smilanko.smartbelt.data.grapher.validationutils;

import java.util.List;

import com.smilanko.smartbelt.data.grapher.common.DataGrapherConstants;
import com.smilanko.smartbelt.data.grapher.common.DataGrapherLogParser;

public class DataGrapherValidaitonUtils {

	private DataGrapherValidaitonUtils() {
		// singleton
	}

	public static void validateNotesFile(final List<String> fileLines) {
		// ensure that lines exist
		if (fileLines.isEmpty()) {
			System.out.println("### empty notes file detected");
			System.exit(-1);
		}

		// ensure that the lines have the correct ending statements
		for (final String line : fileLines) {
			if (!line.endsWith(DataGrapherConstants.SET_START) && !line.endsWith(DataGrapherConstants.SET_END)
					&& !line.endsWith(DataGrapherConstants.LIFT_START) && !line.endsWith(DataGrapherConstants.LIFT_PEAK)
					&& !line.endsWith(DataGrapherConstants.LIFT_REST)) {
				System.out.println("### line has an unexpected ending: " + line);
				System.exit(-1);
			}
		}

		// ensure that the lines follow the set start and set end features
		for (int i = 0; i < fileLines.size(); i++) {
			final String currentLine = fileLines.get(i);
			if (currentLine.endsWith(DataGrapherConstants.SET_END)) {
				// verify that the previous line was a lift rest
				final String previousLine = fileLines.get(i - 1);
				if (!previousLine.endsWith(DataGrapherConstants.LIFT_REST)) {
					errorOutDueToPreviousLine(DataGrapherConstants.SET_END, previousLine);
				}
			} else if (currentLine.endsWith(DataGrapherConstants.SET_START)) {
				if (i != 0) {
					// verify that the previous line is an end of a set
					final String previousLine = fileLines.get(i - 1);
					if (!previousLine.endsWith(DataGrapherConstants.SET_END)) {
						errorOutDueToPreviousLine(DataGrapherConstants.SET_START, previousLine);
					}
				}
			} else {
				continue;
			}
		}

		// ensure that the lines for lifting are consistent
		for (int i = 0; i < fileLines.size(); i++) {
			final String currentLine = fileLines.get(i);
			if (currentLine.endsWith(DataGrapherConstants.LIFT_START)) {
				// ensure previous lines are either SET_START or LIFT REST
				final String previousLine = fileLines.get(i - 1);
				if (!previousLine.endsWith(DataGrapherConstants.SET_START)
						&& !previousLine.endsWith(DataGrapherConstants.LIFT_REST)) {
					errorOutDueToPreviousLine(DataGrapherConstants.LIFT_START, previousLine);
				}
			} else if (currentLine.endsWith(DataGrapherConstants.LIFT_PEAK)) {
				// ensure that the previous lines is LIFT_START
				final String previousLine = fileLines.get(i - 1);
				if (!previousLine.endsWith(DataGrapherConstants.LIFT_START)) {
					errorOutDueToPreviousLine(DataGrapherConstants.LIFT_PEAK, previousLine);
				}
			} else if (currentLine.endsWith(DataGrapherConstants.LIFT_REST)) {
				// ensure that the previous lines is LIFT_START
				final String previousLine = fileLines.get(i - 1);
				if (!previousLine.endsWith(DataGrapherConstants.LIFT_PEAK)) {
					errorOutDueToPreviousLine(DataGrapherConstants.LIFT_REST, previousLine);
				}
			}
		}

		// ensure that the sample increases and is in sync
		int previousSample = -1;
		for (int i = 0; i < fileLines.size(); i++) {
			final int currentSample = DataGrapherLogParser.retreiveSampleFromNotesLine(fileLines.get(i));
			if (currentSample < previousSample) {
				System.out.println("### samples are not in order");
				System.exit(-1);
			} else {
				previousSample = currentSample;
			}
		}

	}

	public static void validateLogFile(final List<String> fileLines) {
		// ensure that lines exist
		if (fileLines.isEmpty()) {
			System.out.println("### empty log file detected");
			System.exit(-1);
		}

		// ensure that the first line is a header
		if (fileLines.get(0).matches(".*\\d+.*")) {
			System.out.println("### the first line is not a header, as it contains numbers");
			System.exit(-1);
		}

		// ensure the order of samples
		for (int i = 1; i < fileLines.size(); i++) {
			final int sample = DataGrapherLogParser.retreiveSampleFromLogFile(fileLines.get(i));
			if (sample != (i - 1)) {
				System.out.println("### the log file is not in order on sample line : " + (i - 1));
				System.exit(-1);
			}
		}
	}

	public static void validateNotesAndLogDataInSync(final List<String> notesLines, final List<String> logLines) {
		for (final String noteLine : notesLines) {
			final int noteSample = DataGrapherLogParser.retreiveSampleFromNotesLine(noteLine);
			boolean wasFound = false;
			for (int i = 1; i < logLines.size(); i++) {
				final int logSample = DataGrapherLogParser.retreiveSampleFromLogFile(logLines.get(i));
				if (noteSample == logSample) {
					wasFound = true;
					break;
				}
			}
			if (!wasFound) {
				System.out.println("## unable to find sample " + noteSample + " in the log file");
				System.exit(-1);
			}
		}
	}

	

	private static void errorOutDueToPreviousLine(final String currentEnding, final String previousLine) {
		System.out.println("### Not expecting this line before a " + currentEnding + " : " + previousLine);
		System.exit(-1);
	}

}
