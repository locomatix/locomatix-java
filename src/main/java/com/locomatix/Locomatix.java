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

package com.locomatix;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.locomatix.auth.LocomatixCredentials;
import com.locomatix.http.HttpClient;
import com.locomatix.http.HttpRequest;
import com.locomatix.internal.ErrorResponseHandler;
import com.locomatix.internal.FeedResponseHandler;
import com.locomatix.internal.FenceKeyResponseHandler;
import com.locomatix.internal.GetAttributesResponseHandler;
import com.locomatix.internal.GetFenceResponseHandler;
import com.locomatix.internal.GetLocationResponseHandler;
import com.locomatix.internal.GetZoneResponseHandler;
import com.locomatix.internal.LXObjectKeyResponseHandler;
import com.locomatix.internal.ListFeedsResponseHandler;
import com.locomatix.internal.ListFencesResponseHandler;
import com.locomatix.internal.ListObjectsResponseHandler;
import com.locomatix.internal.ListZonesResponseHandler;
import com.locomatix.internal.ObjectGridResponseHandler;
import com.locomatix.internal.QueryResponseHandler;
import com.locomatix.internal.ZoneKeyResponseHandler;
import com.locomatix.model.Callback;
import com.locomatix.model.Circle;
import com.locomatix.model.Grid;
import com.locomatix.model.LXObject;
import com.locomatix.model.NameValues;
import com.locomatix.model.Point;
import com.locomatix.model.Rectangle;
import com.locomatix.model.RegionType;
import com.locomatix.model.ResultSet;
import com.locomatix.model.TriggerCondition;

public class Locomatix {

  public static final String DEFAULT_LOCOMATIX_VERSION = "0.9";
  public static final String DEFAULT_FETCH_STARTKEY = "";
  public static final int DEFAULT_FETCH_SIZE = 100;
  
  private LocomatixCredentials credentials;
  
  private ClientConfiguration clientConfiguration;
  
  private HttpClient client;
  
  private final Map<String, String> headers = new HashMap<String, String>();
  
  private URI endpoint;
  
  // handles errors returned by the server
  private ErrorResponseHandler errorResponseHandler = new ErrorResponseHandler();
  
  private volatile int executionTime;
  
