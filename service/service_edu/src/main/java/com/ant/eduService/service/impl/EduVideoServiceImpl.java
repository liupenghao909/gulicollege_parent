package com.ant.eduService.service.impl;

import com.ant.eduService.client.VodClient;
import com.ant.eduService.entity.EduVideo;
import com.ant.eduService.mapper.EduVideoMapper;
import com.ant.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {


    private final VodClient vodClient;

    public EduVideoServiceImpl(VodClient vodClient) {
        this.vodClient = vodClient;
    }

    /**
     * 删除小节，删除对应视频文件
     * @param courseId
     */
    @Override
    public void deleteVideoByCourseId(String courseId) {
        // 根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",courseId);

        List<EduVideo> eduVideos = baseMapper.selectList(eduVideoQueryWrapper);

        List<String> videoIdList = eduVideos.stream()
                .map(EduVideo::getVideoSourceId)
                .filter(StringUtils::isNoneBlank)
                .collect(Collectors.toList());
        //  删除小节下的视频
        vodClient.deleteVideoBatch(videoIdList);

        baseMapper.delete(eduVideoQueryWrapper);
    }
}
