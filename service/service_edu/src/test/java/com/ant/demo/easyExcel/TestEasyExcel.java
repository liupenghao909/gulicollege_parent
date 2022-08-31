package com.ant.demo.easyExcel;


import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {
        // 文件路径加名称
        String filename = "/Users/Penghao/tmpFolder/01.xlsx";

        // 写操作
//        EasyExcel.write(filename,DataDemo.class).sheet("学生列表").doWrite(getData());

        // 读操作
        EasyExcel.read(filename, DataDemo.class, new EasyExcelListener()).sheet().doRead();
    }

    private static List<DataDemo> getData(){
        ArrayList<DataDemo> dataList = new ArrayList<>();

        for(int i = 0;i < 10;i++){
            dataList.add(new DataDemo(i,"tree"+i));
        }

        return dataList;
    }
}
