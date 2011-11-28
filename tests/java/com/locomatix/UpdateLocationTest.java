package com.locomatix;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Point;

public class UpdateLocationTest {

	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static String oid1 = "1";
	
	private Point coordinates = new Point(42.271369, -83.751445);
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		try {
			client.feeds().create(feed).execute();
			client.objects(feed).create(oid1).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
	}
	
	
	@Test
	public void testUpdateLocation() {
		try {
			double lat = coordinates.getLatitude();
			double lon = coordinates.getLongitude();
			long nowInSeconds = System.currentTimeMillis()/1000;
			client.location(feed).update(oid1, lat, lon, nowInSeconds).execute();
		} catch(LocomatixServiceException lse) {
		  System.out.println(lse);
		  lse.printStackTrace();
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testUpdateLocationWithAttributes() {
		double lat = coordinates.getLatitude();
		double lon = coordinates.getLongitude();
		long nowInSeconds = System.currentTimeMillis()/1000;
		try {
			client.location(feed).update(oid1, lat, lon, nowInSeconds).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	
	@Test
	public void testUpdateLocationWhereObjectDoesNotExist() {
		
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
