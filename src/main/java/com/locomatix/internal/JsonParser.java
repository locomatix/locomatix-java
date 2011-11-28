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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.locomatix.LocomatixServiceException;
import com.locomatix.model.Callback;
import com.locomatix.model.Circle;
import com.locomatix.model.Feed;
import com.locomatix.model.Fence;
import com.locomatix.model.Item;
import com.locomatix.model.LXObject;
import com.locomatix.model.Location;
import com.locomatix.model.NameValues;
import com.locomatix.model.Point;
import com.locomatix.model.Polygon;
import com.locomatix.model.Region;
import com.locomatix.model.RegionType;
import com.locomatix.model.ResultSet;
import com.locomatix.model.TriggerCondition;
import com.locomatix.model.URLCallback;
import com.locomatix.model.Zone;
import com.locomatix.util.Maps;

/**
 * Collection of unmarshallers for the Locomatix API JSON responses. These are used
 * internally by the ResponseHandlers.
 *  
 */
class JsonParser {
	
	static interface Unmarshaller<T> {
		
		public T unmarshall(JSONObject json) throws Exception;
		
	}
	
	private static void setNextKey(ResultSet.Builder builder, JSONObject json) throws JSONException {
	  if (json.has(ResponseKeys.NEXTKEY)) {
	    String nextKey = json.getString(ResponseKeys.NEXTKEY);
	    builder.setNextToken(nextKey);
	  }
	}
	
	static <T> ResultSet.Builder parseSearchResultSet(JSONObject json) throws JSONException, IOException {
	  if (isAggregate(json)) {
      return parseAggregate(json);
    } else {
      ResultSet.Builder builder = new ResultSet.Builder();
      JSONArray jarray = json.getJSONArray(ResponseKeys.OBJECTS);
      List<Item> list = new ArrayList<Item>(jarray.length());
      JsonObjectParser<?> objectParser = jsonParserMap.get(LXObject.class);
      JsonObjectParser<?> locationParser = jsonParserMap.get(Location.class);
      for (int i=0; i<jarray.length(); ++i) {
        try {
          Item.Builder ib = new Item.Builder();
          LXObject o = LXObject.class.cast(objectParser.parse(jarray.getJSONObject(i)));
          Location l = Location.class.cast(locationParser.parse(jarray.getJSONObject(i)));
          ib.setObject(LXObject.class, o);
          ib.setObject(Location.class, l);
          list.add(ib.build());
        } catch(JSONException je) {
          je.printStackTrace();
          throw new IOException(je);
        }
      }
      builder.setItems(list);
      setNextKey(builder, json);
      return builder;
    }
	}
	
	
	static boolean isAggregate(JSONObject json) {
	  if (json.has(ResponseKeys.COUNT))
	    return true;
	  else if (json.has(ResponseKeys.SUM))
	    return true;
	  else if (json.has(ResponseKeys.MINIMUM))
	    return true;
	  else if (json.has(ResponseKeys.MAXIMUM))
	    return true;
	  else
	    return false;
	}
	
	static <T> ResultSet.Builder parseResultSet(JSONObject json, Class<T> type, String key) 
	  throws IOException, JSONException {
	  if (isAggregate(json)) {
	    return parseAggregate(json);
	  } else {
	    return parseObjectArray(json, type, key);
	  }
	}
	
	static ResultSet.Builder parseAggregate(JSONObject json) throws JSONException {
	  ResultSet.Builder builder = new ResultSet.Builder();
	  if(json.has(ResponseKeys.COUNT)) {
      builder.count(json.getInt(ResponseKeys.COUNT));
    } else if(json.has(ResponseKeys.SUM)) {
      builder.sum(json.getDouble(ResponseKeys.SUM));
    } else if(json.has(ResponseKeys.MINIMUM)) {
      builder.min(json.getDouble(ResponseKeys.MINIMUM));
    } else if(json.has(ResponseKeys.MAXIMUM)) {
      builder.max(json.getDouble(ResponseKeys.MAXIMUM));
    } else {
      throw new LocomatixServiceException("Unkown response: " + json.toString());
    }
	  
	  return builder;
	}
	
	
	static <T> ResultSet.Builder parseObjectArray(JSONObject json, Class<T> type, String key) throws IOException {
	  try {
	    JSONArray jarray = json.getJSONArray(key);
	    List<Item> list = new ArrayList<Item>(jarray.length());
	    JsonObjectParser<?> parser = jsonParserMap.get(type);
	    if (null == parser)
	      throw new IOException("Unknown locomatix response object");
	    for (int i=0; i<jarray.length(); ++i) {
	      try {
	        Item.Builder builder = new Item.Builder();
	        T instance = type.cast(parser.parse(jarray.getJSONObject(i)));
	        builder.setObject(type, instance);
	        list.add(builder.build());
	      } catch(JSONException je) {
	        throw new IOException(je);
	      }
	    }
    
      ResultSet.Builder builder = new ResultSet.Builder().setItems(list);
      setNextKey(builder, json);
      return builder;
	  } catch(JSONException je) {
	    throw new IOException(je);
	  }
	}
	
	
	static <T> T parseObject(JSONObject json, Class<T> type) throws IOException {
	  JsonObjectParser<?> parser = jsonParserMap.get(type);
	  if (null == parser)
	    throw new IOException("Unknown locomatix response object");
	  return type.cast(parser.parse(json));
	}
	
	
	static abstract class JsonObjectParser<T> {
	  
