<%-- 
    Document   : ShiftMaster
    Created on : Jun 14, 2012, 3:08:53 PM
    Author     : pradnya
--%>

<%@page import="java.awt.Toolkit"%>
<%@page import="java.awt.Dimension"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <%
            HttpSession httpSession = request.getSession(false);
            String sessionID = httpSession.getId();
            //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width=854,height=575;
            //if(screenSize.width-170>width){
            //    width=screenSize.width-170;
            //}
            //if(screenSize.height-200>height){
             //   height=screenSize.height-200;
            //}
        %>
    </head>
    <body>
        <jsp:plugin
            type="applet"
            code="SmartTimeApplet.services.shift.Shift"
            archive="../SmartTimeApplet.jar"
            width="<%=width%>" 
            height="<%=height%>">
            <jsp:params>   
                <jsp:param name="SessionID" value="<%=sessionID%>" />   
            </jsp:params>  
            <jsp:fallback>
                <p>Your browser can't run Java Applets.<BR> Please install JRE1.7</p>
                </jsp:fallback>
            </jsp:plugin>
    </body>
</html>

