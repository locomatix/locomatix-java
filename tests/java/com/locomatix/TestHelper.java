package com.locomatix;

import com.locomatix.auth.BasicLocomatixCredentials;
import com.locomatix.auth.LocomatixCredentials;
import com.locomatix.model.Item;
import com.locomatix.model.LXObject;
import com.locomatix.model.ResultSet;

public class TestHelper {
	
	public static Locomatix newClient() {
		LocomatixCredentials credentials = 
			new BasicLocomatixCredentials(TestConstants.CUSTOMERID, 
				TestConstants.CUSTOMERKEY, TestConstants.SECRETKEY);
		
		ClientConfiguration config = 
			new ClientConfiguration(TestConstants.HOST, TestConstants.PORT);
		config.setTrustAllCertificates(true);
		
		return new Locomatix(credentials, config);
	}
	
	public static void shutdownClient(Locomatix client) {
		if(null != client) {
			client.shutdown();
		}
	}
	
	public static void clearFeed(Locomatix client, String feed) {
		System.out.println("clearing feed " + feed + " ...");
		Locomatix.Objects.List listObjects = client.objects(feed).list();
		ResultSet objects = listObjects.execute();
		while(!objects.getItems().isEmpty()) {
			for(Item item : objects.getItems()) {
			  LXObject o = item.get(LXObject.class);
				try { client.objects(feed).delete(o.getObjectId()).execute(); }
				catch (LocomatixException le) {}
			}
			// are there more objects to fetch?
			if (null == objects.getNextToken()) 
				break;
			listObjects.withNextToken(objects.getNextToken());
			objects = listObjects.execute();
		}
		
	}
	
}