  public Locomatix(LocomatixCredentials credentials, ClientConfiguration clientConfiguration) {
    this.credentials = credentials;
    this.clientConfiguration = ClientConfiguration.newInstance(clientConfiguration);
    createHeaders();
    setEndpoint(this.clientConfiguration.getHost());
    this.client = new HttpClient(this.clientConfiguration);
  }
  
  
  /**
   * Creates the lx credentials headers used in each request to the Locomatix REST service.
   */
  private void createHeaders() {
    headers.put(Headers.LX_API_VERSION, DEFAULT_LOCOMATIX_VERSION);
    headers.put(Headers.LX_CUST_ID, credentials.getCustomerId());
    headers.put(Headers.LX_CUST_KEY, credentials.getCustomerKey());
    headers.put(Headers.LX_SECRET_KEY, credentials.getSecretKey());
  }
  
  
  /**
   * Set the endpoint for this client.
   * 
   * @param endpoint the endpoint for this client
   * 
   * @throws IllegalArgumentException
   *     If any problems are detected with the specified endpoint.
   */
  private void setEndpoint(String endpoint) throws IllegalArgumentException {
    if (endpoint.contains("://") == false) {
      endpoint = clientConfiguration.getProtocol().toString() + "://" + endpoint;
    }
    try {
      this.endpoint =  new URI(endpoint);
    } catch(URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
  
  public void shutdown() {
    client.shutdown();
  }
  
  private void setLastExecutionTime(LocomatixRequest lastRequest, 
          LocomatixResponse<? extends Object> lastResponse) {
    executionTime = lastResponse.getResponseMetadata().getExecutionTime();
    lastRequest.setExecutionTime(executionTime);
  }
  
  
  public Feed feeds() {
    return new Feed();
  }
  
  public final class Feed {
    
    public Create create(String feedId) {
      return new Create(feedId);
    }
    
    public class Create extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/Create.json";
      
      private final String feedId;
      private int objectExpiry = -1;
      private int locationExpiry = -1;
      private NameValues nameValues = NameValues.create();
      
      
      public Create(String feedId) {
        this.feedId = feedId;
      }
      
      public Create withLocationExpiry(int locationExpiry) {
        this.locationExpiry = locationExpiry;
        return this;
      }
      
      public Create withObjectExpiry(int objectExpiry) {
        this.objectExpiry = objectExpiry;
        return this;
      }
      
      public Create withNameValue(String name, String value) {
        if (null != name && null != value) {
          this.nameValues.put(name, value);
        }
        return this;
      }
      
      public Create withNameValues(NameValues nameValues) {
        if (null != nameValues) {
          this.nameValues = nameValues;
        }
        return this;
      }
      
      public void execute() {
        assertParameterNotNull(feedId, "");

        HttpRequest request = new HttpRequest(HttpMethod.POST, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.FEED, feedId);
        request.addParameter(Parameters.OBJECTEXPIRY, String.valueOf(objectExpiry));
        request.addParameter(Parameters.LOCATIONEXPIRY, String.valueOf(locationExpiry));
        addNameValuesParameters(request, nameValues);
        
        LocomatixResponse<String> response = 
            client.execute(request, new FeedResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }
      
    } // end Feed.Create
    
    
    public Delete delete(String feedId) {
      return new Delete(feedId);
    }
    
    
    public class Delete extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/Delete.json";
      
      private final String feedId;
      
      public Delete(String feedId) {
        this.feedId = feedId;
      }
      
      public void execute() {
        assertParameterNotNull(feedId, "");
                
        HttpRequest request = new HttpRequest(HttpMethod.DELETE, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.FEED, feedId);
        
        LocomatixResponse<String> response = 
            client.execute(request, new FeedResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }
      
    } // end Feed.Delete
    
    
    public com.locomatix.Locomatix.Feed.List list() {
      return new com.locomatix.Locomatix.Feed.List();
    }
    
    public class List extends LocomatixRequest implements PageableRequest {
      
      private static final String REST_PATH = "/feed/List.json";
      
      private String startKey;
      
      public List withNextToken(String startKey) {
        this.startKey = startKey;
        return this;
      }
      
      public ResultSet execute() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        if (null != startKey) {
         request.addParameter(Parameters.STARTKEY, startKey);
        }
        request.addParameter(Parameters.FETCHSIZE, String.valueOf(DEFAULT_FETCH_SIZE));
        
        LocomatixResponse<ResultSet.Builder> response = 
          client.execute(request, new ListFeedsResponseHandler(), errorResponseHandler);
        
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
      
    } // Feed.List
    
  }
  
  
  public Objects objects(String feedId)  {
    return new Objects(feedId);
  }
  
  
  public final class Objects {
    
    private final String feedId;
    
    public Objects(String feedId) {
      this.feedId = feedId;
    }
    
    
    public Create create(String objectId) {
      return new Create(objectId);
    }
    
    
    public class Create extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/%s/object/Create.json";
      
      private final String objectId;
      private NameValues nameValues = NameValues.create();
      
      private boolean hasLocation;
      private double latitude;
      private double longitude;
      private long timestamp;
      
      public Create(String objectId) {
        this.objectId = objectId;
      }
      
      public Create withNameValue(String name, String value) {
        nameValues.put(name, value);
        return this;
      }

      public Create withNameValues(NameValues nameValues) {
        if (null != nameValues) {
          this.nameValues = nameValues;
        }
        return this;
      }
      
      public Create withLocation(double latitude, double longitude, long timestamp) {
        hasLocation = true;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        return this;
      }

      public Create withLocation(Point point, long timestamp) {
        if (null == point)
          throw new NullPointerException("Point is null");
        return withLocation(point.getLatitude(), point.getLongitude(), timestamp);
      }

      public void execute() {
        assertParameterNotNull(objectId, "The object id parameter must be specified when creating an object");
        assertParameterNotNull(feedId, "The feed id parameter must be specified when creating an object");
          
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.POST, path);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.OID, objectId);
        addNameValuesParameters(request, nameValues);
        
        if (hasLocation) {
          request.addParameter(Parameters.LATITUDE, String.valueOf(latitude));
          request.addParameter(Parameters.LONGITUDE, String.valueOf(longitude));
          request.addParameter(Parameters.TIME, String.valueOf(timestamp));
        }
        
        LocomatixResponse<Void> response = 
            client.execute(request, new LXObjectKeyResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }
      
    } // end Objects.Create
    
    
    public Delete delete(String objectId) {
      return new Delete(objectId);
    }
    
    
    public class Delete extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/%s/object/Delete.json";
      
      private final String objectId;
      
      public Delete(String objectId) {
        this.objectId = objectId;
      }
      
      public void execute() {
        assertParameterNotNull(objectId, "The object id parameter must be specified when deleting an object");
        assertParameterNotNull(feedId, "The feed id parameter must be specified when deleting an object");
                
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.DELETE, path);
        request.setEndpoint(endpoint);
        
        request.setHeaders(headers);
        request.addParameter(Parameters.OID, objectId);
        
        LocomatixResponse<Void> response = 
            client.execute(request, new LXObjectKeyResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }
    
    } // end Objects.Delete
    
    
    
    public Get get(String objectId) {
      return new Get(objectId);
    }
    
    
    public class Get extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/%s/object/GetAttributes.json";
      
      private final String objectId;
      
      public Get(String objectId) {
        this.objectId = objectId;
      }
      
      
      public LXObject execute() {
        assertParameterNotNull(objectId, "The object id parameter must be specified when requesting an object's attributes");
        assertParameterNotNull(feedId, "The feed id parameter must be specified when requesting an object's attributes");
    
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.GET, path);
        request.setEndpoint(endpoint);
    
        request.setHeaders(headers);
        request.addParameter(Parameters.OID, objectId);
    
        LocomatixResponse<LXObject> response = 
          client.execute(request, new GetAttributesResponseHandler(), errorResponseHandler);
        
