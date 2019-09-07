<%-- 
    Document   : SRAddNew
    Created on : 20 Jun, 2012, 12:31:46 PM
    Author     : Gaurav
--%>

<%@page import="javacodes.login.LoginSession"%>
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
            int width = 854, height = 575;
            //if(screenSize.width-170>width){
            //    width=screenSize.width-170;
            //}
            //if(screenSize.height-200>height){
            //    height=screenSize.height-200;
            //}
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            LoginSession MySession = LoginSession.getLoginSession(httpSession, reqTypes);
            String userID = MySession.getUserID();
            String usertype = "" + MySession.getLoginType();
        %>
    </head>
    <body>
        <jsp:plugin
            type="applet"
            code="SmartTimeApplet.services.shiftroster.ShiftRoster"
            archive="../SmartTimeApplet.jar"
        width="<%=width%>" 
        height="<%=height%>">
            <jsp:params>   
                <jsp:param name="SessionID" value="<%=sessionID%>" />   
                <jsp:param name="userID" value="<%=userID%>" />   
                <jsp:param name="usertype" value="<%=usertype%>" />
            </jsp:params>  
            <jsp:fallback>
                <p>Your browser can't run Java Applets.<BR> Please install JRE1.7</p>
                </jsp:fallback>
            </jsp:plugin>
    </body>
</html>

