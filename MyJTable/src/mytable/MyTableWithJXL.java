package mytable;

import java.io.File;
import java.io.Serializable;
import javax.swing.JOptionPane;
import jxl.CellView;
import jxl.HeaderFooter;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.*;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyTableWithJXL extends MyTable implements Serializable{
    //
    public static final int PAGE_ORIENTATION_PORTRAIT = 11;
    public static final int PAGE_ORIENTATION_LANDSCAPE = 12;
    /**
     * 
     * @param tableModel
     * @param addSearchFields 
     */
    public MyTableWithJXL(MyTableModel tableModel, boolean addSearchFields){
        super(tableModel, addSearchFields);
    }
    /**
     * 
     * @param file
     * @param headerText
     * @param pageorient 
     */
    @SuppressWarnings("UseSpecificCatch")
    public void exportToExcel(File file, String headerText, int pageorient) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            String name = file.getName();
            WritableSheet sheet = workbook.createSheet(name.substring(0, name.indexOf(".")), 0);
            //Page Setup
            SheetSettings s = sheet.getSettings();
            s.setPaperSize(PaperSize.A4);
            if (pageorient == PAGE_ORIENTATION_PORTRAIT) {
                s.setOrientation(PageOrientation.PORTRAIT);
            } else {
                s.setOrientation(PageOrientation.LANDSCAPE);
            }
            s.setHeaderMargin(0.25f);
            s.setFooterMargin(0.25f);
            s.setTopMargin(0.5f);
            s.setBottomMargin(0.5f);
            s.setLeftMargin(0.75f);
            s.setRightMargin(0.75f);
            s.setHorizontalCentre(true);
            //Header
            HeaderFooter header = new HeaderFooter();
            HeaderFooter.Contents c = header.getLeft();
            c.setFontName("SansSerif");
            c.setFontSize(11);
            c.toggleBold();
            c.append(headerText);
            //
            c = header.getRight();
            c.setFontName("SansSerif");
            c.setFontSize(11);
            c.toggleBold();
            c.append("On ");
            c.appendDate();
            c.append(", number of rows ");
            c.append(String.valueOf(getRowCount()));
            sheet.getSettings().setHeader(header);

            //Column Headers
            WritableFont fontHeader = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
            WritableCellFormat formatHeader = new WritableCellFormat(fontHeader);
            formatHeader.setBackground(Colour.LIGHT_GREEN);
            formatHeader.setAlignment(Alignment.LEFT);
            formatHeader.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatHeader.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
            Label label;
            CellView colView = new CellView();
            for (int col = 0; col < getColumnCount(); col++) {
                colView.setAutosize(true);
                sheet.setColumnView(col, colView);
                label = new Label(col, 0, (String) getColumnName(col), formatHeader);
                sheet.addCell(label);
            }
            //Rows
            WritableFont fontData = new WritableFont(WritableFont.TIMES, 11, WritableFont.NO_BOLD);
            WritableCellFormat formatData = new WritableCellFormat(fontData);
            formatData.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatData.setAlignment(Alignment.LEFT);
            formatData.setWrap(true);
            for (int row = 0; row < getRowCount(); row++) {
                for (int col = 0; col < getColumnCount(); col++) {
                    Object obj = getValueAt(row, col);
                    String text = obj.toString();
                    label = new Label(col, row + 1, text, formatData);
                    sheet.addCell(label);
                }
            }
            //Footer
            HeaderFooter footer = new HeaderFooter();
            c = footer.getRight();
            c.setFontName("SansSerif");
            c.setFontSize(11);
            c.append("Page ");
            c.appendPageNumber();
            c.append(" of ");
            c.appendTotalPages();
            sheet.getSettings().setFooter(footer);
            workbook.write();
            workbook.close();
            JOptionPane.showMessageDialog(null, "Report saved at:\n" + file.getAbsolutePath());
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Export Failed : " + e.getMessage());
        }
    }
}