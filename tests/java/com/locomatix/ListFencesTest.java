package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.Circle;
import com.locomatix.model.Point;
import com.locomatix.model.Region;
import com.locomatix.model.ResultSet;
import com.locomatix.model.TriggerCondition;
import com.locomatix.model.URLCallback;

public class ListFencesTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String fenceId1 = "1";
	private static final String fenceId2 = "2";
	private static final String fenceId3 = "3";
	
	private static final Point centerPoint = new Point(42.271369, -83.751445);
	private static final int radius = 1000;
	private static final String callbackUrl = "http://mycallback.com";
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		try {
			Region region = Circle.create(centerPoint, radius);
			URL url = new URL(callbackUrl);
			Callback callback = new URLCallback(url);
			
			client.fences()
			    .create(fenceId1, region, callback, feed)
			     .withTriggerCondition(TriggerCondition.Egress)
			    .execute();
			
			client.fences()
        .create(fenceId2, region, callback, feed)
        .execute();
			
			client.fences()
         .create(fenceId3, region, callback, feed)
         .withTriggerCondition(TriggerCondition.IngressAndEgress)
         .execute();
			
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} catch (MalformedURLException me) {
			me.printStackTrace();
		}
	}
	
	
	@Test
	public void testListFences() throws MalformedURLException {
		
		// fetch all three fences
		try {
			ResultSet list = client.fences().list().execute();
			assertEquals(3, list.getItems().size());
			assertNull(list.getNextToken());
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
		
	}
	
	@AfterClass
	public static void tearDown() {
		try {
		    client.fences().delete(fenceId1).execute();
		    client.fences().delete(fenceId2).execute();
		    client.fences().delete(fenceId3).execute();
		} catch(LocomatixException le) {
			System.out.println(le);
		}
		
		TestHelper.shutdownClient(client);
	}
}
