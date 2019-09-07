<%-- 
    Document   : index
    Created on : Jul 27, 2019, 5:54:11 PM
    Author     : Gurmeet Singh
--%>
<%@page import="server_code.HitCount"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>SignerAppTest</title>
        <link rel="stylesheet" type="text/css" href="./styles/mystyle.css">

        <!--   included scripts     -->
        <script type="text/javascript" src="scripts/jquery-3.0.0.js"></script>
        <script type="text/javascript" src="scripts/signerApp.js"></script>
        
        <!--   page script  -->
        <script type="text/javascript">
            // populate data fields with sample data
            function fillSampleData(){
                var sampleData = getSampleData();
                document.getElementById("data_text").value = sampleData;
                document.getElementById("empno").value = '66-24196';
                document.getElementById("name").value = 'Gurmeet Singh';
                document.getElementById("certsno").value  = '1234567890';
            } 
            //  call back function          
            function receiveSign(sign){
                document.getElementById("sign_text").value = sign;
            }
            // function to prepare data and get sign through signerApp
            function doSign(method){
                //may fetch from server in hidden vars
                var applicatioData = document.getElementById("data_text").value;
                //signer detail, action and datetime
                var empno = document.getElementById("empno").value;
                var name = document.getElementById("name").value;
                var role = document.getElementById("signer_role").value;
                var action = document.getElementById("signer_ction").value;
                var remarks  = document.getElementById("signer_remarks").value;
                var certsno = document.getElementById("certsno").value;
                var datetime = document.getElementById("server_date_time").value;
                //
                var mathodToCall;
                if(1 == method){
                    mathodToCall = signWithMehtod1;
                }else if(2 == method){
                     mathodToCall = signWithMethod2;
                }else{
                    mathodToCall = signWithMethod3;
                }
                mathodToCall(applicatioData, empno, name, role, action, remarks, certsno, datetime, receiveSign);
            }
            //
            function reset(){
                document.getElementById("sign_text").value = "";
            }
        </script>
        
        <!--   Fetching Server Date Time JSP Code -->
        <%
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");
            String serverDateTime = dt.format(new Date());
            int hitCount = HitCount.hitCount;
        %>
    </head>
    <body onload="fillSampleData()"><div class = "main_div">
            <p class = "lbl1">Hit Count = <%=hitCount%></p>
            
            
            <!--  Application Data make it from the application object (you may do it at the server itself) -->
            <p class = "lbl1">Application Data:<br>
            <textarea id="data_text" rows="11" cols="100"></textarea></p>
            
            <!--  Signer Details fetch from the server (logged in person) -->
            <p class = "lbl1">Signer's Details:<br>
            <font class = "lbl2">Unit-Emp.No.: </font> <input type="text" id="empno" size="10" />
            <font class = "lbl2">Name: </font> <input type="text" id="name" size="25" />
            <font class = "lbl2">Cert.Sr.No.: </font> <input type="text" id="certsno" size="20" /> </p>
            
            <!-- Signer Role (in work flow), Action (chosen by the signer) and remarks  (added by the signer) -->
            <p class = "lbl1">Signer's Role, Action and Remarks:<br>
            <font class = "lbl2">Signer Role: </font><select id="signer_role">
                <option>Forwarder</option>
                <option>Recommender</option>
                <option>Approver</option>
            </select>
            <font class = "lbl2">Action: </font><select id="signer_ction">
                <option>Approve</option>
                <option>Disapprove</option>
            </select>
            <!-- Date and Time fetched from the server -->
            <font class = "lbl2">Date and Time: </font><input  id="server_date_time" value="<%=serverDateTime%>"><br>
            <font class = "lbl2">Remarks: </font><input type="text" id="signer_remarks" value="Signer's remarks!!" size="80" /></p>
            
            
            <!-- Digital signature of the signer  -->
            <p class = "lbl1">Digital signature (base64 encoded):<br>
            <textarea id="sign_text" rows="5" cols="100"></textarea></p>
            
                        
            <!-- Buttons for different methods of calling SignerApp  -->
            <button class="button" id="sign_btn1" onclick="doSign(1)">Sign Method 1</button>
            <button class="button blue" id="sign_btn2" onclick="doSign(2)">Sign Method 2</button>
            <button class="button red" id="sign_btn3" onclick="doSign(3)">Sign Method 3</button>
            <button class="button black" id="reset_btn" onclick="reset()">Reset</button>
        
    </div></body>
</html>