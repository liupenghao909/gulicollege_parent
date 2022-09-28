package com.ant.eduService.service;

import com.ant.eduService.entity.EduComment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author ant
 * @since 2022-09-22
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String, Object> getPageCommentByCourseId(Page<EduComment> pageParam, String courseId);
}
