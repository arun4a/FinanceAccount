<%-- 
    Document   : totalCollectionMonthly
    Created on : May 20, 2016, 4:45:54 PM
    Author     : Arun
--%>

<%@page import="com.sun.extra.ExtraMaster"%>
<%@page import="com.sun.extra.ExtraTransAction"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.sun.Accounts.AccountsDetailsAction"%>
<%@page import="com.sun.Accounts.CustomerMaster"%>
<%@page import="java.util.Map"%>
<%@taglib prefix="s"  uri="/struts-tags"%>
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
                };

                function getReport() {
                    document.month.action = "exportMonthlydetails.action";
                    document.month.submit();
                }
        </script>
        <style type="text/css">
            .menu{margin-right: 10px;}
            .menu a{ background: #88C4E2; color: #000; margin-right: 10px; padding: 5px 10px; text-decoration: none; float:left; }
            .menu a:hover{background: #0000FF;color: #FFF;}
            .menu a.menu1{background: #0000FF;color: #FFF;}
        </style>

    </head>


    <%

        ExtraTransAction acount = new ExtraTransAction();
        Map<String, String> report = null;

        String startDate = "0";

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

        report = acount.monthlyReportDetails(startDate);


    %>
    <body> 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  
        <br> 
        <div>

            <div>
                <div class="dispDiv"> 

                    <div class="well">   
                        <form    action="monthlyReportDetails.action" method="post" name="month" onsubmit="return ValidateDate();">
                            <table>
                                <tr> 
                                    <td> Date:</td>
                                    <td>
                                        <input    type="text" name="startDate" value='<%=startDate%>' style="width:120px;height:30px;" placeholder="Enter Start Date" id="startDate"/>
                                    </td>

                                    <td><input type="submit" value="submit" class="submit-button-10" /></td>
                                    <td> </td>
                                    <td> </td>
                                    <td><input type="button" value="Export Monthly Data" onclick="getReport()" class="submit-button-10" /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <%
                        double LasMonthNetAmount = 0, amountTotal = 0, netAmountTotal = 0, TotalCollections = 0, TotalDepositsInAmount = 0, MonthlyInAmountTotal = 0, TotalBondsAmount = 0, ExtraAmount = 0, InterestAmount = 0, TotalDepositsOutAmount = 0;
                        LasMonthNetAmount = Double.parseDouble(report.get("LasMonthNetAmount"));

                        try {
                            TotalCollections = Double.parseDouble(report.get("TotalCollections"));
                        } catch (Exception e) {

                        }

                        try {
                            TotalDepositsInAmount = Double.parseDouble(report.get("TotalDepositsInAmount"));
                        } catch (Exception e) {

                        }
                        MonthlyInAmountTotal = Double.parseDouble(report.get("MonthlyInAmountTotal"));

                        TotalBondsAmount = Double.parseDouble(report.get("TotalBondsAmount"));

                        netAmountTotal = (TotalCollections + LasMonthNetAmount) + TotalDepositsInAmount - TotalBondsAmount;

                        ExtraAmount = Double.parseDouble(report.get("ExtraAmount"));

                        InterestAmount = Double.parseDouble(report.get("InterestAmount"));

                        amountTotal = netAmountTotal - InterestAmount - ExtraAmount;

                        TotalDepositsOutAmount = Double.parseDouble(report.get("TotalDepositsOutAmount"));

                    %>

                    <br/>    


                    <center>
                        <div class="widgetHeader" style="width: 600px">
                            <center>  <h2 style="background:  #fcc661">Monthly Report Details  </h2></center>

                        </div> 
                        <br>
                        <div>

                            <p style="font-size: 20px;font-family:  serif;">   

                                Last Month Net Amount  :&nbsp;&nbsp;<b><%=LasMonthNetAmount%></b><br><br>

                                Total DailySheet Collections  :&nbsp;&nbsp;<b><%=TotalCollections%></b><br><br>

                                Total   Collections  :&nbsp;&nbsp;<b><%=TotalCollections + LasMonthNetAmount%> </b><br><br>


                                Total Deposit In Amount :&nbsp;&nbsp;<b><%=TotalDepositsInAmount%></b><br><br>
                                Total Collections :&nbsp;&nbsp;<b><%=MonthlyInAmountTotal%></b><br><br>

                                Total Issued Bonds Amount :&nbsp;&nbsp;<b><%=TotalBondsAmount%></b><br><br>

                                Total Amount   :&nbsp;&nbsp;<b><%=netAmountTotal%></b><br><br>

                                Total Interest Amount   :&nbsp;&nbsp;<b><%=InterestAmount%></b><br><br>
                                Total Extra Amount   :&nbsp;&nbsp;<b><%=ExtraAmount%></b><br><br> 
                                Total   Amount   :&nbsp;&nbsp;<b><%=amountTotal%></b><br><br>

                                Total   Deposit Out Amount   :&nbsp;&nbsp;<b><%=TotalDepositsOutAmount%></b><br><br>


                                Monthly   Net Amount   :&nbsp;&nbsp;<b><%=(amountTotal - TotalDepositsOutAmount)%></b><br><br> 
                        </div>
                        <div class="widgetFooter" style="width: 600px"></div> </center>


                </div>     
            </div>
        </div>

    </body>
</html>

