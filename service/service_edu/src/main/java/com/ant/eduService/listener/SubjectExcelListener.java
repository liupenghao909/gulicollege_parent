package com.ant.eduService.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ant.eduService.entity.EduSubject;
import com.ant.eduService.entity.excel.SubjectData;
import com.ant.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    private EduSubjectService eduSubjectService;

    public SubjectExcelListener() {}

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    // 读操作
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        // 向数据库中插入一级课程
        // 为避免重复插入，先查数据库中有没有课程
        EduSubject oneSubject = judgeExistOneSubject(subjectData);
        // 如果不存在则证明可以插入
        if (oneSubject == null) {
            oneSubject = new EduSubject();
            oneSubject.setTitle(subjectData.getOneSubject());
            oneSubject.setParentId("0");
            eduSubjectService.save(oneSubject);
        }

        // 向数据库中插入二级课程
        EduSubject twoSubject = judgeExistTwoSubject(subjectData, oneSubject.getId());
        if (twoSubject == null) {
            twoSubject = new EduSubject();
            twoSubject.setTitle(subjectData.getTwoSubject());
            twoSubject.setParentId(oneSubject.getId());
            eduSubjectService.save(twoSubject);
        }
    }

    // 判断一级课程是否存在
    private EduSubject judgeExistOneSubject(SubjectData subjectData) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", subjectData.getOneSubject())
                .eq("parent_id", "0");
        EduSubject oneSubject = eduSubjectService.getOne(queryWrapper);

        return oneSubject;
    }

    // 判断二级课程是否存在
    private EduSubject judgeExistTwoSubject(SubjectData subjectData, String pid) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", subjectData.getTwoSubject())
                .eq("parent_id", pid);
        EduSubject twoSubject = eduSubjectService.getOne(queryWrapper);

        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
