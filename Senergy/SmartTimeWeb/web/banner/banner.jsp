<%@page import="org.apache.catalina.connector.OutputBuffer"%>
<%@page import="javacodes.login.LoginSession"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript">
            var image1="",image2="",image3="",image4="",image5="" ,image6="",image7="",image8="";
            function defualtImages(){
                image1= 'SelfB_S.jpg';
                image2= 'Change_PwdB_S.jpg';
                image3= 'ReportB_S.jpg';
                image4= 'ServicesB_S.jpg';
                image5= 'User_AccessB_S.jpg';
                image6= 'ReaderB_S.jpg';
                image7= 'HomeB_S.jpg';
                image8= 'TeamB_S';
            }
           
            function changeImage(index){
                switch(index){
                    case 1:
                        defualtImages();
                        image1='SelfO_S.jpg'; 
                        break;                        
          
                    case 2:
                        defualtImages();
                        image2='ChangePwdO_S.jpg'; 
                        break;                        
                        
                    case 3:
                        defualtImages();
                        image3='ReportO_S.jpg';  
                        break;                       
                        
                    case 4:
                        defualtImages();
                        image4='ServicesO_S.jpg'; 
                        break;                        
                        
                    case 5:
                        defualtImages();
                        image5='UserAccessO_S.jpg';   
                        break;                        
                        
                    case 6:
                        defualtImages();
                        image6='ReaderO_S.jpg';       
                        break;                        
                    case 7:
                        defualtImages();
                        image7='HomeO_S.jpg';    
                        break;                  
                }                
                document.getElementById('toChange1').src='bannerImages/'+image1;  
                document.getElementById('toChange2').src='bannerImages/'+image2;
                document.getElementById('toChange3').src='bannerImages/'+image3;
                document.getElementById('toChange4').src='bannerImages/'+image4;
                document.getElementById('toChange5').src='bannerImages/'+image5;
                document.getElementById('toChange6').src='bannerImages/'+image6;
                document.getElementById('toChange7').src='bannerImages/'+image7;   
                document.getElementById('toChange8').src='bannerImages/'+image8;               
                
            }   
        </script>
        <base target="southframe">
        <%
            String info = "";
            String loginRef = "../login/login.jsp";
            String loginOrLogout = "Login";
            String ImgloginOrLogout = "bannerImages/Login.jpg";
            String loginTarget = "southframe";
            String label = "";
            String label2 = "";
            HttpSession httpSession = request.getSession(true);
            LoginSession loginSession = null;
            if (httpSession != null) {
                loginSession = (LoginSession) httpSession.getAttribute(LoginSession.LOGIN_SESSION_ATTRIBUTE);
                if (loginSession != null && loginSession.isValid()) {
                    loginRef = "../login/logout.jsp";
                    loginOrLogout = "Logout";
                    ImgloginOrLogout = "bannerImages/Logout.jpg";
                    loginTarget = "_parent";
                    label = loginSession.getDisplayName() + "";
                    label2 = "Logged-In";
                } else {
                    loginSession = null;
                    label = "Not Logged-In";
                    label2 = "      ...";
                }
            } else {
                label = "Could not create a new session";
            }
        %>
        <style type="text/css">
            p.menu
            {
                position:fixed;
                top:42px;
                left:5px;
            }
            p.images
            {
                position:fixed;
                top:-35px;
                left:0px;
            }
            p.info
            {
                position:fixed;
                top:20px;
                right:10px;
            }
            p.loginout
            {
                position:fixed;
                top:10px;
                right:10px;                
            }
        </style>
    </head> 
    <body background="bannerImages/background.png" >
        <p class="loginout">
        <table border="0" width="100%"  cellspacing="0" cellpadding="0" >
            <tr>

            <td align="right" valign="bottom" height="20%">
                <font face="courier" size="2" color="white">
                    <%=label%>
                </font>
            </td>
        </tr> 
        <tr>
        <td align="right" valign="bottom" height="20%">
            <font face="courier" size="2" color="white">
                <%=label2%>
            </font>
        </td
    </tr>
    <tr>
    <td align="right" valign="bottom" height="20%">
        <a href="<%=loginRef%>" target="<%=loginTarget%>" >
            <img src="<%=ImgloginOrLogout%>" width="61" height="18">
        </a>
    </td>
</tr>   
</p>
</table>
<p class="images">            
<table border="0" width="70%"  cellspacing="0" cellpadding="0" >
    <tr>
    <td align="left" valign="center" height="100%">
        <font face="courier" size="2" color="white">
            <img src ="bannerImages/Main.jpg">
        </font>
    </td>

    <!--<td align="right" valign="center" height="100%">
        <font face="courier" color="green">
            <img src="bannerImages/bannerRight.JPG">
        </font>
    </td> -->
