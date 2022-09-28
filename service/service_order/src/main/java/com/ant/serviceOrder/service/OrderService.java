package com.ant.serviceOrder.service;

import com.ant.serviceOrder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author ant
 * @since 2022-09-23
 */
public interface OrderService extends IService<Order> {

    String generateOrder(String courseId, String memberId);

    boolean judgeCourseIsBuy(String courseId, String memberId);
}
