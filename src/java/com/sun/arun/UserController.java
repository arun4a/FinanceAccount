/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.arun;

import com.opensymphony.xwork2.ActionSupport;
import java.util.List;

/**
 *
 * @author sys3038
 */
public class UserController extends ActionSupport {

  private int  userId;
  private String password;
  private String msge,userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
  

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsge() {
        return msge;
    }

    public void setMsge(String msge) {
        this.msge = msge;
    }

    

    public List<User> getUserRecords() {
        UserDao userDao = null;
        List<User> userRecords = null;
        try {
            userDao = new UserDao();
            userRecords = userDao.getUserRecords();

        } catch (Exception ex) {
            System.out.println("Exception occured in UserController.java(getUserRecords)::" + ex.getMessage());
        }
        return userRecords;
    }

    public String updateUserRecord() {
        boolean result = false;
        User user = null;
        UserDao userDao = null;
        try {
            user = new User();
            userDao = new UserDao(); 
            user.setUserName(getUserName());
            user.setPassword(getPassword());
            System.out.println(getPassword()+" getPassword() ");
            result = userDao.updateUser(user);
            if (result) {
                setMsge("Your Password Successfully Updated");
            }
        } catch (Exception ex) {
            System.out.println("Exception occured in UserController.java(updateUserRecord)::" + ex.getMessage());
        }

        return "success";
    }
    public String logic()
    {
        System.out.println("Entered");
        return "success";
    }
}
