package com.ant.serviceOrder.controller;


import com.ant.commonutils.JWTUtils;
import com.ant.commonutils.R;
import com.ant.serviceOrder.entity.Order;
import com.ant.serviceOrder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-09-23
 */
@RestController
@RequestMapping("/serviceOrder/order")

public class OrderController {
    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 根据课程id生成订单存储到数据库中，并返回订单编号
    @GetMapping("/generate/order/{courseId}")
    public R generateOrder(@PathVariable("courseId") String courseId, HttpServletRequest request){
        log.info("Rest to generate order {}",courseId);

        String memberId = JWTUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isBlank(memberId)){
            return R.error().code(28004).message("未登录");
        }

        String orderId = orderService.generateOrder(courseId,memberId);

        return R.ok().data("orderId", orderId);
    }

    // 根据订单id获取订单信息
    @GetMapping("/get/orderInfo/{orderNum}")
    public R getOrderInfo(@PathVariable("orderNum") String orderNum){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNum);
        Order order = orderService.getOne(queryWrapper);

        return R.ok().data("order",order);
    }

    @GetMapping("/judge/order/isPay/{courseId}/{memberId}")
    public boolean judgeCourseIsBuy(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId){

        boolean isBuy = orderService.judgeCourseIsBuy(courseId,memberId);

        return isBuy;

    }
}

