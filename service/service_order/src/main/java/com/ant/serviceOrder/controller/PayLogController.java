package com.ant.serviceOrder.controller;


import com.ant.commonutils.R;
import com.ant.serviceOrder.service.PayLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-09-23
 */
@RestController
@RequestMapping("/serviceOrder/payLog")

public class PayLogController {
    private Logger log = LoggerFactory.getLogger(PayLogController.class);

    private final PayLogService payLogService;

    public PayLogController(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    // 根据订单号生成收款二维码
    @GetMapping("/generate/paymentQR/{orderNum}")
    public R generateWechatPaymentQR(@PathVariable("orderNum") String orderNum){
        log.info("Rest to generate QR code for wechat payment");

        Map<String,Object> map = payLogService.generateWechatPaymentQR(orderNum);

        System.out.println("*****生成二维码map:"+map.toString());

        return R.ok().data("map",map);
    }

    @GetMapping("/get/pay/status/{orderNum}")
    public R getPayStatus(@PathVariable("orderNum") String orderNum){
        log.info("Rest to get pay status");
        Map<String,String> map = payLogService.getPayStatus(orderNum);

        System.out.println("****查询订单状态map:"+map.toString());

        if(CollectionUtils.isEmpty(map)){
            return R.error().message("订单支付出错");
        }

        if(map.get("trade_state").equals("SUCCESS")){
            // 更改订单状态
            payLogService.updatePayStatus(map);

            return R.ok().message("订单支付成功");
        }

        return R.ok().code(25000).message("订单支付中");


    }

}

