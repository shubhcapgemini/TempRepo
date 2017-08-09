package com.cg.mobilebilling.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.Plan;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.beans.StandardPlan;
import com.cg.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.InvalidBillMonthException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.PostpaidAccountNotFoundException;
import com.cg.mobilebilling.services.BillingServices;

@RestController
public class MobileBillingController {

	@Autowired
	private BillingServices services;
	
	
	public MobileBillingController(){
		System.out.println("Mobile Billing Controller");
	}
	
	@RequestMapping(value="/acceptCustomerDetail",method=RequestMethod.POST,consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String>acceptCustomerDetail(@ModelAttribute Customer customer) throws BillingServicesDownException{
		services.acceptCustomerDetails(customer);
		return new ResponseEntity<>("Customer details succesfully added",HttpStatus.OK);
	}
	
	@RequestMapping(value={"/CustomerDetailsRequestParam"},headers="Accept=application/json")
	public ResponseEntity<Customer>getCustomerDetails(@RequestParam("customerID")int customerID) throws CustomerDetailsNotFoundException, BillingServicesDownException{
		Customer customer=services.getCustomerDetails(customerID);
		System.out.println("details "+customer);
		return new ResponseEntity<>(customer,HttpStatus.OK);
	}
	
	@RequestMapping(value={"/allCustomerDetailsJSON"},headers="Accept=application/json")
	public ResponseEntity<ArrayList<Customer>> getAllCustomerDetailsJSON() throws CustomerDetailsNotFoundException, BillingServicesDownException{
	ArrayList<Customer> customerList=(ArrayList<Customer>) services.getAllCustomerDetails();
	return new ResponseEntity<>(customerList,HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteCustomerDetail/{customerID}",method=RequestMethod.DELETE)
	public ResponseEntity<String>deleteCustomerDetail(@PathVariable("customerID")int customerID)throws CustomerDetailsNotFoundException, BillingServicesDownException{
		boolean customer = services.deleteCustomer(customerID);
			if(customer==false)throw new CustomerDetailsNotFoundException("Customer detail not found with product code"+customerID);
		return new ResponseEntity<>("Customer details succesfully deleted",HttpStatus.OK);
	}
	
	@RequestMapping(value="/acceptStandardPlanDetail",method=RequestMethod.POST,consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String>acceptStandardPlanDetail(@ModelAttribute StandardPlan plan) throws BillingServicesDownException, PlanDetailsNotFoundException{
		services.insertPlan(plan);
		return new ResponseEntity<>("plan details succesfully added",HttpStatus.OK);
	}
	
	@RequestMapping(value="/acceptPostpaidAccountDetail",method=RequestMethod.POST,consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String>acceptPostpaidAccountDetail(@ModelAttribute PostpaidAccount account, @RequestParam("customerID")int customerID, @RequestParam("planID")int planID ) throws BillingServicesDownException, PlanDetailsNotFoundException, CustomerDetailsNotFoundException{
		services.openPostpaidMobileAccount(customerID, account, planID);
		return new ResponseEntity<>("postpaidAccount details succesfully added",HttpStatus.OK);
	}
	
	@RequestMapping(value={"/PlanDetailsRequestParam"},headers="Accept=application/json")
	public ResponseEntity<StandardPlan>PlanDetailsRequestParam(@RequestParam("planID")int planID) throws  BillingServicesDownException, PlanDetailsNotFoundException{
		StandardPlan splan= services.getPlan(planID);
		System.out.println("details "+splan);
		return new ResponseEntity<>(splan,HttpStatus.OK);
	}
	
	@RequestMapping(value={"/PostpaidAccountPLanDetails"},headers="Accept=application/json")
	public ResponseEntity<PostpaidAccount>PostpaidAccountPLanDetails(@RequestParam("customerID")int customerID,@RequestParam("mobileNo")long mobileNo) throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException{
		PostpaidAccount pAccount=services.getCustomerPostPaidAccountPlanDetails(customerID, mobileNo);
		System.out.println("details "+pAccount);
		return new ResponseEntity<>(pAccount,HttpStatus.OK);
	}
	
	@RequestMapping(value={"/GenerateMonthlyBill"},headers="Accept=application/json")
	public ResponseEntity<Bill>GenerateMonthlyBill(@RequestParam("customerID")int customerID,@RequestParam("mobileNo")long mobileNo,@RequestParam("billMonth")String billMonth,@RequestParam("noOfLocalSMS")int noOfLocalSMS,@RequestParam("noOfStdSMS")int noOfStdSMS,@RequestParam("noOfLocalCalls")int noOfLocalCalls,@RequestParam("noOfStdCalls")int noOfStdCalls,@RequestParam("internetDataUsageUnits")int internetDataUsageUnits) throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, InvalidBillMonthException, PlanDetailsNotFoundException{
		Bill bill=services.generateMonthlyMobileBill(customerID, mobileNo, billMonth, noOfLocalSMS, noOfStdSMS, noOfLocalCalls, noOfStdCalls, internetDataUsageUnits);
		return new ResponseEntity<>(bill,HttpStatus.OK);
	}
	
	@RequestMapping(value="/deletePostPaidAccountDetail/{customerID}/{mobileNo}",method=RequestMethod.DELETE)
	public ResponseEntity<String>deletePostPaidAccountDetail(@PathVariable("customerID")int customerID,@PathVariable("mobileNo")long mobileNo)throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException{
		boolean postPaidAccount = services.deletePostPaidAccount(customerID, mobileNo);
			if(postPaidAccount==false)throw new CustomerDetailsNotFoundException("postPaidAccount detail not found with product code"+customerID);
		return new ResponseEntity<>("postPaidAccount details succesfully deleted",HttpStatus.OK);
	}
	
	
	@RequestMapping(value={"/GetMonthlyBillDetails"},headers="Accept=application/json")
	public ResponseEntity<Bill>GetMonthlyBillDetails(@RequestParam("mobileNo")long mobileNo,@RequestParam("billMonth")String billMonth) throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillDetailsNotFoundException{
	Bill bill = services.getMobileBillDetails(mobileNo, billMonth);
		System.out.println("details "+bill);
		return new ResponseEntity<>(bill,HttpStatus.OK);
	}
}
