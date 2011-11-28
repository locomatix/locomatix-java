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
 * Represents a Location for an object in a feed.
 * 
 * Each object can be optionally associated with a set of location attributes. 
 * These attributes are used for storing the current location of the object and 
 * its characteristics at the location. These are called the Location of an object. 
 * If the object moves or checks in at a different location, a new Location Profile is created 
 * and associated with the object. As the object continuously moves or checks in at 
 * different location, it leaves a trail or trajectory of Locations.
 *
 */
public class Location {
	
	// the object id of this location
	private String objectId;
	
	// the feed id of this location
	private String feedId;
	
	// name value pairs associated with the location
	private NameValues nameValues = NameValues.emptyNameValues();
	
	// the latitude and longitude of the location
	private Point coordinates;
	
	// a timestamp (number of seconds since unix epoch) of when the location was created
	private long time;
	
	public Location(String objectId, String feedId, 
			Point coordinates, long time) {
		this.objectId = objectId;
		this.feedId = feedId;
		this.coordinates = coordinates;
		this.time = time;
	}
	
	
	public Location(String objectId, String feedId, 
			NameValues nameValues, Point coordinates, long time) {
		this.objectId = objectId;
		this.feedId = feedId;
		if(null != nameValues) this.nameValues = nameValues;
		this.coordinates = coordinates;
		this.time = time;
	}
	
	/**
	 * Returns the object id of this location.
	 * 
	 * @return The object id of this location.
	 */
	public String getObjectId() {
		return objectId;
	}
	
	/**
	 * Returns the feed id of this location.
	 * 
	 * @return The feed id of this location.
	 */
	public String getFeedId() {
		return feedId;
	}
	
	/**
	 * Returns the attributes associated with this location
	 * 
	 * @return The attributes associated with this location.
	 */
	public NameValues getNameValues() {
		return nameValues;
	}
	
	/**
	 * Returns the latitude and longitude of this location.
	 * 
	 * @return The latitude and longitude of this location.
	 */
	public Point getCoordinates() {
		return coordinates;
	}
	
	
	/**
	 * Returns a timestamp (number of seconds since unix epoch) of when the location was created.
	 * 
	 * @return The timestamp (number of seconds since unix epoch) of when the location was created.
	 */
	public long getTime() {
		return time;
	}
	
	public long getTimestamp() {
		return time;
	}
	
	
	@Override
	public String toString() {
	  StringBuilder sb = new StringBuilder("{");
	  sb.append("ObjectID: ").append(objectId).append(", ");
	  sb.append("FeedID: ").append(feedId).append(", ");
	  sb.append("Location: ").append(coordinates).append(", ");
	  sb.append("Timestamp: ").append(time).append(", ");
	  sb.append("NameValues: ").append(nameValues);
	  sb.append("}");
	  return sb.toString();
	}
	
}
