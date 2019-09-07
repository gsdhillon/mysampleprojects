package lib.excel;

import SmartTimeApplet.reports.UserMuster;
import lib.session.MyUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JCheckBox;
import javax.swing.table.TableModel;
import jxl.CellView;
import jxl.HeaderFooter;
import jxl.HeaderFooter.Contents;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import lib.utils.MyFile;
import lib.utils.MyLog;

/**
 * Class MyExcel Created on Jul 1, 2012
 *
 * @version 1.0.0
 * @author
 */
public class MyExcel {

    /**
     *
     * @param userMusters
     * @param monthName
     */
    public static void exportMuster(UserMuster[] userMusters, String divName, int year, int month) throws Exception {
        String title = divName;
        String legend = "Legend:    AA-Absent,  AA*-In Swipe Missing,  AA#-Out Swipe Missing,  WW-Week Off,  LL-On Leave,  HH-Holiday,  CF-Compensatry Off";
        int recordSPerPage = 14;//change if needed
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, 1);
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int totalColumns = numDays + 9;
        SimpleDateFormat df = new SimpleDateFormat("MMMM");
        String monthNm = df.format(calendar.getTime());
        //Set FileName and open workbook
//        File file = MyFile.chooseFileForSave("Muster_" + divName + "_" + monthNm + "_" + year + ".xls");
        File file = MyFile.chooseFileForSave("Muster_" + divName + "_" + monthNm + "_" + year + ".xls");
        if (file == null) {
            return;
        }
        WritableWorkbook workbook = Workbook.createWorkbook(file);
        WritableSheet sheet = workbook.createSheet("Muster_" + divName + "_" + monthNm + "_" + year, 0);
        SheetSettings s = sheet.getSettings();
        s.setPaperSize(PaperSize.LEGAL);
        s.setOrientation(PageOrientation.LANDSCAPE);
        s.setHeaderMargin(0.25f);
        s.setFooterMargin(0.25f);
        s.setTopMargin(0.5f);
        s.setBottomMargin(0.5f);
        s.setLeftMargin(0.75f);
        s.setRightMargin(0.75f);
        s.setHorizontalCentre(true);
        //set header footer
        HeaderFooter header = new HeaderFooter();
        Contents c = header.getLeft();
        c.setFontName("TAHOMA");
        c.setFontSize(14);
        c.toggleBold();
        c.append(title);
//        c = header.getCentre();
//        c.setFontName("TAHOMA");
//        c.setFontSize(14);
//        c.toggleBold();
//        c.append(title);
        c = header.getRight();
        c.setFontName("TAHOMA");
        c.setFontSize(14);
        c.toggleBold();
        c.append("Monthly Muster " + divName + " " + monthNm + ", " + year);
        sheet.getSettings().setHeader(header);
        HeaderFooter footer = new HeaderFooter();
        c = footer.getLeft();
        c.setFontName("TAHOMA");
        c.setFontSize(8);
        //c.toggleBold();
        c.append(legend);
        c = footer.getRight();
        c.setFontName("TAHOMA");
        c.setFontSize(8);
        //c.toggleBold();
        c.append("On ");
        c.appendDate();
        c.append(" Page ");
        footer.getRight().appendPageNumber();
        footer.getRight().append(" of ");
        footer.getRight().appendTotalPages();
        sheet.getSettings().setFooter(footer);
//        //titles Fonts and Formats
//        WritableFont fontTitle1 = new WritableFont(WritableFont.TAHOMA, 13, WritableFont.BOLD);
//        WritableCellFormat formatTitle1 = new WritableCellFormat(fontTitle1);
//        formatTitle1.setAlignment(Alignment.CENTRE);
//        formatTitle1.setVerticalAlignment(VerticalAlignment.CENTRE);
//        WritableFont fontTitle2 = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD);
//        WritableCellFormat formatTitle2 = new WritableCellFormat(fontTitle2);
//        formatTitle2.setAlignment(Alignment.CENTRE);
//        formatTitle2.setVerticalAlignment(VerticalAlignment.CENTRE);
        //header Fonts and Formats
        WritableFont fontHeader = new WritableFont(WritableFont.TAHOMA, 7, WritableFont.NO_BOLD);
        WritableCellFormat formatHeader = new WritableCellFormat(fontHeader);
        formatHeader.setAlignment(Alignment.CENTRE);
        formatHeader.setVerticalAlignment(VerticalAlignment.BOTTOM);
        formatHeader.setOrientation(Orientation.PLUS_90);
        formatHeader.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
        //data Fonts and Formats
        //black foreground - white background
        WritableFont fontData = new WritableFont(WritableFont.TAHOMA, 7, WritableFont.NO_BOLD);
        WritableCellFormat formatDataBlackWhite = new WritableCellFormat(fontData);
        formatDataBlackWhite.setAlignment(Alignment.CENTRE);
        formatDataBlackWhite.setVerticalAlignment(VerticalAlignment.CENTRE);
        formatDataBlackWhite.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
        formatDataBlackWhite.setBackground(Colour.WHITE);
        //black foreground - dark background
        WritableCellFormat formatDataBlackDark = new WritableCellFormat(fontData);
        formatDataBlackDark.setAlignment(Alignment.CENTRE);
        formatDataBlackDark.setVerticalAlignment(VerticalAlignment.CENTRE);
        formatDataBlackDark.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
        formatDataBlackDark.setBackground(Colour.LIGHT_GREEN);
        //black foreground - white background - vertical
        WritableCellFormat formatDataBlackWhiteVertical = new WritableCellFormat(fontData);
        formatDataBlackWhiteVertical.setAlignment(Alignment.CENTRE);
        formatDataBlackWhiteVertical.setVerticalAlignment(VerticalAlignment.CENTRE);
        formatDataBlackWhiteVertical.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
        formatDataBlackWhiteVertical.setBackground(Colour.WHITE);
        formatDataBlackWhiteVertical.setOrientation(Orientation.PLUS_90);
        //black foreground - dark background - vertical
        WritableCellFormat formatDataBlackDarkVertical = new WritableCellFormat(fontData);
        formatDataBlackDarkVertical.setAlignment(Alignment.CENTRE);
        formatDataBlackDarkVertical.setVerticalAlignment(VerticalAlignment.CENTRE);
        formatDataBlackDarkVertical.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
        formatDataBlackDarkVertical.setBackground(Colour.LIGHT_GREEN);
        formatDataBlackDarkVertical.setOrientation(Orientation.PLUS_90);
        //red foregrond - white background
        WritableFont fontDataRed = new WritableFont(WritableFont.TAHOMA, 7, WritableFont.NO_BOLD);
        fontDataRed.setColour(Colour.RED);
        WritableCellFormat formatDataRedWhite = new WritableCellFormat(fontDataRed);
        formatDataRedWhite.setAlignment(Alignment.CENTRE);
        formatDataRedWhite.setVerticalAlignment(VerticalAlignment.CENTRE);
        formatDataRedWhite.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
        formatDataRedWhite.setBackground(Colour.WHITE);
        //red foregrond - dark background
        WritableCellFormat formatDataRedDark = new WritableCellFormat(fontDataRed);
        formatDataRedDark.setAlignment(Alignment.CENTRE);
        formatDataRedDark.setVerticalAlignment(VerticalAlignment.CENTRE);
        formatDataRedDark.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
        formatDataRedDark.setBackground(Colour.LIGHT_GREEN);
//        //legend
//        WritableFont fontLegend = new WritableFont(WritableFont.TAHOMA, 8, WritableFont.BOLD);
//        WritableCellFormat formatLegend = new WritableCellFormat(fontLegend);
//        formatLegend.setAlignment(Alignment.CENTRE);
//        formatLegend.setVerticalAlignment(VerticalAlignment.CENTRE);
        //Add Titles
        Label labelJXL;
        CellView rowView;
        CellView colView;
        int startRow = 0;
        for (int k = 0; k <= userMusters.length; k += recordSPerPage) {
            int startCol = 0;
//            //title1
//            rowView = new CellView();
//            rowView.setSize(20*20);//Row Height
//            sheet.setRowView(startRow, rowView);
//            sheet.mergeCells(0, startRow, totalColumns-1, startRow);
//            labelJXL = new Label(startCol, startRow, title, formatTitle1);
//            sheet.addCell(labelJXL);
//            startRow++;
//            //title2
//            rowView = new CellView();
//            rowView.setSize(14*20);//Row Height
//            sheet.setRowView(startRow, rowView);
//            sheet.mergeCells(0, startRow, totalColumns-1, startRow);
//            labelJXL = new Label(startCol, startRow, title2, formatTitle2);
//            sheet.addCell(labelJXL);
//            startRow++;
            //************** Header **********************************/
            //set row heights of 2 header rows
            rowView = new CellView();
            rowView.setSize(30 * 20);//set row heights in points
            sheet.setRowView(startRow, rowView);
            rowView = new CellView();
            rowView.setSize(40 * 20);//set row heights in points
            sheet.setRowView(startRow + 1, rowView);
            startCol = 0;
            //EMP_NO
            colView = new CellView();
            colView.setAutosize(false);
            colView.setSize(50 * 20);//set col width in points
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "EmpNo", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //NAME
            colView = new CellView();
            colView.setAutosize(false);
            colView.setSize(120 * 20);//set col width in points
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "Name", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //Labels
            colView = new CellView();
            colView.setAutosize(false);
            colView.setSize(52 * 20);//set col width in points
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "Hrs Worked", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //Shift Pattern
            colView = new CellView();
            colView.setAutosize(false);
            colView.setSize(35 * 20);//set col width in points
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "Shift Pattern", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //Header - Weekday, Date
            for (int col = 0; col < numDays; col++) {
                colView = new CellView();
                colView.setAutosize(false);
                colView.setSize(52 * 20);//set col width in points
                sheet.setColumnView(startCol, colView);
                calendar.set(Calendar.DATE, col + 1);
                df = new SimpleDateFormat("MMM dd");//MMM dd, yyyy
                labelJXL = new Label(startCol, startRow, df.format(calendar.getTime()), formatHeader);
                sheet.addCell(labelJXL);
                df = new SimpleDateFormat("EEE");
                labelJXL = new Label(startCol, startRow + 1, df.format(calendar.getTime()), formatHeader);
                sheet.addCell(labelJXL);
                startCol++;
            }
            //Header - Last 5 clomuns
            colView = new CellView();
            colView.setAutosize(false);
            colView.setSize(30 * 20);//set col width in points
            //No of Late Arrival
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "No. Late Arrival", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //No of Early Departure
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "No. Early Depart.", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //No of Leaves
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "No. of Leaves", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //No of Absents
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "No. Absents", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //Average Working Hrs
            colView = new CellView();
            colView.setAutosize(false);
            colView.setSize(55 * 20);//set col width in points
            sheet.setColumnView(startCol, colView);
            sheet.mergeCells(startCol, startRow, startCol, startRow + 1);
            labelJXL = new Label(startCol, startRow, "Total Working Hrs", formatHeader);
            sheet.addCell(labelJXL);
            startCol++;
            //************** Data ********************************/
            startRow += 2;
            boolean dark = true;
            for (int row = k; row < userMusters.length && row < (k + recordSPerPage); row++) {
                WritableCellFormat formatBlack;
                WritableCellFormat formatBlackVertical;
                WritableCellFormat formatRed;
                if (dark) {
                    formatBlack = formatDataBlackDark;
                    formatBlackVertical = formatDataBlackDarkVertical;
                    formatRed = formatDataRedDark;
                    dark = false;
                } else {
                    formatBlack = formatDataBlackWhite;
                    formatBlackVertical = formatDataBlackWhiteVertical;
                    formatRed = formatDataRedWhite;
                    dark = true;
                }
                //set row heights of three rows
                rowView = new CellView();
                rowView.setSize(11 * 20);
                sheet.setRowView(startRow, rowView);
                sheet.setRowView(startRow + 1, rowView);
                sheet.setRowView(startRow + 2, rowView);
                startCol = 0;
                //empno
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, userMusters[row].user.userID, formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                //name
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, userMusters[row].user.name, formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                //labels
                labelJXL = new Label(startCol, startRow, "IN", formatBlack);
                sheet.addCell(labelJXL);
                labelJXL = new Label(startCol, startRow + 1, "OUT", formatBlack);
                sheet.addCell(labelJXL);
                labelJXL = new Label(startCol, startRow + 2, "WH", formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                //shift pattern
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, userMusters[row].user.shiftPattern, formatBlackVertical);
                sheet.addCell(labelJXL);
                startCol++;
                //add Attendaces days
                for (int col = 0; col < numDays; col++) {
                    //IN
                    if (userMusters[row].attendance[col].lateArrival || userMusters[row].attendance[col].inMissing) {
                        labelJXL = new Label(startCol + col, startRow, userMusters[row].attendance[col].inTextExcel, formatRed);
                    } else {
                        labelJXL = new Label(startCol + col, startRow, userMusters[row].attendance[col].inTextExcel, formatBlack);
                    }
                    sheet.addCell(labelJXL);
                    //OUT
                    if (userMusters[row].attendance[col].earlyDeparture || userMusters[row].attendance[col].outMissing) {
                        labelJXL = new Label(startCol + col, startRow + 1, userMusters[row].attendance[col].outTextExcel, formatRed);
                    } else {
                        labelJXL = new Label(startCol + col, startRow + 1, userMusters[row].attendance[col].outTextExcel, formatBlack);
                    }
                    sheet.addCell(labelJXL);
                    //MH
                    if (userMusters[row].attendance[col].isAbscent()) {
                        labelJXL = new Label(startCol + col, startRow + 2, userMusters[row].attendance[col].hrsWorkedExcel, formatRed);
                    } else {
                        labelJXL = new Label(startCol + col, startRow + 2, userMusters[row].attendance[col].hrsWorkedExcel, formatBlack);
                    }
                    sheet.addCell(labelJXL);
                }
                startCol += numDays;
                //numLate
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, String.valueOf(userMusters[row].user.numLate), formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                //numEarly
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, String.valueOf(userMusters[row].user.numEarly), formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                //numLeave
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, String.valueOf(userMusters[row].user.numLeave), formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                //numAbsent
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, String.valueOf(userMusters[row].user.numAbsent), formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                //avgHrsWrk
                sheet.mergeCells(startCol, startRow, startCol, startRow + 2);
                labelJXL = new Label(startCol, startRow, userMusters[row].user.getAvgHrsWrk(), formatBlack);
                sheet.addCell(labelJXL);
                startCol++;
                startRow += 3;
            }
//            //legend
//            rowView = new CellView();
//            rowView.setSize(14*20);
//            sheet.setRowView(startRow, rowView);
//            sheet.mergeCells(0, startRow, totalColumns-1, startRow);
//            labelJXL = new Label(0, startRow, legend, formatLegend);
//            sheet.addCell(labelJXL);
//            startRow++;
            sheet.addRowPageBreak(startRow);
            sheet.addColumnPageBreak(totalColumns);
        }
        //Save and close workbook
        workbook.write();
        workbook.close();
        MyLog.showInfoMsg("Muster saved at:\n" + file.getAbsolutePath());
    }

    /**
     *
     * @param tableModel
     * @param file
     * @throws Exception
     */
    public static void exportJTable(TableModel tableModel, File file) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            String name = file.getName();
            WritableSheet sheet = workbook.createSheet(name.substring(0, name.indexOf(".")), 0);
            //Header
            CellView colView = new CellView();
            colView.setAutosize(true);
            CellView rowView = new CellView();
            rowView.setSize(300);
            WritableFont fontHeader = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat formatHeader = new WritableCellFormat(fontHeader);
            Label label;
            //sheet.setRowView(row+1, rowView);
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                sheet.setColumnView(col, colView);
                label = new Label(col, 0, (String) tableModel.getColumnName(col), formatHeader);
                sheet.addCell(label);
            }
            //Data
            WritableFont fontData = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat formatData = new WritableCellFormat(fontData);
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                //sheet.setRowView(row+1, rowView);
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object obj = tableModel.getValueAt(row, col);
                    String text = "";
                    if (obj instanceof JCheckBox) {
                        text = ((JCheckBox) obj).isSelected() ? "True" : "False";
                    } else {
                        text = obj.toString();
                    }
                    label = new Label(col, row + 1, text, formatData);
                    sheet.addCell(label);
                }
            }
            workbook.write();
            workbook.close();
            MyLog.showInfoMsg("Report saved at:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            MyUtils.showMessage("Export report to Excel : " + e);
        }
    }
    public static void exportJTable(TableModel tableModel, File file, String title, String division, int pageorient) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            String name = file.getName();
            WritableSheet sheet = workbook.createSheet(name.substring(0, name.indexOf(".")), 0);

            SheetSettings s = sheet.getSettings();
            s.setPaperSize(PaperSize.A4);
            if (pageorient == 1) {
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

            HeaderFooter header = new HeaderFooter();
            Contents c = header.getLeft();
            c.setFontName("TAHOMA");
            c.setFontSize(8);
            c.toggleBold();
            c.append(title);
            sheet.getSettings().setHeader(header);
            HeaderFooter footer = new HeaderFooter();
            c = footer.getLeft();
            c.setFontName("TAHOMA");
            c.setFontSize(8);
            c = footer.getRight();
            c.setFontName("TAHOMA");
            c.setFontSize(8);
            //c.toggleBold();
            c.append("On ");
            c.appendDate();
            c.append(" Page ");
            footer.getRight().appendPageNumber();
            footer.getRight().append(" of ");
            footer.getRight().appendTotalPages();
            sheet.getSettings().setFooter(footer);

            //Header
            CellView colView = new CellView();
            colView.setAutosize(true);
            CellView rowView = new CellView();
            rowView.setSize(300);
            WritableFont fontHeader1 = new WritableFont(WritableFont.TIMES, 15, WritableFont.BOLD);
            fontHeader1.setColour(Colour.BLUE);
            WritableCellFormat formatHeader1 = new WritableCellFormat(fontHeader1);
            formatHeader1.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatHeader1.setAlignment(Alignment.CENTRE);

            sheet.mergeCells(0, 0, tableModel.getColumnCount() - 1, 1);
            Label labelJXL = new Label(0, 0, title, formatHeader1);
            sheet.addCell(labelJXL);

            int columns = tableModel.getColumnCount() - 1;
            sheet.mergeCells(0, 2, columns / 2, 2);
            sheet.mergeCells((columns / 2) + 1, 2, columns, 2);
            WritableFont fontHeader2 = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
            fontHeader2.setColour(Colour.BLUE2);
            WritableCellFormat formatHeader2 = new WritableCellFormat(fontHeader2);
            formatHeader2.setBorder(Border.NONE, BorderLineStyle.NONE);
            formatHeader2.setAlignment(Alignment.LEFT);
            formatHeader2.setVerticalAlignment(VerticalAlignment.CENTRE);
            Label labelJXL1 = new Label(0, 2, "Division : " + division, formatHeader2);
            sheet.addCell(labelJXL1);

            WritableCellFormat formatHeader3 = new WritableCellFormat(fontHeader2);
            formatHeader3.setBorder(Border.NONE, BorderLineStyle.NONE);
            formatHeader3.setAlignment(Alignment.LEFT);
            formatHeader3.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatHeader3.setAlignment(Alignment.RIGHT);
            Label labelJXL2 = new Label((columns / 2) + 1, 2, "Total Records : " + tableModel.getRowCount(), formatHeader3);
            sheet.addCell(labelJXL2);

            //sheet.setRowView(row+1, rowView);
            WritableFont fontHeader = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
            WritableCellFormat formatHeader = new WritableCellFormat(fontHeader);
            formatHeader.setBackground(Colour.LIGHT_GREEN);
            formatHeader.setAlignment(Alignment.CENTRE);
            formatHeader.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatHeader.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);

            Label label;
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                colView.setAutosize(true);
                sheet.setColumnView(col, colView);
                label = new Label(col, 3, (String) tableModel.getColumnName(col), formatHeader);
                sheet.addCell(label);
            }
            //Data
            WritableFont fontData = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
            WritableCellFormat formatData = new WritableCellFormat(fontData);
            formatData.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatData.setAlignment(Alignment.CENTRE);
            formatData.setWrap(true);

            for (int row = 3; row < tableModel.getRowCount(); row++) {
                //sheet.setRowView(row+1, rowView);
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object obj = tableModel.getValueAt(row, col);
                    String text = "";
                    if (obj instanceof JCheckBox) {
                        text = ((JCheckBox) obj).isSelected() ? "True" : "False";
                    } else {
                        text = obj.toString();
                    }
                    label = new Label(col, row + 1, text, formatData);
                    sheet.addCell(label);
                }
            }
            workbook.write();
            workbook.close();
            MyLog.showInfoMsg("Report saved at:\n" + file.getAbsolutePath());
        } catch (IOException ex) {
            MyUtils.showMessage("Export report to Excel : " + ex);
        } catch (Exception e) {
            MyUtils.showMessage("Export report to Excel : " + e);
        }
    }
}
