package com.smilanko.smartbelt.capacitance.grapher.domain;

import java.util.ArrayList;
import java.util.List;

public class LiftPeakToRest {

	private List<String> setLineCollection = new ArrayList<String>();
	private int liftPeak;
	private int liftRest;
	
	public List<String> getSetLineCollection() {
		return setLineCollection;
	}

	public void addToSetLineCollection(final String logLine) {
		this.setLineCollection.add(logLine);
	}

	public int getLiftPeak() {
		return liftPeak;
	}

	public void setLiftPeak(int liftPeak) {
		this.liftPeak = liftPeak;
	}

	public int getLiftRest() {
		return liftRest;
	}

	public void setLiftRest(int liftRest) {
		this.liftRest = liftRest;
	}

}
