package io.renren.modules.common.constant;

import org.springframework.stereotype.Component;

/**
 * 常量表
 *
 * @author cxy
 */
@Component
public class KxConstants {

    /**
     * 图片分析服务器地址服务器
     */
    public static String IMG_SERVER_URL="http://117.175.158.169:8082/renren-admin/";

    /**
     * 服务器图片保存路径 计划任务 job-data 预警图片 alarm-data
     */
    public static String IMG_UPLOAD="C:/upload/";

    /**
     * 计划任务文件夹
     */
    public static String IMG_JOB="job-data";

    /**
     * 预警图片文件夹
     */
    public static String IMG_ALARM="alarm-data";

}
