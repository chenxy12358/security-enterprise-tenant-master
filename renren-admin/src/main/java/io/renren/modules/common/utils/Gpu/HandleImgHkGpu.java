package io.renren.modules.common.utils.Gpu;

import io.renren.modules.common.utils.FileUtils;
import io.renren.modules.common.utils.ImageUtil;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_dnn;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_dnn.Net;
import org.bytedeco.opencv.opencv_text.FloatVector;
import org.bytedeco.opencv.opencv_text.IntVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_dnn.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;

/**
 * 使用官方模型和配置
 * 修改了网络大小为 416
 *
 * @author cxy
 * @create 2022/3/23
 */
public class HandleImgHkGpu {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleImgHkGpu.class);
    public static Map<String, Object> cameraLastObject = new HashMap<>();


    private static String uploadPath = "D:/test/out1/";


    private static int width = 416;

    private static int height = 416;
    /**
     * 置信度门限（超过这个值才认为是可信的推理结果）
     */
    private static float confidenceThreshold = 0.01f;


    private static float nmsThreshold = 0.4f;
    private static Net net;


    // 输出层
    private static StringVector outNames;

    // 分类名称
    private static List<String> names;

    public static void main(String[] args) throws Exception {
//        List<String> list = FileUtils.getFilePath("E:/imgHK189/202206181136");
        List<String> list = FileUtils.getFilePath("C:/Users/DS/Desktop/kxkj-server/202206290830");

        init();
        //将图⽚⽂件转化为字节数组字符串，并对其进⾏Base64编码处理
        if (null != list && list.size() > 0) {
            for (String img : list) {
                if (img.contains(".jpeg")) {
                    readImg(img);
                }
            }
        }

    }


    /**
     * 初始化
     *
     * @throws Exception
     */
    public static void init() throws Exception {
        // 初始化打印一下，确保编码正常，否则日志输出会是乱码
        String planFilePath = "D:/AI/config3";
        System.err.println("file.encoding is " + System.getProperty("file.encoding"));

        // 神经网络初始化
        net = readNetFromDarknet(planFilePath + "/yolov4.cfg", planFilePath + "/yolov4.weights");

        // 检查网络是否为空
        if (net.empty()) {
            System.err.println("神经网络初始化失败");
            throw new Exception("神经网络初始化失败");
        }

        // 输出层
        outNames = net.getUnconnectedOutLayersNames();

        // 检查GPU
        if (getCudaEnabledDeviceCount() > 0) {
            net.setPreferableBackend(opencv_dnn.DNN_BACKEND_CUDA);
            net.setPreferableTarget(opencv_dnn.DNN_TARGET_CUDA);
        }
        // 分类名称
        try {
            names = Files.readAllLines(Paths.get(planFilePath + "/coco.names"));
        } catch (IOException e) {
            System.err.println("获取分类名称失败，文件路径[{}]" + planFilePath + "/coco.names");
        }
    }


    public static void readImg(String img) {
        if (img.contains("\\")) {
            img = img.replaceAll("\\\\", "/");
        }
        String filePath = img.substring(img.lastIndexOf("/") + 1);
        String fileName = filePath.substring(filePath.indexOf("-") + 1);
        String cameraIndexCode = filePath.substring(0, filePath.indexOf("-"));
        // 读取文件到Mat
        Mat src = imread(img);

        // 执行推理 start
        // 将图片转为四维blog，并且对尺寸做调整
        Mat inputBlob = blobFromImage(src,
                1 / 255.0,
                new Size(width, height),
                new Scalar(0.0),
                true,
                false,
                CV_32F);

        // 神经网络输入
        net.setInput(inputBlob);

        // 设置输出结果保存的容器
        MatVector outs = new MatVector(outNames.size());

        // 推理，结果保存在outs中
        net.forward(outs, outNames);

        // 释放资源
        inputBlob.release();

        // 执行推理 end
        // 处理原始的推理结果，
        // 对检测到的每个目标，找出置信度最高的类别作为改目标的类别，
        // 还要找出每个目标的位置，这些信息都保存在ObjectDetectionResult对象中
        // 检测到的目标总数  results.size()
        List<ObjectDetectionResult> results = postprocess(src, outs, names);
        // 释放资源
        outs.releaseReference();


        // 计算出总耗时，并输出在图片的左上角
        printTimeUsed(src, net);

        // 将每一个被识别的对象在图片框出来，并在框的左上角标注该对象的类别
        markEveryDetectObject(src, results, fileName, cameraIndexCode, cameraIndexCode, uploadPath);


    }


    /**
     * 将每一个被识别的对象在图片框出来，并在框的左上角标注该对象的类别
     *
     * @param results
     */

    private static String markEveryDetectObject(Mat im, List<ObjectDetectionResult> results,
                                                String fileName, String cameraName, String cameraIndexCode, String outImgFilePath) {
        // 在图片上标出每个目标以及类别和置信度
        List<Map<String, Object>> listBrokenObject = new ArrayList<>();
        String imgBase64 = "";
        Boolean isObject = false;
        for (ObjectDetectionResult result : results) {
            LOGGER.info("类别[{}]，置信度[{}%]", result.getClassName(), result.getConfidence() * 100f);

            // annotate on image
            rectangle(im,
                    new Point(result.getX(), result.getY()),
                    new Point(result.getX() + result.getWidth(), result.getY() + result.getHeight()),
                    Scalar.MAGENTA,
                    1,
                    LINE_8,
                    0);

            // 写在目标左上角的内容：类别+置信度
//            String label = result.getClassName() + ":中文测试" + String.format("%.2f%%", result.getConfidence() * 100f);
            String label = result.getClassName() + ":" + String.format("%1.2f", result.getConfidence());

            // 计算显示这些内容所需的高度
            IntPointer baseLine = new IntPointer();

            Size labelSize = getTextSize(label, FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);
            int top = Math.max(result.getY(), labelSize.height());

            im = setTextToMat(im, label, result.getX(), top - 2, 0, 255, 0);
//            imgBase64 = setTextTOBase64(im, label, result.getX(), top - 2, 0, 255, 0);
            Map<String, Object> map = new HashMap();
            map.put("Object", result.getClassName());
            map.put("Similarity", result.getConfidence());
            map.put("code", result.getClassName());
            map.put("rect", new Rect(result.getX(), result.getY(), result.getWidth(), result.getHeight()));
            listBrokenObject.add(map);
            isObject=true;

        }

        if (isObject) {
            imgBase64=getBase64(im);
            if (null == cameraLastObject.get(cameraIndexCode)) {
                cameraLastObject.put(cameraIndexCode, listBrokenObject);
                // 第一次识别出对象
                FileUtils.createDir(outImgFilePath);//没有文件夹则创建文件夹
                FileUtils.createImg(imgBase64, outImgFilePath, cameraName + "-first-" + fileName,"img");

            } else {
                boolean isSameObj = false;
                boolean isSameObjSave = false;
                List<Map<String, Object>> listDx = (List<Map<String, Object>>) cameraLastObject.get(cameraIndexCode);
                if (listDx.size() == listBrokenObject.size()) {
                    for (Map<String, Object> mapL : listDx) {
                        String codeL = mapL.get("code").toString();
                        Rect rect2dL = (Rect) mapL.get("rect");
                        boolean flag = false;
                        for (Map<String, Object> mapC : listBrokenObject) {
                            String codeC = mapC.get("code").toString();
                            Rect rect2dC = (Rect) mapC.get("rect");
                            if (codeL.equals(codeC)) {
//                                float xsd = RectUtils.DecideOverlap(rect2dL, rect2dC);
                                float xsd = 0.1f;
                                if (xsd > 0.65) { // 大于此值判断为同一个对象
                                    flag = true;
                                    if (xsd < 0.9) { // 小于0.9需要保存txt
                                        isSameObjSave = true;
                                    }
                                }
//                                System.err.println(cameraName + "：标记框相似度=" + xsd);
                            }

                        }
                        if (!flag) {
                            // 不是同一个目标对象
                            isSameObj = true;
                        }
                    }
                } else {
                    // 不是同一个目标对象
                    isSameObj = true;

                }
                if (isSameObj) {
//                    System.err.println(cameraName + "判断为不是同一个目标对象");
                    // 不是同一个目标对象
                    FileUtils.createDir(outImgFilePath);//没有文件夹则创建文件夹
                    FileUtils.createImg(imgBase64, outImgFilePath, cameraName + "-bt-" + fileName,"img");

                } else {
                    // 是同一个目标对象
//                    System.err.println(cameraName + "判断为是同一个目标对象");
//                    if (isSameObjSave) { // 因为可能为错误的认定，保存base64 识别图片到本地
                    FileUtils.createImg(imgBase64, outImgFilePath, cameraName+"-"+(isSameObjSave)+fileName,"img");
//                    }

                }
                cameraLastObject.put(cameraIndexCode, listBrokenObject);
            }
            //测试位置开始 start

            // 保存识别图像到本地 start
            FileUtils.createDir(outImgFilePath);//没有文件夹则创建文件夹
            String jpgFile = Paths.get(outImgFilePath, fileName).toString();
            ImageUtil.generateImage(imgBase64, jpgFile);
            // 保存识别图像到本地 end

        }
        return imgBase64;
    }


    /**
     * 计算出总耗时，并输出在图片的左上角
     *
     * @param src
     */
    private static void printTimeUsed(Mat src, Net net) {
        // 总次数
        long totalNums = net.getPerfProfile(new DoublePointer());
        // 频率
        double freq = getTickFrequency() / 1000;
        // 总次数除以频率就是总耗时
        double t = totalNums / freq;

        // 将本次检测的总耗时打印在展示图像的左上角
        putText(src,
                String.format("time: %.2f ms", t),
                new Point(10, 80),
                FONT_HERSHEY_SIMPLEX,
                0.6,
                new Scalar(0, 255, 0, 0),
                1,
                LINE_AA,
                false);
    }


    /**
     * 推理完成后的操作
     *
     * @param frame
     * @param outs
     * @return
     */
    private static List<ObjectDetectionResult> postprocess(Mat frame, MatVector outs, List<String> names) {
        final IntVector classIds = new IntVector();
        final FloatVector confidences = new FloatVector();
        final RectVector boxes = new RectVector();

        // 处理神经网络的输出结果
        for (int i = 0; i < outs.size(); ++i) {
            // extract the bounding boxes that have a high enough score
            // and assign their highest confidence class prediction.

            // 每个检测到的物体，都有对应的每种类型的置信度，取最高的那种
            // 例如检车到猫的置信度百分之九十，狗的置信度百分之八十，那就认为是猫
            Mat result = outs.get(i);
            FloatIndexer data = result.createIndexer();

            // 将检测结果看做一个表格，
            // 每一行表示一个物体，
            // 前面四列表示这个物体的坐标，后面的每一列，表示这个物体在某个类别上的置信度，
            // 每行都是从第五列开始遍历，找到最大值以及对应的列号，
            for (int j = 0; j < result.rows(); j++) {
                // minMaxLoc implemented in java because it is 1D
                int maxIndex = -1;
                float maxScore = Float.MIN_VALUE;
                for (int k = 5; k < result.cols(); k++) {
                    float score = data.get(j, k);
                    if (score > maxScore) {
                        maxScore = score;
                        maxIndex = k - 5;
                    }
                }

                // 如果最大值大于之前设定的置信度门限，就表示可以确定是这类物体了，
                // 然后就把这个物体相关的识别信息保存下来，要保存的信息有：类别、置信度、坐标
                if (maxScore > confidenceThreshold) {
                    int centerX = (int) (data.get(j, 0) * frame.cols());
                    int centerY = (int) (data.get(j, 1) * frame.rows());
                    int width = (int) (data.get(j, 2) * frame.cols());
                    int height = (int) (data.get(j, 3) * frame.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    // 保存类别
                    classIds.push_back(maxIndex);
                    // 保存置信度
                    confidences.push_back(maxScore);
                    // 保存坐标
                    boxes.push_back(new Rect(left, top, width, height));
                }
            }

            // 资源释放
            data.release();
            result.release();
        }

        // 使用NMS删除重叠的边界框
        IntPointer indices = new IntPointer(confidences.size());
        FloatPointer confidencesPointer = new FloatPointer(confidences.size());
        confidencesPointer.put(confidences.get());

        // 非极大值抑制
        NMSBoxes(boxes, confidencesPointer, confidenceThreshold, nmsThreshold, indices, 1.f, 0);

        // 将检测结果放入BO对象中，便于业务处理
        List<ObjectDetectionResult> detections = new ArrayList<>();
        for (int i = 0; i < indices.limit(); ++i) {
            final int idx = indices.get(i);
            final Rect box = boxes.get(idx);

            final int clsId = classIds.get(idx);

            detections.add(new ObjectDetectionResult(
                    clsId,
                    names.get(clsId),
                    confidences.get(idx),
                    box.x(),
                    box.y(),
                    box.width(),
                    box.height()
            ));

            // 释放资源
            box.releaseReference();
        }

        // 释放资源
        indices.releaseReference();
        confidencesPointer.releaseReference();
        classIds.releaseReference();
        confidences.releaseReference();
        boxes.releaseReference();

        return detections;
    }


    /**
     * 标记识别类 返回 Mat
     *
     * @param im
     * @param content
     * @param leftBottomX
     * @param leftBottomY
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public static Mat setTextToMat(Mat im, String content, double leftBottomX, double leftBottomY, float red, float green, float blue) {
        BufferedImage bufImg = setText(im, content, leftBottomX, leftBottomY, red, green, blue);
        im = Java2DFrameUtils.toMat(bufImg);
        return im;

    }


    public static String getBase64(Mat im) {
        BufferedImage bufImg = Java2DFrameUtils.toBufferedImage(im);
        String oldImg64 = ImageUtil.BufferedImageToBase64(bufImg);
        String img641 = oldImg64;
//        if (ImageUtil.imageSize(oldImg64) > 300) {
//            img641 = ImageUtil.scaleImage(oldImg64, "jpg");
//        }
        return img641;

    }


    /**
     * 标记识别类 返回base64
     *
     * @param im
     * @param content
     * @param leftBottomX
     * @param leftBottomY
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public static String setTextTOBase64(Mat im, String content, double leftBottomX, double leftBottomY, float red, float green, float blue) {
        BufferedImage bufImg = setText(im, content, leftBottomX, leftBottomY, red, green, blue);
        String oldImg64 = ImageUtil.BufferedImageToBase64(bufImg);
        String img641 = oldImg64;
//        if (ImageUtil.imageSize(oldImg64) > 300) {
//            img641 = ImageUtil.scaleImage(oldImg64, "jpg");
//        }
        return img641;

    }


    /**
     * 标记识别类 返回 BufferedImage
     *
     * @param im
     * @param content
     * @param leftBottomX
     * @param leftBottomY
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public static BufferedImage setText(Mat im, String content, double leftBottomX, double leftBottomY, float red, float green, float blue) {
        int fontSize = 14;
        if (leftBottomX <= 0) {
            leftBottomX = 5;
        }
        if (leftBottomY <= fontSize) {
            leftBottomY = fontSize;
        }

        BufferedImage bufImg = Java2DFrameUtils.toBufferedImage(im);
        int width = bufImg.getWidth(); // 像素
        if (width > 3800) { //800万3840*2160 400w 2560×1440P 200w 1920×1080 1280*720
            fontSize = 35;
        } else if (width > 2500) {
            fontSize = 25;
        } else if (width > 1900) {
            fontSize = 20;
        } else if (width > 1200) {
            fontSize = 14;
        }
        Font font = new Font("微软雅黑", Font.PLAIN, fontSize);
        Graphics2D g = bufImg.createGraphics();

        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D stringBounds = font.getStringBounds(content, frc);
        double fontWidth = stringBounds.getWidth();
        g.setBackground(new Color((int) (red + 0.5), (int) (green + 0.5), (int) (blue + 0.5)));//设置背景色
        g.clearRect(new Double(leftBottomX).intValue(), new Double(leftBottomY).intValue() - fontSize + 2,
                new Double(fontWidth).intValue(), fontSize);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

        g.setColor(Color.black);//设置字体色
        g.drawImage(bufImg, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.setFont(font); //设置字体
        //设置水印的坐标 标记类别
        g.drawString(content, new Double(leftBottomX).intValue(), new Double(leftBottomY).intValue());
        g.dispose();
        return bufImg;

    }

}