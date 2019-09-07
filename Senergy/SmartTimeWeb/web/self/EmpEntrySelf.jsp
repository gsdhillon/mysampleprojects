<%-- 
    Document   : EmpEntry
    Created on : Sep 29, 2012, 2:40:11 PM
    Author     : pradnya
--%>

<%@page import="java.awt.Toolkit"%>
<%@page import="java.awt.Dimension"%>
<%@page import="javacodes.login.LoginSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <%
            HttpSession httpSession = request.getSession(false);
            String sessionID = httpSession.getId();
            //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = 854, height = 575;
            //if (screenSize.width - 170 > width) {
            //    width = screenSize.width - 170;
            //}
            //if (screenSize.height - 200 > height) {
            //    height = screenSize.height - 200;
            //}
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };

            LoginSession MySession = LoginSession.getLoginSession(httpSession, reqTypes);
            String empcode = MySession.getUserID();
            String ReportType = "Self";
        %>
    </head>
    <body>
        <jsp:plugin
            type="applet"
            code="SmartTimeApplet.EmpEntryReport.EmpDailyEntryReport"
            archive="../SmartTimeApplet.jar"
        width="<%=width%>" 
        height="<%=height%>">
            <jsp:params>   
                <jsp:param name="SessionID" value="<%=sessionID%>" />   
                <jsp:param name="empcode" value="<%=empcode%>" /> 
                <jsp:param name="ReportType" value="<%=ReportType%>" /> 
            </jsp:params>  
            <jsp:fallback>
                <p>Your browser can't run Java Applets.<BR> Please install JRE1.7</p>
                </jsp:fallback>
            </jsp:plugin>
    </body>
</html> 
