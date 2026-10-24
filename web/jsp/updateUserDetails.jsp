<%-- 
    Document   : updateUserDetails
    Created on : 26 Apr, 2016, 10:07:27 AM
    Author     : sys1089
--%>
 
<%@page import="java.util.Map.Entry"%>
<%@page import="com.sun.Accounts.CustomerMaster"%>
<%@page import="com.sun.Accounts.AccountsDetailsAction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--        <title>JSP Page</title>-->
        <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />  
        <link rel="stylesheet" type="text/css" media="all" href="script/jsDatePick_ltr.min.css" />  
        <script type="text/javascript" src="script/jquery.1.4.2.js"></script>
        <script type="text/javascript" src="script/jsDatePick.jquery.min.1.3.js"></script>
        <script type="text/javascript" src="script/Validations.js"></script>


        <script type="text/javascript">
            window.onload = function() {
                new JsDatePick({
                    useMode: 2,
                    target: "dateId",
                    dateFormat: "%Y-%m-%d"
                });
                new JsDatePick({
                    useMode: 2,
                    target: "dateId1",
                    dateFormat: "%Y-%m-%d"
                });
            };
        </script>
        <style>
            * {
                margin: 0;
                padding: 0;
            }

            .centerObject {
                width:1100px;
                height:750px;
                position:absolute;
                top:50%;
                left:50%;
                margin-left:-400px; /* Half of Width */    
                margin-top:-300px; /* Half of Height */ 
            }

            .centerObject  div{
                float: left;
            }
        </style>
    </head>
    <body>

        <div>
            <%@include file="../panel/menu.jsp" %>
        </div> 
        <!--
                <div style="height: 30px;alignment-adjust: central"><a href="userDetails.action">Users</a>  &nbsp; &nbsp; &nbsp;<a href="report.action">Report</a></div>-->
        <br>




        <%
            String customerNameN = "",
                    custRel = "",
                    fatherName = "",
                    occupation = "",
                    mobileNumber = "",
                    address = "",
                    suretyName = "",
                    sureRel = "",
                    SuretyFatherName = "",
                    suretyOccupation = "",
                    suretyAddress = "",
                    suretyMobileNumber = "",
                    amountNew = "",
                    originalAmount = "",
                    installmentsN = "",
                    accountNo = "",
                    passBook = "",
                    atm = "",
                    cheque = "0";
            String getDetails = "0";
            AccountsDetailsAction accountDetails = new AccountsDetailsAction();
            String totalCollectedAmountDaily = "0";
            String totalCollectedAmountMonth = "0";
            String totalIssuedAmountDaily = "0";
            String totalIssuedAmountMonth = "0";
            String TotalOpeningBalance = "0";
            Map<String, String> issuedBonds = accountDetails.issuedBondsAmountDailyAndMonthly();
            Map<String, String> totalAmount = accountDetails.getTotalAmount("");
            totalCollectedAmountDaily = totalAmount.get("totalCollectedAmountDaily");
            totalCollectedAmountMonth = totalAmount.get("totalCollectedAmountMonth");
            totalIssuedAmountDaily = issuedBonds.get("totalIssuedAmountDaily");
            totalIssuedAmountMonth = issuedBonds.get("totalIssuedAmountMonth");
            TotalOpeningBalance = issuedBonds.get("TotalOpeningBalance");
            String accountNumber = request.getParameter("accountNumberNew");
            try {
                if (request.getParameter("getId") != null || request.getParameter("getId") != "") {
                    getDetails = request.getParameter("getId");
                }

            } catch (Exception e) {

            }

            System.out.println(getDetails);
            try {

                if (getDetails.equals("get")) {
                    Map<Integer, CustomerMaster> custMap = accountDetails.customerDetails("0", accountNumber, "Any", "0");

                    for (Entry<Integer, CustomerMaster> entry : custMap.entrySet()) {
                        CustomerMaster customer = entry.getValue();
                        customerNameN = customer.getCustomerName();
                        custRel = customer.getCustRel();
                        fatherName = customer.getCustomerFatherName();
                        occupation = customer.getCustomerOccupation();
                        mobileNumber = customer.getCustomerMobileNo();
                        address = customer.getCustomerAddres();
                        suretyName = customer.getSuretyName();
                        sureRel = customer.getSurRel();
                        SuretyFatherName = customer.getSuretyFname();
                        suretyOccupation = customer.getSuretyOccupation();
                        suretyAddress = customer.getSuretyAddress();
                        suretyMobileNumber = customer.getSuretyMobileNo();
                        amountNew = customer.getAmount();
                        originalAmount = customer.getOriginalAmount();
                        installmentsN = customer.getInstallments();
                        accountNo = customer.getAccountNo();
                        passBook = customer.getPassbook();
                        atm = customer.getAtm();
                        cheque = customer.getCheque();
                    }
                }
            } catch (Exception e) {

            }

        %>

        <div style="  width: 1200px;  ">  
            <center> 

                <table  style="font-size: 20px;font-family:  serif;">
                    <tr>
                        <td>  Daily Collection              :   <b><%= totalCollectedAmountDaily != null ? totalCollectedAmountDaily : 0%> </b>  </td>
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td>  Daily  Issued Bonds Amount  : <b> <%= totalIssuedAmountDaily != null ? totalIssuedAmountDaily : 0%></b></td>
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td>  <i>Opening Balance</i>       : <b><%= TotalOpeningBalance != null ? TotalOpeningBalance : 0%></b></td>
                    </tr>
                    <tr> 
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                    </tr>
                    <tr> 
                        <td>  Monthly Collection         : <b><%= totalCollectedAmountMonth != null ? totalCollectedAmountMonth : 0%></b></td>
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td>  Monthly  Issued Bonds Amount:<b> <%= totalIssuedAmountMonth != null ? totalIssuedAmountMonth : 0%></b></td>
                        <td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                    </tr> 
                </table>

            </center>
        </div> <br>
        <div style=" text-align: center;height: 10px"> 
            <h4 style="color:blue;">${message}</h4>

        </div>
        <br>
        <div style="width: 1500px;height:900px;  ">
            <center>
            <div style="  width: 100px; float: left;   ;">  
                &nbsp;&nbsp; 
            </div>
            <div style="float: left; width: 455px; height: 890px; border:solid 1px;"> 
                <div class="widgetHeader"><center><h2>Daily Sheet</h2></center></div>    
                <center>
                    <form action="dailySheet" name="daily"  onsubmit="return validateDailySheet();" > 
                        <table  style="alignment-adjust: central;font-size: 18px;font-family:  initial;">

                            <tr><td>&nbsp; </td>  </tr>
                            <tr>
                                <td><b>Date</b> : </td>
                                <td><input  readonly="readonly" type="text" value="<s:property value='#session.dailySheetDate'></s:property>"  name="date" id="dateId"/> </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td>Deposit Amount : </td>
                                    <td><input type="text" name="depositAmount" id="depositAmount" value="0"/> </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td>Deposit Name : </td>
                                    <td><input type="text" name="depositName" id="depositName" value="0"/> </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td>Deposit By  : </td>
                                    <td><input type="text" name="depositBy" id="depositBy" value="0"/> </td>
                                </tr>
                                <tr><td>&nbsp; </td>  </tr>

                                <tr>
                                    <td>Account Number  : </td>
                                    <td><input type="text" name="accountNumber" id="accountNumber"/> </td>
                                </tr>
                                <tr><td> &nbsp;</td> </tr>
                                <tr>
                                    <td>Customer Name  : </td>
                                    <td><input type="text" name="customerName" id="customerName"/> </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td>Actual Amount : </td>
                                    <td><input type="text" name="actaulAmount" id="actaulAmount"/> </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                 <tr>
                                    <td>Paid Amount : </td>
                                    <td><input type="text" name="paidAmount" id="paidAmount"/> </td>
                                </tr>
                                <tr><td> &nbsp;</td>  </tr>
                                <tr>
                                    <td>Installment : </td>
                                    <td><input type="text" name="installment" id="installment"/> </td>
                                </tr>
                                <tr><td>&nbsp; </td>  </tr>
                                <tr>
                                    <td>Close : </td>
                                    <td>
                                        <select name="close" id="close"> 
                                            <option>No</option>   <option >Yes</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr><td>&nbsp; </td> </tr>
                                <tr>
                                    <td><input type="reset" name="Reset"  /> </td>
                                    <td><input type="submit" value="Submit"   /> </td>
                                </tr>
                            </table>
                        </form></center><div class="widgetFooter"></div></div>
                <div style="float: left; width: 900px;height: 890px; border:solid 1px; "> 
                    <div class="widgetHeader"><center><h2>Issued Bond</h2></center></div>  
                    <center>
                        <form action="customerDetails"  method="post" name="issedBondForm"   > 
                            <table style="font-size: 18px;font-family:  initial;"> 
                                <tr><td>&nbsp; </td>  </tr>
                                <tr>
                                    
                                     <td >Date : </td>
                                    <td><input  readonly="readonly" value="<s:property value='#session.issuedBondDate'></s:property>" type="text" name="issuedDate" id="dateId1"/> 
                                </td>  
                                               <td >  </td>
                                    <td colspan="3">A/C No : <input type="text" name="accountNumberNew" id="accountNumberNew"/> 
                                        <input type="button" onclick="getCustomerDetails()"  value="GetDetails" />
                                        <input type="hidden" name="getId" id="getId" />
                                    </td>

                                </tr>
                                <tr><td>&nbsp; </td>  </tr>
                                <tr>  
                                    <td >Customer Name : </td>
                                    <td><input    type="text" name="customerNameN"  id="customerNameN" value="<%=customerNameN%>"/> 
                                </td>  
                                <td colspan="1">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </td> 
                                <td colspan="3">
                                    <select name="custRel" id="custRel" style="width: 60px">
                                        <option
                                            <% if (custRel.equals("S/o")) {%>selected="selected"<%}%>
                                            >S/o</option>
                                        <option <% if (custRel.equals("W/o")) {%>selected="selected"<%}%>>W/o</option>
                                        <option <% if (custRel.equals("D/o")) {%>selected="selected"<%}%>>D/o</option>  
                                    </select> : <input type="text" name="fatherName" placeholder="Name"  id="fatherName" value="<%=fatherName%>"/> </td>

                            </tr>
                            <tr><td>&nbsp; </td>  </tr>
                            <tr> 
                                <td>Occupation : </td>
                                <td><input type="text" name="occupation" id="occupation" value="<%=occupation%>"/> </td>
                                <td> &nbsp;</td>
                                <td colspan="2">Address : <textarea  name="address" id="address" > <%=address%> </textarea>  </td> 
                            </tr>
                            <tr><td> &nbsp;</td>  </tr>

                            <tr>
                                 <td colspan="2">Mobile Number  : <input type="text" name="mobileNumber" id="mobileNumber" value="<%=mobileNumber%>"/></td> 
                              </tr>
                            <tr><td> &nbsp;</td>  </tr>


                            <tr><td colspan="2"><strong>Surety Details</strong> </td>  </tr><tr><td>&nbsp; </td>  </tr>

                            <tr>
                                <td>Surety Name : </td>
                                <td><input    type="text" name="suretyName" id="suretyName"  value="<%=suretyName%>"/> </td>
                                <td>&nbsp; </td>
                                <td colspan="2"><select name="sureRel" id="sureRel" style="width: 60px">
                                        <option
                                            <% if (sureRel.equals("S/o")) {%>selected="selected"<%}%>
                                            >S/o</option>
                                        <option <% if (sureRel.equals("W/o")) {%>selected="selected"<%}%>>W/o</option>
                                        <option <% if (sureRel.equals("D/o")) {%>selected="selected"<%}%>>D/o</option>  
                                    </select> :<input type="text" name="SuretyFatherName" id="SuretyFatherName" value="<%=SuretyFatherName%>" placeholder="Name"/></td>

                            </tr>
                            <tr><td>&nbsp; </td>  </tr>

                            <tr>
                                <td>  Occupation : </td>
                                <td><input type="text" name="suretyOccupation" value="<%=suretyOccupation%>" id="suretyOccupation"/> </td>
                                <td> &nbsp;</td> 
                                <td colspan="2">  Address : <textarea  name="suretyAddress" id="suretyAddress" > <%=suretyAddress%>  </textarea></td>

                            </tr>
                            <tr><td> &nbsp;</td>  </tr>

                            <tr>
                                <td>   Mobile Number  : </td>
                                <td><input type="text" name="suretyMobileNumber" value="<%=suretyMobileNumber%>"  id="suretyMobileNumber"/> </td>
                                <td> &nbsp;</td>
                                <td>&nbsp; </td> 
                            </tr>
                            <tr><td> &nbsp;</td>  </tr>
                            <tr><td colspan="2"><strong>Amount Details</strong> </td>  </tr><tr><td>&nbsp; </td>  </tr>
                            <tr>
                                <td> Amount  : </td>
                                <td><input type="text" name="amountNew" value="<%=amountNew%>" id="amountNew"/> </td>
                                <td> &nbsp;</td>
                                <td colspan="2">Original Amount  : <input type="text" name="originalAmount" value="<%=originalAmount%>"  id="originalAmount"/> </td>

                            </tr>
                            <tr><td> &nbsp;</td>  </tr>

                            <tr>
                                <td> Installments  : </td>
                                <td><input type="text" name="installmentsN" id="installmentsN" value="<%=installmentsN%>"/> </td>
                                <td> &nbsp;</td>
                                <td > Bank Account No  : <input type="text" name="accountNo" value="<%=accountNo%>" id="accountNo"/> </td> 
                            </tr>
                            <tr><td> &nbsp;</td>  </tr>
                            <tr>
                                <td>    PassBook : <input type="checkbox"  <% if (passBook.equals("on")) {%>checked="checked"<%}%> name="passBook" id="passBook"/>   </td>
                                <td>Atm  : <input type="checkbox" <% if (atm.equals("on")) {%>checked="checked"<%}%> name="atm" id="atm"/>   
                                </td>
                                <td>  
                                </td>  <td colspan="2">Cheque : <select name="cheque" id="cheque" >
                                        <%if (cheque.equals("0")) {
                                            } else {%><option><%=cheque%></option><%}%>
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

                            </tr>
                            <tr><td>&nbsp; </td> </tr>
                            <tr>
                                <td>&nbsp; </td>
                                <td><input type="reset" name="Reset"  value="Reset"/> </td>
                                <td><input type="submit" name="Submit" value="Submit" /> </td>
                                <td>&nbsp; </td>
                            </tr>
                        </table><div class="widgetFooter"></div>
                    </form></center></div>
            <br style="clear: left;" />
            </center>
        </div>

        <div class="widgetFooter"></div>
        <br> <br>
        <br>      <br> <br>
        <br>
        <script>
            window.onload(){
                document.getElementById("getId").value = "0";
            }

        </script>
    </body>
</html>
