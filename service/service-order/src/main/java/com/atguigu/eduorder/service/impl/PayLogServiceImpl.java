package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-10-08
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Override
    public Map createNative(String orderNo) {
        try {
//            根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);

//            使用map设置生成二维码参数
            Map m = new HashMap();
            m.put("appid","wx74862e0dfcf69954");
            m.put("mch_id","1558950191"); //商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr()); //生成随机字符串，保证生成的二维码不一样
            m.put("body",order.getCourseTitle()); //课程名称
            m.put("out_trade_no",orderNo); //订单号
            m.put("total_fee",order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");//价格 转化为字符串类型
            m.put("spbill_create_ip", "127.0.0.1"); //支付的IP地址
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify"); //回调
            m.put("trade_type", "NATIVE"); //支付类型


//            发送httpClient请求，传递参数xml格式
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

//            设置xml格式的参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb")); //商户key
            client.setHttps(true); //支持https访问

//            执行请求
            client.post();

//           得到结果，返回结果
            String content = client.getContent();

//            将xml的格式转化为map返回
            Map<String,String> resultMap = WXPayUtil.xmlToMap(content);

//            最终返回数据的封装
            Map map = new HashMap();
            map.put("out_trade_no", orderNo); //订单号
            map.put("course_id", order.getCourseId()); //课程
            map.put("total_fee", order.getTotalFee()); //价格
            map.put("result_code", resultMap.get("result_code")); //订单状态码
            map.put("code_url", resultMap.get("code_url")); //二维码的地址


            return map;

        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.ERROR);
        }
    }


//    查询支付状态
    @Override
    public Map<String, String> getPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String content = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(content);

            return map;
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.ERROR);
        }
    }

//  添加记录到支付表，更新订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
//        从map中获取订单号
        String orderNo = map.get("out_trade_no");

    //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

       if (order.getStatus().intValue() == 1){
           return;
       }

       order.setStatus(1); //修改支付状态为已支付
       orderService.updateById(order);

        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo()); //支付订单号
        payLog.setPayTime(new Date()); //支付时间
        payLog.setPayType(1);//支付类型 微信
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id")); //订单流水号
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表

    }
}
