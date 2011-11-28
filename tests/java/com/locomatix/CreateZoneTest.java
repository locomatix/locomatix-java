package com.locomatix;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.URLCallback;

public class CreateZoneTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	
	private static final String zoneId = "1";
	private static final String zoneId2 = "2";
	//private static final String zoneId3 = "3";
	//private static final String zoneId4 = "4";
	
	//private final Coordinates coordinates = new Coordinates(42.271369, -83.751445);
	//private final int radius = 1000;
	private final String callbackUrl = "http://mycallback.com";
	
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
	public void testCreateZone() throws MalformedURLException {
		
		Callback callback = URLCallback.create(callbackUrl);
		try {
			client.zones(oid1, feed)
			    .create(zoneId, callback, feed)
			    .execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testCreateZoneWithAttributes() throws MalformedURLException {
		
		Callback callback = URLCallback.create(callbackUrl);		
		try {
		  client.zones(oid1, feed)
		    .create(zoneId2, callback, feed)
		    .withNameValue("aaa", "xyz").execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
      client.zones(oid1, feed).delete(zoneId).execute();
	    client.zones(oid1, feed).delete(zoneId2).execute();
		  client.objects(feed).delete(oid1).execute();
	    client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
		
		TestHelper.shutdownClient(client);
	}
}
