package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author author
 * @since 2021-10-08
 */
public interface OrderService extends IService<Order> {

    String createOrder(String courseId, String memberId);

}
