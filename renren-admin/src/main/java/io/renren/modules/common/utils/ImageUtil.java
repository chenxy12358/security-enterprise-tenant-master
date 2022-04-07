package io.renren.modules.common.utils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author cxy
 * @create 2022/3/24
 */
public class ImageUtil {
    /**
     * 将BufferedImage类型转换成Mat类型
     *
     * @param bfImg
     * @param imgType bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
     * @param matType 转换成mat的type 如 CvType.CV_8UC3
     * @return
     */
    public static Mat Img2Mat(BufferedImage bfImg, int imgType, int matType) {
        BufferedImage original = bfImg;
        int itype = imgType;
        int mtype = matType;

        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        if (original.getType() != itype) {
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), itype);

            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }

        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), mtype);
        mat.put(0, 0, pixels);

        return mat;
    }

    /**
     * Mat转换成BufferedImage
     *
     * @param matrix        要转换的Mat
     * @param fileExtension 格式为 ".jpg", ".png", etc
     * @return
     */

    public static BufferedImage Mat2BufImg(Mat matrix, String fileExtension) {

        // convert the matrix into a matrix of bytes appropriate for

        // this file extension

        MatOfByte mob = new MatOfByte();

        Imgcodecs.imencode(fileExtension, matrix, mob);

        // convert the "matrix of bytes" into a byte array

        byte[] byteArray = mob.toArray();

        BufferedImage bufImage = null;

        try {

            InputStream in = new ByteArrayInputStream(byteArray);

            bufImage = ImageIO.read(in);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return bufImage;

    }


}
