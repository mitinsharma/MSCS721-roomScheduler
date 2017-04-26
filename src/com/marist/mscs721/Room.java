/**
 * file: Meeting.java
 * author: Mitin Sharma
 * course: MSCS 721
 * Assignment: Assignment 1
 * due date: 02/09/2017
 * version: 1.0
 * 
 * Meeting Model
 * Class deals with Room, add new room, remove room.
 */
package com.marist.mscs721;

import java.util.ArrayList;

public class Room {	
	
	private String name;
	private String building;
	private String location;
	private int capacity;
	private ArrayList<Meeting> meetings;
	
	/**
	 * Constructor function
	 * @param newName
	 * @param newCapacity
	 */
	public Room(String newName, int newCapacity,String newBuilding, String newLocation) {
		setName(newName);
		setBuilding(newBuilding);
		setLocation(newLocation);
		setCapacity(newCapacity);
		setMeetings(new ArrayList<Meeting>());
	}
	
	/**
	 * Overloaded constructor to deal with json string
	 * @param data
	 */
	public Room(String data)
	{
		int namePosn = data.indexOf("name") + 4;

		setName(data.substring(namePosn + 1, data.indexOf(",")));
		data = data.substring(data.indexOf(",")+ 1, data.length());
		int capPosn = data.indexOf("capacity") + 8;
		setCapacity((int)Double.parseDouble(data.substring(capPosn + 1, data.indexOf(","))));
		data = data.substring(data.indexOf(",")+ 1, data.length());
		int meetPosn = data.indexOf("meetings") + 8;
		data = data.substring(0, data.length() - 1);
		if(data.length() > 14)
		{
			String[] meetStrings = data.substring(meetPosn + 3, data.length() - 1).split("}");
			ArrayList<Meeting> meetingsA = new ArrayList<Meeting>();
			for(int i = 0; i < meetStrings.length; i++)
			{
				meetingsA.add(new Meeting(meetStrings[i]));
			}
			setMeetings(meetingsA);
		}
		else
		{
			setMeetings(new ArrayList<Meeting>());
		}
	}

	/**
	 * Function for adding meeting
	 * @param newMeeting
	 */
	public void addMeeting(Meeting newMeeting) {
		this.getMeetings().add(newMeeting);
	}

	
	/**
	 * Function returns name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Function returns building name
	 * @return
	 */
	public String getBuilding() {
		return building;
	}
	
	/**
	 * Function returns name
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Function set room name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Function set building name
	 * @param name
	 */
	public void setBuilding(String name) {
		this.building = name;
	}
	
	/**
	 * Function set Location name
	 * @param name
	 */
	public void setLocation(String name) {
		this.location = name;
	}

	/**
	 * Function returns capacity
	 * @return
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Function save capacity
	 * @param capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Function return meetings
	 * @return
	 */
	public ArrayList<Meeting> getMeetings() {
		return meetings;
	}

	/**
	 * Function set meetings
	 * @param meetings
	 */
	public void setMeetings(ArrayList<Meeting> meetings) {
		this.meetings = meetings;
	}
	
}
