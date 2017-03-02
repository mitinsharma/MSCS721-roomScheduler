/**
 * file: Meeting.java
 * author: Mitin Sharma
 * course: MSCS 721
 * Assignment: Assignment 1
 * due date: 02/09/2017
 * version: 1.0
 * 
 * Meeting Model
 * Class deals with data and functions of meeting class.
 */
package test.java;

import java.sql.Timestamp;

public class Meeting {
	
	private Timestamp startTime = null;
	private Timestamp stopTime = null;
	private String subject = null;

	/**
	 * Constructor Functions 
	 * @param newStartTime
	 * @param newEndTime
	 * @param newSubject
	 */
	public Meeting(Timestamp newStartTime, Timestamp newEndTime, String newSubject) {
		setStartTime(newStartTime);
		setStopTime(newEndTime);
		if (newSubject.isEmpty()) {
			setSubject("N/A");
		}
		else {
			setSubject(newSubject);
		}
	}
	
	/**
	 * Overloaded Constructor 
	 * get data to deal with json data
	 * @param data
	 */
	public Meeting(String data){
		data = data.substring(data.indexOf("startTime"));
		String[] datapieces = data.split(",");
		setStartTime(parseTime(datapieces[0].substring(10)));
		setStopTime(parseTime(datapieces[1].substring(10)));
		setSubject(datapieces[2].substring(9));
	}

	/**
	 * Function used to convert meeting to string type
	 */
	public String toString() {
		return this.getStartTime().toString() + " - " + this.getStopTime() + ": " + getSubject();
	}
	
	/**
	 * Function start time
	 * @return
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * Function set start time
	 * @param startTime
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * Function return stop time
	 * @return
	 */
	public Timestamp getStopTime() {
		return stopTime;
	}

	/**
	 * Function set stop time
	 * @param stopTime
	 */
	public void setStopTime(Timestamp stopTime) {
		this.stopTime = stopTime;
	}

	/**
	 * Function to get subject
	 * @return
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * function to set subject
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * Function convert string data to timestamp
	 * @param data
	 * @return
	 */
	public Timestamp parseTime(String data)
	{
		return Timestamp.valueOf(data);
	}
}
