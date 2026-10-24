/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.deposit;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author Arun
 */
public class DepositsController extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private String depositNameSave, depositIdSave, res_msg;
    private String amountSave;
    private String accountNumberSave;
    private String opendDateSave;
    private String closeDateSave;
    private String mobileNoSave, depositTypeSave;
    
      private String depositNameSearch, depositAccountNumber;

    public String getDepositNameSearch() {
        return depositNameSearch;
    }

    public void setDepositNameSearch(String depositNameSearch) {
        this.depositNameSearch = depositNameSearch;
    }

    public String getDepositAccountNumber() {
        return depositAccountNumber;
    }

    public void setDepositAccountNumber(String depositAccountNumber) {
        this.depositAccountNumber = depositAccountNumber;
    }
    
    

    public String getAccountNumberSave() {
        return accountNumberSave;
    }

    public void setAccountNumberSave(String accountNumberSave) {
        this.accountNumberSave = accountNumberSave;
    }

    public String getOpendDateSave() {
        return opendDateSave;
    }

    public void setOpendDateSave(String opendDateSave) {
        this.opendDateSave = opendDateSave;
    }

    public String getCloseDateSave() {
        return closeDateSave;
    }

    public void setCloseDateSave(String closeDateSave) {
        this.closeDateSave = closeDateSave;
    }

    public String getDepositTypeSave() {
        return depositTypeSave;
    }

    public void setDepositTypeSave(String depositTypeSave) {
        this.depositTypeSave = depositTypeSave;
    }

    List<DepositsMaster> displayDeposits;

    HttpServletRequest request;
    HttpServletResponse response;

    public List<DepositsMaster> getDisplayDeposits() {
        return displayDeposits;
    }

    public void setDisplayDeposits(List<DepositsMaster> displayDeposits) {
        this.displayDeposits = displayDeposits;
    }

    public String getRes_msg() {
        return res_msg;
    }

    public void setRes_msg(String res_msg) {
        this.res_msg = res_msg;
    }

    public String getDepositNameSave() {
        return depositNameSave;
    }

    public void setDepositNameSave(String depositNameSave) {
        this.depositNameSave = depositNameSave;
    }

    public String getDepositIdSave() {
        return depositIdSave;
    }

    public void setDepositIdSave(String depositIdSave) {
        this.depositIdSave = depositIdSave;
    }

    public String getAmountSave() {
        return amountSave;
    }

    public void setAmountSave(String amountSave) {
        this.amountSave = amountSave;
    }

    public String getMobileNoSave() {
        return mobileNoSave;
    }

    public void setMobileNoSave(String mobileNoSave) {
        this.mobileNoSave = mobileNoSave;
    }

    public String displayDepositDetails() {
        DepositsAction depositAction = null;
        Map<String, String> displayMap;
        ValueStack stack = null;
        try {
            stack = ActionContext.getContext().getValueStack();
            depositAction = new DepositsAction();
            displayDeposits = depositAction.DisplayDepositsDetails(getDepositAccountNumber(),getDepositNameSearch());
            displayMap = depositAction.getTotalAmount();
            stack.push(displayMap);
//            System.out.println(" getDepositNameSearch()  "+getDepositNameSearch());
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsController(displayDepositDetails) : : " + e.getMessage());
        }
        return SUCCESS;
    }

    public String saveDeposits() {
        DepositsAction depositAction = null;
        Map<String, String> displayMap;
        ValueStack stack = null;
        try {
            stack = ActionContext.getContext().getValueStack();
            depositAction = new DepositsAction();
            int result_type = depositAction.saveDepistsDetails(getDepositNameSave(), getAmountSave(), getAccountNumberSave(), getMobileNoSave(), getDepositTypeSave(), getCloseDateSave(), getOpendDateSave());
            displayDeposits = depositAction.DisplayDepositsDetails(getDepositAccountNumber(),getDepositNameSearch());
            displayMap = depositAction.getTotalAmount();
            stack.push(displayMap);
            if (result_type == 1) {
                res_msg = getDepositNameSave() + " Insertion Successfully!";
            
            } else if (result_type == 0) {
                res_msg = getDepositNameSave() + " Insertion Failed!";
             
            } else if (result_type == -1) {
                res_msg = "Sorry record already existed!";
              
            }

        } catch (Exception ex) {
            System.out.println("Exception occured in DepositsController(saveDeposits) ::" + ex.getMessage());
        }
        return SUCCESS;

    }
    
    
    public String depositDetailsDelete() {

        DepositsAction depositAction = null;
        Map<String, String> displayMap;
        ValueStack stack = null;
        try {
            stack = ActionContext.getContext().getValueStack();
            depositAction = new DepositsAction();
            int i  = depositAction.deleteDeposits(getDepositIdSave());
            displayDeposits = depositAction.DisplayDepositsDetails(getDepositAccountNumber(),getDepositNameSearch());
            displayMap = depositAction.getTotalAmount();
            stack.push(displayMap);
            if (i >= 0) {
                res_msg = getDepositNameSave() + " Deleted Successfully!";
                return SUCCESS;
            } else {
                res_msg = getDepositNameSave() + " Delete Failed!";
                return SUCCESS;
            }

        } catch (Exception ex) {
            System.out.println("Exception occured in DepositsController(updateDepositsDetails) ::" + ex.getMessage());
        }
        return SUCCESS;
    }


    public String updateDepositsDetails() {

        DepositsAction depositAction = null;
        Map<String, String> displayMap;
        ValueStack stack = null;
        try {
            stack = ActionContext.getContext().getValueStack();
            depositAction = new DepositsAction();
            boolean result = depositAction.updateDeposits(getDepositNameSave(), getAmountSave(), getAccountNumberSave(), getDepositIdSave(), getMobileNoSave(), getDepositTypeSave(), getCloseDateSave(), getOpendDateSave());
            displayDeposits = depositAction.DisplayDepositsDetails(getDepositAccountNumber(),getDepositNameSearch());
            displayMap = depositAction.getTotalAmount();
            stack.push(displayMap);
            if (result) {
                res_msg = getDepositNameSave() + " Updation Successfully!";
                return SUCCESS;
            } else {
                res_msg = getDepositNameSave() + " Updation Failed!";
                return "failure";
            }

        } catch (Exception ex) {
            System.out.println("Exception occured in DepositsController(updateDepositsDetails) ::" + ex.getMessage());
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
