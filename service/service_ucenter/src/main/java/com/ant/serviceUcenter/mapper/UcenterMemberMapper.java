package com.ant.serviceUcenter.mapper;

import com.ant.serviceUcenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author ant
 * @since 2022-09-18
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer getMemberRegisterNum(@Param("day") String day);
}
