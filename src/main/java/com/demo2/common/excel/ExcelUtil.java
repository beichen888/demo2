package com.demo2.common.excel;

import com.demo2.common.MessageCode;
import com.demo2.common.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读取excel文件
 *
 * @author
 */
public class ExcelUtil {
    /**
     * excel2003 后缀名
     */
    private static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    /**
     * excel2007,2010后缀
     */
    private static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    /**
     * 读取excel文件
     *
     * @param path 文件path
     * @return ExcelSheet集合
     */
    public static List<ExcelSheet> readExcel(String path) throws AppException {
        List<ExcelSheet> excelSheets = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(path);
            Workbook workbook;
            String postfix = getPostfix(path);
            if (StringUtils.isEmpty(postfix)) {
                return null;
            }
            // EXCEL2003
            if (OFFICE_EXCEL_2003_POSTFIX.equalsIgnoreCase(postfix)) {
                workbook = new HSSFWorkbook(is);
                // EXCEL2007,2010
            } else if (OFFICE_EXCEL_2010_POSTFIX.equalsIgnoreCase(postfix)) {
                workbook = new XSSFWorkbook(is);
            } else {
                throw new AppException(MessageCode.FILE_POSTFIX_ERROR, path);
            }
            // 读取sheet
            for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {
                Sheet sheet = workbook.getSheetAt(sheetCount);

                if (null == sheet) {
                    continue;
                }
                List<ExcelRow> excelLineList = getRowsData(sheet);

                ExcelSheet excelSheet = new ExcelSheet(sheet.getSheetName(), excelLineList);
                excelSheets.add(excelSheet);
            }
            is.close();
        } catch (IOException exception) {
            throw new AppException(MessageCode.FILE_READ_ERROR);
        }
        return excelSheets;

    }

    /**
     * 取得单个sheet的所有行数据
     *
     * @param sheet sheet
     * @return 所有行数据
     */
    private static List<ExcelRow> getRowsData(Sheet sheet) {
        // 读取行
        List<ExcelRow> excelLineList = new ArrayList<>();
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row xssfRow = sheet.getRow(rowNum);
            if (null == xssfRow) {
                continue;
            }

            List<String> rowData = getRowData(xssfRow);
            ExcelRow excelRow = new ExcelRow(rowData);
            excelLineList.add(excelRow);
        }
        return excelLineList;
    }

    /**
     * 取得单个行中所有cell数据
     *
     * @param row excel row
     * @return 所有cell数据
     */
    private static List<String> getRowData(Row row) {
        List<String> rowData = new ArrayList<>();
        // 读取cell
        int cellCnt = row.getLastCellNum();
        for (int cellIndex = 0; cellIndex < cellCnt; cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            rowData.add(getCellValue(cell));
        }
        return rowData;
    }

    /**
     * 取得cell单元格数据
     *
     * @param cell 单元格
     * @return 单元格数据
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        String ret;
        switch (cell.getCellTypeEnum()) {
            case BLANK:
                ret = "";
                break;
            case BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = getCellValue(evaluator.evaluateInCell(cell));
                break;
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                        .getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {
                        // 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    ret = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil.getJavaDate(value);
                    ret = sdf.format(date);
                } else {
                    HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                    ret = dataFormatter.formatCellValue(cell);
                }
                break;
            case STRING:
                ret = cell.getRichStringCellValue().getString();
                break;
            case ERROR:
                ret = null;
                break;
            case _NONE:
                ret = null;
                break;
            default:
                ret = null;
        }
        return ret;
    }

    /**
     * 获取文件扩展名
     *
     * @param path 文件path
     * @return 文件扩展名
     */
    private static String getPostfix(String path) {
        File file = new File(path);
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    /**
     * 取得单元格内容
     *
     * @param row        行
     * @param cellNumber 单元格
     * @return 单元格内容
     */
    public static String getCellValue(Row row, int cellNumber) {
        Cell cell = row.getCell(cellNumber);
        return getCellValue(cell).trim();
    }

    /**
     * 判断空行
     *
     * @param row Row
     * @return 是否为空
     */
    public static boolean isEmptyRow(Row row) {
        int startCellNum = row.getFirstCellNum();
        int endCellNum = row.getLastCellNum();
        if (startCellNum < 0 || endCellNum < 0) {
            return true;
        }
        for (int c = startCellNum; c <= endCellNum; c++) {
            String cellValue = getCellValue(row.getCell(c));
            if (cellValue != null && !cellValue.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}



