package com.ant.serviceOrder.service;

import com.ant.serviceOrder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author ant
 * @since 2022-09-23
 */
public interface PayLogService extends IService<PayLog> {

    Map<String, Object> generateWechatPaymentQR(String orderNum);

    Map<String, String> getPayStatus(String orderNum);

    void updatePayStatus(Map<String, String> map);
}
