package com.ant.eduService.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 进行课程查询时可能用到的过滤条件
 */
@Data
public class CourseQuery implements Serializable {
    // 序列化ID
    private static final long serialVersionUID = 1L;

    // 课程名称
    private String title;

    // 课程状态
    private String status;

}
