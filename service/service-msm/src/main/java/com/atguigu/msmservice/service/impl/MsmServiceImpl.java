package com.atguigu.msmservice.service.impl;

import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.HttpUtils;
import com.atguigu.msmservice.utils.RandomUtil;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


//    发送短信
    @Override
    public boolean send(String phone) {
//        基础参数
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "";

//        生成验证码
        String code = RandomUtil.getFourBitRandom();
        String expireAt = "5";

//        请求参数
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:"+ code + ",expire_at:" + expireAt);
        bodys.put("phone_number",phone);
        bodys.put("template_id", "TPL_0001");
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            if (response.getStatusLine().getStatusCode() != 200){
                return false;
            }
//            将验证码存入redis，过期时间为五分钟
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }
    }
}
