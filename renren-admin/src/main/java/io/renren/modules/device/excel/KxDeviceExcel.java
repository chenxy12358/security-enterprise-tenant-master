package io.renren.modules.device.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 设备信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-16
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxDeviceExcel {
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
    @ExcelProperty(value = "序号", index = 6)
    private String serialNo;
    @ExcelProperty(value = "制造厂", index = 7)
    private String manufactory;
    @ExcelProperty(value = "协议类型", index = 8)
    private String proType;
    @ExcelProperty(value = "协议", index = 9)
    private String protocols;
    @ExcelProperty(value = "基本信息", index = 10)
    private String baseInfo;
    @ExcelProperty(value = "状态", index = 11)
    private String status;
    @ExcelProperty(value = "机构id", index = 12)
    private Long orgId;
    @ExcelProperty(value = "站点id", index = 13)
    private Long stationId;
    @ExcelProperty(value = "设备类型id", index = 14)
    private Long deviceTypeId;
    @ExcelProperty(value = "设备模式id", index = 15)
    private Long deviceModelId;
    @ExcelProperty(value = "任务id", index = 16)
    private Long scheduleJobId;
    @ExcelProperty(value = "属性", index = 17)
    private String properties;
    @ExcelProperty(value = "备注", index = 18)
    private String remark;
    @ExcelProperty(value = "是否删除", index = 19)
    private String deleted;
    @ExcelProperty(value = "是否可用", index = 20)
    private String enable;
    @ExcelProperty(value = "删除者", index = 21)
    private Long deleter;
    @ExcelProperty(value = "删除时间", index = 22)
    private Date deletedTime;
}