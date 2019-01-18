package com.demo2.common.excel;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ExcelRow {

    /**
     *  行数据
      */
    private List<String> rowData;

    public ExcelRow(List<String> rowData) {
        this.rowData = rowData;
    }

    private int size() {
        if (rowData != null) {
            return rowData.size();
        } else {
            return 0;
        }
    }

    public String getCellValue(int index) {
        if (index >= size()) {
            return null;
        }
        return rowData.get(index);
    }

    public boolean isBlank(){
        String content;
        for (int idx = 0; idx < size(); idx++) {
            content = StringUtils.trim(getCellValue(idx));
            if (StringUtils.isNotBlank(content)) {
                return false;
            }
        }
        return true;
    }
}
