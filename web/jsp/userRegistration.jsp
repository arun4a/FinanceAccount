<%-- 
    Document   : userRegistration
    Created on : 24 Mar, 2016, 4:43:11 PM
    Author     : Arun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %> 
<!DOCTYPE html>
<html>
    <head>
           
            <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />  
        <script src="script/Validations.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
               <title> Finance Account</title>
<link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">

    </head>
    <body> 
        <div id="top-part">
            <div id="header">
                <div class="space">
                    <!--                    Finance-->
                </div>

                <div class="tlinks">
                    <div style="float: left; margin-top: 1px;">
                                <b>Hi  </b>,
                                <p style="margin-top: 3px;"> </p>
                            </div>
                            <!--                    <a href="logout.action"> Logout </a>-->
                    </div>
                </div>
            </div>
            <div id="mainwrap">
                <div id="wrap"> 

                </div> 
            </div> 
        <div style=" text-align: center;height: 10px">
            <%
                if (request.getParameter("message") != null) {
            %>
            <h5 style="color:blue;"><%=request.getParameter("message")%></h5>
            <%}%>
        </div>
        <div style="alignment-adjust: central">
            
            <form action="userRegistration" name="userReg" method="post" onsubmit="return validateReg();">

                <table align="center" style="font-size: 18px;font-family:  initial;">
                    <thead>
                    <td style="alignment-adjust: central"><label style="alignment-adjust: central"> User Registration</label></td> 
                    </thead>
                    <tr>
                        <td  >First Name :</td>
                        <td > <input name="fName" type="text" id="fName"/>  </td>
                    </tr>

                    <tr>
                        <td> Sur Name :</td>
                        <td>  <input name="sName" type="text" id="sName"/> </td>
                    </tr>
                    <tr>
                        <td> Father Name :</td>
                        <td>  <input name="fatherName" type="text" id="fatherName"/> </td>
                    </tr>
                    <tr>
                        <td>User Name :</td>
                        <td>   <input name="userName" type="text" id="userName"/>   </td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td>  <input name="password" type="password" id="password"/>       </td>
                    </tr>
                    <tr>
                        <td> Confirm Password :</td>
                        <td>   <input name="cpassword" type="password" id="cpassword"/>       </td>
                    </tr>

                    <tr>
                        <td>Phone Number :</td>
                        <td>    <input name="phoneNo" type="text" id="phoneNo"/>     </td>
                    </tr>

                    <tr>
                        <td>Enter address :</td>
                        <td> <textarea name="address" id="address"></textarea> </td>
                    </tr>

                    <tr>
                        <td> <input type="reset" value="reset"/> </td>
                        <td> <input type="submit" value="Add"/>  </td>
                    </tr>
                    <tr>
                        <td> <a href="failAction.action">Click To Login</a> </td>
                        
                    </tr>

                </table>



            </form>
        </div>
    </body>
</html>
