package com.ant.eduService.controller;


import com.ant.commonutils.JWTUtils;
import com.ant.commonutils.R;
import com.ant.commonutils.vo.MemberBaseInfo;
import com.ant.eduService.client.UCenterClient;
import com.ant.eduService.entity.EduComment;
import com.ant.eduService.service.EduCommentService;
import com.ant.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-09-22
 */
@RestController
@RequestMapping("/eduService/eduComment")

public class EduCommentController {

    private final EduCommentService eduCommentService;

    private final UCenterClient uCenterClient;

    public EduCommentController(EduCommentService eduCommentService, UCenterClient uCenterClient) {
        this.eduCommentService = eduCommentService;
        this.uCenterClient = uCenterClient;
    }

    // 根据courseId分页查询评论
    @GetMapping("/get/pageComment/{page}/{limit}")
    public R getPageCommentByCourseId(@PathVariable("page") Long page,
                                      @PathVariable("limit") Long limit,
                                      @RequestParam(value = "courseId",required = false)String courseId){

        Page<EduComment> pageParam = new Page<EduComment>(page, limit);

        Map<String,Object> map = eduCommentService.getPageCommentByCourseId(pageParam,courseId);

        return R.ok().data("map",map);
    }

    // 保存评论
    @PostMapping("/save/comment")
    public R saveComment(@RequestBody EduComment eduComment, HttpServletRequest request){

        String memberId = JWTUtils.getMemberIdByJwtToken(request);

        if(StringUtils.isBlank(memberId)){
            return R.error().code(28004).message("未登录");
        }
        // 通过memberId查询用户信息
        MemberBaseInfo memberBaseInfo = uCenterClient.getMemberBaseInfo(memberId);
        eduComment.setMemberId(memberId);
        eduComment.setNickname(memberBaseInfo.getNickname());
        eduComment.setAvatar(memberBaseInfo.getAvatar());

        boolean save = eduCommentService.save(eduComment);

        return R.ok();

    }

}

