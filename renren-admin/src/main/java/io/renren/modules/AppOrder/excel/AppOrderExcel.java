package io.renren.modules.AppOrder.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * 兑换记录
 *
 * @author WEI 
 * @since 3.0 2022-08-13
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class AppOrderExcel {
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
    @ExcelProperty(value = "订单编号", index = 7)
    private String transactionId;
    @ExcelProperty(value = "订单状态", index = 8)
    private String status;
    @ExcelProperty(value = "商品名称", index = 9)
    private String goodsName;
    @ExcelProperty(value = "商品數量", index = 10)
    private Integer goodsCount;
    @ExcelProperty(value = "图片地址", index = 11)
    private String url;
    @ExcelProperty(value = "总环保积分", index = 12)
    private Integer totalNamorl;
    @ExcelProperty(value = "总实践积分", index = 13)
    private Integer totalSpecial;
    @ExcelProperty(value = "用户id", index = 14)
    private Long userId;
}