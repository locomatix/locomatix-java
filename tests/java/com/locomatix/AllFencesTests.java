package com.locomatix;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	
	CreateFenceTest.class, 
	DeleteFenceTest.class,
	GetFenceTest.class,
	ListFencesTest.class,
	DeactivateActivateFenceTest.class
	
})

public class AllFencesTests {

}
