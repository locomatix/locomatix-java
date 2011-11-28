package com.locomatix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	
	CreateObjectTest.class, 
	DeleteObjectTest.class,
	GetAttributesTest.class, 
	UpdateAttributesTest.class, 
	ListObjectsTest.class, 
	
	GetLocationTest.class,
	UpdateLocationTest.class
	
})


public class AllObjectsTests {

}
