package com.ant.eduService.service;

import com.ant.eduService.entity.EduSubject;
import com.ant.eduService.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author ant
 * @since 2022-08-29
 */
public interface EduSubjectService extends IService<EduSubject> {

    List<OneSubject> getSubjectList();
}
