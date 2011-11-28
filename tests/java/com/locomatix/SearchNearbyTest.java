package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.NameValues;
import com.locomatix.model.Point;
import com.locomatix.model.ResultSet;

public class SearchNearbyTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	private static final String oid2 = "2";
	private static final String oid3 = "3";
	private static final String oid4 = "4";
	
	private static final Point coordinates = new Point(42.271369, -83.751445);
	
	private final double radius = 1000;
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		try {
			client.feeds().create(feed).execute();
			
			double lat = coordinates.getLatitude();
			double lon = coordinates.getLongitude();
			long nowInSeconds = System.currentTimeMillis() / 1000;
			
			client.objects(feed).create(oid1).withLocation(lat, lon, nowInSeconds).execute();
			client.objects(feed).create(oid2).withLocation(lat, lon, nowInSeconds).execute();
			client.objects(feed).create(oid3).withLocation(lat, lon, nowInSeconds).execute();
			client.objects(feed).create(oid4).withLocation(lat, lon, nowInSeconds).execute();
            
      NameValues attrs = NameValues.create();
			attrs.put("value", "1");
			client.location(feed).update(oid3, lat, lon, nowInSeconds).withNameValues(attrs).execute();
			NameValues attrs2 = NameValues.create();
			attrs2.put("value", "2");
			client.location(feed).update(oid4, lat, lon, nowInSeconds).withNameValues(attrs2).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		}
	}
	
	
	@Test
	public void testSearchNearbyCircle() {
		try {
			ResultSet resultSet = 
			    client.search(feed).nearby(oid1, feed).withRadius(radius).execute();
			
			assertEquals(3, resultSet.getItems().size());
			assertNull(resultSet.getNextToken());
		} catch(LocomatixServiceException lse) {
			System.out.println(lse);
			assertTrue(false);
		}
		
	}
	
	
	@Test
	public void testSearchNearbyCount() {
	    try {
	        Locomatix.Search.Nearby nearbySearch = 
	            client.search(feed).nearby(oid1, feed).withRadius(radius).select("count(*)");
	        ResultSet resultSet = nearbySearch.execute();
          assertEquals(3, resultSet.count());
          assertNull(resultSet.getNextToken());
        } catch(LocomatixServiceException lse) {
            System.out.println(lse);
            assertTrue(false);
        }
	}
	
	@Test
  public void testSearchNearbyAggregates() {
        try {
            // test sum
            Locomatix.Search.Nearby nearbySearch = client.search(feed).nearby(oid1, feed).withRadius(radius);
            nearbySearch.select("sum(location.value)");
            ResultSet resultSet = nearbySearch.execute();
            
            assertEquals(3, new Double(resultSet.sum()).intValue());
            assertNull(resultSet.getNextToken());
        
            // test min
            nearbySearch.select("min(location.value)");
            resultSet = nearbySearch.execute();
            
            assertEquals(1, new Double(resultSet.min()).intValue());
            assertNull(resultSet.getNextToken());
            
            // test max
            nearbySearch.select("max(location.value)");
            resultSet = nearbySearch.execute();
            
            assertEquals(2, new Double(resultSet.max()).intValue());
            assertNull(resultSet.getNextToken());
        
        } catch(LocomatixServiceException lse) {
            System.out.println(lse);
            assertTrue(false);
        }
    }
	
	
	@Test
	public void testSearchNearbyAggregatesWithWhereClause() {
    try {
      // test sum
      Locomatix.Search.Nearby nearbySearch = 
        client.search(feed).nearby(oid1, feed).withRadius(radius);
      nearbySearch.select("sum(location.value)").where("location.value > \"1\"");
      ResultSet resultSet = nearbySearch.execute();
            
      assertEquals(2, new Double(resultSet.sum()).intValue());
      assertNull(resultSet.getNextToken());
        
      // test min
      nearbySearch.select("min(location.value)").where("location.value > \"1\"");;
      resultSet = nearbySearch.execute();
            
      assertEquals(2, new Double(resultSet.min()).intValue());
      assertNull(resultSet.getNextToken());
            
      // test max
      nearbySearch.select("max(location.value)").where("location.value < \"2\"");;
      resultSet = nearbySearch.execute();
            
      assertEquals(1, new Double(resultSet.max()).intValue());
      assertNull(resultSet.getNextToken());
        
    } catch(LocomatixServiceException lse) {
      System.out.println(lse);
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
