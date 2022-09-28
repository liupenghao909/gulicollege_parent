package com.ant.eduService.service.impl;

import com.ant.eduService.entity.EduChapter;
import com.ant.eduService.entity.EduVideo;
import com.ant.eduService.entity.chapter.ChapterVO;
import com.ant.eduService.entity.chapter.VideoVO;
import com.ant.eduService.mapper.EduChapterMapper;
import com.ant.eduService.service.EduChapterService;
import com.ant.eduService.service.EduVideoService;
import com.ant.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程章节信息 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    private final EduVideoService eduVideoService;

    public EduChapterServiceImpl(EduVideoService eduVideoService) {
        this.eduVideoService = eduVideoService;
    }

    @Override
    public List<ChapterVO> getSubjectVideoList(String courseId) {
        // 从数据库中查询出courseId所有的章节列表
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(chapterQueryWrapper);

        // 从数据哭中查询出courseId所有的小节列表
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(videoQueryWrapper);

        List<ChapterVO> finalList = new ArrayList<>();

        // 进行数据封装，封装到ChapterVO中
        eduChapterList.forEach(eduChapter -> {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(eduChapter, chapterVO);

            String chapterId = eduChapter.getId();
            List<VideoVO> videoVOList = eduVideoList.stream()
                    .filter(eduVideo -> eduVideo.getChapterId().equals(chapterId))
                    .map(eduVideo -> {
                        VideoVO videoVO = new VideoVO();
                        BeanUtils.copyProperties(eduVideo, videoVO);
                        return videoVO;
                    }).collect(Collectors.toList());
            chapterVO.setChildren(videoVOList);

            finalList.add(chapterVO);
        });

        return finalList;
    }

    @Override
    public boolean deleteChapterById(String chapterId) {
        // 查询章节下有没有小节
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(eduVideoQueryWrapper);
        // 有小节删除错误
        if(count > 0){
            return false;
        }

        int i = baseMapper.deleteById(chapterId);
        if(i <= 0){
            throw new GuliException(20001,"删除章节失败");
        }

        return true;
    }

    @Override
    public void deleteChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id",courseId);

        baseMapper.delete(eduChapterQueryWrapper);
    }

}
