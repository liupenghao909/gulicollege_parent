package com.ant.eduService.controller;


import com.ant.commonutils.R;
import com.ant.eduService.entity.vo.CourseInfoVO;
import com.ant.eduService.service.EduCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
@RestController
@RequestMapping("/eduService/eduCourse")
@CrossOrigin
public class EduCourseController {
    private final Logger log = LoggerFactory.getLogger(EduLoginController.class);

    private final EduCourseService eduCourseService;

    public EduCourseController(EduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
    }

    @PostMapping("/save/course/info")
    public R saveCourseInfo(@RequestBody CourseInfoVO courseInfoVO){
        log.info("Rest to save course info");

        eduCourseService.saveCourseInfo(courseInfoVO);

        return R.ok();
    }

}

