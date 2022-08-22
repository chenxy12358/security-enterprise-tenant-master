package io.renren.modules.AppArticles.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * 文章管理
 *
 * @author WEI 
 * @since 3.0 2022-08-17
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class AppArticlesExcel {
    @ExcelProperty(value = "id", index = 0)
    private Long id;
    @ExcelProperty(value = "备注", index = 1)
    private String remark;
    @ExcelProperty(value = "学校编号", index = 2)
    private Long schoolCode;
    @ExcelProperty(value = "租户编码", index = 3)
    private Long tenantCode;
    @ExcelProperty(value = "创建者", index = 4)
    private Long creator;
    @ExcelProperty(value = "创建时间", index = 5)
    private Date createDate;
    @ExcelProperty(value = "更新者", index = 6)
    private Long updater;
    @ExcelProperty(value = "更新时间", index = 7)
    private Date updateDate;
    @ExcelProperty(value = "标题", index = 8)
    private String title;
    @ExcelProperty(value = "内容", index = 9)
    private String content;
    @ExcelProperty(value = "发布时间", index = 10)
    private Date pubDate;
    @ExcelProperty(value = "阅读次数", index = 11)
    private Integer readCount;
    @ExcelProperty(value = "文章类型", index = 12)
    private String type;
}