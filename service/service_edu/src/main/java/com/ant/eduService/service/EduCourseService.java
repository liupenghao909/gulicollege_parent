package com.ant.eduService.service;

import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.vo.CourseInfoVO;
import com.ant.eduService.entity.vo.PublishCourseInfoVO;
import com.ant.eduService.entity.vo.frontVO.FrontCourseSearchConditionVO;
import com.ant.eduService.entity.vo.frontVO.WebCourseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVO courseInfoVO);

    CourseInfoVO getCourseInfoById(String courseId);

    void updateCourseInfo(CourseInfoVO courseInfoVO);

    PublishCourseInfoVO getPublishCourseInfo(String courseId);

    void deleteCourseById(String courseId);

    Map<String, Object> getCourseListWithCondition(Page<EduCourse> pageParam, FrontCourseSearchConditionVO courseSearchConditionVO);

    WebCourseVO getWebCourseInfo(String courseId);
}
