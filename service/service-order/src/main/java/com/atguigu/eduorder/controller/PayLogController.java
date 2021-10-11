package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-10-08
 */
@Api(description = "微信支付二维码接口")
@RestController
@RequestMapping("/eduorder/paylog")
public class PayLogController {


    @Autowired
    private PayLogService payLogService;

    @Autowired
    private OrderService orderService;

    /**
     * 生成二维码接口
     * @param orderNo
     * @return
     */
    @ApiOperation("生成二维码接口")
    @GetMapping("/createNative/{orderNo}")
    public Result createNative(@PathVariable String orderNo){
        Map map = payLogService.createNative(orderNo);
        return Result.ok(map);
    }

    /**
     * 查询订单状态
     * @param orderNo
     * @return
     */
    @ApiOperation("查询订单状态")
    @GetMapping("/getPayStatus/{orderNo}")
    public Result getPayStatus(@PathVariable String orderNo){
//        根据订单号查询订单支付状态
       Map<String,String> map = payLogService.getPayStatus(orderNo);
       if (map == null){
           return Result.setResultCodeEnum(ResultCodeEnum.WXPAY_ERROR); //支付失败
       }
//       判断订单状态
       if (map.get("trade_state").equals("SUCCESS")){
//           添加记录到支付表，更新订单状态
           payLogService.updateOrderStatus(map);
           return Result.setResultCodeEnum(ResultCodeEnum.WXPAY_SUCCESS); //支付成功
       }
       return Result.setResultCodeEnum(ResultCodeEnum.WXPAY_INFO); //支付中
    }


    /**
     * 根据用户id和课程id判断课程是否已经支付
     * @param courseId
     * @param memberId
     * @return
     */
    @ApiOperation("根据用户id和课程id判断课程是否已经支付")
    @GetMapping("/getByCourse/{courseId}/{memberId}")
    public boolean getByCourse(@PathVariable String courseId,@PathVariable String  memberId){

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);
        int count = orderService.count(wrapper);
        if (count == 0){
            return false;
        }
        return true;
    }
}

