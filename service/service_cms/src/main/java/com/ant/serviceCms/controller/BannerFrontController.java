package com.ant.serviceCms.controller;

import com.ant.commonutils.R;
import com.ant.serviceCms.entity.CrmBanner;
import com.ant.serviceCms.service.CrmBannerService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 谷粒商城的用户前台使用的controller
 */
@RestController
@RequestMapping("/serviceCms/bannerFront")
 // 跨域
public class BannerFrontController {

    private final CrmBannerService bannerService;

    public BannerFrontController(CrmBannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * 查询所有banner
     */
    @GetMapping("/get/allBanner")
    public R getAllBanner(){
        List<CrmBanner> bannerList = bannerService.getAllBanner();

        return R.ok().data("bannerList",bannerList);
    }
}
