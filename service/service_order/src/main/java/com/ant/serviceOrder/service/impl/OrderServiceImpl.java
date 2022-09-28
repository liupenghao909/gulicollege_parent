package com.ant.serviceOrder.service.impl;

import com.ant.commonutils.vo.MemberBaseInfo;
import com.ant.commonutils.vo.WebCourseVO;
import com.ant.serviceOrder.client.EduClient;
import com.ant.serviceOrder.client.UCenterClient;
import com.ant.serviceOrder.entity.Order;
import com.ant.serviceOrder.mapper.OrderMapper;
import com.ant.serviceOrder.service.OrderService;
import com.ant.serviceOrder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.management.Query;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-09-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final EduClient eduClient;

    private final UCenterClient uCenterClient;

    public OrderServiceImpl(EduClient eduClient, UCenterClient uCenterClient) {
        this.eduClient = eduClient;

        this.uCenterClient = uCenterClient;
    }

    @Override
    public String generateOrder(String courseId, String memberId) {
        // 根据memberId 远程调用查询用户信息
        MemberBaseInfo memberBaseInfo = uCenterClient.getMemberBaseInfo(memberId);

        // 根据courseId 远程调用查询课程信息
        WebCourseVO webCourseVO = eduClient.orderGetCourseInfo(courseId);

        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseCover(webCourseVO.getCover());
        order.setCourseTitle(webCourseVO.getTitle());
        order.setMemberId(memberId);
        order.setNickname(memberBaseInfo.getNickname());
        order.setMobile(memberBaseInfo.getMobile());
        order.setTeacherName(webCourseVO.getTeacherName());
        order.setTotalFee(webCourseVO.getPrice());
        order.setPayType(1);  // 支付类型，1 微信
        order.setStatus(0);   // 支付状态 0 未支付

        baseMapper.insert(order);

        return order.getOrderNo();

    }

    @Override
    public boolean judgeCourseIsBuy(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",1);

        Integer count = baseMapper.selectCount(queryWrapper);

        return count > 0;
    }
}
