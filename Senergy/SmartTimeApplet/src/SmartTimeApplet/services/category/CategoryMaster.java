package SmartTimeApplet.services.category;

/**
 *
 * @author Chittaranjan Moolya
 */
import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

public class CategoryMaster extends MyPanel {

    private MyTextField txtCatCode, txtCategName;
    private MyButton bAdd, bCancel;
    private JSpinner MinOTLIMIT, MinCompLIMIT, MinGraceLate, MinGraceEarly;
    private JCheckBox ChkOT, ChkCompensatory;
    private Category CategoryApplet;
    private String CatCode;
    private String Job;

    public CategoryMaster(Category CategoryApplet, String Job, String CatCode) {
        try {
            this.CategoryApplet = CategoryApplet;
            this.Job = Job;
            this.CatCode = CatCode;

            addCategoryPanel();
            if (Job.equals("update")) {

                FillForm();

            }
        } catch (Exception ex) {
            MyUtils.showException("Category Master", ex);
        }


    }

    private void FillForm() throws Exception {

        txtCatCode.setEditable(false);

        MyHTTP myHTTP = MyUtils.createServletConnection("CategoryFormServlet");
        myHTTP.openOS();
        myHTTP.println("FormDetails");
        myHTTP.println(CatCode);
        myHTTP.closeOS();
        myHTTP.openIS();
        String result = myHTTP.readLine();
        myHTTP.closeIS();
        if (result.startsWith("ERROR")) {
            MyUtils.showException("Database Query", new Exception(result));
        } else {
            Depacketizer dp = new Depacketizer(result);
            txtCatCode.setText(dp.getString());
            txtCategName.setText(dp.getString());
            ChkOT.setSelected(biz(dp.getString()));

            MinOTLIMIT.setValue(getTime(dp.getString()));

            ChkCompensatory.setSelected(biz(dp.getString()));
            MinCompLIMIT.setValue(getTime(dp.getString()));
            MinGraceLate.setValue(getTime(dp.getString()));
            dp.getString();
            MinGraceEarly.setValue(getTime(dp.getString()));
        }
    }

    private String foo(boolean a) {
        if (a) {
            return "1";
        } else {
            return "0";
        }
    }

    private boolean biz(String a) {
        if (a.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    private void addCategoryPanel() {
        setLayout(new BorderLayout());
        MyPanel MainPanel = new MyPanel(new BorderLayout());

        MyPanel StatusBarPanel = new MyPanel(new FlowLayout());
        StatusBarPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        MyLabel lblStatusbar = new MyLabel();

        StatusBarPanel.add(lblStatusbar);
        MainPanel.add(StatusBarPanel, BorderLayout.SOUTH);


        MyPanel CategoryPanel = new MyPanel();
        CategoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        CategoryPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        MyLabel lblCategoryCode = new MyLabel(1, "Category Code :");

        CategoryPanel.add(lblCategoryCode);

        txtCatCode = new MyTextField();
        txtCatCode.setToolTipText("Enter Category Code");
        txtCatCode.setPreferredSize(new Dimension(50, 20));
        CategoryPanel.add(txtCatCode);

//        MyLabel SpaceLabel = new MyLabel();
//        SpaceLabel.setPreferredSize(new Dimension(200, 15));
//        CategoryPanel.add(SpaceLabel);

        MyLabel lblCategoryName = new MyLabel(1, "Category Name :");
        CategoryPanel.add(lblCategoryName);

        txtCategName = new MyTextField();
        txtCategName.setToolTipText("Enter Category Name");
        txtCategName.setPreferredSize(new Dimension(100,20));
        CategoryPanel.add(txtCategName);

        MainPanel.add(CategoryPanel, BorderLayout.NORTH);

        MyPanel ButtonGracetmPanel = new MyPanel(new BorderLayout());
        MyPanel ButtonPanel = new MyPanel(new FlowLayout());
        ButtonPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        if (Job.equals("add")) {
            bAdd = new MyButton("Add Category", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    addCategory();
                }
            };
        } else if (Job.equals("update")) {
            bAdd = new MyButton("Update Category", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    updateCategory();
                }
            };
        }
        bAdd.setToolTipText("Add New Category");
        ButtonPanel.add(bAdd);



        bCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
                CategoryApplet.showHomePanel();
            }
        };
        bCancel.setToolTipText("Cancel Process");
        ButtonPanel.add(bCancel);



        ButtonGracetmPanel.add(ButtonPanel, BorderLayout.SOUTH);


        MyPanel Psecondlast = new MyPanel(new GridLayout(2, 1, 50, 0));

        MyPanel POT = new MyPanel(new GridLayout(1, 2, 0, 50));

        MyPanel OT = new MyPanel(new GridLayout(2, 1));
        OT.setBorder(BorderFactory.createLineBorder(Color.black));
        MyPanel PCheckBox = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        ChkOT = new JCheckBox("Over Time");
        PCheckBox.add(ChkOT);

        OT.add(PCheckBox);

        MyPanel PMinOT = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblMinOTLimit = new MyLabel(1, " Min OT Limit  :");
        PMinOT.add(lblMinOTLimit);

        MinOTLIMIT = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(MinOTLIMIT, "HH:mm:ss");
        timeEditor.setForeground(new Color(103, 213, 83));
        MinOTLIMIT.setEditor(timeEditor);
        MinOTLIMIT.setValue(new Date()); // will only show the current time

        PMinOT.add(MinOTLIMIT);

        OT.add(PMinOT);

        POT.add(OT);

        /*
         *
         * For Compenstatory Off Panel
         *
         */

        MyPanel PCompensatory = new MyPanel(new GridLayout(2, 1));
        PCompensatory.setBorder(BorderFactory.createLineBorder(Color.black));
        MyPanel PCompensatory1 = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        ChkCompensatory = new JCheckBox("Compensatory off");
        PCompensatory1.add(ChkCompensatory);

        PCompensatory.add(PCompensatory1);

        MyPanel PMinCompen = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblMinComLimit = new MyLabel(1, "Min Compensatory Limit:");
        PMinCompen.add(lblMinComLimit);

        MinCompLIMIT = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor ComtimeEditor = new JSpinner.DateEditor(MinCompLIMIT, "HH:mm:ss");
        ComtimeEditor.setForeground(new Color(103, 213, 83));
        MinCompLIMIT.setEditor(ComtimeEditor);
        MinCompLIMIT.setValue(new Date()); // will only show the current time
        PMinCompen.add(MinCompLIMIT);

        PCompensatory.add(PMinCompen);

        POT.add(PCompensatory);
        //    OT.add(PMinOT);     
        Psecondlast.add(POT);

        /*
         *
         *
         * For Grace Early and Grace Late Panel
         *
         */

        MyPanel PGraceLateEarly = new MyPanel(new GridLayout(3, 2));
