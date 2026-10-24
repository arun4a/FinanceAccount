<%-- 
    Document   : closingCustomers
    Created on : 16 Apr, 2016, 3:55:00 PM
    Author     : sys1089
--%>


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
        <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />   
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <script type="text/javascript" src="script/Validations.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Finance Account</title>
        <link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">
    </head>


    <%

        int totNoRecords = 0;
        AccountsDetailsAction acount = new AccountsDetailsAction();
        Map<Integer, CustomerMaster> customerDetails = null;
        int pageNo = 1;
        int totNoOfPages = 0;

        int limit = 15;
        String customerNameSearch = "0", accountNumber = "0", mobileNumber = "0";
        try {
            if (request.getParameter("customerNameSearch").equals(null) || request.getParameter("customerNameSearch").equals("")) {
            } else {
                customerNameSearch = request.getParameter("customerNameSearch");
            }
        } catch (Exception e) {
            customerNameSearch = "0";

        }

        try {
            if (request.getParameter("accountNumber").equals(null) || request.getParameter("accountNumber").equals("")) {
            } else {
                accountNumber = request.getParameter("accountNumber");
            }
        } catch (Exception e) {
            accountNumber = "0";
        }

        try {
            if (request.getParameter("mobileNumber").equals(null) || request.getParameter("mobileNumber").equals("")) {
            } else {
                mobileNumber = request.getParameter("mobileNumber");
            }
        } catch (Exception e) {
            mobileNumber = "0";
        }

        customerDetails = acount.customerDetails(customerNameSearch, accountNumber, "close", mobileNumber);

        if (customerNameSearch.equals("0")) {
            customerNameSearch = "";
        }
        if (accountNumber.equals("0")) {
            accountNumber = "";
        }

        if (mobileNumber.equals("0")) {
            mobileNumber = "";
        }

        AccountsDetailsAction accountDetails = new AccountsDetailsAction();
        String totalCollectedAmountDaily = "0";
        String totalIssuedAmountDaily = "0";
        Map<String, String> issuedBonds = accountDetails.issuedBondsAmountDailyAndMonthly();
        Map<String, String> totalAmount = accountDetails.getTotalAmount("");
        totalCollectedAmountDaily = totalAmount.get("totalCollectedAmountDaily");
        totalIssuedAmountDaily = issuedBonds.get("totalIssuedAmountDaily");
    %>
    <body> 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  
        <br><br>
        <div class="dispDiv"> 

            <div class="well">   
                <form    action="closingCustomerAction.action" method="post" onsubmit="return ValidateCustomerEntry();">
                    <table>
                        <tr>

                            <td>Customer Name:</td>
                            <td>
                                <input    type="text" name="customerNameSearch" value='<%=customerNameSearch%>' style="width:120px;height:30px;" placeholder="Enter Customer Name" id="customerNameSearch"/>


                            </td>
                            <td>Account Number :</td>
                            <td> 
                                <input     type="text" name="accountNumber" value='<%=accountNumber%>' style="width:120px;height:30px;" placeholder="Enter Account Number" id="accountNumber"/>

                            </td>
                            <td>Mobile Number:</td>
                            <td> 
                                <input     type="text" name="mobileNumber" value='<%=mobileNumber%>' style="width:120px;height:30px;" placeholder="Enter Mobile Number" id="mobileNumber"/>

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
            <div style=" text-align: center;height: 5px">
                <s:if test="%{#request.message != null}">
                    <script>
                        alert("${message}");
                    </script> 
                </s:if>    </div> 
            <br>


         
            <div class="wrapper pB30" style="margin-left: 100px;width: 1200px;"> 
                   <div style="width: 1220px;text-align: center" class="wrapper" ><h2 style="background:  #fcc661"  >Closing Customers</h2></div>
                <div class="emiTable-wrap">


                    <div style="display: block;" class="dTable">   

                        <div class="dTable_replace">
                            <table cellpadding="0" cellspacing="0">
                                <tbody>
                                    <tr class="headerTr"> 

                                        <td class="emi-serviceTaxHeader">A/c No</td>
                                        <td class="emi-serviceTaxHeader">Customer Name</td>
                                        <td class="emi-serviceTaxHeader">Surety Name</td>

                                        <td class="emi-serviceTaxHeader">Date Of Issue</td>
                                        <td class="emi-serviceTaxHeader">Date Of Close</td>
                                        <td  class="emi-serviceTaxHeader">Amount</td> 

                                        <td class="emi-serviceTaxHeader">Original Amount</td>

                                        <td class="emi-serviceTaxHeader">Paid amount</td>
                                        <td class="emi-serviceTaxHeader">Due Amount</td>
                                        <td class="emi-serviceTaxHeader">Total Installments</td>

                                        <td class="emi-serviceTaxHeader">Completed Installments</td>
                                        <td class="emi-serviceTaxHeader">Status</td>
                                    </tr> 

                                    <%
                                        if (customerDetails != null) {
                                            double dueAmount = 0;
                                            String monthName = "0";
                                            for (Entry<Integer, CustomerMaster> entry : customerDetails.entrySet()) {
                                                CustomerMaster customer = entry.getValue();

                                                try {
                                                    dueAmount = Double.parseDouble(customer.getAmount()) - customer.getPaidAmount();
                                                } catch (Exception e) {

                                                }


                                    %> 


                                    <% if (monthName.equals("0")) {


                                    %>
                                    <tr style="font-size: 15px ;font-family:  inherit; color: blue"> 
                                        <td colspan="12"><b><%=customer.getClosingMonthName()%></b></td>
                                    </tr> <%}%>


                                    <% if (!monthName.equals("0") && !monthName.equals(customer.getClosingMonthName())) {


                                    %>
                                    <tr style="font-size: 15px ;font-family:  inherit;color: blue"> 
                                        <td colspan="12"><b><%=customer.getClosingMonthName()%></b></td>
                                    </tr> <%}%>

                                    <tr style=" font-size: 12px;font-family:  initial;color: red"> 
                                        <td class="emi-serviceTaxHeader" style="text-align: left;"><a href="customerDetailsUpdateClose.action?customerId=<%=customer.getCustomerId()%>"><%=customer.getAccountNumberNew()%></a></td>
                                        <td  class="emi-serviceTaxHeader" style="text-align: left;"><%=customer.getCustomerName()%> </td> 
                                        <td class="emi-serviceTaxHeader" style="text-align: left;"><%=customer.getSuretyName()%></td>   
                                        <td class="emi-serviceTaxHeader" style="text-align: center;"><%=customer.getDateOfIssue()%></td>
                                        <td class="emi-serviceTaxHeader" style="text-align: center;"><%=customer.getClosingDate() != null ? customer.getClosingDate() : ""%></td>
                                        <td class="emi-serviceTaxHeader" style="text-align: right" ><%=customer.getAmount()%></td> 
                                        <td class="emi-serviceTaxHeader" style="text-align: right"><%=customer.getOriginalAmount()%></td> 
                                        <td  class="emi-serviceTaxHeader" style="text-align: right"><%=customer.getPaidAmount()%></td>  
                                        <td class="emi-serviceTaxHeader" style="text-align: right"><%=dueAmount%></td>   
                                        <td class="emi-serviceTaxHeader" style="text-align: center"><a href="installDetailsClose.action?accountNumberNew=<%=customer.getAccountNumberNew()%>"><%=customer.getInstallments()%></a></td>  
                                        <td class="emi-serviceTaxHeader" style="text-align: center"><%=customer.getCompletedInstallments()%></td>   
                                        <td class="emi-serviceTaxHeader" style="text-align: center" ><%=customer.getStatus()%></td>   
                                    </tr> 
                                    <%
                                            monthName = customer.getClosingMonthName();
                                        }%> 
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
