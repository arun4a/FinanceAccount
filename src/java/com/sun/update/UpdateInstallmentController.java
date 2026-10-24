/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.update;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.Accounts.AccountsDetailsAction;
import com.sun.Accounts.InstallmentDetails;
import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sys1089
 */
public class UpdateInstallmentController extends ActionSupport {

    private String actualAmount, accountNumber, accountId, openingBalance, depositAmount, depositByPartner, customerName, installment, depositName, createdBy, date, time, close;
    private String message, paidAmount, startDate, endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getDepositByPartner() {
        return depositByPartner;
    }

    public void setDepositByPartner(String depositByPartner) {
        this.depositByPartner = depositByPartner;
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

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    List<InstallmentDetails> installDispaly = new ArrayList();

    List< InstallmentDetails> installList;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<InstallmentDetails> getInstallList() {
        return installList;
    }

    public void setInstallList(List<InstallmentDetails> installList) {
        this.installList = installList;
    }

    public List<InstallmentDetails> getInstallDispaly() {
        return installDispaly;
    }

    public void setInstallDispaly(List<InstallmentDetails> installDispaly) {
        this.installDispaly = installDispaly;
    }

    public String getDailySheetDetailsInReport() {
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            installDispaly = updateAction.DisplayInstallDetails(getAccountId(), con);
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(getDailySheetDetailsInReport) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }

    public String displayInstallments() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();

            account = new AccountsDetailsAction();
            installList = account.installmentDetails(getAccountNumber());
            installDispaly = updateAction.DisplayInstallDetails(getAccountId(), con);
            setMessage("");
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(displayInstallments) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;
    }

    public String updateInstallDetails() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            int i = updateAction.UpdateDailySheetDetails(getActualAmount(), getPaidAmount(), getAccountId(), getAccountNumber(), getClose(), getCustomerName(), getDate(), getDepositAmount(), getDepositByPartner(), getDepositName(), getInstallment(), getOpeningBalance(), getCreatedBy(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Updated Successfully ");
            } else {
                setMessage(getCustomerName() + " Update Failed ");
            }
            installList = account.installmentDetails(getAccountNumber());
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(updateInstallDetails) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;

    }

    public String deleteInstallDetails() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            int i = updateAction.DeleteDailySheetDetails(getAccountId(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Deleted Successfully ");
            } else {
                setMessage(getCustomerName() + " Deleted Failed ");
            }
            installList = account.installmentDetails(getAccountNumber());
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(deleteInstallDetails) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;

    }

    public String displayInstallmentsClose() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            setMessage("");
            account = new AccountsDetailsAction();
            installList = account.installmentDetails(getAccountNumber());
            installDispaly = updateAction.DisplayInstallDetails(getAccountId(), con);

        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(displayInstallmentsClose) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;
    }

    public String updateInstallDetailsClose() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            int i = updateAction.UpdateDailySheetDetails(getActualAmount(), getPaidAmount(), getAccountId(), getAccountNumber(), getClose(), getCustomerName(), getDate(), getDepositAmount(), getDepositByPartner(), getDepositName(), getInstallment(), getOpeningBalance(), getCreatedBy(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Updated Successfully ");
            } else {
                setMessage(getCustomerName() + " Update Failed ");
            }
            installList = account.installmentDetails(getAccountNumber());
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(updateInstallDetailsClose) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;

    }

    public String deleteInstallDetailsClose() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            int i = updateAction.DeleteDailySheetDetails(getAccountId(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Deleted Successfully ");
            } else {
                setMessage(getCustomerName() + " Deleted Failed ");
            }
            installList = account.installmentDetails(getAccountNumber());
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(deleteInstallDetailsClose) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;

    }

    public String updateInstallDetailsInReports() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            int i = updateAction.UpdateDailySheetDetails(getActualAmount(), getPaidAmount(), getAccountId(), getAccountNumber(), getClose(), getCustomerName(), getDate(), getDepositAmount(), getDepositByPartner(), getDepositName(), getInstallment(), getOpeningBalance(), getCreatedBy(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Updated Successfully ");
            } else {
                setMessage(getCustomerName() + " Update Failed ");
            }
            installList = account.installmentDetails(getAccountNumber());
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(updateInstallDetailsInReports) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;

    }

    public String deleteInstallDetailsInReports() {

        AccountsDetailsAction account = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            account = new AccountsDetailsAction();
            int i = updateAction.DeleteDailySheetDetails(getAccountId(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Deleted Successfully ");
            } else {
                setMessage(getCustomerName() + " Deleted Failed ");
            }
            installList = account.installmentDetails(getAccountNumber());
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateInstallmentController(deleteInstallDetailsInReports) :: " + e.getMessage());
        } finally {

        }
        return SUCCESS;

    }

}
