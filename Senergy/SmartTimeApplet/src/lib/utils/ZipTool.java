package lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 */
public class ZipTool {
    //private static String pathSep = System.getProperty( "path.separator");// ; on windows
    private static String fileSep = System.getProperty( "file.separator");// \ on windows
    private static byte[] buffer = new byte[2 * 10448576];
    //private static int count;
    /**
     *
     * @param dir
     * @param destDir
     * @return
     * @throws Exception
     */
    public static File zipDirectory(File dir, File destDir) throws Exception{
        if(dir == null || !dir.exists()){
            throw new Exception("DirectoryNotExists-ZipDir");
        }
        if(!dir.isDirectory()){
            throw new Exception("NotADirectory-ZipDir");
        }
        if(!destDir.exists()){
            destDir.mkdirs();
        }
        int parentLen = dir.getParent().length();
        File zipFile = new File(destDir, dir.getName() + ".zip");
        //System.out.println("Creating " + zipFile.getAbsolutePath()+" ...");
        //long t1 = new Date().getTime();
        //count = 0;
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        addToZip(zos, dir, parentLen);
        zos.close();
        return zipFile;
        //long t2 = new Date().getTime();
        //System.out.println(count + " files/dirs added to zipDirectory in " + ((t2 - t1) / 1000) + " seconds.");
    }
    /**
     *
     * @param zos
     * @param file
     * @param parentLen    -  making path from absolute to relative
     * @throws IOException
     */
    private static void addToZip(ZipOutputStream zos, File file, int parentLen) throws Exception {
        //count++;
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            String relativePath = file.getAbsolutePath().substring(parentLen);
            ZipEntry zipEntry = new ZipEntry(relativePath);
            zos.putNextEntry(zipEntry);
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
            //System.out.println(relativePath+" .....File");
        } else {
            String relativePath = file.getAbsolutePath().substring(parentLen)+fileSep;
            ZipEntry zipEntry = new ZipEntry(relativePath);
            zos.putNextEntry(zipEntry);
            //System.out.println(relativePath+" -----Dir");
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                addToZip(zos, f, parentLen);
            }
        }
    }
    /**
     *
     * @param f
     * @throws Exception
     */
    public static File unzip(File f, File destDir) throws Exception {
        if(!destDir.exists()){
            destDir.mkdirs();
        }
        File root = null;
        ZipFile zipFile = new ZipFile(f);
        Enumeration<?> zipEntriesEnum = (Enumeration<?>) zipFile.entries();
        //System.out.println("Extracting "+f.getAbsolutePath()+" ...");
        //long t1 = new Date().getTime();
        //count = 0;
        while (zipEntriesEnum.hasMoreElements()) {
            //count++;
            ZipEntry zipEntry = (ZipEntry) zipEntriesEnum.nextElement();
            String name = zipEntry.getName();
            //long size = (long) (zipEntry.getSize()/1000);
            //long compressedSize = (long)(zipEntry.getCompressedSize()/1000);
            //System.out.printf("name: %-20s | size: %6dkB | compressed size: %6dkB\n", name, size, compressedSize);
            File file = new File(destDir, name);//new File(name);//
            if(root==null){
                root = file;
            }
            if (name.endsWith(fileSep)) {
                //Entry is directory
                file.mkdirs();
                continue;
            }
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            InputStream is = zipFile.getInputStream(zipEntry);
            FileOutputStream fos = new FileOutputStream(file);
            int length;
            while ((length = is.read(buffer)) >= 0) {
                fos.write(buffer, 0, length);
            }
            is.close();
            fos.close();
        }
        //long t2 = new Date().getTime();
        //System.out.println(count + " files/dirs extracted in " + ((t2 - t1) / 1000) + " seconds.");
        zipFile.close();
        return root;
    }
}