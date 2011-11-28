package com.locomatix;

import static org.junit.Assert.assertEquals;
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
import com.locomatix.model.URLCallback;

public class DeactivateActivateFenceTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String fenceId1 = "1";
	
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
			    .execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} catch (MalformedURLException me) {
			me.printStackTrace();
		}
	}
	
	@Test
	public void testDeactivateFence() {
		try {
			client.fences().deactivate(fenceId1).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testDeactivatingFenceAlreadyDeactivated() {
		try {
		    client.fences().deactivate(fenceId1).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertEquals("FenceAlreadyInActive", lse.getMessage());
			assertTrue(true);
		}
	}
	
	
	@Test
	public void testActivateFence() {
		try {
			client.fences().activate(fenceId1).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testActivatingFenceAlreadyActivated() {
		try {
			client.fences().activate(fenceId1).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertEquals("FenceAlreadyActive", lse.getMessage());
			assertTrue(true);
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
			client.fences().delete(fenceId1).execute();
		} catch(LocomatixException lse) {
			System.out.println(lse);
		}
		
		TestHelper.shutdownClient(client);
	}
	
}
