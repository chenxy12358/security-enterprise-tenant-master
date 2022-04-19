package io.renren.modules.stationTrack.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 桩点位移轨迹
 *
 * @author cxy 
 * @since 3.0 2022-04-19
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class KxStationTrackExcel {
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
    @ExcelProperty(value = "桩点id", index = 6)
    private Long stationId;
    @ExcelProperty(value = "经度", index = 7)
    private BigDecimal lng;
    @ExcelProperty(value = "维度", index = 8)
    private BigDecimal lat;
    @ExcelProperty(value = "位置", index = 9)
    private String position;
    @ExcelProperty(value = "备注", index = 10)
    private String remark;
    @ExcelProperty(value = "是否删除", index = 11)
    private String deleted;
}