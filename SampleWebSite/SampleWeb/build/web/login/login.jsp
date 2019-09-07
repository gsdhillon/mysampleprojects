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
      <table border="1" width="600" align="center">
          <tr height="400">
              <td>
                  <jsp:plugin
                    type="applet"
                    code="applet.LoginApplet"
                    archive="../Applets.jar"
                    width="600"
                    height="400">
                   <jsp:params>
                      <jsp:param name="SessionID" value="<%=sessionID%>" />
                   </jsp:params>
                </jsp:plugin>
              </td>
          </tr>
      </table>
</body>
</html>  