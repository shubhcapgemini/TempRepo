package com.cg.mobilebilling.tests;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.cg.mobilebilling.beans.Address;
import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.Plan;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.beans.StandardPlan;
import com.cg.mobilebilling.daoservices.BillingDAOServices;
import com.cg.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.InvalidBillMonthException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.PostpaidAccountNotFoundException;
import com.cg.mobilebilling.services.BillingServices;
import com.cg.mobilebilling.services.BillingServicesImpl;

public class MobileBillingTests {


	private static BillingServices billingServices;
	private static BillingDAOServices daoServices;

	@BeforeClass
	public static void setUpBillingServices(){
		daoServices= Mockito.mock(BillingDAOServices.class);
		billingServices = new BillingServicesImpl(daoServices);
	}

	
	@Before 
	public void setUpTestData(){
		
		Map<Integer, Bill> bills=  new HashMap<>();
		bills.put(1, new Bill(1, 45, 45, 45, 45, 45, 45, "sept", 45, 45, 45, 45, 45, 45, 45));
		Map<Long, PostpaidAccount> postpaidAccounts= new HashMap<>();
		postpaidAccounts.put((long) 1, new PostpaidAccount(98977, new Plan(1, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 54, "pune", "silver"),bills));
		
		Customer customer1= new Customer(1, "aa", "bb", "aa@gmail.com", "02-09-1993", "password", new Address(2202, "lko", "up"), postpaidAccounts);
		
		List<Customer> custlist= new ArrayList<>();
		custlist.add(customer1);
		List<Bill> billList= new ArrayList<>();
		Bill billtemp= new Bill(1, 45, 45, 45, 45, 45, 45, "sept", 45, 45, 45, 45, 45, 45, 45);
		billList.add(billtemp);
		StandardPlan splan= new StandardPlan(1, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 54, "pune", "silver");
		
		Mockito.when(daoServices.getCustomer(1)).thenReturn(customer1);
				
	}

	@Test(expected=CustomerDetailsNotFoundException.class)
	public void testgetCustomerDetailsForInvalidCustomerId() throws CustomerDetailsNotFoundException, BillingServicesDownException{
		billingServices.getCustomerDetails(4545);
	}

	@Test
	public void testgetCustomerDetailsForValidCustomerId() throws CustomerDetailsNotFoundException, BillingServicesDownException {
		Customer expectedData = new Customer(1, "aa", "bb", "aa@gmail.com", "02-09-1993", "password", new Address(2202, "lko", "up"));
		Customer actualData = billingServices.getCustomerDetails(1);
		assertEquals(expectedData,actualData);
	}

