package com.ant.eduService.service.impl;

import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.EduCourseDescription;
import com.ant.eduService.entity.EduTeacher;
import com.ant.eduService.entity.vo.CourseInfoVO;
import com.ant.eduService.entity.vo.PublishCourseInfoVO;
import com.ant.eduService.entity.vo.frontVO.FrontCourseSearchConditionVO;
import com.ant.eduService.entity.vo.frontVO.WebCourseVO;
import com.ant.eduService.mapper.EduCourseMapper;
import com.ant.eduService.service.EduChapterService;
import com.ant.eduService.service.EduCourseDescriptionService;
import com.ant.eduService.service.EduCourseService;
import com.ant.eduService.service.EduVideoService;
import com.ant.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final EduVideoService eduVideoService;

    private final EduChapterService eduChapterService;

    public EduCourseServiceImpl(EduCourseDescriptionService eduCourseDescriptionService, EduVideoService eduVideoService, EduChapterService eduChapterService) {
        this.eduCourseDescriptionService = eduCourseDescriptionService;
        this.eduVideoService = eduVideoService;
        this.eduChapterService = eduChapterService;
    }

    /**
     * 保存课程信息
     * @param courseInfoVO
     * @return
     */
    @Override
    public String saveCourseInfo(CourseInfoVO courseInfoVO) {

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

        return eduCourse.getId();


    }

    @Override
    public CourseInfoVO getCourseInfoById(String courseId) {
        // 获取课程基本信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        // 获取课程描述信息
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);

        // 数据封装
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        BeanUtils.copyProperties(eduCourse,courseInfoVO);
        courseInfoVO.setDescription(eduCourseDescription.getDescription());

        return courseInfoVO;
    }

    @Override
    public void updateCourseInfo(CourseInfoVO courseInfoVO) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO,eduCourse);
        int update = baseMapper.updateById(eduCourse);

        if(update < 1){
            throw new GuliException(20001,"更新课程基本信息失败");
        }

        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVO.getId());
        eduCourseDescription.setDescription(courseInfoVO.getDescription());

        boolean isSuccess = eduCourseDescriptionService.updateById(eduCourseDescription);
        if(!isSuccess){
            throw new GuliException(20001,"更新课程描述信息失败");
        }
    }

    @Override
    public PublishCourseInfoVO getPublishCourseInfo(String courseId) {
        PublishCourseInfoVO publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);

        return publishCourseInfo;
    }

    @Override
    public void deleteCourseById(String courseId) {
        // 删除小节
        eduVideoService.deleteVideoByCourseId(courseId);
        // 删除章节
        eduChapterService.deleteChapterByCourseId(courseId);
        // 删除描述
        eduCourseDescriptionService.removeById(courseId);
        // 删除课程本身
        baseMapper.deleteById(courseId);
    }

    /**
     * 前台网站 课程带条件的分页查询
     * @param pageParam
     * @param courseSearchConditionVO
     * @return
     */
    @Override
    public Map<String, Object> getCourseListWithCondition(Page<EduCourse> pageParam, FrontCourseSearchConditionVO courseSearchConditionVO) {
        QueryWrapper<EduCourse> queryWrapper = null;
        // 拼接查询条件
        if(courseSearchConditionVO != null){
            queryWrapper = new QueryWrapper<>();

            // 一级分类
            if(!StringUtils.isBlank(courseSearchConditionVO.getSubjectParentId())){
                queryWrapper.eq("subject_parent_id",courseSearchConditionVO.getSubjectParentId());
            }

            // 二级分类
            if(!StringUtils.isBlank(courseSearchConditionVO.getSubjectId())){
                queryWrapper.eq("subject_id",courseSearchConditionVO.getSubjectId());
            }

            // 销量排行
            if(!StringUtils.isBlank(courseSearchConditionVO.getBuyCountSort())){
                queryWrapper.orderByDesc("buy_count");
            }

            // 最新时间排序
            if(!StringUtils.isBlank(courseSearchConditionVO.getGmtCreateSort())){
                queryWrapper.orderByDesc("gmt_create");
            }

            // 销量排行
            if(!StringUtils.isBlank(courseSearchConditionVO.getPriceSort())){
                queryWrapper.orderByDesc("price");
            }

        }
        baseMapper.selectPage(pageParam,queryWrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public WebCourseVO getWebCourseInfo(String courseId) {
        WebCourseVO webCourseVO = baseMapper.getWebCourseInfo(courseId);

        return webCourseVO;
    }
}
