package io.renren.modules.common.constant;

import org.springframework.stereotype.Component;

/**
 * 常量表
 *
 * @author cxy
 */
@Component
public class DeviceInterfaceConstants {

    /**
     *  Emd.Method.Normal 接口
     */
    public static String INTERFACE_NORMAL="Emd.Method.Normal";

    /**
     *  Emd.Method.CTRL ptz控制 接口
     */
    public static String INTERFACE_CTRL="Emd.Method.Ctrl";


    //Method
    /**
     * 获取本地音频文件列表
     */
    public static String METHOD_GETAUDIOLIST="GetAudioList";

    /**
     * 发送配置
     */
    public static String METHOD_SETCONFIG="SetConfig";

    /**
     * 设置AI任务
     */
    public static String METHOD_SETAITASK="SetAITask";

    /**
     * 设置AI报警参数
     */
    public static String METHOD_SETALARMPARAM="SetAlarmParam";

    /**
     * 通知设备保持推送流
     */
    public static String METHOD_KEEPVIDEOSTREAM="KeepVideoStream";

    /**
     * 获取BaseInfo的json
     */
    public static String METHOD_GETSYSBASEINFO="GetSysBaseInfo";

    /**
     * 发送视频流到流媒体
     */
    public static String METHOD_SENDVIDEOSTREAM="SendVideoStream";

    /**
     * 发送ptz控制
     */
    public static String METHOD_PTZCONTROL="PtzControl";
    /**
     * VPN
     */
    public static String METHOD_VPN_CONNECT="Connect";
    public static String METHOD_VPN_GETSTAT="GetStat";
    public static String METHOD_VPN_CLOSE="Close";

}
