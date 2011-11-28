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
 * Specifies constants that define the Locomatix Region Types. 
 *
 */
public enum RegionType {

	CIRCLE("Circle"),
	
	POLYGON("Polygon");
	
	private String type;
	
	private RegionType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
	public static RegionType fromValue(String value) {
		if(value == null || "".equals(value))
			throw new IllegalArgumentException("Value cannot be null or empty!");
		else if(CIRCLE.toString().equalsIgnoreCase(value))
			return CIRCLE;
		else if(POLYGON.toString().equalsIgnoreCase(value))
			return POLYGON;
		else
			throw new IllegalArgumentException("Cannot create Region enum from " + value + " value!");
	}
}
