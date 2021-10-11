package com.atguigu.demo;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasy {
    public static void main(String[] args) {

//        String filename = "C:\\Users\\Administrator\\Desktop\\abc.xlsx";
////        写操作
//        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

        String filename = "C:\\Users\\Administrator\\Desktop\\abc.xlsx";

        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();

    }

    public static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("admin" + i);
            list.add(demoData);
        }
        return list;
    }
}
