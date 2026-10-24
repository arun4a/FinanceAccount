/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mobile;

import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author sys1089
 */
public class MobileController extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    HttpServletRequest request;
    HttpServletResponse response; 
    private String customerMobId, customer2mob;
    private String surety2mob,customerNameSearch,accountNumber; 

    public String getCustomerNameSearch() {
        return customerNameSearch;
    }

    public void setCustomerNameSearch(String customerNameSearch) {
        this.customerNameSearch = customerNameSearch;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerMobId() {
        return customerMobId;
    }

    public void setCustomerMobId(String customerMobId) {
        this.customerMobId = customerMobId;
    }

    public String getCustomer2mob() {
        return customer2mob;
    }

    public void setCustomer2mob(String customer2mob) {
        this.customer2mob = customer2mob;
    }

    public String getSurety2mob() {
        return surety2mob;
    }

    public void setSurety2mob(String surety2mob) {
        this.surety2mob = surety2mob;
    }
    
public String mobileNumberAdd(){
    MobileAction mobileAction=null;
    try {
        mobileAction=new MobileAction();
        int i=mobileAction.UpdateCustMobile(getCustomer2mob(),getSurety2mob(),getCustomerMobId());
    } catch (Exception e) {
    }
    return SUCCESS;
}
    
    
    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        request = hsr;
    }

    @Override
    public void setServletResponse(HttpServletResponse hsr) {
        response = hsr;
    }

}
