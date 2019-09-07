
<%@page import="lib.utils.Depacketizer"%>
<%
    Depacketizer packetData = null;
    Depacketizer packetvisinfo = null, packetcompinfo = null, packethostinfo = null, packetapplicant = null, packetapprover = null;
    String date = "", time = "", purpose = "", visitplace = "", entryat = "";
    String visinfo = "", cominfo = "", hostinfo = "", applicant = "";
    int visitor = 0;
    if (request.getSession().getAttribute("data") != null) {

        String packet = request.getSession().getAttribute("data").toString();
        System.out.println("@@@@@@@@@@@@@ packet : " + packet);

        Depacketizer dp = new Depacketizer(packet);

        String data = dp.getString();
        packetData = new Depacketizer(data);
        date = packetData.getString();
        time = packetData.getString();
        purpose = packetData.getString();
        visitplace = packetData.getString();
        entryat = packetData.getString();

        visinfo = dp.getString();
        packetvisinfo = new Depacketizer(visinfo);
        visitor = packetvisinfo.getInt();

        cominfo = dp.getString();
        packetcompinfo = new Depacketizer(cominfo);

        hostinfo = dp.getString();
        packethostinfo = new Depacketizer(hostinfo);

        applicant = dp.getString();
        packetapplicant = new Depacketizer(applicant);

        String approver = dp.getString();
        packetapprover = new Depacketizer(approver);

    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<form>
    <input type="button" value="Print this page" onClick="window.print()">
</form>
<html>
    <head>
        <style type="text/css">
            p,body,table,tr,th,td,h1,h3,h2,h4{margin:.0em 0 .0em 0}
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CEP Application</title>
    </head>
    <body>
        <center>
            <h1><center>CENTRAL INDUSTRIAL SECURITY FORCE(CISF) Complex,Tarapur</center></h1>
            <h3><center><U>Request for Entry Permit for INDIAN NATIONALS</U></center></h3>
        </center>
        <table rows="1" rows="1" cols="2"  width="80%">
            <tr>
                <!--<th>Form-ID : 1234 </th>-->
            <th align="right">Date :<%=date%></th>
            <td>
                </tr>
        </table>

        <center>
            &nbsp;
            <table width="80%"><tr><td><h4>Visit Details</h4></td></tr></table>
            <table style="border:1px solid black;border-collapse:collapse;" rows="4" cols="4" width="80%" height="40">
                <tr>
                <th style="border:1px solid black;">From</th>
                <th style="border:1px solid black;">  To  </th>
                <th style="border:1px solid black;">Expected Time</th>
                <th style="border:1px solid black;">Expected Gate</th>
                </tr>
                <tr>
                <td style="border:1px solid black;" align="center"><%=date%></td>
                <td style="border:1px solid black;" align="center"><%=date%></td>
                <td style="border:1px solid black;" align="center"><%=time%></td>
                <td style="border:1px solid black;" align="center"><%=entryat%></td>
                </tr>
                <tr>
                <td style="border:1px solid black;"><b>Purpose<b></td>
                            <td style="border:1px solid black;" colspan="3"><%=purpose%></td>
                            </tr>
                            <tr>
                            <td style="border:1px solid black;"><b>Building List<b></td>
                                        <td style="border:1px solid black;" colspan="3"><%=visitplace%></td>
                                        </tr>
                                        </table>
                                        </center>

                                        <center>
                                            &nbsp;
                                            <table cellpadding="0" cellspacing="0" width="80%"><tr><td><h4>Visitors Details</h4></td></tr></table>
                                            <table cellpadding="0" cellspacing="0" style="border:1px solid black;border-collapse:collapse;" rows="3" cols="7" width="80%" height="60">
                                                <tr>
                                                <th style="border:1px solid black;">SNo.</th>
                                                <th colspan="2" style="border:1px solid black;">Name</th>
                                                <th style="border:1px solid black;">App ID</th>
                                                <th style="border:1px solid black;">Sex</th>
                                                <th style="border:1px solid black;">Occupation</th>            
                                                <th style="border:1px solid black;">Having PVC?</th>
                                                </tr>

                                                <% for (int row = 1; row <= visitor; row++) {%>
                                                <TR>                  
                                                <TD style="border:1px solid black;" align="center"><%= row%></TD>
                                                <TD colspan="2" style="border:1px solid black;" align="center"><%= packetvisinfo.getString()%></TD>
                                                <TD style="border:1px solid black;" align="center"><%= packetvisinfo.getString()%></TD>
                                                <TD style="border:1px solid black;" align="center"><%= packetvisinfo.getString()%></TD>
                                                <TD style="border:1px solid black;" align="center"><%= packetvisinfo.getString()%></TD>
                                                <TD style="border:1px solid black;" align="center"><%= packetvisinfo.getString()%></TD>
                                                </TR>
                                                <% }%>

                                                <tr>
                                                <td style="border:1px solid black;" colspan="2"><b>Company </b></td>
                                                <td style="border:1px solid black;" colspan="3"><%=packetcompinfo.getString()%></td>
                                                <th style="border:1px solid black;">Type</th>
                                                <td style="border:1px solid black;" align="center"><%=packetcompinfo.getString()%></td>
                                                </tr>
                                                <tr>
                                                <td style="border:1px solid black;" colspan="2"><b>Address </b></td>
                                                <td style="border:1px solid black;" colspan="5"><%=packetcompinfo.getString()%></td>
                                                </tr>
                                            </table>
                                        </center>

                                        <center>
                                            &nbsp;
                                            <table width="80%"><tr><td><h4>Officers to be Visited</h4></td></tr></table>
                                            <table style="border:1px solid black;border-collapse:collapse;" rows="2" cols="6" width="80%" height="30">
                                                <tr>
                                                <th style="border:1px solid black;" align="center">Emp.No.</th>
                                                <th style="border:1px solid black;" colspan="2" align="center">Name</th>
                                                <th style="border:1px solid black;" align="center">Division/Organization</th>
                                                <th style="border:1px solid black;" align="center">Contact No.</th>
                                                <th style="border:1px solid black;">Signature</th>
                                                </tr>
                                                <tr>
                                                <td style="border:1px solid black;" align="center"><%=packethostinfo.getString()%></td>
                                                <td colspan="2" style="border:1px solid black;" align="center"><%=packethostinfo.getString()%></td>
                                                <td style="border:1px solid black;" align="center"><%=packethostinfo.getString()%></td>
                                                <td style="border:1px solid black;" align="center"><%=packethostinfo.getString()%></td>
                                                <td style="border:1px solid black;"></td>
                                                </tr>
                                            </table>
                                        </center>

                                        <center>
                                            &nbsp;
                                            <table width="80%"><tr><td><h4>Applicant</h4></td></tr></table>
                                            <table style="border:1px solid black;border-collapse:collapse;" rows="2" cols="6" width="80%" height="30">
                                                <tr>
                                                <th style="border:1px solid black;" align="center">Emp.No.</th>
                                                <th style="border:1px solid black;" colspan="2" align="center">Name</th>
                                                <th style="border:1px solid black;" align="center">Division/Organization</th>
                                                <th style="border:1px solid black;" align="center">Contact No.</th>
                                                <th style="border:1px solid black;">Signature</th>
                                                </tr>
                                                <tr>
                                                <td style="border:1px solid black;" align="center"><%=packetapplicant.getString()%></td>
                                                <td colspan="2" style="border:1px solid black;" align="center"><%=packetapplicant.getString()%></td>
                                                <td style="border:1px solid black;" align="center"><%=packetapplicant.getString()%></td>
                                                <td style="border:1px solid black;" align="center"><%=packetapplicant.getString()%></td>
                                                <td style="border:1px solid black;" align="center"></td>
                                                </tr>
                                            </table>
                                        </center>

                                        <div style="width:40%;float:right"><h3><u>Visit Approved By</u></h3>
                                            <div style="width:98%;float:right"><b> Signature</b> </div>
                                            &nbsp;
                                            &nbsp;
                                            <div style="width:98%;float:right"><b> Name          : <%=packetapprover.getString()%></b> </div>
                                            <div style="width:98%;float:right"><b> Designation   : <%=packetapprover.getString()%></b> </div>
                                            <div style="width:98%;float:right"><b> Comp.Code No. : <%=packetapprover.getString()%></b> </div>
                                        </div>
                                        </body>
                                        </html>




