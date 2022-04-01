package io.renren.modules.scheduleItem.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 计划任务拍照结果
 *
 * @author cxy 
 * @since 3.0 2022-03-05
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxScheduleItemExcel {
    @ExcelProperty(value = "租户编码", index = 0)
    private Long tenantCode;
    @ExcelProperty(value = "创建者", index = 1)
    private Long creator;
    @ExcelProperty(value = "创建时间", index = 2)
    private Date createDate;
    @ExcelProperty(value = "更新者", index = 3)
    private Long updater;
    @ExcelProperty(value = "创建时间", index = 4)
    private Date updateDate;
    @ExcelProperty(value = "设备编码", index = 5)
    private String deviceNo;
    @ExcelProperty(value = "设备id", index = 6)
    private Long deviceId;
    @ExcelProperty(value = "计划任务id", index = 7)
    private Long scheduleJobId;
    @ExcelProperty(value = "类型", index = 8)
    private String itemType;
    @ExcelProperty(value = "版本", index = 9)
    private String version;
    @ExcelProperty(value = "信号", index = 10)
    private String signalType;
    @ExcelProperty(value = "属性", index = 11)
    private Object properties;
    @ExcelProperty(value = "内容", index = 12)
    private Object content;
    @ExcelProperty(value = "备注", index = 13)
    private String remark;
    @ExcelProperty(value = "是否删除", index = 14)
    private String deleted;
}