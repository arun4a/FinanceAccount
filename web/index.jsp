<%-- 
    Document   : index
    Created on : 5 Mar, 2016, 2:13:26 PM
    Author     : Arun
--%>

<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.util.Map"%> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
    <head>
        <title> Finance Account</title>
        <link rel="icon" href="img/ganesh-ji-pics-collection.jpg" type="image/icon">
        <link rel="stylesheet" type="text/css" media="all" href="css/indexcss.css" />  
        <link rel="stylesheet" type="text/css" media="all" href="css/main.css" />  
        <script src="script/Validations.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <style type="text/css">

        </style>
    </head>
    <body onload="noBack();"   onpageshow="if (event.persisted) noBack();" onunload="">
        <!--50100124602312-->
        <div id="top-part">
            <div id="header">
                <div class="space">
                    <!--Laxmi Ganapathi   Finance--> 
                </div>

                <div class="tlinks">
                    <div style="float: left; margin-top: 1px;">

                        <p style="margin-top: 3px;"> </p>
                    </div>
                    <!--                    <a href="logout.action"> Logout </a>-->
                </div>
            </div>
        </div>
        <div id="mainwrap">
            <div id="wrap"> 

            </div> 
        </div> 
        <s:if test="{#session.error != null}" >
            <h3><s:property value="#session.error"></s:property></h3>
        </s:if>






        <div id="form_container">
            <div class="widgetHeader"><center><h2>Login</h2></center></div>  

            <form action="userLogin" name="user" method="post" onsubmit="return validate();" id="form_536009" class="appnitro"  >
                <div class="form_description">

                </div>						
                <ul  style="margin:0 0 0 197px;">
                    <li id="li_1" >
                        <label class="description" for="element_1">User Id <b id="altusername" class="alertMsg" style="display: block;"></b></label>
                        <div>
                            <input id="username" name="userName"   class="element text medium" type="text" maxlength="50" value=""/> 
                        </div> 
                    </li>		<li id="li_2" >
                        <label class="description" for="element_2">Password <b id="altpassword" class="alertMsg" style="display: block;"></b></label>
                        <div>
                            <input id="password" name="password" class="element text medium" type="password" maxlength="50" value=""/> 
                        </div> 
                    </li>
                    <li class="buttons">
                        <input type="hidden" name="form_id" value="536009" />

                        <input id="saveForm" class="button_text" type="submit" name="submit" value="Submit"   style="font-size:12px; margin-left:108px;">
                    </li>
                </ul>
            </form>	
        </div>




    </body>
</html>
