package com.ant.eduService.controller;


import com.alibaba.excel.EasyExcel;
import com.ant.commonutils.R;
import com.ant.eduService.entity.excel.SubjectData;
import com.ant.eduService.entity.subject.OneSubject;
import com.ant.eduService.listener.SubjectExcelListener;
import com.ant.eduService.service.EduSubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-08-29
 */
@RestController
@RequestMapping("/eduService/eduSubject")
  // 跨域
public class EduSubjectController {

    private final Logger log = LoggerFactory.getLogger(EduSubjectController.class);

    private final EduSubjectService eduSubjectService;

    public EduSubjectController(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }


    @PostMapping("/save")
    public R saveSubject(MultipartFile file){
        log.info("Rest to save subject info by reading subject excel file");
        try {
            EasyExcel.read(file.getInputStream(), SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e){
            log.debug("读取excel文件时发生错误", e);
        }

        return R.ok();
    }


    @GetMapping("/get/subject/list")
    public R getSubjectList(){

        List<OneSubject> oneSubjectList = eduSubjectService.getSubjectList();

        return R.ok().data("list",oneSubjectList);
    }

}

