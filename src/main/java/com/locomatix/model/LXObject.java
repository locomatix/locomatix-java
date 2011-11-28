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
 * Represents a Locomatix object.
 * 
 * Objects are fundamental entities in Locomatix. Objects are used to represent 
 * real-world and conceptual entities (e.g.) a user of a social network application, 
 * a vehicle or a cab, a coupon, etc. An object is uniquely identified by an object id 
 * and a feed id, they also may consists of multiple attributes (Name Value Pairs) that describe it.
 *
 */
public class LXObject {
	
	// the id of this object
	private String objectId;
	
	// the id of the feed for which the object is in
	private String feedId;
	
	// the attributes (name-value pairs) for this object
	private NameValues nameValues = NameValues.emptyNameValues();
	
	/**
	 * Creates a new LXObject with an object id and feed id.  Objects in Locomatix are 
	 * uniquely defined by an object id and feed id.
	 * 
	 * @param objectId The id for this object.
	 * 
	 * @param feedId The feed id for this object.
	 */
	public LXObject(String objectId, String feedId) {
		this.objectId = objectId;
		this.feedId = feedId;
	}
	
	/**
	 * Creates a new LXObject with an object id, feed id, a Map of attributes.
	 * 
	 * @param objectId The id for this object.
	 * 
	 * @param feedId The feed id for this object.
	 * 
	 * @param attributes The attributes for this.
	 */
	public LXObject(String objectId, String feedId, NameValues nameValues) {
		this(objectId, feedId);
		if(null != nameValues) {
			this.nameValues = nameValues;
		}
	}
	
	/**
	 * Returns the id for this Object.
	 * 
	 * @return The id for this Object.
	 */
	public String getObjectId() {
		return objectId;
	}
	
	/**
	 * Returns the feed id for this Object.
	 * 
	 * @return The feed id for this Object.
	 */
	public String getFeedId() {
		return feedId;
	}
	
	/**
	 * Returns the attributes (name-value pairs) for this Object.
	 * 
	 * @return The attributes (name-value pairs) for this Object.
	 */
	public NameValues getNameValues() {
		return nameValues;
	}
	
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + (null != objectId ? objectId.hashCode() : 0);
		result = 37*result + (null != feedId ? feedId.hashCode() : 0);
		return result;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof LXObject) {
			LXObject other = (LXObject) o;
			return objectId.equals(other.objectId) &&
				feedId.equals(other.feedId) &&
				nameValues.equals(other.nameValues);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
	  StringBuilder sb = new StringBuilder("{");
	  sb.append("ObjectID: ").append(objectId).append(", ");
	  sb.append("FeedId: ").append(feedId).append(", ");
	  sb.append("NameValues: ").append(nameValues);
	  sb.append("}");
	  return sb.toString();
	}
}
