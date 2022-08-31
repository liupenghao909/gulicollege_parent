package com.ant.eduService.service.impl;

import com.ant.eduService.entity.EduSubject;
import com.ant.eduService.entity.subject.OneSubject;
import com.ant.eduService.entity.subject.TwoSubject;
import com.ant.eduService.mapper.EduSubjectMapper;
import com.ant.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-08-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    // 获取课程分类列表（树形）
    @Override
    public List<OneSubject> getSubjectList() {
        // 获取一级课程分类
        QueryWrapper<EduSubject> oneSubjectQueryWrapper = new QueryWrapper<>();
        oneSubjectQueryWrapper.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(oneSubjectQueryWrapper);

        // 获取二级课程分类
        QueryWrapper<EduSubject> twoSubjectQueryWrapper = new QueryWrapper<>();
        oneSubjectQueryWrapper.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(twoSubjectQueryWrapper);

        List<OneSubject> finalOneSubjectList = new ArrayList<>();

        // 对一级课程和二级课程封装到返回结果对象中
        oneSubjectList.forEach(eduSubject -> {
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(eduSubject,oneSubject);

            String parentId = eduSubject.getId();

            List<TwoSubject> finalTwoSubjectList = twoSubjectList.stream()
                    .filter(twoEduSubject -> twoEduSubject.getParentId().equals(parentId))
                    .map(s -> {
                        TwoSubject twoSubject = new TwoSubject();
                        BeanUtils.copyProperties(s, twoSubject);

                        return twoSubject;
                    })
                    .collect(Collectors.toList());
            oneSubject.setChildren(finalTwoSubjectList);

            finalOneSubjectList.add(oneSubject);
        });

        return finalOneSubjectList;
    }
}
