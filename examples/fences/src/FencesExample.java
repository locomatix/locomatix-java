
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.locomatix.ClientConfiguration;
import com.locomatix.Locomatix;
import com.locomatix.LocomatixException;
import com.locomatix.auth.BasicLocomatixCredentials;
import com.locomatix.auth.LocomatixCredentials;
import com.locomatix.model.Callback;
import com.locomatix.model.Circle;
import com.locomatix.model.Fence;
import com.locomatix.model.LXObject;
import com.locomatix.model.ResultSet;
import com.locomatix.model.URLCallback;

public class FencesExample {

  private static final int LOCATION_TTL = 3600;
  private static final int NUM_CUSTOMERS = 10; 
  private static final String CUSTOMERS = "customers";
  private static final double FENCE_RADIUS = 2000;

  private final String CUSTOMERID = "<enter your locomatix custid>"; 
  private final String CUSTOMERKEY = "<enter your locomatix custkey>";
  private final String SECRETKEY = "<enter your locomatix secretkey>";

  private final String HOST = "<enter host>";
  private final int PORT = 443;
  private final Random rand = new Random();
  
  // For storing details about each store 
  static class StoreInfo {
    public String storeId;
    public String storeAddress;
    public double storeLatitude;
    public double storeLongitude;

    public StoreInfo(String id, String address, double lat, double lon) {
      this.storeId = id;
      this.storeAddress = address;
      this.storeLatitude = lat;
      this.storeLongitude = lon; 
    }

    static StoreInfo create(String id, String address, double lat, double lon) {
      return new StoreInfo(id, address, lat, lon);
    }
  }
  
  private List<StoreInfo> storeinfos = Arrays.asList(
    StoreInfo.create("walmart-showers-mountain-view-ca", "600 Showers Drive, Mountain View, CA", 37.404, -122.109),
    StoreInfo.create("walmart-ranch-drive-milpitas-ca", "301 Ranch Drive, Milpitas, CA", 37.431, -121.919),
    StoreInfo.create("walmart-story-san-jose-ca", "777 Story Road, San Jose, CA", 37.332, -121.859), 
    StoreInfo.create("walmart-osgood-fremont-ca", "44009 Osgood road, Fremont, CA", 37.511, -121.941), 
    StoreInfo.create("walmart-albrae-fremont-ca", "40580 Albrae Street, Fremont, CA", 37.522, -121.985), 
    StoreInfo.create("walmart-dyer-union-city-ca","30600 Dyer Street, Union City, CA", 37.604, -122.067), 
    StoreInfo.create("walmart-hesperion-san-leandro-ca", "15555 Hesperion Boulevard, San Leandro, CA", 37.690, -122.130), 
    StoreInfo.create("walmart-davis-san-leandro-ca","1919 Davis Street, San Leandro, CA", 37.717, -122.177), 
    StoreInfo.create("walmart-edgewater-oakland-ca","38400 Edgewater Drive, Oakland, CA", 37.739, -122.197), 
    StoreInfo.create("walmart-monterey-san-jose-ca","5502 Monterey Highway, San Jose, CA",37.258, -121.799) 
  );

  
  // Private variable to store locomatix connection object
  private Locomatix lx;

  // Create circular geo-fences around the stores
  public void createCircularFences(String fromFeed, String url) {
  
    // Create a URL callback
    Callback callback = URLCallback.create(url);

    // Traverse each store
    for (StoreInfo si : storeinfos) {

      // Make a circular region for the geo-fence
      Circle cr = Circle.create(si.storeLatitude, si.storeLongitude, FENCE_RADIUS);

      // Create a circular geo fence around the store
      lx.fences().create(si.storeId, cr, callback, fromFeed)
                 .withNameValue("address", si.storeAddress).execute(); 
    }
  }

  // Delete the geo-fences around the stores
  public void deleteFences() {

    // List the geo fences 
    ResultSet rs = lx.fences().list().execute();

    // Traverse each fence
    while (rs.hasNext())
    {
      // Get the details of each fence
      Fence f = rs.next().get(Fence.class);

      // Delete the fence
      lx.fences().delete(f.getFenceId()).execute();
    } 
  }

  // Delete the customer objects
  public void deleteObjects() {

    // List the customer objects 
    ResultSet rs = lx.objects(CUSTOMERS).list().execute();

    // Traverse each customer object
    while (rs.hasNext())
    {
      // Get the details of each customer object
      LXObject o = rs.next().get(LXObject.class); 

      // Delete the customer object
      lx.objects(CUSTOMERS).delete(o.getObjectId()).execute();
    }
  }

  // Create the customer objects
  public void createObjects() {
    for (int i = 0; i < NUM_CUSTOMERS ; i++) {

      // Form the customer object id
      String oid = "user-id-" + String.valueOf(i); 

      // Now create the customer object in the feed
      lx.objects(CUSTOMERS).create(oid).execute();
    }
  }

  // Move the customer objects
  public void moveObjects() {
    for (int i = 0 ; i < NUM_CUSTOMERS ; i++) {

      // Form the customer object id
      String oid = "user-id-" + String.valueOf(i); 

      // Get a random store
      StoreInfo store = storeinfos.get(rand.nextInt(storeinfos.size()));
       
      // Update the location of the customer to be inside the store geo-fence
      long currtime = System.currentTimeMillis()/1000;
      lx.location(CUSTOMERS).update(oid, store.storeLatitude, store.storeLongitude, currtime).execute();
    }
  }
  
  // Create a feed for customers
  public void createFeeds() {
    lx.feeds().create(CUSTOMERS).withLocationExpiry(LOCATION_TTL).execute();
  }

  public void deleteFeeds() {
    lx.feeds().delete(CUSTOMERS).execute();
  }
  
  
  public FencesExample() {

    // Create the credentials for locomatix connection
    LocomatixCredentials credentials = 
           new BasicLocomatixCredentials(CUSTOMERID, CUSTOMERKEY, SECRETKEY); 

    // Create the host and port to connect to locomatix
    ClientConfiguration cltconfig =
           new ClientConfiguration(HOST, PORT);
    cltconfig.setTrustAllCertificates(true);

    // Now create an locomatix connection instance
    lx = new Locomatix(credentials, cltconfig);
  }

  public static void main(String[] args) {

    if (args.length != 1) {
      System.out.println("Usage: FencesExample <callback-url>");
      System.exit(1);
    }

    String callbackurl = args[0]; 
    FencesExample example = new FencesExample();

    try {
      // Start with clean slate - delete fences and objects, if they exist
      example.deleteFences();
      example.deleteObjects();
      example.deleteFeeds();
    } catch(LocomatixException le) {
      // ignore
    }

    example.createFeeds();
    
    // Create circular geo fences around the store
    example.createCircularFences(CUSTOMERS, callbackurl);

    // Create the customers and move them to close to store to trigger geo-fences
    example.createObjects();
    example.moveObjects();
    
    System.out.println("done.");
  }
}
