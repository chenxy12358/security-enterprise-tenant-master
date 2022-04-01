package io.renren.modules.deviceData.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 设备位置数据
 *
 * @author cxy 
 * @since 3.0 2022-03-18
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxDeviceGpsExcel {
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
    @ExcelProperty(value = "经度", index = 8)
    private Double longitude;
    @ExcelProperty(value = "纬度", index = 9)
    private String latitude;
    @ExcelProperty(value = "传感器编号", index = 10)
    private Long sensorNo;
    @ExcelProperty(value = "内容", index = 11)
    private Object content;
    @ExcelProperty(value = "备注", index = 12)
    private String remark;
}