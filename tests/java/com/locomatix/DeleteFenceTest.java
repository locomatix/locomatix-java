package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.Circle;
import com.locomatix.model.Point;
import com.locomatix.model.Region;
import com.locomatix.model.URLCallback;

public class DeleteFenceTest {

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
			Callback callback = URLCallback.create(callbackUrl);
			client.fences()
			    .create(fenceId1, region, callback, feed)
			    .execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} 
	}
	
	@Test
	public void testDeleteFence() {
		try {
			client.fences().delete(fenceId1).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testDeleteFenceThatDoesNotExists() throws MalformedURLException {
		try {
		    client.fences().delete("does_not_exist").execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertEquals("FenceDoesNotExist", lse.getMessage());
		}
	}
	
	@AfterClass
	public static void tearDown() {
		TestHelper.shutdownClient(client);
	}
	
}
