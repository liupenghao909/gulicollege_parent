package com.ant.serviceUcenter.service;

import com.ant.serviceUcenter.entity.UcenterMember;
import com.ant.serviceUcenter.entity.vo.RegisterVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author ant
 * @since 2022-09-18
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVO registerVO);

    UcenterMember getMemberByOpenId(String openId);

    Integer getMemberRegisterNum(String day);
}
