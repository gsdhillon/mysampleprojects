<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <%
            HttpSession httpSession = request.getSession(false);
            String sessionID = httpSession.getId();
        %>
    </head>
    <body>
        <jsp:plugin
            type="applet"
            code="applet.services.MonthlyMuster"
            archive="../Applets.jar"
            width="100%" 
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