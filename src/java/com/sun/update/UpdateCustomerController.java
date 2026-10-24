package com.sun.update;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.Accounts.CustomerMaster;
import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Arun
 */
public class UpdateCustomerController extends ActionSupport {

    private String customerSecMobileNo,suretySecMobileNo,customerId, accountNo, customerName, customerFatherName, suretyName, customerAddres, suretyAddress,
            atm, accountNumberNew, customerMobileNo, suretyMobileNo, cheque, passbook, amount, dateOfIssue, issuedBy, suretyOccupation, customerOccupation, suretyFname, installments, originalAmount, status;
    private String message;
    private String sureRel;
    private String custRel;

    public String getCustomerSecMobileNo() {
        return customerSecMobileNo;
    }

    public void setCustomerSecMobileNo(String customerSecMobileNo) {
        this.customerSecMobileNo = customerSecMobileNo;
    }

    public String getSuretySecMobileNo() {
        return suretySecMobileNo;
    }

    public void setSuretySecMobileNo(String suretySecMobileNo) {
        this.suretySecMobileNo = suretySecMobileNo;
    }

    public String getSureRel() {
        return sureRel;
    }

    public void setSureRel(String sureRel) {
        this.sureRel = sureRel;
    }

    public String getCustRel() {
        return custRel;
    }

    public void setCustRel(String custRel) {
        this.custRel = custRel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNumberNew() {
        return accountNumberNew;
    }

    public void setAccountNumberNew(String accountNumberNew) {
        this.accountNumberNew = accountNumberNew;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerFatherName() {
        return customerFatherName;
    }

    public void setCustomerFatherName(String customerFatherName) {
        this.customerFatherName = customerFatherName;
    }

    public String getSuretyName() {
        return suretyName;
    }

    public void setSuretyName(String suretyName) {
        this.suretyName = suretyName;
    }

    public String getCustomerAddres() {
        return customerAddres;
    }

    public void setCustomerAddres(String customerAddres) {
        this.customerAddres = customerAddres;
    }

    public String getSuretyAddress() {
        return suretyAddress;
    }

    public void setSuretyAddress(String suretyAddress) {
        this.suretyAddress = suretyAddress;
    }

    public String getAtm() {
        return atm;
    }

    public void setAtm(String atm) {
        this.atm = atm;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public String getSuretyMobileNo() {
        return suretyMobileNo;
    }

    public void setSuretyMobileNo(String suretyMobileNo) {
        this.suretyMobileNo = suretyMobileNo;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public String getPassbook() {
        return passbook;
    }

    public void setPassbook(String passbook) {
        this.passbook = passbook;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getSuretyOccupation() {
        return suretyOccupation;
    }

    public void setSuretyOccupation(String suretyOccupation) {
        this.suretyOccupation = suretyOccupation;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getSuretyFname() {
        return suretyFname;
    }

    public void setSuretyFname(String suretyFname) {
        this.suretyFname = suretyFname;
    }

    public String getInstallments() {
        return installments;
    }

    public void setInstallments(String installments) {
        this.installments = installments;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    List<CustomerMaster> displayCustomerDetails;

    public List<CustomerMaster> getDisplayCustomerDetails() {
        return displayCustomerDetails;
    }

    public void setDisplayCustomerDetails(List<CustomerMaster> displayCustomerDetails) {
        this.displayCustomerDetails = displayCustomerDetails;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

        
         public String customerDetailsDispaly() {
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            displayCustomerDetails = updateAction.displayCustomerDetails(getCustomerId(), con);

        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateCustomerController(customerDetailsDispaly) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }
         
         
    public String customerDetailsDispalyClose() {
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            displayCustomerDetails = updateAction.displayCustomerDetails(getCustomerId(), con);

        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateCustomerController(customerDetailsDispaly) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }

        
         public String customerDetailsDelete() { 
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            int i = updateAction.deleteCustomerDetails(getCustomerId(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Deleted Successfully ");
            } else {
                setMessage(getCustomerName() + " Deleted Failed ");
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateCustomerController(customerDetailsDelete) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }
         
    public String customerDetailsUpdates() { 
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            int i = updateAction.updateCustomerDetails(getCustomerSecMobileNo(),getSuretySecMobileNo(),getSureRel(),getCustRel(),getCustomerId(), getAccountNumberNew(), getAccountNo(), getOriginalAmount(), getInstallments(), getAmount(), getCustomerName(), getDateOfIssue(), getCustomerFatherName(), getCustomerOccupation(), getCustomerAddres(), getCustomerMobileNo(), getSuretyName(), getSuretyFname(), getSuretyOccupation(), getSuretyAddress(), getSuretyMobileNo(), getAtm(), getCheque(), getPassbook(), getIssuedBy(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Updated Successfully ");
            } else {
                setMessage(getCustomerName() + " Update Failed ");
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateCustomerController(customerDetailsUpdates) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }

    public String customerDetailsUpdatesClose() { 
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            int i = updateAction.updateCustomerDetails(getCustomerSecMobileNo(),getSuretySecMobileNo(),getSureRel(),getCustRel(),getCustomerId(), getAccountNumberNew(), getAccountNo(), getOriginalAmount(), getInstallments(), getAmount(), getCustomerName(), getDateOfIssue(), getCustomerFatherName(), getCustomerOccupation(), getCustomerAddres(), getCustomerMobileNo(), getSuretyName(), getSuretyFname(), getSuretyOccupation(), getSuretyAddress(), getSuretyMobileNo(), getAtm(), getCheque(), getPassbook(), getIssuedBy(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Updated Successfully ");
            } else {
                setMessage(getCustomerName() + " Update Failed ");
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateCustomerController(customerDetailsUpdatesClose) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }
    
    public String customerDetailsDeleteClose() { 
        DBAccessMain dbAccess = null;
        Connection con = null;
        UpdateDetailsAction updateAction = null;
        try {
            updateAction = new UpdateDetailsAction();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            int i = updateAction.deleteCustomerDetails(getCustomerId(), con);
            if (i > 0) {
                setMessage(getCustomerName() + " Details Deleted Successfully ");
            } else {
                setMessage(getCustomerName() + " Deleted Failed ");
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateCustomerController(customerDetailsDeleteClose) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }
    
}
