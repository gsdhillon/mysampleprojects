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
public class DoseIZIX4ParamPanel extends JPanel{
    public DoseIZIX4ParamPanel(){
        setBorder(BorderFactory.createTitledBorder("Dose Profile (IZ - IX) 4 params  T, T-S, K, D/K"));
        int width = 800;  int height = 160;
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        //
        final JTextField inFileTxt = new JTextField(FileUtil.homePath +
                "Dose_p5cm_by_p5cm_LUNG-inflated_7cm_ICRU_With_PROFILE_plane.egslst"
        );
        final JTextField outFileTxt = new JTextField(FileUtil.homePath + 
                "Dose_p5cm_by_p5cm_LUNG-inflated_7cm_ICRU_With_PROFILE_plane.csv"
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
        final JTextField fromLineNoTxt = new JTextField("3754");
        p.add(fromLineNoTxt);
        p.add(new JLabel("Line No. Upto: "));
        final JTextField uptoLineNoTxt = new JTextField("6815");
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
                   ZONAL OUTPUT GRID: NON-ROTATED
                   ******************************
                   
             T  : Total dose (Gray/incident particle)                         
             T-S: Total dose minus stoppers                                   
             K  : Kerma                                                       
             D/K: Dose to kerma        
     * 
     */
    private final class IZIX{
        int iz;
        int ix;
        String t;
        String tP;
        String ts;
        String tsP;
        String k;
        String kP;
        String dk;
        String dkP;
        String getT() {
            return  t + ", " + tP ;
        }
        String getTS() {
            return  ts + ", " + tsP ;
        }
        String getK() {
            return  k + ", " + kP ;
        }
        String getDK() {
            return  dk+ ", " + dkP ;
        }
        
        static final String headerH = "IZ, Param";
        
        private String getIXH() {
            return "IX="+ix + ", " + "+/-";
        }
        
        String getTH() {
            return iz + ", " + "T";
        }
        String getTSH() {
            return iz + ", " + "TS";
        }
        String getKH() {
            return iz + ", " + "K";
        }
        String getDKH() {
            return iz + ", " + "DK";
        }

       
        
    }
    
    int lineNo = 1;
    String line = "";
    
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
                    IZIX[] irls = readIrlValues(br, line);
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
            int count = 0;
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))){
                Collection<List<IZIX>> irlListOfList = irlMap.values();
                
                //
                boolean headerLineAdded = false;
                
                for(List<IZIX> irlList : irlListOfList){
                    count++;
                    String headerLine = IZIX.headerH;
                    String lineT = null;
                    String lineTS = null;
                    String lineK = null;
                    String lineDK = null;
                    for(IZIX irl: irlList){
                        if(lineT ==null){
                            lineT = irl.getTH();
                            lineTS = irl.getTSH();
                            lineK = irl.getKH();
                            lineDK = irl.getDKH();
                        }
                        headerLine += /* ", IX" */ ", " + irl.getIXH();
                        lineT  += /* ","+ irl.ix + */ ", " + irl.getT();
                        lineTS += /* ","+ irl.ix + */", "+irl.getTS();
                        lineK  += /* ","+ irl.ix + */", "+irl.getK();
                        lineDK += /* ","+ irl.ix + */", "+irl.getDK();
                    }
                    if(!headerLineAdded){
                        writer.write(headerLine);writer.newLine();
                        headerLineAdded=true;
                    }
                    writer.write(lineT);writer.newLine();
                    writer.write(lineTS);writer.newLine();
                    writer.write(lineK);writer.newLine();
                    writer.write(lineDK);writer.newLine();
                 }
            }
            JOptionPane.showMessageDialog(this, "IZ = "+count+" records saved at output file.");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(line);
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
    private IZIX[] readIrlValues(BufferedReader br, String line) throws Exception {
        IZIX[] irl = new IZIX[3];
        //IRL no
        for(int i=0;i<3;i++){
            irl[i] = new IZIX();
            int off = (i*23);
            irl[i].iz = Integer.parseInt(line.substring(off+10, off+14).trim());
            irl[i].ix = Integer.parseInt(line.substring(off+17, off+21).trim());
        }
        //T
        line = br.readLine().trim();lineNo++;
        for(int i=0;i<3;i++){
            int off = (i*23);
            irl[i].t = line.substring(off+5, off+15).trim();
            irl[i].tP = line.substring(off+17, off+22).trim();
        }
        //T-S
        line = br.readLine().trim();lineNo++;
        for(int i=0;i<3;i++){
            int off = (i*23);
            irl[i].ts = line.substring(off+5, off+15).trim();
            irl[i].tsP = line.substring(off+17, off+22).trim();
        }
        //K
        line = br.readLine().trim();lineNo++;
        for(int i=0;i<3;i++){
            int off = (i*23);
            irl[i].k = line.substring(off+5, off+15).trim();
            irl[i].kP = line.substring(off+17, off+22).trim();
        }
        //D/K
        line = br.readLine().trim();lineNo++;
        for(int i=0;i<3;i++){
            int off = (i*23);
            irl[i].dk = line.substring(off+5, off+15).trim();
            irl[i].dkP = line.substring(off+17, off+22).trim();
        }
        
        //return both left and right IRL 
        return irl;
    }
}