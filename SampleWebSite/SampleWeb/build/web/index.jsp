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
      <jsp:plugin
        type="applet"
        code="applet.HomeApplet"
        archive="./MyApplet.jar"
        width="100%"
        height="100%"
        align="center">
        <jsp:params>
            <jsp:param name="SessionID" value="<%=sessionID%>" />
        </jsp:params>
    </jsp:plugin>
</body>
</html>  