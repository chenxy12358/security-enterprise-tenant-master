package io.renren.modules.common.utils;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import org.bytedeco.javacv.Frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Objects;

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

    /**
     *
     * @param imgStr
     * @param outImgFilePath
     * @return
     */
    public static boolean generateImage(String imgStr, String outImgFilePath) {
        if(!imgStr.contains(",")){
            imgStr=  "data:image/jpeg;base64," + imgStr;
        }
        String[] baseStrs = imgStr.split(",");
        //取索引为1的元素进行处理
        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(baseStrs[1]);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成图片
            OutputStream out = new FileOutputStream(outImgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * BufferedImage 编码转换为 base64
     *
     * @param bufferedImage
     * @return
     */
    public static String BufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(bufferedImage, "jpg", baos);//写入流中
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(Objects.requireNonNull(bytes)).trim();//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
//        return "data:image/jpeg;base64," + png_base64;
        return  png_base64;
    }

    public static BufferedImage toBufferedImage(org.bytedeco.opencv.opencv_core.Mat src) {
        OpenCVFrameConverter.ToMat matConv = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter biConv = new Java2DFrameConverter();
        Frame f = matConv.convert(src).clone();
        Throwable var2 = null;

        BufferedImage var3;
        try {
            var3 =  Java2DFrameConverter.cloneBufferedImage(biConv.getBufferedImage(f));
        } catch (Throwable var12) {
            var2 = var12;
            throw var12;
        } finally {
            if (f != null) {
                if (var2 != null) {
                    try {
                        f.close();
                    } catch (Throwable var11) {
                        var2.addSuppressed(var11);
                    }
                } else {
                    f.close();
                }
            }

        }

        return var3;
    }


    public static org.bytedeco.opencv.opencv_core.Mat toMat(BufferedImage src) {
        OpenCVFrameConverter.ToMat matConv = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter biConv = new Java2DFrameConverter();
        return matConv.convertToMat(biConv.convert(src)).clone();
    }

}
