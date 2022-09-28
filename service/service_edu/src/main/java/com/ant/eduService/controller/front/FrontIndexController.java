package com.ant.eduService.controller.front;

import com.ant.commonutils.R;
import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.EduTeacher;
import com.ant.eduService.service.EduCourseService;
import com.ant.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduService/front")

public class FrontIndexController {

    private final EduCourseService eduCourseService;

    private final EduTeacherService eduTeacherService;

    public FrontIndexController(EduCourseService eduCourseService, EduTeacherService eduTeacherService) {
        this.eduCourseService = eduCourseService;
        this.eduTeacherService = eduTeacherService;
    }

    /**
     * 根据id进行降序排序，获取前八个课程和前四个讲师
     * @return
     */
    @Cacheable(key = "'selectIndexList'",value = "HotTeacherAndCourse")
    @GetMapping("/get/teacherAndTeacher")
    public R getCourseAndTeacher(){
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("id");
        courseQueryWrapper.last("limit 8");

        List<EduCourse> courseList = eduCourseService.list(courseQueryWrapper);

        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByDesc("id");
        teacherQueryWrapper.last("limit 4");

        List<EduTeacher> teacherList = eduTeacherService.list(teacherQueryWrapper);

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);

    }
}
