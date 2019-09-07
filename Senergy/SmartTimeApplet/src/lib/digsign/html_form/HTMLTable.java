package lib.digsign.html_form;

import java.io.Serializable;

/**
 * HTMLTable.java
 */
public class HTMLTable  implements Serializable{
    public static final int WIDTH = 700;
    public static final String  borderColor = "rgb(0,0,0)";
    private static final String cellLT = " style = 'border: 1px solid "+borderColor+"' ";
    private static final String cellT =  " style = 'border-right: 1px solid "+borderColor+"; border-top: 1px solid "+borderColor+"; border-bottom: 1px solid "+borderColor+";' ";
    private static final String cellL = " style = 'border-right: 1px solid "+borderColor+"; border-left: 1px solid "+borderColor+"; border-bottom: 1px solid "+borderColor+";' ";
    private static final String cellA = " style = 'border-right: 1px solid "+borderColor+"; border-bottom: 1px solid "+borderColor+";' ";
    private static final String tableTag = "<TABLE width='"+WIDTH+"' align='center' style='font-family: sans-serif; font-size: 12pt' cellspacing='0' cellpadding='0' >";
    private StringBuilder sb;
    private boolean rowStarted;
    private int topMargin;
    private boolean topRowAdded;
    private boolean top;
    private boolean left;
    public HTMLTable(int topMargin){
        this.topMargin = topMargin;
        sb = new StringBuilder(tableTag)
             .append("<tr height='")
             .append(String.valueOf(topMargin))
             .append("'><td></td></tr></TABLE>")
             .append(tableTag);
        rowStarted = false;
        topRowAdded = false;
    }
    /**
     *
     * @return
     */
    public String getTable(){
        if(rowStarted){
            sb.append("</tr>");
        }
        sb.append("</TABLE>");
        String table = sb.toString();
        sb = new StringBuilder(tableTag);
        rowStarted = false;
        topRowAdded = false;
        return table;
    }
    /**
     *
     */
    public void newRow() {
        if(rowStarted){
            sb.append("</tr>");
        }
        sb.append("<tr height='35'>");
        rowStarted = true;
        left = true;
        if(!topRowAdded){
            top = true;
            topRowAdded = true;
        }else{
            top = false;
        }
    }
    /**
     *
     * @param data
     * @param width
     * @param rowSpan
     * @param colSpan
     */
    public void addDataCell(String data, int width, int rowSpan, int colSpan){
        sb.append(addCell(data, width, rowSpan, colSpan, false, "left"));
    }
    /**
     *
     * @param label
     * @param width
     * @param rowSpan
     * @param colSpan
     */
    public void addLabelCell(String label, int width, int rowSpan, int colSpan){
        sb.append(addCell(label, width, rowSpan, colSpan, true, "left"));
    }
    /**
     *
     * @param data
     * @param rowSpan
     * @param colSpan
     */
    public void addDataCell(String data, int rowSpan, int colSpan){
        sb.append(addCell(data, 0, rowSpan, colSpan, false, "left"));
    }
    /**
     *
     * @param label
     * @param rowSpan
     * @param colSpan
     */
    public void addLabelCell(String label, int rowSpan, int colSpan){
        sb.append(addCell(label, 0, rowSpan, colSpan, false, "left"));
    }
    /**
     * 
     * @param data
     * @param width
     * @param colSpan
     * @param align 
     */
    public void addCellNoBorder(String data, int width, int colSpan, String align){
        sb.append(addCell(data, width, "", 1, colSpan, false, align));
        topRowAdded = false;
    }
    
    /**
     *
     * @param data
     * @param width
     * @param border
     * @param colSpan
     * @return
     */
    private String addCell(String data, int width, int rowSpan, int colSpan, boolean isLabel, String align){
        String border;
        if(top && left){
            border = cellLT;
        }else if(top){
            border = cellT;
        }else if(left){
            border = cellL;
        }else{
            border = cellA;
        }
        if(left){
            left = false;
        }
        return addCell(data, width, border, rowSpan, colSpan, isLabel, align);
    }
    
    /**
     *
     * @param data
     * @param width
     * @param border
     * @param colSpan
     * @return
     */
    private String addCell(String data, int width, String border, int rowSpan, int colSpan, boolean isLabel, String align){
        String rowSpanStyle = "";
        if(rowSpan > 1){
            rowSpanStyle = " rowspan = '"+rowSpan+"'";
        }
        String colSpanStyle = "";
        if(colSpan > 1){
            colSpanStyle = " colspan = '"+colSpan+"'";
        }
        if(data == null || data.trim().equals("")){
            data = "&nbsp;";
        }
        if(isLabel){
            data = "<font color='blue'>"+data+"</font>";
        }
        String widthStyle = "";
        if(width > 0){
            widthStyle = "width='"+width+"'";
        }
        return
        "<td "+widthStyle+" align = '"+align+"' "+border+" "+rowSpanStyle+" "+colSpanStyle+"  >" +
        data+
        "</td>";
    }
}