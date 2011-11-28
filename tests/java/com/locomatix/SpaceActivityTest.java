package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Circle;
import com.locomatix.model.Item;
import com.locomatix.model.LXObject;
import com.locomatix.model.Location;
import com.locomatix.model.Point;
import com.locomatix.model.ResultSet;

public class SpaceActivityTest {

	private static Locomatix client;
	
	private static final String feed = "testfeed_sa";
	
	private static final String oid = "3";
	
	private static final long startTime = 0;
	
	private static final long endTime = 1000;
	
	private static final Point coordinates = new Point(42.271369, -83.751445);
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		try {
		  client.feeds().create(feed).execute();
			client.objects(feed).create(oid).execute();
			
			double lat = coordinates.getLatitude();
			double lon = coordinates.getLongitude();
			
			client.location(feed).update(oid, lat, lon, endTime - 120).execute();
			client.location(feed).update(oid, lat, lon, endTime - 60).execute();
		} catch(LocomatixException le) {
			System.err.println(le);
		}
	}
	
	@Test
	public void testSpaceActivityCircleRegion() {
		
		try {
			final int radius = 1000;
			Circle region = Circle.create(coordinates, radius);
			ResultSet resultSet = 
			    client.analytics(feed).activity(region, startTime, endTime).execute();
			
			assertNull(resultSet.getNextToken());
			assertEquals(2, resultSet.getItems().size());
			for (Item item : resultSet.getItems()) {
			  LXObject obj = item.get(LXObject.class);
			  Location l = item.get(Location.class); 
			  assertEquals(oid, obj.getObjectId());
			  assertEquals(feed, obj.getFeedId());
			  assertEquals(coordinates, l.getCoordinates());
			}
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
			TestHelper.clearFeed(client, feed);
			client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {
			System.err.println(le);
		}
		TestHelper.shutdownClient(client);
	}
}
