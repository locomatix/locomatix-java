package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.ResultSet;

public class ListObjectsTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static String oid1 = "1";
	private static String oid2 = "2";
	private static String oid3 = "3";
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		try {
			client.feeds().create(feed).execute();
			
			client.objects(feed).create(oid1).execute();
			client.objects(feed).create(oid2).execute();
			client.objects(feed).create(oid3).execute();
		} catch(LocomatixException le) {
		  le.printStackTrace();
			System.out.println(le.getMessage());
		}
	}
	
	@Test
	public void testListObjects() {
		
		try {
			//ListObjectsRequest request = new ListObjectsRequest(feed);
			ResultSet list = client.objects(feed).list().execute();
			assertEquals(3, list.getItems().size());
			assertNull(list.getNextToken());
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
		
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
			TestHelper.clearFeed(client, feed);
			client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
		
		TestHelper.shutdownClient(client);
	}
	
}
