package com.locomatix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	
	SpaceActivityTest.class,
	LocationHistoryTest.class,
	GetObjectGridTest.class
	
})

public class AllAnalyticsTests {

}
