package com.smilanko.smartbelt.data.grapher.graphs;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import com.smilanko.smartbelt.data.grapher.common.DataGrapherConstants;

public class ScatterPlotGraph {

	private ScatterPlotGraph() {
		// singleton
	}

	public static BufferedImage createScatterPlot(final String title, final XYDataset data, final double minCapacitance,
			final double maxCapacitance) {
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "sample", "pF", data, PlotOrientation.VERTICAL,
				true, false, false);
		chart.getPlot().setBackgroundPaint(new Color(255, 255, 255));
		final NumberAxis range = (NumberAxis) chart.getXYPlot().getRangeAxis();
		range.setRange(minCapacitance - DataGrapherConstants.GRAPH_CAPACITANCE_MIN_OFFSET,
				DataGrapherConstants.GRAPH_CAPACITANCE_MAX_OFFSET + maxCapacitance);
		range.setTickUnit(new NumberTickUnit(20));
		chart.getLegend().setPosition(RectangleEdge.TOP);
		return chart.createBufferedImage(DataGrapherConstants.IMAGE_WIDTH, DataGrapherConstants.IMAGE_HEIGHT);
	}

}
