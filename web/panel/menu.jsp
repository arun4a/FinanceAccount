<%-- 
    Document   : header
    Created on : 2 Apr, 2016, 2:33:29 PM
    Author     : sys1089
--%>

<!DOCTYPE html> 
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%> 
<%@taglib prefix="s"  uri="/struts-tags"%>
<html>
    <head>  
    </head>
    <body>  
        <div id="top-part">
            <div id="header">
                <div class="space">

                    <!--Laxmi Ganapathi   Finance--> 
                </div>  
                <div class="tlinks">  
                    <div style="float: left; margin-top: 1px;">
                        <b>Hi  <s:property value="#session.userName" />  </b> 
                        <p style="margin-top: 3px;"> <a href="logout.action">LogOut</a></p>
                    </div>  
                </div>
            </div>
        </div>
        <div id="mainwrap">
            <div id="wrap"> 
                <ul id="nav">
                    <div style="width:1200px; margin:0px auto;">   
                        <li><a href="successAction.action" >DashBoard</a></li>
                        <!--                        <li><a href="successAction.action">User Accounts</a></li>-->
                        <li><a href="customerTab.action">Customer </a></li>
                        <li><a href="closingCustomerAction.action">Closing Accounts</a></li>  
                        <li><a href="report.action">Report</a></li>
                        <li><a href="displayDepositDetails.action">Deposits</a></li>
                        <li><a href="extraTransaction.action">Extra Transactions</a></li>  
                        <li><a href="mobileNumber.action">Mobile Number</a></li>  
                        <li><a href="monthlyReportDetails.action">Monthly Report</a></li>  
                    </div>
                </ul>  
            </div> 
        </div> 
    </body>
</html>

