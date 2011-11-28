package com.locomatix.auth;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BasicLocomatixCredentialsTest {

	private String customerId = "customerid";
	private String customerKey = "customerkey";
	private String secretKey = "secretkey";
	
	@Before
	public void setUp() throws Exception {
	}

	@Test 
	public void simpleCredentialsCheck() {
		BasicLocomatixCredentials credentials = 
			new BasicLocomatixCredentials(customerId, customerKey, secretKey);
		assertTrue(customerId.equals(credentials.getCustomerId()));
		assertTrue(customerKey.equals(credentials.getCustomerKey()));
		assertTrue(secretKey.equals(credentials.getSecretKey()));
	}
	
}
