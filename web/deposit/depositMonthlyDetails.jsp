<%-- 
    Document   : depositMonthlyDetails
    Created on : 27 Apr, 2016, 6:02:22 PM
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
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />  
        <link rel="stylesheet" type="text/css" media="all" href="script/jsDatePick_ltr.min.css" />  
        <script type="text/javascript" src="script/Validations.js"></script>
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
                    var depositAccountNoUpdate = document.getElementById("depositAccountNoUpdate").value;
                    var paidAmountUpdate = document.getElementById("paidAmountUpdate").value;

                    if (dateId == null || dateId == "") {
                        alert("Please Select Date ");
                        setTimeout(function() {
                            document.getElementById("dateId").focus();
                        }, 0);
                        return false;
                    }
//
                    if (depositAccountNoUpdate == null || depositAccountNoUpdate == "") {
                        alert("Please enter Account Number ");
                        setTimeout(function() {
                            document.getElementById("depositAccountNoUpdate").focus();
                        }, 0);
                        return false;
                    }

                    if (paidAmountUpdate == null || paidAmountUpdate == "") {
                        alert("Please enter Amount ");
                        setTimeout(function() {
                            document.getElementById("paidAmountUpdate").focus();
                        }, 0);
                        return false;
                    }

                    if (isNaN(paidAmountUpdate)) {
                        alert("Please enter Numbers Only ");
                        setTimeout(function() {
                            document.getElementById("paidAmountUpdate").focus();
                            document.getElementById("paidAmountUpdate").select();
                        }, 0);
                        return false;
                    }
                    if (value == "delete") {
                        document.updatePaymentInterest.action = "deleteMonthPayment.action";
                    } else {
                        document.updatePaymentInterest.action = "updateMonthPayment.action";
                    }
                    document.updatePaymentInterest.submit();
                }



        </script>

    </head> 
    <body> 
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>  
        <br><br>
        <div style=" text-align: center;height: 10px">  
                <s:if test="%{#request.res_msg != null}">
                    <script> 
                        alert("${res_msg}");
                    </script> 
                </s:if>  
        </div>
        <br><br>
        <div style="width: 1400px;height: 1200px;  ">
            <div style="float: left; width: 600px; height: 1000px;   text-align: center"> 


                <div style="margin-left: 10px;width:578px" >
                    <h2 style="background:  #fcc661;" >Interest Details </h2>
                </div>  


                <div class="wrapper pB30" style="margin-left: 10px;width:580px;"> 
                    <div class="emiTable-wrap">

                        <div style="display: block;" class="dTable">   

                            <div class="dTable_replace">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody><tr class="headerTr">  
                                            <td class="emi-serviceTaxHeader">A/C No</td>  
                                            <td class="emi-serviceTaxHeader">Payment Date</td>
                                            <td class="emi-serviceTaxHeader">Paid Amount</td>  
                                            <td class="emi-serviceTaxHeader">Monthly Interest</td> 
                                        </tr> 

                                        <s:iterator value="%{#request.displayPaymentDetails}" var="map">  
                                            <tr> 
                                                <td class="emi-serviceTaxHeader"> 
                                                    <a href="displayPaymentInterestDetails.action?depositsIdDispaly=<s:property value='depositsPaidId'></s:property>&depositAccountNumber=<s:property value="depositAccountNo"></s:property> "> <s:property value="depositAccountNo"></s:property> </a></td>
                                                <td class="emi-serviceTaxHeader"  style=" text-align: center"> <s:property value="crtDate"></s:property> </td>   
                                                <td class="emi-serviceTaxHeader"  style=" text-align: right"> <s:property value="paidAmountFromActual"></s:property> </td> 
                                                <td class="emi-serviceTaxHeader"  style=" text-align: right"> <s:property value="paidAmount"></s:property> </td> 
                                                </tr> 
                                        </s:iterator> 
                                    </tbody></table></div>
                            <div class="botButtion"> 
                            </div>
                        </div> 
                    </div> 
                </div>


            </div>  <div style=" float: left; width: 50px;  "> </div>
            <s:iterator value="%{#request.displayPaymentInterest}" var="map">
                <div style="text-align: right;float: left; width: 700px; height: 390px;  border:1px solid; border-color: blue;"> 
                    <div  >
                        <center><h2 style="background:  #fcc661;">Update Interest Details </h2></center> 
                    </div> 
                    <center>   
                        <form name="updatePaymentInterest"   method="post" > 
                            <table  style="alignment-adjust: central"> 
                                <tr><td>&nbsp; </td>  </tr>
                                <tr>
                                    <td>Payment Date : </td>
                                    <td>
                                        <input    type="hidden" name="depositsIdUpdate" value="<s:property value='depositsPaidId'></s:property>" id="depositsIdUpdate"/>
                                        <input  readonly="readonly" type="text" name="date" id="dateId"  value="<s:property value='actualCrtDate'></s:property>" /> </td>
                                    </tr>

                                    <tr><td> &nbsp;</td>  </tr>
                                    <tr>
                                        <td>Account Number : </td>
                                        <td>

                                            <input type="hidden" name="depositAccountNumber" id="depositAccountNumber"  value="<s:property value='depositAccountNo'></s:property>"/>
                                        <input type="text" name="depositAccountNoUpdate" id="depositAccountNoUpdate"  value="<s:property value='depositAccountNo'></s:property>"/> </td>
                                    </tr>

                                    <tr><td>&nbsp; </td>  </tr>
                                    <tr>
                                        <td>Paid Amount : </td>
                                        <td><input type="text" name="paidAmountFromActualUpdate" id="paidAmountFromActualUpdate"  value="<s:property value='paidAmountFromActual'></s:property>"/> </td>
                                    </tr>


                                    <tr><td>&nbsp; </td>  </tr>
                                    <tr>
                                        <td>Monthly Interest : </td>
                                        <td><input type="text" name="paidAmountUpdate" id="paidAmountUpdate"  value="<s:property value='paidAmount'></s:property>"/> </td>
                                    </tr>

                                    <tr><td> &nbsp;</td>  </tr> 

                                    <tr>
                                        <td><input type="submit" class="submit-button-10" name="Delete" value="Delete"  onclick="validateDailySheetu('delete')" /> </td>
                                        <td><input type="submit" class="submit-button-10" name="Update" value="Update" onclick="validateDailySheetu('submit')"  /> </td>
                                    </tr>
                                </table>    
                            </form></center>
                    </div> </s:iterator>
            <br style="clear: left;" />
        </div> 


    </body>
</html>
