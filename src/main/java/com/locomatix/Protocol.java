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
 * Represents the communication protocol to use when sending requests to Locomatix.
 * Communication over HTTPS is the default. 
 */
public enum Protocol {

	HTTP("http"),
	
	HTTPS("https");

	private final String protocol;
	
	private Protocol(String protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public String toString() {
		return protocol;
	}
	
}
