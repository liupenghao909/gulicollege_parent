package com.ant.serviceMsm.controller;


import com.ant.commonutils.R;
import com.ant.commonutils.RandomUtil;
import com.ant.serviceMsm.service.MsmService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/serviceMsm/api")
 // 跨域
public class MsmController {
    private final Logger log = LoggerFactory.getLogger(MsmController.class);

    private final MsmService msmService;

    private final StringRedisTemplate redisTemplate;

    public MsmController(MsmService msmService, StringRedisTemplate redisTemplate) {
        this.msmService = msmService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送短信验证码，使用redis设置短信的有效时间
     * @param phoneNumber
     * @return
     */
    @GetMapping("/send/message/{phoneNumber}")
    public R sendMessage(@PathVariable("phoneNumber") String phoneNumber){
        log.info("Rest to send message by alibaba");

        // 先查询redis里面手机号有没有验证码
        String sendedCode = redisTemplate.opsForValue().get(phoneNumber);

        if(!StringUtils.isBlank(sendedCode)){
            return R.ok();
        }

        // 随机生成一个四位的验证码
        String code = RandomUtil.getFourBitRandom();
        boolean isSuccess = msmService.sendMessage(code,phoneNumber);

        if(isSuccess){
            // 将验证码存储到redis中，设置验证码的有效时间为五分钟
            redisTemplate.opsForValue().set(phoneNumber,code,5, TimeUnit.MINUTES);
            return R.ok();
        }

        return R.error().message("发送验证码信息失败");

    }
}
