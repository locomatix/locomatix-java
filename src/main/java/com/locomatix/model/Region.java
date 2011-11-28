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
 * An abstract region object from which all regions will be derived.
 * 
 */
public abstract class Region {
	
	protected final Map<String, String> properties = new HashMap<String, String>();
	
	/**
	 * Gets the kind of region this is
	 * 
	 * @return
	 */
	public abstract RegionType getRegionType();
	
	/**
	 * Gets a map of name-value pairs describing the region.  The name-value pairs 
	 * are used as form parameters when creating a Zone or Fence.
	 * 
	 * @return The name-value pairs describing the region.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
}
