/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.Accounts;

import com.sun.DBOperations.DBAccessMain;
import com.sun.extra.ExtraTransAction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sys1089
 */
public class MonthlyNetAmountThread implements Runnable {

    boolean loop = false;
    Thread netThread = null;
    String date = null;

    public MonthlyNetAmountThread(String date) {
        try {
            this.date = date;
            loop = true;
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {
            if (loop) {
                try {
                    netThread = new Thread(this);
                    netThread.start();

                } catch (Exception e) {
                    System.out.println("Exception in MonthlyNetAmountThread(Initialize):Inner :: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            loop = false;
            System.out.println("Exception in MonthlyNetAmountThread(Initialize):Outer ::" + e.getMessage());
        }
    }

    @Override
    public void run() {

        PreparedStatement pst = null;
        ResultSet rs = null;
        while (loop) {
            Connection con = null;
            try {
                con = new DBAccessMain().getConnection();
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
                Date da = new Date();
                this.date = sm.format(da);
                pst = con.prepareStatement("SELECT * FROM net_amount_monthly ");
                rs = pst.executeQuery();
                while (rs.next()) //                SELECT date_of_issue FROM  customer group by  month(date_of_issue),year(date_of_issue)
                {
//                    System.out.println(rs.getString("date") + " =========net Amount Thread Running==========" + new Date());
                    netAmountCalculation(rs.getString("date"), con);
                }

                netAmountCalculation(date, con);
//                System.out.println(" =========net Amount Thread Running==========" + new Date());

                Thread.currentThread().sleep(1000 * 5 * 60);
            } catch (Exception e) {
                try {
                    Thread.currentThread().sleep(1000 * 5 * 60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MonthlyNetAmountThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Exception in MonthlyNetAmountThread(run):  ::" + e.getMessage());
            } finally {
                DBAccessMain.closeResultSet(rs);
                DBAccessMain.closePrepareStatement(pst);
                DBAccessMain.closeDbMainConnection(con);
            }
        }

    }

    public void destroy() {
        try {
            loop = false;
        } catch (Exception e) {
            loop = false;
            System.out.println("Exception in MonthlyNetAmountThread(destroy):: " + e.getMessage());
        }
    }

    public double getNetAmount(String date, Connection con) {
        double monthlyNetAmount = 0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ExtraTransAction action = null;
        try {
            action = new ExtraTransAction();
            Map<String, String> report = action.monthlyReportDetails(date);

            double amountTotal = 0, netAmountTotal = 0, TotalCollections = 0, TotalDepositsInAmount = 0, MonthlyInAmountTotal = 0, TotalBondsAmount = 0, ExtraAmount = 0, InterestAmount = 0, TotalDepositsOutAmount = 0;

            try {
                TotalCollections = Double.parseDouble(report.get("TotalCollections"));
            } catch (Exception e) {
                TotalCollections = 0;
            }
//            System.out.println("TotalCollections=========== " + TotalCollections);
            try {
                TotalDepositsInAmount = Double.parseDouble(report.get("TotalDepositsInAmount"));
            } catch (Exception e) {
                TotalDepositsInAmount = 0;
            }

            MonthlyInAmountTotal = Double.parseDouble(report.get("MonthlyInAmountTotal"));
            TotalBondsAmount = Double.parseDouble(report.get("TotalBondsAmount"));
            netAmountTotal = TotalCollections + TotalDepositsInAmount - TotalBondsAmount;
            amountTotal = netAmountTotal - Double.parseDouble(report.get("InterestAmount")) - Double.parseDouble(report.get("ExtraAmount"));
            TotalDepositsOutAmount = Double.parseDouble(report.get("TotalDepositsOutAmount"));

            monthlyNetAmount = (amountTotal - TotalDepositsOutAmount);

//            System.out.println("TotalDepositsInAmount=========== " + TotalDepositsInAmount);
//            System.out.println("MonthlyInAmountTotal=========== " + MonthlyInAmountTotal);
//            System.out.println("TotalBondsAmount=========== " + TotalBondsAmount);
//            System.out.println("netAmountTotal=========== " + netAmountTotal);
//            System.out.println("amountTotal=========== " + amountTotal);
//            System.out.println("monthlyNetAmount=========== " + monthlyNetAmount);
            pst = con.prepareStatement("SELECT net_amount FROM net_amount_monthly where month(date)=month('" + date + "' - INTERVAL 1 MONTH) and year(date)=year('" + date + "' - INTERVAL 1 MONTH)");
            rs = pst.executeQuery();
            if (rs.next()) {
//                System.out.println("rs.getDouble(\"net_amount\")=========== " + rs.getDouble("net_amount"));

                monthlyNetAmount = monthlyNetAmount + (rs.getDouble("net_amount"));

//                System.out.println("monthlyNetAmount=========== " + monthlyNetAmount);
            }

            System.out.println(date + "==============monthlyNetAmount========" + monthlyNetAmount);

        } catch (Exception e) {
            System.out.println("Exception in MonthlyNetAmountThread(getNetAmount)::" + e.getMessage());
        }
        return monthlyNetAmount;
    }

    public void netAmountCalculation(String date, Connection con) {
        PreparedStatement pst = null, pstUpdate = null, pstCheck = null;
        ResultSet rs = null, rsCheck = null;
        double monthlyNetAmount = 0;

        try {
            monthlyNetAmount = getNetAmount(date, con);
            pstCheck = con.prepareStatement("SELECT * FROM  net_amount_monthly where month(date)=month('" + date + "') and year(date)=year('" + date + "')");
            rsCheck = pstCheck.executeQuery();
            if (rsCheck.next()) {
                pstUpdate = con.prepareStatement("UPDATE  `net_amount_monthly` SET `date` =?,`net_amount` = ? WHERE `id` = ?");
                pstUpdate.setString(1, date);
                pstUpdate.setString(2, monthlyNetAmount + "");
                pstUpdate.setString(3, rsCheck.getString("id"));
            } else {
                pstUpdate = con.prepareStatement("INSERT INTO `net_amount_monthly`(`date`,`net_amount`) VALUES (?,?)");
                pstUpdate.setString(1, date);
                pstUpdate.setString(2, monthlyNetAmount + "");
            }
            pstUpdate.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception in MonthlyNetAmountThread(netAmountCalculation)::" + e.getMessage());
        } finally {
            DBAccessMain.closeResultSet(rs);
            DBAccessMain.closeResultSet(rsCheck);
            DBAccessMain.closePrepareStatement(pstCheck);
            DBAccessMain.closePrepareStatement(pstUpdate);
            DBAccessMain.closePrepareStatement(pst);
        }
    }

}
