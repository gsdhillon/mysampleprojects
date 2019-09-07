package dataformat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;


/**
 *
 * @author gsdhillon@gmail.com
 */
public class FluenceIZIX12ParamPanel extends JPanel{
    
        /**
      
                   ZONAL OUTPUT GRID: NON-ROTATED
                   ******************************
                   
             Te-: total electron fluence/source particle(incl. secondaries)   
             E1 : average total electron energy                               
             Tph: total photon fluence/source particle(incl. secondaries)     
             E2 : average total photon energy                                 
             Te+: total positron fluence/source particle(incl. secondaries)   
             E3 : average total positron energy                               
             Pe-: primary electron fluence                                    
             E4 : average primary electron energy                             
             Pph: primary photon fluence                                      
             E5 : average primary photon energy                               
             Pe+: primary positron fluence                                    
             E6 : average primary positron energy 
     * 
     */
    private final class IZIX{
        int iz;
        int ix;
        //1  Te-
        String teMinus;
        String teMinusP; //precision +/- %
        //2  E1
        String e1;
        String e1P;
        //3 Tph
        String tph;
        String tphP;
        //4 E2
        String e2;
        String e2P;
        //5 Te+
        String tePlus;
        String tePlusP;
        //6 E3
        String e3;
        String e3P;
        //7 Pe-
        String peMinus;
        String peMinusP;
        //8 E4
        String e4;
        String e4P;
        //9 Pph
        String pph;
        String pphP;
        //10 E5
        String e5;
        String e5P;
        //11 Pe+
        String pePlus;
        String pePlusP;
        //12 E6
        String e6;
        String e6P;
        
        static final String headerH = "IZ, Param";
        private String getIXH() {
            return "IX="+ix + ", " + "+/-";
        }
        
        String get(int paramNo, boolean isHead) throws Exception{
            switch (paramNo) {
                case 1:
                    return isHead ? (iz + ", " + "Te-") : (teMinus + ", " + teMinusP);
                case 2:
                    return isHead ? (iz + ", " + "E1") : (e1 + ", " + e1P);
                case 3:
                    return isHead ? (iz + ", " + "Tph") : (tph + ", " + tphP);
                case 4:
                    return isHead ? (iz + ", " + "E2") : (e2 + ", " + e2P);
                case 5:
                    return isHead ? (iz + ", " + "Te+") : (tePlus + ", " + tePlusP);
                case 6:
                    return isHead ? (iz + ", " + "E3-") : (e3 + ", " + e3P);
                case 7:
                    return isHead ? (iz + ", " + "Pe-") : (peMinus + ", " + peMinusP);
                case 8:
                    return isHead ? (iz + ", " + "E4") : (e4 + ", " + e4P);
                case 9:
                    return isHead ? (iz + ", " + "Pph") : (pph + ", " + pphP);
                case 10:
                    return isHead ? (iz + ", " + "E5") : (e5 + ", " + e5P);
                case 11:
                    return isHead ? (iz + ", " + "Pe+") : (pePlus + ", " + pePlusP);
                case 12:
                    return isHead ? (iz + ", " + "E6") : (e6 + ", " + e6P);

                default:
                    throw new Exception("Get Param Failed! ParamNo. " + paramNo + " undefined.");
            }
        }

        void readParamWithPreceision(int paramNo, String line, int off) throws Exception {
            switch (paramNo) {
                case 1:
                    teMinus = line.substring(off + 5, off + 15).trim();
                    teMinusP = line.substring(off + 17, off + 22).trim();
                    break;
                case 2:
                    e1 = line.substring(off + 5, off + 15).trim();
                    e1P = line.substring(off + 17, off + 22).trim();
                    break;
                case 3:
                    tph = line.substring(off + 5, off + 15).trim();
                    tphP = line.substring(off + 17, off + 22).trim();
                    break;
                case 4:
                    e2 = line.substring(off + 5, off + 15).trim();
                    e2P = line.substring(off + 17, off + 22).trim();
                    break;
                case 5:
                    tePlus = line.substring(off + 5, off + 15).trim();
                    tePlusP = line.substring(off + 17, off + 22).trim();
                    break;
                case 6:
                    e3 = line.substring(off + 5, off + 15).trim();
                    e3P = line.substring(off + 17, off + 22).trim();
                    break;
                case 7:
                    peMinus = line.substring(off + 5, off + 15).trim();
                    peMinusP = line.substring(off + 17, off + 22).trim();
                    break;
                case 8:
                    e4 = line.substring(off + 5, off + 15).trim();
                    e4P = line.substring(off + 17, off + 22).trim();
                    break;
                case 9:
                    pph = line.substring(off + 5, off + 15).trim();
                    pphP = line.substring(off + 17, off + 22).trim();
                    break;
                case 10:
                    e5 = line.substring(off + 5, off + 15).trim();
                    e5P = line.substring(off + 17, off + 22).trim();
                    break;
                case 11:
                    pePlus = line.substring(off + 5, off + 15).trim();
                    pePlusP = line.substring(off + 17, off + 22).trim();
                    break;
                case 12:
                    e6 = line.substring(off + 5, off + 15).trim();
                    e6P = line.substring(off + 17, off + 22).trim();
                    break;
                default:
                    throw new Exception("Read Param Failed! ParamNo. " + paramNo + " undefined.");
            }
        }
        
    }
    
    int lineNo = 1;
    String line = "";

