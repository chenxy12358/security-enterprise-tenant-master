package io.renren.modules.battery.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * 电池信息 
 *
 * @author zhengweicheng 
 * @since 3.0 2022-02-08
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxBatteryExcel {
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
    @ExcelProperty(value = "设备id", index = 6)
    private Long deviceId;
    @ExcelProperty(value = "站点id", index = 7)
    private Long stationId;
    @ExcelProperty(value = "内容", index = 8)
    private Object content;
    @ExcelProperty(value = "属性", index = 9)
    private Object properties;
    @ExcelProperty(value = "备注", index = 10)
    private String remark;
}