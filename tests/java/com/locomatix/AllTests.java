package com.locomatix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	
	AllObjectsTests.class,
	
	AllFeedsTests.class,
	
	AllFencesTests.class,
	
	AllZonesTests.class,
	
	AllSearchesTests.class,
	
	AllAnalyticsTests.class
	
})

public class AllTests {

}
