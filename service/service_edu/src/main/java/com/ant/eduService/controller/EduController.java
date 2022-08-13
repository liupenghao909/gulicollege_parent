package com.ant.eduService.controller;


import com.ant.commonutils.R;
import com.ant.eduService.entity.EduTeacher;
import com.ant.eduService.entity.vo.TeacherQuery;
import com.ant.eduService.service.EduTeacherService;


import com.ant.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-05-28
 */
@Api(value = "讲师管理")
@RestController
@RequestMapping("/eduService/teacher")
@CrossOrigin
public class EduController {
    private final Logger log = LoggerFactory.getLogger(EduController.class);

    private final EduTeacherService eduTeacherService;

    public EduController(EduTeacherService eduTeacherService) {
        this.eduTeacherService = eduTeacherService;
    }

    // 1. 查询讲师表所有数据
    // rest风格
    @GetMapping("/findAllTeacher")
    @ApiOperation(value = "所有讲师列表")
    public R findAllTeacher(){
        log.info("Rest to get all teachers");

        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据ID删除讲师")
    public R removeTeacher(@ApiParam(name = "id",value = "讲师ID",required = true) @PathVariable String id){
        log.info("Rest to delete teacher by id");

        boolean b = eduTeacherService.removeById(id);
        if(b==true){
            return R.ok();
        }

        return R.error();
    }

    // 3 分页查询讲师的方法
    // current为当前页，limit为每页记录数
    @GetMapping("/pageTeacher/{current}/{limit}")
    @ApiOperation(value = "分页查询讲师")
    public R pageListTeacher(@ApiParam(value = "当前页")@PathVariable(value = "current") Long current,
                             @ApiParam(value = "每页记录数")@PathVariable(value = "limit") Long limit){
        log.info("Rest to search teachers in page");

        // 创建Page对象
        Page<EduTeacher> page = new Page<>(current,limit);
        // 调用方法实现分页
        eduTeacherService.page(page, null);
        // 调用方法时候，底层封装，把分页所有数据封装到对象里
        List<EduTeacher> teachers = page.getRecords();
        long total = page.getTotal();

        return R.ok().data("total",total).data("rows",teachers);

    }

    // 带分页的条件查询的方法
    @ApiOperation("带分页的条件查询")
    @PostMapping ("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable("current") Long current,
                                  @PathVariable("limit") Long limit,
                                  // 请求体
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        log.info("Rest to get teacher page with condition");
        // 创建page对象
        Page<EduTeacher> page = new Page<>(current,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)){
            // 构建条件
            wrapper.like("name",name);   // 模糊查询
        }

        if(level != null){
            wrapper.eq("level",level);
        }

        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }

        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }

        wrapper.orderByDesc("gmt_create");

        // 多条件组合查询
        eduTeacherService.page(page,wrapper);
        // 调用方法实现条件查询分页
        return R.ok().data("total",page.getTotal()).data("items",page.getRecords());
    }

    // 添加讲师方法
    @ApiOperation(value = "添加讲师")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        log.info("Rest to add teacher");

        boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }

        return R.error();
    }

    // 根据id值查询讲师信息
    @GetMapping("/getTeacher/{id}")
    public R getTeacherById(@ApiParam("讲师id值") @PathVariable("id") String id){
        log.info("Rest to get teacher by id");
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("item",teacher);
    }

    // 修改讲师
    @PostMapping("updateTeacher")
    public R updateTeacherById(@RequestBody EduTeacher eduTeacher){
        log.info("Rest to update teacher by id");
        boolean b = eduTeacherService.updateById(eduTeacher);
        if(b){
            return R.ok();
        }

        return R.error();


    }

}

