/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Common;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class ReportTableModel extends MyTableModel {

    private ReportClass[] RptData;

    public ReportTableModel(TupleClass[] tpl) throws Exception {
        for (int i = 0; i < tpl.length; i++) {

            switch (i) {
                case 0:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[0];
                        }
                    });
                    break;
                case 1:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[1];
                        }
                    });
                    break;
                case 2:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[2];
                        }
                    });
                    break;
                case 3:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[3];
                        }
                    });
                    break;
                case 4:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[4];
                        }
                    });
                    break;
                case 5:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[5];
                        }
                    });
                    break;
                case 6:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[6];
                        }
                    });
                    break;
                case 7:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[7];
                        }
                    });
                    break;
                case 8:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[8];
                        }
                    });
                    break;
                case 9:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
                case 10:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
                case 11:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
                case 12:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
                case 13:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
                case 14:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
                case 15:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
                case 16:
                    addColumn(new MyTableColumn(tpl[i].name, tpl[i].width, tpl[i].dataType) {

                        @Override
                        public String getValueAt(int row) {
                            return RptData[row].returnVals[9];
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void exchange(int row1, int row2) {
        ReportClass temp = RptData[row1];
        RptData[row1] = RptData[row2];
        RptData[row2] = temp;
    }

    public void setData(ReportClass[] Absent) {
        rowCount = 0;
        this.RptData = Absent;
        if (Absent != null) {
            rowCount = Absent.length;
        }
        fireTableDataChanged();
    }
}