//        PGraceLateEarly.setPreferredSize(new Dimension(10, 10));


        PGraceLateEarly.setBorder(BorderFactory.createLineBorder(Color.black));


        MyPanel PSpace1 = new MyPanel();
        PGraceLateEarly.add(PSpace1);

        MyPanel PSpace2 = new MyPanel();
        PGraceLateEarly.add(PSpace2);

        //Grace Late Panel

        MyPanel PGraceLate = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblGraceLate = new MyLabel(1, "Grace Late Time:");
        PGraceLate.add(lblGraceLate);

        MinGraceLate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor GraceLateEditor = new JSpinner.DateEditor(MinGraceLate, "HH:mm:ss");
        GraceLateEditor.setForeground(new Color(103, 213, 83));
        MinGraceLate.setEditor(GraceLateEditor);
        MinGraceLate.setValue(new Date()); // will only show the current time
        PGraceLate.add(MinGraceLate);

        PGraceLateEarly.add(PGraceLate);
        //Grace Early Panel
        MyPanel PGraceEarly = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblGraceEarly = new MyLabel(1, "     Grace Early Time :");
        PGraceEarly.add(lblGraceEarly);

        MinGraceEarly = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor GraceEarlyEditor = new JSpinner.DateEditor(MinGraceEarly, "HH:mm:ss");
        GraceEarlyEditor.setForeground(new Color(103, 213, 83));
        MinGraceEarly.setEditor(GraceEarlyEditor);
        MinGraceEarly.setValue(new Date()); // will only show the current time
        PGraceEarly.add(MinGraceEarly);

        PGraceLateEarly.add(PGraceEarly);


        MyPanel PSpace3 = new MyPanel();
        PGraceLateEarly.add(PSpace3);

        MyPanel PSpace4 = new MyPanel();
        PGraceLateEarly.add(PSpace4);

        Psecondlast.add(PGraceLateEarly);
        ButtonGracetmPanel.add(Psecondlast, BorderLayout.NORTH);

        MainPanel.add(ButtonGracetmPanel, BorderLayout.CENTER);

        this.setLayout(new GridLayout(1, 1));
        this.add(MainPanel, BorderLayout.CENTER);

    }

    private void updateCategory() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("CategoryFormServlet");
                    // MyUtils.showMessage("in the update");
                    myHTTP.openOS();
                    myHTTP.println("Update");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showException("Database ERROR", new Exception(result));
                    } else {
                        CategoryApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Updateserverlet", ex);

                }
            }
        }
    }

    private void addCategory() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("CategoryFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddCategory");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        CategoryApplet.showHomePanel();
                        //loadPage("services/category.jsp","_self");

                    }
                } catch (Exception ex) {
                    MyUtils.showException("AddCategory", ex);
                }
            }
        }
    }

    private String CreatePacket() {
        try {
            Packetizer a = new Packetizer();
            a.addString(txtCatCode.getText());
            a.addString(txtCategName.getText());
            a.addString(foo(ChkOT.isSelected()));
            a.addString(StringTime(MinOTLIMIT.getValue().toString()));
            a.addString(foo(ChkCompensatory.isSelected()));
            a.addString(StringTime(MinCompLIMIT.getValue().toString()));
            a.addString(StringTime(MinGraceLate.getValue().toString()));
            a.addString(StringTime(MinGraceEarly.getValue().toString()));

            return a.getPacket();

        } catch (Exception ex) {
            MyUtils.showException("Create Packet", ex);
            return "PacketFail";
        }
    }

    private String StringTime(String DateTime) {
        String[] ret = DateTime.split(" ");
        return ret[3];
    }

    private Date getTime(String time) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Integer.parseInt(time.substring(0, 2)),
                Integer.parseInt(time.substring(3, 5)), Integer.parseInt(time.substring(6, 8)));
        return cal.getTime();
    }

    private boolean FormFilled() {
        if (txtCatCode.getText().equals("") || txtCategName.getText().equals("")) {
            MyUtils.showMessage("Form not filled");
            return false;
        }
        return true;
    }
}