	  T parse(JSONObject json) throws IOException {
	    try {
	      return doParse(json);
	    } catch(JSONException je) {
	      throw new IOException(je);
	    }
	  }
	  
	  abstract T doParse(JSONObject json) throws IOException, JSONException;
	}
	
	
	static <T> List<T> parseArrray(JSONArray jarray, Class<T> type) throws IOException {
	  List<T> list = new ArrayList<T>(jarray.length());
	  JsonObjectParser<?> parser = jsonParserMap.get(type);
	  if (null == parser)
      throw new IOException("Unknown locomatix response object");
	  for (int i=0; i<jarray.length(); ++i) {
	    try {
	      list.add(type.cast(parser.parse(jarray.getJSONObject(i))));
	    } catch(JSONException je) {
	      throw new IOException(je);
	    }
	  }
	  
	  return list;
	}
	
	
	static final Map<Class<?>, JsonObjectParser<?>> jsonParserMap = Maps.newHashMap();
	
	static {
	  jsonParserMap.put(Feed.class, new JsonFeedParser());
	  jsonParserMap.put(LXObject.class, new JsonLXObjectParser());
	  jsonParserMap.put(Location.class, new JsonLocationParser());
	  jsonParserMap.put(Zone.class, new JsonZoneParser());
	  jsonParserMap.put(Fence.class, new JsonFenceParser());
	}
	
	
	static class JsonFeedParser extends JsonObjectParser<Feed> {

    @Override
    Feed doParse(JSONObject json) throws JSONException {
      String feedId = json.getString(ResponseKeys.FEED);
      int locationExpiry = json.getInt(ResponseKeys.LOCATIONEXPIRY);
      int objectExpiry = json.getInt(ResponseKeys.OBJECTEXPIRY);
      
      NameValues nameValues = NameValues.emptyNameValues();
      if (json.has(ResponseKeys.NAMEVALUES)) {
        nameValues = parseNameValues(json.getJSONArray(ResponseKeys.NAMEVALUES));
      }
       
      return new Feed(feedId, locationExpiry, objectExpiry, nameValues);
    }
	  
	}
	
	static class JsonLXObjectParser extends JsonObjectParser<LXObject> {

    @Override
    LXObject doParse(JSONObject json) throws JSONException {
      String objectId = json.getString(ResponseKeys.OBJECTID);
      String feedId = json.getString(ResponseKeys.FEED);
      
      NameValues nameValues = NameValues.emptyNameValues();
      if (json.has(ResponseKeys.OBJECTNAMEVALUES)) {
        nameValues = parseNameValues(json.getJSONArray(ResponseKeys.OBJECTNAMEVALUES));
      }
      
      return new LXObject(objectId, feedId, nameValues);
    }
    
	}
	
	
	
	static class JsonLocationParser extends JsonObjectParser<Location> {

    @Override
    Location doParse(JSONObject json) throws JSONException {
      String feedId = json.getString(ResponseKeys.FEED);
      String objectId = json.getString(ResponseKeys.OBJECTID);
      
      NameValues nameValues = NameValues.emptyNameValues();
      if (json.has(ResponseKeys.LOCATIONNAMEVALUES)) {
        nameValues = parseNameValues(json.getJSONArray(ResponseKeys.LOCATIONNAMEVALUES));
      }
      
      long time = -1;
      if (json.has(ResponseKeys.TIME)) {
        time = json.getLong(ResponseKeys.TIME);
      }
      
      Point coordinate = null;
      if (json.has(ResponseKeys.LATITUDE) && json.has(ResponseKeys.LONGITUDE)) {
        double lat = json.getDouble(ResponseKeys.LATITUDE);
        double lon = json.getDouble(ResponseKeys.LONGITUDE);
        coordinate = new Point(lat, lon);
      }
      
      return new Location(objectId, feedId, nameValues, coordinate, time);
    }
	  
	}
	
	
	static class JsonFenceParser extends JsonObjectParser<Fence> {

    @Override
    Fence doParse(JSONObject json) throws JSONException, MalformedURLException {
      String fenceId = json.getString(ResponseKeys.FENCEID);
      
      Callback callback = unmarshallCallback(json.getJSONObject(ResponseKeys.CALLBACK));
      JSONObject jsonRegion = json.getJSONObject(ResponseKeys.REGION);
      Region region = unmarshallRegion(jsonRegion);
      
      TriggerCondition trigger = 
        TriggerCondition.valueOf(json.getString(ResponseKeys.TRIGGER));
      String predicate = json.getString(ResponseKeys.PREDICATE);
      boolean active = isActive(json.getString(ResponseKeys.STATE));
      
      NameValues nameValues = NameValues.emptyNameValues();
      // are there name value pairs?
      if(json.has(ResponseKeys.NAMEVALUES)) {
        nameValues = parseNameValues(json.getJSONArray(ResponseKeys.NAMEVALUES));
      }
      
      return new Fence(fenceId, region, trigger, callback, predicate, nameValues, active);
    }
	  
	}
	