	@Test(expected=CustomerDetailsNotFoundException.class)
	public void testChangePlanDataForInvalidCustomerId() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
		billingServices.changePlan(4545, 993619482, 3);
	}
	@Test(expected=PostpaidAccountNotFoundException.class)
	public void testChangePlanDataForInvalidMobileNo() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
		billingServices.changePlan(1, 993619882, 3);
	}
	@Test(expected=PlanDetailsNotFoundException.class)
	public void testChangePlanDataForInvalidPlanId() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
		billingServices.changePlan(1, 993619482, 31);
	}

	@Test
	public void testChangePlanDataForValidData() throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException {
		PostpaidAccount expectedData=new PostpaidAccount(98977, new Plan(1, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 54, "pune", "silver"), new Bill(1, 45, 45, 45, 45, 45, 45, "sept", 45, 45, 45, 45, 45, 45, 45));
		PostpaidAccount actualData=billingServices.changePlan(1, 993611948, 1);
				assertEquals(expectedData,actualData);
	}

	@Test(expected=PostpaidAccountNotFoundException.class)
	public void testcloseCustomerPostPaidAccountForInvalidMobileNo() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
		billingServices.closeCustomerPostPaidAccount(99164545);
	}
	@Test
	public void testcloseCustomerPostPaidAccountForValidMobileNo() throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException {
		PostpaidAccount expectedData=new PostpaidAccount(993611948);
		boolean actualData=billingServices.closeCustomerPostPaidAccount(993611948);
		assertTrue(actualData);
	}

	@Test(expected=CustomerDetailsNotFoundException.class)
	public void testdeleteCustomerForInvalidCustomerId() throws CustomerDetailsNotFoundException, BillingServicesDownException {
		billingServices.deleteCustomer(45456);
	}
	@Test
	public void testdeleteCustomerForValidCustomerId() throws CustomerDetailsNotFoundException, BillingServicesDownException {
		Customer expectedData = new Customer(1, "aa", "bb", "as@gmail.com", "02-03-1993", "aaa", new Address(221011, "kanpur", "Up"));
		boolean actualData = billingServices.deleteCustomer(1);
		assertTrue(actualData);
	}

	@Test(expected=CustomerDetailsNotFoundException.class)
	public void testgetCustomerAllPostpaidAccountsDetailsForInvalidCustomerId() throws CustomerDetailsNotFoundException, BillingServicesDownException {
		billingServices.getCustomerAllPostpaidAccountsDetails(4545);
	}

	@Test
	public void testgetCustomerAllPostpaidAccountDetailsForValidCustomerId() throws CustomerDetailsNotFoundException, BillingServicesDownException {
		PostpaidAccount expectedData = new PostpaidAccount(98977, new Plan(1, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 54, "pune", "silver"),new Customer(1, "aa", "bb", "aa@gmail.com", "02-09-1993", "password", new Address(2202, "lko", "up")));
		List<PostpaidAccount> actualData = billingServices.getCustomerAllPostpaidAccountsDetails(1);
		
		assertEquals(expectedData, actualData);
	}
	
	@Test(expected=BillDetailsNotFoundException.class)
	public void testgetCustomerPostPaidAccountAllBillDetailsForInvalidMobileNo() throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, BillDetailsNotFoundException {
		billingServices.getCustomerPostPaidAccountAllBillDetails(4545666);
	}
	
	@Test
	public void testgetCustomerPostPaidAccountAllBillDetailsForValidMobileNo() throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, BillDetailsNotFoundException {
		Bill expectedData = new Bill(3, 45, 45, 45, 45, 45, "sep", 45, 45, 45, 45, 45, 45, 45, 45, new PostpaidAccount(993611948));
		List<Bill> actualData = billingServices.getCustomerPostPaidAccountAllBillDetails(993611948);
		assertEquals(expectedData,actualData);
	}
	
	@Test(expected=PlanDetailsNotFoundException.class)
	public void testgetCustomerPostPaidAccountPlanDetailsForInvalidMobileNo() throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, BillDetailsNotFoundException {
		billingServices.getCustomerPostPaidAccountAllBillDetails(4545666);
	}
	
	@Test
	public void testgetCustomerPostPaidAccountPlanDetailsForValidMobileNo() throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, BillDetailsNotFoundException, PlanDetailsNotFoundException {
		PostpaidAccount expectedData= new PostpaidAccount(993611948);
		PostpaidAccount actualData = billingServices.getCustomerPostPaidAccountPlanDetails(1, 993611948);
		assertEquals(expectedData,actualData);
	}
	
	@Test(expected=PlanDetailsNotFoundException.class)
	public void testgetPlanDetailsForInvalidPlanId() throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, BillDetailsNotFoundException, PlanDetailsNotFoundException {
		billingServices.getPlan(5454);
	}
	@Test
	public void testgetPlanDetailsForValidPlanId() throws PlanDetailsNotFoundException, BillingServicesDownException{
		StandardPlan expectedData= new StandardPlan(3, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, "pune", "silver");
		StandardPlan actualData = billingServices.getPlan(3);
		assertEquals(expectedData,actualData);
	}
	
	@Test(expected=BillDetailsNotFoundException.class)
	public void testgenerateMonthlyMobileBillForInvalidCustomerId() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillingServicesDownException, PlanDetailsNotFoundException {
		billingServices.generateMonthlyMobileBill(4545, 993611948, "sept", 45, 45, 45, 45, 45);
	}
	
	@Test(expected=PostpaidAccountNotFoundException.class)
	public void testgenerateMonthlyMobileBillForInvalidMobileNo() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillingServicesDownException, PlanDetailsNotFoundException {
		billingServices.generateMonthlyMobileBill(1, 2525, "sept", 45, 45, 45, 45, 45);
	}
	@Test(expected=InvalidBillMonthException.class)
	public void testgenerateMonthlyMobileBillForInvalidBillMonth() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillingServicesDownException, PlanDetailsNotFoundException {
		billingServices.generateMonthlyMobileBill(1, 2525, "aaa", 45, 45, 45, 45, 45);
	}
	@Test
	public void testgenerateMonthlyMobileBillForValidData() throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillingServicesDownException, PlanDetailsNotFoundException{
		Bill expectedData=  new Bill(1, 45, 45, 45, 45, 45, 45, "sept", 45, 45, 45, 45, 45, 45, 45);
		Bill actualData = billingServices.generateMonthlyMobileBill(1, 993611948, "sep", 45, 45, 45, 45, 45);
		assertEquals(expectedData,actualData);
	}
	
	@After 
	public void tearDownTestData(){

	}

	@AfterClass 
	public static void  tearDownBillinglServicesq(){
		billingServices = null;
	}

}
