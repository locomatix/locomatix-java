package com.locomatix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locomatix.model.LXObject;
import com.locomatix.model.NameValues;


public class GetAttributesTest {
	
	private static Locomatix client;
	
	private static final String feed = "testfeed";
	
	private static final String oid1 = "1";
	private static final String oid2 = "2";
	private static final String oid3 = "3";
	
	private static NameValues attributes = NameValues.create();
	
	private static final List<String> hobbies = new ArrayList<String>();
	
	private static NameValues jsonAttributes = NameValues.create();
	
	private static final JSONObject info = new JSONObject();
	
	private static final String BUSINESS_INFO = "businesss_info";
	
	
	@BeforeClass
	public static void setUp() {
		client = TestHelper.newClient();
		
		try {
			client.feeds().create(feed).execute();
			
			attributes.put("name", "chris");
			attributes.put("username", "cckellogg");
			hobbies.add("golf");
			hobbies.add("cricket");
			attributes.putAll("hobbies", hobbies);
			
			client.objects(feed).create(oid1).withNameValues(attributes).execute();
			
			jsonAttributes.put("business", "vinology");
			
			info.put("street", "110 South Main Street")
			.put("city", "Ann Arbor")
			.put("state", "MI")
			.put("zipcode", "48104")
			.put("phone", "734-222-9841")
			.put("url", "http://www.vinowinebars.net/vinology/index.html");
			
			jsonAttributes.put(BUSINESS_INFO, info.toString());
			System.out.println("Attributes with JSON String: " + jsonAttributes);
			
			client.objects(feed).create(oid2).withNameValues(jsonAttributes).execute();
			
			
			client.objects(feed).create(oid3).execute();
		} catch(LocomatixException le) {
			System.out.println(le.getMessage());
		} catch(JSONException je) {
			System.out.println(je.getMessage());
		}
	}
	
	@Test
	public void testGetAttributes() {
		try {
			LXObject lxo = client.objects(feed).get(oid1).execute();
			assertEquals(oid1, lxo.getObjectId());
			assertEquals(feed, lxo.getFeedId());
			System.out.println("Attributes: " + lxo.getNameValues());
			assertEquals(attributes, lxo.getNameValues());
			assertEquals(hobbies, lxo.getNameValues().get("hobbies"));
		} catch(LocomatixServiceException lse) {
		  System.out.println(lse);
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testGetJSONAttributes() throws JSONException {
		try {
			LXObject lxo = client.objects(feed).get(oid2).execute();
			assertEquals(oid2, lxo.getObjectId());
			assertEquals(feed, lxo.getFeedId());
			System.out.println("Attributes with JSON String: " + lxo.getNameValues());
			assertEquals(jsonAttributes, lxo.getNameValues());
			
			String s = lxo.getNameValues().getFirst(BUSINESS_INFO);
			
			System.out.println("json: " + info.toString());
			JSONObject businessInfo = new JSONObject(s);
			System.out.println("json: " + businessInfo.toString());
			assertEquals(info.getString("phone"), businessInfo.getString("phone"));
			assertEquals(info.getString("zipcode"), businessInfo.getString("zipcode"));
			assertEquals(info.getString("street"), businessInfo.getString("street"));
			assertEquals(info.getString("state"), businessInfo.getString("state"));
			assertEquals(info.getString("city"), businessInfo.getString("city"));
			assertEquals(info.getString("url"), businessInfo.getString("url"));
			
		} catch(LocomatixServiceException lse) {
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testGetObjectWithNoAttributes() {
		try {
			LXObject lxo = client.objects(feed).get(oid3).execute();
			assertEquals(oid3, lxo.getObjectId());
			assertEquals(feed, lxo.getFeedId());
			assertEquals(NameValues.emptyNameValues(), lxo.getNameValues());
		} catch(LocomatixServiceException lse) {
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
