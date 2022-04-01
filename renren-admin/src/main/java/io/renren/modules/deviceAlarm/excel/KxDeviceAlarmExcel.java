package io.renren.modules.deviceAlarm.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 可视化告警
 *
 * @author cxy 
 * @since 3.0 2022-02-25
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxDeviceAlarmExcel {
    @ExcelProperty(value = "id", index = 0)
    private Long id;
    @ExcelProperty(value = "租户编码", index = 1)
    private Long tenantCode;
    @ExcelProperty(value = "创建者", index = 2)
    private Long creator;
    @ExcelProperty(value = "创建时间", index = 3)
    private Date createDate;
    @ExcelProperty(value = "更新者", index = 4)
    private Long updater;
    @ExcelProperty(value = "创建时间", index = 5)
    private Date updateDate;
    @ExcelProperty(value = "设备编码", index = 6)
    private String deviceNo;
    @ExcelProperty(value = "设备id", index = 7)
    private Long deviceId;
    @ExcelProperty(value = "站点id", index = 8)
    private Long stationId;
    @ExcelProperty(value = "级别", index = 9)
    private Integer level;
    @ExcelProperty(value = "处理类型", index = 10)
    private String handleType;
    @ExcelProperty(value = "信号", index = 11)
    private String signal;
    @ExcelProperty(value = "发送信息", index = 12)
    private Object senderInfo;
    @ExcelProperty(value = "内容", index = 13)
    private Object content;
    @ExcelProperty(value = "备注", index = 14)
    private String remark;
}