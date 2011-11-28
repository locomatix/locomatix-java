package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.URLCallback;

public class DeleteZoneTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	
	private static final String zoneId = "1";
	
	//private final Coordinates coordinates = new Coordinates(42.271369, -83.751445);
	//private static final int radius = 1000;
	private static final String callbackUrl = "http://mycallback.com"; 
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		try {
		  client.feeds().create(feed).execute();
		  client.objects(feed).create(oid1).execute();
			
		  Callback callback = URLCallback.create(callbackUrl);
			client.zones(oid1, feed)
			    .create(zoneId, callback, feed)
			    .execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} 
	}
	
	@Test
	public void testDeleteZone() throws MalformedURLException {
		try {
			client.zones(oid1, feed).delete(zoneId).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testDeleteZoneThatDoesNotExist() {
		try {
		  client.zones(oid1, feed).delete(zoneId).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertTrue(true);
			assertEquals("ZoneDoesNotExist", lse.getMessage());
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
		    client.objects(feed).delete(oid1).execute();
		    client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
		
		TestHelper.shutdownClient(client);
	}
}
