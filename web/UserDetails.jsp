<%-- 
    Document   : UserDetails
    Created on : 29 Apr, 2016, 3:30:38 PM
    Author     : sys3038
--%>

<%@page import="java.util.Iterator"%>
<%@page import="com.sun.arun.User"%>
<%@page import="java.util.List"%>
<%@page import="com.sun.arun.UserDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Finance Account</title>
        <link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">
        <script>
            function Validate() {
                var password = document.getElementById("password").value;
                var cnewPassword = document.getElementById("cnewPassword").value;
                if (password == "")
                {
                    alert("Please enter Password");
                    setTimeout(function() {
                        document.getElementById("password").focus();
                    }, 0);
                    return false;
                }
                if (cnewPassword == "")
                {
                    alert("Please enter Confirm Password");
                    setTimeout(function() {
                        document.getElementById("cnewPassword").focus();
                    }, 0);
                    return false;
                }

                if (cnewPassword != password)
                {
                    alert("Password And Confirm Password Should be Same");
                    setTimeout(function() {
                        document.getElementById("cnewPassword").focus();
                        document.getElementById("cnewPassword").select();
                    }, 0);
                    return false;
                }
            }
        </script>

    </head>
    <body> 

        <p>

        <form method="post" action="userController" onsubmit="return Validate();">
            <table align="center">

                <tr>
                    <td>User Name</td>   
                    <td>:</td>
                    <td><input type='text' name='userName' id="userName"   ></td>

                </tr>
                <tr>
                    <td>New Password</td>   
                    <td>:</td>
                    <td><input type='password' name="password" id="password"   ></td>

                </tr>
                <tr>
                    <td>Confirm Password</td>   
                    <td>:</td>
                    <td><input type='password' name='cnewPassword' id="cnewPassword"   ></td>

                </tr>

                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>
                        <input type="submit"  value="Update"/>
                    </td>
                </tr>
            </table>
        </form>


    </body>
</html>
