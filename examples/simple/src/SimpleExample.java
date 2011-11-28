
import java.util.Date;

import com.locomatix.ClientConfiguration;
import com.locomatix.Locomatix;
import com.locomatix.auth.BasicLocomatixCredentials;
import com.locomatix.auth.LocomatixCredentials;
import com.locomatix.model.Location;
import com.locomatix.model.NameValues;

public class SimpleExample {
  
  
  public static void main(String[] args) {

    String customerId = "<enter your locomatix custid>";
    String customerKey = "<enter your locomatix custkey>";
    String secretKey = "<enter your locomatix secret key>";

    String host = "<enter the locomatix host>";
    String port = "<enter the locomatix port>";

    // Make an instance of Locomatix credentials
    LocomatixCredentials credentials = 
              new BasicLocomatixCredentials(customerId, customerKey, secretKey);

    // Make an instance clt config with locomatix host and port 
    ClientConfiguration cltconfig =
           new ClientConfiguration(host, Integer.parseInt(port));
    cltconfig.setTrustAllCertificates(true);

    // Now create a locomatix connection instance
    Locomatix lx = new Locomatix(credentials, cltconfig);

    // Set the name of feed and id of the object
    String custfeed = "customers";
    String objectId = "george@georgecompany.com";

    // Create the name values for the object 
    NameValues nvpairs = NameValues.create();
    nvpairs.put("name", "george");
    nvpairs.put("age", "34");
    nvpairs.put("favorite-color", "yellow");

    // Create the feed called customers
    lx.feeds().create(custfeed).execute();

    // Now let's create a new customer named George
    lx.objects(custfeed).create(objectId).withNameValues(nvpairs).execute();

    // George is at the Wisconsin State Capital, let's update his location
    long nowInSeconds = System.currentTimeMillis()/1000;
    lx.location(custfeed).update(objectId, 43.07475, -89.38434, nowInSeconds).execute();
 
    // Where is George?, let's get George's last known location
    Location gloc = lx.location(custfeed).get(objectId).execute();

    System.out.println("George's location is " + gloc.getCoordinates().toString());
    System.out.println("Updated at " + new Date(gloc.getTime()*1000).toString());

    // Now delete the object
    lx.objects(custfeed).delete(objectId).execute(); 

    // Now delete the feed -- the feed has to be empty in order to delete
    lx.feeds().delete(custfeed).execute(); 

    // shutdown the client
    lx.shutdown();
  }
  
}
