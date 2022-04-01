package io.renren.modules.pub.excel;

//import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 通讯记录表
 *
 * @author zhengweicheng
 */
@Data
public class SocketExcel {
  //  @Excel(name = "")
    private Long id;
  //  @Excel(name = "协议类型")
    private String type;
  //  @Excel(name = "位置")
    private String addr;
   // @Excel(name = "协议内容")
    private String content;
  //  @Excel(name = "创建时间")
    private Date createDate;

}