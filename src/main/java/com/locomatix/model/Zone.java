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
 * A Zone is a region around an object (the zone moves with the object) that will be continually 
 * evaluated by the server. A notification is sent to the callback URL when any object 
 * enters or leaves this dynamic zone.  A list of the current objects in the zone 
 * is sent in the alert message.
 * 
 */
public class Zone {

	// the object id of this zone
	private String objectId;
	
	// the feed id of this zone
	private String feedId;
	
	// the id of this zone
	private String zoneId;
	
	private double radius;
	
	// the trigger condition for this zone
	private TriggerCondition trigger;
	
	// the callback for this zone
	private Callback callback;
	
	private String query;
	
	private NameValues nameValues = NameValues.emptyNameValues();
	
	private boolean active;
	
	public Zone(String zoneId, String objectId, String feedId, double radius, 
			TriggerCondition trigger, Callback callback, String query, boolean active) {
		this.zoneId = zoneId;
		this.objectId = objectId;
		this.feedId = feedId;
		this.radius = radius;
		this.trigger = trigger;
		this.callback = callback;
		this.query = query;
		this.active = active;
	}
	
	
	public Zone(String zoneId, String objectId, String feedId, double radius, 
			TriggerCondition trigger, Callback callback, String query, 
			NameValues nameValues, boolean active) {
		this(zoneId, objectId, feedId, radius, trigger, callback, query, active);
		if(null != nameValues) {
			this.nameValues = nameValues;
		}
	}
	
	/**
	 * Returns the object id for this zone.
	 * 
	 * @return The object id for this zone.
	 */
	public String getObjectId() {
		return objectId;
	}
	
	/**
	 * Returns the feed id for this zone.
	 * 
	 * @return The feed id for this zone.
	 */
	public String getFeedId() {
		return feedId;
	}
	
	/**
	 * Returns the id for this zone.
	 * 
	 * @return The zone id for this zone.
	 */
	public String getZoneId() {
		return zoneId;
	}
	
	/**
	 * Returns the region for this zone.
	 * 
	 * @return The region for this zone.
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Returns the trigger condition for this zone.
	 * 
	 * @return The trigger condition for this zone.
	 */
	public TriggerCondition getTriggerCondition() {
		return trigger;
	}
	
	/**
	 * Returns the callback for this zone.
	 * 
	 * @return The callback for this zone.
	 */
	public Callback getCallback() {
		return callback;
	}
	
	public String getQuery() {
		return query;
	}
	
	public NameValues getNameValues() {
		return nameValues;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public String toString() {
	  StringBuilder sb = new StringBuilder("{");
	  sb.append("ZoneID: ").append(zoneId);
	  sb.append(", ObjectID: ").append(objectId);
	  sb.append(", FeedID: ").append(feedId);
	  sb.append(", Radius: ").append(radius);
	  sb.append(", TriggerCondition: ").append(trigger);
	  sb.append(", Callback: ").append(callback);
	  sb.append(", Query: ").append(query);
	  sb.append(", isActive: ").append(active);
	  sb.append(", NameValues: ").append(nameValues);
	  sb.append("}");
	  return sb.toString();
	}
	
	
}
