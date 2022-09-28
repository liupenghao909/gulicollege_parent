package com.ant.eduService.service;

import com.ant.eduService.entity.EduChapter;
import com.ant.eduService.entity.chapter.ChapterVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程章节信息 服务类
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVO> getSubjectVideoList(String courseId);

    boolean deleteChapterById(String chapterId);

    void deleteChapterByCourseId(String courseId);
}
