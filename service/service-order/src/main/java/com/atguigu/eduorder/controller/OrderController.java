package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.Result;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-10-08
 */
@Api(description = "订单接口")
@RestController
@RequestMapping("/eduorder/order")
public class OrderController {


    @Autowired
    private OrderService orderService;


    /**
     * 生成订单
     * @param courseId
     * @param request
     * @return
     */
    @ApiOperation("生成订单")
    @GetMapping("/createOrder/{courseId}")
    public Result createOrder(@PathVariable String courseId, HttpServletRequest request){
//        创建订单，返回订单号
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = orderService.createOrder(courseId,memberId);

        return Result.ok(orderNo);
    }


    /**
     * 根据订单号查询订单信息
     * @param orderId
     * @return
     */
    @ApiOperation("根据订单号查询订单信息")
    @GetMapping("/getOrderById/{orderId}")
    public Result getOrderById(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return Result.ok(order);
    }
}

