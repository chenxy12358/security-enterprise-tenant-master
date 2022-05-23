package io.renren.modules.common.utils;

import io.renren.modules.discernConfig.dto.KxAIPzVO;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * 使用官方模型和配置
 * 修改了网络大小为 416
 *
 * @author cxy
 * @create 2022/3/23
 */
public class HandleImg {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleImg.class);

    public static void main(String[] args) throws Exception {
        String outImgFilePath = "D:/ai_demo/out1/";
        String planFilePath = "D:/ai_demo/kxAi/";
        double picWidth = 1920;
        double picHeight = 1080;
        String infoPack[] = {"D:/ai_demo/test_pic/1.jpeg", "D:/ai_demo/test_pic/2.jpeg", "D:/ai_demo/test_pic/3.jpeg"};
        for (String str : infoPack) {
            long Start = System.currentTimeMillis();
            System.err.println(Start);
            analysisImgImg(str, planFilePath, outImgFilePath, 1920, 1080, null, false);
            long end = System.currentTimeMillis();
            System.err.println("===============");
            System.err.println(end - Start);
        }
//                String url ="https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg";
        String url = "http://117.175.158.169:1080//app-data/DeviceUpload/Data-Jpeg/KX-V22P40-AI00010/2022-2/2022-02-11/2022-02-11_18-23-25.jpeg";
        File file = HandleImg.getLogoFromWeb(url, "D:/ai_demo/out110/");
        HandleImg.analysisImgImg(file,
                planFilePath, outImgFilePath, picWidth, picHeight, null, true);
    }


    /**
     * 读取要被推理的图片
     *
     * @param file           原图文件
     * @param planFilePath   配置文件路径
     * @param outImgFilePath 输出文件路径
     * @param picWidth       图片宽度
     * @param picHeight      图片高度
     * @param listDis        设备对应的配置
     * @param deleteFlag     是否删除原图
     * @throws IOException
     */
    public static List<Object> analysisImgImg(File file, String planFilePath, String outImgFilePath
            , double picWidth, double picHeight, List<KxAIPzVO> listDis, boolean deleteFlag) throws Exception {
        return analysisImgImg(file.getPath(), planFilePath, outImgFilePath
                , picWidth, picHeight, listDis, deleteFlag);

    }

    /**
     * 读取要被推理的图片
     *
     * @param imgFilePath    原图路径
     * @param planFilePath   配置文件路径
     * @param outImgFilePath 输出文件路径
     * @param picWidth       图片宽度
     * @param picHeight      图片高度
     * @param listDis        设备对应的配置
     * @param deleteFlag     是否删除原图
     * @throws IOException
     */
    public static List<Object> analysisImgImg(String imgFilePath, String planFilePath, String outImgFilePath
            , double picWidth, double picHeight, List<KxAIPzVO> listDis, boolean deleteFlag) throws Exception {

        Loader.load(opencv_java.class); // 加载opencv
        List<String> list = new ArrayList<>();
        List<Object> returnList = new ArrayList<>();
        // 读取类别名称
        Reader inputStream = new FileReader(planFilePath + "coco.names");
        BufferedReader reader = new BufferedReader(inputStream);
        String tempLine = "";
        while ((tempLine = reader.readLine()) != null) {
            list.add(tempLine);
        }

        LOGGER.debug("listDis=" + listDis.size());
        String[] names = list.toArray(new String[list.size()]);
        // 定义对象
        String jpgFile = "";
        List<Map<String, String>> listBrokenObject = new ArrayList<>();
        Mat im = null;
        Mat out = null;
        MatOfInt indexs = null;
        MatOfRect2d boxes = null;
        MatOfFloat con = null;

        try {
            im = Imgcodecs.imread(imgFilePath, Imgcodecs.IMREAD_COLOR);
            if (im.empty()) {
                System.out.println("read image fail");
                LOGGER.debug("read image fail");
            }
            if (deleteFlag) {
                File file = new File(imgFilePath);
                if (file.exists()) {
                    file.delete();
                    System.out.println("删除成功");
                }
            }
            // 指定配置文件和模型文件加载网络 D:\ai_demo\kxAi
            String cfgFile = planFilePath + "yolov4.cfg";
            String weights = planFilePath + "yolov4.weights";
            Net net = Dnn.readNetFromDarknet(cfgFile, weights);
            if (net.empty()) {
                System.out.println("init net fail");
                LOGGER.debug("init net fail");
            }

            // 设置计算后台：如果电脑有GPU，可以指定为：DNN_BACKEND_CUDA
            net.setPreferableBackend(Dnn.DNN_BACKEND_OPENCV);
            // 指定为 CPU 模式
            net.setPreferableTarget(Dnn.DNN_TARGET_CPU);
//            net.setPreferableTarget(Dnn.DNN_TARGET_CUDA);

            // 图片预处理：将图片转换为 608 大小的图片，这个数值最好与配置文件的网络大小一致
            // 缩放因子大小，opencv 文档规定的：https://github.com/opencv/opencv/blob/master/samples/dnn/models.yml#L31
            float scale = 1 / 255F;
            Mat inputBlob = Dnn.blobFromImage(im, scale, new Size(picWidth, picHeight), new Scalar(0), true, false);
            // 输入图片到网络中
            net.setInput(inputBlob);

            // 推理
            List<String> outLayersNames = net.getUnconnectedOutLayersNames();
            out = net.forward(outLayersNames.get(0));
            if (out.empty()) {
                System.out.println("forward result is null");
                LOGGER.debug("forward result is null");
            }

            // 处理 out 的结果集: 移除小的置信度数据和去重
            List<Rect2d> rect2dList = new ArrayList<>();
            List<Float> confList = new ArrayList<>();
            List<Integer> objIndexList = new ArrayList<>();
            String typeClass = "";
            String Confidence = "";
            // 每个 row 就是一个单元，cols 就是当前单元的预测信息
            for (int i = 0; i < out.rows(); i++) {
                int size = out.cols() * out.channels();
                float[] data = new float[size];
                // 将结果拷贝到 data 中，0 表示从索引0开始拷贝
                out.get(i, 0, data);
                float confidence = -1; // 置信度
                int objectClass = -1; // 类型索引
                // data中的前4个是box的数据，第5个是分数，后面是每个 classes 的置信度
                int pro_index = 5;
                for (int j = pro_index; j < out.cols(); j++) {
                    if (confidence < data[j]) {
                        // 记录本单元中最大的置信度及其类型索引
                        confidence = data[j];
                        objectClass = j - pro_index;
                    }
                }
                if (confidence > 0) { // 置信度大于 0 的才记录
                    // 计算中点、长宽、左下角点位
                    float centerX = data[0] * im.cols();
                    float centerY = data[1] * im.rows();
                    float width = data[2] * im.cols();
                    float height = data[3] * im.rows();
                    float leftBottomX = centerX - width / 2;
                    float leftBottomY = centerY - height / 2;
                    // 记录box信息、置信度、类型索引
                    rect2dList.add(new Rect2d(leftBottomX, leftBottomY, width, height));
                    confList.add(confidence);
                    objIndexList.add(objectClass);
                }
            }
            if (rect2dList.isEmpty()) {
                System.out.println("not object");
                LOGGER.debug("not object");
            }
            // box 去重
            indexs = new MatOfInt();
            boxes = new MatOfRect2d(rect2dList.toArray(new Rect2d[0]));
            float[] confArr = new float[confList.size()];
            for (int i = 0; i < confList.size(); i++) {
                confArr[i] = confList.get(i);
            }
            con = new MatOfFloat(confArr);
            // NMS 算法去重
            Dnn.NMSBoxes(boxes, con, 0.01F, 0.01F, indexs);
            if (indexs.empty()) {
                System.out.println("indexs is empty");
                LOGGER.debug("indexs is empty");
            }
            boolean isOut = false;
            // 去重后的索引
            int[] ints = indexs.toArray();
            int[] classesNumberList = new int[names.length];
            for (int i : ints) {
                typeClass = names[objIndexList.get(i)]; // 类别
                Confidence = String.format("%1.2f", confList.get(i)); //置信度

                LOGGER.debug("typeClass233=" + typeClass);
                for (KxAIPzVO kxAIPzVO : listDis) {
                    String code = kxAIPzVO.getCode();
                    String name = kxAIPzVO.getName();
                    BigDecimal thresh = kxAIPzVO.getThresh();
                    if (typeClass.equals(code) && kxAIPzVO.isEnable() && confList.get(i) >= thresh.floatValue()) {
                        LOGGER.debug("typeClass=" + typeClass);
                        Map map = new HashMap();
                        map.put("Object", name);
                        map.put("Similarity", confList.get(i));
                        listBrokenObject.add(map);
                        // 与 names 的索引位置相对应
                        Rect2d rect2d = rect2dList.get(i);
                        Integer obj = objIndexList.get(i);
                        classesNumberList[obj] += 1;

                        LOGGER.debug("1122");
                        //标记框颜色 start
                        int topclass  =  null == kxAIPzVO.getSort()  ? 0: kxAIPzVO.getSort();
                        LOGGER.debug("topclass=" + topclass);
                        int nclasses  = listDis.size();
                        LOGGER.debug("nclasses=" + nclasses);
                        int   offset = (topclass * 123457) % nclasses;
                        LOGGER.debug("offset=" + offset);
                        float red    = getColor(2, offset, nclasses);
                        float green  = getColor(1, offset, nclasses);
                        float blue   = getColor(0, offset, nclasses);
                        //标记框颜色 end

                        LOGGER.debug("red=" + red+";green=" + green+";blue=" + blue);
                        // 将 box 信息画在图片上, Scalar 对象是 BGR 的顺序，与RGB顺序反着的。
                        Imgproc.rectangle(im, new Point(rect2d.x, rect2d.y), new Point(rect2d.x + rect2d.width, rect2d.y + rect2d.height),
                                new Scalar(blue, green, red), 1);
                        String content = name + ":" + Confidence;
                        LOGGER.debug("red=" + red+";green=" + green+";content=" + content);
                        im = setText(im, content, rect2d.x, rect2d.y,red,green,blue);
                        isOut = true;
                    }

                }
            }

            if (isOut) {
                LOGGER.debug("isOut=" + isOut);
                String fileName = "";
                if(imgFilePath.contains("\\")){
                    imgFilePath=imgFilePath.replaceAll("\\\\","/");
                }
                fileName = imgFilePath.substring(imgFilePath.lastIndexOf("/") + 1);
                createDir(outImgFilePath);//没有文件夹则创建文件夹
                fileName = "ai-s-" + System.currentTimeMillis() + "-" + fileName;
                jpgFile = Paths.get(outImgFilePath, fileName).toString();
                Imgcodecs.imwrite(jpgFile, im);
                LOGGER.debug("输出文件路径" + jpgFile);

            }
        } catch (Exception e) {
            LOGGER.error("识别图像出错:"+e);
            LOGGER.error(e.getMessage());
            System.err.println(e.getMessage());
            throw e;
        } finally {
            // 释放资源
            if (im != null) {
                im.release();
            }
            if (out != null) {
                out.release();
            }
            if (indexs != null) {
                indexs.release();
            }
            if (boxes != null) {
                boxes.release();
            }
            if (con != null) {
                con.release();
            }
            returnList.add(jpgFile);
            returnList.add(listBrokenObject);
            return returnList;
        }


    }

    public static Mat setText(Mat im, String content, double leftBottomX, double leftBottomY,float red,float green,float blue) {
        int fontSize=17;
        Font font = new Font("微软雅黑", Font.PLAIN, fontSize);
        BufferedImage bufImg = ImageUtil.Mat2BufImg(im, ".png");

        LOGGER.debug("content1=" + content);
        Graphics2D g = bufImg.createGraphics();

        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D stringBounds = font.getStringBounds(content, frc);
        double fontWidth = stringBounds.getWidth();
        g.setBackground(new Color((int)(red+0.5), (int)(green+0.5), (int)(blue+0.5)));//设置背景色
        g.clearRect(new Double(leftBottomX).intValue(), new Double(leftBottomY).intValue() - fontSize-2, new Double(fontWidth).intValue() + 1, fontSize+2);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

        LOGGER.debug("fontWidth=" + fontWidth);

        g.setColor(Color.black);//设置字体色
        g.drawImage(bufImg, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.setFont(font); //设置字体
        //设置水印的坐标
        g.drawString(content, new Double(leftBottomX).intValue(), new Double(leftBottomY).intValue());
        g.dispose();
        im = ImageUtil.Img2Mat(bufImg, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);// CvType.CV_8UC3
        return im;

    }


    public static File getLogoFromWeb(String logourl, String uploadPath) {
        try {
            URL url = new URL(logourl);
            String fileType = logourl.substring(logourl.lastIndexOf("."), logourl.length());
            File outFile = new File(uploadPath + UUID.randomUUID().toString().replace("-", "") + fileType);
            OutputStream os = new FileOutputStream(outFile);
            InputStream is = url.openStream();
            byte[] buff = new byte[1024];
            while (true) {
                int readed = is.read(buff);
                if (readed == -1) {
                    break;
                }
                byte[] temp = new byte[readed];
                System.arraycopy(buff, 0, temp, 0, readed);
                os.write(temp);
            }
            is.close();
            os.close();
            return outFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 如果文件夹不存在就创建文件夹
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }


    /**
     * 计算rgb 权重规则 （与设备端一致）
     *
     * topclass 当前识别到的物体的顺序
     * nclasses 物体种类数
     * int   offset = (topclass * 123457) % nclasses;
     * float red    = get_color(2, offset, nclasses);
     * float green  = get_color(1, offset, nclasses);
     * float blue   = get_color(0, offset, nclasses);
     * @param c
     * @param x
     * @param max
     * @return
     */
    public static float getColor(int c, int x, int max) {
        float colors[][] = {{1,0,1},{0,0,1},{0,1,1},{0,1,0},{1,1,0},{1,0,0}};
        float ratio = ((float)x/max)*5;
        int i = (int) Math.floor(ratio);  //取整,返回的是小于或等于ratio的最大整数
        int j = (int) Math.ceil(ratio);   //取整,返回的是大于ratio的最小整数
        ratio -= i;
        float r = (1-ratio)*colors[i][c] + ratio*colors[j][c];
        return r*255;
    }



}