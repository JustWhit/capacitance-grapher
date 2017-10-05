package com.smilanko.smartbelt.capacitance.grapher.graphs;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import com.smilanko.smartbelt.capacitance.grapher.common.CapacitanceGrapherConstants;

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
		range.setRange(minCapacitance - CapacitanceGrapherConstants.GRAPH_CAPACITANCE_MIN_OFFSET,
				CapacitanceGrapherConstants.GRAPH_CAPACITANCE_MAX_OFFSET + maxCapacitance);
		range.setTickUnit(new NumberTickUnit(20));
		chart.getLegend().setPosition(RectangleEdge.TOP);
		return chart.createBufferedImage(CapacitanceGrapherConstants.IMAGE_WIDTH, CapacitanceGrapherConstants.IMAGE_HEIGHT);
	}

}
