package com.cg.mobilebilling.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
@Service
@Transactional
public class BillingServicesImpl implements BillingServices {

	@Autowired
	BillingDAOServices dao;
	

	public BillingServicesImpl(BillingDAOServices dao) {
		super();
		this.dao = dao;
	}

	@Override
	public PostpaidAccount openPostpaidMobileAccount(int customerID,PostpaidAccount account, int planID)
			throws PlanDetailsNotFoundException, CustomerDetailsNotFoundException, BillingServicesDownException {
			StandardPlan splan= dao.getPlan(planID);
			Plan plan=new Plan(splan.getPlanID(),splan.getMonthlyRental(),splan.getFreeLocalCalls(), splan.getFreeStdCalls(), splan.getFreeLocalSMS(), splan.getFreeStdSMS(), splan.getFreeInternetDataUsageUnits(), splan.getLocalCallRate(), splan.getStdCallRate(), splan.getLocalSMSRate(), splan.getStdSMSRate(), splan.getInternetDataUsageRate(), splan.getPlanCircle(), splan.getPlanName());
			account.setPlan(plan);
			return dao.insertPostPaidAccount(customerID, account);
	}

	@Override
	public Bill generateMonthlyMobileBill(int customerID, long mobileNo, String billMonth, int noOfLocalSMS,
			int noOfStdSMS, int noOfLocalCalls, int noOfStdCalls, int internetDataUsageUnits)
					throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException,
					BillingServicesDownException, PlanDetailsNotFoundException {
		PostpaidAccount p= dao.getPlanDetails(customerID, mobileNo);
		int BillednoOfLocalSMS = (noOfLocalSMS-p.getPlan().getFreeLocalSMS());
		int BillednoOfStdSMS = (noOfStdSMS-p.getPlan().getFreeStdSMS());
		int BillednoOfLocalCalls = (noOfLocalCalls-p.getPlan().getFreeLocalCalls());
		int BillednoOfStdCalls = (noOfStdCalls-p.getPlan().getFreeStdCalls());
		int BilledinternetDataUsageUnits = (internetDataUsageUnits-p.getPlan().getFreeInternetDataUsageUnits());

		float localCallAmount= BillednoOfLocalCalls*p.getPlan().getLocalCallRate();
		float StdCallAmount = BillednoOfStdCalls*p.getPlan().getStdCallRate();
		float LocalSMSAmount= BillednoOfLocalSMS*p.getPlan().getLocalSMSRate();
		float StdSMSAmount = BillednoOfStdSMS*p.getPlan().getStdSMSRate();
		float internetAmount = BilledinternetDataUsageUnits*p.getPlan().getInternetDataUsageRate();

		float vat=  0.08f;
		float serviceTax= 0.15f;
		float Amount= localCallAmount+StdCallAmount+LocalSMSAmount+StdSMSAmount+internetAmount; 
		float VatAmount=  Amount*vat;
		float serviceTaxAmount = serviceTax*Amount;

		float TotalBillAmount= Amount+VatAmount+serviceTaxAmount;

		Bill bill=new Bill(BillednoOfLocalSMS, BillednoOfStdSMS, BillednoOfLocalCalls, BillednoOfStdCalls, internetDataUsageUnits, billMonth, TotalBillAmount, LocalSMSAmount, StdSMSAmount, localCallAmount, StdCallAmount, internetAmount, serviceTaxAmount, VatAmount);
		return dao.insertMonthlybill(customerID, mobileNo, bill);

	}

	@Override
	public Customer getCustomerDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException {

	 Customer cust= dao.getCustomer(customerID);
	 if (cust==null) throw new CustomerDetailsNotFoundException("Customer Details with Customer ID:" +customerID+ " not found");
	return cust;
		
		
	}

	@Override
	public List<Customer> getAllCustomerDetails() throws BillingServicesDownException {
		return dao.getAllCustomers();

	}

	@Override
	public PostpaidAccount getPostPaidAccountDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException {

		
		return dao.getCustomerPostPaidAccount(customerID, mobileNo);
	
	}

	@Override
	public List<PostpaidAccount> getCustomerAllPostpaidAccountsDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException {
		return dao.getCustomerPostPaidAccounts(customerID);
	}

	@Override
	public Bill getMobileBillDetails(long mobileNo, String billMonth)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException,
			BillDetailsNotFoundException, BillingServicesDownException {
	
		 Bill bill =dao.getMonthlyBill(mobileNo, billMonth);
		 if(bill==null)throw new InvalidBillMonthException("bill details for month" +billMonth+ "not found");
		return bill;
		
	
	}

	@Override
	public List<Bill> getCustomerPostPaidAccountAllBillDetails(long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException,
			BillDetailsNotFoundException {

	return dao.getCustomerPostPaidAccountAllBills(mobileNo);
		
		
	}

	@Override
	public PostpaidAccount changePlan(int customerID, long mobileNo, int planID) throws CustomerDetailsNotFoundException,
	PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
		StandardPlan splan= dao.getPlan(planID);
		Plan plan=new Plan(splan.getPlanID(),splan.getMonthlyRental(),splan.getFreeLocalCalls(), splan.getFreeStdCalls(), splan.getFreeLocalSMS(), splan.getFreeStdSMS(), splan.getFreeInternetDataUsageUnits(), splan.getLocalCallRate(), splan.getStdCallRate(), splan.getLocalSMSRate(), splan.getStdSMSRate(), splan.getInternetDataUsageRate(), splan.getPlanCircle(), splan.getPlanName());
		Customer cust =dao.getCustomer(customerID);
		PostpaidAccount acc= new PostpaidAccount();

		/*  cust.setCustomerID(customerID);*/
		acc.setPlan(plan);
		acc.setCustomer(cust);
		acc.setMobileNo(mobileNo);
		return dao.updatePostPaidAccount(customerID, acc);
	}

	@Override
	public boolean closeCustomerPostPaidAccount(long mobileNo)
			throws  PostpaidAccountNotFoundException, BillingServicesDownException {
		return dao.deletePostPaidAccount(mobileNo);
	}

	@Override
	public boolean deleteCustomer(int customerID)
			throws BillingServicesDownException, CustomerDetailsNotFoundException {

		return dao.deleteCustomer(customerID);
	}


	@Override
	public Customer acceptCustomerDetails(Customer customer) throws BillingServicesDownException {

		return dao.insertCustomer(customer);
	}

	@Override
	public StandardPlan insertPlan(StandardPlan plan) throws PlanDetailsNotFoundException {

		return dao.insertPlan(plan);
	}

	@Override
	public StandardPlan getPlan(int planID) throws PlanDetailsNotFoundException, BillingServicesDownException {

		StandardPlan splan=dao.getPlan(planID);
		if(splan==null) throw new PlanDetailsNotFoundException("Plan details with Plan Id: "+planID+ " not found");
		return splan;
	}

	@Override
	public PostpaidAccount getCustomerPostPaidAccountPlanDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException,
			PlanDetailsNotFoundException {
		getCustomerDetails(customerID);
		PostpaidAccount pAccount= dao.getPlanDetails(customerID, mobileNo);
		if(pAccount==null) throw new PostpaidAccountNotFoundException("PostpaidAccount Details with mobile Number:" +mobileNo+ "not found");
		return pAccount;
	}



	@Override
	public List<StandardPlan> getAllPlans() throws BillingServicesDownException {

		return dao.getAllPlans();
	}

}