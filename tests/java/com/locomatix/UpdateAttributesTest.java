package com.locomatix;

import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.NameValues;

public class UpdateAttributesTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	private static final String oid2 = "2";
	
	private static final NameValues attributes = NameValues.create();
	
	private static final NameValues jsonAttributes = NameValues.create();
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		try {
		  client.feeds().create(feed).execute();
			
		  client.objects(feed).create(oid1).execute();
		  client.objects(feed).create(oid2).execute();
			
		  attributes.put("name", "chris");
			attributes.put("username", "cckellogg");
			
			jsonAttributes.put("business", "vinology");
			JSONObject info = new JSONObject();
			info.put("street", "110 South Main Street")
			.put("city", "Ann Arbor")
			.put("state", "MI")
			.put("zipcode", "48104")
			.put("phone", "734-222-9841")
			.put("url", "http://www.vinowinebars.net/vinology/index.html");
			
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} catch(JSONException je) {
			System.out.println(je.getMessage());
		}
	}
	
	@Test
	public void testUpdateAttributes() {
		try {
			client.objects(feed).update(oid1).withNameValues(attributes).execute();
		} catch(LocomatixException le) {
			assertTrue(false);
		}
		
		try {
			 client.objects(feed).update(oid2).withNameValues(jsonAttributes).execute();
		} catch(LocomatixException le) {
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
