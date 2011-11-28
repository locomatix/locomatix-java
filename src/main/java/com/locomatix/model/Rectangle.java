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
import java.util.Collections;
import java.util.List;

public class Rectangle extends Polygon {
  
  private final Point southWest;
  private final Point northEast;
  
  public Rectangle(Point southWest, Point northEast) {
    super(createRectangle(southWest, northEast));
    this.southWest = southWest;
    this.northEast = northEast;
  }
  
  private static List<Point> createRectangle(Point southWest, Point northEast) {
    if (null == southWest)
      throw new IllegalArgumentException("southwest point is null");
    if (null == northEast)
      throw new IllegalArgumentException("northeast point is null");
    List<Point> points = new ArrayList<Point>(4);
    points.add(southWest);
    points.add(new Point(northEast.getLatitude(), southWest.getLongitude()));
    points.add(northEast);
    points.add(new Point(southWest.getLatitude(), northEast.getLongitude()));
    points.add(southWest);
    return Collections.unmodifiableList(points);
  }
  
  public Point getSouthWest() {
    return southWest;
  }
  
  public Point getNorthEast() {
    return northEast;
  }
  
  public static Rectangle create(Point southWest, Point northEast) {
    return new Rectangle(southWest, northEast);
  }
  
  public static Rectangle create(double swLatitude, double swLongitude, 
      double neLatitude, double neLongitude) {
    return create(new Point(swLatitude, swLongitude), new Point(neLatitude, neLongitude));
  }
  
}
