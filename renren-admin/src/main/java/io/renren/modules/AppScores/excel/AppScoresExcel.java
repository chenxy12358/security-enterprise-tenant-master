package io.renren.modules.AppScores.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * 积分记录
 *
 * @author WEI 
 * @since 3.0 2022-08-13
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class AppScoresExcel {
    @ExcelProperty(value = "id", index = 0)
    private Long id;
    @ExcelProperty(value = "备注", index = 1)
    private String remark;
    @ExcelProperty(value = "租户编码", index = 2)
    private Long tenantCode;
    @ExcelProperty(value = "创建者", index = 3)
    private Long creator;
    @ExcelProperty(value = "创建时间", index = 4)
    private Date createDate;
    @ExcelProperty(value = "更新者", index = 5)
    private Long updater;
    @ExcelProperty(value = "更新时间", index = 6)
    private Date updateDate;
    @ExcelProperty(value = "用户id", index = 7)
    private Long userId;
    @ExcelProperty(value = "环保积分", index = 8)
    private Integer normalScore;
    @ExcelProperty(value = "实践积分", index = 9)
    private Integer specialScore;
}