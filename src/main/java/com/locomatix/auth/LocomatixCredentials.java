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

public interface LocomatixCredentials {

	/**
	 * Returns the LX customer ID for this credentials object
	 * 
	 * @return The LX customer ID for this credentials object
	 */
	public String getCustomerId();

	/**
	 * Returns the LX customer key for this credentials object
	 * 
	 * @return The LX customer key for this credentials object
	 */
	public String getCustomerKey();
	
	/**
	 * Returns the LX customer secret for this credentials object
	 * 
	 * @return The LX customer secret for this credentials object
	 */
	public String getSecretKey();

}
