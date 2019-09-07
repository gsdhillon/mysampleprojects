<%@page import="javacode.LoginSession"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
<base target="southframe">
<%
    String info = "Rec.Resolution:1024x768";
    String loginRef = "../login/login.jsp";
    String loginOrLogout = "Login";
    String loginTarget = "southframe";
    String label = "";
    HttpSession httpSession = request.getSession(true);
    if(httpSession != null){
        LoginSession loginSession = (LoginSession)httpSession.getAttribute(LoginSession.LOGIN_SESSION_ATTRIBUTE);
        if(loginSession != null && loginSession.isValid()){
            loginRef = "../login/logout.jsp";
            loginOrLogout = "Logout";
            loginTarget = "_parent";
            label = loginSession.getDisplayName()+" Logged-In";
        }else{
            label = "Not Logged-In";
        }
    }else{
        label = "Could not create a new session";
    }
%>
</head>
<body background="bannerImages/backgrd.jpg">
    <table border="1" width="100%" cellspacing="0" cellpadding="0">
        <tr>
            <td rowspan="3" height="100%">
                <img alt="Head" src="bannerImages/head.jpg" >
            </td>
            <td align="center" valign="top" height="20%">
                <font face="courier" size="2" color="blue">
                    <%=info%>
                </font>
            </td>
        </tr>
        <tr>
            <td align="center" valign="center" height="80%">
                <img alt="Logo"  src="bannerImages/logo.jpg">
            </td>
        </tr>
        <tr>
            <td align="center" valign="bottom" height="20%">
                <font face="courier" color="green">
                    <%=label%>
                </font>
            </td>
        </tr>
    </table>
     <table border="1" width="100%" cellspacing="0" cellpadding="0">
        <tr>
            <td>
                <a href="../home/home.jsp">
                Home
                </a>
            </td>
            <td>
               <a href="../services/services.jsp">
                Services
                </a>
            </td>
            <td>
                <a href="../reports/reports.jsp">
                Reports
                </a>
            </td>
            <td>
                <a  href="<%=loginRef%>" target="<%=loginTarget%>">
                <%=loginOrLogout%>
                </a>
            </td>
        </tr>
    </table>  
</body>
</html> 