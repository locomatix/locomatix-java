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
 * Point class based on code from this source
 * http://code.google.com/p/location-source/source/browse/trunk/LocationSource/src/java/javax/microedition/location/Coordinates.java
 *
 */
public class Point {

	private double latitude;
	private double longitude;
	
	public Point(double latitude, double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	private void setLatitude(double latitude) {
		if(Double.isNaN(latitude) || (latitude < -89.9999999 || latitude >= 89.9999999)) {
			throw new IllegalArgumentException("Latitude (" + latitude + ") is invalid.");
		} else {
			this.latitude = latitude;
		}
	}
	
	private void setLongitude(double longitude) {
		if(Double.isNaN(longitude) || (longitude < -180.0 || longitude >= 180.0)) {
			throw new IllegalArgumentException("Longitude (" + longitude + ") is invalid.");
		} else {
			this.longitude = longitude;
		}
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(latitude);
		sb.append(", ").append(longitude).append(")");
		
		return sb.toString();
	}
	
	
	public boolean equals(Object other) {
		// this is an allowable difference to account for floating point imprecision.
		final double tolerance = 0.000001;
		
		if(null == other)
			return false;
		// is this a coordinates object?
		if((other instanceof Point) == false)
			return false;
		
		// otherwise it is a Coordinates object
		Point c = (Point) other;
		
		// are the latitudes equal?
		if((latitude < c.latitude - tolerance) || (latitude > c.latitude + tolerance))
			return false;
		
		// are the longitudes equal?
		if((longitude < c.longitude - tolerance) || (longitude > c.longitude + tolerance))
			return false;
		
		// the two coordinates are equal
		return true;
	}

	
	public static Point create(double latitude, double longitude) {
	  return new Point(latitude, longitude);
	}
	
}
