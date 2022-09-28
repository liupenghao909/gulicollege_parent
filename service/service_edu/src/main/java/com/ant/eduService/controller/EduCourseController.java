package com.ant.eduService.controller;


import com.ant.commonutils.R;
import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.EduTeacher;
import com.ant.eduService.entity.vo.CourseInfoVO;
import com.ant.eduService.entity.vo.CourseQuery;
import com.ant.eduService.entity.vo.PublishCourseInfoVO;
import com.ant.eduService.entity.vo.TeacherQuery;
import com.ant.eduService.service.EduCourseService;
import com.ant.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

public class EduCourseController {
    private final Logger log = LoggerFactory.getLogger(EduLoginController.class);

    private final EduCourseService eduCourseService;

    public EduCourseController(EduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
    }

    /**
     *
     * 将课程信息保存到数据库中
     * @param courseInfoVO
     * @return
     */
    @PostMapping("/save/course/info")
    public R saveCourseInfo(@RequestBody CourseInfoVO courseInfoVO){
        log.info("Rest to save course info");

        String courseId = eduCourseService.saveCourseInfo(courseInfoVO);

        return R.ok().data("courseId",courseId);
    }

    /**
     * 根据课程ID从数据库中获取课程信息
     */
    @GetMapping("/get/courseInfo/{courseId}")
    public R getCourseInfoById(@PathVariable("courseId") String courseId){
        CourseInfoVO courseInfoVO = eduCourseService.getCourseInfoById(courseId);

        return R.ok().data("courseInfoVO",courseInfoVO);
    }

    @PostMapping("/update/courseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVO courseInfoVO){
        eduCourseService.updateCourseInfo(courseInfoVO);

        return R.ok();
    }


    @GetMapping("/get/publishCourse/{courseId}")
    public R getPublishCourseInfo(@PathVariable("courseId") String courseId){

        PublishCourseInfoVO publishCourseInfoVO = eduCourseService.getPublishCourseInfo(courseId);

        return R.ok().data("publishCourseInfoVO",publishCourseInfoVO);
    }

    @GetMapping("/publish/course/{courseId}")
    public R publishCourse(@PathVariable("courseId") String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        // 更新发布状态
        eduCourse.setStatus(CourseStatusEnum.COURSE_STATUS_Normal.getStatus());

        eduCourseService.updateById(eduCourse);

        return R.ok();
    }

    enum CourseStatusEnum{
        //  未发布
        COURSE_STATUS_Draft("Draft"),
        // 已发布
        COURSE_STATUS_Normal("Normal");

        private String status;

        CourseStatusEnum(String status){
            this.status = status;
        }


        public String getStatus() {
            return status;
        }
    }

    /**
     * 带条件的分页查询课程
     */
    @PostMapping ("/pageCourseCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable("current") Long current,
                                  @PathVariable("limit") Long limit,
                                  // 请求体
                                  @RequestBody(required = false) CourseQuery courseQuery){
        log.info("Rest to get course page with condition");
        // 创建page对象
        Page<EduCourse> page = new Page<>(current,limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String courseTitle = courseQuery.getTitle();
        String courseStatus = courseQuery.getStatus();
        if(!StringUtils.isEmpty(courseTitle)){
            // 构建条件
            wrapper.like("title",courseTitle);   // 模糊查询
        }

        if(!StringUtils.isBlank(courseStatus)){
            wrapper.eq("status",courseStatus);
        }

        wrapper.orderByDesc("gmt_create");

        // 多条件组合查询
        eduCourseService.page(page,wrapper);
        // 调用方法实现条件查询分页
        return R.ok().data("total",page.getTotal()).data("items",page.getRecords());
    }


    /**
     * 删除课程
     * 在删除课程时，要删除课程对应的小节、章节、描述以及课程本身
     * @param courseId
     * @return
     */
    @GetMapping("/delete/course/{courseId}")
    public R deleteCourseById(@PathVariable("courseId") String courseId){
        eduCourseService.deleteCourseById(courseId);

        return R.ok();

    }

}

