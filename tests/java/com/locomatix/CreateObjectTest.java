package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Location;
import com.locomatix.model.NameValues;

public class CreateObjectTest {

	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	private static final String oid2 = "2";
	private static final String oid3 = "3";
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		try {
			client.feeds().create(feed).execute();
		} catch(LocomatixException le) {
			System.out.println(le);
		}
	}
	
	@Test
	public void testCreateObject() {
		try {
			client.objects(feed).create(oid1).execute();
		} catch(LocomatixServiceException le) {
			System.out.println("testCreateObject: " + le);
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testCreateObjectWithAttributes() {
		NameValues nameValues = NameValues.create();
		nameValues.put("name", "chris");
		nameValues.put("email", "cckellogg@locomatix.com");
		
		try {
			client.objects(feed).create(oid2).withNameValues(nameValues).execute();
		} catch(LocomatixServiceException le) {
			System.out.println("testCreateObjectWithAttributes: " + le);
			assertTrue(false);
		}
	}
	
	@Test
	public void testCreateObjectThatAlreadyExists() {
		try {
			client.objects(feed).create(oid1).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertEquals("ObjectAlreadyExists", lse.getMessage());
			assertTrue(true);
		}
	}
	
	
	@Test
	public void testCreateObjectWithLocation() {
		try {
			double lat = 42.271369;
			double lon = -83.751445;
			long nowInSeconds = System.currentTimeMillis() / 1000;
			
			client.objects(feed).create(oid3).withLocation(lat, lon, nowInSeconds).execute();
			
			Location l = client.location(feed).get(oid3).execute();
			assertEquals(nowInSeconds, l.getTimestamp());
			assertEquals(new com.locomatix.model.Point(lat, lon), l.getCoordinates());
		} catch(LocomatixServiceException lse) {
			System.out.println(lse);
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
