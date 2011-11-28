package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Feed;
import com.locomatix.model.ResultSet;

public class ListFeedsTest {

	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final Set<Feed> feeds = new HashSet<Feed>();
	
	private static final int MAX_FEEDID = 40;
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		for(int i=1; i<MAX_FEEDID; ++i) {
			try { 
				String feedid = feed + (i + 1);
				client.feeds().create(feedid).withNameValue("myid", feedid).execute();
				feeds.add(new Feed(feedid));
			} catch(LocomatixServiceException lse) {
			  System.out.println("Error creating feed " + feed + (i + 1));
				System.out.println(lse);
			}
		}
		
	}
	
	@Test
	public void testListFeeds() {
		
		try {
		  Locomatix.Feed.List listFeeds = client.feeds().list();
		  ResultSet feedList = listFeeds.execute();
		  //System.out.println("ExecutionTime: " + listFeeds.getExecutionTime());
		  Set<Feed> feedSet = new HashSet<Feed>();
		  while(feedList.hasNext()) {
		    Feed f = feedList.next().get(Feed.class);
		    //System.out.println("Feed: " + f);
		    feedSet.add(f);
		  }
		  
		  //System.out.println("feeds: " + feeds);
			/*
			while(!feedList.getItems().isEmpty()) {
			  for (Item item : feedList.getItems()) {
			    feedSet.add(item.get(Feed.class));
			  }
			  if (null == feedList.getNextToken()) 
			    break;
			  listFeeds.withNextToken(feedList.getNextToken());
			  feedList = listFeeds.execute();
			}
			*/
			assertEquals(feeds.size(), feedSet.size());
			assertEquals(feeds, feedSet);
		} catch(LocomatixServiceException lse) {
			assertTrue(false);		
		}
		
	}
	
	
	@AfterClass
	public static void tearDown() {
		client = TestHelper.newClient();
		Locomatix.Feed.List listFeeds = client.feeds().list();
		ResultSet feedsToDelete = listFeeds.execute();
		while (feedsToDelete.hasNext()) {
		  Feed f = feedsToDelete.next().get(Feed.class);
		  try { client.feeds().delete(f.getFeedId()).execute(); } 
      catch(LocomatixServiceException lse) {}
		}
	}
}
