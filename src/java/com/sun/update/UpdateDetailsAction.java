/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.update;

import com.sun.Accounts.CustomerMaster;
import com.sun.Accounts.InstallmentDetails;
import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sys1089
 */
public class UpdateDetailsAction {

    public List<InstallmentDetails> DisplayInstallDetails(String installmentId, Connection con) {
        List<InstallmentDetails> display = null;
        PreparedStatement pstDisplay = null, pstStatus = null;
        ResultSet res = null, resStatus = null;
        InstallmentDetails instalMaster = null;
        try {

            display = new ArrayList<InstallmentDetails>();
            pstDisplay = con.prepareStatement(" select (ifnull(amount,0)-ifnull(customer_paid_amount,0)) as dueAmount, customer_paid_amount,amount,account_id,opening_balance,deposit_amount,deposit_by_partner,customer_id,customer_name,installment,account_number,deposit_name,created_by,date,time from accounts where account_id=?");
            pstDisplay.setString(1, installmentId);
            res = pstDisplay.executeQuery();
            if (res.next()) {
                instalMaster = new InstallmentDetails();
                instalMaster.setAccountId(res.getString("account_id"));
                instalMaster.setDueAmount(res.getString("dueAmount"));
                instalMaster.setAcutalAmount(res.getString("amount"));
                instalMaster.setPaidAmount(res.getString("customer_paid_amount"));
                instalMaster.setOpeningBalance(res.getString("opening_balance"));
                instalMaster.setDepositAmount(res.getString("deposit_amount"));
                instalMaster.setDepositByPartner(res.getString("deposit_by_partner"));
                instalMaster.setCustomerId(res.getString("customer_id"));
                instalMaster.setCustomerName(res.getString("customer_name"));
                instalMaster.setInstallment(res.getString("installment"));
                instalMaster.setAccountNumber(res.getString("account_number"));
                instalMaster.setDepositName(res.getString("deposit_name"));
                instalMaster.setCreatedBy(res.getString("created_by"));
                instalMaster.setDate(res.getString("date"));
                instalMaster.setTime(res.getString("time"));
                pstStatus = con.prepareStatement("select status from customer where account_number=?");
                pstStatus.setString(1, res.getString("account_number"));
                resStatus = pstStatus.executeQuery();
                if (resStatus.next()) {
                    instalMaster.setStatus(resStatus.getString("status"));
                }
                display.add(instalMaster);
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateDetailsAction(DisplayInstallDetails) :: " + e.getMessage());
        }
        return display;
    }

    public int UpdateDailySheetDetails(String actualAmount, String paidAmount, String accountId, String accountNumber, String close, String customerName, String date, String depositAmount, String depositBy, String depositName, String installment, String openingBalance, String createdBy, Connection con) {
        int i = 0;
        PreparedStatement pstUpdateDaily = null, pstSaveCustomer = null;
        try {
            pstUpdateDaily = con.prepareStatement("UPDATE  `accounts` set `opening_balance`=?,`deposit_amount`=?,`deposit_by_partner`=?,`account_number`=?,`customer_name`=?,`installment`=?,deposit_name=?,created_by=?,date=? ,amount=?, customer_paid_amount=? WHERE  account_id=? ");
            pstUpdateDaily.setString(1, openingBalance);
            pstUpdateDaily.setString(2, depositAmount);
            pstUpdateDaily.setString(3, depositBy);
            pstUpdateDaily.setString(4, accountNumber);
            pstUpdateDaily.setString(5, customerName);
            pstUpdateDaily.setString(6, installment);
            pstUpdateDaily.setString(7, depositName);
            pstUpdateDaily.setString(8, createdBy);
            pstUpdateDaily.setString(9, date);
            pstUpdateDaily.setString(10, actualAmount);
            pstUpdateDaily.setString(11, paidAmount);
            pstUpdateDaily.setString(12, accountId);
            i = pstUpdateDaily.executeUpdate();

            if (close.equals("Yes")) {
                pstSaveCustomer = con.prepareStatement("update `customer` set status ='close',closing_date=? where account_number =?");
                pstSaveCustomer.setString(1, date);
                pstSaveCustomer.setString(2, accountNumber);
                pstSaveCustomer.executeUpdate(); 
            } else {
                pstSaveCustomer = con.prepareStatement("update `customer` set status ='open',closing_date=? where account_number =?");
                pstSaveCustomer.setString(2, accountNumber);
                pstSaveCustomer.setString(1, "");
                pstSaveCustomer.executeUpdate();
            }

        } catch (Exception e) { 
            System.out.println(" Exception Occured in UpdateDetailsAction(UpdateDailySheetDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstUpdateDaily);
            DBAccessMain.closePrepareStatement(pstSaveCustomer);
        }
        return i;
    }

    public List<CustomerMaster> displayCustomerDetails(String customerId, Connection con) {
        List<CustomerMaster> displayCustomer = null;
        CustomerMaster customerMaster = null;
        ResultSet res = null, resAccount = null;
        PreparedStatement pstCustomer = null, pstAccounts = null;
        try {
            displayCustomer = new LinkedList<CustomerMaster>();
            pstCustomer = con.prepareStatement("SELECT * FROM  customer  where customer_id=?");
            pstCustomer.setString(1, customerId);
            res = pstCustomer.executeQuery();
            while (res.next()) {
                customerMaster = new CustomerMaster();

                customerMaster.setCustomerSecMobileNo(res.getString("cust_second_mobno"));
                customerMaster.setSuretySecMobileNo(res.getString("surety_second_mobno"));

                customerMaster.setAmount(res.getString("amount"));
                customerMaster.setAtm(res.getString("atm_submitted"));
                customerMaster.setCustomerId(res.getString("customer_id"));
                customerMaster.setCustomerName(res.getString("customer_name"));
                customerMaster.setCustomerFatherName(res.getString("customer_fatherName"));
                customerMaster.setSuretyName(res.getString("surety_name"));
                customerMaster.setCustomerAddres(res.getString("cus_permanent_address"));
                customerMaster.setSuretyAddress(res.getString("surety_address"));
                customerMaster.setCustomerMobileNo(res.getString("cus_mobile_number"));
                customerMaster.setSuretyMobileNo(res.getString("surety_mobile_number"));
                customerMaster.setCheque(res.getString("cheque"));
                customerMaster.setPassbook(res.getString("passbook"));
                customerMaster.setDateOfIssue(res.getString("date_of_issue"));
                customerMaster.setIssuedBy(res.getString("issued_by"));
                customerMaster.setSuretyOccupation(res.getString("surety_occupation"));
                customerMaster.setCustomerOccupation(res.getString("customer_occupation"));
                customerMaster.setSuretyFname(res.getString("surety_fname"));
                customerMaster.setInstallments(res.getString("installments"));
                customerMaster.setOriginalAmount(res.getString("original_amount"));
                customerMaster.setStatus(res.getString("status"));
                customerMaster.setAccountNumberNew(res.getString("account_number"));
                customerMaster.setSurRel(res.getString("sur_rel"));
                customerMaster.setCustRel(res.getString("cust_rel"));
                customerMaster.setAccountNo(res.getString("acountNo"));

                displayCustomer.add(customerMaster);
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateDetailsAction(displayCustomerDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstAccounts);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
            DBAccessMain.closeResultSet(resAccount);
        }
        return displayCustomer;
    }

    public int updateCustomerDetails(String custSecMobile, String surettySecMobile, String surRel, String custRel, String customerId, String accountNumberNew, String accountNo, String originalAmount, String installmentsN, String amountNew, String customerNameN, String issuedDate, String sName, String occupation, String address, String mobileNumber, String suretyName, String sSuretyName, String suretyOccupation, String suretyAddress, String suretyMobileNumber, String atm, String cheque, String passBook, String createdBy, Connection con) {
        int i = 0;
        PreparedStatement pstSaveCustomer = null;
        try {
            pstSaveCustomer = con.prepareStatement("UPDATE `customer` set `customer_name`=?,`customer_fatherName`=?,cus_mobile_number=?,cus_permanent_address=?,customer_occupation=?,`surety_name`=?, `surety_address`=?,\n"
                    + "`surety_mobile_number`=?,`surety_occupation`=?,surety_fname=?,`cheque`=?,`passbook`=?,atm_submitted=?,`amount`=?,`date_of_issue`=?,`issued_by`=?,installments=?,original_amount=?,account_number=?,acountNo=?,sur_rel=?,cust_rel=?,cust_second_mobno=?,surety_second_mobno=? where customer_id=?  ");
            pstSaveCustomer.setString(1, customerNameN);
            pstSaveCustomer.setString(2, sName);
            pstSaveCustomer.setString(3, mobileNumber);
            pstSaveCustomer.setString(4, address);
            pstSaveCustomer.setString(5, occupation);
            pstSaveCustomer.setString(6, suretyName);
            pstSaveCustomer.setString(7, suretyAddress);
            pstSaveCustomer.setString(8, suretyMobileNumber);
            pstSaveCustomer.setString(9, suretyOccupation);
            pstSaveCustomer.setString(10, sSuretyName);
            pstSaveCustomer.setString(11, cheque);
            pstSaveCustomer.setString(12, passBook);
            pstSaveCustomer.setString(13, atm);
            pstSaveCustomer.setString(14, amountNew);
            pstSaveCustomer.setString(15, issuedDate);
            pstSaveCustomer.setString(16, createdBy);
            pstSaveCustomer.setString(17, installmentsN);
            pstSaveCustomer.setString(18, originalAmount);
            pstSaveCustomer.setString(19, accountNumberNew);
            pstSaveCustomer.setString(20, accountNo);
            pstSaveCustomer.setString(21, surRel);
            pstSaveCustomer.setString(22, custRel);
            pstSaveCustomer.setString(23, custSecMobile);
            pstSaveCustomer.setString(24, surettySecMobile);
            pstSaveCustomer.setString(25, customerId);
            i = pstSaveCustomer.executeUpdate();

        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateDetailsAction(updateCustomerDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstSaveCustomer);
        }
        return i;

    }

    public int deleteCustomerDetails(String customerId, Connection con) {
        int i = 0;
        PreparedStatement pstSaveCustomer = null;
        try {
            pstSaveCustomer = con.prepareStatement("delete from `customer`    where customer_id=?  ");
            pstSaveCustomer.setString(1, customerId);
            i = pstSaveCustomer.executeUpdate();
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateDetailsAction(updateCustomerDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstSaveCustomer);
        }
        return i;
    }

    int DeleteDailySheetDetails(String accountId, Connection con) {
        int i = 0;
        PreparedStatement pstUpdateDaily = null, pstSaveCustomer = null;
        try {
            pstUpdateDaily = con.prepareStatement("DELETE  from `accounts` WHERE  account_id=? ");
            pstUpdateDaily.setString(1, accountId);
            i = pstUpdateDaily.executeUpdate();
        } catch (Exception e) {
            System.out.println(" Exception Occured in UpdateDetailsAction(DeleteDailySheetDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstUpdateDaily);
            DBAccessMain.closePrepareStatement(pstSaveCustomer);
        }
        return i;
    }

}
