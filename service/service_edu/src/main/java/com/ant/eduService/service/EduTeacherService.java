package com.ant.eduService.service;

import com.ant.eduService.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author ant
 * @since 2022-05-28
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String, Object> getTeacherListInFront(Page<EduTeacher> pageParam);
}
