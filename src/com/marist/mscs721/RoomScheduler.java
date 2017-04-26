/**
 * file: RoomScheduler.java
 * author: Mitin Sharma
 * course: MSCS 721
 * Assignment: Assignment 1
 * due date: 02/09/2017
 * version: 1.0
 * 
 * Finding bugs and adding export/import functionality
 */
package com.marist.mscs721;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;

import org.apache.log4j.FileAppender;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.FileAppender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class RoomScheduler {
	//Keyboard Scanner Class decalaration
	public static Scanner keyboard = new Scanner(System.in);
	public static Scanner sc;
	private static final Logger logger = Logger.getLogger(RoomScheduler.class.getName());
	/**
	 * Main function
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		boolean check = new File("log4j.properties").exists();
		if(check==true)PropertyConfigurator.configure("log4j.properties");
		else logger.error("Log4j properties file not found");
		logger.info("Program Starts");
		Boolean end = false;
		keyboard= new Scanner(System.in);
		ArrayList<Room> rooms = new ArrayList<Room>();
		Meeting meeting = null;
		

		while (!end) {
			switch (mainMenu()) {

			case 1:
				logger.info("Add Room Call");
				System.out.println(addRoom(rooms));
				break;
			case 2:
				logger.info("Remove Room Call");
				System.out.println(removeRoom(rooms));
				break;
			case 3:
				logger.info("Schedule Room Call");
				System.out.print(scheduleRoom(rooms,meeting));
				break;
			case 4:
				logger.info("List Schedule Call");
				System.out.println(listSchedule(rooms));
				break;
			case 5:
				logger.info("Lists Room Call");
				System.out.println(listRooms(rooms));
				break;
			case 6:
				logger.info("Import Room Call");
				rooms = importRooms();
				break;
			case 7:
				logger.info("Export Room Call");
				exportRooms(rooms);
				break;
			case 0:
				end = true;
				logger.info("Program Stops");
				break;
			default:
				logger.warn("Please enter number between 0 - 7");
			}

		}

	}

	/**
	 * Function list schedules
	 * @param roomList
	 * @return
	 */
	protected static String listSchedule(ArrayList<Room> roomList) {
		String roomName = getRoomName();
		System.out.println(roomName + " Schedule");
		System.out.println("---------------------");
		
		for (Meeting m : getRoomFromName(roomList, roomName).getMeetings()) {
			System.out.println(m.toString());
		}

		return "";
	}

	/**
	 * Prompt Menu
	 * @return
	 */
	protected static int mainMenu() {
		System.out.println("Main Menu:");
		System.out.println("  1 - Add a room");
		System.out.println("  2 - Remove a room");
		System.out.println("  3 - Schedule a room");
		System.out.println("  4 - List Schedule");
		System.out.println("  5 - List Rooms");
		System.out.println("  6 - Import rooms from JSON file");
		System.out.println("  7 - Export rooms to JSON file");
		System.out.println("  0 - Exit");
		System.out.println("Enter your selection: ");

		
		return keyboard.nextInt();
	}

	/**
	 * Function add new room
	 * @param roomList
	 * @return
	 */
	public static String addRoom(ArrayList<Room> roomList) {
		String name="";
		boolean roomExists = true, capValid = true;
		int capacity = 0;
		while(roomExists){
			roomExists = false;
			System.out.println("Add a room:");
			name = getRoomName();
			if(checkRoomExist(roomList,name)){
				roomExists = true;
				logger.warn("Caution: Room name you entered is already exist, please try another name.");
			}
		}
		String building = getBuildingName();
		String location = getLocationName();
		while(capValid){
			capValid = false;
			try{
				System.out.println("Room capacity?");
				capacity = keyboard.nextInt();
				if(capacity<1) { 
					capValid = true; 
					logger.warn("Caution: Capacity cannot be less than 1.");
				}
			}
			catch(Exception e){
				logger.warn("Caution: Please enter valid integer.");
				capValid = true;
			}
		}
		Room newRoom = new Room(name, capacity,building,location);
		roomList.add(newRoom);

		return "Room '" + newRoom.getName() + "' added successfully!";
	}

	/**
	 * Function remove room
	 * @param roomList
	 * @return
	 */
	protected static String removeRoom(ArrayList<Room> roomList) {
		System.out.println("Remove a room:");
		String name = getRoomName();
		if(checkRoomExist(roomList,name)){
			roomList.remove(findRoomIndex(roomList, name));
			return "Room removed successfully!";
		}
		else{
			return "Sorry no room found.";
		}
		
	}

	/**
	 * 
	 * Function list rooms
	 * @param roomList
	 * @return
	 */
	protected static String listRooms(ArrayList<Room> roomList) {
		System.out.println("Room Name - Capacity - Building - Location");
		System.out.println("---------------------");

		for (Room room : roomList) {
			System.out.println(room.getName() + " \t\t " + room.getCapacity()+" \t\t " + room.getBuilding()+ " \t\t" + room.getLocation());
		}

		System.out.println("---------------------");

		return roomList.size() + " Room(s)";
	}

	/**
	 * Function schedule room
	 * @param roomList
	 * @return
	 */
	protected static String scheduleRoom(ArrayList<Room> roomList,Meeting meeting) {
		System.out.println("Schedule a room:");
		boolean checkRoom = true, error = true;
		String name="";
		Timestamp startTimestamp= null, endTimestamp=null;
		
		do{
			while(error){
				System.out.println("Start Date? (yyyy-mm-dd):");
				String startDate = keyboard.next();
				System.out.println("Start Time?");
				String startTime = keyboard.next();
				startTime = startTime + ":00.0";
				String timestamp = startDate + " " + startTime;
				if(checkTimeStamp(timestamp)){
					startTimestamp = Timestamp.valueOf(timestamp);
					error = false;
				}
				else{
					logger.warn("Caution: Invalid timestamp, please try again.");
				}
			}
		
			error = true;
			while(error){
				System.out.println("End Date? (yyyy-mm-dd):");
				String endDate = keyboard.next();
				System.out.println("End Time?");
				String endTime = keyboard.next();
				endTime = endTime + ":00.0";
				
				String timestamp = endDate + " " + endTime;
				if(checkTimeStamp(timestamp)){
					endTimestamp = Timestamp.valueOf(timestamp);
					error = false;
				}
				else{
					logger.warn("Caution: Invalid timestamp, please try again.");
				}
			}
		}while(compareTimeStamp(startTimestamp,endTimestamp));
		

		//List Available room as reference
		
		listAvailableRoom(roomList,startTimestamp, endTimestamp);
		
		
		while(checkRoom){	
			checkRoom = false;
			name = getRoomName();
			if(!checkRoomExist(roomList,name)){
				checkRoom = true;
				logger.warn("Caution: Room not exist, enter valid room name.");
			}
		}
		
		System.out.println("Subject?");
		String subject = getSubject();
		
		Room curRoom = getRoomFromName(roomList, name);

		meeting = new Meeting(startTimestamp, endTimestamp, subject);

		curRoom.addMeeting(meeting);

		return "Successfully scheduled meeting!\n";
	}
	
	/**
	 * List Available room
	 */
	protected static void listAvailableRoom(ArrayList<Room> roomList,Timestamp starttp,Timestamp endtp){
		System.out.println("---------Rooms Available------------");
		System.out.println("Room Name - Capacity");
		System.out.println("---------------------");

		for (Room room : roomList) {
			if(checkSchedule(roomList,room.getName(),starttp,endtp))
				System.out.println(room.getName() + " \t\t " + room.getCapacity());
		}

		System.out.println("---------------------");

	}
	
	/**
	 * Function Check schedules
	 * @param roomList
	 * @return
	 */
	protected static Boolean checkSchedule(ArrayList<Room> roomList,String roomName,Timestamp starttp,Timestamp endtp) {
		
		Boolean flag = true;
		//System.out.println("Checking for " + roomName);
		for (Meeting m : getRoomFromName(roomList, roomName).getMeetings()) {
			//System.out.println(m.toString());
			Timestamp meeting_start_time = m.getStartTime();
			Timestamp meeting_end_time = m.getStopTime();
			//System.out.println("checking for room : "+ roomName);
			if((compareTimeStamp(meeting_start_time,starttp) && compareTimeStamp(meeting_end_time,starttp)) 
					&& (compareTimeStamp(endtp,meeting_start_time) && compareTimeStamp(endtp,meeting_end_time))){
				flag = false;
				//System.out.println("false");
			}
			else{
				flag = true;
				//System.out.println("true");
			}
				
		
		}

		return flag;
	}
	
	/**
	 * Function get room model from name
	 * @param roomList
	 * @param name
	 * @return
	 */
	protected static Room getRoomFromName(ArrayList<Room> roomList, String name) {
		boolean checkRoom = true;
		while(checkRoom){	
			checkRoom = false;
			if(!checkRoomExist(roomList,name)){
				checkRoom = true;
				logger.warn("Caution: Room not exist, enter valid room name.");
			}
		}return roomList.get(findRoomIndex(roomList, name));
		
	}

	
	/**
	 * Function find room index in array
	 */
	protected static int findRoomIndex(ArrayList<Room> roomList, String roomName) {
		int roomIndex = 0;

		for (Room room : roomList) {
			if (room.getName().compareTo(roomName) == 0) {
				break;
			}
			roomIndex++;
		}

		return roomIndex;
	}

	
	/**
	 * Function get room names
	 * @return
	 */
	protected static String getRoomName() {
		System.out.println("Room Name?");
		sc = new Scanner(System.in);
		return sc.nextLine();
	}
	
	/**
	 * Function get building names
	 * @return
	 */
	protected static String getBuildingName() {
		System.out.println("Building Name?");
		sc = new Scanner(System.in);
		return sc.nextLine();
	}
	
	/**
	 * Function get location names
	 * @return
	 */
	protected static String getLocationName() {
		System.out.println("Location Name?");
		sc = new Scanner(System.in);
		return sc.nextLine();
	}
	
	protected static String getSubject() {
		sc = new Scanner(System.in);
		return sc.nextLine();
	}
	
	public static boolean compareTimeStamp(Timestamp startTimestamp, Timestamp endTimestamp)
	{
		if(startTimestamp.before(endTimestamp)){
				return false;
		}
		else
		{
			//logger.warn("Caution: Start date cannot be after end date");
			return true;
		}
	}
	
	/**
	 * Function check if room exists or not
	 * @param roomList
	 * @param name
	 * @return
	 */
	protected static boolean checkRoomExist(ArrayList<Room>roomList,String name) {
		boolean res=false;
		for(Room r : roomList){
			if(r.getName().compareToIgnoreCase(name)==0){
				res = true;
				break;
			}
		}
		return res;
	}
	
	/**
	 * Function validate correct timestamp
	 * @param t
	 * @return
	 */
	protected static Boolean checkTimeStamp(String t)
	{
		boolean returnVal = true;
		try
		{
			Timestamp.valueOf(t);
		}
		catch(IllegalArgumentException e)
		{
			returnVal = false;
		}
		return returnVal;
	}
	
	/**
	 * Json Function to import rooms from json string
	 * @return
	 */
	protected static ArrayList<Room> importRooms()
	{
		ArrayList<Room> finalRooms = new ArrayList<Room>();
		//get filename
		boolean inputVerify = false;
		String filename = "";
		while(!inputVerify)
		{
			System.out.println("Please specify the full pathname and filename of the file you wish to read from:");
			 filename = keyboard.next();
			if(filename.contains(".json"))
			{
				inputVerify = true;
			}
			else
			{
				logger.error("ERROR: Please specify a .JSON file.");
			}
		}
		//read from file
		Gson gson = new Gson();
		logger.info("Gson created");
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			ArrayList<?> rooms = gson.fromJson(br, ArrayList.class);
			for(Object r: rooms)
			{
				finalRooms.add(new Room(r.toString(), 0,"",""));
			}

		}
		//catch if unable to find file
		catch(IOException e)
		{
			logger.error("FAILURE: ERROR READING FROM DISK. Unable to open file. Please check file exists and path is correct.");
		}
		return finalRooms;
	}
	
	/**
	 * Function export data to json string
	 * @param roomList
	 */
	protected static void exportRooms(ArrayList<Room> roomList)
	{
		//get new gson object
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
		String json = gson.toJson(roomList);
		//ensure input is correct
		boolean inputCheck = false;
		System.out.println("Please specify full pathname and file to save to:");
		String filename = keyboard.next();
		while(!inputCheck)
		{
			if(!filename.contains(".json"))
			{
				logger.error("ERROR: Please make sure your filename ends with .json");
				System.out.println("Please specify full pathname and file to save to:");
				filename = keyboard.next();
			}
			else
			{
				inputCheck = true;
			}
		}
		//write to file
		try{
			FileWriter writer = new FileWriter(filename);
			writer.write(json);
			writer.close();
			System.out.println("Files exported to " + filename + ".");
		}
		catch(IOException e)
		{
			logger.error("ERROR WRITING TO DISK: Unable to create JSON file. Please try again.");
		}
	}
	
}
