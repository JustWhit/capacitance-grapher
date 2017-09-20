package com.smilanko.smartbelt.data.grapher.domain;

import java.util.ArrayList;
import java.util.List;

public class LiftStartToPeak {

	private List<String> setLineCollection = new ArrayList<String>();
	private int liftStart;
	private int liftPeak;
	
	public List<String> getSetLineCollection() {
		return setLineCollection;
	}

	public void addToSetLineCollection(final String logLine) {
		this.setLineCollection.add(logLine);
	}

	public int getLiftStart() {
		return liftStart;
	}

	public void setLiftStart(int liftStart) {
		this.liftStart = liftStart;
	}

	public int getLiftPeak() {
		return liftPeak;
	}

	public void setLiftPeak(int liftPeak) {
		this.liftPeak = liftPeak;
	}

}
