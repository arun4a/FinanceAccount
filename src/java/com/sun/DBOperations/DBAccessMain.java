/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.DBOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author Arun
 */
public class DBAccessMain {

    Connection con;
    BasicDataSource bds;

    public DBAccessMain() {
        con = null;
        bds = null;
        Context intCntxt = null, envCntxt = null;
        try {
            if (bds == null) {
                intCntxt = new InitialContext();
                envCntxt = (Context) intCntxt.lookup("java:comp/env");
                bds = (BasicDataSource) envCntxt.lookup("jdbc/MainDB");

            }

        } catch (Exception e) {
            System.out.println(" Exception Occured In DBAccessMain.java(DBAccessMain()) :: " + e.getMessage());
        } finally {
            intCntxt = null;
            envCntxt = null;
        }
    }

    public Connection getConnection() {

        try {
            con = null;
            synchronized (bds) {
                con = bds.getConnection();
                con.setReadOnly(false);
            }
            return con;
        } catch (Exception e) {
            System.out.println("Exception occured in DBAccessMain(getConnection) ::" + e.getMessage());
            return null;
        } finally {
            con = null;
            //ds=null;
        }
    }

    public static void closeDbMainConnection(java.sql.Connection con) {
        try {
            if (con != null) {
                con.close();
            }
            con = null;
        } catch (Exception e) {
            // System.out.println("Exception occured in DBConnectionMain(closeDbMainConnection) ::"+e.getMessage());
        }
    }

    public static void closePrepareStatement(PreparedStatement pstmt) {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            System.out.println("Exception occured in DBAccessMain(closePrepareStatement) ::" + e.getMessage());
        }
    }

    public static void closeResultSet(ResultSet res) {
        try {
            if (res != null) {
                res.close();
            }
            res = null;
        } catch (Exception e) {
            System.out.println("Exception occured in DBAccessMain(closeResultSet) ::" + e.getMessage());
        }
    }

}
