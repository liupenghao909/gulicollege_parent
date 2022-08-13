package com.ant.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类
 *
 */
@Data
public class R {

    @ApiModelProperty(value = "响应是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据，放在键值对中")
    private Map<String,Object> data = new HashMap<>();

    // 把构造方法私有化
    private R(){

    }

    // 成功静态方法
    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");

        return r;
    }

    // 失败静态方法
    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");

        return r;
    }

    // 返回this为了能链式编程
    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public R data(Map<String,Object> map){
        this.setData(map);
        return this;
    }




}
