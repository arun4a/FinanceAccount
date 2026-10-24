<%-- 
    Document   : ExtraTransactions
    Created on : 29 Apr, 2016, 3:30:29 PM
    Author     : Arun
--%>


<%@page import="com.sun.Accounts.AccountsDetailsAction"%>
<%@ taglib uri="/struts-tags" prefix="s"%> 
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%> 
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" media="all" href="script/jsDatePick_ltr.min.css" />  
        <script type="text/javascript" src="script/jquery.1.4.2.js"></script>
        <script type="text/javascript" src="script/jsDatePick.jquery.min.1.3.js"></script>
        <script type="text/javascript" src="script/Validations.js"></script>

        <script type="text/javascript">
                window.onload = function() {
                    new JsDatePick({
                        useMode: 2,
                        target: "extraDateSave",
                        dateFormat: "%Y-%m-%d"
                    });

                    new JsDatePick({
                        useMode: 2,
                        target: "extraSearchDate",
                        dateFormat: "%Y-%m-%d"
                    });

                };



        </script>


        <%
            AccountsDetailsAction accountDetails = new AccountsDetailsAction();
            String totalCollectedAmountDaily = "0";
            String totalIssuedAmountDaily = "0";

            Map<String, String> issuedBonds = accountDetails.issuedBondsAmountDailyAndMonthly();
            Map<String, String> totalAmoundt = accountDetails.getTotalAmount("");
            totalCollectedAmountDaily = totalAmoundt.get("totalCollectedAmountDaily");
            totalIssuedAmountDaily = issuedBonds.get("totalIssuedAmountDaily");
        %>

    </head>
    <body> 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  
        <div class="mainDiv">
            <div class="well"  style="height:300px">
                <div style="float: left; width: 500px;  border:solid 1px; text-align: center"> 
                    <div class="widgetHeader"><center><h2 style="background:  #fcc661;width: 470px" >Extra Transaction Details</h2></center></div>    
                    <br>
                    <center>
                        <form name="extraDetails"  method="POST" autocomplete="off">
                            <table >
                                <tr>
                                    <td> 
                                        <label class="description" for="element_1">Purpose </label>
                                    </td>
                                    <td>
                                        :
                                    </td>
                                    <td>
                                        <textarea name="extraPurposeSave" id="extraPurposeSave"><%=(request.getParameter("extraPurpose") != null ? request.getParameter("extraPurpose") : "")%></textarea>

                                        <input type="hidden"  name="extraIdSave" id="extraIdSave" value="<%=(request.getParameter("extraId") != null ? request.getParameter("extraId") : "")%>"  />

                                    </td>
                                    <td>
                                        <b id="altcamp_id" class="alertMsg" style="display: block;"></b>
                                    </td> 
                                </tr>

                                <tr>
                                    <td>
                                        <label class="description" for="element_1">Amount</label>
                                    </td>
                                    <td>:</td>
                                    <td>
                                        <input type="text"  name="extraAmountSave" id="extraAmountSave" value="<%=(request.getParameter("extraAmount") != null ? request.getParameter("extraAmount") : "")%>"  />
                                    </td>
                                    <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td>

                                </tr>
                                <tr>
                                    <td>
                                        <label class="description" for="element_1"> Date</label>
                                    </td>
                                    <td>:</td>
                                    <td>
                                        <input type="text"  name="extraDateSave" id="extraDateSave" value="<%=(request.getParameter("openDate") != null ? request.getParameter("openDate") : "")%>"  />
                                    </td>
                                    <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td>

                                </tr>


                                <tr >
                                    <%if (request.getParameter("extraId") != null) {%>
                                    <td colspan="1" style="text-align:center"><input type="submit" value="Delete" id="typeDelete" onclick="return ExtraDeleteDeposits();"  class="submit-button-10"/></td>
                                    <td colspan="2" style="text-align:center"><input type="submit" value="Update" id="type" onclick="return ExtraUpdateDeposits();"  class="submit-button-10"/></td>
                                        <%} else {%>
                                    <td colspan="4" style="text-align:center"><input type="submit" value="Submit" id="type" onclick="return ExtraSaveDeposits();"  class="submit-button-10"/></td>
                                        <%}%>

                                </tr>
                                <tr>
                                    <td colspan="3" style="text-align:center"> 
                                        <s:if test="%{#request.res_msg != null}">
                                            <script>
                                                alert("${res_msg}");
                                            </script> 
                                        </s:if> 


                                    </td>
                                </tr>
                            </table>
                        </form></center>
                </div>

            </div>

            <div class="well">   
                <form    action="searchExtraDetails.action" method="post" onsubmit="return ValidateSearchDeposits();">
                    <table>
                        <tr> 
                            <td>Date : </td>
                            <td>
                                <input    type="text" name="extraSearchDate"  style="width:120px;height:30px;"  id="extraSearchDate"/>
                            </td> 
                            <td>&nbsp;</td>
                            <td><input type="submit" value="Search"   class="submit-button-10"/></td>

                            <td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td > <b> Daily Collection  : </b> <font style="color: orange"> <b><%= totalCollectedAmountDaily != null ? totalCollectedAmountDaily : 0%> </b> </font> </td>
                            <td> &nbsp;&nbsp;&nbsp;&nbsp;</td> <td> <b> Daily  Issued Bonds Amount </b> : <font style="color: orange"><b> <%= totalIssuedAmountDaily != null ? totalIssuedAmountDaily : 0%></b></font></td>



                        </tr>
                    </table>
                </form>
            </div> 


            
            <div class="wrapper pB30" style="margin-left: 300px;width: 949px;"> 
                <div style="width: 951px;text-align: center" class="wrapper" ><h2 style="background:  #fcc661"  >  Extra Transaction Details</h2></div>
                <div class="emiTable-wrap">

                    <div style="display: block;" class="dTable">   

                        <div class="dTable_replace">
                            <table cellpadding="0" cellspacing="0">
                                <tbody><tr class="headerTr">

                                        <td class="emi-serviceTaxHeader">Date </td>
                                        <td class="emi-serviceTaxHeader">Amount</td>
                                        <td class="emi-serviceTaxHeader">Purpose</td>

                                    </tr> 



                                    <s:iterator value="%{#request.displayExtraTransaction }" var="v">
                                        <tr style="font-size: 12px;font-family:  initial;">    			
                                            <!--deposit_id,deposit_name,amount,interest,mobile_number,created_date,time-->
                                            <td  class="emi-serviceTaxHeader"><a href="displayExtraDetails.action?openDate=<s:property value='extraDate'></s:property>&extraId=<s:property value='extraId'></s:property>&extraAmount=<s:property value='extraAmount'></s:property>&extraPurpose=<s:property value='extraPurpose'></s:property>"  ><s:property value="extraDate"></s:property></a></td> 
                                            <td  class="emi-serviceTaxHeader"><s:property value='extraAmount'></s:property></td> 
                                            <td  class="emi-serviceTaxHeader"><s:property value='extraPurpose'></s:property></td>   
                                            </tr> 
                                    </s:iterator>  
                                </tbody></table></div>
                        <div class="botButtion"> 
                        </div>
                    </div> 
                </div> 
            </div>

        </div>            



    </body>
</html>



