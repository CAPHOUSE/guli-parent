package com.atguigu.eduorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-order")
public interface OrderClient {

    @GetMapping("/eduorder/paylog/getByCourse/{courseId}/{memberId}")
    public boolean getByCourse(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}
