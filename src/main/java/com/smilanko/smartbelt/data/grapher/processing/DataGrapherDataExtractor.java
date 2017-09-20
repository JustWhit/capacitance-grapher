package com.smilanko.smartbelt.data.grapher.processing;

import java.util.ArrayList;
import java.util.List;

import com.smilanko.smartbelt.data.grapher.common.DataGrapherConstants;
import com.smilanko.smartbelt.data.grapher.common.DataGrapherLogParser;
import com.smilanko.smartbelt.data.grapher.domain.BenchSet;
import com.smilanko.smartbelt.data.grapher.domain.LiftPeakToRest;
import com.smilanko.smartbelt.data.grapher.domain.LiftStartToPeak;

public class DataGrapherDataExtractor {

	private DataGrapherDataExtractor() {
		// singleton
	}

	public static List<BenchSet> retreiveBenchSets(final List<String> notesLines, final List<String> logLines) {
		final List<BenchSet> benchSets = new ArrayList<BenchSet>();

		// look for the bench set start
		for (int i = 0; i < notesLines.size(); i++) {
			final String setStartLine = notesLines.get(i);
			final BenchSet benchSet = new BenchSet();
			if (setStartLine.endsWith(DataGrapherConstants.SET_START)) {
				benchSet.setSetStartSample(DataGrapherLogParser.retreiveSampleFromNotesLine(setStartLine));
				// loop again to find when the bench set ends
				for (int reLooper = i; reLooper <= notesLines.size(); reLooper++) {
					final String setEndLine = notesLines.get(reLooper);
					if (setEndLine.endsWith(DataGrapherConstants.SET_END)) {
						benchSet.setSetEndSample(DataGrapherLogParser.retreiveSampleFromNotesLine(setEndLine));
						i = reLooper;
						break;
					}
				}
				if (benchSet.getSetEndSample() == 0) {
					System.out.println("### unable to find an ending of a bech set: " + benchSet.getSetStartSample());
					System.exit(-1);
				}
				benchSets.add(benchSet);
			}
		}

		// for each bench, load the lift start and lift peak
		for (final BenchSet benchSet : benchSets) {
			final int sampleStart = benchSet.getSetStartSample();
			final int sampleEnd = benchSet.getSetEndSample();
			for (int i = 0; i < notesLines.size(); i++) {
				final String currentLine = notesLines.get(i);
				final int currentSample = DataGrapherLogParser.retreiveSampleFromNotesLine(currentLine);
				if (currentLine.endsWith(DataGrapherConstants.LIFT_START)) {
					if (currentSample >= sampleStart && currentSample <= sampleEnd) {
						// add a lift start to peak entry
						final LiftStartToPeak liftStartToPeak = new LiftStartToPeak();
						liftStartToPeak.setLiftStart(currentSample);
						// set the next value as the lift peak
						final int nextSample = DataGrapherLogParser.retreiveSampleFromNotesLine(notesLines.get(i + 1));
						liftStartToPeak.setLiftPeak(nextSample);
						benchSet.addToLiftStartToPeakCollection(liftStartToPeak);
					}
				} else if (currentLine.endsWith(DataGrapherConstants.LIFT_PEAK)) { 
					if (currentSample >= sampleStart && currentSample <= sampleEnd) {
						// add a lift rest to peak entry
						final LiftPeakToRest liftPeakToRest = new LiftPeakToRest();
						liftPeakToRest.setLiftPeak(currentSample);
						// set the next value as the lift rest
						final int nextSample = DataGrapherLogParser.retreiveSampleFromNotesLine(notesLines.get(i + 1));
						liftPeakToRest.setLiftRest(nextSample);
						benchSet.addToLiftPeakToRestCollection(liftPeakToRest);
					}
				}
			}
		}

		System.out.println("### " + benchSets.size() + " bench sets were found in the notes/log file combination");

		// load the samples for each set
		for (final BenchSet bench : benchSets) {
			for (int i = 1; i < logLines.size(); i++) {
				final String logLine = logLines.get(i);
				final int sample = DataGrapherLogParser.retreiveSampleFromLogFile(logLine);
				if (sample >= bench.getSetStartSample() && sample <= bench.getSetEndSample()) {
					bench.addToSetLineCollection(logLine);
				}
				// add the line to lift start data if applicable
				for (final LiftStartToPeak startToPeak : bench.getLiftStartToPeakCollection()) {
					if (sample >= startToPeak.getLiftStart() && sample <= startToPeak.getLiftPeak()) {
						startToPeak.addToSetLineCollection(logLine);
					}
				}
				// add the line to lift rest data if applicable
				for (final LiftPeakToRest peakToRest : bench.getLiftPeakToRestCollection()) {
					if (sample >= peakToRest.getLiftPeak() && sample <= peakToRest.getLiftRest()) {
						peakToRest.addToSetLineCollection(logLine);
					}
				}
			}
		}

		return benchSets;

	}

}
