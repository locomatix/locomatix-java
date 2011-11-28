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
package com.locomatix;

/**
 * A class for the Locomatix API HTTP headers needed with each request.
 *
 */
class Headers {
	
	// no instantiation
	private Headers() {}
	
	/**
	 * The Locomatix API version header
	 */
	public static final String LX_API_VERSION = "lx-apiversion";
	
	/**
	 * The Locomatix customer id header
	 */
	public static final String LX_CUST_ID = "lx-custid";
	
	/**
	 * The Locomatix customer key header
	 */
	public static final String LX_CUST_KEY = "lx-custkey";
	
	/**
	 * The Locomatix secret key header
	 */
	public static final String LX_SECRET_KEY = "lx-secretkey";
	
}
