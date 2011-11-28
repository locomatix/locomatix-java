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
package com.locomatix.internal;

/**
 * Specifies constants for parsing Locomatix JSON.
 *
 */
class ResponseKeys {

	// no instantiation
	private ResponseKeys() {}
	
	public static final String EXECUTIONTIME = "ExecutionTime";
	public static final String STATUS = "Status";
	public static final String RESPONSETYPE = "ResponseType";
	public static final String RESULT = "Result";
	
	public static final String OBJECT = "Object";
	public static final String OBJECTS = "Objects";
	public static final String LOCATION = "Location";
	public static final String ZONE = "Zone";
	public static final String ZONES = "Zones";
	public static final String FENCE = "Fence";
	public static final String FENCES = "Fences";
	public static final String FEEDS = "Feeds";
	
	public static final String OBJECTPROFILE = "ObjectProfile";
	public static final String OBJECTNAMEVALUES = "ObjectNameValues";
	public static final String OBJECTID = "ObjectID";
	public static final String FEED = "Feed";
	public static final String ZONEID = "ZoneID";
	public static final String FENCEID = "FenceID";
	public static final String TRIAL = "Trail";
	public static final String ACTIVIY = "Activity";
	
	public static final String LOCATIONEXPIRY = "LocationExpiry";
	public static final String OBJECTEXPIRY = "ObjectExpiry";
	
	public static final String NEXTKEY = "NextKey";
	public static final String NAMEVALUES = "NameValues";

	public static final String LOCATIONPROFILE = "LocationProfile";
	public static final String LOCATIONNAMEVALUES = "LocationNameValues";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longitude";
	public static final String TIME = "Time";

	public static final String FOLLOWOBJECT = "FollowObject";
	
	public static final String REGION = "Region";
	public static final String REGIONTYPE = "RegionType";
	public static final String REGIONPARAMS = "RegionParams";

	public static final String POINTS = "Points";
	
	public static final String CALLBACK = "Callback";
	public static final String CALLBACKTYPE = "CallbackType";
	public static final String CALLBACKURL = "CallbackURL";
	
	public static final String TRIGGER = "Trigger";
	public static final String FROMFEEDS = "FromFeeds";
	public static final String FROMFEED = "FromFeed";
	public static final String PREDICATE = "Predicate";
	
	public static final String STATE = "State";
	
	public static final String OBJECTGRID = "ObjectGrid";
	
	public static final String COUNT = "Count";
	public static final String SUM = "Sum";
	public static final String MINIMUM = "Minimum";
	public static final String MAXIMUM = "Maximum";
	
}
