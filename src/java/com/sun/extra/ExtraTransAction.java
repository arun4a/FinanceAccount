/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.extra;

import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sys1089
 */
public class ExtraTransAction {

    public Map<String, String> monthlyReportDetails(String startDate) {
        Map<String, String> monthlyReport = new HashMap<String, String>();
        monthlyReport.put("LasMonthNetAmount", "0");
        monthlyReport.put("TotalCollections", "0");
        monthlyReport.put("TotalMontlyCloseAmount", "0");

        monthlyReport.put("TotalDepositsInAmount", "0");

        monthlyReport.put("MonthlyInAmountTotal", "0");

        monthlyReport.put("TotalDepositsOutAmount", "0");
        monthlyReport.put("TotalBondsAmount", "0");
        monthlyReport.put("TotalmonthlyAmount", "0");
        monthlyReport.put("InterestAmount", "0");
        monthlyReport.put("ExtraAmount", "0");

        DBAccessMain dbAccess = null;
        Connection con = null;
        ResultSet res = null, resAccount = null, resExtra = null, resInterest = null, resBonds = null, resDepositOut = null;
        PreparedStatement pstCustomer = null, pstAccounts = null, pstBonds = null, pstAmount = null, pstExtra = null, pstInterest = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            double totalMonthlyInAmount = 0;

            String custmQuery = "  select sum(customer_paid_amount)  as totalCollections  from accounts where monthname(date)= monthname('" + startDate + "') and year(date)=year('" + startDate + "')  ";
            pstAccounts = con.prepareStatement(custmQuery);
            res = pstAccounts.executeQuery();
            if (res.next()) {
                monthlyReport.put("TotalCollections", res.getString("totalCollections"));
                totalMonthlyInAmount += res.getDouble("totalCollections");
            } else {
                monthlyReport.put("TotalCollections", "0");
            }

            pstCustomer = con.prepareStatement("SELECT coalesce(sum(amount),0) as amount FROM deposits where deposit_type='In' and monthname(created_date)= monthname('" + startDate + "') and year(created_date)=year('" + startDate + "')");
            res = pstCustomer.executeQuery();
            if (res.next()) {
                monthlyReport.put("TotalDepositsInAmount", res.getString("amount"));
                totalMonthlyInAmount += res.getDouble("amount");
            }

            pstCustomer = con.prepareStatement("SELECT net_amount FROM net_amount_monthly where month(date)=month('" + startDate + "' - INTERVAL 1 MONTH) and year(date)=year('" + startDate + "' - INTERVAL 1 MONTH)");
            res = pstCustomer.executeQuery();
            if (res.next()) {
                monthlyReport.put("LasMonthNetAmount", res.getString("net_amount") + "");
            }

            monthlyReport.put("MonthlyInAmountTotal", String.valueOf(totalMonthlyInAmount));

            pstBonds = con.prepareStatement("select coalesce( sum(original_amount),0) as amountBonds from customer where monthname(date_of_issue)= monthname('" + startDate + "') and year(date_of_issue)=year('" + startDate + "')");
            resBonds = pstBonds.executeQuery();
            if (resBonds.next()) {
                monthlyReport.put("TotalBondsAmount", resBonds.getString("amountBonds"));
            }

            pstExtra = con.prepareStatement("SELECT coalesce(sum(ifnull(amount,0)),0) as Extra FROM  extra_transactions where monthname(crt_date)= monthname('" + startDate + "') and year(crt_date)=year('" + startDate + "')");
            resExtra = pstExtra.executeQuery();
            if (resExtra.next()) {
                monthlyReport.put("ExtraAmount", resExtra.getString("Extra"));
            }

            pstInterest = con.prepareStatement("SELECT coalesce(sum(ifnull(amount,0)),0) as interest FROM  deposit_details where monthname(crt_date)= monthname('" + startDate + "') and year(crt_date)=year('" + startDate + "')");
            resInterest = pstInterest.executeQuery();
            if (resInterest.next()) {
                monthlyReport.put("InterestAmount", resInterest.getString("interest"));
            }

            String depositOut = "select ifnull((SELECT  sum(ifnull(amount,0)) as OutAmount FROM  `deposits` where deposit_type='Out' and  \n"
                    + " monthname(closing_date)= monthname('" + startDate + "') and year(closing_date)=year('" + startDate + "')  ),0)+ \n"
                    + "       ifnull(\n"
                    + "         (SELECT sum(ifnull(paid_amount,0)) as paidAmount FROM deposit_details  "
                    + "         where monthname(crt_date)= monthname('" + startDate + "') and year(crt_date)=year('" + startDate + "') and  "
                    + "         deposit_account_no in  (select deposit_account_no FROM  `deposits` where deposit_type='In'  )),0) as totalOut";

            pstAmount = con.prepareStatement(depositOut);
            resDepositOut = pstAmount.executeQuery();
            if (resDepositOut.next()) {
                monthlyReport.put("TotalDepositsOutAmount", resDepositOut.getString("totalOut"));
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in ExtraTransAction(monthlyReportDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstAccounts);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
            DBAccessMain.closeResultSet(resAccount);
        }

        return monthlyReport;

    }

