package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.NameValues;
import com.locomatix.model.TriggerCondition;
import com.locomatix.model.URLCallback;
import com.locomatix.model.Zone;

public class GetZoneTest {

	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	
	private static final String zoneId = "1";
	private static final String zoneId2 = "2";
	
	//private final Coordinates coordinates = new Coordinates(42.271369, -83.751445);
	private static final double radius = 500;
	private static final String callbackUrl = "http://mycallback.com"; 
	
	private static final NameValues nameValues = NameValues.create();
	
	static {
		nameValues.put("aaa", "aaa");
		nameValues.put("bbb", "bbb");
	}
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		try { 
		    client.feeds().create(feed).execute();
		    client.objects(feed).create(oid1).execute();
		} catch(LocomatixServiceException lse) {
			System.out.println(lse);
		}
		
		try {
			Callback callback = URLCallback.create(callbackUrl);
			
			client.zones(oid1, feed)
			    .create(zoneId, callback, feed)
			    .execute();
			
			client.zones(oid1, feed)
                .create(zoneId2, callback, feed)
                .withNameValues(nameValues).execute();
			
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} 
	}
	
	@Test
	public void testGetZone() throws MalformedURLException {
		
	  URL url = new URL(callbackUrl);
		URLCallback callback = new URLCallback(url);
		
		try {
			Zone z = client.zones(oid1, feed).get(zoneId).execute();
			assertEquals(zoneId, z.getZoneId());
			assertEquals(oid1, z.getObjectId());
			assertEquals(feed, z.getFeedId());
			
			assertEquals(new Double(radius), new Double(z.getRadius()));
			
			assertEquals(callback.getCallbackType(), z.getCallback().getCallbackType());
			assertEquals(callback.getProperties(), z.getCallback().getProperties());
			URLCallback urlcb = (URLCallback) z.getCallback();
			assertEquals(callback.getURL(), urlcb.getURL());
			
			assertEquals(TriggerCondition.Ingress, z.getTriggerCondition());
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testGetZoneWithAttributes() throws MalformedURLException {
		
	  URL url = new URL(callbackUrl);
		URLCallback callback = new URLCallback(url);
		
		try {
			Zone z = client.zones(oid1, feed).get(zoneId2).execute();
			assertEquals(zoneId2, z.getZoneId());
			assertEquals(oid1, z.getObjectId());
			assertEquals(feed, z.getFeedId());
			
			assertEquals(new Double(radius), new Double(z.getRadius()));
			
			assertEquals(callback.getCallbackType(), z.getCallback().getCallbackType());
			assertEquals(callback.getProperties(), z.getCallback().getProperties());
			URLCallback urlcb = (URLCallback) z.getCallback();
			assertEquals(callback.getURL(), urlcb.getURL());
			
			assertEquals(TriggerCondition.Ingress, z.getTriggerCondition());
			
			assertEquals(nameValues, z.getNameValues());
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
    public void testGetZoneThatDoesNotExists() throws MalformedURLException {
        try {
            client.zones(oid1, feed).get("does_not_exist").execute();
            assertTrue(false);
        } catch(LocomatixServiceException lse) {
            assertEquals("ZoneDoesNotExist", lse.getMessage());
        }
    }

	@Test
	public void testGetZoneWhereObjectDoesNotExists() throws MalformedURLException {
		try {
		    client.zones("does_not_exist", feed).get(zoneId).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertEquals("ObjectDoesNotExist", lse.getMessage());
		}
	}
	
	
	@Test
	public void testGetZoneWhereFeedDoesNotExists() throws MalformedURLException {
		try {
			client.zones(oid1, "does_not_exist").get(zoneId).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertEquals("FeedDoesNotExist", lse.getMessage());
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
