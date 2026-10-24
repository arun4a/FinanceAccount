/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.Accounts;

import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sys1089
 */
public class AccountsDetailsAction {

    public int SaveDailySheetDetails(String actualAmount, String paidAmount, String accountNumber, String close, String customerName, String date, String depositAmount, String depositBy, String depositName, String installment, String openingBalance, String createdBy, Connection con) {
        int i = 0;
        PreparedStatement pstSaveDaily = null, pstSaveCustomer = null;
        try {
            pstSaveDaily = con.prepareStatement("INSERT INTO  `accounts`(`deposit_amount`,`deposit_by_partner`,`account_number`,`customer_name`,`installment`,deposit_name,created_by,date,time,customer_paid_amount,amount)VALUES (?,?,?,?,?,?,?,?,now(),?,?)");
            pstSaveDaily.setString(1, depositAmount);
            pstSaveDaily.setString(2, depositBy);
            pstSaveDaily.setString(3, accountNumber);
            pstSaveDaily.setString(4, customerName);
            pstSaveDaily.setString(5, installment);
            pstSaveDaily.setString(6, depositName);
            pstSaveDaily.setString(7, createdBy);
            pstSaveDaily.setString(8, date);
            pstSaveDaily.setString(9, paidAmount);
            pstSaveDaily.setString(10, actualAmount);
            i = pstSaveDaily.executeUpdate();
            try {
                if (close.equals("Yes")) {
                    pstSaveCustomer = con.prepareStatement("update `customer` set status ='close' ,closing_date=? where account_number =?");
                    pstSaveCustomer.setString(1, date);
                    pstSaveCustomer.setString(2, accountNumber);
                    pstSaveCustomer.executeUpdate();

                }
            } catch (Exception e) {
                   System.out.println(" Exception Occured in AccountsDetailsAction(SaveDailySheetDetails) inner :: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(SaveDailySheetDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstSaveDaily);
        }
        return i;
    }

    int SaveCustomerDetails(String bankAccount, String sureRel, String custRel, String accountNumberNew, String accountNo, String originalAmount, String installmentsN, String amountNew, String customerNameN, String issuedDate, String sName, String occupation, String address, String mobileNumber, String suretyName, String sSuretyName, String suretyOccupation, String suretyAddress, String suretyMobileNumber, String atm, String cheque, String passBook, String createdBy, Connection con) {
        int i = 0;
        PreparedStatement pstSaveCustomer = null;
        try {
            pstSaveCustomer = con.prepareStatement(" INSERT INTO  `customer`( `customer_name`,`customer_fatherName`,cus_mobile_number,cus_permanent_address,customer_occupation,`surety_name`, `surety_address`,\n"
                    + "`surety_mobile_number`,`surety_occupation`,surety_fname,`cheque`,`passbook`,atm_submitted,`amount`,`date_of_issue`,`issued_by`,installments,original_amount,account_number,acountNo,sur_rel,cust_rel) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
            pstSaveCustomer.setString(20, bankAccount);
            pstSaveCustomer.setString(21, sureRel);
            pstSaveCustomer.setString(22, custRel);
            i = pstSaveCustomer.executeUpdate();
        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(SaveCustomerDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstSaveCustomer);
        }
        return i;

    }

    public Map<Integer, CustomerMaster> customerDetails(String customerName, String accountNumber, String status, String mobileNumber) {
        Map<Integer, CustomerMaster> customerDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        CustomerMaster customerMaster = null;
        ResultSet res = null, resAccount = null;
        PreparedStatement pstCustomer = null, pstAccounts = null;
        try {
            customerDetails = new LinkedHashMap();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();

            String custmQuery = " SELECT  concat(monthname(closing_date),'-',year(closing_date))  as closeMonthName,concat(monthname(date_of_issue),'-',year(date_of_issue))  as monthName, customer_id, customer_name, customer_fatherName,  surety_name, cus_permanent_address, surety_address, atm_submitted,\n"
                    + "    cus_mobile_number, surety_mobile_number, cheque,  passbook,    amount, DATE_FORMAT(date_of_issue,'%d-%b-%Y') as  dateofissue ,\n"
                    + "    issued_by, surety_occupation, customer_occupation, surety_fname,  installments,\n"
                    + "    original_amount,  status, account_number, acountNo, closing_date as d,DATE_FORMAT(closing_date,'%d-%b-%Y') as  closing_date,  sur_rel,\n"
                    + "    cust_rel, cust_second_mobno, surety_second_mobno\n"
                    + " FROM  customer  ";
            try {

                if (status.equals("Any")) {
                    custmQuery += "  Where account_number='" + accountNumber + "' ";
                } else {
                    custmQuery += "Where  status='" + status + "'";

                    if (!"0".equals(customerName)) {
                        custmQuery += " and customer_name like '%" + customerName + "%' ";
                    }
                    if (!"0".equals(accountNumber)) {
                        custmQuery += "  and account_number='" + accountNumber + "' ";
                    }

                    if (!"0".equals(mobileNumber)) {
                        custmQuery += "  and cus_mobile_number='" + mobileNumber + "' ";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (status.equals("close")) {
                    custmQuery += " order by d ";
                } else {
                    custmQuery += " order by date_of_issue ";
                }
            } catch (Exception e) {
                custmQuery += " order by date_of_issue ";
            }

            System.out.println(" " + custmQuery);
            
            pstCustomer = con.prepareStatement(custmQuery);
            res = pstCustomer.executeQuery();
            while (res.next()) {
                customerMaster = new CustomerMaster();
                customerMaster.setClosingMonthName(res.getString("closeMonthName"));
                customerMaster.setClosingDate(res.getString("closing_date"));
                customerMaster.setMonthName(res.getString("monthName"));
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
                customerMaster.setDateOfIssue(res.getString("dateofissue"));
                customerMaster.setIssuedBy(res.getString("issued_by"));
                customerMaster.setSuretyOccupation(res.getString("surety_occupation"));
                customerMaster.setCustomerOccupation(res.getString("customer_occupation"));
                customerMaster.setSuretyFname(res.getString("surety_fname"));
                customerMaster.setInstallments(res.getString("installments"));
                customerMaster.setOriginalAmount(res.getString("original_amount"));
                customerMaster.setStatus(res.getString("status"));
                customerMaster.setAccountNo(res.getString("acountNo"));
                customerMaster.setAccountNumberNew(res.getString("account_number"));
                customerMaster.setCustRel(res.getString("cust_rel"));
                customerMaster.setSurRel(res.getString("sur_rel"));
                pstAccounts = con.prepareStatement("SELECT sum(ifnull(customer_paid_amount,0)) as paidAmount,count(installment) as completedInstallments FROM accounts where account_number=?");
                pstAccounts.setString(1, res.getString("account_number"));
                resAccount = pstAccounts.executeQuery();
                if (resAccount.next()) {
                    customerMaster.setPaidAmount(resAccount.getDouble("paidAmount"));
                    customerMaster.setCompletedInstallments(resAccount.getInt("completedInstallments"));
                }
                customerDetails.put(res.getInt("customer_id"), customerMaster);

            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(customerDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstAccounts);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
            DBAccessMain.closeResultSet(resAccount);
        }
        return customerDetails;

    }

    public Map<String, String> getTotalAmount(String date) {
        Map<String, String> totalCollectedAmountDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        ResultSet res = null, resDaily = null;
        PreparedStatement pstCustomerDaily = null, pstCustomerMonthly = null;
        try {
            totalCollectedAmountDetails = new HashMap<String, String>();
            totalCollectedAmountDetails.put("totalCollectedAmountDaily", "0");
            totalCollectedAmountDetails.put("totalCollectedAmountMonth", "0");
            totalCollectedAmountDetails = new LinkedHashMap();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstCustomerDaily = con.prepareStatement("SELECT sum(ifnull(customer_paid_amount,0)) as dailyCollectedAmount FROM  accounts where  date= curdate() ");
            resDaily = pstCustomerDaily.executeQuery();
            if (resDaily.next()) {
                totalCollectedAmountDetails.put("totalCollectedAmountDaily", resDaily.getString("dailyCollectedAmount"));
            }

            pstCustomerMonthly = con.prepareStatement("SELECT sum(ifnull(customer_paid_amount,0)) as MonthlyCollectedAmount FROM  accounts where month(date)=month(curdate()) and year(date)=year(curdate())");
            res = pstCustomerMonthly.executeQuery();
            if (res.next()) {
                totalCollectedAmountDetails.put("totalCollectedAmountMonth", res.getString("MonthlyCollectedAmount"));
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(getTotalAmount) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstCustomerMonthly);
            DBAccessMain.closeResultSet(res);
        }
        return totalCollectedAmountDetails;

    }

    public List< InstallmentDetails> installmentDetails(String accountNo) {
        List<InstallmentDetails> installmentsDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        InstallmentDetails instalMaster = null;
        ResultSet res = null, resStatus = null;
        PreparedStatement pstCustomer = null, pstStatus = null;
        try {
            installmentsDetails = new LinkedList<InstallmentDetails>();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstCustomer = con.prepareStatement("SELECT (ifnull(amount,0)-ifnull(customer_paid_amount,0)) as dueAmount, customer_paid_amount,amount,account_id,opening_balance,deposit_amount,deposit_by_partner,customer_id,customer_name,installment,account_number,deposit_name,created_by,DATE_FORMAT(date,'%d-%b-%Y') as date,time FROM  accounts where account_number='" + accountNo + "'");
            res = pstCustomer.executeQuery();
            while (res.next()) {
                instalMaster = new InstallmentDetails();
                instalMaster.setDueAmount(res.getString("dueAmount"));
                instalMaster.setAcutalAmount(res.getString("amount"));
                instalMaster.setPaidAmount(res.getString("customer_paid_amount"));
                instalMaster.setAccountId(res.getString("account_id"));
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

                installmentsDetails.add(instalMaster);
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(installmentDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closeResultSet(res);
        }
        return installmentsDetails;

    }

    public Map<Integer, InstallmentDetails> reports(String customerId) {
        Map<Integer, InstallmentDetails> installmentsDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        InstallmentDetails instalMaster = null;
        ResultSet res = null;
        PreparedStatement pstCustomer = null;
        try {
            installmentsDetails = new LinkedHashMap();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstCustomer = con.prepareStatement("SELECT (ifnull(amount,0)-ifnull(customer_paid_amount,0)) as dueAmount, ifnull(customer_paid_amount,0) as customer_paid_amount,amount, account_id,opening_balance,deposit_amount,deposit_by_partner,customer_id,customer_name,installment,account_number,deposit_name,created_by,DATE_FORMAT(date,'%d-%b-%Y') as date,time FROM  accounts where customer_id='" + customerId + "'");
            res = pstCustomer.executeQuery();
            while (res.next()) {
                instalMaster = new InstallmentDetails();
                instalMaster.setDueAmount(res.getString("dueAmount"));
                instalMaster.setAcutalAmount(res.getString("amount"));
                instalMaster.setPaidAmount(res.getString("customer_paid_amount"));
                instalMaster.setAccountId(res.getString("account_id"));
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
                installmentsDetails.put(res.getInt("account_id"), instalMaster);
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(reports) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
        }
        return installmentsDetails;

    }

    public List< InstallmentDetails> reportsByDates(String fromDate, String toDate) {
        List<InstallmentDetails> installmentsDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        InstallmentDetails instalMaster = null;
        ResultSet res = null;
        PreparedStatement pstCustomer = null;
        try {

            installmentsDetails = new LinkedList<InstallmentDetails>();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstCustomer = con.prepareStatement("SELECT (ifnull(amount,0)-ifnull(customer_paid_amount,0)) as dueAmount, ifnull(customer_paid_amount,0) as customer_paid_amount,amount,account_id,opening_balance,deposit_amount,deposit_by_partner,customer_id,customer_name,installment,account_number,deposit_name,created_by,DATE_FORMAT(date,'%d-%b-%Y') as date,time FROM  accounts where  date between ? and ? ");
            pstCustomer.setString(1, fromDate);
            pstCustomer.setString(2, toDate);
            res = pstCustomer.executeQuery();
            while (res.next()) {
                instalMaster = new InstallmentDetails();
                instalMaster.setDueAmount(res.getString("dueAmount"));
                instalMaster.setAcutalAmount(res.getString("amount"));
                instalMaster.setPaidAmount(res.getString("customer_paid_amount"));
                instalMaster.setAccountId(res.getString("account_id"));
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
                installmentsDetails.add(instalMaster);
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(reportsByDates) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closeResultSet(res);
        }
        return installmentsDetails;

    }

    public Map<String, String> issuedBondsAmountDailyAndMonthly() {
        Map<String, String> issuedBondsAmountDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        ResultSet res = null, resMonthly = null;
        PreparedStatement pstCustomer = null, pstDaily = null;
        try {

            issuedBondsAmountDetails = new HashMap<String, String>();
            issuedBondsAmountDetails.put("TotalOpeningBalance", "0");
            issuedBondsAmountDetails.put("TotalDailyExtraTransaction", "0");
            issuedBondsAmountDetails.put("TotalMonthlyExtraTransaction", "0");

            issuedBondsAmountDetails.put("totalIssuedAmountDaily", "0");
            issuedBondsAmountDetails.put("totalIssuedAmountMonth", "0");
            issuedBondsAmountDetails = new LinkedHashMap();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDaily = con.prepareStatement("select sum(original_amount) as dailyIssuedAmount from  customer where date_of_issue=curdate()");
            resMonthly = pstDaily.executeQuery();
            if (resMonthly.next()) {
                issuedBondsAmountDetails.put("totalIssuedAmountDaily", resMonthly.getString("dailyIssuedAmount"));
            }

            pstCustomer = con.prepareStatement("select sum(original_amount) as monthIssuedAmount from  customer where month(date_of_issue)=month(curdate()) and year(date_of_issue)=year(curdate()) ");
            res = pstCustomer.executeQuery();
            if (res.next()) {
                issuedBondsAmountDetails.put("totalIssuedAmountMonth", res.getString("monthIssuedAmount"));
            }

            pstCustomer = con.prepareStatement("select ((select ifnull(sum(ifnull(customer_paid_amount,0)),0)  FROM  accounts)- (SELECT ifnull(sum(amount),0) FROM extra_transactions)-(SELECT ifnull(sum(original_amount),0) FROM customer where status='open')) as opening");
            res = pstCustomer.executeQuery();
            if (res.next()) {
                issuedBondsAmountDetails.put("TotalOpeningBalance", res.getString("opening"));
            }

            pstDaily = con.prepareStatement("select sum(amount) as dailyIssuedAmount from  extra_transactions where crt_date=curdate()");
            resMonthly = pstDaily.executeQuery();
            if (resMonthly.next()) {
                issuedBondsAmountDetails.put("TotalMonthlyExtraTransaction", resMonthly.getString("dailyIssuedAmount"));
            }

            pstCustomer = con.prepareStatement("select sum(amount) as monthIssuedAmount from  extra_transactions where   month(crt_date)=month(curdate()) and year(crt_date)=year(curdate()) ");
            res = pstCustomer.executeQuery();
            if (res.next()) {
                issuedBondsAmountDetails.put("TotalDailyExtraTransaction", res.getString("monthIssuedAmount"));
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(issuedBondsAmountDailyAndMonthly) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
        }
        return issuedBondsAmountDetails;

    }

    public Map<Integer, CustomerMaster> issuedBondsReport(String startDate, String endDate, String status) {
        Map<Integer, CustomerMaster> customerDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        CustomerMaster customerMaster = null;
        ResultSet res = null, resAccount = null;
        PreparedStatement pstCustomer = null, pstAccounts = null;
        try {
            customerDetails = new LinkedHashMap();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();

            String custmQuery = " SELECT customer_id, customer_name, customer_fatherName,  surety_name, cus_permanent_address, surety_address, atm_submitted,\n"
                    + "    cus_mobile_number, surety_mobile_number, cheque,  passbook,    amount, DATE_FORMAT(date_of_issue,'%d-%b-%Y') as  date_of_issue ,\n"
                    + "    issued_by, surety_occupation, customer_occupation, surety_fname,  installments,\n"
                    + "    original_amount,  status, account_number, acountNo, DATE_FORMAT(closing_date,'%d-%b-%Y') as  closingDate,  sur_rel,\n"
                    + "    cust_rel, cust_second_mobno, surety_second_mobno\n"
                    + "FROM  customer  ";

            try {
                if (status.equals("close")) {
                    custmQuery += " Where date(closing_date) between '" + startDate + "' and '" + endDate + "'  ";

                } else {
                    custmQuery += " Where date_of_issue between '" + startDate + "' and '" + endDate + "'  ";
                }
            } catch (Exception e) {
                e.printStackTrace();
                custmQuery += " Where date_of_issue between '" + startDate + "' and '" + endDate + "'  ";
            }

            try {
                if (!status.equals("")) {
                    custmQuery += "   and  status= '" + status + "' ";
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            pstCustomer = con.prepareStatement(custmQuery);
            res = pstCustomer.executeQuery();
            while (res.next()) {
                customerMaster = new CustomerMaster();
                customerMaster.setClosingDate(res.getString("closingDate"));
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
                pstAccounts = con.prepareStatement("SELECT sum(ifnull(customer_paid_amount,0)) as paidAmount,count(installment) as completedInstallments FROM accounts where account_number=?");
                pstAccounts.setString(1, res.getString("account_number"));
                resAccount = pstAccounts.executeQuery();
                if (resAccount.next()) {
                    customerMaster.setPaidAmount(resAccount.getDouble("paidAmount"));
                    customerMaster.setCompletedInstallments(resAccount.getInt("completedInstallments"));
                }
                customerDetails.put(res.getInt("customer_id"), customerMaster);

            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(issuedBondsReport) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstAccounts);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
            DBAccessMain.closeResultSet(resAccount);
        }
        return customerDetails;

    }

    public Map<String, String> GetCustomerDetails(String accountNumber, Connection con) {

        Map<String, String> dailyMap = new HashMap<String, String>();
        ResultSet res = null;
        PreparedStatement pstCustomer = null;
        try {
            pstCustomer = con.prepareStatement("select customer_name from customer where account_number=?");
            pstCustomer.setString(1, accountNumber);
            res = pstCustomer.executeQuery();
            if (res.next()) {
                dailyMap.put("getDetailsCustomerName", res.getString("customer_name"));
                dailyMap.put("getDetailsAccountNumber", accountNumber);
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in AccountsDetailsAction(GetCustomerDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
        }
        return dailyMap;

    }

}
