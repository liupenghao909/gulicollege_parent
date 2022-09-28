package com.ant.eduService.service.impl;

import com.ant.eduService.entity.EduComment;
import com.ant.eduService.mapper.EduCommentMapper;
import com.ant.eduService.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-09-22
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Override
    public Map<String, Object> getPageCommentByCourseId(Page<EduComment> pageParam, String courseId) {
        QueryWrapper<EduComment> queryWrapper = null;
        if(!StringUtils.isBlank(courseId)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", courseId);
            queryWrapper.orderByDesc("gmt_create");
        }
        baseMapper.selectPage(pageParam,queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("items", pageParam.getRecords());
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());

        return map;

    }
}
