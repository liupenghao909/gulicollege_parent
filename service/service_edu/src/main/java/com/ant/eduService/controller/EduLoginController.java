package com.ant.eduService.controller;

import com.ant.commonutils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/eduService/user")
@CrossOrigin
public class EduLoginController {
    private final Logger log = LoggerFactory.getLogger(EduLoginController.class);

    // 登陆功能
    @PostMapping("/login")
    public R login(){
        log.info("Rest to login");
        return R.ok().data("token","admin");
    }

    // 获取用户信息功能
    @GetMapping("/info")
    public R getInfo(){
        log.info("Rest to get user information when login");
        return R.ok()
                .data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

    }
}
