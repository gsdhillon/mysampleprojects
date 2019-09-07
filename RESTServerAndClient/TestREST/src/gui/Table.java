package gui;

import javafx.scene.layout.Border;

/**
 *
 * @author GurmeetSingh, gsdhillon@gmail.com
 */
public class Table {
    private String title = null;
    private final String start = "<table width='700'>\n<tr>\n";
    private final StringBuilder body = new StringBuilder();
    private final String end = "</tr>\n</table>";
    private String thClass;
    private String tdClass;
    
    
    public Table(boolean border) {
        decorate(border);
    }
    
    public Table(String title, boolean border){
        this.title = title;
        decorate(border);
    }

    private void decorate(boolean border) {
        if(border){
            thClass = " class='b2' ";
            tdClass = " class='b1' ";
        }else{
            thClass = " class='b0' ";
            tdClass = " class='b0' ";
        }
    }
    
    
    
    public Table row(){
        body.append("</tr>\n<tr>\n");
        return this;
    }
    
    public Table th(String th, int widthPercent){
        body.append("\t")
            .append("<th")
            .append(thClass)
            .append("width='")
            .append(widthPercent)
            .append("%'")
            .append(">")
            .append(th)
            .append("</th>")
            .append("\n");
        return this;
    }
    public Table td(String td, int colspan){
        body.append("\t")
            .append("<td")
            .append(tdClass)
            .append("colspan=")
            .append(colspan)
            .append(">")
            .append(td)
            .append("</td>")
            .append("\n");
        return this;
    }
    public Table td(String td){
        body.append("\t")
            .append("<td")
            .append(tdClass)
            .append(">")
            .append(td)
            .append("</td>")
            .append("\n");
        return this;
    }
    public Table h1(String h){
        body.append("\t")
            .append("<th")
            .append(thClass)
            .append(">")
            .append("<h1>")
            .append(h)
            .append("</h1>")
            .append("</th>")
            .append("\n");
        return this;
    }
    public Table h2(String h){
        body.append("\t")
            .append("<th")
            .append(thClass)
            .append(">")
            .append("<h2>")
            .append(h)
            .append("</h2>")
            .append("</th>")
            .append("\n");
        return this;
    }

    public String content() {
        
        String heading = "";
        if(title != null){
            heading += "<h3>"+title+"</h3>";
        }
        return 
                heading +
                start + body.toString() + end;
    }
    
}
