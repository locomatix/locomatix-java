package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeleteFeedTest {

	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private final String oid = "1";
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		client.feeds().create(feed).execute();
	}
	
	@Test
	public void testDeleteFeed() {
		try {
			client.feeds().delete(feed).execute();
			assertTrue(true);
		} catch(LocomatixException le) {
			System.out.println(le);
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testDeleteFeedThatDoesNotExist() {
		try {
			client.feeds().delete("feed_does_not_exist").execute();
			assertTrue(false);
		} catch(LocomatixException le) {
			System.out.println(le);
			assertEquals("FeedDoesNotExist", le.getMessage());
			assertTrue(true);
		}
	}
	
	@Test
	public void testDeleteUnemptyFeed() {
		try {
			client.feeds().create(feed).execute();
			client.objects(feed).create(oid).execute();
			client.feeds().delete(feed).execute();
			assertTrue(false);
		} catch(LocomatixException le) {
			System.out.println(le);
			assertEquals("FeedNotEmpty", le.getMessage());
			assertTrue(true);
		} finally {
			try { 
				client.objects(feed).delete(oid).execute();
				client.feeds().delete(feed).execute();
			} catch(LocomatixException le) {
				System.out.println("testDeleteUnemptyFeed: " + le);
			}
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		TestHelper.shutdownClient(client);
	}
	
}
