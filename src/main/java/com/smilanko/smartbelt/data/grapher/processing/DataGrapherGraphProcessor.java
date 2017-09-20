package com.smilanko.smartbelt.data.grapher.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.smilanko.smartbelt.data.grapher.common.DataGrapherLogParser;
import com.smilanko.smartbelt.data.grapher.domain.BenchSet;
import com.smilanko.smartbelt.data.grapher.domain.LiftPeakToRest;
import com.smilanko.smartbelt.data.grapher.domain.LiftStartToPeak;
import com.smilanko.smartbelt.data.grapher.graphs.ScatterPlotGraph;

public class DataGrapherGraphProcessor {

	private DataGrapherGraphProcessor() {
		// singleton
	}

	public static void createBenchSetScatterPlot(final String plotName, final BenchSet benchSet) {
		final XYSeriesCollection dataset = new XYSeriesCollection();

		// plot all of the lift start to lift peaks as the next series
		final XYSeries liftStartToPeakSet = new XYSeries("Lift Start To Peak");
		for (final LiftStartToPeak liftStartToPeak : benchSet.getLiftStartToPeakCollection()) {
			for (final String benchDataLine : liftStartToPeak.getSetLineCollection()) {
				final int sample = DataGrapherLogParser.retreiveSampleFromLogFile(benchDataLine);
				final double capacitance = DataGrapherLogParser.retrieveCapacitanceFromLogFile(benchDataLine);
				liftStartToPeakSet.add(sample, capacitance);
			}
		}
		dataset.addSeries(liftStartToPeakSet);

		// plot all of the lift peak to lift rest as the next series
		final XYSeries liftPeakToRestSet = new XYSeries("Lift Peak To Rest");
		for (final LiftPeakToRest liftPeakToRest : benchSet.getLiftPeakToRestCollection()) {
			for (final String benchDataLine : liftPeakToRest.getSetLineCollection()) {
				final int sample = DataGrapherLogParser.retreiveSampleFromLogFile(benchDataLine);
				final double capacitance = DataGrapherLogParser.retrieveCapacitanceFromLogFile(benchDataLine);
				liftPeakToRestSet.add(sample, capacitance);
			}
		}
		dataset.addSeries(liftPeakToRestSet);

		final XYSeries completeSetData = new XYSeries("Un-Correlated data");

		// plot all of the set data as the base series
		final List<String> benchDataLines = benchSet.getSetLineCollection();
		double minPf = Double.MAX_VALUE;
		double maxPf = Double.MIN_VALUE;
		for (final String benchDataLine : benchDataLines) {
			final int sample = DataGrapherLogParser.retreiveSampleFromLogFile(benchDataLine);
			final double capacitance = DataGrapherLogParser.retrieveCapacitanceFromLogFile(benchDataLine);
			completeSetData.add(sample, capacitance);
			minPf = capacitance < minPf ? capacitance : minPf;
			maxPf = capacitance > maxPf ? capacitance : maxPf;
		}
		dataset.addSeries(completeSetData);

		final BufferedImage scatterPlot = ScatterPlotGraph.createScatterPlot(plotName, dataset, minPf, maxPf);
		storeGraphToDisk(plotName, scatterPlot);
	}

	private static void storeGraphToDisk(final String filename, final BufferedImage image) {
		try {
			final File file = new File(filename + ".png");
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			System.out.println("### unable to store the created chart :: " + e.getMessage());
			System.exit(-1);
		}
	}

}
