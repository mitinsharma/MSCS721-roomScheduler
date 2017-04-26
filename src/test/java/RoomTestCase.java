package test.java;

import static org.junit.Assert.*;

import org.junit.Test;

public class RoomTestCase {

	@Test
	public void getbuildingTest() {
		Room r = new Room("Room name",5,"Building name","Location Name");
		String res = r.getBuilding();
		assertEquals("Building name", res);
	}

	

}
