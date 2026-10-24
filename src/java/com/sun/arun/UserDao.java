/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.arun;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author sys3038
 */
public class UserDao {

    public List<User> getUserRecords() {
        List<User> userList = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "way2online");
            pstmt = con.prepareStatement("select user_id,user_name,user_phone_number, password,user_address,user_father_name,sur_name,first_name from user");
            rs = pstmt.executeQuery();
            userList = new ArrayList<User>();
            while (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setUserPhoneNumber(rs.getString("user_phone_number"));
                user.setPassword(rs.getString("password"));
                user.setUserAddress(rs.getString("user_address"));
                user.setUserFatherName(rs.getString("user_father_name"));
                user.setSurName(rs.getString("sur_name"));
                user.setFirstName(rs.getString("first_name"));

                userList.add(user);

            }

        } catch (ClassNotFoundException ex) {
            System.out.println("Exception occured in UserDao.java(getUserRecords)::" + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Exception occured in UserDao.java(getUserRecords)::" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception occured in UserDao.java(getUserRecords)::" + ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return userList;
    }

    public boolean deleteUser(int id) {
        boolean flag = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "way2online");
            pstmt = con.prepareStatement("delete from user where user_id=?");
            pstmt.setInt(1, id);
            flag = pstmt.executeUpdate() >= 1;
        } catch (ClassNotFoundException ex) {
            System.out.println("Exception occured in UserDao.java(deleteUser)::" + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Exception occured in UserDao.java(deleteUser)::" + ex.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return flag;
    }

    public boolean updateUser(User user) {
        CryptoUtil util=null;
        boolean flag = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            util=new CryptoUtil();
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "root");
            pstmt = con.prepareStatement("update user set password=? where user_name=?");
            System.out.println(" "+util.encrypt(user.getPassword())); 
            pstmt.setString(1, util.encrypt(user.getPassword()));
            pstmt.setString(2, user.getUserName());
            flag = pstmt.executeUpdate() >= 1;
        } catch (ClassNotFoundException ex) {
            System.out.println("Exception occured in UserDao.java(updateUser)::" + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Exception occured in UserDao.java(updateUser)::" + ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return flag;
    }

}
