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
 * A Fence represents a static region that will be continually evaluated continually 
 * evaluated by the server.  A notification will be sent to the callback URL when any 
 * object enters or leaves this fenced region. A list of the current objects in the 
 * fenced region is sent in the alert message. 
 *
 */
public class Fence {

	// the id of this fence
	private String fenceId;
	
	// the details about the region for this fence
	private Region region;
	
	// the trigger condition for this fence
	private TriggerCondition trigger;
	
	// the callback for this fence
	private Callback callback;
	
	private String query;
	
	private NameValues nameValues = NameValues.emptyNameValues();
	
	private boolean active;
	
	public Fence(String fenceId, Region region, TriggerCondition trigger, 
			Callback callback, String query, boolean active) {
		this.fenceId = fenceId;
		this.region = region;
		this.trigger = trigger;
		this.callback = callback;
		this.query = query;
		this.active = active;
	}
	
	
	public Fence(String fenceId, Region region, TriggerCondition trigger, 
			Callback callback, String query, NameValues nameValues, boolean active) {
		this(fenceId, region, trigger, callback, query, active);
		if(null != nameValues) {
			this.nameValues = nameValues;
		}
	}
	
	
	/**
	 * Returns the id of this fence.
	 * 
	 * @return The id of this fence.
	 */
	public String getFenceId() {
		return fenceId;
	}
	
	
	/**
	 * Gets the Region for this fence.
	 * 
	 * @return The Region for this fence.
	 */
	public Region getRegion() {
		return region;
	}
	
	/**
	 * Gets the TriggerCondition for this fence.
	 * 
	 * @return The TriggerCondition for this fence.
	 */
	public TriggerCondition getTriggerCondition() {
		return trigger;
	}
	
	/**
	 * Get the Callback for this fence.
	 * 
	 * @return The Callback for this fence.
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
	  sb.append("FenceID: ").append(fenceId);
	  sb.append(", Region: ").append(region.getProperties());
	  sb.append(", TriggerCondition: ").append(trigger);
	  sb.append(", Callback: ").append(callback);
	  sb.append(", Query: ").append(query);
	  sb.append(", isActive: ").append(active);
	  sb.append(", NameValues: ").append(nameValues);
	  sb.append("}");
	  return sb.toString();
	}
	
}
