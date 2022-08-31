package com.ant.eduService.service;

import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.vo.CourseInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
public interface EduCourseService extends IService<EduCourse> {

    void saveCourseInfo(CourseInfoVO courseInfoVO);
}
