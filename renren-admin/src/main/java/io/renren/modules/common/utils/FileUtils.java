package io.renren.modules.common.utils;

import io.renren.modules.common.constant.KxConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cxy
 * @create 2022/6/22
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);


    /**
     * 如果文件夹不存在就创建文件夹
     *
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 保存文件到本地
     * @param result
     * @param outPath
     * @param outFileName
     * @throws Exception
     */
    public static void writeOcrStrtoFile(String result, String outPath, String outFileName)  {

        File dir = new File(outPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File txt = new File(outPath + "/" + outFileName);
        try {
            txt.createNewFile();
            byte bytes[] = new byte[512];
            bytes = result.getBytes();
            int b = bytes.length; // 是字节的长度，不是字符串的长度
            FileOutputStream fos = new FileOutputStream(txt);
//		fos.write(bytes, 0, b);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("保存文件出错",e);
        }
    }


    /**
     * 获取文件夹下所有文件的路径
     *
     * @param folderPath
     * @return
     */
    public static List<String> getFilePath(String folderPath) {
        File folder = new File(folderPath);
        List<String> filePathList = new ArrayList<>();
        String rootPath;
        if (folder.exists()) {
            String[] fileNameList = folder.list();
            if (null != fileNameList && fileNameList.length > 0) {
                if (folder.getPath().endsWith(File.separator)) {
                    rootPath = folder.getPath();
                } else {
                    rootPath = folder.getPath() + File.separator;
                }
                for (String fileName : fileNameList) {
                    filePathList.add(rootPath + fileName);
                }
            }
        }
        return filePathList;
    }

    /**
     * FileTime 转 Date
     *
     * @param fileTime
     * @return
     */
    public static String formatDateTime(FileTime fileTime) {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime localDateTime = fileTime

                .toInstant()

                .atZone(ZoneId.systemDefault())

                .toLocalDateTime();

        return localDateTime.format(DATE_FORMATTER);

    }

    /**
     * 新建文件夹
     * @return
     */
    public static String getFileFolder(String type) {
        Date millisecondDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        if("h".equals(type)){
            formatter = new SimpleDateFormat("yyyyMMddHH");
        }
        if("d".equals(type)){
            formatter = new SimpleDateFormat("yyyyMMdd");
        }
        String millisecondStrings = formatter.format(millisecondDate);
        String filePath="";
        if(!millisecondStrings.equals(filePath)){
            filePath=millisecondStrings;
        }
        String outImgFilePath= KxConstants.IMG_UPLOAD+filePath+"D:/imgHK189//";
        File file = new File(outImgFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return outImgFilePath;
    }


    /**
     * 生成识别图片
     *
     * @param imgBase64      识别后端图片base64
     * @param outImgFilePath
     * @param cameraName
     * @param fileFolderName
     */
    public static void createImg(String imgBase64, String outImgFilePath, String cameraName, String fileFolderName) {

        String outPath = outImgFilePath + fileFolderName;
        File dir = new File(outPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        String jpgFile = Paths.get(outPath, cameraName + "-" + txt + "-a.jpeg").toString();
        String jpgFile = Paths.get(outPath, cameraName+ "-a.jpeg").toString();
        ImageUtil.generateImage(imgBase64, jpgFile);


    }

}
