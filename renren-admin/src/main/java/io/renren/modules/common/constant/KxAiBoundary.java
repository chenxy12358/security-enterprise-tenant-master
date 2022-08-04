package io.renren.modules.common.constant;

import org.springframework.stereotype.Component;

/**
 * 常量表
 *
 * @author cxy
 */
@Component
public class KxAiBoundary {

    /**
     * 已标注
     */
    public static String BOUNDARY_STATUS_MARK = "0";

    /**
     * 已发送
     */
    public static String BOUNDARY_STATUS_SEND = "1";

    /**
     * 预置位发送session前缀
     */
    public static String PRESET_SEND = "YZW_SEND_";

    /**
     * 重合度
     */
    public static float boundary_in_similarity = 0f;

    /**
     * 除外重合度
     */
    public static float boundary_out_similarity = 1f;

    /**
     * 圈内
     */
    public static String TYPE_DETECT = "Detect";

    /**
     *圈外
     */
    public static String TYPE_BLOCK = "Block";

    /**
     * 识别图是否画出标记框
     */
    public static boolean DRAW_BOUNDARY =true;



}