	static class JsonZoneParser extends JsonObjectParser<Zone> {

    @Override
    Zone doParse(JSONObject json) throws JSONException, MalformedURLException {
      String zoneId = json.getString(ResponseKeys.ZONEID);
      JSONObject followObject = json.getJSONObject(ResponseKeys.FOLLOWOBJECT);
      String objectId = followObject.getString(ResponseKeys.OBJECTID);
      String feedId = followObject.getString(ResponseKeys.FEED);
      
      Callback callback = 
        JsonParser.unmarshallCallback(json.getJSONObject(ResponseKeys.CALLBACK));
      
      JSONObject params = json.getJSONObject(ResponseKeys.REGION).getJSONObject(ResponseKeys.REGIONPARAMS);
      double radius = params.getDouble("Radius");
      
      TriggerCondition trigger = 
        TriggerCondition.valueOf(json.getString(ResponseKeys.TRIGGER));
      String predicate = json.getString(ResponseKeys.PREDICATE);
      
      boolean active = isActive(json.getString(ResponseKeys.STATE));
      
      NameValues nameValues = NameValues.emptyNameValues();
      // are there name value pairs?
      if(json.has(ResponseKeys.NAMEVALUES)) {
        nameValues = 
          parseNameValues(json.getJSONArray(ResponseKeys.NAMEVALUES));
      }
      
      return new Zone(zoneId, objectId, feedId, radius, trigger, callback, predicate, nameValues, active);
    }
	  
	}
	
	
	static NameValues parseNameValues(JSONArray nvarray) throws JSONException {
    NameValues nameValues = NameValues.create();
	  for (int i=0; i<nvarray.length(); ++i) {
      JSONObject nameValue = nvarray.getJSONObject(i);
      if (nameValue.names().length() > 0) {
        String name = nameValue.names().getString(0);
        String value = nameValue.getString(name);
        nameValues.put(name, value);
      }
    }
    return nameValues;
  }
	
	static final class URLCallbackUnmarshaller implements 
		Unmarshaller<URLCallback> {
		
		@Override
		public URLCallback unmarshall(JSONObject json) throws MalformedURLException, JSONException {
			URL url = new URL(json.getString(ResponseKeys.CALLBACKURL));
			return new URLCallback(url);
		}
	}
	
	
	static Callback unmarshallCallback(JSONObject callback) throws JSONException, MalformedURLException {
		String callbackType = callback.getString(ResponseKeys.CALLBACKTYPE);
		if(callbackType.equals("URL")) {
			return new URLCallbackUnmarshaller().unmarshall(callback);
		} else {
			throw new LocomatixServiceException("Unknown callback type: " + callbackType);
		}
	}
	
	
	
	static class CircleRegionUnmarshaller implements Unmarshaller<Circle> {

		@Override
		public Circle unmarshall(JSONObject json) throws JSONException {
			JSONObject params = json.getJSONObject(ResponseKeys.REGIONPARAMS);
			int radius = params.getInt("Radius");
			if(params.has(ResponseKeys.LATITUDE) && params.has(ResponseKeys.LONGITUDE)) {
				double latitude = params.getDouble(ResponseKeys.LATITUDE);
				double longitude = params.getDouble(ResponseKeys.LONGITUDE);
				return Circle.create(new Point(latitude, longitude), radius);
			} 
				
			throw new LocomatixServiceException("Unable to parse: " + json.toString() + " as CircleRegion");
		}
	}
	
	static class PolygonRegionUnmarshaller implements Unmarshaller<Polygon> {

		@Override
		public Polygon unmarshall(JSONObject json) throws JSONException {
			JSONObject params = json.getJSONObject(ResponseKeys.REGIONPARAMS);
			JSONArray pointsArray = params.getJSONArray(ResponseKeys.POINTS);
			JSONObject location;
			ArrayList<Point> points = new ArrayList<Point>(pointsArray.length());
			for(int i=0; i< pointsArray.length(); ++i) {
				location = pointsArray.getJSONObject(i);
				double latitude = location.getDouble(ResponseKeys.LATITUDE);
				double longitude = location.getDouble(ResponseKeys.LONGITUDE);
				points.add(new Point(latitude, longitude));
			}
			
			return new Polygon(points);
		}
		
	}
	
	
	static Region unmarshallRegion(JSONObject region) throws JSONException {
		RegionType regionType = RegionType.fromValue(region.getString(ResponseKeys.REGIONTYPE));
		if(RegionType.CIRCLE == regionType) {
			return new CircleRegionUnmarshaller().unmarshall(region);
		} else if(RegionType.POLYGON == regionType) {
			return new PolygonRegionUnmarshaller().unmarshall(region);
		} else {
			throw new LocomatixServiceException("Unknown region type: " + regionType);
		}
	}
	
	static boolean isActive(String state) {
		return "active".equalsIgnoreCase(state);
	}
	
	
}
