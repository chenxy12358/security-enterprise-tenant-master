package io.renren.modules.AppUser.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * APP用户
 *
 * @author WEI 
 * @since 3.0 2022-08-22
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class AppUserExcel {
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
    @ExcelProperty(value = "人员类型", index = 7)
    private String type;
    @ExcelProperty(value = "昵称", index = 8)
    private String nickName;
    @ExcelProperty(value = "手机号码", index = 9)
    private String phoneNum;
    @ExcelProperty(value = "学校名称", index = 10)
    private String schoolName;
    @ExcelProperty(value = "专业", index = 11)
    private String professional;
    @ExcelProperty(value = "班级", index = 12)
    private String classes;
    @ExcelProperty(value = "班級", index = 13)
    private String studentNum;
}