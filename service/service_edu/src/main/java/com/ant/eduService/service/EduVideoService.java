package com.ant.eduService.service;

import com.ant.eduService.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
public interface EduVideoService extends IService<EduVideo> {

    void deleteVideoByCourseId(String courseId);
}
