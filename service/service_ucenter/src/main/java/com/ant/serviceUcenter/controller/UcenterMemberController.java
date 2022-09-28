package com.ant.serviceUcenter.controller;


import com.ant.commonutils.JWTUtils;
import com.ant.commonutils.R;
import com.ant.commonutils.vo.MemberBaseInfo;
import com.ant.serviceUcenter.entity.UcenterMember;
import com.ant.serviceUcenter.entity.vo.RegisterVO;
import com.ant.serviceUcenter.service.UcenterMemberService;
import com.ant.servicebase.exceptionhandler.GuliException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-09-18
 */
@RestController
@RequestMapping("/serviceUcenter/ucenterMember")

public class UcenterMemberController {
    private final Logger log = LoggerFactory.getLogger(UcenterMemberController.class);

    private final UcenterMemberService ucenterMemberService;

    public UcenterMemberController(UcenterMemberService ucenterMemberService) {
        this.ucenterMemberService = ucenterMemberService;
    }

    //  登陆,返回一个token字符串
    @PostMapping("/login")
    public R login(@RequestBody UcenterMember member){
        log.info("Rest to login in");
        String token = ucenterMemberService.login(member);

        return R.ok().data("token",token);

    }

    // 注册
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO){
        log.info("Rest to member {} register",registerVO.getMobile());

        ucenterMemberService.register(registerVO);

        return R.ok();
    }


    // 根据token获取用户信息
    @GetMapping("/get/memberInfo")
    public R getMemberInfo(HttpServletRequest request){
        String memberId = JWTUtils.getMemberIdByJwtToken(request);
        UcenterMember memberInfo = ucenterMemberService.getById(memberId);

        return R.ok().data("memberInfo",memberInfo);
    }

    @GetMapping("/get/member/baseInfo/{memberId}")
    public MemberBaseInfo getMemberBaseInfo(@PathVariable("memberId") String memberId){
        UcenterMember member = ucenterMemberService.getById(memberId);
        MemberBaseInfo memberBaseInfo = new MemberBaseInfo();
        BeanUtils.copyProperties(member,memberBaseInfo);

        return memberBaseInfo;
    }

    @GetMapping("/get/member/registerNum/{day}")
    public R getMemberRegisterNum(@PathVariable("day") String day){
        Integer num = ucenterMemberService.getMemberRegisterNum(day);

        return R.ok().data("num",num);
    }

}

