package com.ant.eduService.controller;

import com.ant.commonutils.R;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/eduService/user")
@CrossOrigin
public class EduLoginController {
    // 登陆功能
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    // 获取用户信息功能
    @GetMapping("/getInfo")
    public R getInfo(){
        Map<String,Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://www.bing.com/images/search?view=detailV2&ccid=dF0qHThL&id=CC646D8DCE4CE01624852A1AA4F4727C125DAD9A&thid=OIP.dF0qHThLBaFpScpK5zr88gHaHY&mediaurl=https%3a%2f%2fpic54.photophoto.cn%2f20200327%2f0005018631050388_b.jpg&exph=987&expw=991&q=%e8%9a%82%e8%9a%81%e5%8d%a1%e9%80%9a%e7%85%a7%e7%89%87&simid=607997198646461635&FORM=IRPRST&ck=BEA36C7790D690876018B57B1D38DFEC&selectedIndex=12&qpvt=%e8%9a%82%e8%9a%81%e5%8d%a1%e9%80%9a%e7%85%a7%e7%89%87");
        return R.ok().data(map);
    }
}
