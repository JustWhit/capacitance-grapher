package com.smilanko.smartbelt.capacitance.grapher;

import java.io.File;
import java.util.List;

import com.smilanko.smartbelt.capacitance.grapher.domain.BenchSet;
import com.smilanko.smartbelt.capacitance.grapher.fileutils.CapacitanceGrapherFileReader;
import com.smilanko.smartbelt.capacitance.grapher.processing.CapacitanceGrapherDataExtractor;
import com.smilanko.smartbelt.capacitance.grapher.processing.CapacitanceGrapherGraphProcessor;
import com.smilanko.smartbelt.capacitance.grapher.validationutils.CapacitanceGrapherValidaitonUtils;

public class CapacitanceGrapher {

	public static void main(String[] args) throws Exception {

		// validate the received files
		checkFilesPresent(args);
		checkFileOrder(args);

		// read the notes into memory and validate
		final File notesFile = new File(args[1]);
		final List<String> notesLines = CapacitanceGrapherFileReader.readLines(notesFile);
		CapacitanceGrapherValidaitonUtils.validateNotesFile(notesLines);

		// read the log data into memory and validate
		final File logFile = new File(args[0]);
		final List<String> logLines = CapacitanceGrapherFileReader.readLines(logFile);
		CapacitanceGrapherValidaitonUtils.validateLogFile(logLines);

		// ensure that the notes samples are present in the log file
		CapacitanceGrapherValidaitonUtils.validateNotesAndLogDataInSync(notesLines, logLines);

		// retrieve the bench sets
		final String basePlotName = logFile.getName().substring(0, logFile.getName().lastIndexOf("."));
		final List<BenchSet> benchSets = CapacitanceGrapherDataExtractor.retreiveBenchSets(notesLines, logLines);
		for (int i = 0; i < benchSets.size(); i++) {
			CapacitanceGrapherGraphProcessor.createBenchSetScatterPlot("capacitance_set_" + i + "_" + basePlotName, benchSets.get(i));
		}

		// begin creating the graphs
		System.out.println("## graphs were created");

	}

	private static void checkFilesPresent(final String... args) {
		if (args == null || args.length != 2) {
			System.out.println("### expecting two files, in order: notes and *.log");
			System.exit(-1);
		}
	}

	private static void checkFileOrder(final String... args) {
		if (!args[0].endsWith(".log")) {
			System.out.println("### the first argument needs to be the log file containig all of the data");
			System.exit(-1);
		}
		if (!args[1].endsWith("notes")) {
			System.out.println("### the second argument needs to be the notes file");
			System.exit(-1);
		}
	}

}
