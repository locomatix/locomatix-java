package com.locomatix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	
	CreateFeedTest.class, 
	DeleteFeedTest.class,
	ListFeedsTest.class
	
})

public class AllFeedsTests {

}
