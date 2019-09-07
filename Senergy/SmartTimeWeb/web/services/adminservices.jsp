<%-- 
    Document   : adminservices
    Created on : Nov 21, 2012, 1:19:38 PM
    Author     : pradnya
--%>

<%@page import="javacodes.login.LoginSession"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
    <%
        /***************************Login Test*********************************/
        String buttonsframe = "../home/homeLinks.html";
        String mainframe = "../loginRequest.html";
        HttpSession httpSession = request.getSession(false);
        if(httpSession != null){
            LoginSession loginSession = (LoginSession)httpSession.getAttribute(LoginSession.LOGIN_SESSION_ATTRIBUTE);
            if(loginSession!=null && loginSession.isValid()){
                    buttonsframe = "./admin_services_links.html";
                    mainframe = "./servicesHelp.html";
            }
        }
        /********************* End login test ***************************/
    %>
</head>
    <frameset cols="150,*" border="0">
        <frame name="buttonsframe" noresize src="<%=buttonsframe%>"></frame>
        <frame name="mainframe" id="mainframe" src="<%=mainframe%>" ></frame>
        <noframes>
        <body>
            <p>This page uses frames, but your browser doesn't support them.</p>
        </body>
        </noframes>
    </frameset>
</html>
 
