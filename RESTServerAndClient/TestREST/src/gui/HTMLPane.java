package gui;


import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
//import javax.swing.JTextPane;

public class HTMLPane{
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

//        JTextPane pane = new JTextPane();
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(pane);

        frame.getContentPane().add(scrollPane);

        //
        String doc = new HTMLDoc()
        .add(
            new Table(false)
                    .h1("Bhabha Atomic Research Centre")
            .row()
                    .h2("Computer Division, E&I Group")
            .row()
                    .h2("Holiday/Late Working Application")
            .content()
        ).add(
            new Table(true)
                .th("Form ID: 1002", 50)
                .th("Date : 21/08/2019", 50)
            .content()
        ).add(
            new Table("Title of Table2", true)
                .th("GET", 40)
                .th("POST", 60)
            .row()
                .td("GET is a safe method (idempotent)")
                .td("POST is non-idempotent method")
            .row()
                .td("We can send limited data with GET method and it's sent in the header request URL.")
                .td("we can send large amount of data with POST because it's part of the body.")
            .content()
        ).htmlContent();
        

        System.out.println(doc);
        pane.setText(doc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

