package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.URLCallback;

public class DeactivateActivateZoneTest {
	
private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	
	private static final String zoneId = "1";
	
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
	public void testDeactivateZone() throws MalformedURLException {
		try {
			client.zones(oid1, feed).deactivate(zoneId).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testDeactivingZoneAlreadyDeactivated() {
		try {
			client.zones(oid1, feed).deactivate(zoneId).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertTrue(true);
			assertEquals("ZoneAlreadyInActive", lse.getMessage());
		}
	}
	
	
	@Test
	public void testDeactivingZoneThatDoesNotExist() {
		try {
		    client.zones(oid1, feed).deactivate("does_not_exist").execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertTrue(true);
			assertEquals("ZoneDoesNotExist", lse.getMessage());
		}
	}
	
	
	
	
	@Test
	public void testActivateZone() throws MalformedURLException {
		try {
		    client.zones(oid1, feed).activate(zoneId).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testActivingZoneAlreadyActivated() {
		try {
		    client.zones(oid1, feed).activate(zoneId).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertTrue(true);
			assertEquals("ZoneAlreadyActive", lse.getMessage());
		}
	}
	
	
	@Test
	public void testActivingZoneThatDoesNotExist() {
		try {
		    client.zones(oid1, feed).activate("does_not_exist").execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertTrue(true);
			assertEquals("ZoneDoesNotExist", lse.getMessage());
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
		    client.zones(oid1, feed).delete(zoneId).execute();
			client.objects(feed).delete(oid1).execute();
			client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
		
		TestHelper.shutdownClient(client);
	}
	
}
