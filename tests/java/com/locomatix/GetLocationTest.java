package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Location;
import com.locomatix.model.NameValues;
import com.locomatix.model.Point;

public class GetLocationTest {
	
private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static String oid1 = "1";
	private static String oid2 = "2";
	private static String oid3 = "3";
	
	private static final Point coordinates = new Point(42.271369, -83.751445);
	
	private static final NameValues attributes = NameValues.create();
	
	private static final long nowInSeconds = System.currentTimeMillis()/1000;
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		double lat = coordinates.getLatitude();
		double lon = coordinates.getLongitude();
		attributes.put("status", "playing golf");
		
		try {
			
			client.feeds().create(feed).execute();
			
			client.objects(feed).create(oid1).execute();
			client.objects(feed).create(oid2).execute();
			client.objects(feed).create(oid3).execute();
			
			client.location(feed).update(oid1, lat, lon, nowInSeconds).execute();
			client.location(feed).update(oid2, lat, lon, nowInSeconds)
			.withNameValues(attributes).execute();
			
			
		} catch(LocomatixException le) {
			System.out.println(le);
		}
	}
	
	@Test
	public void testGetLocation() {
		try {
			Location l = client.location(feed).get(oid1).execute();
			
			assertEquals(oid1, l.getObjectId());
			assertEquals(feed, l.getFeedId());
			assertTrue(nowInSeconds == l.getTime());
			assertEquals(coordinates, l.getCoordinates());
			
		} catch(LocomatixServiceException lse) {
			System.out.println(lse);
			assertTrue(false);
		}
	}
	
	@Test
	public void testGetLocationWithAttributes() {
		try {
			Location l = client.location(feed).get(oid2).execute();
			
			assertEquals(oid2, l.getObjectId());
			assertEquals(feed, l.getFeedId());
			assertTrue(nowInSeconds == l.getTime());
			assertEquals(coordinates, l.getCoordinates());
			assertEquals(attributes, l.getNameValues());
			
		} catch(LocomatixServiceException lse) {
			System.out.println(lse);
			assertTrue(false);
		}
	}
	
	
	
	@Test
	public void testGetLocationWhereObjectDoesNotExist() {
		//"ObjectDoesNotHaveLocation"
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