</tr>
</p>
</table>
<p class="menu">
<table border="0"  cellspacing="2" cellpadding="0"> <!-- width="250" -->
    <tr>
    <td align='right'><a href='../home/home.jsp' ><img src='bannerImages/Home.jpg' width='61' height='18'></a></td>

    <%
        if (loginSession != null) {
                if ((loginSession.getLoginType() == LoginSession.TYPE_USER)) {
                    if ((!loginSession.isTeamLeader())) {
                        String buttonCode2 = "<td align= 'right'><a href='../self/self.jsp' ><div><img src='bannerImages/SelfB_S.jpg' id='toChange1' width='61' height='18'  onclick='changeImage(1)'></div></a></td>";
                        out.println(buttonCode2);
                        String buttonCode3 = "<td align= 'right'><a href='../visitor/Visitor.jsp' ><div><img src='bannerImages/Change_PwdB_S.jpg' id='toChange2' width='61' height='18'  onclick='changeImage(2)'></div></a></td>";
                        out.println(buttonCode3);
                        String buttonCode4 = "<td align= 'right'><a href='../ChangePassword/ChangePassword.jsp' ><div><img src='bannerImages/Change_PwdB_S.jpg' id='toChange2' width='61' height='18'  onclick='changeImage(2)'></div></a></td>";
                        out.println(buttonCode4);
                    } else if (loginSession.isTeamLeader()) {//(loginSession.isTeamLeader()) {
                        String buttonCode2 = "<td align= 'right'><a href='../self/self.jsp' ><div><img src='bannerImages/SelfB_S.jpg' id='toChange1' width='61' height='18'  onclick='changeImage(1)'></div></a></td>";
                        out.println(buttonCode2);
                        String buttonCode3 = "<td align='right'><a href='../reports/reports.jsp' ><div><img src='bannerImages/TeamB_S.jpg' id='toChange1' width='61' height='18' onclick='changeImage(8)'></a></td>";
                        out.println(buttonCode3);
                        String buttonCode4 = "<td align= 'right'><a href='../ChangePassword/ChangePassword.jsp' ><div><img src='bannerImages/Change_PwdB_S.jpg' id='toChange2' width='61' height='18'  onclick='changeImage(2)'></div></a></td>";
                        out.println(buttonCode4);
                    }
                } else if (loginSession.getLoginType() == LoginSession.TYPE_OFFICE) {
                    String buttonCode1 = "<td align= 'right'><a href='../services/services.jsp' ><div><img src='bannerImages/ServicesB_S.jpg' id='toChange4' width='61' height='18'  onclick='changeImage(4)'></div></a></td>";
                    out.println(buttonCode1);
                    //String buttonCode2 = "<td align= 'right'><a href='../reports/musterReport.jsp' >Muster Report</a></td>";
                    //out.println(buttonCode2);
                    String buttonCode3 = "<td align='right'><a href='../reports/reports.jsp' ><div><img src='bannerImages/ReportB_S.jpg' id='toChange3' width='61' height='18'  onclick='changeImage(3)'></div></a></td>";
                    out.println(buttonCode3);
                    String buttonCode4 = "<td align= 'right'><a href='../ChangePassword/ChangePassword.jsp' ><div><img src='bannerImages/Change_PwdB_S.jpg' id='toChange2' width='61' height='18'  onclick='changeImage(2)'></div></a></td>";
                    out.println(buttonCode4);
                } else {//LoginSession.TYPE_ADMIN
                    String buttonCode = "<td align='right'><a href='../reader/Reader.jsp' ><div><img src='bannerImages/ReaderB_S.jpg' id='toChange6' width='61' height='18'  onclick='changeImage(6)'></div></a></td>";
                    out.println(buttonCode);
                    String buttonCode1 = "<td align= 'right'><a href='../services/adminservices.jsp' ><div><img src='bannerImages/ServicesB_S.jpg' id='toChange4' width='61' height='18'  onclick=changeImage(4)></div></a></td>";
                    out.println(buttonCode1);
                    String buttonCode2 = "<td align= 'right'><a href='../User/AddUserAccess.jsp'><div><img src='bannerImages/User_AccessB_S.jpg' id='toChange5' width='61' height='18'  onclick=changeImage(5)></div></a></td>";
                    out.println(buttonCode2);
                    String buttonCode3 = "<td align= 'right'><a href='../ChangePassword/ChangePassword.jsp' ><div><img src='bannerImages/Change_PwdB_S.jpg' id='toChange2' width='61' height='18'  onclick='changeImage(2)'></a></td>";
                    out.println(buttonCode3);
                }
            }

    %>

    <%--  <td align="right">
        <a href="<%=loginRef%>" target="<%=loginTarget%>" >
            <img src="<%=ImgloginOrLogout%>" width="61" height="18">
        </a>
    </td>  --%>  
</tr>
</table>
</p>

</body>
</html> 
