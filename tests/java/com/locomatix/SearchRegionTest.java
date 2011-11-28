package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Circle;
import com.locomatix.model.Point;
import com.locomatix.model.ResultSet;

public class SearchRegionTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	private static final String oid2 = "2";
	private static final String oid3 = "3";
	private static final String oid4 = "4";
	
	private static final Point coordinates = new Point(42.271369, -83.751445);
	

	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		try {
			client.feeds().create(feed).execute();
			
			client.objects(feed).create(oid1).execute();
			client.objects(feed).create(oid2).execute();
			client.objects(feed).create(oid3).execute();
			client.objects(feed).create(oid4).execute();
			
			double lat = coordinates.getLatitude();
			double lon = coordinates.getLongitude();
			long nowInSeconds = System.currentTimeMillis() / 1000;
			
			client.location(feed).update(oid1, lat, lon, nowInSeconds).execute();
			client.location(feed).update(oid2, lat, lon, nowInSeconds).execute();
			client.location(feed).update(oid3, lat, lon, nowInSeconds).execute();
			client.location(feed).update(oid4, lat, lon, nowInSeconds).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
	}
	
	
	@Test
	public void testSearchRegionCircle() {
		int radius = 1000;
		Circle region = Circle.create(coordinates, radius);
		try {
		  ResultSet resultSet = client.search(feed).region(region).execute();
		    assertEquals(4, resultSet.getItems().size());
		  assertNull(resultSet.getNextToken());
		} catch(LocomatixException le) {
			le.printStackTrace();
			assertTrue(false);
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
