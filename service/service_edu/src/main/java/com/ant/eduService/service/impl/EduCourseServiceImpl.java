package com.ant.eduService.service.impl;

import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.EduCourseDescription;
import com.ant.eduService.entity.vo.CourseInfoVO;
import com.ant.eduService.mapper.EduCourseMapper;
import com.ant.eduService.service.EduCourseDescriptionService;
import com.ant.eduService.service.EduCourseService;
import com.ant.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    private final EduCourseDescriptionService eduCourseDescriptionService;

    public EduCourseServiceImpl(EduCourseDescriptionService eduCourseDescriptionService) {
        this.eduCourseDescriptionService = eduCourseDescriptionService;
    }

    /**
     * 保存课程信息
     * @param courseInfoVO
     */
    @Override
    public void saveCourseInfo(CourseInfoVO courseInfoVO) {

        // 将表单中课程表中的信息插入到课程表中
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO, eduCourse);
        int insertNum = baseMapper.insert(eduCourse);

        if(insertNum < 0) {
            throw new GuliException(20001,"向edu_course表中插入课程信息失败");
        }


        // 将表单中课程描述表中的信息插入到课程描述表中
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVO.getDescription());
        // 一门课的信息存储在不同的表中保持id一样，实现一对一对应关系
        eduCourseDescription.setId(eduCourse.getId());

        boolean save = eduCourseDescriptionService.save(eduCourseDescription);

        if(!save){
            throw new GuliException(20001,"向edu_course_description表中插入课程信息失败");
        }


    }
}
