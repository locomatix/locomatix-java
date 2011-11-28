package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.Callback;
import com.locomatix.model.NameValues;
import com.locomatix.model.ResultSet;
import com.locomatix.model.URLCallback;
import com.locomatix.model.Zone;

public class ListZonesTest {

private static Locomatix client;
    
    private static final String feed = "testfeed";
    
    private static final String oid1 = "1";
    
    private static final String zoneId1 = "1";
    private static final String zoneId2 = "2";
    private static final String zoneId3 = "3";
    
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
            client.feeds().create(feed).execute();
            client.objects(feed).create(oid1).execute();
        } catch(LocomatixServiceException lse) {
            System.out.println(lse);
        }
        
        try {
            Callback callback = URLCallback.create(callbackUrl);
            
            client.zones(oid1, feed)
                .create(zoneId1, callback, feed)
                .execute();
            client.zones(oid1, feed)
                .create(zoneId2, callback, feed)
                .execute();
            client.zones(oid1, feed)
                .create(zoneId3, callback, feed)
                .execute();
            
        } catch(LocomatixException le) {
            System.out.println(le.getMessage());
        } 
    }
    
    @Test
    public void testListZones() throws MalformedURLException {
        try {
            ResultSet zones = client.zones(oid1, feed).list().execute();
            assertEquals(3, zones.getItems().size());
            assertEquals(zoneId1, zones.getItems().get(0).get(Zone.class).getZoneId());
        } catch(LocomatixServiceException lse) {
            assertTrue(false);
        }
    }
    
    
    @AfterClass
    public static void tearDown() {
      try {
        client.zones(oid1, feed).delete(zoneId1).execute();
        client.zones(oid1, feed).delete(zoneId2).execute();
        client.zones(oid1, feed).delete(zoneId3).execute();
        client.objects(feed).delete(oid1).execute();
        client.feeds().delete(feed).execute();
      } catch(LocomatixException le) {
        System.out.println(le.getMessage());
      }
        
      TestHelper.shutdownClient(client);
    }
    
}
