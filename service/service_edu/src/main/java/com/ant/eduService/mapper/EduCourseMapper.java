package com.ant.eduService.mapper;

import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.vo.PublishCourseInfoVO;
import com.ant.eduService.entity.vo.frontVO.WebCourseVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程基本信息 Mapper 接口
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    PublishCourseInfoVO getPublishCourseInfo(String courseId);

    WebCourseVO getWebCourseInfo(String courseId);
}
