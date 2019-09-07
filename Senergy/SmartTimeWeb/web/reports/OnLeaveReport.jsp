<%-- 
    Document   : OnLeaveReport
    Created on : Jul 12, 2012, 3:17:31 PM
    Author     : pradnya
--%>

<%@page import="javacodes.login.LoginSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <%
            HttpSession httpSession = request.getSession(false);
            String sessionID = httpSession.getId();
            int[] reqTypes = {
                    LoginSession.TYPE_USER,
                    LoginSession.TYPE_OFFICE,
                    LoginSession.TYPE_ADMIN
                };
            
            LoginSession MySession = LoginSession.getLoginSession(httpSession, reqTypes);
            String userID=MySession.getUserID();
        %>
    </head>
    <body>
        <jsp:plugin
            type="applet"
            code="SmartTimeApplet.reports.OnLeave.OnLeaveApplet"
            archive="../SmartTimeApplet.jar"
            width="100%" 
            height="98%">
            <jsp:params>   
                <jsp:param name="SessionID" value="<%=sessionID%>" />
                <jsp:param name="userID" value="<%=userID%>" />   
            </jsp:params>  
            <jsp:fallback>
                <p>Your browser can't run Java Applets.<BR> Please install JRE1.7</p>
                </jsp:fallback>
            </jsp:plugin>
    </body>
</html>