    public FluenceIZIX12ParamPanel(){
        setBorder(BorderFactory.createTitledBorder("Fluence (IZ - IX) 12 params  Te-, E1, Tph, E2, Te+, E3, Pe-, E4, Pph, E5, Pe+, E6"));
        int width = 800;  int height = 160;
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        //
        final JTextField inFileTxt = new JTextField(FileUtil.homePath +
                "Fluence_for_1_by_1cm2_Bone_cortical_6MV.egslst"
        );
        final JTextField outFileTxt = new JTextField(FileUtil.homePath + 
                "Fluence_for_1_by_1cm2_Bone_cortical_6MV.csv"
        );
        
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
        final JTextField fromLineNoTxt = new JTextField("480");
        p.add(fromLineNoTxt);
        p.add(new JLabel("Line No. Upto: "));
        final JTextField uptoLineNoTxt = new JTextField("1419");
        p.add(uptoLineNoTxt);
        b = new JButton("Do Format");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doFormat(inFileTxt.getText(), outFileTxt.getText(), fromLineNoTxt.getText(), uptoLineNoTxt.getText());
            }
        });
        p.add(b, BorderLayout.EAST);
    
        //
        add(p);
        setPreferredSize(new Dimension(width+40, height));
    }
    
    /**
     * 
     * @param inFilePath
     * @param outFilePath
     * @param lineFrom 
     */
    @SuppressWarnings({"CallToThreadDumpStack", "UseSpecificCatch"})
    private void doFormat(String inFilePath, String outFilePath, String lineFrom, String lineUpto) {
        try{
            File inFile = new File(inFilePath);
            File outFile = new File(outFilePath);
            int fromLine = Integer.parseInt(lineFrom.trim());
            int uptoLine = Integer.parseInt(lineUpto.trim());
            
            //
        //    List<String> listLines = new ArrayList<>();
            Map<Integer, List<IZIX>> irlMap = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
                //go to fromLine
                lineNo = 1;
                while(lineNo<fromLine){
                    br.readLine();lineNo++;
                }
                line = findNextIRL(br, uptoLine);
                //
                while(line != null){
//                    count++;
//                    listLines.add(line);
                    IZIX[] irls = readIzIxAndParamValues(br, line);
                    if(irlMap.get(irls[0].iz) == null){
                        irlMap.put(irls[0].iz, new ArrayList<IZIX>());
                    }
                    List<IZIX> irlList = irlMap.get(irls[0].iz);
                    irlList.addAll(Arrays.asList(irls));
                    //
                    line = findNextIRL(br, uptoLine);
                }
            }
            
            
            //
            int izCount = 0;
            int ixCount = 0;
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))){
                Collection<List<IZIX>> irlListOfList = irlMap.values();
                
                //
                boolean headerLineAdded = false;
                String headerLine = IZIX.headerH;
                //all IZ values and multiple IX values each
                for(List<IZIX> irlList : irlListOfList){
                    izCount++;
                    //make 12 lines of text 
                    String[] parmLines = new String[13];
                    boolean paraHeadAdded = false;
                    //all IX vlaues
                    ixCount = 0;
                    for(IZIX irl: irlList){
                        ixCount ++;
                        //make header line
                        if(!headerLineAdded){
                            headerLine += /* ", IX" */ ", " + irl.getIXH();
                        }
      
                        //add param head to each line
                        if(!paraHeadAdded){
                            for(int paramNo=1; paramNo<=12; paramNo++){
                                parmLines[paramNo] = irl.get(paramNo, true);
                            }
                            paraHeadAdded = true;
                        }
      
                        //add corresponding param values to each line for this IX
                        for(int paramNo=1; paramNo<=12; paramNo++){
                            parmLines[paramNo]  += /* ","+ irl.ix + */ ", " + irl.get(paramNo, false);
                        }
                    }
                    
                    //write 12 lines of text to output file
                    if(!headerLineAdded){
                        writer.write(headerLine);writer.newLine();
                        headerLineAdded = true;
                    }
                    for(int paramNo=1; paramNo<=12; paramNo++){
                        writer.write(parmLines[paramNo]);writer.newLine();
                    }
                 }
            }
            JOptionPane.showMessageDialog(this, "IZ="+izCount+"xIX="+ixCount+" with 12 params saved at output file.");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Pricessing input line no "+(lineNo-1)+": '"+line+"'\n Exception-"+e.getMessage());
            JOptionPane.showMessageDialog(this, "Pricessing input line no "+(lineNo-1)+".\n Exception-"+e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * 
     * @param br
     * @return
     * @throws Exception 
     */
    private String findNextIRL(BufferedReader br, int uptoLine) throws Exception {
        do{
            if(lineNo > uptoLine){
                return null;
            }
            line = br.readLine();lineNo++;
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
    private IZIX[] readIzIxAndParamValues(BufferedReader br, String line) throws Exception {
        int n = 0;
        if(line.length() >= 68){
            n = 3;
        }else if(line.length() >= 46){
            n = 2;
        }else if(line.length() >= 22){
            n = 1;
        }else{
            throw new Exception("Not found enough text to extract IRL IZ IX");
        }
        
        IZIX[] irl = new IZIX[n];
        //IZ and IX no
        for(int i=0;i<n;i++){
            irl[i] = new IZIX();
            int off = (i*23);
            irl[i].iz = Integer.parseInt(line.substring(off+10, off+14).trim());
            irl[i].ix = Integer.parseInt(line.substring(off+17, off+21).trim());
        }
        
        //read 12 params with fixed order
        for(int paramNo=1;paramNo<=12;paramNo++){
            line = br.readLine().trim();lineNo++;
            for(int i=0;i<n;i++){
                int off = (i*23);
                irl[i].readParamWithPreceision(paramNo, line, off);
            }
        }

        //return both left and right IRL 
        return irl;
    }
}