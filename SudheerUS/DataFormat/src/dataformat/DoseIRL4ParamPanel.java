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
public class DoseIRL4ParamPanel extends JPanel{
    public DoseIRL4ParamPanel(){
        setBorder(BorderFactory.createTitledBorder("Dose 4 params  T, T-S, K, D/K"));
        int width = 800; int height = 160;
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        //
        final JTextField inFileTxt = new JTextField(FileUtil.homePath + "Dose_2cm_by_2cm_BONE_Compat_6cm_ICRU_With_depth-Gurmeet.egslst");
        final JTextField outFileTxt = new JTextField(FileUtil.homePath + "Dose_2cm_by_2cm_BONE_Compat_6cm_ICRU_With_depth-Gurmeet.csv");
        
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
        final JTextField fromLineNoTxt = new JTextField("1568");
        p.add(fromLineNoTxt);
        p.add(new JLabel("First IRL No on Right: "));
        final JTextField firstIRLOnRight = new JTextField("181");
        p.add(firstIRLOnRight);
        b = new JButton("Do Format");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doFormat(inFileTxt.getText(), outFileTxt.getText(), fromLineNoTxt.getText(), firstIRLOnRight.getText());
            }
        });
        p.add(b, BorderLayout.EAST);
    
        //
        add(p);
        setPreferredSize(new Dimension(width+40, height));
    }
    
    /**
     * 
     */
    private class IRL{
        int no;
        String t;
        String tP;
        String ts;
        String tsP;
        String k;
        String kP;
        String dk;
        String dkP;

        public String getCsv() {
//            System.out.println("IRL{" + "no=" + no + ", t=" + t + ", ts=" + ts + ", k=" + k + ", dk=" + dk + '}');
            return no + ", " 
                    + t + ", " + tP + ", " 
                    + ts + ", " + tsP + ", " 
                    + k + ", " + kP + ", " 
                    + dk+ ", " + dkP ;
        }
    }
    /**
     * 
     * @param inFilePath
     * @param outFilePath
     * @param lineFrom 
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
                writer.write("IRL, T, +-, T-S, +-, K, +-, D/K, +-");
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
        //T
        line = br.readLine().trim();
        irlLeft.t = line.substring(5, 15).trim();
        irlLeft.tP = line.substring(17, 22).trim();
        irlRight.t = line.substring(28, 38).trim();
        irlRight.tP = line.substring(40, 45).trim();
        //T-S
        line = br.readLine().trim();
        irlLeft.ts = line.substring(5, 15).trim();
        irlLeft.tsP = line.substring(17, 22).trim();
        irlRight.ts = line.substring(28, 38).trim();
        irlRight.tsP = line.substring(40, 45).trim();
        //K
        line = br.readLine().trim();
        irlLeft.k = line.substring(5, 15).trim();
        irlLeft.kP = line.substring(17, 22).trim();
        irlRight.k = line.substring(28, 38).trim();
        irlRight.kP = line.substring(40, 45).trim();
        //D/K
        line = br.readLine().trim();
        irlLeft.dk = line.substring(5, 15).trim();
        irlLeft.dkP = line.substring(17, 22).trim();
        irlRight.dk = line.substring(28, 38).trim();
        irlRight.dkP = line.substring(40, 45).trim();
        //return both left and right IRL 
        return new IRL[] {irlLeft, irlRight};
    }
}