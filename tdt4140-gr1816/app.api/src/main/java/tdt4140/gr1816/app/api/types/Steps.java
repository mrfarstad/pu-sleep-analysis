package tdt4140.gr1816.app.api.types;

import java.util.Date;

public class Steps {

	private int stepsID;
	private 	Date date;
	private int steps;
	private Person patient;
	
	public Steps(int stepsID, Date date, int steps, Person patient) {
		this.stepsID = stepsID;
		this.date = date;
		this.steps = steps;
		this.patient = patient;
	}

	public int getStepsID() {
		return stepsID;
	}

	public Date getDate() {
		return date;
	}

	public int getSteps() {
		return steps;
	}

	public Person getPatient() {
		return patient;
	}
	
	
	
}
