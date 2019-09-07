<%@page import="javacodes.login.LoginSession"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
</head>
<body>
    <%
        /************************ Logout *************************/
        HttpSession httpSession = request.getSession(false);
        if(httpSession != null){
            LoginSession loginSession = (LoginSession)httpSession.getAttribute(LoginSession.LOGIN_SESSION_ATTRIBUTE);
            if(loginSession != null){
                loginSession.invalidate();
                httpSession.removeAttribute(LoginSession.LOGIN_SESSION_ATTRIBUTE);
            }
        }
        /********************* End Logout ************************/
        /*********** Redirect request ****************/
        try{
            response.sendRedirect("../index.jsp");
        }catch(Exception e){
        }
        /*********************************************/
    %>
</body>
</html>
 