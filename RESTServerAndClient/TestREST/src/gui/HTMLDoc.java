package gui;

/**
 *
 * @author GurmeetSingh, gsdhillon@gmail.com
 */
public class HTMLDoc {
    private final String start =
    "<!DOCTYPE html>"+
    "<html>\n"+
        "<head>\n"+
            "<style type='text/css'>\n"+
                "\th1, h2, h3, th, td { font-family:sans-serif }\n"+
                "\th1, h2, h3, th { font-weight:bold }\n"+
                "\th1 { font-size: 20 }\n"+
                "\th2 { font-size: 18 }\n"+
                "\th3 { font-size: 16 }\n"+
                "\tth { font-size: 15 }\n"+
                "\ttd { font-size: 15 }\n"+
                "\t.b2 { border: 1px solid black }\n"+
                "\t.b1 { border: 1px solid black }\n"+
                "\t.b0 { border: none }\n"+
            "</style>\n"+
        "</head>\n"
      + "<body>\n";
    private StringBuilder body = new StringBuilder();
    private final String end = 
        "\n</body>"+
    "\n</html>";

    public HTMLDoc() {
    }
    
    public HTMLDoc add(String content){
        body.append(content);
        return this;
    }

    public String htmlContent() {
        return start + body.toString() + end;
    }
    
    
    
}
