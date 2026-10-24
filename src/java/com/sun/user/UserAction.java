/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.user;

import com.sun.DBOperations.DBAccessMain;
import com.sun.arun.CryptoUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sys1089
 */
public class UserAction {

    public int RegisterUserDetails(String address, String userName, String fName, String sName, String password, String phoneNo, String fatherName, Connection con) {
        PreparedStatement pst = null;
        int i = 0;
        try {
            pst = con.prepareStatement("INSERT INTO  `user`( `user_name`,`user_phone_number`,`password`,`user_address`,`user_father_name`,`sur_name`,`first_name`) VALUES (?,?,?,?,?,?,?)");
            pst.setString(1, userName);
            pst.setString(2, phoneNo);
            pst.setString(3, password);
            pst.setString(4, address);
            pst.setString(5, fatherName);
            pst.setString(6, sName);
            pst.setString(7, fName);
            i = pst.executeUpdate();

        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                i = -1;
            }

            System.out.println(" Exception occured in UserAction.java(RegisterUserDetails) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pst);
        }
        return i;

    }

    public List<User> userLoginValidate(String userName, String password, Connection con) {
        CryptoUtil util = null;
        List<User> userDetails = new ArrayList<User>();
        User u = null;
        PreparedStatement pst = null, pstLastLogin = null;
        ResultSet rs = null, rsLast = null;
        try {
            pst = con.prepareStatement("select * from user where user_name=? ");
            pst.setString(1, userName);
            rs = pst.executeQuery();
            if (rs.next()) {
                util = new CryptoUtil();
                if (password.equals(util.decrypt(rs.getString("password")))) {
                    u = new User();
                    u.setUserFname(rs.getString("user_father_name"));
                    u.setAddress(rs.getString("user_address"));
                    u.setUserId(rs.getInt("user_id"));
                    u.setUserName(rs.getString("user_name"));
                    u.setPhoneNumber(rs.getString("user_phone_number"));
                    pstLastLogin = con.prepareStatement("select * from login_details where user_id=? order by login_id desc limit 1");
                    pstLastLogin.setString(1, rs.getString("user_id"));
                    rsLast = pstLastLogin.executeQuery();
                    if (rs.next()) {
                        u.setLogin_date(rsLast.getString("login_date"));
                        u.setLogin_time(rsLast.getString("login_time"));
                    }
                    userDetails.add(u);
                }
            }
        } catch (Exception e) {

            System.out.println(" Exception Occured In UserAction (userLoginValidate ) :: " + e.getMessage());
        } finally {
            DBAccessMain.closePrepareStatement(pst);
            DBAccessMain.closePrepareStatement(pstLastLogin);
            DBAccessMain.closeResultSet(rs);
            DBAccessMain.closeResultSet(rsLast);
        }
        return userDetails;
    }

    public int registerLogin(int adminId, Connection con, HttpServletRequest request) {
        int insert = 0;
        PreparedStatement pstmt = null;
        try {

            String ipAddress = request.getHeader("X-FORWARDED-FOR"); // Public IP
            // Address
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            String[] browserdeatils;
            String browserresult = "";
            try {

                if (request.getHeader("User-Agent").indexOf("Firefox") != -1) {
                    browserdeatils = request.getHeader("User-Agent").split("Firefox/");
                    browserresult = "Mozilla FireFox " + browserdeatils[1];
                } else if (request.getHeader("User-Agent").indexOf("Chrome") != -1) {
                    browserdeatils = request.getHeader("User-Agent").split("Chrome/");
                    String[] br1 = browserdeatils[1].split(" ");
                    browserresult = "Google Chrome " + br1[0];
                } else if (request.getHeader("User-Agent").indexOf("MSIE") != -1) {
                    browserdeatils = request.getHeader("User-Agent").split("MSIE ");
                    String[] br1 = browserdeatils[1].split("; ");
                    browserresult = "Internet Explorer " + br1[0];
                } else if (request.getHeader("User-Agent").indexOf("Safari") != -1) {
                    if (request.getHeader("User-Agent").indexOf("Maxthon") != -1) {
                        String[] br1 = request.getHeader("User-Agent").split(" Maxthon/");
                        browserresult = "Maxthon " + br1[1];
                    } else {
                        browserdeatils = request.getHeader("User-Agent").split("Version/");
                        String[] br1 = browserdeatils[1].split(" Safari/");
                        browserresult = "Apple Safari " + br1[0];
                    }
                } else if (request.getHeader("User-Agent").indexOf("Opera Mobi") != -1) {
                    browserdeatils = request.getHeader("User-Agent").split("Version/");
                    browserresult = "Opera Mini(Mobile) " + browserdeatils[1];
                    if (browserdeatils.length > 1) {
                        browserresult = "Opera " + browserdeatils[1];
                    } else {
                        browserresult = " ";
                    }
                } else if (request.getHeader("User-Agent").indexOf("Opera") != -1) {
                    browserdeatils = request.getHeader("User-Agent").split("Version/");
                    if (browserdeatils.length > 1) {
                        browserresult = "Opera " + browserdeatils[1];
                    } else {
                        browserresult = " ";
                    }
                } else {
                    browserresult = " ";
                }
            } catch (Exception e) {
                System.out.println(" Exception while getting browser :"
                        + e.getMessage());
            }

            pstmt = con.prepareStatement("INSERT INTO login_details (user_id,login_date,login_time,login_ip,browser) VALUES(?,CURRENT_DATE,CURRENT_TIME,?,?)");
            pstmt.setInt(1, adminId);
            pstmt.setString(2, ipAddress);
            pstmt.setString(3, browserresult);
            insert = pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("exc in UserAction (registerLogin) : " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
        return insert;
    }
}
