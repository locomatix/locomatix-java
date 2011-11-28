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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.locomatix.util.Lists;
import com.locomatix.util.StringUtils;

public class Polygon extends Region {
	
  public static final class Builder {
    
    private final List<Point> points = Lists.newArrayList();
    
    public Builder add(Point point) {
      if (null == point)
        throw new IllegalArgumentException("Point is null");
      points.add(point);
      return this;
    }
    
    public Builder add(double latitude, double longitude) {
      return add(new Point(latitude, longitude));
    }
    
    
    public Polygon build() {
      return new Polygon(points);
    }
  
  }

  private final List<Point> ring;
	
  public Polygon(List<Point> ring) {
    if (null == ring) 
      throw new IllegalArgumentException("Polygon ring is null");
    this.ring = Collections.unmodifiableList(ring);
    properties.put("region", RegionType.POLYGON.toString().toLowerCase());
    properties.put("points", formatPoints(this.ring));
  }
	
	
  public List<Point> getRing() {
    return ring;
  }
	
  @Override
  public RegionType getRegionType() {
    return RegionType.POLYGON;
  }
	
  private static final String POINT_FORMAT = "%s,%s";
	
  private static String formatPoints(List<Point> points) {
    ArrayList<String> pointsAsStrings = new ArrayList<String>(points.size());
    for(Point point : points) {
      pointsAsStrings.add(String.format(POINT_FORMAT, String.valueOf(point.getLatitude()), 
          String.valueOf(point.getLongitude())));
    }
    return StringUtils.join(pointsAsStrings, "|");
  }
	
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    sb.append("Polygon: ").append(ring);
    sb.append("}");
    return sb.toString();
  }


  public static Polygon create(List<Point> ring) {
    return new Polygon(ring);
  }
	
  public static Polygon create(Point... points) {
	  return new Polygon(Arrays.asList(points));
  }
	
}
