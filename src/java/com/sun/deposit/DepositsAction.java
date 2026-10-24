/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.deposit;

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
 * @author Arun
 */
public class DepositsAction {

    public int saveDepistsDetails(String depositName, String amount, String accountNumber, String mobileNo, String depositType, String closeDate, String openDate) {
        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("INSERT INTO  `deposits`( `deposit_name`,`amount`,`deposit_account_no`,`created_date`,`time`,mobile_number,deposit_type)VALUES  (?,?,?,?,now(),?,?) ");
            pstDeposit.setString(1, depositName.trim());
            pstDeposit.setString(2, amount.trim());
            pstDeposit.setString(3, accountNumber.trim());
            pstDeposit.setString(4, openDate.trim());
            pstDeposit.setString(5, mobileNo.trim());
            pstDeposit.setString(6, depositType.trim());
            result = pstDeposit.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occured in DepositsAction(saveDepistsDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;

    }

    public boolean updateDeposits(String depositName, String amount, String accountNumber, String depositId, String mobileNo, String depositType, String closeDate, String openDate) {
        boolean result = false;

        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("UPDATE  `deposits` set  `deposit_name`=?,`amount`=?,`deposit_account_no`=?, `created_date`=? ,mobile_number=?, deposit_type=? ,closing_date=? where deposit_id=?");
            pstDeposit.setString(1, depositName.trim());
            pstDeposit.setString(2, amount.trim());
            pstDeposit.setString(3, accountNumber.trim());
            pstDeposit.setString(4, openDate.trim());
            pstDeposit.setString(5, mobileNo.trim());
            pstDeposit.setString(6, depositType.trim());
            try {
                pstDeposit.setString(7, closeDate.trim());
            } catch (Exception e) {
                pstDeposit.setString(7, "");
            }

            pstDeposit.setString(8, depositId);
            result = (pstDeposit.executeUpdate() >= 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception Occured in DepositsAction(updateDeposits) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public List<DepositsMaster> DisplayDepositsDetails(String AccountNumber, String DepositName) {
        List<DepositsMaster> display = new LinkedList<DepositsMaster>();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null, pstDetails = null;
        ResultSet rs = null, rsDetails = null;
        DepositsMaster depositsMaster = null;
        try {
            String query = "SELECT closing_date as closingDate,created_date as createdDate,\n"
                    + " deposit_id, deposit_name, coalesce(sum(amount),0) as amount , interest, mobile_number,DATE_FORMAT(created_date,'%d-%b-%Y') as  created_date , \n"
                    + " time, deposit_type, DATE_FORMAT(closing_date,'%d-%b-%Y') as  closing_date , group_concat(deposit_account_no) as deposit_account_no , closing_time  "
                    + "FROM deposits where ";
            try {
                if (AccountNumber.equals(null) || AccountNumber.equals("")) {
                    query += " deposit_account_no is not null  and ";
                } else {
                    query += " deposit_account_no ='" + AccountNumber + "'  and ";
                }
            } catch (Exception e) {
                query += " deposit_account_no is not null  and ";
            }

            try {
                if (DepositName.equals(null) || DepositName.equals("")) {
                    query += " deposit_name is not null   ";
                } else {
                    query += " deposit_name like '%" + DepositName + "%'  ";
                }
            } catch (Exception e) {
                query += " deposit_name is not null   ";
            }
            query += " group by mobile_number order by createdDate  ";
//            System.out.println("=======" + query);
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                double dueAmount = 0;
                depositsMaster = new DepositsMaster();
                depositsMaster.setActualCloseDate(rs.getString("closingDate"));
                depositsMaster.setActualOpenDate(rs.getString("createdDate"));
                depositsMaster.setDepositId(rs.getString("deposit_id"));
                depositsMaster.setDepositName(rs.getString("deposit_name"));
                depositsMaster.setAmount(rs.getString("amount"));
                depositsMaster.setAccountNumber(rs.getString("deposit_account_no"));
                depositsMaster.setMobileNo(rs.getString("mobile_number"));
                depositsMaster.setOpenDate(rs.getString("created_date"));
                depositsMaster.setTime(rs.getString("time"));
                dueAmount = rs.getDouble("amount");
                try {
                    depositsMaster.setCloseDate(rs.getString("closing_date"));
                } catch (Exception e) {
                    depositsMaster.setCloseDate("");
                }

                depositsMaster.setDepositType(rs.getString("deposit_type"));
//                System.out.println(""+ rs.getString("deposit_account_no"));
                System.out.println("select sum(ifnull(amount,0)) as paidAmount,sum(ifnull(paid_amount,0)) as paidAmountFromActaul from deposit_details where  deposit_account_no in ('"+rs.getString("deposit_account_no")+"')");
                pstDetails = con.prepareStatement("select sum(ifnull(amount,0)) as paidAmount,sum(ifnull(paid_amount,0)) as paidAmountFromActaul from deposit_details where  deposit_account_no in ("+rs.getString("deposit_account_no")+") ");
//                pstDetails.setString(1, rs.getString("deposit_account_no"));
                rsDetails = pstDetails.executeQuery();
                if (rsDetails.next()) {
                    dueAmount -= rsDetails.getDouble("paidAmountFromActaul");
                    depositsMaster.setPaidAmount(rsDetails.getString("paidAmount"));
                    depositsMaster.setPadiAmountFromActual(rsDetails.getString("paidAmountFromActaul"));
                }

                depositsMaster.setDueAmount(String.valueOf(dueAmount));

                display.add(depositsMaster);
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsAction(DisplayDepositsDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return display;
    }

    public Map<String, String> getTotalAmount() {
        Map<String, String> display = new HashMap<String, String>();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            double totalAmount = 0.0;
            display.put("TotalDepositAmount", "0");
            display.put("TotalDepositInAmount", "0");
            display.put("TotalDepositOutAmount", "0");
            display.put("TotalMontlyPaidInterest", "0");
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pst = con.prepareStatement("SELECT  sum(ifnull(amount,0)) as InAmount FROM  `deposits` where deposit_type='In' ");
            rs = pst.executeQuery();
            if (rs.next()) {
                display.put("TotalDepositInAmount", rs.getString("InAmount"));
                totalAmount += rs.getDouble("InAmount");
            }

            pst = con.prepareStatement("select ifnull((SELECT  sum(ifnull(amount,0)) as OutAmount FROM  `deposits` where deposit_type='Out'),0)+ \n"
                    + "         ifnull((SELECT sum(ifnull(paid_amount,0)) as paidAmount FROM deposit_details where deposit_account_no in \n"
                    + "         (select deposit_account_no FROM  `deposits` where deposit_type='In' )),0) as totalOut ");
            rs = pst.executeQuery();
            if (rs.next()) {
                display.put("TotalDepositOutAmount", rs.getString("totalOut"));
                totalAmount += rs.getDouble("totalOut");
            }

            pst = con.prepareStatement("select sum(ifnull(amount,0)) as paidAmount from deposit_details ");
            rs = pst.executeQuery();
            if (rs.next()) {
                display.put("TotalMontlyPaidInterest", rs.getString("paidAmount"));
            }
            display.put("TotalDepositAmount", String.valueOf(totalAmount));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception Occured in DepositsAction(getTotalAmount) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return display;
    }

    public int SaveMonthlyInterest(String paidAmount, String accountNumberMonth, String paymentDate, String amountMonth) {
        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("INSERT INTO  deposit_details(crt_date,crt_time,deposit_account_no,amount,paid_amount) VALUES  (?,now(),?,?,?) ");
            pstDeposit.setString(1, paymentDate);
            pstDeposit.setString(2, accountNumberMonth);
            pstDeposit.setString(3, amountMonth);
            pstDeposit.setString(4, paidAmount);
            result = pstDeposit.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occured in DepositsAction(SaveMonthlyInterest) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public int UpdateMonthlyInterest(String paidAm, String depositsId, String paymentDate, String accountNumberMonth, String amountMonth) {
        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("UPDATE  deposit_details set crt_date=?,deposit_account_no=?,amount=? ,paid_amount=? where  deposits_id=? ");
            pstDeposit.setString(1, paymentDate);
            pstDeposit.setString(2, accountNumberMonth);
            pstDeposit.setString(3, amountMonth);
            pstDeposit.setString(4, paidAm);
            pstDeposit.setString(5, depositsId);
            result = pstDeposit.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occured in DepositsAction(UpdateMonthlyInterest) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public int DeleteMonthlyInterest(String depositsId) {
        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("delete from   deposit_details  where   deposits_id=? ");
            pstDeposit.setString(1, depositsId);
            result = pstDeposit.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occured in DepositsAction(DeleteMonthlyInterest) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public List<MonthlyMaster> DisplayMonthlyDetails(String depositAccountNumber) {
        List<MonthlyMaster> display = new LinkedList<MonthlyMaster>();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null, pstDetails = null;
        ResultSet rs = null, rsDetails = null;
        MonthlyMaster depositsMaster = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pst = con.prepareStatement(" SELECT ifnull(paid_amount,0) as paidAmount, deposits_id, DATE_FORMAT(crt_date,'%d-%b-%Y') as  crt_date ,crt_time,deposit_account_no,amount, DATE_FORMAT(update_date,'%d-%b-%Y') as  update_date   FROM deposit_details  where deposit_account_no=? ");
            pst.setString(1, depositAccountNumber);
            rs = pst.executeQuery();
            while (rs.next()) {
                depositsMaster = new MonthlyMaster();
                depositsMaster.setPaidAmountFromActual(rs.getString("paidAmount"));
                depositsMaster.setCrtDate(rs.getString("crt_date"));
                depositsMaster.setCrtTime(rs.getString("crt_time"));
                depositsMaster.setDepositAccountNo(rs.getString("deposit_account_no"));
                depositsMaster.setDepositsPaidId(rs.getString("deposits_id"));
                depositsMaster.setPaidAmount(rs.getString("amount"));
                depositsMaster.setUpdateDate(rs.getString("update_date"));
                display.add(depositsMaster);
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsAction(DisplayMonthlyDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return display;

    }

    public List<MonthlyMaster> GetPaymentInterestDetails(String depositsIdDispaly) {
        List<MonthlyMaster> display = new LinkedList<MonthlyMaster>();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        MonthlyMaster depositsMaster = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pst = con.prepareStatement(" SELECT ifnull(paid_amount,0) as paidAmount, crt_date as crtDate,deposits_id, DATE_FORMAT(crt_date,'%d-%b-%Y') as  crt_date ,crt_time,deposit_account_no,amount, DATE_FORMAT(update_date,'%d-%b-%Y') as  update_date   FROM deposit_details  where deposits_id=? ");
            pst.setString(1, depositsIdDispaly);
            rs = pst.executeQuery();
            while (rs.next()) {
                depositsMaster = new MonthlyMaster();
                depositsMaster.setPaidAmountFromActual(rs.getString("paidAmount"));
                depositsMaster.setActualCrtDate(rs.getString("crtDate"));
                depositsMaster.setCrtDate(rs.getString("crt_date"));
                depositsMaster.setCrtTime(rs.getString("crt_time"));
                depositsMaster.setDepositAccountNo(rs.getString("deposit_account_no"));
                depositsMaster.setDepositsPaidId(rs.getString("deposits_id"));
                depositsMaster.setPaidAmount(rs.getString("amount"));
                depositsMaster.setUpdateDate(rs.getString("update_date"));
                display.add(depositsMaster);
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsAction(GetPaymentInterestDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return display;
    }

    public Map<Integer, DepositsMaster> DepositsReport(String startDate, String endDate, String status) {
        Map<Integer, DepositsMaster> mapReport = new HashMap<Integer, DepositsMaster>();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null, pstDetails = null;
        ResultSet rs = null, rsDetails = null;
        DepositsMaster depositsMaster = null;
        try {
            String query = "SELECT deposit_id, deposit_name, amount, interest, mobile_number,DATE_FORMAT(created_date,'%d-%b-%Y') as  created_date , time, deposit_type, DATE_FORMAT(closing_date,'%d-%b-%Y') as  closing_date , deposit_account_no, closing_time\n"
                    + "FROM  deposits where  created_date between '" + startDate + "' and '" + endDate + "' ";
            try {
                if (status.equals(null) || status.equals("")) {
                    query += " and deposit_type is not null   ";
                } else {
                    query += " and deposit_type ='" + status + "'  ";
                }
            } catch (Exception e) {
                query += " and deposit_type is not null   ";
            }
//            System.out.println(" query " + query);
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                depositsMaster = new DepositsMaster();
                depositsMaster.setDepositId(rs.getString("deposit_id"));
                depositsMaster.setDepositName(rs.getString("deposit_name"));
                depositsMaster.setAmount(rs.getString("amount"));
                depositsMaster.setAccountNumber(rs.getString("deposit_account_no"));
                depositsMaster.setMobileNo(rs.getString("mobile_number"));
                depositsMaster.setOpenDate(rs.getString("created_date"));
                depositsMaster.setTime(rs.getString("time"));
                try {
                    depositsMaster.setCloseDate(rs.getString("closing_date"));
                } catch (Exception e) {
                    depositsMaster.setCloseDate("");
                }
                depositsMaster.setDepositType(rs.getString("deposit_type"));
                pstDetails = con.prepareStatement("select ifnull(sum(ifnull(amount,0)),0) as paidAmount from deposit_details where  deposit_account_no =?");
                pstDetails.setString(1, rs.getString("deposit_account_no"));
                rsDetails = pstDetails.executeQuery();
                if (rsDetails.next()) {
//                    System.out.println(" " + rs.getString("deposit_account_no"));
                    depositsMaster.setPaidAmount(rsDetails.getString("paidAmount"));
                } else {
                    depositsMaster.setPaidAmount("0");
                }
                mapReport.put(rs.getInt("deposit_id"), depositsMaster);
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsAction(DepositsReport) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return mapReport;
    }

    public Map<Integer, MonthlyMaster> MonthlyInterestReport(String startDate, String endDate) {
        Map<Integer, MonthlyMaster> mapReport = new HashMap<Integer, MonthlyMaster>();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null, pstD = null;
        ResultSet rs = null, rsD = null;
        MonthlyMaster depositsMaster = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pst = con.prepareStatement(" SELECT deposits_id, DATE_FORMAT(crt_date,'%d-%b-%Y') as  crt_date ,crt_time,deposit_account_no,amount, DATE_FORMAT(update_date,'%d-%b-%Y') as  update_date   FROM deposit_details where  crt_date between '" + startDate + "' and '" + endDate + "' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                depositsMaster = new MonthlyMaster();
                depositsMaster.setCrtDate(rs.getString("crt_date"));
                depositsMaster.setCrtTime(rs.getString("crt_time"));
                depositsMaster.setDepositAccountNo(rs.getString("deposit_account_no"));
                depositsMaster.setDepositsPaidId(rs.getString("deposits_id"));
                depositsMaster.setPaidAmount(rs.getString("amount"));
                depositsMaster.setUpdateDate(rs.getString("update_date"));
                pstD = con.prepareStatement("select * from deposits where deposit_account_no=?");
                pstD.setString(1, rs.getString("deposit_account_no"));
                rsD = pstD.executeQuery();
                if (rsD.next()) {
                    depositsMaster.setDepositName(rsD.getString("deposit_name"));
                }
                mapReport.put(rs.getInt("deposits_id"), depositsMaster);
            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsAction(MonthlyInterestReport) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
        }
        return mapReport;
    }

    public int deleteDeposits(String depositIdSave) {

        int result = 0;
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstDeposit = null, pstInterest = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            pstDeposit = con.prepareStatement("delete from   deposits  where   deposit_id=? ");
            pstDeposit.setString(1, depositIdSave);
            result = pstDeposit.executeUpdate();

            pstInterest = con.prepareStatement("delete from   deposit_details  where   deposit_account_no=(select deposit_account_no from deposits  where   deposit_id=? ) ");
            pstInterest.setString(1, depositIdSave);
            result = pstInterest.executeUpdate();
//            System.out.println(" " + result);

        } catch (SQLException e) {
            System.out.println(" Exception Occured in DepositsAction(deleteDeposits) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstDeposit);
        }
        return result;
    }

    public Map<String, String> getDepositAmmountDetails(String accountNumberMonth, String date) {

        Map<String, String> mapList = new LinkedHashMap<String, String>();
        mapList.clear();
        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int dates = 0;

        try {
//            System.out.println(accountNumberMonth + " " + date);
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();

//select datediff(crt_date,'2016-06-06') as days,crt_date from deposit_details where deposit_account_no='001' order by crt_date desc limit 1
            pst = con.prepareStatement("select datediff('" + date + "',crt_date) as days,crt_date from deposit_details where deposit_account_no='" + accountNumberMonth + "' order by crt_date desc limit 1");
            rs = pst.executeQuery();
            if (rs.next()) {
                dates = rs.getInt("days");
            }
            DBAccessMain.closeResultSet(rs);
            DBAccessMain.closePrepareStatement(pst);
            pst = con.prepareStatement("SELECT (((D.amount-sum(ifnull(DD.paid_amount,0)))*2)/100) as interest FROM  deposits D left join deposit_details DD "
                    + " on D.deposit_account_no=DD.deposit_account_no  where  D.deposit_account_no=? ");
            pst.setString(1, accountNumberMonth);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (dates == 30 || dates == 31) {
                    mapList.put("Interest", rs.getString("interest"));
                } else {
                    mapList.put("Interest", Math.round((rs.getFloat("interest") / 30) * dates) + "");
                }
                mapList.put("EnterAccountNumber", accountNumberMonth);
                mapList.put("InterestForDays", "for " + dates + " days  ");
                mapList.put("EnterPaymentDate", date);

            }
        } catch (Exception e) {
            System.out.println(" Exception Occured in DepositsAction(getDepositAmmountDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closeResultSet(rs);
            DBAccessMain.closeDbMainConnection(con);

        }

        return mapList;
    }
}
