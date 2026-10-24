/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.Accounts;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author sys1089
 */
public class DailySheetController extends ActionSupport implements ServletRequestAware, SessionAware {

    private String date, openingBalance, depositAmount, depositName, depositBy,
            accountNumber, customerName, installment, close, message, actaulAmount;
    private String originalAmount, installmentsN, amountNew, customerNameN, issuedDate, fatherName, occupation, address, mobileNumber, suretyName,
            SuretyFatherName, suretyOccupation, suretyAddress, suretyMobileNumber, atm, cheque, passBook, accountNumberNew, accountNo;
    private String paidAmount;
    private String custRel;
    private String sureRel;

    private Map<String, String> detailMap = new HashMap<String, String>();

    public Map<String, String> getDetailMap() {
        return detailMap;
    }

    public void setDetailMap(Map<String, String> detailMap) {
        this.detailMap = detailMap;
    }

    public String getActaulAmount() {
        return actaulAmount;
    }

    public void setActaulAmount(String actaulAmount) {
        this.actaulAmount = actaulAmount;
    }

    public String getCustRel() {
        return custRel;
    }

    public void setCustRel(String custRel) {
        this.custRel = custRel;
    }

    public String getSureRel() {
        return sureRel;
    }

    public void setSureRel(String sureRel) {
        this.sureRel = sureRel;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    HttpServletRequest request;
    HttpServletResponse response;
    private SessionMap<String, Object> map;
    List< InstallmentDetails> installList;

    public List<InstallmentDetails> getInstallList() {
        return installList;
    }

    public void setInstallList(List<InstallmentDetails> installList) {
        this.installList = installList;
    }

    public String getAccountNumberNew() {
        return accountNumberNew;
    }

    public void setAccountNumberNew(String accountNumberNew) {
        this.accountNumberNew = accountNumberNew;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public String getInstallmentsN() {
        return installmentsN;
    }

    public void setInstallmentsN(String installmentsN) {
        this.installmentsN = installmentsN;
    }

    public String getAmountNew() {
        return amountNew;
    }

    public void setAmountNew(String amountNew) {
        this.amountNew = amountNew;
    }

    public String getCustomerNameN() {
        return customerNameN;
    }

    public void setCustomerNameN(String customerNameN) {
        this.customerNameN = customerNameN;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSuretyName() {
        return suretyName;
    }

    public void setSuretyName(String suretyName) {
        this.suretyName = suretyName;
    }

    public String getSuretyFatherName() {
        return SuretyFatherName;
    }

    public void setSuretyFatherName(String SuretyFatherName) {
        this.SuretyFatherName = SuretyFatherName;
    }

    public SessionMap<String, Object> getMap() {
        return map;
    }

    public void setMap(SessionMap<String, Object> map) {
        this.map = map;
    }

    public String getSuretyOccupation() {
        return suretyOccupation;
    }

    public void setSuretyOccupation(String suretyOccupation) {
        this.suretyOccupation = suretyOccupation;
    }

    public String getSuretyAddress() {
        return suretyAddress;
    }

    public void setSuretyAddress(String suretyAddress) {
        this.suretyAddress = suretyAddress;
    }

    public String getSuretyMobileNumber() {
        return suretyMobileNumber;
    }

    public void setSuretyMobileNumber(String suretyMobileNumber) {
        this.suretyMobileNumber = suretyMobileNumber;
    }

    public String getAtm() {
        return atm;
    }

    public void setAtm(String atm) {
        this.atm = atm;
    }

    public String getPassBook() {
        return passBook;
    }

    public void setPassBook(String passBook) {
        this.passBook = passBook;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public String getDepositBy() {
        return depositBy;
    }

    public void setDepositBy(String depositBy) {
        this.depositBy = depositBy;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String instalDetailsClose() {

        String result = "fail";
        ValueStack stack = ActionContext.getContext().getValueStack();
        DBAccessMain dbAccess = null;
        Connection con = null;
        AccountsDetailsAction account = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            installList = account.installmentDetails(getAccountNumberNew());
            if (installList.size() > 0) {
                result = SUCCESS;
                stack.push(installList);
                setMessage("");
            } else {
                result = "fail";
                setMessage("No Installments Paid");
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DailySheetController(instalDetailsClose) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return result;
    }

    public String instalDetails() {
        String result = "fail";
        ValueStack stack = ActionContext.getContext().getValueStack();
        DBAccessMain dbAccess = null;
        Connection con = null;
        AccountsDetailsAction account = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            installList = account.installmentDetails(getAccountNumberNew());
            if (installList.size() > 0) {
                result = SUCCESS;
                stack.push(installList);
                setMessage("");
            } else {
                result = "fail";
                setMessage("No Installments Paid");
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DailySheetController(instalDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return result;
    }

    public String getCustomerDailySheet() {

        String result = "fail";

        DBAccessMain dbAccess = null;
        Connection con = null;
        AccountsDetailsAction account = null;
        ValueStack stack = ActionContext.getContext().getValueStack();

        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            detailMap = account.GetCustomerDetails(getAccountNumber(), con);
            stack.push(detailMap);
        } catch (Exception e) {
            System.out.println(" Exception Occured in DailySheetController(getCustomerDailySheet) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }

        return SUCCESS;
    }

    public String dailySheet() {
        String result = "fail";

        DBAccessMain dbAccess = null;
        Connection con = null;
        AccountsDetailsAction account = null;

        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            Object createdBy = map.get("userId");
            int i = account.SaveDailySheetDetails(getActaulAmount(), getPaidAmount(), getAccountNumber(), getClose(), getCustomerName(), getDate(), getDepositAmount(), getDepositBy(), getDepositName(), getInstallment(), getOpeningBalance(), String.valueOf(createdBy), con);

            map.put("dailySheetDate", getDate());
            if (i > 0) {
                setMessage(getCustomerName() + " " + getInstallment() + " Installment  Inserted Successfully");
                result = SUCCESS;
            } else {
                setMessage(getCustomerName() + " Insert Failed");
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in DailySheetController(dailySheet) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return result;
    }

    public String customerDetails() {
        String result = "fail";
        DBAccessMain dbAccess = null;
        Connection con = null;
        AccountsDetailsAction account = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            Object createdBy = map.get("userId");
//             , , , , ,  ,  ,  ,  ,
////                            ,  ,  ,  ,atm,checkbox,passBook 
//            System.out.println(" getsSuretyName() "+getSuretyFatherName());
            int i = account.SaveCustomerDetails(getAccountNo(), getSureRel(), getCustRel(), getAccountNumberNew(), getAccountNo(), getOriginalAmount(), getInstallmentsN(), getAmountNew(), getCustomerNameN(), getIssuedDate(), getFatherName(),
                    getOccupation(), getAddress(), getMobileNumber(), getSuretyName(), getSuretyFatherName(), getSuretyOccupation(), getSuretyAddress(),
                    getSuretyMobileNumber(), getAtm(), getCheque(), getPassBook(), String.valueOf(createdBy), con);
            map.put("issuedBondDate", getIssuedDate());
            if (i > 0) {
                setMessage(getCustomerNameN() + " Details Added Successfully");
                result = SUCCESS;
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in DailySheetController(customerDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return result;

    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.map = (SessionMap<String, Object>) map;

    }

}
