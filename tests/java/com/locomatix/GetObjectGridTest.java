package com.locomatix;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Grid;
import com.locomatix.model.Point;
import com.locomatix.model.Rectangle;

public class GetObjectGridTest {
	
	private static Locomatix client;
	
	private static final String feed = "gridfeed";
	
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
	public void testObjectGrid() {
		try {
			Point loc00 = new Point(37.759859,-122.441082);
			createObjects("cell00", 1, loc00);
			
			Point loc10 = new Point(37.76203,-122.392502);
			createObjects("cell10", 2, loc10);
			
			Point loc01 = new Point(37.787539,-122.452927);
			createObjects("cell01", 3, loc01);
			
			Point loc11 = new Point(37.791608,-122.399197);;
			createObjects("cell11", 4, loc11);
			
			Point bl = new Point(37.752258,-122.455158);
			Point tr = new Point(37.808428,-122.38924);
			
			long startTime = timestamp - 600;
			Locomatix.Analytics.Histogram gridRequest = 
			    client.analytics(feed).histogram(Rectangle.create(bl, tr), startTime);
			gridRequest.withHorizontalSlices(2).withVerticalSlices(2);
			Grid objectGrid = gridRequest.execute();
			System.out.println("ObjectGrid: " + objectGrid);
			assertEquals(1, objectGrid.getCount(0, 1));
			assertEquals(2, objectGrid.getCount(1, 1));
			assertEquals(3, objectGrid.getCount(0, 0));
			assertEquals(4, objectGrid.getCount(1, 0));
			
			int[] data = objectGrid.getGrid();
			assertEquals(1, data[2]);
			assertEquals(2, data[3]);
			assertEquals(3, data[0]);
			assertEquals(4, data[1]);
			
		} catch(LocomatixServiceException lse) {
			System.out.println(lse);
			lse.printStackTrace();
		}
		
	}
	
	private static final long timestamp = 1000;
	
	private void createObjects(String prefix, int count, Point p) {
	  long time = timestamp;
		for(int i=0; i<count; ++i) {
			String objectId = String.format("%s_object%d", prefix, i);
			client.objects(feed).create(objectId).withLocation(p, time).execute();
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
			TestHelper.clearFeed(client, feed);
			client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {}
		TestHelper.shutdownClient(client);
	}
	
}
