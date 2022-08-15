package io.renren.modules.common.constant;

import org.springframework.beans.factory.annotation.Value;
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
//    public static String IMG_SERVER_URL="http://117.175.158.169:8082/renren-admin/";

    /**
     * 服务器图片保存路径 计划任务 job-data 预警图片 alarm-data
     */
    public static String IMG_UPLOAD="D:/upload/";

    /**
     * 计划任务文件夹
     */
    public static String IMG_JOB="job-data";

    /**
     * 预警图片文件夹
     */
    public static String IMG_ALARM="alarm-data";

    /**
     * 预置位文件夹
     */
    public static String IMG_PRESET="preset-data";

    /**
     * 是
     */
    public static String YSC="t" ;

    /**
     * 否
     */
    public static String NO="f";


    /**
     * 识别类型码表
     */
    public static String KX_DISCERN_TYPE="kx_discern_type";


    /**
     * 流媒体服务器地址
     */
    public static String STREAMING_MEDIA_SERVERADDR;

    /**
     * 流媒体服务器端口
     */
    public static String STREAMING_MEDIA_SERVERPORT;

    public String getStreamingMediaServeraddr() {
        return STREAMING_MEDIA_SERVERADDR;
    }

    @Value("${kxkj.streamingMedia.host}")
    public void setStreamingMediaServeraddr(String streamingMediaServeraddr) {
        STREAMING_MEDIA_SERVERADDR = streamingMediaServeraddr;
    }

    public String getStreamingMediaServerport() {
        return STREAMING_MEDIA_SERVERPORT;
    }

    @Value("${kxkj.streamingMedia.port}")
    public void setStreamingMediaServerport(String streamingMediaServerport) {
        STREAMING_MEDIA_SERVERPORT = streamingMediaServerport;
    }
}
