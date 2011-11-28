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
 * Reserved words used by the Locomatix API as URL parameters.  These words
 * cannot be used as keys in name-value pairs for Objects and Locations.
 */
class Parameters {

	// no instantiation
	private Parameters() {}
	
	public static final String OID = "oid";
	
	public static final String FEED = "feed";
	
	public static final String OBJECTEXPIRY = "object_expiry";
	
	public static final String LOCATIONEXPIRY = "location_expiry";
	
	public static final String TIME = "time";
	
	public static final String ALLOWEDEXPIRED = "allowexpired";
	
	public static final String LATITUDE = "latitude";
	
	public static final String LONGITUDE = "longitude";
	
	public static final String ZONEID = "zoneid";
	
	public static final String FENCEID = "fenceid";
	
	public static final String REGION = "region";
	
	public static final String CALLBACKTYPE = "callbacktype";
	
	public static final String TRIGGER = "trigger";
	
	public static final String FROMFEED = "fromfeed";
	
	public static final String STARTKEY = "startkey";
	
	public static final String FETCHSIZE = "fetchsize";
	
	public static final String STARTTIME = "starttime";
	
	public static final String ENDTIME = "endtime";
	
	public static final String PREDICATE = "predicate";
	
	public static final String ONETIMEALERT = "onetimealert";
	
	public static final String STATE = "state";
	
	public static final String GRID_BBOX = "grid_bbox";
	
	public static final String GRID_HORIZONTAL_SLICES = "grid_horizontal_slices";
	
	public static final String GRID_VERTICAL_SLICES = "grid_vertical_slices";
	
	public static final String GRID_STARTTIME = "grid_starttime";
	
}
