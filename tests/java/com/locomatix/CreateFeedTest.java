package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CreateFeedTest {

private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String feed2 = "testfeed2";
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
	}
	
	
	@Test
	public void testCreateFeed() {
		try {
			client.feeds().create(feed).execute();
			assertTrue(true);
		} catch(LocomatixException le) {
			System.out.println(le);
			assertTrue(false);
		}
	}
	
	@Test
	public void testCreateFeedThatAlreadyExists() {
		try {
			client.feeds().create(feed).execute();
			assertTrue(false);
		} catch(LocomatixException le) {
			System.out.println(le);
			assertEquals("FeedAlreadyExists", le.getMessage());
			assertTrue(true);
		}
	}
	
	@Test
	public void testCreateFeedWithAttributes() {
		try {
			Locomatix.Feed.Create createFeed = client.feeds().create(feed2);
			createFeed.withNameValue("key", "value");
			createFeed.execute();
		} catch(LocomatixException le) {
			System.out.println(le);
			assertTrue(false);
		}
	}
	
	
	@AfterClass
	public static void tearDown() {
		try {
			client.feeds().delete(feed).execute();
			client.feeds().delete(feed2).execute();
		} catch(LocomatixException le) {
			System.out.println(le);
		}
		TestHelper.shutdownClient(client);
	}
	
}
