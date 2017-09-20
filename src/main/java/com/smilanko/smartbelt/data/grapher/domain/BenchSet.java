package com.smilanko.smartbelt.data.grapher.domain;

import java.util.ArrayList;
import java.util.List;

public class BenchSet {

	private List<String> setLineCollection = new ArrayList<String>();
	private List<LiftStartToPeak> liftStartToPeakCollection = new ArrayList<LiftStartToPeak>();
	private List<LiftPeakToRest> liftPeakToRestCollection = new ArrayList<LiftPeakToRest>();
	private int setStartSample;
	private int setEndSample;

	public List<LiftStartToPeak> getLiftStartToPeakCollection() {
		return liftStartToPeakCollection;
	}
	
	public void addToLiftStartToPeakCollection(final LiftStartToPeak liftStartToPeak) {
		this.liftStartToPeakCollection.add(liftStartToPeak);
	}
	
	public List<LiftPeakToRest> getLiftPeakToRestCollection() {
		return liftPeakToRestCollection;
	}
	
	public void addToLiftPeakToRestCollection(final LiftPeakToRest liftPeakToRest) {
		this.liftPeakToRestCollection.add(liftPeakToRest);
	}

	public List<String> getSetLineCollection() {
		return setLineCollection;
	}

	public void addToSetLineCollection(final String logLine) {
		this.setLineCollection.add(logLine);
	}

	public int getSetStartSample() {
		return setStartSample;
	}

	public void setSetStartSample(int setStartSample) {
		this.setStartSample = setStartSample;
	}

	public int getSetEndSample() {
		return setEndSample;
	}

	public void setSetEndSample(int setEndSample) {
		this.setEndSample = setEndSample;
	}

}
