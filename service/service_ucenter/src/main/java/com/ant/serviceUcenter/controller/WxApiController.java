package com.ant.serviceUcenter.controller;

import com.ant.commonutils.JWTUtils;
import com.ant.serviceUcenter.config.properties.WeiXinQRCodeProperties;
import com.ant.serviceUcenter.entity.UcenterMember;
import com.ant.serviceUcenter.service.UcenterMemberService;
import com.ant.serviceUcenter.utils.HttpClientUtils;
import com.ant.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @Controller和@RestController的不同
 *
 *  @Controller：标识一个Spring类是Spring MVC controller处理器
 *  @RestController：@RestController是@Controller和@ResponseBody的结合体，两个标注合并起来的作用。
 *  @Controller类中的方法可以直接通过返回String跳转到jsp、ftl、html等模版页面。在方法上加@ResponseBody注解，也可以返回实体对象。
 *  @RestController类中的所有方法只能返回String、Object、Json等实体对象，不能跳转到模版页面
 *
 * @RestController中的方法如果想跳转页面，则用ModelAndView进行封装
 */
//@RestController
@Controller   // 只是请求地址，不需要返回数据
@RequestMapping("/serviceUcenter/WxXin/api")
 // 跨域
@EnableConfigurationProperties(WeiXinQRCodeProperties.class)
public class WxApiController {

    private final WeiXinQRCodeProperties weiXinQRCodeProperties;

    private final UcenterMemberService ucenterMemberService;

    public WxApiController(WeiXinQRCodeProperties weiXinQRCodeProperties, UcenterMemberService ucenterMemberService) {
        this.weiXinQRCodeProperties = weiXinQRCodeProperties;
        this.ucenterMemberService = ucenterMemberService;
    }

    /**
     * 获取微信扫描二维码
     * 通过重定向到拼接的获取二维码的地址获取二维码
     * @return
     */
    @GetMapping("/get/QRCode")
    public String getQRCode(){

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // redirect_url 编码
        String redirectUrl = null;
        try {
            redirectUrl = URLEncoder.encode(weiXinQRCodeProperties.getRedirectUrl(), "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String url = String.format(
                baseUrl,
                weiXinQRCodeProperties.getAppId(),
                redirectUrl,
                "atguigu");

        // 重定向
        return "redirect:"+url;

    }

    /**
     * 获取扫码人的信息
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(String code,String state){
        // 第一步 获取code


        /**
         * 第二步 根据code作为参数请求固定的地址获取access_toke + openid
         * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
         */
        String accessTokenBaseUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String accessTokenUrl = String.format(
                accessTokenBaseUrl,
                weiXinQRCodeProperties.getAppId(),
                weiXinQRCodeProperties.getAppSecret(),
                code);

        Gson gson = new Gson();

        try {
            String accessTokenJsonStr = HttpClientUtils.get(accessTokenUrl);
            HashMap accessTokenMap = gson.fromJson(accessTokenJsonStr, HashMap.class);
            String accessToken = (String) accessTokenMap.get("access_token");
            String openId = (String) accessTokenMap.get("openid");

            // 根据openId去数据库中的用户表中查询是否有此微信用户，如果没有自动完成注册
            UcenterMember ucenterMember = ucenterMemberService.getMemberByOpenId(openId);
            if(ucenterMember == null){

                /**
                 *  第三步 通过access_token + openid 作为参数 访问固定Url 获取微信用户信息
                 *  https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
                 *
                 *  */
                String userInfoBaseUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(
                        userInfoBaseUrl,
                        accessToken,
                        openId);

                String userInfoJsonStr = HttpClientUtils.get(userInfoUrl);
                HashMap userInfoMap = gson.fromJson(userInfoJsonStr, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");
                String headImgUrl = (String) userInfoMap.get("headimgurl");

                ucenterMember = new UcenterMember();
                ucenterMember.setAvatar(headImgUrl);
                ucenterMember.setNickname(nickname);
                ucenterMember.setOpenid(openId);
                boolean save = ucenterMemberService.save(ucenterMember);
            }

            // 因为cookie跨域不能使用，所以用token记录用户信息，并将token放到url中传递给前端页面，让其显示用户信息
            String token = JWTUtils.getJWTToken(ucenterMember.getId(), ucenterMember.getNickname());

            // 重定向到首页,加上带有用户信息的token
            return "redirect:http://localhost:3000?token="+token;

        }catch (Exception e){
            throw new GuliException(20001,"扫码登陆异常");
        }

    }
}
