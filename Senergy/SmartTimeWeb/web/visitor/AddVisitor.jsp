<%-- 
    Document   : AddVisitor
    Created on : Dec 19, 2012, 1:08:29 PM
    Author     : Roop
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
            int width = 900, height = 600;
            if (screenSize.width - 170 > width) {
                width = screenSize.width - 170;
            }
            if (screenSize.height - 200 > height) {
                height = screenSize.height - 200;
            }
        %>
    </head>
    <body>
        <%--    
                changed on 07-01-2012
                code="SmartTimeApplet.visitor.AddVisitor.AddNewVisitor" 
                width="<%=width%>" 
                height="<%=height%>">
        --%>
        <jsp:plugin
            type="applet"
            code="SmartTimeApplet.visitor.vis_app.ShowCompanyListApplet"
            archive="../SmartTimeApplet.jar"
            width="98%" 
            height="98%">
            <jsp:params>   
                <jsp:param name="SessionID" value="<%=sessionID%>" />   
            </jsp:params>  
            <jsp:fallback>
                <p>Your browser can't run Java Applets.<BR> Please install JRE1.6</p>
                </jsp:fallback>
            </jsp:plugin>
    </body>
</html>