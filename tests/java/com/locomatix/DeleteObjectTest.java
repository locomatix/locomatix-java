package com.locomatix;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeleteObjectTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static String oid1 = "1";
	private static String oid2 = "2";
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		try {
			client.feeds().create(feed).execute();
			client.objects(feed).create(oid1).execute();
			client.objects(feed).create(oid2).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
	}
	
	@Test
	public void testDeleteObject() {
		try {
			client.objects(feed).delete(oid1).execute();
		} catch(LocomatixException le) {
			le.printStackTrace();
			assertTrue(false);
		}
		
		try {
			client.objects(feed).delete(oid2).execute();
		} catch(LocomatixException le) {
			le.printStackTrace();
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testDeleteObjectThatDoesNotExist() {
		try {
			client.objects(feed).delete(oid1).execute();
			assertTrue(false);
		} catch(LocomatixServiceException lse) {
			assertTrue(true);
			System.out.println(lse.getMessage());
		}
	}
	
	
	
	@AfterClass
	public static void tearDown() {
		try {
			TestHelper.clearFeed(client, feed);
			client.feeds().delete(feed).execute();
		} catch(LocomatixException le) {
			System.out.println(le);
		}
		TestHelper.shutdownClient(client);
	}
	
}
