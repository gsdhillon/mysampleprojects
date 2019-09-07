package lib.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 */
public class MyFile {
    /**
     * browse for save location folder only
     * @param fileName
     * @return
     * @throws Exception
     */
    public static File chooseSaveDir(final String fileName) throws Exception{
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose save location");
        chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), fileName));
        int returnVal = chooser.showSaveDialog(new JFrame());
        if(returnVal != JFileChooser.APPROVE_OPTION){
            return null;
        }
        File file = chooser.getSelectedFile();
        if(file.exists()){
            if(JOptionPane.NO_OPTION ==
               JOptionPane.showConfirmDialog(new JFrame(),
                    "File "+file.getAbsolutePath()+" is already exists.\n"
                    + "Do you want to replace it?",
                    "",
                    JOptionPane.YES_NO_OPTION)){
                return chooseSaveDir(fileName);
            }
        }
        return file;
    }
    /**
     *
     * @return
     * @throws Exception
     */
    public static File chooseFileForOpen(File defaultDir) throws Exception{
        JFileChooser chooser = new JFileChooser(defaultDir);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int returnVal = chooser.showOpenDialog(new JFrame());
        if(returnVal != JFileChooser.APPROVE_OPTION){
            throw new Exception("OPERATION_CANCELLED");
        }
        return chooser.getSelectedFile();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static File chooseFileForSave(String fileName){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setDialogTitle("Choose save location");
        chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), fileName));
        int returnVal = chooser.showSaveDialog(new JFrame());
        if(returnVal != JFileChooser.APPROVE_OPTION){
            return null;
        }else{
            return chooser.getSelectedFile();
        }
    }
    /**
     * Open default directory
     * @return
     */
    public static File chooseDirForOpen(File defaultDir){
        JFileChooser chooser = new JFileChooser(defaultDir);
        //chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int returnVal = chooser.showOpenDialog(new JFrame());
        if(returnVal != JFileChooser.APPROVE_OPTION){
            return null;
        }
        File dir = chooser.getSelectedFile();
        return dir;
    }
    /**
     * Open default directory
     * @return
     */
    public static File chooseDirForSave(File defaultDir){
        JFileChooser chooser = new JFileChooser(defaultDir);
        //chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = chooser.showOpenDialog(new JFrame());
        if(returnVal != JFileChooser.APPROVE_OPTION){
            return null;
        }
        return chooser.getSelectedFile();
    }
    /**
     *
     * @param file
     * @throws Exception
     */
    public static void openFileInWindows(File file) throws Exception{
        if(!file.exists()){
            throw new Exception("FileDoesNotExists-WindowsRuntime");
        }
        Process p = Runtime.getRuntime().exec(
                "rundll32 SHELL32.DLL, "
            +   "ShellExec_RunDLL "
            +   "\""+file.getAbsolutePath()+"\"");
        p.waitFor();
    }
    /**
     *
     * @param src
     * @param dest
     * @throws Exception
     */
    public static void copyFolder(File src, File dest) throws Exception{
         if(src.isDirectory()){
            //if directory not exists, create it
            if(!dest.exists()){
               dest.mkdir();
               //System.out.showDebugMessage("Directory copied from " + src + "  to " + dest);
            }
            //list all the directory contents
            String files[] = src.list();
            for (String file : files) {
               //construct the src and dest file structure
               File srcFile = new File(src, file);
               File destFile = new File(dest, file);
               //recursive copy
               copyFolder(srcFile,destFile);
            }
    	}else{
            //if file, then copy it
            //Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = in.read(buffer)) > 0){
               out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            //System.out.showDebugMessage("File copied from " + src + " to " + dest);
    	}
    }
    /**
     * 
     * @param file
     */
    public static void showDirectory(File file) throws Exception{
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

    /**
     *
     * @param folder
     */
    public static void createFolder(String folderPath) {
        try{
            File directory = new File(folderPath);
            if(!directory.exists()){
                if(directory.mkdir()){
                    MyLog.showDebugMessage("Folder Created '"+directory.getAbsolutePath()+"'");
                }else{
                    MyLog.showDebugMessage("Could not create directory "+directory.getAbsolutePath());
                }
            }
        }catch(Exception e){
            MyLog.showException(e);
        }
    }
    /**
     * delete all files from temp dir
     */
    public static void deleteDir(File dir) {
        try{
            if (dir.isFile()) {
                dir.delete();
            } else {
                File[] fileList = dir.listFiles();
                for (File f : fileList) {
                    deleteDir(f);
                }
                dir.delete();
            }
        }catch(Exception e){
            System.out.println("Could not delete Temp dir "+e.getMessage());
            //MyLog.logError(e);
        }
    }

    /**
     * name=value each line
     * @param paramFile
     * @param paramName
     * @return
     */
    public static String readParameter(File paramFile, String paramName) throws Exception{
        String record = null;
        String result = null;
        FileInputStream fis = new FileInputStream(paramFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        record = br.readLine();
        while(record != null){
            record = record.trim();
            if(record.startsWith(paramName)){
                result = record.substring((record.indexOf('=')+1)).trim();
                br.close();
                //System.out.showDebugMessage(name+"="+result);
                return result;
            }
            record = br.readLine();
        }
        br.close();
        throw new Exception("NoRecordFound - "+paramName);
    }
}