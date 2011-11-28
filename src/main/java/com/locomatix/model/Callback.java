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

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract callback object from which all callbacks will be derived. A callback 
 * tells Locomatix where it should send notifications.
 * 
 */
public abstract class Callback {
	
	static final String CALLBACKTYPE = "callbacktype";
	
	protected Map<String, String> properties = new HashMap<String, String>();
	
	/**
	 * Returns the class type for this callback.
	 * 
	 * @return the call type for this callback.
	 */
	public abstract CallbackType getCallbackType();
	
	/*
	 * Returns the name value pairs describing the callback.  These name
	 * value pairs are used as url parameters.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
}
