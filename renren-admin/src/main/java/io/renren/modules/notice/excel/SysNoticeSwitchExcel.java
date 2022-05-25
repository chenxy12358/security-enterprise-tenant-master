package io.renren.modules.notice.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * notice
 *
 * @author WEI 
 * @since 3.0 2022-05-23
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysNoticeSwitchExcel {
    @ExcelProperty(value = "Long", index = 0)
    private Long userId;
    @ExcelProperty(value = "String", index = 1)
    private String status;
}