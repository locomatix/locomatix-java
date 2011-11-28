package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.ResultSet;

public class QueryObjectsTest {

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
      
      client.objects(feed).create(oid1).withNameValue("rating", "1").execute();
      client.objects(feed).create(oid2).withNameValue("rating", "2").execute();
      client.objects(feed).create(oid3).withNameValue("rating", "3").execute();
    } catch(LocomatixException le) {
      le.printStackTrace();
      System.out.println(le.getMessage());
    }
  }
  
  @Test
  public void testFindAllObjects() {
    try {
      ResultSet list = client.objects(feed).query().select("*").execute();
      assertEquals(3, list.getItems().size());
      assertNull(list.getNextToken());
    } catch(Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
    
    try {
      ResultSet list = client.objects(feed).query().select("*").where("rating > \"1\"").execute();
      assertEquals(2, list.getItems().size());
      assertNull(list.getNextToken());
    } catch(Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
    
    try {
      ResultSet list = client.objects(feed).query().select("*").where("rating < \"3\"").execute();
      assertEquals(2, list.getItems().size());
      assertNull(list.getNextToken());
    } catch(Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
    
    try {
      ResultSet list = client.objects(feed).query().select("*").where("rating = \"3\"").execute();
      assertEquals(1, list.getItems().size());
      assertNull(list.getNextToken());
    } catch(Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
    
  }
  
  
  
  @Test
  public void testCountObjects() {
    try {
      ResultSet list = client.objects(feed).query().select("count(*)").execute();
      assertEquals(3, list.count());
      assertNull(list.getNextToken());
    } catch(Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
    
  }
  
  
  @Test
  public void testSumObjects() {
    try {
      ResultSet list = client.objects(feed).query().select("sum(rating)").execute();
      assertEquals(6, new Double(list.sum()).intValue());
      assertNull(list.getNextToken());
    } catch(Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
  }
  
  @Test
  public void testMinimumObjects() {
    try {
      ResultSet list = client.objects(feed).query().select("min(rating)").execute();
      assertEquals(1, new Double(list.min()).intValue());
      assertNull(list.getNextToken());
    } catch(Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
  }
  
  @Test
  public void testMaximumObjects() {
    try {
      ResultSet list = client.objects(feed).query().select("max(rating)").execute();
      assertEquals(3, new Double(list.max()).intValue());
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
