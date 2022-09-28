package com.ant.eduService.controller.front;

import com.ant.commonutils.JWTUtils;
import com.ant.commonutils.R;
import com.ant.eduService.client.OrderClient;
import com.ant.eduService.entity.EduCourse;
import com.ant.eduService.entity.chapter.ChapterVO;
import com.ant.eduService.entity.vo.frontVO.FrontCourseSearchConditionVO;
import com.ant.eduService.entity.vo.frontVO.WebCourseVO;
import com.ant.eduService.service.EduChapterService;
import com.ant.eduService.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduService/frontCourse")

public class FrontCourseController {

    private final EduCourseService eduCourseService;

    private final EduChapterService eduChapterService;

    private final OrderClient orderClient;

    public FrontCourseController(EduCourseService eduCourseService, EduChapterService eduChapterService, OrderClient orderClient) {
        this.eduCourseService = eduCourseService;
        this.eduChapterService = eduChapterService;
        this.orderClient = orderClient;
    }

    /**
     * 课程带条件的分页查询
     * @param page
     * @param limit
     * @param courseSearchConditionVO
     * @return
     */
    @PostMapping("/get/condition/pageCourse/{page}/{limit}")
    public R getCourseList(@PathVariable("page") Long page, @PathVariable("limit") Long limit,
                           @RequestBody(required = false) FrontCourseSearchConditionVO courseSearchConditionVO){

        Page<EduCourse> pageParam = new Page<>(page,limit);

        Map<String,Object> map = eduCourseService.getCourseListWithCondition(pageParam,courseSearchConditionVO);

        return R.ok().data("map",map);
    }

    @GetMapping("/get/webCourse/info/{courseId}")
    public R getWebCourseInfoById(@PathVariable("courseId") String courseId, HttpServletRequest request){

        WebCourseVO webCourseVO = eduCourseService.getWebCourseInfo(courseId);

        List<ChapterVO> chapterVOList = eduChapterService.getSubjectVideoList(courseId);

        // 获取课程购买状态
        // 默认没买
        boolean isBuy = false;
        String memberId = JWTUtils.getMemberIdByJwtToken(request);
        if(!StringUtils.isEmpty(memberId)){
            isBuy = orderClient.judgeCourseIsBuy(courseId,memberId);
        }

        return R.ok().data("webCourseVO",webCourseVO).data("chapterVOList",chapterVOList).data("isBuy",isBuy);
    }


    // 分布式远程调用，根据课程id获取课程信息
    @GetMapping("/order/get/courseInfo/{courseId}")
    public WebCourseVO orderGetCourseInfo(@PathVariable("courseId") String courseId){
        WebCourseVO courseInfo = eduCourseService.getWebCourseInfo(courseId);

        return courseInfo;
    }
}
