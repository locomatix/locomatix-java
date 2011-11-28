package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.Circle;
import com.locomatix.model.NameValues;
import com.locomatix.model.Point;
import com.locomatix.model.Polygon;
import com.locomatix.model.Region;
import com.locomatix.model.URLCallback;

public class CreateFenceTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String fenceId1 = "1";
	private static final String fenceId2 = "2";
	//private static final String fenceId3 = "33";
	//private static final String fenceId4 = "4";
	private static final String fenceId5 = "5";
	
	private Point centerPoint = new Point(42.271369, -83.751445);
	private int radius = 1000;
	private String callbackUrl = "http://mycallback.com"; 
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
	}
	
	@Test
	public void testCreateFence() throws MalformedURLException {
		
		Region region = Circle.create(centerPoint, radius);
		URL url = new URL(callbackUrl);
		Callback callback = new URLCallback(url);
		
		try {
		    client.fences().create(fenceId1, region, callback, feed).execute();
		    
		    client.fences().get(fenceId1).execute();
			
		} catch(LocomatixServiceException lse) {
			lse.printStackTrace();
			assertTrue(false);
		}
		
	}
	
	
	@Test
	public void testCreateFenceWithAttributes() throws MalformedURLException {
	  Region region = Circle.create(centerPoint, radius);
		URL url = new URL(callbackUrl);
		Callback callback = new URLCallback(url);
		
		NameValues nameValues = NameValues.create();
		
		nameValues.put("aaa", "aaa");
		nameValues.put("bbb", "bbb");
		
		try {
		  client.fences().create(fenceId2, region, callback, feed)
		    .withNameValues(nameValues).execute();
			client.fences().get(fenceId2).execute();
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
		
	}
	
	
	
	@Test
	public void testCreateFenceThatAlreadyExists() throws MalformedURLException {
	  Region region = Circle.create(centerPoint, radius);
	  URL url = new URL(callbackUrl);
		Callback callback = new URLCallback(url);
		
		try {
			
			client.fences()
			    .create(fenceId1, region, callback, feed)
			    .execute();
			assertTrue(false);
			
		} catch(LocomatixServiceException lse) {
			assertEquals("FenceAlreadyExists", lse.getMessage());
		}
	}
	
	@Test
	public void testCreatePolygonRegionFence() throws MalformedURLException {
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(37.791744,-122.424088));
		points.add(new Point(37.791744,-122.424088));
		points.add(new Point(37.786453,-122.419796));
		points.add(new Point(37.78781,-122.410011));
		points.add(new Point(37.786725,-122.40469));
		points.add(new Point(37.792422,-122.40572));
		points.add(new Point(37.794593,-122.412586));
		points.add(new Point(37.791337,-122.417221));
		points.add(new Point(37.791744,-122.424088));
		
		
		Polygon region = Polygon.create(points);
		URL url = new URL(callbackUrl);
		Callback callback = new URLCallback(url);
        
		try {
		  client.fences().create(fenceId5, region, callback, feed)
		    .execute();
			client.fences().get(fenceId5).execute();	
		} catch(LocomatixServiceException lse) {
			lse.printStackTrace();
			assertTrue(false);
		}
	}
	
	@AfterClass
	public static void tearDown() {
		try {
		  client.fences().delete(fenceId1).execute();
		  client.fences().delete(fenceId2).execute();
		  //client.fences().delete(fenceId3).execute();
		  //client.fences().delete(fenceId4).execute();
		  client.fences().delete(fenceId5).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
		
		TestHelper.shutdownClient(client);
	}
	
}
