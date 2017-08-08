package com.cg.mobilebilling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
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
	
}
