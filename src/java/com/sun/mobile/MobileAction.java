/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mobile;

import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sys1089
 */
public class MobileAction {

    public List<MobileMaster> MobileNumberDetails(String customerName, String accountNumber) {
        List<MobileMaster> customerDetails = null;
        DBAccessMain dbAccess = null;
        Connection con = null;
        MobileMaster customerMaster = null;
        ResultSet res = null, resAccount = null;
        PreparedStatement pstCustomer = null, pstAccounts = null;
        try {
            customerDetails = new LinkedList<MobileMaster>();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            String custmQuery = "SELECT * FROM  customer Where  status is not  null ";
            try {
                if (!"0".equals(customerName)) {
                    custmQuery += " and customer_name = '" + customerName + "'";
                }
                if (!"0".equals(accountNumber)) {
                    custmQuery += "  and account_number=" + accountNumber;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(" custmQuery " + custmQuery);
            pstCustomer = con.prepareStatement(custmQuery);
            res = pstCustomer.executeQuery();
            if (res.next()) {
                customerMaster = new MobileMaster();
                customerMaster.setCustId(res.getString("customer_id"));
                customerMaster.setAccountNo(res.getString("account_number"));
                customerMaster.setSuretyMobileNo(res.getString("surety_mobile_number"));
                customerMaster.setCustMobileNo(res.getString("cus_mobile_number"));
                customerMaster.setCustName(res.getString("customer_name"));
                customerMaster.setSuretyName(res.getString("surety_name"));
//              customerMaster.setSuretyMobileNoSec(res.getString("surety_second_mobno"));
//              customerMaster.setCustMobileNoSec(res.getString("cust_second_mobno"));
                customerDetails.add(customerMaster);
                try {
                    if (res.getString("cust_second_mobno") != null || res.getString("surety_second_mobno") != null) {
                        customerMaster = new MobileMaster();
                        customerMaster.setCustId(res.getString("customer_id"));
                        customerMaster.setAccountNo(res.getString("account_number"));
                        customerMaster.setSuretyMobileNo(res.getString("surety_second_mobno"));
                        customerMaster.setCustMobileNo(res.getString("cust_second_mobno"));
                        customerMaster.setCustName(res.getString("customer_name"));
                        customerMaster.setSuretyName(res.getString("surety_name"));
                        customerDetails.add(customerMaster);
                    }
                } catch (Exception e) {
                }

            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in MobileAction(MobileNumberDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstAccounts);
            DBAccessMain.closePrepareStatement(pstCustomer);
            DBAccessMain.closeResultSet(res);
            DBAccessMain.closeResultSet(resAccount);
        }
        return customerDetails;

    }

    public int UpdateCustMobile(String customer2mob, String surety2mob, String customerMobId) {

        DBAccessMain dbAccess = null;
        Connection con = null;
        PreparedStatement pstCustomer = null, pstAccounts = null;
        int i = 0;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
//            System.out.println(customer2mob+" customer2mob "+surety2mob);
            try {
                if (customer2mob.trim().equals(null) || customer2mob.trim().equals("")) {}else{
                    pstCustomer = con.prepareStatement("update customer set cust_second_mobno=? where customer_id=? ");
                    pstCustomer.setString(1, customer2mob);
                    pstCustomer.setString(2, customerMobId);
                    i = pstCustomer.executeUpdate();
                }

            } catch (Exception e) {
            } 
            try {
                if (surety2mob.trim().equals(null) || surety2mob.trim().equals("")) {}else{
                    pstCustomer = con.prepareStatement("update customer set surety_second_mobno=? where customer_id=? ");
                    pstCustomer.setString(1, surety2mob);
                    pstCustomer.setString(2, customerMobId);
                    i = pstCustomer.executeUpdate();
                }
            } catch (Exception e) {
            }

        } catch (Exception e) {
            System.out.println(" Exception Occured in MobileAction(UpdateCustMobile) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
            DBAccessMain.closePrepareStatement(pstAccounts);
            DBAccessMain.closePrepareStatement(pstCustomer);
        }
        return i;

    }

}