        setLastExecutionTime(this, response);
        return response.getResult();
      }
      
    } // end Objects.Get
    
    
    public Update update(String objectId) {
      return new Update(objectId);
    }
    
    
    public class Update extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/%s/object/UpdateAttributes.json";
      
      private final String objectId;
      private NameValues nameValues = NameValues.create();
      
      public Update(String objectId) {
        this.objectId = objectId;
      }
      
      public Update withNameValue(String name, String value) {
        nameValues.put(name, value);
        return this;
      }

      public Update withNameValues(NameValues nameValues) {
        if (null != nameValues) {
          this.nameValues = nameValues;
        }
        return this;
      }

      public void execute() {
        assertParameterNotNull(objectId, "The object id parameter must be specified when updating an object's attributes");
        assertParameterNotNull(feedId, "The feed id parameter must be specified when updating an object's attributes");
        
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.PUT, path);
        request.setEndpoint(endpoint);
        
        request.setHeaders(headers);
        request.addParameter(Parameters.OID, objectId);
        addNameValuesParameters(request, nameValues);
        
        LocomatixResponse<Void> response = 
            client.execute(request, new LXObjectKeyResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }
      
    } // end Objects.Update
    
    
    public com.locomatix.Locomatix.Objects.List list() {
      return new List();
    }
    
    
    public class List extends LocomatixRequest implements PageableRequest {
      
      private static final String REST_PATH = "/ListObjects.json";
      
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
      
      private String startKey = null;
      private int fetchSize = DEFAULT_FETCH_SIZE;
      
      public List() {
          lql.select("*").from(feedId);
      }
      
      
      public List withNextToken(String startKey) {
        this.startKey = startKey;
        return this;
      }
      
      public ResultSet execute() {
        assertParameterNotNull(feedId, "The feed id parameter must be specified when requesting a list of objects");
        
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        
        request.setHeaders(headers);
        
        String query = String.format("select * from %s", feedId);
        request.addParameter(Parameters.PREDICATE, query);
        
        // is there a start key?
        if (null != startKey) {
          request.addParameter(Parameters.STARTKEY, startKey);
        }
        request.addParameter(Parameters.FETCHSIZE, String.valueOf(fetchSize));
        
        LocomatixResponse<ResultSet.Builder> response = 
          client.execute(request, new ListObjectsResponseHandler(), errorResponseHandler);
        
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
      
    } // end Objects.List

    public com.locomatix.Locomatix.Objects.Query query() {
      return new Query();
    }

    public class Query extends LocomatixRequest implements PageableRequest {
      
      private static final String REST_PATH = "/ListObjects.json";
      
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
      
      private String startKey = null;
      private int fetchSize = DEFAULT_FETCH_SIZE;
      
      public Query() {
        lql.select("*").from(feedId);
      }
      
      public Query withNextToken(String startKey) {
        this.startKey = startKey;
        return this;
      }

      public Query select(String ...fields) {
        if (null != fields) {
          lql.select(fields);
        }
        return this;
      }
      
      public Query where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }
      
      public ResultSet execute() {
        assertParameterNotNull(feedId, "The feed id parameter must be specified when requesting a list of objects");
        
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        
        request.setHeaders(headers);
        request.addParameter(Parameters.PREDICATE, lql.toLql());
        
        // is there a start key?
        if (null != startKey) {
          request.addParameter(Parameters.STARTKEY, startKey);
        }
        request.addParameter(Parameters.FETCHSIZE, String.valueOf(fetchSize));
        
        LocomatixResponse<ResultSet.Builder> response = 
          client.execute(request, new ListObjectsResponseHandler(), errorResponseHandler);
        
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
    } // end Objects.Query
    
  } // Objects
  
  
  public com.locomatix.Locomatix.Location location(String feedId) {
    return new com.locomatix.Locomatix.Location(feedId);
  }
  
  
  public class Location {
    
    private final String feedId;
    
    public Location(String feedId) {
      this.feedId = feedId;
    }
    
    
    public Update update(String objectId, double latitude, double longitude, long timestamp) {
      return new Update(objectId, latitude, longitude, timestamp);
    }
    
    public Update update(String objectId, Point point, long timestamp) {
      if (null == point)
        throw new NullPointerException("Null point");
      return new Update(objectId, point.getLatitude(), point.getLongitude(), timestamp);
    }
    
    public class Update extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/%s/object/UpdateLocation.json";
      
      private final String objectId;
      private final double latitude;
      private final double longitude;
      private final long timestamp;
      private NameValues attributes = NameValues.create();
      
      public Update(String objectId, double latitude, double longitude, long timestamp) {
        this.objectId = objectId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
      }

      public Update withNameValue(String name, String value) {
        if (null != name && null != value) {
          this.attributes.put(name, value);
        }
        return this;
      }
      
      public Update withNameValues(NameValues nameValues) {
        if (null != nameValues) {
          this.attributes = nameValues;
        }
        return this;
      }
      
      public void execute() {
        assertParameterNotNull(objectId, "The object id parameter must be specified when updating a location");
        assertParameterNotNull(feedId, "The feed id parameter must be specified when updating a location");
        
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.PUT, path);
        request.setEndpoint(endpoint);
        
        request.setHeaders(headers);
        
        request.addParameter(Parameters.OID, objectId);
        request.addParameter(Parameters.LATITUDE, String.valueOf(latitude));
        request.addParameter(Parameters.LONGITUDE, String.valueOf(longitude));
        request.addParameter(Parameters.TIME, String.valueOf(timestamp));
        addNameValuesParameters(request, attributes);
        
        LocomatixResponse<Void> response = 
            client.execute(request, new LXObjectKeyResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }
      
    } // end Location.Update
    
    
    public com.locomatix.Locomatix.Location.Get get(String objectId) {
      return new com.locomatix.Locomatix.Location.Get(objectId);
    }
    
    public class Get extends LocomatixRequest {
      
      private static final String REST_PATH = "/feed/%s/object/GetLocation.json";
      
      private final String objectId;
      private boolean allowExpired;
      
      public Get(String objectId) {
        this.objectId = objectId;
      }

      public Get allowExpired(boolean allowExpired) {
        this.allowExpired = allowExpired;
        return this;
      }
      
      public com.locomatix.model.Location execute() {
        assertParameterNotNull(objectId, "The object id parameter must be specified when fetching a location");
        assertParameterNotNull(feedId, "The feed id parameter must be specified when fetching a location");
                
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.GET, path);
        request.setEndpoint(endpoint);
        
        request.setHeaders(headers);
        
        request.addParameter(Parameters.OID, objectId);
        
        if(allowExpired) {
          request.addParameter(Parameters.ALLOWEDEXPIRED, "true");
        }
        
        LocomatixResponse<com.locomatix.model.Location> response = 
          client.execute(request, new GetLocationResponseHandler(), errorResponseHandler);
        
        setLastExecutionTime(this, response);
        return response.getResult();
      }
      
    } // end Location.Get
    
    
  } // end Location
  
  
  
  public Search search(String feedId) {
    return new Search(feedId);
  }
  
  public class Search {
    
    private final String fromFeed;
    
    public Search(String fromFeed) {
      this.fromFeed = fromFeed;
    }
    
    public Locomatix.Search.Region region(com.locomatix.model.Region region) {
      return new Region(region);
    }
    
    
    public class Region extends LocomatixRequest implements PageableRequest {
      
      private static final String REST_PATH = "/search/Region.json";
      
      private com.locomatix.model.Region region;
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
      
      private String startKey;
      
      public Region(com.locomatix.model.Region region) {
        this.region = region;
        lql.select("*", "location.*").from(fromFeed);
      }
      
      public Region select(String ...fields) {
        if (null != fields) {
            lql.select(fields);
        }
        return this;
      }
      
      public Region where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }
      
      
      public Region withNextToken(String nextToken) {
        this.startKey = nextToken;
        return this;
      }
      
      public ResultSet execute() {
        assertParameterNotNull(fromFeed, "The feed parameter must be specified when searching a region");
        assertParameterNotNull(region, "The region parameter must be specified when searching a region");
        if (region instanceof Circle) {
          assertCircleRegionCenterNotNull((Circle) region);
        }
        
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameters(region.getProperties());
        request.addParameter(Parameters.PREDICATE, lql.toLql());
        if(null != startKey) {
          request.addParameter(Parameters.STARTKEY, startKey);
        }
        request.addParameter(Parameters.FETCHSIZE, String.valueOf(DEFAULT_FETCH_SIZE));
        
        LocomatixResponse<ResultSet.Builder> response = 
          client.execute(request, new QueryResponseHandler(), errorResponseHandler);
        
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
      
    } // end Search.Region
    
    
    public Nearby nearby(String objectId, String feedId) {
      return new Nearby(objectId, feedId);
    }
    
    
    public class Nearby extends LocomatixRequest implements PageableRequest {
      
      private static final String REST_PATH = "/search/NearbyObject.json";
      
      private final String objectId;
      private final String feedId;
      private double radius = 500d;
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
      
      private String startKey;
      
      public Nearby(String objectId, String feedId) {
        this.objectId = objectId;
        this.feedId = feedId;
        lql.select("*", "location.*").from(feedId);
      }
      
      public Nearby select(String ...fields) {
        if (null != fields) {
          lql.select(fields); 
        }
        return this;
      }
      
      public Nearby where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }
      
      public Nearby withRadius(double radius) {
        if (radius < 0)
          throw new IllegalArgumentException("radius (" + radius + ") must be positive");
        this.radius = radius;
        return this;
      }
      
      public Nearby withNextToken(String startKey) {
          this.startKey = startKey;
          return this;
      }
      
      public ResultSet execute() {
        assertParameterNotNull(objectId, "The object id parameter must be specified when searching nearby");
        assertParameterNotNull(feedId, "The feed parameter must be specified when searching nearby");
        
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        
        request.setHeaders(headers);
        
        request.addParameter(Parameters.FEED, feedId);
        request.addParameter(Parameters.OID, objectId);
        request.addParameter(Parameters.REGION, RegionType.CIRCLE.toString().toLowerCase());
        request.addParameter("radius", String.valueOf(radius));
        request.addParameter(Parameters.PREDICATE, lql.toLql());
        
        if(null != startKey) {
          request.addParameter(Parameters.STARTKEY, startKey);
        }
        request.addParameter(Parameters.FETCHSIZE, String.valueOf(DEFAULT_FETCH_SIZE));
        LocomatixResponse<ResultSet.Builder> response = 
          client.execute(request, new QueryResponseHandler(), errorResponseHandler);
        
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
      
    } // end Search.Nearby
    
  }
  
  
  //--------------------------------- Zone API --------------------------------------------
  
  public Locomatix.Zone zones(String objectid, String feedid) {
    return new Zone(objectid, feedid);
  }
    
  public final class Zone {
    private final String feedId;
    private final String objectId;

    public Zone(String objectId, String feedId) {
      this.objectId = objectId;
      this.feedId = feedId;
    }
       
    public Locomatix.Zone.Create create(String zoneId, Callback callback, String fromFeed) {
      return new Create(zoneId, callback, fromFeed);
    }
      
    public class Create extends LocomatixRequest {
      
      private static final String REST_PATH = "/zone/%s/Create.json";
      	
      private final String zoneId;
      private double radius = 500d; // default to 500 meters
      private TriggerCondition triggerCondition = TriggerCondition.Ingress;
      private final Callback callback;
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
      private NameValues nameValues = NameValues.create(); 
				
      // Should we make zones as default 250 meters?
      public Create(String zoneId, Callback callback, String fromFeed) {
        this.zoneId = zoneId;
      	lql.from(fromFeed);
        this.callback = callback;
      }
        
      public Create withRadius(double radius) {
        this.radius = radius;
        return this;
      }
      
      public Create withTriggerCondition(TriggerCondition triggerCondition) {
        assertParameterNotNull(triggerCondition, "TriggerCondition is null");
        this.triggerCondition = triggerCondition;
        this.triggerCondition = null;
        return this;
      }
      
      public Create withNameValue(String name, String value) {
        if (null != name && null != value) {
          this.nameValues.put(name, value);
        }
        return this;
      }
        
      public Create withNameValues(NameValues nameValues) {
        if (null != nameValues) {
          this.nameValues = nameValues;
        }
        return this;
      }

      public Create where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }

      public void execute() {
        assertParameterNotNull(zoneId, "A zoneId must be specified when creating a zone");
        assertParameterNotNull(objectId, "An objectId must be specified when creating a zone");
	    	    
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.POST, path);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.ZONEID, zoneId);
        request.addParameter(Parameters.OID, objectId);
        request.addParameter(Parameters.REGION, RegionType.CIRCLE.toString().toLowerCase());
        request.addParameter("radius", String.valueOf(radius));
        request.addParameter(Parameters.TRIGGER, triggerCondition.toString());
        request.addParameters(callback.getProperties());
        request.addParameter(Parameters.PREDICATE, lql.toString());
	    	    
        addNameValuesParameters(request, nameValues);
	    	    
        LocomatixResponse<Void> response = 
	    	        client.execute(request, new ZoneKeyResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }
				
    } // end Zone.Create
			
	    
    public Delete delete(String zoneId) {
      return new Delete(zoneId);
    }
			
    public class Delete extends LocomatixRequest {
          
      private static final String REST_PATH = "/zone/%s/Delete.json";
      
      private final String zoneId;
          
      public Delete(String zoneId) {
        this.zoneId = zoneId;
      }
        
      public void execute() {
        assertParameterNotNull(zoneId, "");
        assertParameterNotNull(objectId, "");
              
        String path = createURI(REST_PATH, feedId);
        HttpRequest request = new HttpRequest(HttpMethod.DELETE, path);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.ZONEID, zoneId);
        request.addParameter(Parameters.OID, objectId);
        LocomatixResponse<Void> response = 
                  client.execute(request, new ZoneKeyResponseHandler(), errorResponseHandler);
        setLastExecutionTime(this, response);
      }

    } // end Zone.Delete

      
     public Activate activate(String zoneId) {
       return new Activate(zoneId);
     }

     public class Activate extends LocomatixRequest {
          
       private static final String REST_PATH = "/zone/%s/Activate.json";
        
       private final String zoneId;
          
       public Activate(String zoneId) {
         this.zoneId = zoneId;
       }
          
       public void execute() {
         assertParameterNotNull(zoneId, "");
         assertParameterNotNull(objectId, "");
         assertParameterNotNull(feedId, "");
                
         String path = createURI(REST_PATH, feedId);
         HttpRequest request = new HttpRequest(HttpMethod.POST, path);
         request.setEndpoint(endpoint);
         request.setHeaders(headers);
         request.addParameter(Parameters.ZONEID, zoneId);
         request.addParameter(Parameters.OID, objectId);
         LocomatixResponse<Void> response = 
                    client.execute(request, new ZoneKeyResponseHandler(), errorResponseHandler);
         setLastExecutionTime(this, response);
       }
      }

      public Deactivate deactivate(String zoneId) {
        return new Deactivate(zoneId);
      }

      public class Deactivate extends LocomatixRequest {
          
        private static final String REST_PATH = "/zone/%s/DeActivate.json";
        
        private String zoneId;
          
        public Deactivate(String zoneId) {
          this.zoneId = zoneId;
        }
          
        public void execute() {
          assertParameterNotNull(zoneId, "");
          assertParameterNotNull(objectId, "");
          assertParameterNotNull(feedId, "");
              
          String path = createURI(REST_PATH, feedId);
          HttpRequest request = new HttpRequest(HttpMethod.POST, path);
          request.setEndpoint(endpoint);
          request.setHeaders(headers);
          request.addParameter(Parameters.ZONEID, zoneId);
          request.addParameter(Parameters.OID, objectId);
          LocomatixResponse<Void> response = 
                    client.execute(request, new ZoneKeyResponseHandler(), errorResponseHandler);
          setLastExecutionTime(this, response);
        }
      
      } // end Zone.Deactive
      
      
      public Get get(String zoneId) {
          return new Get(zoneId);
      }
      
      public class Get extends LocomatixRequest {
          
        private static final String REST_PATH = "/zone/%s/Get.json";
          
        private final String zoneId;
        
        public Get(String zoneId) {
          this.zoneId = zoneId;
        }
          
        public com.locomatix.model.Zone execute() {
          assertParameterNotNull(zoneId, 
                      "The zone id parameter must be specified when fetching a zone");
          assertParameterNotNull(objectId, 
                      "The object id parameter must be specified when fetching a zone");
          assertParameterNotNull(feedId, 
                      "The feed id parameter must be specified when fetching a zone");
              
          String path = createURI(REST_PATH, feedId);
          HttpRequest request = new HttpRequest(HttpMethod.GET, path);
          request.setEndpoint(endpoint);
          request.setHeaders(headers);
          request.addParameter(Parameters.OID, objectId);
          request.addParameter(Parameters.ZONEID, zoneId);
              
          LocomatixResponse<com.locomatix.model.Zone> response = 
                    client.execute(request, new GetZoneResponseHandler(), errorResponseHandler);
              
          setLastExecutionTime(this, response);
          return response.getResult();
        }
      
      } // end Zone.Get
      
      public Locomatix.Zone.List list() {
        return new List();
      }
      
      public class List extends LocomatixRequest implements PageableRequest {
      
        private static final String REST_PATH = "/zone/%s/List.json";
          
        private String startKey;
          
        public List withNextToken(String startKey) {
          this.startKey = startKey;
          return this;
        }
          
        public ResultSet execute() {
          assertParameterNotNull(objectId, 
                      "The object id parameter must be specified when listing zones");
          assertParameterNotNull(feedId, 
                      "The feed id parameter must be specified when creating listing zones");
              
          String path = createURI(REST_PATH, feedId);
          HttpRequest request = new HttpRequest(HttpMethod.GET, path);
          request.setEndpoint(endpoint);
          request.setHeaders(headers);
          request.addParameter(Parameters.OID, objectId);
          if (null != startKey) {
            request.addParameter(Parameters.STARTKEY, startKey);
          }
          request.addParameter(Parameters.FETCHSIZE, String.valueOf(DEFAULT_FETCH_SIZE));
             
          LocomatixResponse<ResultSet.Builder> response = 
                 client.execute(request, new ListZonesResponseHandler(), errorResponseHandler);
              
          response.getResult().pageableRequest(this);
          setLastExecutionTime(this, response);
          return response.getResult().build();
        }
      
      } // end Zone.List
      
  } // end Zones

  
  //--------------------------------- Fence API --------------------------------------------
    
  public Fence fences() {
    return new Fence();
  }
      
  public final class Fence {
      
    public Fence() {
      //this(null);
    }
      
    public Create create(String fenceId, com.locomatix.model.Region region, 
        Callback callback, String fromFeed) {
      return new Create(fenceId, region, callback, fromFeed);
    }
          
    public class Create extends LocomatixRequest {
          
      private static final String REST_PATH = "/fence/Create.json";
        
      private final String fenceId;
      private final com.locomatix.model.Region region;
      private TriggerCondition triggerCondition = TriggerCondition.Ingress;
      private final Callback callback;
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
      private NameValues nameValues = NameValues.create();
	            
      public Create(String fenceId, com.locomatix.model.Region region, Callback callback, String fromFeed) {
        this.fenceId = fenceId;
        this.region = region;
        lql.from(fromFeed);
        this.callback = callback;
      }
	      
      public Create withTriggerCondition(TriggerCondition triggerCondition) {
        assertParameterNotNull(triggerCondition, "trigger condition must not be null");
        this.triggerCondition = triggerCondition;
        return this;
      }
      
      public Create withNameValue(String name, String value) {
        if (null != name && null != value) {
	        this.nameValues.put(name, value);
        }
	      return this;
      }
	            
      public Create withNameValues(NameValues nameValues) {
        if (null != nameValues) {
          this.nameValues = nameValues;
        }
        return this;
      }
	            
      public Create where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }

      public void execute() {
        assertParameterNotNull(fenceId, "A fenceId is required when creating a fence");
        assertParameterNotNull(region, "A region is required when creating a a fence");
        assertParameterNotNull(callback, "A callback is required when creating a fence");
        // is this a circle region?
        if (region instanceof Circle) {
          assertCircleRegionCenterNotNull((Circle) region);
        }

        HttpRequest request = new HttpRequest(HttpMethod.POST, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.FENCEID, fenceId);
        request.addParameters(region.getProperties());
        request.addParameter(Parameters.TRIGGER, triggerCondition.toString());
        request.addParameters(callback.getProperties());
        request.addParameter(Parameters.PREDICATE, lql.toLql());
	         
        addNameValuesParameters(request, nameValues);
        client.execute(request, new FenceKeyResponseHandler(), errorResponseHandler);
      }
	            
    } // end Fence.Create
	       
    public Delete delete(String fenceId) {
      return new Delete(fenceId);
    }
	        
    public class Delete extends LocomatixRequest {
	        
      private static final String REST_PATH = "/fence/Delete.json";
	        
      private final String fenceId;
	        
      public Delete(String fenceId) {
        this.fenceId = fenceId;
      }
	        
      public void execute() {
        assertParameterNotNull(fenceId, "");
	            
        HttpRequest request = new HttpRequest(HttpMethod.DELETE, REST_PATH);
	      request.setEndpoint(endpoint);
	      request.setHeaders(headers);
	      request.addParameter(Parameters.FENCEID, fenceId);
	      client.execute(request, new FenceKeyResponseHandler(), errorResponseHandler);
      }

    } // end Fence.Delete

    public Activate activate(String fenceId) {
      return new Activate(fenceId);
    }

    public class Activate extends LocomatixRequest {
      private static final String REST_PATH = "/fence/Activate.json";
              
      private final String fenceId;
          
      public Activate(String fenceId) {
        this.fenceId = fenceId;
      }
     
      public void execute() {
        assertParameterNotNull(fenceId, "");
                
        HttpRequest request = new HttpRequest(HttpMethod.POST, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.FENCEID, fenceId);
        client.execute(request, new FenceKeyResponseHandler(), errorResponseHandler);
      }
          
    } // end Fence.Activate

    public Deactivate deactivate(String fenceId) {
      return new Deactivate(fenceId);
    } 

    public class Deactivate extends LocomatixRequest {
          
      private static final String REST_PATH = "/fence/DeActivate.json";
          
      private final String fenceId;
       
      public Deactivate(String fenceId) {
        this.fenceId = fenceId;
      }
     
      public void execute() {
        assertParameterNotNull(fenceId, "");
                
        HttpRequest request = new HttpRequest(HttpMethod.POST, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.FENCEID, fenceId);
        client.execute(request, new FenceKeyResponseHandler(), errorResponseHandler);
      }
    } // end Fence.Deactivate
      
      
    public Get get(String fenceId) {
      return new Get(fenceId);
    }
      
    public class Get extends LocomatixRequest {
          
      private static final String REST_PATH = "/fence/Get.json";
          
      private final String fenceId;
           
      public Get(String fenceId) {
        this.fenceId = fenceId;
      }
          
      public com.locomatix.model.Fence execute() {
        assertParameterNotNull(fenceId, "The fence id parameter must be specified when fetching a fence");
              
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.FENCEID, fenceId);
               
        LocomatixResponse<com.locomatix.model.Fence> response = 
                  client.execute(request, new GetFenceResponseHandler(), errorResponseHandler);
               
        setLastExecutionTime(this, response);
        return response.getResult();
      }
    } // end Fence.Get
      
    public Locomatix.Fence.List list() {
      return new List();
    }
      
    public class List extends LocomatixRequest implements PageableRequest {
          
      private static final String REST_PATH = "/fence/List.json";
           
      private String startKey;
          
      public List withNextToken(String startKey) {
        this.startKey = startKey;
         return this;
      }
          
      public ResultSet execute() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
              
        if (null != startKey) {
          request.addParameter(Parameters.STARTKEY, startKey);
        }
        request.addParameter(Parameters.FETCHSIZE, String.valueOf(DEFAULT_FETCH_SIZE));
              
        LocomatixResponse<ResultSet.Builder> response = 
           client.execute(request, new ListFencesResponseHandler(), errorResponseHandler);
              
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
    }
  }
  
  
  //--------------------------------- Analytics API --------------------------------------------
      
  public Analytics analytics(String feedId) {
    return new Analytics(feedId);
  }

  public final class Analytics {
    private final String feedId;

    public Analytics(String feedId) {
      this.feedId = feedId;
    }

    public History history(String objectId, long startTime, long endTime) {
      return new History(objectId, startTime, endTime);
    }
          
    public class History extends LocomatixRequest implements PageableRequest {
          
      private static final String REST_PATH = "/analytics/ObjectTrail.json";
      
      private final long startTime;
      private final long endTime;

      private final LQLQueryBuilder lql = new LQLQueryBuilder();
       
      private String startKey;
      
      public History(String objectId, long startTime, long endTime) {
        assertValidTimeRange(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        lql.select("location.*").from(feedId).with(objectId);
      }

      public History select(String... clause) {
        if (null != clause) {
          lql.select(clause);
         }
         return this;
      }
              
      public History where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }

      public History withNextToken(String nextToken) {
        this.startKey = nextToken;
        return this;
      }
      
      
      public ResultSet execute() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameter(Parameters.STARTTIME, String.valueOf(startTime));
        request.addParameter(Parameters.ENDTIME, String.valueOf(endTime));
        request.addParameter(Parameters.PREDICATE, lql.toLql());
        if (null != startKey) {
          request.addParameter(Parameters.STARTKEY, startKey);
        }
        
        LocomatixResponse<ResultSet.Builder> response = 
                  client.execute(request, new QueryResponseHandler(), errorResponseHandler);
              
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
    } // end Analytics.History

      
    public Activity activity(com.locomatix.model.Region region, long startTime, long endTime) {
      return new Activity(region, startTime, endTime);
    }
      
      
    public class Activity extends LocomatixRequest implements PageableRequest {
          
      private static final String REST_PATH = "/analytics/SpaceActivity.json";
         
      private final com.locomatix.model.Region region;
      private final long startTime;
      private final long endTime;
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
          
      private String startKey;
          
      public Activity(com.locomatix.model.Region region, long startTime, long endTime) {
        assertValidTimeRange(startTime, endTime);
        this.region = region;
        this.startTime = startTime;
        this.endTime = endTime;
        lql.select("location.*").from(feedId);
      }

      public Activity select(String... clause) {
        if (null != clause) {
          lql.select(clause);
        }
        return this;
      }
              
      public Activity where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }

      public Activity withNextToken(String startKey) {
        this.startKey = startKey;
        return this;
      }
          
      public ResultSet execute() {
        assertParameterNotNull(region, "The object id parameter must be specified when getting spatial activity");
        // is this a circle region?
        if (region instanceof Circle) {
          assertCircleRegionCenterNotNull((Circle) region);
        }
        assert(startTime < endTime);
              
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        request.addParameters(region.getProperties());
              
        request.addParameter(Parameters.STARTTIME, String.valueOf(startTime));
        request.addParameter(Parameters.ENDTIME, String.valueOf(endTime));
        request.addParameter(Parameters.PREDICATE, lql.toLql());
              
        if (null != startKey) {
          request.addParameter(Parameters.STARTKEY, startKey);
        }
        request.addParameter(Parameters.FETCHSIZE, String.valueOf(DEFAULT_FETCH_SIZE));
              
        LocomatixResponse<ResultSet.Builder> response = 
             client.execute(request, new QueryResponseHandler(), errorResponseHandler);
              
        response.getResult().pageableRequest(this);
        setLastExecutionTime(this, response);
        return response.getResult().build();
      }
              
    } // end Analytics.Activity
      
      
    public Histogram histogram(Rectangle rectangle, long startTime) {
      return new Histogram(rectangle, startTime);
    }
      
    public class Histogram extends LocomatixRequest {
          
      private static final String REST_PATH = "/analytics/ObjectGrid.json";
      
      private final Point southWest;
      private final Point northEast;
      private int horizontalSlices = 10;
      private int verticalSlices = 10;
      private final long startTime;
      private final LQLQueryBuilder lql = new LQLQueryBuilder();
          
      public Histogram(Rectangle rectangle, long startTime) {
        assertParameterNotNull(rectangle, "RectangleRegion can not be null");
        this.southWest = rectangle.getSouthWest();
        this.northEast = rectangle.getNorthEast();
        this.startTime = startTime;
        lql.select("count(location.*)").from(feedId);
      }
          
      public Histogram select(String ...fields) {
        if (null != fields) {
          lql.select(fields);
        }
        return this;
      }
          
      public Histogram where(String clause) {
        if (null != clause) {
          lql.where(clause);
        }
        return this;
      }
      
      public Histogram withHorizontalSlices(int horizontalSlices) {
        if (horizontalSlices < 1)
          throw new IllegalArgumentException("HorizontalSlices (" + horizontalSlices + ") must be at least 1");
        this.horizontalSlices = horizontalSlices;
        return this;
      }
      
      public Histogram withVerticalSlices(int verticalSlices) {
        if (verticalSlices < 1)
          throw new IllegalArgumentException("VerticalSlices (" + verticalSlices + ") must be at least 1");
        this.verticalSlices = verticalSlices;
        return this;
      }
      
      
      public Grid execute() {
        assertParameterNotNull(southWest, "A south west point is required to fetch an object grid");
        assertParameterNotNull(northEast, "A north east point is required to fetch an object grid");
        
        HttpRequest request = new HttpRequest(HttpMethod.GET, REST_PATH);
        request.setEndpoint(endpoint);
        request.setHeaders(headers);
        
        StringBuilder bbox = new StringBuilder();
        bbox.append(southWest.getLatitude()).append(",");
        bbox.append(southWest.getLongitude()).append("|");
        bbox.append(northEast.getLatitude()).append(",");
        bbox.append(northEast.getLongitude());
        request.addParameter(Parameters.GRID_BBOX, bbox.toString());
        request.addParameter(Parameters.GRID_HORIZONTAL_SLICES, String.valueOf(horizontalSlices));
        request.addParameter(Parameters.GRID_VERTICAL_SLICES, String.valueOf(verticalSlices));
        request.addParameter(Parameters.GRID_STARTTIME, String.valueOf(startTime));
        request.addParameter(Parameters.PREDICATE, lql.toLql());
              
        ObjectGridResponseHandler handler = 
             new ObjectGridResponseHandler(horizontalSlices, verticalSlices);
              
        LocomatixResponse<Grid> response = 
              client.execute(request, handler, errorResponseHandler);
              
        setLastExecutionTime(this, response);
        return response.getResult();
      }
          
    } // end Analytics.Grid 
      
  }
  
  
  // returns a resource path with the feed id encoded.
  private String createURI(String route, String feed) {
    try {
      return String.format(route, URLEncoder.encode(feed, "UTF-8"));
    } catch(UnsupportedEncodingException uee) {
      throw new IllegalArgumentException(uee);
    }
  }
  
  
  private void addNameValuesParameters(HttpRequest request, NameValues nameValues) {
    for (Map.Entry<String, String> entry : nameValues.entries()) {
      request.addParameter(entry.getKey(), entry.getValue());
    }
  }
  
  
  
  // helper method to check for null parameters.
  private void assertParameterNotNull(Object parameterValue, String errorMessage) {
    if(null == parameterValue)
      throw new IllegalArgumentException(errorMessage);
  }
  
  // helper method to check for a valid time ranges
  private void assertValidTimeRange(long startTime, long endTime) {
      if (startTime > endTime) {
          String errorMessage = 
              String.format("Invalid TimeRange StartTime[%s] > EndTime[%s]", startTime, endTime);
          throw new IllegalArgumentException(errorMessage);
      }
  }
  
  // helper method to check that a circle has a center point
  private void assertCircleRegionCenterNotNull(com.locomatix.model.Circle region) {
      assertParameterNotNull(region.getCenter(), "No center specified for the circle region");
  }
  
  
}
