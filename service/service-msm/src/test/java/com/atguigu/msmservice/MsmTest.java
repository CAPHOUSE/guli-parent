package com.atguigu.msmservice;

import com.atguigu.msmservice.utils.RandomUtil;
import org.junit.Test;

public class MsmTest {

    @Test
    public void test(){
        String fourBitRandom = RandomUtil.getFourBitRandom();
        System.out.println(fourBitRandom);
    }
}
