<%-- 
    Document   : UpdataCustomer
    Created on : 18 Apr, 2016, 12:34:50 PM
    Author     : sys1089
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
    <head>
        <s:if test="#session.userName == null">
            <script type="text/javascript">
                window.parent.window.location = "logout.action";
            </script>
        </s:if>
        <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />  
        <script type="text/javascript" src="script/Validations.js"></script>
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Finance Account</title>
        <link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">
        <link rel="stylesheet" type="text/css" media="all" href="script/jsDatePick_ltr.min.css" />  
        <script type="text/javascript" src="script/jquery.1.4.2.js"></script>
        <script type="text/javascript" src="script/jsDatePick.jquery.min.1.3.js"></script>
        <script type="text/javascript" src="script/Validations.js"></script>

        <script type="text/javascript">
                window.onload = function() {
                    new JsDatePick({
                        useMode: 2,
                        target: "dateId1",
                        dateFormat: "%Y-%m-%d"
                    });
                };</script>

    </head>
    <body>
        <div>
            <%@include file="../panel/menu.jsp" %>
        </div>   
        <br>
        <div class="widgetHeader"  style="width: 1000px;margin-left:450px">
            <center> <h2  style="background:  #fcc661">Update Customer Details</h2></center>
        </div>  
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 

        <div style=" text-align: center"> 

            <s:iterator value="%{#request.displayCustomerDetails}" var="cust" >
                <form  name="customerForm"  method="post"   > 
                    <center>      <table style="alignment-adjust: central;font-size: 18px;font-family:  initial;">

                            <tr><td>&nbsp; </td>  </tr>
                            <tr>
                                <td>Date : </td>
                                <td><input  readonly="readonly" type="text" name="dateOfIssue" id="dateId1" value="<s:property value='dateOfIssue'></s:property>"/> 
                                    <input type="hidden" name="customerId"  value="<s:property value='customerId'></s:property>" id="customerId"/>
                                    </td>
                                    <td> &nbsp;</td> 
                                    <td>Account Number  : </td>
                                    <td><input type="text" name="accountNumberNew" id="accountNumberNew" value="<s:property value='accountNumberNew'></s:property>"/> </td>
                                </tr>
                                <tr><td>&nbsp; </td>  </tr>

                                <tr> 
                                    <td>Customer Name : </td>
                                    <td><input    type="text" name="customerName" value="<s:property value='customerName'></s:property>" id="customerName"/> </td>

                                    <td> &nbsp;</td>
                                    <td>    <select name="custRel" id="custRel" style="width: 60px">
                                            <option><s:property  value="custRel"></s:property></option>
                                            <option>S/o</option>
                                            <option>W/o</option>
                                            <option>D/o</option>  
                                        </select>  </td>
                                    <td><input type="text" name="customerFatherName" id="customerFatherName" value="<s:property value='customerFatherName'></s:property>"/> </td>


                                </tr>

                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td>Occupation : </td>
                                    <td><input type="text" name="customerOccupation" id="customerOccupation" value="<s:property value='customerOccupation'></s:property>"/> </td>
                                    <td> &nbsp;</td>  
                                    <td>Mobile Number  : </td>
                                    <td><input type="text" name="customerMobileNo" id="customerMobileNo" value="<s:property value='customerMobileNo'></s:property>"/> </td> 
                                </tr>
                                <tr> <td>Address : </td>
                                    <td> <textarea  name="customerAddres" id="customerAddres"  ><s:property value='customerAddres'></s:property></textarea>  </td>
                                    <td> &nbsp;</td>
                                    <td>Second Mobile Number  : </td>
                                    <td><input type="text" name="customerSecMobileNo" id="customerSecMobileNo" value="<s:property value='customerSecMobileNo'></s:property>"/> </td> 

                                </tr>
                                <tr><td>&nbsp; </td>  </tr>

                                <tr><td><strong>Surety Details</strong> </td>  </tr>
                                <tr><td>&nbsp; </td>  </tr>

                                <tr>
                                    <td>Surety Name : </td>
                                    <td><input    type="text" name="suretyName" id="suretyName" value="<s:property value='suretyName'></s:property>"/> </td>
                                    <td>&nbsp; </td>
                                    <td>  

                                        <select name="sureRel" id="sureRel" style="width: 60px">
                                            <option><s:property  value="surRel"></s:property></option>
                                            <option>S/o</option>
                                            <option>W/o</option>
                                            <option>D/o</option>  
                                        </select> 
                                    </td>
                                    <td><input type="text" name="suretyFname" id="suretyFname" value="<s:property value='suretyFname'></s:property>"/> </td>
                                </tr>

                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td>Surety Occupation : </td>
                                    <td><input type="text" name="suretyOccupation" id="suretyOccupation" value="<s:property value='suretyOccupation'></s:property>"/> </td>
                                    <td> &nbsp;</td> 
                                    <td>Surety Address : </td>
                                    <td> <textarea  name="suretyAddress" id="suretyAddress" ><s:property value='suretyAddress'></s:property></textarea>  </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>

                                <tr>
                                    <td> Surety Mobile Number  : </td>
                                    <td><input type="text" name="suretyMobileNo" id="suretyMobileNo" value="<s:property value='suretyMobileNo'></s:property>"/> </td>
                                    <td> &nbsp;</td>  
                                    <td>Second Mobile Number  : </td>
                                    <td><input type="text" name="suretySecMobileNo" id="suretySecMobileNo" value="<s:property value='suretySecMobileNo'></s:property>"/> </td> 

                                </tr> 
                                <tr><td> &nbsp;</td>  </tr>
                                <tr><td><strong>Amount Details</strong> </td>  </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td> Amount  : </td>
                                    <td><input type="text" name="amount" id="amount" value="<s:property value='amount'></s:property>"/> </td>
                                    <td> &nbsp;</td>
                                    <td>Original Amount  : </td>
                                    <td><input type="text" name="originalAmount" id="originalAmount" value="<s:property value='originalAmount'></s:property>"/> </td>
                                </tr>

                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td> Installments  : </td>
                                    <td><input type="text" name="installments" id="installments" value="<s:property value='installments'></s:property>"/> </td>
                                    <td> &nbsp;</td>
                                    <td>Bank Account No   : </td>
                                    <td><input type="text" name="accountNo"  value="<s:property value='accountNo'></s:property>"   id="accountNo"/>  </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td style="vertical-align: bottom"> Cheque : 
                                    </td>
                                    <td style="vertical-align: top">  
                                        <select name="cheque" id="cheque" > 
                                            <option> <s:property value="cheque"></s:property></option>
                                            <option>0</option>
                                            <option>1</option>
                                            <option>2</option>
                                            <option>3</option>
                                            <option>4</option>
                                            <option>5</option>
                                            <option>6</option>
                                            <option>7</option>
                                            <option>8</option>
                                            <option>9</option>
                                            <option>10</option>
                                        </select> 
                                    </td> 

                                    <td> Atm  : <input type="checkbox" name="atm"   id="atm" <s:if test="%{atm == 'on'}">checked="checked"</s:if>/> 
                                    </td>
                                    <td>PassBook  : <input type="checkbox" name="passbook" id="passbook" <s:if test="%{passbook == 'on'}">checked="checked"</s:if>/> 
                                    </td>

                                </tr>
                                <tr><td>&nbsp; </td> </tr>
                                <tr><td>&nbsp; </td> <td>&nbsp; </td> 
                                    <td><input type="submit" name="Delete"  class="submit-button-10" value="Delete"  onclick="ValidateCustomerUpdate('Delete', 'Open');" /> </td><td>&nbsp; </td> 
                                    <td><input type="submit" name="Update"  class="submit-button-10" value="Update"  onclick="ValidateCustomerUpdate('Update', 'Open');" /> </td>
                                </tr>
                            </table></center>     
                    </form>
            </s:iterator>
            <br><br><br><br>
        </div>
    </body>
</html>
