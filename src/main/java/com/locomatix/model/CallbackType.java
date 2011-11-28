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
package com.locomatix.model;

/**
 * Specifies constants that define the Locomatix Callback Types. 
 *
 */
public enum CallbackType {
	
	// A URL to post notifications
	URL("URL");
	
	private String type;
	
	private CallbackType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}