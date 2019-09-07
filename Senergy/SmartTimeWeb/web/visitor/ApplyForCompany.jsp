<%-- 
    Document   : ApplyForCompany
    Created on : Nov 22, 2012, 10:28:15 AM
    Author     : ROOP
--%>

<%@page import="java.awt.Dimension"%>
<%@page import="java.awt.Toolkit"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <%
            HttpSession httpSession = request.getSession(false);
            String sessionID = httpSession.getId();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = 800, height = 550;
            if (screenSize.width - 170 > width) {
                width = screenSize.width - 170;
            }
            if (screenSize.height - 200 > height) {
                height = screenSize.height - 200;
            }
        %>
    </head>
    <body>
        <jsp:plugin
            type="applet"
            code="SmartTimeApplet.visitor.AddCompany.AddCompany"
            archive="../SmartTimeApplet.jar"
        width="<%=width%>" 
        height="<%=height%>">
            <jsp:params>   
                <jsp:param name="SessionID" value="<%=sessionID%>" />   
            </jsp:params>  
            <jsp:fallback>
                <p>Your browser can't run Java Applets.<BR> Please install JRE1.6</p>
                </jsp:fallback>
            </jsp:plugin>
    </body>
</html>