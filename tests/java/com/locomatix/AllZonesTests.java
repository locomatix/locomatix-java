package com.locomatix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	
	CreateZoneTest.class, 
	DeleteZoneTest.class, 
	GetZoneTest.class,
	DeactivateActivateZoneTest.class,
	ListZonesTest.class
	
})

public class AllZonesTests {

}
