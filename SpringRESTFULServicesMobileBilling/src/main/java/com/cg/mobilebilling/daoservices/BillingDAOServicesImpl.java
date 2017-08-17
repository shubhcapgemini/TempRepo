package com.cg.mobilebilling.daoservices;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.beans.StandardPlan;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;


@Repository
public class BillingDAOServicesImpl implements BillingDAOServices {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Customer insertCustomer(Customer customer) throws BillingServicesDownException {
		em.persist(customer);
		em.flush();
		return customer;
	}

	@Override
	public PostpaidAccount insertPostPaidAccount(int customerID, PostpaidAccount account) {
		Customer customer=em.find(Customer.class,customerID);
		account.setCustomer(customer);
		em.persist(account);
		customer.setPostpaidAccounts(account);
		return account;
	}



	@Override
	public Bill insertMonthlybill(int customerID, long mobileNo, Bill bill) {
		PostpaidAccount paccount=em.find(PostpaidAccount.class, mobileNo);
		bill.setPostpaidaccount(paccount);
		em.persist(bill);
		paccount.setBills(bill);
		return bill;
	}

	@Override
	public StandardPlan insertPlan(StandardPlan plan) throws PlanDetailsNotFoundException {
		em.persist(plan);
		em.flush();
		return plan;
	}



	@Override
	public Bill getMonthlyBill(long mobileNo, String billMonth) {
		String query = "Select b from Bill b where b.postpaidaccount.mobileNo=:mobileNo and b.billMonth=:billMonth" ;
		TypedQuery<Bill> qry = em.createQuery(query, Bill.class);
		qry.setParameter("mobileNo", mobileNo);
		qry.setParameter("billMonth", billMonth);
		Bill bill = qry.getSingleResult();
		return bill;
	}

	@Override
	public List<Bill> getCustomerPostPaidAccountAllBills(long mobileNo) {
		TypedQuery<Bill> query = em.createQuery("select b from Bill b where b.postpaidaccount.mobileNo=:mobileNo",Bill.class);

		query.setParameter("mobileNo", mobileNo);
		return query.getResultList();
	}

	@Override
	public List<PostpaidAccount> getCustomerPostPaidAccounts(int customerID) {
		TypedQuery<PostpaidAccount> query = em.createQuery("select p from PostpaidAccount p where p.customer.customerID=:customerID",PostpaidAccount.class);
		query.setParameter("customerID", customerID);
		return query.getResultList(); 
	}

	@Override
	public Customer getCustomer(int customerID) {
		Customer customer = em.find(Customer.class,customerID);
		return customer;
	}

	@Override
	public List<Customer> getAllCustomers() {
		TypedQuery<Customer> query = em.createQuery("select c from Customer c",Customer.class);

		return query.getResultList(); 
	}

	@Override
	public List<StandardPlan> getAllPlans() {
		TypedQuery<StandardPlan> query = em.createQuery("select s from StandardPlan s",StandardPlan.class);
		return query.getResultList();
	}


	@Override
	public PostpaidAccount getCustomerPostPaidAccount(int customerID, long mobileNo) {
		String query = "Select p from PostpaidAccount p where p.customer.customerID=:customerID and p.mobileNo=:mobileNo" ;
		TypedQuery<PostpaidAccount> qry = em.createQuery(query, PostpaidAccount.class);
		qry.setParameter("customerID", customerID);
		qry.setParameter("mobileNo", mobileNo);
		PostpaidAccount postPaidAccount = qry.getSingleResult();
		return postPaidAccount;
	}

	@Override
	public PostpaidAccount getPlanDetails(int customerID, long mobileNo) {
		PostpaidAccount pAccount = em.find(PostpaidAccount.class, mobileNo);
		return pAccount;
	}

	@Override
	public boolean deleteCustomer(int customerID) {
		Customer cust=  em.find(Customer.class, customerID);
		if(cust!=null) {
		em.remove(getCustomer(customerID));
		return true;
		}else {
			return false;
		}
	}

	@Override
	public StandardPlan getPlan(int planID) {
		StandardPlan plan = em.find(StandardPlan.class,planID);
		return plan;
	}

	@Override
	public PostpaidAccount updatePostPaidAccount(int customerID, PostpaidAccount account) {
		PostpaidAccount acc = em.merge(account);
		return acc;
	}

	@Override
	public boolean deletePostPaidAccount(long mobileNo) {
	
		PostpaidAccount acc= em.find(PostpaidAccount.class, mobileNo);
		if(acc!=null) {
		 em.createQuery("delete from PostpaidAccount p where p.mobileNo=:mobileNo", PostpaidAccount.class).setParameter("mobileNo", mobileNo).executeUpdate();
			 em.flush();
		/*	 em.refresh(getCustomerPostPaidAccount(customerID, mobileNo));*/
	/*	em.remove(acc);*/
			return true;
		}else {
			return false;
		}
	}	
}