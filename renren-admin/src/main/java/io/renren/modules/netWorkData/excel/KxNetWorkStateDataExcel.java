package io.renren.modules.netWorkData.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 网络状态信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-19
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxNetWorkStateDataExcel {
    @ExcelProperty(value = "更新时间", index = 0)
    private Date updateDate;
    @ExcelProperty(value = "设备id", index = 1)
    private Long deviceid;
    @ExcelProperty(value = "桩点名称", index = 2)
    private Long stationid;
    @ExcelProperty(value = "内容", index = 3)
    private Object content;
    @ExcelProperty(value = "网卡名称", index = 4)
    private String nicName;
    @ExcelProperty(value = "网络模式", index = 5)
    private String accessTech;
    @ExcelProperty(value = "运营商", index = 6)
    private String operatorName;
    @ExcelProperty(value = "信号质量", index = 7)
    private String signalQuality;
    @ExcelProperty(value = "属性", index = 8)
    private Object properties;
}