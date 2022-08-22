package io.renren.modules.AppGoods.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * 商品管理
 *
 * @author WEI 
 * @since 3.0 2022-08-13
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class AppGoodsExcel {
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
    @ExcelProperty(value = "环保积分", index = 8)
    private Integer normalPrice;
    @ExcelProperty(value = "组合-环保积分", index = 9)
    private Integer comNormalPrice;
    @ExcelProperty(value = "组合-实践积分", index = 10)
    private Integer comSpecialPrice;
    @ExcelProperty(value = "库存数量", index = 11)
    private Integer remainCount;
    @ExcelProperty(value = "已售数量", index = 12)
    private Integer saleCount;
    @ExcelProperty(value = "商品名称", index = 13)
    private String name;
    @ExcelProperty(value = "图片路径", index = 14)
    private String url;
}