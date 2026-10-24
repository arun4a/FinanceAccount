/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.user;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author sys1089
 */
public class UserRegistrationController extends ActionSupport implements ServletRequestAware, ServletResponseAware {

   
    private String fatherName, fName, sName, userName, password, phoneNo, address, message;
    HttpServletRequest request;
    HttpServletResponse response;

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String userRegistration() {
        String result = "failure";

        DBAccessMain dbAccess = null;
        Connection con = null;
        UserAction user = null;
        try {
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            user = new UserAction();
            int i = user.RegisterUserDetails(getAddress(), getUserName(), getfName(), getsName(), getPassword(), getPhoneNo(), getFatherName(), con);
            if (i > 0) {
                setMessage(getUserName() + " successfully registerd ");
                result = SUCCESS;
            } else if (i == -1) {
                setMessage(" User Name " + getUserName() + " already exists Please choose another ");
                result = SUCCESS;
            }
        } catch (Exception e) {
            System.out.println(" exception occured in UserRegistrationController(userRegistration) :: " + e.getMessage());
        }

        return result;

    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

}
