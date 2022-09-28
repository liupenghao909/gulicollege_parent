package com.ant.serviceOrder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ant.serviceOrder.config.properties.WechatPaymentQRProperties;
import com.ant.serviceOrder.entity.Order;
import com.ant.serviceOrder.entity.PayLog;
import com.ant.serviceOrder.mapper.PayLogMapper;
import com.ant.serviceOrder.service.OrderService;
import com.ant.serviceOrder.service.PayLogService;
import com.ant.serviceOrder.utils.HttpClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-09-23
 */
@Service
@EnableConfigurationProperties(WechatPaymentQRProperties.class)
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    private final OrderService orderService;

    private final WechatPaymentQRProperties wechatPaymentQRProperties;

    public PayLogServiceImpl(OrderService orderService, WechatPaymentQRProperties wechatPaymentQRProperties) {
        this.orderService = orderService;
        this.wechatPaymentQRProperties = wechatPaymentQRProperties;
    }

    @Override
    public Map<String, Object> generateWechatPaymentQR(String orderNum) {
        try {
            //根据订单id获取订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNum);
            Order order = orderService.getOne(wrapper);
            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", wechatPaymentQRProperties.getAppid());
            m.put("mch_id", wechatPaymentQRProperties.getPartner());
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderNum);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", wechatPaymentQRProperties.getNotifyurl());
            m.put("trade_type", "NATIVE");

            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, wechatPaymentQRProperties.getPartnerkey()));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //4、封装返回结果集

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNum);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));
            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> getPayStatus(String orderNum) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", wechatPaymentQRProperties.getAppid());
            m.put("mch_id", wechatPaymentQRProperties.getPartner());
            m.put("out_trade_no", orderNum);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, wechatPaymentQRProperties.getPartnerkey()));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updatePayStatus(Map<String, String> map) {
        // 从订单表中根据订单编号查询订单信息
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",map.get("out_trade_no"));

        Order order = orderService.getOne(queryWrapper);

        // 如果订单已完成，直接返回
        if(order.getStatus().intValue() == 1){
            return;
        }

        // 将状态改为1
        order.setStatus(1);
        orderService.updateById(order);

        // 向支付记录表中插入数据
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id")); // 流水号
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }
}
