package io.renren.modules.common.utils.Gpu;

import cn.hutool.json.JSONObject;
import io.renren.modules.common.constant.KxAiBoundary;
import io.renren.modules.common.utils.FileUtils;
import io.renren.modules.common.utils.ImageUtil;
import io.renren.modules.common.utils.RectUtils;
import io.renren.modules.discernConfig.dto.KxAIPzVO;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_dnn.Net;
import org.bytedeco.opencv.opencv_text.FloatVector;
import org.bytedeco.opencv.opencv_text.IntVector;
import org.opencv.core.Rect2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bytedeco.opencv.global.opencv_core.CV_32F;
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
public class HandleImgHkCpu {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleImgHkCpu.class);
    /**
     * 置信度门限（超过这个值才认为是可信的推理结果）
     */
    private static float confidenceThreshold = 0.01f;

    private static float nmsThreshold = 0.2f;

    /**
     * @param imgFilePath
     * @param planFilePath
     * @param outImgFilePath
     * @param picWidth
     * @param picHeight
     * @param listDis
     * @param deleteFlag
     * @param jsonBoubdary   标记范围框
     * @return
     */
    public static List<Object> analysisImgByCPU(String imgFilePath, String planFilePath, String outImgFilePath
            , int picWidth, int picHeight, List<KxAIPzVO> listDis, boolean deleteFlag, JSONObject jsonBoubdary) {
        List<Object> returnList = new ArrayList<>();
        // 初始化打印一下，确保编码正常，否则日志输出会是乱码
//        System.err.println("file.encoding is " + System.getProperty("file.encoding"));
        Mat inputBlob = null;
        MatVector outs = null;
        Mat src = null;
        try {
            // 神经网络初始化
            Net net = readNetFromDarknet(planFilePath + "/yolov4.cfg", planFilePath + "/yolov4.weights");

            // 检查网络是否为空
            if (net.empty()) {
                System.err.println("神经网络初始化失败");
                throw new Exception("神经网络初始化失败");
            }

            // 输出层
            StringVector outNames = net.getUnconnectedOutLayersNames();

            // 分类名称
            List<String> names = Files.readAllLines(Paths.get(planFilePath + "/coco.names"));
            // 读取文件到Mat
            src = imread(imgFilePath);
            if (deleteFlag) {
                File file = new File(imgFilePath);
                if (file.exists()) {
                    file.delete();
                    System.out.println("删除成功");
                }
            }
            // 执行推理 start
            // 将图片转为四维blog，并且对尺寸做调整
            inputBlob = blobFromImage(src,
                    1 / 255.0,
                    new Size(picWidth, picHeight),
                    new Scalar(0.0),
                    true,
                    false,
                    CV_32F);

            // 神经网络输入
            net.setInput(inputBlob);

            // 设置输出结果保存的容器
            outs = new MatVector(outNames.size());

            // 推理，结果保存在outs中
            net.forward(outs, outNames);

            // 执行推理 end
            // 处理原始的推理结果，
            // 对检测到的每个目标，找出置信度最高的类别作为改目标的类别，
            // 还要找出每个目标的位置， ObjectDetectionResultCPU
            // 检测到的目标总数  results.size()
            List<ObjectDetectionResult> results = postprocess(src, outs, names);
            LOGGER.info("一共检测到{}个目标" ,results.size());


            if (null != results && results.size() > 0) {
                // 将每一个被识别的对象在图片框出来，并在框的左上角标注该对象的类别
                returnList = markEveryDetectObject(imgFilePath, src, results, listDis, outImgFilePath, jsonBoubdary);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("识别图像出错:", e);
            LOGGER.error("识别图像出错:listDis+" + listDis);
        } finally {
            if (inputBlob != null) {
                inputBlob.release();
            }
            if (outs != null) {
                outs.releaseReference();
            }
            if (src != null) {
                src.release();
            }
            if (null == returnList || returnList.size() == 0) {
                returnList.add("");
                LOGGER.info("未识别出目标对象:");
            }
            return returnList;
        }

    }

    /**
     * 将每一个被识别的对象在图片框出来，并在框的左上角标注该对象的类别
     *
     * @param results
     */
    private static List<Object> markEveryDetectObject(String imgFilePath, Mat im, List<ObjectDetectionResult> results, List<KxAIPzVO> listDis,
                                                      String outImgFilePath, JSONObject jsonBoubdary) {

        List<Rect2d> rectList = new ArrayList<Rect2d>();
        String rectType = KxAiBoundary.TYPE_DETECT;
        if (!jsonBoubdary.isEmpty() && !jsonBoubdary.isNull("rectList")) {
            if (!jsonBoubdary.isNull("type")) {
                rectType = jsonBoubdary.getStr("type");
            }
            rectList = jsonBoubdary.getJSONArray("rectList").toList(Rect2d.class);

        }
        // 在图片上标出每个目标以及类别和置信度
        List<Object> returnList = new ArrayList<>();
        List<Map<String, Object>> listBrokenObject = new ArrayList<>();
        String jpgFile = "";
        Boolean isObject = false;
        for (ObjectDetectionResult result : results) {
            Boolean isBoubdary = false;
            System.err.println(result.getConfidence());
            if (null != rectList && rectList.size() > 0) {
                for (Rect2d r2 : rectList) {
                    if(KxAiBoundary.TYPE_DETECT.equals(rectType)){
                        if(KxAiBoundary.DRAW_BOUNDARY){ //是否画框
                            rectangle(im,
                                    new Point((int)r2.x, (int)r2.y),
                                    new Point((int)r2.x+ (int)r2.width,
                                            (int)r2.y+ (int)r2.height),
                                    new Scalar(0, 255, 0, 0),
                                    1,
                                    LINE_8,
                                    0);
                        }
                        Rect2d r1 = new Rect2d(result.getX(), result.getY(), result.getWidth(), result.getHeight());
                        float xsd = RectUtils.DecideOverlap(r1, r2);
                        if (xsd > KxAiBoundary.boundary_in_similarity) { // 大于此值判断为在标记框内
                            isBoubdary=true;
                        }
                    }else {
                        // TODO: 2022/8/2 显示框外目标
                        if(KxAiBoundary.DRAW_BOUNDARY){ //是否画框
                            rectangle(im,
                                    new Point((int)r2.x, (int)r2.y),
                                    new Point((int)r2.x+ (int)r2.width,
                                            (int)r2.y+ (int)r2.height),
                                    new Scalar(0, 0, 255, 0),
                                    1,
                                    LINE_8,
                                    0);
                        }
                    }
                }

            }else {
                isBoubdary=true;
            }

            String typeClass = result.getClassName().trim(); // 类别
            float confidence = result.getConfidence();
            for (KxAIPzVO kxAIPzVO : listDis) {
                String code = kxAIPzVO.getCode();
                String name = kxAIPzVO.getName();
                BigDecimal thresh = kxAIPzVO.getThresh();
                if (typeClass.equalsIgnoreCase(code) && kxAIPzVO.isEnable() && confidence >= thresh.floatValue()&& isBoubdary) {
                    LOGGER.info("类别[{}]，置信度[{}%]", result.getClassName(), result.getConfidence() * 100f);
                    Map<String, Object> map = new HashMap();
                    map.put("Object", name);
                    map.put("Similarity", confidence);
                    listBrokenObject.add(map);
                    //标记框颜色 start
                    int topclass = null == kxAIPzVO.getSort() ? 0 : kxAIPzVO.getSort();
                    int nclasses = listDis.size();
                    int offset = (topclass * 123457) % nclasses;
                    float red = RectUtils.getColor(2, offset, nclasses);
                    float green = RectUtils.getColor(1, offset, nclasses);
                    float blue = RectUtils.getColor(0, offset, nclasses);
                    //标记框颜色 end
                    // annotate on image
                    rectangle(im,
                            new Point(result.getX(), result.getY()),
                            new Point(result.getX() + result.getWidth(),
                                    result.getY() + result.getHeight()),
                            new Scalar(blue, green, red, 0),
                            1,
                            LINE_8,
                            0);

                    // 写在目标左上角的内容：类别+置信度
                    String label = name + ":" + String.format("%1.2f", confidence);
//                    String label = typeClass + ":" + String.format("%1.2f", confidence);

                    // 计算显示这些内容所需的高度
                    IntPointer baseLine = new IntPointer();

                    Size labelSize = getTextSize(label, FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);
                    int top = Math.max(result.getY(), labelSize.height());

                    // 添加内容到图片上
//                    putText(im, label, new org.bytedeco.opencv.opencv_core.Point(result.getX(), top - 4), FONT_HERSHEY_SIMPLEX, 0.5, new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 0), 1, LINE_4, false);
                    im = setTextToMat(im, label, result.getX(), top - 2, red, green, blue);
                    isObject = true;

                }
            }
        }
        if (isObject) {
            String fileName = "";
            if (imgFilePath.contains("\\")) {
                imgFilePath = imgFilePath.replaceAll("\\\\", "/");
            }
            fileName = imgFilePath.substring(imgFilePath.lastIndexOf("/") + 1);
            FileUtils.createDir(outImgFilePath);//没有文件夹则创建文件夹
            fileName = "ai-s-" + System.currentTimeMillis() + "-" + fileName;
            jpgFile = Paths.get(outImgFilePath, fileName).toString();
            imwrite(jpgFile, im);
            LOGGER.debug("输出文件路径" + jpgFile);
        }
        returnList.add(jpgFile);
        returnList.add(listBrokenObject);
        return returnList;
    }

    /**
     * 推理完成后的操作
     *
     * @param frame
     * @param outs
     * @return
     */
    private static List<ObjectDetectionResult> postprocess(Mat frame, MatVector outs, List<String> names) {
        IntVector classIds = new IntVector();
        FloatVector confidences = new FloatVector();
        RectVector boxes = new RectVector();

        List<ObjectDetectionResult> detections = new ArrayList<>();
        FloatPointer confidencesPointer = new FloatPointer();
        IntPointer indices = new IntPointer();

        try {
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
//            IntPointer indices = new IntPointer(confidences.size());
//            FloatPointer confidencesPointer = new FloatPointer(confidences.size());
            indices = new IntPointer(confidences.size());
            confidencesPointer = new FloatPointer(confidences.size());
            confidencesPointer.put(confidences.get());

            // 非极大值抑制
            NMSBoxes(boxes, confidencesPointer, confidenceThreshold, nmsThreshold, indices, 1.f, 0);

            // 将检测结果放入BO对象中，便于业务处理
//            List<ObjectDetectionResult> detections = new ArrayList<>();
            for (int i = 0; i < indices.limit(); ++i) {
                int idx = indices.get(i);
                Rect box = boxes.get(idx);

                int clsId = classIds.get(idx);

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
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("识别图像出错:postprocess");
            LOGGER.error(e.getMessage());
            throw e;

        } finally {
            if (classIds != null) {
                classIds.releaseReference();
            }
            if (confidences != null) {
                confidences.releaseReference();
            }
            if (boxes != null) {
                boxes.releaseReference();
            }
            if (confidencesPointer != null) {
                confidencesPointer.releaseReference();
            }
            if (indices != null) {
                indices.releaseReference();
            }
        }
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
//        im = Java2DFrameUtils.toMat(bufImg);
        im = ImageUtil.toMat(bufImg);
        return im;

    }


    /**
     * @param im
     * @return
     */
    public static String getBase64(Mat im) {
//        BufferedImage bufImg = Java2DFrameUtils.toBufferedImage(im);
        BufferedImage bufImg = ImageUtil.toBufferedImage(im);
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
//        if (leftBottomX <= 0) {
//            leftBottomX = 5;
//        }
        if (leftBottomY <= fontSize) {
            leftBottomY = fontSize;
        }
//        BufferedImage bufImg = Java2DFrameUtils.toBufferedImage(im);
        BufferedImage bufImg = ImageUtil.toBufferedImage(im);
        int width = bufImg.getWidth(); // 像素
        if (width > 3800) {       //800万3840*2160 400w 2560×1440P 200w 1920×1080 1280*720
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