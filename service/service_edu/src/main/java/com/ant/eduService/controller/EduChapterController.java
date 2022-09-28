package com.ant.eduService.controller;


import com.ant.commonutils.R;
import com.ant.eduService.entity.EduChapter;
import com.ant.eduService.entity.chapter.ChapterVO;
import com.ant.eduService.service.EduChapterService;
import com.ant.servicebase.exceptionhandler.GuliException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程章节信息 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
@RestController
@RequestMapping("/eduService/eduChapter")

public class EduChapterController {


    private final EduChapterService eduChapterService;

    public EduChapterController(EduChapterService eduChapterService) {
        this.eduChapterService = eduChapterService;
    }

    /**
     * 根据课程ID查询出课程下的所有章节、小节
     * @param courseId
     * @return
     */
    @GetMapping("/get/chapterAndVideo/list/{courseId}")
    public R getSubjectVideoList(@PathVariable("courseId") String courseId){
        List<ChapterVO> chapterVOList = eduChapterService.getSubjectVideoList(courseId);

        return R.ok().data("chapterVideoList", chapterVOList);
    }

    /**
     * 根据chapterId获取章节信息
     * @param chapterId
     * @return
     */
    @GetMapping("/get/chapter/{chapterId}")
    public R getChapterById(@PathVariable("chapterId") String chapterId) {
        EduChapter chapter = eduChapterService.getById(chapterId);

        return R.ok().data("chapter",chapter);
    }

    /**
     * 保存章节信息到数据库表中
     */
    @PostMapping("/add/chapter")
    public R addChapter(@RequestBody EduChapter chapter){
        boolean save = eduChapterService.save(chapter);

        if(!save){
            throw new GuliException(20001,"保存章节信息失败");
        }

        return R.ok();
    }

    /**
     * 更新章节信息
     * @param chapter
     * @return
     */
    @PostMapping("/update/chapter")
    public R updateChapter(@RequestBody EduChapter chapter){
        boolean b = eduChapterService.updateById(chapter);
        if(!b){
            throw new GuliException(20001,"更新章节信息失败");
        }

        return R.ok();
    }

    /**
     * 删除章节信息
     *  如果章节下存在小节，则不能删除
     *  否则能删除
     * @param chapterId
     * @return
     */
    @GetMapping("/delete/chapter/{chapterId}")
    public R deleteChapterById(@PathVariable("chapterId") String chapterId){
        // 删除章节信息，如果章节下有小节信息则删除失败，否则删除成功
        boolean isSuccess = eduChapterService.deleteChapterById(chapterId);

        if(isSuccess){
            return R.ok();
        }

        return R.error();
    }


}

