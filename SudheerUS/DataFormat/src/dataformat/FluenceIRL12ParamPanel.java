package dataformat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author gsdhillon@gmail.com
 */
public class FluenceIRL12ParamPanel extends JPanel{
    public FluenceIRL12ParamPanel(){
        setBorder(BorderFactory.createTitledBorder("Fluence 12 params  Te-, E1, Tph, E2, Te+, E3, Pe-, E4, Pph, E5, Pe+, E6"));
        int width = 800; int height = 160;
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        //
        final JTextField inFileTxt = new JTextField(FileUtil.homePath + "Fluence_p5cm_by_p5cm_LUNG_inflated_ICRU_With_depth_6MV_Gurmeet.egslst");
        final JTextField outFileTxt = new JTextField(FileUtil.homePath + "Fluence_p5cm_by_p5cm_LUNG_inflated_ICRU_With_depth_6MV_Gurmeet.csv");

        //
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(width, 30));
        p.add(new JLabel("Input file: "), BorderLayout.WEST);
        p.add(inFileTxt, BorderLayout.CENTER);
        JButton b = new JButton("Browse");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileUtil.browse(inFileTxt, outFileTxt);
            }
        });
        p.add(b, BorderLayout.EAST);
        add(p);
        //
        p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(width, 30));
        p.add(new JLabel("Output file: "), BorderLayout.WEST);
        p.add(outFileTxt, BorderLayout.CENTER);
        add(p);
        
        //
        p = new JPanel(new GridLayout(1,5));
        p.setPreferredSize(new Dimension(width, 30));
        p.add(new JLabel("Line No. From: "));
        final JTextField fromLineNoTxt = new JTextField("1114");
        p.add(fromLineNoTxt);
        p.add(new JLabel("First IRL No on Right: "));
        final JTextField firstIRLOnRight = new JTextField("166");
        p.add(firstIRLOnRight);
        b = new JButton("Do Format");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doFormat(inFileTxt.getText(), outFileTxt.getText(), fromLineNoTxt.getText(), firstIRLOnRight.getText());
            }
        });
        p.add(b);
        add(p);
        //
        setPreferredSize(new Dimension(width+40, height));
    }
    
    /**
     * 
     */
    private class IRL{
        int no;
        String teMinus;
        String teMinusP;
        String e1;
        String e1P;
        String tph;
        String tphP;
        String e2;
        String e2P;
        String tePlus;
        String tePlusP;
        String e3;
        String e3P;
        String peMinus;
        String peMinusP;
        String e4;
        String e4P;
        String pph;
        String pphP;
        String e5;
        String e5P;
        String pePlus;
        String pePlusP;
        String e6;
        String e6P;

        public String getCsv() {
//            System.out.println("IRL{" + "no=" + no + ", t=" + t + ", ts=" + ts + ", k=" + k + ", dk=" + dk + '}');
            return no + ", " 
                    + teMinus + ", " + teMinusP + ", " 
                    + e1 + ", " + e1P + ", " 
                    + tph + ", " + tphP + ", " 
                    + e2+ ", " + e2P + ", " 
                    + tePlus + ", " + tePlusP + ", " 
                    + e3 + ", " + e3P + ", " 
                    + peMinus + ", " + peMinusP + ", " 
                    + e4+ ", " + e4P + ", " 
                    + pph + ", " + pphP + ", " 
                    + e5 + ", " + e5P + ", " 
                    + pePlus + ", " + pePlusP + ", " 
                    + e6+ ", " + e6P ;
        }
    }
    /**
     * 
     * @param inFilePath
     * @param outFilePath
     * @param lineFrom
     * @param lastIrlNoOnLeft 
     */
    @SuppressWarnings({"CallToThreadDumpStack", "UseSpecificCatch"})
    private void doFormat(String inFilePath, String outFilePath, String lineFrom, String firstIRLNoOnRight) {
        try{
            File inFile = new File(inFilePath);
            File outFile = new File(outFilePath);
            int fromLine = Integer.parseInt(lineFrom.trim());
            int firstIrlOnRt = Integer.parseInt(firstIRLNoOnRight.trim());
            
            List<IRL> list1 = new ArrayList<>();
            List<IRL> list2 = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
                //go to fromLine
                for(int i=1; i<fromLine;i++){
                    br.readLine();
                }
                String line = findNextIRL(br);
                while(line != null){
                    IRL[] irls = readIrlValues(br, line);
                    list1.add(irls[0]);
                    list2.add(irls[1]);
                    line = findNextIRL(br);
                    //has been read all IRL so break loop
                    //file is too large 
                    if(irls[0].no >= (firstIrlOnRt-1)){
                        break;
                    }
                }
            }
            
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))){
                writer.write("IRL, Te-, +-, E1, +-, Tph, +-, E2, +-, Te+, +-, E3, +-, Pe-, +-, E4, +-, Pph, +-, E5, +-, Pe+, +-, E6, +-");
                writer.newLine();
                for(IRL irl : list1){
                    writer.write(irl.getCsv());
                    writer.newLine();
                }
                for(IRL irl : list2){
                    writer.write(irl.getCsv());
                    writer.newLine();
                }
             }
            
             JOptionPane.showMessageDialog(this, "Saved "+(list1.size()+list2.size())+" records at output file.");
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error-"+e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 
     * @param br
     * @return
     * @throws Exception 
     */
    private String findNextIRL(BufferedReader br) throws Exception {
        String line;
        do{
            line = br.readLine();
            if(line == null){
                return null;
            }
            line = line.trim();
            if(line.startsWith("|IRL")) {
                return line;
            }
        }while(true);
    }
    
    /**
     * 
     * @param br
     * @param line
     * @return
     * @throws Exception 
     */
    private IRL[] readIrlValues(BufferedReader br, String line) throws Exception {
        IRL irlLeft = new IRL();
        IRL irlRight = new IRL();
        //IRL no
        irlLeft.no = Integer.parseInt(line.substring(4, 7).trim());
        irlRight.no = Integer.parseInt(line.substring(27, 30).trim());
        //Te-
        line = br.readLine().trim();
        irlLeft.teMinus = line.substring(5, 15).trim();
        irlLeft.teMinusP = line.substring(17, 22).trim();
        irlRight.teMinus = line.substring(28, 38).trim();
        irlRight.teMinusP = line.substring(40, 45).trim();
        //E1
        line = br.readLine().trim();
        irlLeft.e1 = line.substring(5, 15).trim();
        irlLeft.e1P = line.substring(17, 22).trim();
        irlRight.e1 = line.substring(28, 38).trim();
        irlRight.e1P = line.substring(40, 45).trim();
        //Tph
        line = br.readLine().trim();
        irlLeft.tph = line.substring(5, 15).trim();
        irlLeft.tphP = line.substring(17, 22).trim();
        irlRight.tph = line.substring(28, 38).trim();
        irlRight.tphP = line.substring(40, 45).trim();
        //E2
        line = br.readLine().trim();
        irlLeft.e2 = line.substring(5, 15).trim();
        irlLeft.e2P = line.substring(17, 22).trim();
        irlRight.e2 = line.substring(28, 38).trim();
        irlRight.e2P = line.substring(40, 45).trim();
        //Te+
        line = br.readLine().trim();
        irlLeft.tePlus = line.substring(5, 15).trim();
        irlLeft.tePlusP = line.substring(17, 22).trim();
        irlRight.tePlus = line.substring(28, 38).trim();
        irlRight.tePlusP = line.substring(40, 45).trim();
        //E3
        line = br.readLine().trim();
        irlLeft.e3 = line.substring(5, 15).trim();
        irlLeft.e3P = line.substring(17, 22).trim();
        irlRight.e3 = line.substring(28, 38).trim();
        irlRight.e3P = line.substring(40, 45).trim();
        //Pe-
        line = br.readLine().trim();
        irlLeft.peMinus = line.substring(5, 15).trim();
        irlLeft.peMinusP = line.substring(17, 22).trim();
        irlRight.peMinus = line.substring(28, 38).trim();
        irlRight.peMinusP = line.substring(40, 45).trim();
        //E4
        line = br.readLine().trim();
        irlLeft.e4 = line.substring(5, 15).trim();
        irlLeft.e4P = line.substring(17, 22).trim();
        irlRight.e4 = line.substring(28, 38).trim();
        irlRight.e4P = line.substring(40, 45).trim();
        //Pph
        line = br.readLine().trim();
        irlLeft.pph = line.substring(5, 15).trim();
        irlLeft.pphP = line.substring(17, 22).trim();
        irlRight.pph = line.substring(28, 38).trim();
        irlRight.pphP = line.substring(40, 45).trim();
        //E5
        line = br.readLine().trim();
        irlLeft.e5 = line.substring(5, 15).trim();
        irlLeft.e5P = line.substring(17, 22).trim();
        irlRight.e5 = line.substring(28, 38).trim();
        irlRight.e5P = line.substring(40, 45).trim();
        //Pe+
        line = br.readLine().trim();
        irlLeft.pePlus = line.substring(5, 15).trim();
        irlLeft.pePlusP = line.substring(17, 22).trim();
        irlRight.pePlus = line.substring(28, 38).trim();
        irlRight.pePlusP = line.substring(40, 45).trim();
        //E6
        line = br.readLine().trim();
        irlLeft.e6 = line.substring(5, 15).trim();
        irlLeft.e6P = line.substring(17, 22).trim();
        irlRight.e6 = line.substring(28, 38).trim();
        irlRight.e6P = line.substring(40, 45).trim();
        //return both left and right IRL 
        return new IRL[] {irlLeft, irlRight};
    }
}