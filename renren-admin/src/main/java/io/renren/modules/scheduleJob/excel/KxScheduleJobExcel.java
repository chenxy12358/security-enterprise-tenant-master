package io.renren.modules.scheduleJob.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 计划任务
 *
 * @author cxy 
 * @since 3.0 2022-02-27
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxScheduleJobExcel {
    @ExcelProperty(value = "租户编码", index = 0)
    private Long tenantCode;
    @ExcelProperty(value = "创建者", index = 1)
    private Long creator;
    @ExcelProperty(value = "创建时间", index = 2)
    private Date createDate;
    @ExcelProperty(value = "更新者", index = 3)
    private Long updater;
    @ExcelProperty(value = "更新时间", index = 4)
    private Date updateDate;
    @ExcelProperty(value = "名称", index = 5)
    private String name;
    @ExcelProperty(value = "站点id", index = 6)
    private Long deviceId;
    @ExcelProperty(value = "设备类型id", index = 7)
    private Long deviceTypeId;
    @ExcelProperty(value = "设备模式id", index = 8)
    private Long deviceModelId;
    @ExcelProperty(value = "属性", index = 9)
    private Object properties;
    @ExcelProperty(value = "事件类型", index = 10)
    private String itemType;
    @ExcelProperty(value = "版本", index = 11)
    private String version;
    @ExcelProperty(value = "内容", index = 12)
    private Object content;
    @ExcelProperty(value = "状态", index = 13)
    private String status;
    @ExcelProperty(value = "备注", index = 14)
    private String remark;
    @ExcelProperty(value = "机构id", index = 15)
    private Long orgId;
    @ExcelProperty(value = "是否删除", index = 16)
    private String deleted;
    @ExcelProperty(value = "是否可用", index = 17)
    private String enable;
    @ExcelProperty(value = "删除者", index = 18)
    private Long deleter;
    @ExcelProperty(value = "删除时间", index = 19)
    private Date deletedTime;
}