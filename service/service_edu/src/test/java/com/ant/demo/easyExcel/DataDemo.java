package com.ant.demo.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataDemo {
    @ExcelProperty(value = "学生编号", index = 0)  // 设置表头名称
    private Integer sno;

    @ExcelProperty(value = "学生姓名",index = 1)
    private String sname;
}
