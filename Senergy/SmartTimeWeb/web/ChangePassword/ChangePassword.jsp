<%-- 
    Document   : ChangePassword
    Created on : Oct 6, 2012, 12:14:56 PM
    Author     : pradnya
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%
            //only at userLogin.jsp getSession(true) is used
            //at all other jsp or servlets getSession(false) will be used
            HttpSession httpSession = request.getSession(true);
            String sessionID = httpSession.getId();
        %>
    </head>
    <body leftmargin="0" topmargin="0">
        <table border="0" width="500" align="center">
            <tr height="300">
            <td>
                <jsp:plugin
                    type="applet"
                    code="SmartTimeApplet.UserAccess.ChangePasswordApplet"
                    archive="../SmartTimeApplet.jar"
                    width="500"
                    height="350">
                    <jsp:params>
                        <jsp:param name="SessionID" value="<%=sessionID%>" />
                    </jsp:params>
                </jsp:plugin>
            </td>
        </tr>          
    </table>
</body>
</html>  
