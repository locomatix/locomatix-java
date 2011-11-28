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


public class Circle extends Region {
	
	/**
	 * The radius of the circle in meters.
	 */
	private double radius;
	
	private Point center;
	
	public Circle(Point centerPoint, double radius) {
	  if (null == centerPoint)
      throw new IllegalArgumentException("CenterPoint is null.");
    setRadius(radius);
    center = new Point(centerPoint.getLatitude(), centerPoint.getLongitude());
    properties.put("region", RegionType.CIRCLE.toString().toLowerCase());
    properties.put("radius", String.valueOf(radius));
    properties.put("latitude", String.valueOf(centerPoint.getLatitude()));
    properties.put("longitude", String.valueOf(centerPoint.getLongitude()));
  }
	
	/**
	 * Get the radius of the circle in meters.
	 * 
	 * @return the radius in meters.
	 */
	public double getRadius() {
		return radius;
	}
	
	
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Set the radius for this region in meters.
	 * 
	 * @param radius in meters.
	 */
	private void setRadius(double radius) {
		if(radius < 0 || radius > 100000) {
			throw new IllegalArgumentException("Radius (" + radius + ") is invalid.");
		} else {
			this.radius = radius;
		}
	}
	
	public RegionType getRegionType() {
		return RegionType.CIRCLE;
	}

	@Override
	public String toString() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("Center: ").append(center).append(", ");
	  sb.append("Radius: ").append(radius).append(" meters");
	  return sb.toString();
	}
	
	
	public static Circle create(Point centerPoint, double radius) {
	  return new Circle(centerPoint, radius);
	}
	
	public static Circle create(double latitude, double longitude, double radius) {
    return new Circle(new Point(latitude, longitude), radius);
	}
	
}
