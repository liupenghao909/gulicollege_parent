package com.ant.eduService.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 进行讲师查询时可能用到的过滤条件
 */
@ApiModel(value = "Teacher查询对象",description = "讲师查询对象封装")
@Data
public class TeacherQuery implements Serializable {
    // 序列化ID
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教师名称，模糊查询")
    private String name;

    @ApiModelProperty("头衔 1高级讲师  2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间",example = "2019-01-01 10:10:10")
    private String begin;

    @ApiModelProperty(value = "查询结束时间",example = "2019-01-01 10:10:10")
    private String end;


}
