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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author sys1089
 */
public class DepositMonthPaymentController extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private String accountNumberMonth, paymentDate, res_msg;
    private String amountMonth, depositAccountNumber;
    private String paidAmountFromActualUpdate, depositsIdDispaly, depositsIdUpdate, date, depositAccountNoUpdate, paidAmountUpdate, paidAmountA;

    public String getPaidAmountFromActualUpdate() {
        return paidAmountFromActualUpdate;
    }

    public void setPaidAmountFromActualUpdate(String paidAmountFromActualUpdate) {
        this.paidAmountFromActualUpdate = paidAmountFromActualUpdate;
    }

    public String getPaidAmountA() {
        return paidAmountA;
    }

    public void setPaidAmountA(String paidAmountA) {
        this.paidAmountA = paidAmountA;
    }

    public String getDepositsIdUpdate() {
        return depositsIdUpdate;
    }

    public void setDepositsIdUpdate(String depositsIdUpdate) {
        this.depositsIdUpdate = depositsIdUpdate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepositAccountNoUpdate() {
        return depositAccountNoUpdate;
    }

    public void setDepositAccountNoUpdate(String depositAccountNoUpdate) {
        this.depositAccountNoUpdate = depositAccountNoUpdate;
    }

    public String getPaidAmountUpdate() {
        return paidAmountUpdate;
    }

    public void setPaidAmountUpdate(String paidAmountUpdate) {
        this.paidAmountUpdate = paidAmountUpdate;
    }

    HttpServletRequest request;
    HttpServletResponse response;

    public String getDepositsIdDispaly() {
        return depositsIdDispaly;
    }

    public void setDepositsIdDispaly(String depositsIdDispaly) {
        this.depositsIdDispaly = depositsIdDispaly;
    }
    private Map<String, String> stateMap = new LinkedHashMap<String, String>();

    List<DepositsMaster> displayDeposits;

    List<MonthlyMaster> displayPaymentDetails;
    List<MonthlyMaster> displayPaymentInterest;

    public Map<String, String> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<String, String> stateMap) {
        this.stateMap = stateMap;
    }

    public List<MonthlyMaster> getDisplayPaymentInterest() {
        return displayPaymentInterest;
    }

    public void setDisplayPaymentInterest(List<MonthlyMaster> displayPaymentInterest) {
        this.displayPaymentInterest = displayPaymentInterest;
    }

    public List<MonthlyMaster> getDisplayPaymentDetails() {
        return displayPaymentDetails;
    }

    public void setDisplayPaymentDetails(List<MonthlyMaster> displayPaymentDetails) {
        this.displayPaymentDetails = displayPaymentDetails;
    }

    public String getDepositAccountNumber() {
        return depositAccountNumber;
    }

    public void setDepositAccountNumber(String depositAccountNumber) {
        this.depositAccountNumber = depositAccountNumber;
    }

    public String getAccountNumberMonth() {
        return accountNumberMonth;
    }

    public void setAccountNumberMonth(String accountNumberMonth) {
        this.accountNumberMonth = accountNumberMonth;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getAmountMonth() {
        return amountMonth;
    }

    public void setAmountMonth(String amountMonth) {
        this.amountMonth = amountMonth;
    }

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

    public String getAccountDeatils() {

        Map<String, String> displayMap;
        DepositsAction depositAction = null;
        ValueStack va = ActionContext.getContext().getValueStack();

        try {
//            System.out.println(" getPaymentDategetPaymentDate =========" + getPaymentDate());
            displayMap = new HashMap<String, String>();
            depositAction = new DepositsAction(); 
            stateMap = depositAction.getDepositAmmountDetails(getAccountNumberMonth(), getPaymentDate().replaceAll(",", ""));
            va.push(stateMap);
            displayDeposits = depositAction.DisplayDepositsDetails("", "");
            displayMap = depositAction.getTotalAmount();
            va.push(displayMap);

        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsController(getAccountDeatils) : : " + e.getMessage());
        }

        return SUCCESS;
    }

    public String updateMonthPayment() {
        DepositsAction depositAction = null;
        try {
            depositAction = new DepositsAction();
            int i = depositAction.UpdateMonthlyInterest(getPaidAmountFromActualUpdate(), getDepositsIdUpdate(), getDate(), getDepositAccountNoUpdate(), getPaidAmountUpdate());
            if (i > 0) {
                setRes_msg(getDepositAccountNoUpdate() + " Monthly Interest record Updated successfully ");
            } else {
                setRes_msg(getDepositAccountNoUpdate() + " Monthly Interest record Failed To Update ");
            }
            displayPaymentDetails = depositAction.DisplayMonthlyDetails(getDepositAccountNumber());
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsController(updateMonthPayment) : : " + e.getMessage());
        }
        return SUCCESS;
    }

    public String deleteMonthPayment() {
        DepositsAction depositAction = null;
        try {
            depositAction = new DepositsAction();
            int i = depositAction.DeleteMonthlyInterest(getDepositsIdUpdate());
            if (i > 0) {
                setRes_msg(getDepositAccountNoUpdate() + " Monthly Interest record Deleted successfully ");
            } else {
                setRes_msg(getDepositAccountNoUpdate() + " Monthly Interest record Failed To delete ");
            }

            displayPaymentDetails = depositAction.DisplayMonthlyDetails(getDepositAccountNumber());

        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsController(deleteMonthPayment) : : " + e.getMessage());
        }
        return SUCCESS;
    }

    public String displayPaymentInterestDetails() {
        DepositsAction depositAction = null;
        try {
            depositAction = new DepositsAction();
            displayPaymentDetails = depositAction.DisplayMonthlyDetails(getDepositAccountNumber());
            displayPaymentInterest = depositAction.GetPaymentInterestDetails(getDepositsIdDispaly());

        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsController(displayPaymentInterestDetails) : : " + e.getMessage());
        }
        return SUCCESS;
    }

    public String displayMonthlyDetails() {
        DepositsAction depositAction = null;
        try {
            depositAction = new DepositsAction();
            displayPaymentDetails = depositAction.DisplayMonthlyDetails(getDepositAccountNumber());

        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsController(displayMonthlyDetails) : : " + e.getMessage());
        }
        return SUCCESS;
    }

    public String depositMonthPayment() {
        int i = 0;
        Map<String, String> displayMap;
        DepositsAction depositAction = null;
        ValueStack stack = ActionContext.getContext().getValueStack();
        try {
            displayMap = new HashMap<String, String>();
            depositAction = new DepositsAction();
            i = depositAction.SaveMonthlyInterest(getPaidAmountA(), getAccountNumberMonth(), getPaymentDate(), getAmountMonth());

            if (i > 0) {
                setRes_msg(getAccountNumberMonth() + " Monthly Interest record Inserted successfully ");
            } else {
                setRes_msg(getAccountNumberMonth() + " Monthly Interest record Failed To Inserted ");
            }
            displayDeposits = depositAction.DisplayDepositsDetails("", "");

            displayMap = depositAction.getTotalAmount();
            stack.push(displayMap);
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsController(depositMonthPayment) : : " + e.getMessage());
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
