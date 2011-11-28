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
import com.locomatix.model.Fence;
import com.locomatix.model.NameValues;
import com.locomatix.model.Point;
import com.locomatix.model.Region;
import com.locomatix.model.TriggerCondition;
import com.locomatix.model.URLCallback;

public class GetFenceTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String fenceId1 = "1";
	private static final String fenceId2 = "2";
	//private static final String fenceId3 = "3";
	
	private static final Point centerPoint = new Point(42.271369, -83.751445);
	private static final int radius = 1000;
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
			Region region = Circle.create(centerPoint, radius);
			URL url = new URL(callbackUrl);
			Callback callback = new URLCallback(url);
			
			
			client.fences()
			    .create(fenceId1, region, callback, feed)
			    .execute();
			
			client.fences()
                .create(fenceId2, region, callback, feed)
                .withNameValues(nameValues).execute();
			
			/*
			CreateFenceRequest request3 = 
				new CreateFenceRequest(fenceId3, region, TriggerCondition.Ingress, 
						callback, feed).withStateInactive(true);
			*/
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} catch (MalformedURLException me) {
			me.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetFence() throws MalformedURLException {
		
		Circle region = Circle.create(centerPoint, radius);
		URL url = new URL(callbackUrl);
		URLCallback callback = new URLCallback(url);
		
		try {
			Fence f = client.fences().get(fenceId1).execute();
			
			assertEquals(fenceId1, f.getFenceId());
			
			assertEquals(region.getProperties(), f.getRegion().getProperties());
			assertEquals(region.getRegionType(), f.getRegion().getRegionType());
			Circle cr = (Circle) f.getRegion();
			assertEquals(new Double(region.getRadius()), new Double(cr.getRadius()));
			
			assertEquals(centerPoint, cr.getCenter());
			
			assertEquals(callback.getCallbackType(), f.getCallback().getCallbackType());
			assertEquals(callback.getProperties(), f.getCallback().getProperties());
			URLCallback urlcb = (URLCallback) f.getCallback();
			assertEquals(callback.getURL(), urlcb.getURL());
			
			assertEquals(TriggerCondition.Ingress, f.getTriggerCondition());
			
			assertTrue(f.isActive());
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testGetFenceWithAttributes() throws MalformedURLException {
		
		Circle region = Circle.create(centerPoint, radius);
		URL url = new URL(callbackUrl);
		URLCallback callback = new URLCallback(url);
		
		try {
			Fence f = client.fences().get(fenceId2).execute();
			
			assertEquals(fenceId2, f.getFenceId());
			
			assertEquals(region.getProperties(), f.getRegion().getProperties());
			assertEquals(region.getRegionType(), f.getRegion().getRegionType());
			Circle cr = (Circle) f.getRegion();
			assertEquals(new Double(region.getRadius()), new Double(cr.getRadius()));
			
			assertEquals(callback.getCallbackType(), f.getCallback().getCallbackType());
			assertEquals(callback.getProperties(), f.getCallback().getProperties());
			URLCallback urlcb = (URLCallback) f.getCallback();
			assertEquals(callback.getURL(), urlcb.getURL());
			
			assertEquals(TriggerCondition.Ingress, f.getTriggerCondition());
			
			
			assertEquals(nameValues, f.getNameValues());
			
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
		
	}
	
	
	/*
	@Test
	public void testGetInactiveFence() throws MalformedURLException {
		
		CircleRegion region = new CircleRegion(radius, centerPoint.getLatitude(), centerPoint.getLongitude());
		URL url = new URL(callbackUrl);
		URLCallback callback = new URLCallback(url);
		
		try {
			Fence f = client.fences().get(fenceId3).execute();
			
			assertEquals(fenceId3, f.getFenceId());
			
			assertEquals(region.getProperties(), f.getRegion().getProperties());
			assertEquals(region.getRegionType(), f.getRegion().getRegionType());
			CircleRegion cr = (CircleRegion) f.getRegion();
			assertEquals(region.getRadius(), cr.getRadius());
			
			assertEquals(callback.getCallbackType(), f.getCallback().getCallbackType());
			assertEquals(callback.getProperties(), f.getCallback().getProperties());
			URLCallback urlcb = (URLCallback) f.getCallback();
			assertEquals(callback.getURL(), urlcb.getURL());
			
			assertEquals(TriggerCondition.Ingress, f.getTriggerCondition());
			
			assertFalse(f.isActive());
			
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	*/
	
	@Test
	public void testGetFenceThatDoesNotExists() throws MalformedURLException {
		
		try {
		    client.fences().get("does_not_exist").execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertEquals("FenceDoesNotExist", lse.getMessage());
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
		  client.fences().delete(fenceId1).execute();
		  client.fences().delete(fenceId2).execute();  
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
		
		TestHelper.shutdownClient(client);
	}
	
	
}
