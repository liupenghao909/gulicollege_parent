package com.ant.eduService.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectData {
    // 一级课程
    @ExcelProperty(index = 0)
    private String oneSubject;

    // 二级课程
    @ExcelProperty(index = 1)
    private String twoSubject;
}
