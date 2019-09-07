<%@page import="javacode.LoginSession"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
    <%
        /***************************Login Test*********************************/
        String buttonsframe = "";
        String mainframe = "../loginRequest.html";
        HttpSession httpSession = request.getSession(false);
        if(httpSession != null){
            LoginSession loginSession = (LoginSession)httpSession.getAttribute(LoginSession.LOGIN_SESSION_ATTRIBUTE);
            if(loginSession!=null && loginSession.isValid()){
                    buttonsframe = "./servicesLinks.html";
                    mainframe = "./servicesHelp.html";
            }
        }
        /********************* End login test ***************************/
    %>
</head>
    <frameset cols="20%,*" border="1">
        <frame name="buttonsframe" noresize src="<%=buttonsframe%>"></frame>
        <frame name="mainframe" id="mainframe" src="<%=mainframe%>" ></frame>
        <noframes>
        <body>
            <p>This page uses frames, but your browser doesn't support them.</p>
        </body>
        </noframes>
    </frameset>
</html>
 