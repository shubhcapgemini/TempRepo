package com.cg.mobilebilling.tests;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.cg.mobilebilling.services.BillingServices;
import com.cg.mobilebilling.services.BillingServicesImpl;

public class MobileBillingTests {
	

	private static BillingServices billingServices;
	
	@BeforeClass
	public static void setUpBillingServices(){
		billingServices = new BillingServicesImpl();
	}
	
	@Before 
	public void setUpTestData(){
	
	}
	
	
	
	@After 
	public void tearDownTestData(){
		
	}
	
	@AfterClass 
	public static void  tearDownBillinglServicesq(){
		billingServices = null;
	}

}
