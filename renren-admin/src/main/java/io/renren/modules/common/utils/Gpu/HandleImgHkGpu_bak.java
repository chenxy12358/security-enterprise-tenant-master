package io.renren.modules.common.utils.Gpu;

import io.renren.modules.common.utils.FileUtils;
import io.renren.modules.common.utils.ImageUtil;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_dnn;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.StringVector;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_dnn.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;

/**
 * 使用官方模型和配置
 * 修改了网络大小为 416
 *
 * @author cxy
 * @create 2022/3/23
 */
public class HandleImgHkGpu_bak {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleImgHkGpu_bak.class);


    private static ExecutorService fixPool = Executors.newFixedThreadPool(20);

    private static String uploadPath = "D:/test/out1/";

    private static String planFilePath = "D:/AI/config3";

    private static int width = 416;

    private static String type = "cpu";

    private static int height = 416;
    /**
     * 置信度门限（超过这个值才认为是可信的推理结果）
     */
    private static float confidenceThreshold = 0.01f;

    private static float nmsThreshold = 0.01f;
    private static Net net;


    // 输出层
    private static StringVector outNames;

    // 分类名称
    private static List<String> names;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        List<String> list = FileUtils.getFilePath("E:/imgHK189/202206181136/");
//        List<String> list = FileUtils.getFilePath("E:/img/img/");
        init();
        int i=0;
        //将图⽚⽂件转化为字节数组字符串，并对其进⾏Base64编码处理
        if (null != list && list.size() > 0) {
            for (String img : list) {
                long Start = System.currentTimeMillis();
                System.err.println(Start);
//                init();
                if(img.contains(".jpeg")){

                    readImg(img);
                }
                System.err.println("===============++i");
                System.err.println(++i);
                System.err.println(img);
                long end = System.currentTimeMillis();
                System.err.println("===============");
                System.err.println(end - Start);
            }
        }
        long end = System.currentTimeMillis();
        System.err.println("end-startTime:"+(end-startTime));

    }


    /**
     * 初始化
     *
     * @throws Exception
     */
    private static void init() throws Exception {
        // 初始化打印一下，确保编码正常，否则日志输出会是乱码
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
            type="gpu";
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

    private static void readImg(String str1) {
        // 读取文件到Mat
        Mat src = imread(str1);

        // 执行推理
        MatVector outs = doPredict(src);

        // 处理原始的推理结果，
        // 对检测到的每个目标，找出置信度最高的类别作为改目标的类别，
        // 还要找出每个目标的位置，这些信息都保存在ObjectDetectionResult对象中
        // 检测到的目标总数  results.size()
        List<ObjectDetectionResult> results = postprocess(src, outs);
        // 释放资源
        outs.releaseReference();
        System.err.println("一共检测到{}个目标" + results.size());


        // 计算出总耗时，并输出在图片的左上角
        printTimeUsed(src);

        // 将每一个被识别的对象在图片框出来，并在框的左上角标注该对象的类别
        src = markEveryDetectObject(src, results);
        boolean flag = results.size() == 0 ? false : true;
        // 将添加了标注的图片保持在磁盘上，并将图片信息写入map（给跳转页面使用）
        saveMarkedImage(src, flag);

    }


    /**
     * 将添加了标注的图片保持在磁盘上，并将图片信息写入map（给跳转页面使用）
     *
     * @param src
     */
    private static void saveMarkedImage(Mat src, boolean flag) {
        String newFileName = UUID.randomUUID() + ".png";
        if (flag) {
            // 新的图片文件名称
            newFileName = "ai-" + UUID.randomUUID() + ".png";
            imwrite(uploadPath + "/" + newFileName, src);
        }
        // 图片写到磁盘上
//        imwrite(uploadPath + "/" + newFileName, src);
    }

    /**
     * 将每一个被识别的对象在图片框出来，并在框的左上角标注该对象的类别
     *
     * @param results
     */
    private static Mat markEveryDetectObject(Mat im, List<ObjectDetectionResult> results) {
        // 在图片上标出每个目标以及类别和置信度
        for (ObjectDetectionResult result : results) {
            System.err.println("类别[{}]，置信度[{}%]" + result.getClassName() + result.getConfidence() * 100f);
            LOGGER.info("类别[{}]，置信度[{}%]", result.getClassName(), result.getConfidence() * 100f);

            // annotate on image
            rectangle(im,
                    new org.bytedeco.opencv.opencv_core.Point(result.getX(), result.getY()),
                    new org.bytedeco.opencv.opencv_core.Point(result.getX() + result.getWidth(), result.getY() + result.getHeight()),
                    org.bytedeco.opencv.opencv_core.Scalar.MAGENTA,
                    1,
                    LINE_8,
                    0);

            // 写在目标左上角的内容：类别+置信度
//            String label = result.getClassName() + ":中文测试" + String.format("%.2f%%", result.getConfidence() * 100f);
            String label = result.getClassName() + ":中文测试" + String.format("%1.2f", result.getConfidence());

            // 计算显示这些内容所需的高度
            IntPointer baseLine = new IntPointer();

            org.bytedeco.opencv.opencv_core.Size labelSize = getTextSize(label, FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);
            int top = Math.max(result.getY(), labelSize.height());

            im = setTextToMat(im, label, result.getX(), top - 2, 0, 255, 0);

        }
        return im;
    }

    /**
     * 用神经网络执行推理
     *
     * @param src
     * @return
     */
    private static MatVector doPredict(Mat src) {
        // 将图片转为四维blog，并且对尺寸做调整
        Mat inputBlob = blobFromImage(src,
                1 / 255.0,
                new org.bytedeco.opencv.opencv_core.Size(width, height),
                new org.bytedeco.opencv.opencv_core.Scalar(0.0),
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

        return outs;
    }

    /**
     * 计算出总耗时，并输出在图片的左上角
     *
     * @param src
     */
    private static void printTimeUsed(Mat src) {
        // 总次数
        long totalNums = net.getPerfProfile(new DoublePointer());
        // 频率
        double freq = getTickFrequency() / 1000;
        // 总次数除以频率就是总耗时
        double t = totalNums / freq;

        // 将本次检测的总耗时打印在展示图像的左上角
        putText(src,
                String.format(type+"-time: %.2f ms", t),
                new org.bytedeco.opencv.opencv_core.Point(10, 80),
                FONT_HERSHEY_SIMPLEX,
                0.6,
                new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 0),
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
    private static List<ObjectDetectionResult> postprocess(Mat frame, MatVector outs) {
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
                    boxes.push_back(new org.bytedeco.opencv.opencv_core.Rect(left, top, width, height));
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
            final org.bytedeco.opencv.opencv_core.Rect box = boxes.get(idx);

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
    public static String setTextTOBase64(Mat im, String content, double leftBottomX, double leftBottomY, float red, float green, float blue) throws Exception {
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