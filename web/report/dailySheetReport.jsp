<%-- 
    Document   : dailySheetReport
    Created on : 19 Apr, 2016, 3:14:10 PM
    Author     : Arun
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sun.Accounts.InstallmentDetails"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.sun.Accounts.AccountsDetailsAction"%>
<%@page import="com.sun.Accounts.CustomerMaster"%>
<%@page import="java.util.Map"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                window.onload = function() {
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

                    new JsDatePick({
                        useMode: 2,
                        target: "dateId",
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
        SimpleDateFormat dbFormate = new SimpleDateFormat("yyyy-MM-dd");
        AccountsDetailsAction acount = new AccountsDetailsAction();
        List< InstallmentDetails> report = null;
        String startDate = "0", endDate = "0";

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
            if (request.getParameter("endDate").equals(null) || request.getParameter("endDate").equals("")) {
                endDate = dbFormate.format(new Date());
            } else {
                endDate = request.getParameter("endDate");
            }
        } catch (Exception e) {
            endDate = dbFormate.format(new Date());
        }

        report = acount.reportsByDates(startDate, endDate);

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
            <p> <a href="reportDailySheet.action" style="background: #0000FF;color: #FFF;"><strong>Daily Sheet Report</strong></a> </p>
            <p> <a href="reportDeposits.action" ><strong>Deposit Report</strong></a> </p>
            <p> <a href="reportMonthlyInterest.action"><strong>Monthly Interest Report</strong></a> </p>
            <p> <a href="reportExtraTransaction.action"><strong>Extra Transaction Report</strong></a> </p>

        </div>  
        <br>
        <div>

            <div>
                <div class="dispDiv"> 

                    <div class="well">   
                        <form    action="reportDailySheet.action" method="post" onsubmit="return ValidateDate();">
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

                                    <td><input type="submit" value="submit"  class="submit-button-10"/></td>

                                    <td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td > <b> Daily Collection  : </b> <font style="color: orange"> <b><%= totalCollectedAmountDaily != null ? totalCollectedAmountDaily : 0%> </b> </font> </td>
                                    <td> &nbsp;&nbsp;&nbsp;&nbsp;</td> <td> <b> Daily  Issued Bonds Amount </b> : <font style="color: orange"><b> <%= totalIssuedAmountDaily != null ? totalIssuedAmountDaily : 0%></b></font></td>

                                </tr>
                            </table>
                        </form>
                    </div>
                    <%
                        double totalAmount = 0, collectedAmount = 0, dueAmount = 0;
                        for (int i = 0; i < report.size(); i++) {
                            totalAmount += Double.parseDouble(report.get(i).getAcutalAmount());

                            try {
                                collectedAmount += Double.parseDouble(report.get(i).getPaidAmount());
                            } catch (Exception e) {

                            }

                        }
                        dueAmount = totalAmount - collectedAmount;
                    %> 

                    <center><div style="width: 1500px">
                            <div style="float: left; width: 800px; "> 


                                <p style="margin-left: 50px ;width: 800px;text-align: center;font-size :18px" class="wrapper">       Total Amount :  <b><%=totalAmount%> </b> &nbsp;&nbsp;&nbsp;
                                    Total Collected Amount :  <b><%=collectedAmount%> </b> &nbsp;&nbsp;&nbsp;
                                    Total Due Amount :  <b><%=dueAmount%> </b> &nbsp;&nbsp;&nbsp; </p>
                                
                                <div class="wrapper pB30" style="margin-left: 50px ;width: 800px"> 
                                    <div style="width: 800px;text-align: center" class="wrapper" ><h2 style="background:  #fcc661"  > Daily Sheet Report </h2>

                                </div>
                                    <div class="emiTable-wrap">

                                        <div style="display: block;" class="dTable">   

                                            <div class="dTable_replace">
                                                <table cellpadding="0" cellspacing="0">
                                                    <tr class="headerTr">  
                                                        <td class="emi-serviceTaxHeader"> A/C No</td>
                                                        <td class="emi-serviceTaxHeader">Customer Name</td> 
                                                        <td class="emi-serviceTaxHeader">  Deposit By Partner</td>  
                                                        <td class="emi-serviceTaxHeader">Deposited Name</td> 
                                                        <td class="emi-serviceTaxHeader">Collected Date</td>    
                                                        <td class="emi-serviceTaxHeader">Amount</td>   
                                                        <td class="emi-serviceTaxHeader">Paid Amount</td>   
                                                        <td class="emi-serviceTaxHeader">Due Amount</td>    
                                                    </tr> 




                                                    <%
                                                        if (report != null) {
                                                            for (int i = 0; i < report.size(); i++) {
                                                    %> 
                                                    <tr style="font-size: 12px;font-family:  initial;" > 
                                                        <td class="emi-serviceTaxHeader" ><a href="getDailySheetDetailsInReport.action?accountId=<%=report.get(i).getAccountId()%>&accountNumber=<%=report.get(i).getAccountNumber()%>&startDate=<%=startDate%>&endDate=<%=endDate%>"> <%=report.get(i).getAccountNumber()%></a> </td>
                                                        <td class="emi-serviceTaxHeader" style="text-align: left"><%=report.get(i).getCustomerName()%></td>
                                                        <td class="emi-serviceTaxHeader"  style="text-align: center;"><%=report.get(i).getDepositByPartner()%></td>    
                                                        <td class="emi-serviceTaxHeader" ><%=report.get(i).getDepositName()%></td>   
                                                        <td class="emi-serviceTaxHeader" ><%=report.get(i).getDate()%></td>  
                                                        <td  class="emi-serviceTaxHeader"  style="text-align: right" ><%=report.get(i).getAcutalAmount()%></td>  
                                                        <td class="emi-serviceTaxHeader"   style="text-align: right" ><%=report.get(i).getPaidAmount()%></td>  
                                                        <td class="emi-serviceTaxHeader"   style="text-align: right" ><%=report.get(i).getDueAmount()%></td>  
                                                    </tr> 
                                                    <%}%> 
                                                    <%}%>    
                                                </table></div>
                                            <div class="botButtion"> 
                                            </div>
                                        </div> 
                                    </div> 
                                </div>






                            </div>     
                            <div style="float: left; width: 100px;height: 700px;   ">

                            </div>
                            <s:iterator value="%{#request.installDispaly}" var="map">  <div style="float: left; width: 500px;height: 780px;   border:solid 1px;">
                                    <div class="widgetHeader">
                                        <center><h2 style="background:  #fcc661;width: 476px;">Update Daily Sheet Details </h2></center> 
                                    </div> 
                                    <center>    
                                        <form  name="daily" method="post" > 
                                            <table  style="alignment-adjust: central;font-size: 18px;font-family:  initial;"> 
                                                <tr><td>&nbsp; </td>  </tr>
                                                <tr>
                                                    <td>Date : </td>
                                                    <td> 
                                                        <input    type="hidden" name="startDate" value="<%=startDate%>" id="startDate"/>
                                                        <input    type="hidden" name="endDate" value="<%=endDate%>" id="endDate"/>

                                                        <input    type="hidden" name="accountId" value="<s:property value='accountId'></s:property>" id="accountId"/>
                                                        <input  readonly="readonly" type="text" name="date" id="dateId"  value="<s:property value='date'></s:property>" /> </td>
                                                    </tr>

                                                    <tr><td> &nbsp;</td>  </tr>
                                                    <tr>
                                                        <td>Deposit Amount : </td>
                                                        <td><input type="text" name="depositAmount" id="depositAmount"  value="<s:property value='depositAmount'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td> &nbsp;</td>  </tr>
                                                    <tr>
                                                        <td>Deposit Name : </td>
                                                        <td><input type="text" name="depositName" id="depositName"  value="<s:property value='depositName'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td> &nbsp;</td>  </tr>
                                                    <tr>
                                                        <td>Deposit By  : </td>
                                                        <td><input type="text" name="depositByPartner" id="depositByPartner"  value="<s:property value='depositByPartner'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td>&nbsp; </td>  </tr>

                                                    <tr>
                                                        <td>Account Number  : </td>
                                                        <td><input type="text" name="accountNumber" id="accountNumber"  value="<s:property value='accountNumber'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td> &nbsp;</td> </tr>
                                                    <tr>
                                                        <td>Customer Name  : </td>
                                                        <td><input type="text" name="customerName" id="customerName"  value="<s:property value='customerName'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td> &nbsp;</td>  </tr>
                                                    <tr>
                                                        <td>Actual Amount : </td>
                                                        <td><input type="text" name="actualAmount" id="actualAmount"  value="<s:property value='acutalAmount'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td> &nbsp;</td>  </tr>

                                                    <tr>
                                                        <td>Paid Amount : </td>
                                                        <td><input type="text" name="paidAmount" id="paidAmount"  value="<s:property value='paidAmount'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td> &nbsp;</td>  </tr>

                                                    <tr>
                                                        <td>Installment : </td>
                                                        <td><input type="text" name="installment" id="installment"  value="<s:property value='installment'></s:property>"/> </td>
                                                    </tr>
                                                    <tr><td>&nbsp; </td>  </tr>
                                                    <tr>
                                                        <td>Close : </td>
                                                        <td>

                                                            <select name="close" id="close">  

                                                                <option <s:if test="%{#status =='close'}">selected="selected"</s:if> >No</option>
                                                            <option  <s:if test="%{#status =='open'}">selected="selected"</s:if>  >Yes</option>
                                                            </select> </td>
                                                    </tr>
                                                    <tr><td>&nbsp; </td> </tr>
                                                    <tr>

                                                        <td colspan="2" style="text-align: center"><input type="submit" name="Delete" value="Delete" class="submit-button-10" onclick="validateDailySheetu('Delete')"  /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="Update"  value="Update" class="submit-button-10" onclick="validateDailySheetu('Update')" /> </td>

                                                    </tr>
                                                </table>    
                                            </form></center>
                                    </div> </s:iterator> 
                            </div>
                        <s:if test="%{#request.message != null}">
                            <script>
                                alert("${message}");
                            </script> 
                        </s:if>    
                    </center>
                </div>
            </div>
        </div>

        <script>
            function  validateDailySheetu(value) {
                var dateId = document.getElementById("dateId").value;
                var depositAmount = document.getElementById("depositAmount").value;
                var accountNumber = document.getElementById("accountNumber").value;
                var customerName = document.getElementById("customerName").value;
                var installment = document.getElementById("installment").value;
                if (dateId == null || dateId == "") {
                    alert("Please Select Date ");
                    setTimeout(function() {
                        document.getElementById("dateId").focus();
                    }, 0);
                    return false;
                }
//  
                if (depositAmount == null || depositAmount == "") {
                    alert("Please enter Deposit Amount ");
                    setTimeout(function() {
                        document.getElementById("depositAmount").focus();
                    }, 0);
                    return false;
                }

                if (isNaN(depositAmount)) {
                    alert("Please enter numbers only  ");
                    setTimeout(function() {
                        document.getElementById("depositAmount").focus();
                        document.getElementById("depositAmount").select();
                    }, 0);
                    return false;
                }

                if (accountNumber == null || accountNumber == "") {
                    alert("Please enter Account Number ");
                    setTimeout(function() {
                        document.getElementById("accountNumber").focus();
                    }, 0);
                    return false;
                }


                if (customerName == null || customerName == "") {
                    alert("Please enter Customer Name ");
                    setTimeout(function() {
                        document.getElementById("customerName").focus();
                    }, 0);
                    return false;
                }

                if (installment == null || installment == "") {
                    alert("Please enter installment ");
                    setTimeout(function() {
                        document.getElementById("installment").focus();
                    }, 0);
                    return false;
                }
                if (isNaN(installment)) {
                    alert("Please enter numbers only  ");
                    setTimeout(function() {
                        document.getElementById("installment").focus();
                        document.getElementById("installment").select();
                    }, 0);
                    return false;
                }
                if (value == "Delete") {
                    document.daily.action = "deleteInstallDetailsInReports.action";
                    document.customerForm.submit();
                } else {
                    document.daily.action = "updateInstallDetailsInReports.action";
                    document.customerForm.submit();
                }


            }
        </script>
    </body>
</html>

