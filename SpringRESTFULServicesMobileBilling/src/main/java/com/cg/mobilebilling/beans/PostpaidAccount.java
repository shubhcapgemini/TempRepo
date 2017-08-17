package com.cg.mobilebilling.beans;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PostpaidAccount {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long mobileNo;

	@Embedded
	private Plan plan;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="customerID")
	private Customer customer;

	@OneToMany(mappedBy = "postpaidaccount" , fetch=FetchType.EAGER, cascade=CascadeType.ALL , orphanRemoval=true)
	private Map<Integer, Bill> bills = new HashMap<>();
	
	public PostpaidAccount() {
		super();
	}
	

	public PostpaidAccount(long mobileNo, Plan plan, Customer customer, Map<Integer, Bill> bills) {
		super();
		this.mobileNo = mobileNo;
		this.plan = plan;
		this.customer = customer;
		this.bills = bills;
	}


	public PostpaidAccount(long mobileNo) {
		super();
		this.mobileNo = mobileNo;
	}


	public PostpaidAccount(long mobileNo, Plan plan, Map<Integer, Bill> bills) {
		super();
		this.mobileNo = mobileNo;
		this.plan = plan;
		this.bills = bills;
	}
	
	public PostpaidAccount(int i, Plan plan2, Bill bill) {
		// TODO Auto-generated constructor stub
	}


	public PostpaidAccount(int i, Plan plan2, Customer customer2) {
		// TODO Auto-generated constructor stub
	}


	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Map<Integer, Bill> getBills() {
		return bills;
	}

	public void setBills(Bill bills) {
		if(this.bills !=null){
			this.bills .put((this.bills .size()+1),bills);	
		}else
		{
			System.out.println("null");
		}
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "PostpaidAccount [mobileNo=" + mobileNo + ", plan=" + plan + "]";
	}
}