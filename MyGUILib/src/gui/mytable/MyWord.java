package gui.mytable;

import java.io.FileOutputStream;
import org.apache.poi.xwpf.usermodel.*;
/**
 * Class MyWord
 * Created on Aug 31, 2013
 * @version 1.0.0
 * @author Gurmeet Singh
 */
public class MyWord {
    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args){
        try {
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph para1 = document.createParagraph();
            para1.setAlignment(ParagraphAlignment.CENTER);
            para1.setBorderBottom(Borders.SINGLE);
            para1.setBorderTop(Borders.SINGLE);
            para1.setBorderRight(Borders.SINGLE);
            para1.setBorderLeft(Borders.SINGLE);
            para1.setBorderBetween(Borders.SINGLE);

            XWPFRun p1r1 = para1.createRun();
            p1r1.setBold(true);
            p1r1.setItalic(true);
            p1r1.setText("Hello world! This is paragraph one!");
            p1r1.addBreak();

            XWPFRun p1r2 = para1.createRun();
            p1r2.setText("Run two!");
            p1r2.setTextPosition(100);

            XWPFRun p1r3 = para1.createRun();
            p1r3.setStrike(true);
            p1r3.setFontSize(20);
            p1r3.setSubscript(VerticalAlign.SUBSCRIPT);
            p1r3.setText(" More text in paragraph one...");

            XWPFParagraph para2 = document.createParagraph();
            para2.setAlignment(ParagraphAlignment.DISTRIBUTE);
            para2.setIndentationRight(200);
            XWPFRun p2r2 = para2.createRun();
            p2r2.setText("And this is paragraph two.");


            XWPFTable table = document.createTable();
            table.setWidth(800);
            table.setCellMargins(100, 20, 0, 0);
            XWPFTableRow tableTwoRowOne = table.getRow(0);
            tableTwoRowOne.getCell(0).setText("HHHHH");
            tableTwoRowOne.createCell().setText("GGGGGG");
            for (int i = 1; i < 5; i++) {
                XWPFTableRow tr = table.createRow();
                tr.getCell(0).setText("WWWWWWWW");
                tr.getCell(1).setText("ZZZZZZZZZ");
            }
            FileOutputStream os = new FileOutputStream("D:/Gurmeet/BARC/MyTable/HelloPOI.doc");
            document.write(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}