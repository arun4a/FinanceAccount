<%-- 
    Document   : DepositDetails
    Created on : 19 Apr, 2016, 5:42:52 PM
    Author     : Arun
--%>

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
                        target: "opendDateSave",
                        dateFormat: "%Y-%m-%d"
                    });
                    new JsDatePick({
                        useMode: 2,
                        target: "closeDateSave",
                        dateFormat: "%Y-%m-%d"
                    });


                    g_calendarObject = new JsDatePick({
                        useMode: 2,
                        target: "paymentDate",
                        dateFormat: "%Y-%m-%d"
                    });
                    g_calendarObject.setOnSelectedDelegate(function() {
                        changeAccount();
                    });

                };


                function onlobody() {
                    new JsDatePick({
                        useMode: 2,
                        target: "opendDateSave",
                        dateFormat: "%Y-%m-%d"
                    });
                    new JsDatePick({
                        useMode: 2,
                        target: "closeDateSave",
                        dateFormat: "%Y-%m-%d"
                    });


                    g_calendarObject = new JsDatePick({
                        useMode: 2,
                        target: "paymentDate",
                        dateFormat: "%Y-%m-%d"
                    });
                    g_calendarObject.setOnSelectedDelegate(function() {
                        var obj = g_calendarObject.getSelectedDay();
                        var date = obj.year + "-";
                        if (obj.month.toString().length === 1) {
                            date += "0" + obj.month + "-";
                        } else {
                            date += obj.month + "-";
                        }
                        if (obj.day.toString().length === 1) {
                            date += "0" + obj.day;
                        } else {
                            date += obj.day;
                        }
                        changeAccount(date);
                    });

                }

        </script> 

        <script src="script/jquery-1.8.2.js" type="text/javascript"></script>
        <script>

                function changeAccount(date) {
                    var accountNumberMonth = (document.getElementById("accountNumberMonth").value).trim();
                    document.getElementById("paymentDate").value = date;
                    if (accountNumberMonth == "" || accountNumberMonth == null) {
                        alert("Please enter Account Number");
                        setTimeout(function() {
                            document.getElementById("accountNumberMonth").focus();
                        }, 0)
                        return false;
                    }
                    document.depositsDetailsMonth.action = "ajaxAction.action";
                    document.depositsDetailsMonth.submit();
                }

        </script>


    </head>
    <body onload="onlobody()" > 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  

        <div class="mainDiv">
            <div class="well"  style="height: 450px">
                <div style="float: left; width: 450px; height: 420px; border:solid 1px; text-align: center"> 
                    <div class="widgetHeader"><center><h2 style="width: 94.5%;background:  #fcc661" >Deposit Details</h2></center></div>    
                    <br>
                    <center>
                        <form name="depositsDetails"  method="POST" autocomplete="off">
                            <table>
                                <tr>
                                    <td> 
                                        <label class="description" for="element_1">Deposit Name </label>
                                    </td>
                                    <td>
                                        :
                                    </td>
                                    <td>
                                        <input type="text"  name="depositNameSave" id="depositNameSave" value="<%=(request.getParameter("depositName") != null ? request.getParameter("depositName") : "")%>"  />
                                        <input type="hidden"  name="depositIdSave" id="depositIdSave" value="<%=(request.getParameter("depositId") != null ? request.getParameter("depositId") : "")%>"  />

                                    </td>
                                    <td>
                                        <b id="altcamp_id" class="alertMsg" style="display: block;"></b>
                                    </td> 
                                </tr>

                                <tr>
                                    <td>
                                        <label class="description" for="element_1">Account Number</label>
                                    </td>
                                    <td>:</td>
                                    <td>
                                        <input type="text"  name="accountNumberSave" id="accountNumberSave" value="<%=(request.getParameter("accountNumber") != null ? request.getParameter("accountNumber") : "")%>"  />
                                    </td>
                                    <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td>

                                </tr>
                                <tr>
                                    <td>
                                        <label class="description" for="element_1">Opening Date</label>
                                    </td>
                                    <td>:</td>
                                    <td>
                                        <input type="text"  name="opendDateSave" id="opendDateSave" value="<%=(request.getParameter("openDate") != null ? request.getParameter("openDate") : "")%>"  />
                                    </td>
                                    <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td>

                                </tr>
                                <tr>
                                    <td>
                                        <label class="description" for="element_1">Amount</label>
                                    </td>
                                    <td>:</td>


                                    <td>
                                        <input type="text"  name="amountSave" id="amountSave" value="<%=(request.getParameter("amount") != null ? request.getParameter("amount") : "")%>"  />
                                    </td>
                                    <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td>

                                </tr>

                                <tr>
                                    <td>
                                        <label class="description" for="element_1">  Closing Date</label>
                                    </td>
                                    <td>:</td>
                                    <td  >  
                                        <input type="text" name="closeDateSave" id="closeDateSave"  value="<%=(request.getParameter("closeDate") != null ? (request.getParameter("closeDate")) : "")%>"/>
                                    </td> 
                                </tr>  

                                <tr>
                                    <td>
                                        <label class="description" for="element_1">  Mobile No</label>
                                    </td>
                                    <td>:</td>
                                    <td >  
                                        <input type="text" name="mobileNoSave" id="mobileNoSave" value="<%=(request.getParameter("mobileNo") != null ? (request.getParameter("mobileNo")) : "")%>"/>
                                    </td> 
                                </tr>  
                                <tr>
                                    <td>
                                        <label class="description" for="element_1">  Deposit Type</label>
                                    </td>
                                    <td>:</td>
                                    <td  >  
                                        <select name="depositTypeSave"   id="depositTypeSave" >
                                            <option
                                                <%if (request.getParameter("depositType") != null && request.getParameter("depositType").equals("In")) {%>
                                                selected
                                                <%}%>>In</option>
                                            <option  <%if (request.getParameter("depositType") != null && request.getParameter("depositType").equals("Out")) {%>
                                                selected
                                                <%}%>>Out</option>
                                        </select>
                                    </td> 
                                </tr>  
                                <tr >
                                    <%if (request.getParameter("depositId") != null) {%>
                                    <td colspan="1" style="text-align:center"><input type="submit" value="Delete" id="typeDelete" class="submit-button-10" onclick="return DeleteDeposits();"/></td>
                                    <td colspan="2" style="text-align:center"><input type="submit" value="Update" id="type" class="submit-button-10" onclick="return UpdateDeposits();"/></td>
                                        <%} else {%>
                                    <td colspan="4" style="text-align:center"><input type="submit" value="Submit" id="type"  class="submit-button-10" onclick="return SaveDeposits();"/></td>
                                        <%}%>

                                </tr>

                            </table>
                        </form></center>
                </div>
                <div style="float: left; width: 450px;height: 415px;  border:solid 1px;  text-align: center"> 
                    <div class="widgetHeader"><center><h2 style="width: 94.5%;background:  #fcc661">Monthly payment</h2></center></div> 
                    <br>
                    <center>
                        <form name="depositsDetailsMonth"  method="POST"  >
                            <table> 
                                <tr>
                                    <td>
                                        <label class="description" for="element_1">Account Number</label>
                                    </td>
                                    <td>:</td>
                                    <td>
                                        <input type="text"  name="accountNumberMonth" id="accountNumberMonth" value="<s:property value='EnterAccountNumber'></s:property>"      />
                                        </td>
                                        <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td>

                                    </tr>
                                    <tr>
                                        <td>
                                            <label class="description" for="element_1">Payment Date</label>
                                        </td>
                                        <td>:</td>
                                        <td>
                                            <input type="text" onclick="clear()" name="paymentDate" id="paymentDate" value="<s:property value='EnterPaymentDate'></s:property>"      />
                                        </td>
                                        <td>  </td>

                                    </tr>
                                    <tr>
                                        <td>
                                            <label class="description" for="element_1">Out Amount</label>
                                        </td>
                                        <td>:</td>
                                        <td>
                                            <input type="text"  name="paidAmountA" id="paidAmountA" value="0"   />
                                        </td>
                                        <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td>

                                    </tr>

                                    <tr>
                                        <td>
                                            <label class="description" for="element_1"> Interest  <s:property value='InterestForDays'></s:property> </label>
                                        </td>
                                        <td>:</td>
                                        <td>
                                            <input type="text"  name="amountMonth" id="amountMonth" value="<s:property value='Interest'></s:property>"   />
                                        </td>
                                        <td> <b id="altis_active" class="alertMsg" style="display: block;"></b></td> 
                                    </tr> 
                                    <tr > 
                                        <td colspan="3" style="text-align:center"><input type="submit" value="Submit" class="submit-button-10"  id="type" onclick="return SaveMonthlyInterest();"/></td> 
                                    </tr> 
                                </table>
                            </form> 
                        </center>
                    </div> 

                    <div style="float: left; width: 420px; height: 415px;  border:solid 1px;  "> 

                        <div class="widgetHeader "><center><h2 style="width: 94.5%;background:  #fcc661">Total Deposit Details</h2></center></div>  <br> 
                        <p style="font-size: 20px;font-family:  serif;">   
                            Total Deposit Amount :&nbsp;&nbsp;<s:property value="TotalDepositAmount" /><br><br>
                        Total Deposit In Amount   :&nbsp;&nbsp;<s:property value="TotalDepositInAmount" /><br><br>
                        Total Deposit Out Amount   :&nbsp;&nbsp;<s:property value="TotalDepositOutAmount" /><br><br>
                        Total Paid Interest   :&nbsp;&nbsp;<s:property value="TotalMontlyPaidInterest" /><br><br></p>  
                </div>

            </div>

            <s:if test="%{#request.res_msg != null}">
                <script>
                    alert("${res_msg}");
                </script> 
            </s:if>   


            <div class="well">   
                <form    action="searchDeposits.action" method="post" onsubmit="return ValidateSearchDeposits();">
                    <table>
                        <tr> 
                            <td>Depositor Name:</td>
                            <td>
                                <input    type="text" name="depositNameSearch"  style="width:120px;height:30px;" placeholder="Enter Deposit Name" id="depositNameSearch"/>
                            </td>
                            <td>Account Number :</td>
                            <td> 
                                <input     type="text" name="depositAccountNumber"   style="width:120px;height:30px;" placeholder="Enter Account Number" id="depositAccountNumber"/>
                            </td>
                            <td>&nbsp;</td>
                            <td><input type="submit" value="Search" class="submit-button-10"   /></td>
                        </tr>
                    </table>
                </form>
            </div> 


        </div>            


        <div class="wrapper pB30" style="width: 1000px;margin-left: 300px"> 
            <div style="width: 1005px;text-align: center" class="wrapper" ><h2 style="background:  #fcc661"  >  Deposits Details</h2></div>
            <div class="emiTable-wrap">

                <div style="display: block;" class="dTable">   

                    <div class="dTable_replace">
                        <table cellpadding="0" cellspacing="0">
                            <tbody><tr class="headerTr">
                                    <td class="emi-serviceTaxHeader">Depositor Name</td>
                                    <td class="emi-serviceTaxHeader">A/C No</td>
                                    <td class="emi-serviceTaxHeader">Mobile Number</td>

                                    <td class="emi-serviceTaxHeader">Open Date</td>
                                    <td class="emi-serviceTaxHeader">Close Date</td>
                                    <td  >Original Amount</td> 

                                    <td class="emi-serviceTaxHeader">Out Amount</td>

                                    <td class="emi-serviceTaxHeader">Due Amount</td>
                                    <td class="emi-serviceTaxHeader">Monthly Interest</td>
                                    <td class="emi-serviceTaxHeader">Deposit Type</td>
                                </tr> 

                                <s:iterator value="%{#request.displayDeposits }" var="v">
                                    <tr  style=" font-size: 12px">    			
                                        <!--deposit_id,deposit_name,amount,interest,mobile_number,created_date,time-->
                                        <td  class="emi-serviceTaxHeader"  style="text-align: left; color: #f90 "><a href="displayDepositDetails.action?depositId=<s:property value='depositId'></s:property>&depositName=<s:property value='depositName'></s:property>&closeDate=<s:property value='actualCloseDate'></s:property>&amount=<s:property value='amount'></s:property>&mobileNo=<s:property value='mobileNo'></s:property>&depositType=<s:property value='depositType'></s:property>&openDate=<s:property value='actualOpenDate'></s:property>&accountNumber=<s:property value='accountNumber'></s:property>"  ><s:property value="depositName"></s:property></a></td> 
                                        <td style="text-align: left; " class="emi-serviceTaxHeader"><s:property value='accountNumber'></s:property></td> 

                                            <td class="emi-serviceTaxHeader"><s:property value="mobileNo"></s:property></td> 
                                        <td style="text-align: left; " class="emi-serviceTaxHeader"><s:property value="openDate"></s:property></td>  
                                        <td style="text-align: left; " class="emi-serviceTaxHeader"><s:property value="closeDate"></s:property></td> 
                                        <td style="text-align: right;color: orangered " ><s:property value='amount'></s:property></td>  
                                        <td style="text-align: right;   color: #f90" class="emi-serviceTaxHeader"><s:property value='padiAmountFromActual'></s:property></td>  
                                        <td style="text-align: right; " class="emi-serviceTaxHeader"><s:property value='dueAmount'></s:property></td>  
                                        <td style="text-align: right; " class="emi-serviceTaxHeader"><a href="displayMonthlyDetails.action?depositAccountNumber=<s:property value='accountNumber'></s:property>"> <s:property value="paidAmount"></s:property></a></td>  
                                        <td style="text-align: center; " class="emi-serviceTaxHeader"><s:property value="depositType"></s:property></td> 
                                        </tr> 
                                </s:iterator>


                            </tbody></table></div>
                    <div class="botButtion"> 
                    </div>
                </div> 
            </div> 
        </div>  
    </body>
</html>


