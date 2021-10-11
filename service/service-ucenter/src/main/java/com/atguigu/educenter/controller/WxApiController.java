package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantPropertiesUtil;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Api(description = "微信接口")
@Controller
@RequestMapping("/educenter/wx")
public class WxApiController {


    @Autowired
    private UcenterMemberService memberService;

    /**
     * 创建二维码
     * @return
     */
    @ApiOperation("创建二维码")
    @GetMapping("/create")
    public String getWxCode(){
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

//        对地址进行编码
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //生成qrcodeUrl
        String url = String.format(baseUrl, ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl, "atguigu");

        return "redirect:" + url;
    }


    /**
     * 获取扫描人信息，添加数据
     * @return
     */
    @ApiOperation("获取扫描人信息，添加数据")
    @GetMapping("/callback")
    public String callback(String code,String state){
        try {
//      获取code
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //向认证服务器发送请求换取access_token
            String accessTokenUrl = String.format(baseAccessTokenUrl, ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET, code);

            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

//            分割json数据
            Gson gson = new Gson();
            Map mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");



//            把扫码人信息存入数据库
            UcenterMember member = memberService.getMemberId(openid);
            if (member == null){
//            拿着access_token和openid，再去请求微信提供的固定地址
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
//            拼接参数
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

//            发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);

                Map userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickName = (String) userInfoMap.get("nickname");
                String headimgurl = (String)userInfoMap.get("headimgurl");
                member = new UcenterMember();
                member.setAvatar(headimgurl);
                member.setNickname(nickName);
                member.setOpenid(openid);
                memberService.save(member);
            }

            String token = JwtUtils.createJwtToken(member.getId(), member.getNickname());
            return "redirect:http://localhost:3000?token=" + token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.ERROR);
        }

    }

}
