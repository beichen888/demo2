package com.demo2.common.excel;

import java.io.Serializable;
import java.util.List;

public class ExcelSheet implements Serializable {


    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * sheet中的数据
     */
    private List<ExcelRow> excelRows;

    public ExcelSheet(String sheetName, List<ExcelRow> rows) {
        this.sheetName = sheetName;
        this.excelRows = rows;
    }

    public List<ExcelRow> getExcelRows() {
        return excelRows;
    }

    public String getSheetName() {
        return sheetName;
    }

}
