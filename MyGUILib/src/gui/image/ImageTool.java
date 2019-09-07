package gui.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageTool {
    private static final String OUTPUT_FORMAT_ID = "jpeg";
    public static byte[] scaleImage(File file, int targetWidth, int targetHeight) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] imageBytes = new byte[fis.available()];
        fis.read(imageBytes);
        fis.close();
        return scaleImage(imageBytes, targetWidth, targetHeight);
    }
    public static byte[] scaleImage(byte[] imageBytes, int w, int h) throws Exception {
        InputStream imageStream = new BufferedInputStream(new ByteArrayInputStream(imageBytes));
        Image image = (Image) ImageIO.read(imageStream);
        int aw = w;
        int ah = h;
        double ratio = (double) aw / (double) ah;
        int iw = image.getWidth(null);
        int ih = image.getHeight(null);
        double imageRatio = (double) iw / (double) ih;
        if (ratio < imageRatio) {
            ah = (int) (aw / imageRatio);
        } else {
            aw = (int) (ah * imageRatio);
        }
        // Draw the scaled image
        BufferedImage thumbImage = new BufferedImage(aw, ah, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // White Background
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fill(new Rectangle2D.Double(0, 0, w,h));
        graphics2D.fillRect(0, 0, w, h);
        // insert the resized thumbnail between X and Y of the image 
        graphics2D.drawImage(image, 0, 0, aw, ah, null);
        // Write the scaled image to the outputstream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(thumbImage, OUTPUT_FORMAT_ID, out);
        return out.toByteArray();
    }
}