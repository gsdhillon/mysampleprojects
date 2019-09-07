<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sample Web</title>
    </head>
    <frameset rows="160,*" border="1">
        <frame name="northframe" scrolling="no" noresize src="banner/banner.jsp"></frame>
        <frame name="southframe" id="southframe"  noresize src="home/home.jsp"></frame>
        <noframes>
            <body>
                <p>This page uses frames, but your browser doesn't support them.</p>
            </body>
        </noframes>
    </frameset>
</html> 