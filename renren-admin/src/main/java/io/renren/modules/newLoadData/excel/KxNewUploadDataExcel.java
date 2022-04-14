package io.renren.modules.newLoadData.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 最新上传数据
 *
 * @author WEI 
 * @since 3.0 2022-04-14
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxNewUploadDataExcel {
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
    @ExcelProperty(value = "站点id", index = 6)
    private Long stationId;
    @ExcelProperty(value = "设备id", index = 7)
    private Long deviceId;
    @ExcelProperty(value = "String", index = 8)
    private String type;
}