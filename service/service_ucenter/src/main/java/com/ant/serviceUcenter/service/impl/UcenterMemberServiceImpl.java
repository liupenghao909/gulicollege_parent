package com.ant.serviceUcenter.service.impl;

import com.ant.commonutils.JWTUtils;
import com.ant.commonutils.MD5;
import com.ant.serviceUcenter.config.properties.WeiXinQRCodeProperties;
import com.ant.serviceUcenter.entity.UcenterMember;
import com.ant.serviceUcenter.entity.vo.RegisterVO;
import com.ant.serviceUcenter.mapper.UcenterMemberMapper;
import com.ant.serviceUcenter.service.UcenterMemberService;
import com.ant.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-09-18
 */
@Service
@EnableConfigurationProperties(WeiXinQRCodeProperties.class)
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    private final StringRedisTemplate redisTemplate;

    public UcenterMemberServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 登陆使用手机号+密码，返回token
    @Override
    public String login(UcenterMember member) {
        // 先判读手机号或密码是否为null
        String mobile = member.getMobile();
        String password = member.getPassword();

        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(password)){
            throw new GuliException(20001,"手机号或密码为空");
        }

        // 从数据库中根据手机号查询用户信息
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(queryWrapper);

        if(mobileMember == null){
            throw new GuliException(20001,"用户不存在");
        }

        // 数据库中不会存储密码明文，都会经过加密存储到数据库中
        // 本项目采用的是MD5加密
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())){
            throw new GuliException(20001,"用户名或密码错误");
        }

        // 检查账户是否禁用
        if(mobileMember.getIsDisabled()){
            throw new GuliException(20001,"账户暂时不可用");
        }

        String jwtToken = JWTUtils.getJWTToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    // 注册
    @Override
    public void register(RegisterVO registerVO) {
        String code = registerVO.getCode();
        String mobile = registerVO.getMobile();
        String nickname = registerVO.getNickname();
        String password = registerVO.getPassword();

        // 验证是否有空数值
        if(StringUtils.isBlank(code) || StringUtils.isBlank(mobile) ||
                StringUtils.isBlank(nickname) || StringUtils.isBlank(password)){
            throw new GuliException(20001,"注册信息缺失");
        }

        // 对比验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"验证码错误");
        }

        // 查询数据库中是否已经有手机号
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new GuliException(20001,"当时手机号已注册");
        }

        // 将用户信息存到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        // 设置账户可用
        member.setIsDisabled(false);
        // 设置默认的头像
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epMicP9UT6mVjYWdno0OJZkOXiajG0sllJTbGJ9DYiceej2XvbDSGCK8LCF7jv1PuG2uoYlePWic9XO8A/132");

        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getMemberByOpenId(String openId) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openId);
        UcenterMember member = baseMapper.selectOne(queryWrapper);

        return member;
    }

    @Override
    public Integer getMemberRegisterNum(String day) {
        Integer num = baseMapper.getMemberRegisterNum(day);

        return num;
    }
}
