package io.renren.modules.common.constant;

import org.springframework.stereotype.Component;

/**
 * 常量表
 *
 * @author cxy
 */
@Component
public class AlertConstants {

    /**
     * 预警数据类型
     */

    /**
     * 图片
     */
    public static int ALERT_TYPE_IMG=1001 ;
    public static String ALERT_TYPE_IMG_NAME="图片预警" ;
    public static String ALERT_TYPE_IMG_TITLE="您有新的预警图片通知，请及时处理！" ;

    /**
     * 气体
     */
    public static int ALERT_TYPE_GAS=1002;
    public static String ALERT_TYPE_GAS_NAME="燃气预警";
    public static String ALERT_TYPE_GAS_TITLE="您有新的燃气数据预警通知，请及时查看！";


    /**
     * 温度预警
     */
    public static int ALERT_TYPE_TEMP =1003;
    public static String ALERT_TYPE_TEMP_NAME="温度预警";
    public static String ALERT_TYPE_TEMP_TITLE="您有新的温度数据预警通知，请及时查看！";

    /**
     * 倾斜预警
     */
    public static int ALERT_TYPE_TILT =1004;
    public static String ALERT_TYPE_TILT_NAME="倾斜预警";
    public static String ALERT_TYPE_TILT_TITLE="您有新的设备倾斜预警通知，请及时查看！";



    /**
     * 湿度预警
     */
    public static int ALERT_TYPE_HUMIDITY =1005;
    public static String ALERT_TYPE_HUMIDITY_NAME="湿度预警";
    public static String ALERT_TYPE_HUMIDITY_TITLE="您有新的湿度数据预警通知，请及时查看！";

}
