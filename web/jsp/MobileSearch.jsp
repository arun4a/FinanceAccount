<%-- 
    Document   : MobileSearch
    Created on : 2 May, 2016, 9:27:16 AM
    Author     : Arun
--%>

<%@page import="com.sun.mobile.MobileMaster"%>
<%@page import="com.sun.mobile.MobileAction"%>
<%@page import="java.util.Map.Entry"%>  
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
        <link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">
        <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />   
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <script type="text/javascript" src="script/Validations.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Finance Account</title>
        <link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">
    </head>


    <%

        MobileAction acount = new MobileAction();
        List<MobileMaster> customerDetails = new LinkedList<MobileMaster>();
        String customerNameSearch = "0", accountNumber = "0";
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
        if (customerNameSearch.equals("0") && accountNumber.equals("0")) {

        } else {
            customerDetails = acount.MobileNumberDetails(customerNameSearch, accountNumber);
        }

        if (customerNameSearch.equals("0")) {
            customerNameSearch = "";
        }
        if (accountNumber.equals("0")) {
            accountNumber = "";
        }


    %>
    <body> 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  
        <div>
            <br/>   
            <div>
                <div class="dispDiv"> 

                    <div class="well">   
                        <form    action="mobileNumber.action" method="post" onsubmit="return ValidateMobile();">
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
                                    <td>&nbsp;</td>
                                    <td><input type="submit" value="submit" class="submit-button-10" /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                                       <s:if test="%{#request.message != null}">
                                            <script>
                                                alert("${message}");
                                            </script> 
                                        </s:if>  
                    <%
                        String custId = "";
                        if (customerDetails.size() > 0) {
                            for (int i = 0; i < customerDetails.size(); i++) {
                                custId = customerDetails.get(i).getCustId();
                            }
                        }
                    %> 
                    <div class="well">   
                        <form    action="mobileNumberAdd.action" method="post" onsubmit="return ValidateMobile();">
                            <table>
                                <tr> 
                                    <td>Customer Second Mobile Number:</td>
                                    <td>
                                        <input    type="hidden" name="customerNameSearch" value='<%=customerNameSearch%>'    id="customerNameSearch"/>
                                        <input     type="hidden" name="accountNumber" value='<%=accountNumber%>'   id="accountNumber"/>
                                        <input    type="hidden" name="customerMobId" value='<%=custId%>'    id="customerMobId"/>
                                        <input    type="text" name="customer2mob"  style="width:120px;height:20px;"  id="customer2mob"/>
                                    </td>
                                    <td>Surety Second Mobile Number:</td>
                                    <td> 
                                        <input     type="text" name="surety2mob"  style="width:120px;height:20px;"   id="surety2mob"/>
                                    </td> 
                                    <td>&nbsp;</td>
                                    <td><input type="submit" value="Add" class="submit-button-10" /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
 
                    <div style="margin-left: 300px ;width: 949px;text-align: center" class="wrapper" ><h2 style="background:  #fcc661"  >  Mobile Number Details </h2></div>
                    <div class="wrapper pB30" style="margin-left: 300px"> 
                        <div class="emiTable-wrap">

                            <div style="display: block;" class="dTable">   

                                <div class="dTable_replace">
                                    <table cellpadding="0" cellspacing="0">
                                        <tbody><tr class="headerTr">

                                                <td class="emi-serviceTaxHeader">Customer   Mobile Number </td>
                                                <td class="emi-serviceTaxHeader">Surety  Mobile Number</td> 

                                            </tr> 


                                            <%
                                                if (customerDetails.size() > 0) {
                                                    for (int i = 0; i < customerDetails.size(); i++) {

                                            %> 
                                            <tr   style="font-size: 13px;font-family:  initial;"> 
                                                <td class="emi-serviceTaxHeader" style="text-align: center;"> <%=customerDetails.get(i).getCustMobileNo()%> </td>
                                                <td class="emi-serviceTaxHeader" style="text-align: center;"><%=customerDetails.get(i).getSuretyMobileNo()%></td>   
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
            </div></div> 

    </body>
</html>
