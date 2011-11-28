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


public class Feed {

	private final String feedId;
	private final int locationExpiry;
	private final int objectExpiry;
	private NameValues nameValues = NameValues.emptyNameValues();
	
	public Feed(String feedId) {
		this(feedId, -1, -1, null);
	}
	
	public Feed(String feedId, int locationExpiry, int objectExpiry, NameValues nameValues) {
	    this.feedId = feedId;
	    this.locationExpiry = locationExpiry;
	    this.objectExpiry = objectExpiry;
	    if (null != nameValues) {
	        this.nameValues = nameValues;
	    }
	}
	
	public String getFeedId() {
		return feedId;
	}
	
	public int getObjectExpiry() {
		return objectExpiry;
	}
	
	public int getLocationExpiry() {
		return locationExpiry;
	}
	
	public NameValues getNameValues() {
	  return nameValues;
	}
	
	@Override
	public int hashCode() {
	    return feedId.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
	    if (!(o instanceof Feed))
	        return false;
	    Feed that = (Feed) o;
	    return this.feedId.equals(that.feedId);
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{FeedID: ").append(feedId);
		sb.append(", ObjectExpiry: ");
		String expiry = isForever(objectExpiry) ? "Forever" : Long.toString(objectExpiry);
		sb.append(expiry);
		sb.append(", LocationExpiry: ");
		expiry = isForever(locationExpiry) ? "Forever" : Long.toString(locationExpiry);
		sb.append(expiry);
		sb.append(", NameValues: ").append(nameValues);
		sb.append("}");
		return sb.toString();
	}
	
	static boolean isForever(long expiry) {
		return (expiry < 0);
	}
	
}
