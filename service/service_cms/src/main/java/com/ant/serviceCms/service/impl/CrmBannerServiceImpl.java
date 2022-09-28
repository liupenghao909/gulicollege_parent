package com.ant.serviceCms.service.impl;

import com.ant.serviceCms.entity.CrmBanner;
import com.ant.serviceCms.mapper.CrmBannerMapper;
import com.ant.serviceCms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-09-12
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    @Cacheable(key = "'selectIndexList'",value = "banner")
    public List<CrmBanner> getAllBanner() {
        List<CrmBanner> bannerList = baseMapper.selectList(null);

        return bannerList;
    }
}
