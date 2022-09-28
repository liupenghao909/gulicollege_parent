package com.ant.eduService.controller.front;


import com.ant.commonutils.R;
import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.EduTeacher;
import com.ant.eduService.service.EduCourseService;
import com.ant.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduService/frontTeacher")
 // 跨域
public class FrontTeacherController {
    private final Logger log = LoggerFactory.getLogger(FrontTeacherController.class);

    private final EduTeacherService eduTeacherService;

    private final EduCourseService eduCourseService;

    public FrontTeacherController(EduTeacherService eduTeacherService, EduCourseService eduCourseService) {
        this.eduTeacherService = eduTeacherService;
        this.eduCourseService = eduCourseService;
    }


    @GetMapping("/get/teacherList/{page}/{limit}")
    public R getTeacherListWithPage(@PathVariable("page")Long page,@PathVariable("limit")Long limit){
        log.info("Rest to get teacher list in web front");

        Page<EduTeacher> pageParam = new Page<>(page, limit);
        Map<String,Object> map = eduTeacherService.getTeacherListInFront(pageParam);

        return R.ok().data("map",map);
    }

    // 讲师详情页面信息
    @GetMapping("/get/teacherAndCourseInfo/{teacherId}")
    public R getTeacherAndCourseInfo(@PathVariable("teacherId") String teacherId){
        // 获取讲师详情信息
        EduTeacher teacherInfo = eduTeacherService.getById(teacherId);

        // 获取讲师所讲课程的信息
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = eduCourseService.list(queryWrapper);

        return R.ok().data("teacherInfo",teacherInfo).data("courseList",courseList);
    }

}
