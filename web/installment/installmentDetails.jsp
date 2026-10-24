<%-- 
    Document   : installmentDetails
    Created on : 5 Apr, 2016, 7:01:39 PM
    Author     : Arun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />  
        <link rel="stylesheet" type="text/css" media="all" href="script/jsDatePick_ltr.min.css" />  
        <script type="text/javascript" src="script/jquery.1.4.2.js"></script>
        <script type="text/javascript" src="script/jsDatePick.jquery.min.1.3.js"></script>


        <script type="text/javascript">
                window.onload = function() {
                    new JsDatePick({
                        useMode: 2,
                        target: "dateId",
                        dateFormat: "%Y-%m-%d"
                    });

                };

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
                        document.daily.action = "deleteInstallDetails.action";
                        document.customerForm.submit();
                    } else {
                        document.daily.action = "updateInstallDetails.action";
                        document.customerForm.submit();
                    }


                }
        </script>
        <script type="text/javascript" src="script/Validations.js"></script> 
    </head> 
    <body> 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  
        <br> 

        <s:if test="%{#request.message != ''}">
            <script> 
            alert("${message}");
            </script> 
        </s:if>     
        <br> 
        <div style="width: 1700px;height: 1200px;  ">
            <div  id="instdiv" style="float: left; width: 850px; height: 1000px;   text-align: center" >  
             



                <div class="wrapper pB30" style="margin-left: 10px;width: 800px;"> 
                        <div  >
                    <h2 style="background:  #fcc661;width: 780px;">Installment Details </h2>
                </div> 
                    <div class="emiTable-wrap">

                        <div style="display: block;" class="dTable">   

                            <div class="dTable_replace">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody><tr class="headerTr"> 
                                            <td class="emi-serviceTaxHeader">Date</td>
                                            <td class="emi-serviceTaxHeader">A/C No</td>  
                                            <td class="emi-serviceTaxHeader">Deposited By Partner</td>
                                            <td class="emi-serviceTaxHeader">Installment</td>  
                                            <td class="emi-serviceTaxHeader">Deposit Name </td>
                                            <td class="emi-serviceTaxHeader">Actual Amount</td>  
                                            <td class="emi-serviceTaxHeader">Paid amount</td>
                                            <td class="emi-serviceTaxHeader">Due amount</td>   
                                        </tr> 


                                        <s:iterator value="%{#request.installList}" var="map">  
                                            <tr style="font-size: 12px;font-family:  initial;">

                                                <td  class="emi-serviceTaxHeader">

                                                    <s:a href="displayInstall.action?accountId=%{accountId}&accountNumber=%{accountNumber}"> <s:property value="date"></s:property> </s:a></td>
                                                <td   class="emi-serviceTaxHeader" style="  text-align: left"> <s:property value="accountNumber"></s:property> </td> 
                                                <td class="emi-serviceTaxHeader" style=" text-align: left"> <s:property value="depositByPartner"></s:property> </td> 
                                                <td  class="emi-serviceTaxHeader"  > <s:property value="installment"></s:property> </td>  
                                                <td  class="emi-serviceTaxHeader" style=" text-align: left"> <s:property value="depositName"></s:property> </td>    
                                                <td  class="emi-serviceTaxHeader" style=" text-align: right"> <s:property value="acutalAmount"></s:property> </td> 
                                                <td  class="emi-serviceTaxHeader" style=" text-align: right"> <s:property value="paidAmount"></s:property> </td> 
                                                <td  class="emi-serviceTaxHeader" style=" text-align: right"> <s:property value="dueAmount"></s:property> </td> 
                                                </tr> 
                                        </s:iterator>
                                    </tbody></table></div>
                            <div class="botButtion"> 
                            </div>
                        </div> 
                    </div> 
                </div>




                <div id="instdivUpdateDIV" hidden>
                    <div class="widgetHeader">
                        <h2>Installment Details </h2>
                    </div>  
                    <table class="tableDisplay" >
                        <thead> 
                            <tr style="font-style: oblique ;font-size: 13px">
                                <th width="7%" style="height:5px;">Date</th>
                                <th width="7%" style="height:5px;">Account No</th> 
                                <th width="7%" style="height:5px;">Deposited By Partner</th> 
                                <th width="5%" style="height:15px;">Installment</th>  
                                <th width="8%" style="height:5px;">Deposit Name </th>   
                                <th width="8%" style="height:5px;">Actual Amount </th>   
                                <th width="10%" style="height:15px;">Paid amount</th> 
                                <th width="10%" style="height:15px;">Due amount</th> 
                            </tr>  
                            <s:iterator value="%{#request.installList}" var="map">  
                                <tr style="font-size: 18px;font-family:  initial;">

                                    <td width="7%" style="height:5px;"> 
                                        <s:property value="date"></s:property>  </td>
                                    <td width="7%" style="height:5px;"> <s:property value="accountNumber"></s:property> </td> 
                                    <td width="7%" style="height:5px;"> <s:property value="depositByPartner"></s:property> </td> 
                                    <td width="5%" style="height:15px;"> <s:property value="installment"></s:property> </td>  
                                    <td width="8%" style="height:5px;"> <s:property value="depositName"></s:property> </td>    
                                    <td width="10%" style="height:15px;"> <s:property value="acutalAmount"></s:property> </td> 
                                    <td width="10%" style="height:15px;"> <s:property value="paidAmount"></s:property> </td> 
                                    <td width="10%" style="height:15px;"> <s:property value="dueAmount"></s:property> </td> 
                                    </tr> 
                            </s:iterator>
                        </thead> 
                    </table>    </div>
                <p style="text-align: right;margin-right:  10px;"><input type="button" class="submit-button-10"  onclick="printDiv('instdivUpdateDIV')" value="Print" /></p>
            </div>
            <div style=" float: left; width: 50px;  "> </div>
            <s:iterator value="%{#request.installDispaly}" var="map">
                <div style="text-align: right;float: left; width: 700px; height: 820px;  border:1px solid; border-color: blue;" id="instdivUpdate"> 
                    <div  >
                        <center><h2 style="background:  #fcc661;">Update Daily Sheet Details </h2></center> 
                    </div> 
                    <center>    
                        <form  name="daily" method="post" > 
                            <table  style="alignment-adjust: central;font-size: 18px;font-family:  initial;"> 
                                <tr><td>&nbsp; </td>  </tr>
                                <tr>
                                    <td>Date : </td>
                                    <td>
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
                                                <option>No</option>
                                                <option >Yes</option>
                                            </select> </td>
                                    </tr>
                                    <tr><td>&nbsp; </td> </tr>
                                    <tr>

                                        <td colspan="2"><input type="submit" name="Delete" value="Delete" class="submit-button-10"  onclick="validateDailySheetu('Delete')"  /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" class="submit-button-10"  name="Update"  value="Update" onclick="validateDailySheetu('Update')" /> </td>

                                    </tr>
                                </table> 
                            </form></center>
                    </div> </s:iterator>    
            <br style="clear: left;" />
        </div> 


    </body>
</html>
