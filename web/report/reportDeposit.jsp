<%-- 
    Document   : reportDeposit
    Created on : 29 Apr, 2016, 11:06:45 AM
    Author     : Arun
--%>

<%@page import="com.sun.Accounts.AccountsDetailsAction"%>
<%@page import="com.sun.deposit.DepositsMaster"%>
<%@page import="com.sun.deposit.DepositsAction"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map.Entry"%> 
<%@page import="com.sun.Accounts.CustomerMaster"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>



<html>
    <head>
        <s:if test="#session.userName == null">
            <script type="text/javascript">
                window.parent.window.location = "logout.action";
            </script>
        </s:if>
        <title> Finance Account</title>
        <link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">
    <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />   
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="script/jsDatePick_ltr.min.css" />  
        <script type="text/javascript" src="script/jquery.1.4.2.js"></script>
        <script type="text/javascript" src="script/jsDatePick.jquery.min.1.3.js"></script>
        <script type="text/javascript" src="script/Validations.js"></script>

        <script type="text/javascript">
                window.onload = function () {
                    new JsDatePick({
                        useMode: 2,
                        target: "startDate",
                        dateFormat: "%Y-%m-%d"
                    });
                    new JsDatePick({
                        useMode: 2,
                        target: "endDate",
                        dateFormat: "%Y-%m-%d"
                    });
                };
        </script>
        <style type="text/css">
             .menu{margin-right: 10px;}
            .menu a{ background:  #fcc661; color: #000; margin-right: 10px; padding: 5px 10px; text-decoration: none; float:left; }
            .menu a:hover{background:   #fcc661;color: #FFF;}
            .menu a.menu1{background:  #fcc661;color: #FFF;}
        </style>

    </head>


    <%

        DepositsAction acount = new DepositsAction();
        Map<Integer, DepositsMaster> report = null;
        String startDate = "0", endDate = "0", status = "";

        SimpleDateFormat dbFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -3);

        try {
            if (request.getParameter("startDate").equals(null) || request.getParameter("startDate").equals("")) {
                startDate = dbFormate.format(c.getTime());
            } else {
                startDate = request.getParameter("startDate");
            }
        } catch (Exception e) {
            startDate = dbFormate.format(c.getTime());
        }

        try {
            if (request.getParameter("status").equals(null) || request.getParameter("status").equals("")) {

            } else {
                status = request.getParameter("status");
            }
        } catch (Exception e) {

        }

        try {
            if (request.getParameter("endDate").equals(null) || request.getParameter("endDate").equals("")) {
                endDate = dbFormate.format(new Date());
            } else {
                endDate = request.getParameter("endDate");
            }
        } catch (Exception e) {
            endDate = dbFormate.format(new Date());
        }
        report = acount.DepositsReport(startDate, endDate, status);

        AccountsDetailsAction accountDetails = new AccountsDetailsAction();
        String totalCollectedAmountDaily = "0";
        String totalIssuedAmountDaily = "0";

        Map<String, String> issuedBonds = accountDetails.issuedBondsAmountDailyAndMonthly();
        Map<String, String> totalAmoundt = accountDetails.getTotalAmount("");
        totalCollectedAmountDaily = totalAmoundt.get("totalCollectedAmountDaily");
        totalIssuedAmountDaily = issuedBonds.get("totalIssuedAmountDaily");
    %>
    <body> 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  
        <br>   
        <div class="menu">  
            <p>    <a href="report.action"  > <strong>Issued Bonds Report</strong></a></p>
            <p> <a href="reportDailySheet.action"><strong>Daily Sheet Report</strong></a> </p>
            <p> <a href="reportDeposits.action" style="background: #0000FF;color: #FFF;"><strong>Deposit Report</strong></a> </p>
            <p> <a href="reportMonthlyInterest.action"><strong>Monthly Interest Report</strong></a> </p>
            <p> <a href="reportExtraTransaction.action"><strong>Extra Transaction Report</strong></a> </p>
            <!--                    <p> <a href="depositReport.action">Deposits Report</a> </p>-->
        </div> 
        <br>

        <div class="dispDiv"> 

            <div class="well">   
                <form    action="reportDeposits.action" method="post" onsubmit="return ValidateDate();">
                    <table>
                        <tr>

                            <td>Start Date:</td>
                            <td>
                                <input    type="text" name="startDate" value='<%=startDate%>' style="width:120px;height:30px;" placeholder="Enter Start Date" id="startDate"/>


                            </td>
                            <td>End Date :</td>
                            <td> 
                                <input     type="text" name="endDate" value='<%=endDate%>' style="width:120px;height:30px;" placeholder="Enter End Date" id="endDate"/>
                            </td>
                            <td>&nbsp;</td>

                            <td>Status :</td>
                            <td> 
                                <select name="status" id="status">
                                    <option></option>
                                    <option  >In</option>
                                    <option  >Out</option> 
                                </select> 
                            </td>
                            <td>&nbsp;</td>

                            <td><input type="submit" value="submit" class="submit-button-10" /></td>

                            <td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td > <b> Daily Collection  : </b> <font style="color: orange"> <b><%= totalCollectedAmountDaily != null ? totalCollectedAmountDaily : 0%> </b> </font> </td>
                            <td> &nbsp;&nbsp;&nbsp;&nbsp;</td> <td> <b> Daily  Issued Bonds Amount </b> : <font style="color: orange"><b> <%= totalIssuedAmountDaily != null ? totalIssuedAmountDaily : 0%></b></font></td>

                        </tr>
                    </table>
                </form>
            </div>
            <%
                double totalPaidAmount = 0, totalAmount = 0;
                for (Entry<Integer, DepositsMaster> entry : report.entrySet()) {
                    DepositsMaster customer = entry.getValue();
                    try {
                        totalPaidAmount += Double.parseDouble(customer.getPaidAmount().trim());
                    } catch (Exception e) {

                    }

                    totalAmount += Double.parseDouble(customer.getAmount().trim());
                }
            %>

            <br/>    
             
                 <div style="margin-left: 300px ;width: 949px;text-align: center;font-size: 18px" class="wrapper" > 
                    Total Amount :  <b><%=totalAmount%> </b> &nbsp;&nbsp;&nbsp;
                    Total Paid Amount :  <b><%=totalPaidAmount%> </b> &nbsp;&nbsp;&nbsp;

                </div> 
                
                    <div class="wrapper pB30" style="margin-left: 300px;width: 949px"> 
                        <div style="width: 949px;text-align: center" class="wrapper" ><h2 style="background:  #fcc661"  > Deposit Report</h2></div>
                        <div class="emiTable-wrap">

                            <div style="display: block;" class="dTable">   

                                <div class="dTable_replace">
                                    <table cellpadding="0" cellspacing="0">
                                        <tbody><tr class="headerTr">

                                                <td class="emi-serviceTaxHeader">A/C No</td>
                                                <td class="emi-serviceTaxHeader">Deposit Name</td>  
                                                <td class="emi-serviceTaxHeader">Mobile Number</td>
                                                <td class="emi-serviceTaxHeader">Amount</td>  
                                                <td class="emi-serviceTaxHeader">Paid Interest</td>
                                                <td class="emi-serviceTaxHeader">Open Date</td>  
                                                <td class="emi-serviceTaxHeader">Close Date</td>
                                                <td class="emi-serviceTaxHeader">Status</td>  

                                            </tr> 


                                            
                                            
                                             <%
                        if (report != null) {
                            for (Entry<Integer, DepositsMaster> entry : report.entrySet()) {
                                DepositsMaster customer = entry.getValue();
                    %> 
                    <tr style="font-size: 12px;font-family:  initial;"> 
                        <td class="emi-serviceTaxHeader"  style="text-align: left;"> <%=customer.getAccountNumber()%> </td>
                        <td class="emi-serviceTaxHeader" style="text-align: left;"><%=customer.getDepositName()%></td>
                        <td  class="emi-serviceTaxHeader" style="text-align: center;"><%=customer.getMobileNo()%></td>   
                        <td class="emi-serviceTaxHeader" style="text-align: right;"><%=customer.getAmount()%></td>  
                        <td class="emi-serviceTaxHeader" style="text-align: right;"><%=customer.getPaidAmount()%></td>   
                        <td  class="emi-serviceTaxHeader" style="text-align: center" ><%=customer.getOpenDate()%></td> 
                        <td class="emi-serviceTaxHeader" style="text-align: center"><%=customer.getCloseDate() != null ? customer.getCloseDate() : ""%></td>  
                        <td class="emi-serviceTaxHeader" style="text-align: center"><%=customer.getDepositType()%></td>  
                    </tr> 
                    <%}%> 
                    <%}%>  
                    
                      
                                        </tbody></table></div>
                                <div class="botButtion"> 
                                </div>
                            </div> 
                        </div> 
                    </div>
                                        
                                        
        </div>      

    </body>
</html>
