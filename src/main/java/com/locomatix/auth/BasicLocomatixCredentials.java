/**
 * Copyright 2011 Locomatix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.locomatix.auth;

/**
 * Basic implementation of the LocomatixCredentials interface that allows callers to
 * pass in the Locomatix customer id, customer key, and secret key in the constructor.
 *
 */
public class BasicLocomatixCredentials implements LocomatixCredentials {

	// the Locomatix customer id for this credentials object
	private final String customerId;
	
	// the Locomatix customer key for this credentials object
	private final String customerKey;
	
	// the Locomatix secret key for this credentials object
	private final String secretKey;
	
	
	/**
	 * Creates a new BasicLocomatixCredentials object, with the specified Locomatix 
	 * customer id, customer key, and secret key.
	 * 
	 * @param customerId
	 * 		The Locomatix customer id.
	 * 
	 * @param customerKey
	 * 		The Locomatix customer key.
	 * 
	 * @param secretKey
	 * 		The Locomatix secret key.
	 */
	public BasicLocomatixCredentials(String customerId, String customerKey, String secretKey) {
		this.customerId = customerId;
		this.customerKey = customerKey;
		this.secretKey = secretKey;
	}
	
	@Override
	public String getCustomerId() {
		return customerId;
	}

	@Override
	public String getCustomerKey() {
		return customerKey;
	}

	@Override
	public String getSecretKey() {
		return secretKey;
	}

}
