package com.ant.serviceCms.controller;


import com.ant.commonutils.R;
import com.ant.serviceCms.entity.CrmBanner;
import com.ant.serviceCms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 谷粒商城的管理后台使用的controller
 * </p>
 *
 * @author ant
 * @since 2022-09-12
 */
@RestController
@RequestMapping("/serviceCms/bannerAdmin")
 // 跨域
public class BannerAdminController {

    private final Logger log = LoggerFactory.getLogger(BannerAdminController.class);

    private final CrmBannerService crmBannerService;

    public BannerAdminController(CrmBannerService crmBannerService) {
        this.crmBannerService = crmBannerService;
    }

    /**
     * 分页查询banner
      */
    @GetMapping("/get/banner/page/{page}/{limit}")
    public R pageBanner(@PathVariable("page")Long page
                        ,@PathVariable("limit")Long limit){

        Page<CrmBanner> bannerPage = new Page<>(page,limit);

        IPage<CrmBanner> bannerIPage = crmBannerService.page(bannerPage, null);

        return R.ok().data("items",bannerIPage.getRecords()).data("total",bannerIPage.getTotal());
    }


    /**
     * 根据id查询banner
     */
    @GetMapping("/get/banner/{bannerId}")
    public R getBannerById(@PathVariable("bannerId") String bannerId){
        CrmBanner crmBanner = crmBannerService.getById(bannerId);

        return R.ok().data("banner",crmBanner);
    }

    /**
     * 添加banner到数据库中
     */
    @PostMapping("/add/banner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);

        return R.ok();
    }

    /**
     * 删除banner
     */
    @GetMapping("/delete/banner/{bannerId}")
    public R deleteBannerById(@PathVariable("bannerId") String bannerId){
        crmBannerService.removeById(bannerId);

        return R.ok();

    }

    /**
     * 更新banner的值
     */
    @PostMapping("/update/banner")
    public R updateBannerById(@RequestBody CrmBanner crmBanner){
        crmBannerService.updateById(crmBanner);

        return R.ok();
    }

}

