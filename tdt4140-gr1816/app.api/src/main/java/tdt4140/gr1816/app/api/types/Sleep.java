package tdt4140.gr1816.app.api.types;

import java.sql.Time;
import java.util.Date;

public class Sleep {

	private int sleepID;
	private 	Date date;
	private Time duration;
	private double efficiency;
	private Person patient;
	
	public Sleep(int sleepID, Date date, Time duration, double efficiency, Person patient) {
		this.sleepID = sleepID;
		this.date = date;
		this.duration = duration;
		this.efficiency = efficiency;
		this.patient = patient;
	}

	public int getSleepID() {
		return sleepID;
	}

	public Date getDate() {
		return date;
	}

	public Time getDuration() {
		return duration;
	}

	public double getEfficiency() {
		return efficiency;
	}

	public Person getPattient() {
		return patient;
	}
	
	
}
