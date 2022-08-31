package com.ant.demo.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class EasyExcelListener extends AnalysisEventListener<DataDemo> {

    @Override
    public void invoke(DataDemo dataDemo, AnalysisContext analysisContext) {
        System.out.println("****"+dataDemo);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
