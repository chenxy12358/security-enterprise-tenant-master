package io.renren.modules.gas.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 燃气信息 
 *
 * @author WEI 
 * @since 3.0 2022-02-18
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxGasDataExcel {
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
    @ExcelProperty(value = "更新时间", index = 5)
    private Date updateDate;
    @ExcelProperty(value = "设备id", index = 6)
    private Long deviceId;
    @ExcelProperty(value = "站点id", index = 7)
    private Long stationId;
    @ExcelProperty(value = "内容", index = 8)
    private Object content;
    @ExcelProperty(value = "浓度单位", index = 9)
    private String unit;
    @ExcelProperty(value = "类型", index = 10)
    private String gasType;
    @ExcelProperty(value = "传感器编号", index = 11)
    private Long sensorId;
    @ExcelProperty(value = "报警状态", index = 12)
    private String alarmStatus;
    @ExcelProperty(value = "一级告警设定值", index = 13)
    private Double firsLevelAlarm;
    @ExcelProperty(value = "当前浓度值", index = 14)
    private Double concentrationValue;
    @ExcelProperty(value = "二级告警设定值", index = 15)
    private Double secondaryLevelAlarm;
    @ExcelProperty(value = "属性", index = 16)
    private Object properties;
    @ExcelProperty(value = "备注", index = 17)
    private String remark;
}