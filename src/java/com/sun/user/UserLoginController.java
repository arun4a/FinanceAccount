/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.user;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import com.sun.Accounts.AccountsDetailsAction;
import com.sun.Accounts.MonthlyNetAmountThread;
import com.sun.DBOperations.DBAccessMain;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author sys1089
 */
public class UserLoginController extends ActionSupport implements SessionAware, ServletRequestAware {

    HttpServletRequest request;
    private String userName;
    private String password;
    private String errorMsg;
    private String id;

    private SessionMap<String, Object> map;
    private List<User> userDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    public String userLogin() {
        HttpSession sess = null;
        String result = "success";
        UserAction userAction = null;
        Connection con = null;
        DBAccessMain dbAccess = null;
        AccountsDetailsAction accountDetails = null;
        ValueStack valueStack = null;
        try {
            sess = request.getSession(true);
            valueStack = ActionContext.getContext().getValueStack();
            setId(sess.getId());
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();
            userAction = new UserAction();
            userDetails = userAction.userLoginValidate(getUserName(), getPassword(), con);
            if (userDetails.size() > 0) {

                accountDetails = new AccountsDetailsAction();
                MonthlyNetAmountThread thread = new MonthlyNetAmountThread("");

                Map<String, String> issuedBonds = accountDetails.issuedBondsAmountDailyAndMonthly();
                Map<String, String> totalAmount = accountDetails.getTotalAmount("");

                //    System.out.println(accountDetails.issuedBondsAmountDailyAndMonthly()+" 88888  "+accountDetails.getTotalAmount(""));
                valueStack.push(issuedBonds);
                valueStack.push(totalAmount);
                map.put("userDetails", userDetails);
                map.put("userId", userDetails.get(0).getUserId());
                map.put("userName", userDetails.get(0).getUserName());
                map.remove("error");
                userAction.registerLogin(userDetails.get(0).getUserId(), con, request);

            } else {
                result = "failure";
                map.put("error", "Please Enter Correct User Name and password");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception Occured In UserLoginController(userLogin) :: " + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return result;
    }

    public String logout() {
        try {
            map.remove("userDetails");
        } catch (Exception e) {
        }
        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.map = (SessionMap<String, Object>) map;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

}
