package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.NameValues;
import com.locomatix.model.Point;
import com.locomatix.model.ResultSet;

public class LocationHistoryTest {

	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	
	private static final long now = System.currentTimeMillis()/1000;
	private static final long startTime = now - 300;
	
	private Point coordinates = new Point(42.271369, -83.751445);
	
	@BeforeClass
	public static void setUp() {
	  client = TestHelper.newClient();
		try {
		    client.feeds().create(feed).execute();
		    client.objects(feed).create(oid1).execute();
		} catch(LocomatixServiceException le) {
			System.out.println(le);
		}
	}
	
	@Test
	public void testLocationHistory() {
		
		try {
			long endTime = System.currentTimeMillis()/1000;
			ResultSet resultSet = 
			    client.analytics(feed).history(oid1, startTime, endTime).execute();
			assertEquals(0, resultSet.getItems().size());
		} catch(Exception ex) {
			assertTrue(false);
			ex.printStackTrace();
		}
		
		double lat = coordinates.getLatitude();
		double lon = coordinates.getLongitude();
		
		try {
			
			client.location(feed).update(oid1, lat, lon, now - 120).execute();
			client.location(feed).update(oid1, lat, lon, now - 60).execute();
			NameValues attrs = NameValues.create();
			attrs.put("status", "at joya");
			client.location(feed).update(oid1, lat, lon, now).withNameValues(attrs).execute();
		} catch(LocomatixServiceException le) {
		    System.out.println(le);
		}
		
		try { Thread.sleep(1500); } catch(Throwable t) {}
		
		long endTime = System.currentTimeMillis()/1000;
		try {
			ResultSet resultSet = 
			    client.analytics(feed).history(oid1, startTime, endTime).execute();
			assertEquals(3, resultSet.getItems().size());
		} catch(Exception ex) {
			assertTrue(false);
			ex.printStackTrace();
		}
		
	}
	
	@AfterClass
	public static void tearDown() {
		try {
			TestHelper.clearFeed(client, feed);
			client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
		TestHelper.shutdownClient(client);
	}
	
}
