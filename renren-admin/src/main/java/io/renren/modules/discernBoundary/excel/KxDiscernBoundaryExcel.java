package io.renren.modules.discernBoundary.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * 识别边界标定
 *
 * @author cxy 
 * @since 3.0 2022-07-19
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxDiscernBoundaryExcel {
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
    @ExcelProperty(value = "设备类型id", index = 7)
    private Long deviceId;
    @ExcelProperty(value = "设备编号", index = 8)
    private String deviceNo;
    @ExcelProperty(value = "相机名称", index = 9)
    private String cameraName;
    @ExcelProperty(value = "预置位", index = 10)
    private String presetNo;
    @ExcelProperty(value = "图片尺寸", index = 11)
    private String sessionTime;
    @ExcelProperty(value = "是否可用", index = 12)
    private String enable;
    @ExcelProperty(value = "备注", index = 13)
    private String remark;
    @ExcelProperty(value = "是否删除", index = 14)
    private String deleted;
    @ExcelProperty(value = "删除者", index = 15)
    private Long deleter;
    @ExcelProperty(value = "删除时间", index = 16)
    private Date deletedTime;
}