    public Map<Integer, ExtraMaster> extraTransactionReport(String startDate, String endDate) {
        Map<Integer, ExtraMaster> customerDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        ExtraMaster customerMaster = null;
        ResultSet res = null, resAccount = null;
        PreparedStatement pstCustomer = null, pstAccounts = null;
        try {
            customerDetails = new LinkedHashMap();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();

            String custmQuery = "SELECT * FROM  extra_transactions Where crt_date between '" + startDate + "' and '" + endDate + "'  ";
            pstCustomer = con.prepareStatement(custmQuery);
            res = pstCustomer.executeQuery();
            while (res.next()) {
                customerMaster = new ExtraMaster();
                customerMaster.setExtraAmount(res.getString("amount"));
                customerMaster.setExtraDate(res.getString("crt_date"));
                customerMaster.setExtraPurpose(res.getString("purpose"));
                customerDetails.put(res.getInt("transaction_id"), customerMaster);

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

    public List<ExtraMaster> DisplayExtraDetails(String startDate) {

        List<ExtraMaster> display = new LinkedList<ExtraMaster>();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null, pstDetails = null;
        ResultSet rs = null, rsDetails = null;
        ExtraMaster depositsMaster = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            String query = "SELECT  * FROM  `extra_transactions`  where  ";
            try {
                if (startDate.equals(null) || startDate.equals("")) {
                    query += " crt_date is not null   ";
                } else {
                    query += " crt_date='" + startDate + "'  ";
                }
            } catch (Exception e) {
                query += " crt_date is not null   ";
            }

            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                depositsMaster = new ExtraMaster();
                depositsMaster.setExtraAmount(rs.getString("amount"));
                depositsMaster.setExtraDate(rs.getString("crt_date"));
                depositsMaster.setExtraId(rs.getString("transaction_id"));
                depositsMaster.setExtraPurpose(rs.getString("purpose"));
                display.add(depositsMaster);
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in ExtraTransAction(DisplayExtraDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return display;
    }

    public int extraUpdate(String extraAmountSave, String extraDateSave, String extraIdSave, String extraPurposeSave) {

        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null, pstInterest = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("update extra_transactions set amount=? , crt_date=?,purpose=? where transaction_id=?");
            pstDeposit.setString(1, extraAmountSave);
            pstDeposit.setString(2, extraDateSave);
            pstDeposit.setString(3, extraPurposeSave);
            pstDeposit.setString(4, extraIdSave);
            result = pstDeposit.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occured in ExtraTransAction(extraUpdate) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public int extraSave(String extraAmountSave, String extraDateSave, String extraPurposeSave) {

        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null, pstInterest = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("insert into  extra_transactions ( amount, crt_date,purpose,crt_time) values(?,?,?,now())");
            pstDeposit.setString(1, extraAmountSave);
            pstDeposit.setString(2, extraDateSave);
            pstDeposit.setString(3, extraPurposeSave);
            result = pstDeposit.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occured in ExtraTransAction(extraSave) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public int extraDelete(String extraIdSave) {

        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("delete from extra_transactions   where transaction_id=?");
            pstDeposit.setString(1, extraIdSave);
            result = pstDeposit.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occured in ExtraTransAction(extraDelete) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public String TotalExtraAmount(String Date) {

        String display = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            String query = "SELECT  sum(amount) as Amount FROM  `extra_transactions`  where  ";
            try {
                if (Date.equals(null) || Date.equals("")) {
                    query += " crt_date is not null   ";
                } else {
                    query += " crt_date='" + Date + "'  ";
                }
            } catch (Exception e) {
                query += " crt_date is not null   ";
            }
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                display = rs.getString("Amount");
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in ExtraTransAction(DisplayExtraDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return display;

    }

}
