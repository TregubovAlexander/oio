package ru.elsu.oio.utils;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;

public final class ExcelUtil {
    private ExcelUtil() {}

    /**
     * Получение ячейки по имени
     * @param cellName имя ячейки, котоую нужно найти и вернуть.
     * @param workbook Экземпляр класса XSSFWorkbook, инкапсулирующий ссылку
     *        на книгу Excel, в которой будет искаться именованная ячейка.
     * @return Экземпляр класса XSSFCell, инкапсулирующий ссылку на именованную
     *        ячейку, либо null, если она не найден.
     */
    public static Cell getNamedCell(String cellName, Workbook workbook) {
        Name name = workbook.getName(cellName);
        CellReference cellRef = new CellReference(name.getRefersToFormula());
        Sheet sheet = workbook.getSheet(cellRef.getSheetName());
        Row row = sheet.getRow(cellRef.getRow());
        return row.getCell((int)cellRef.getCol());
    }

    /**
     * Получение диапазона по имени
     * @param rangeName имя дианазона, который нужно вернуть
     * @param workbook Экземпляр класса XSSFWorkbook, инкапсулирующий ссылку
     *        на книгу Excel, в которой будет искаться именованная ячейка.
     * @return Найденный диапазон, или null, если диапазон не найден
     */
    public static AreaReference getNamedArea(String rangeName, Workbook workbook) {
        int rangeNameIdx = workbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return null;

        Name aNamedRange = workbook.getNameAt(rangeNameIdx);
        return new AreaReference(aNamedRange.getRefersToFormula(), SpreadsheetVersion.EXCEL2007);
    }

    /**
     * Возвращает стиль одной ячейки, со всеми установленными границами черного цвета
     * @param borderStyle   стиль границы
     * @param wb            экземпляр рабочей книги
     * @return              экземпляр класса CellStyle
     */
    public static CellStyle setCellBorderStyle(BorderStyle borderStyle, Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return cellStyle;
    }

    /**
     * Устанавливаем внешние границы для диапазона ячеек
     * @param borderStyle   стиль рамки
     * @param region        диапазон
     * @param sheet         лист
     */
    public static void setRangeBorder(BorderStyle borderStyle, CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderTop(borderStyle, region, sheet);
        RegionUtil.setBorderBottom(borderStyle, region, sheet);
        RegionUtil.setBorderLeft(borderStyle, region, sheet);
        RegionUtil.setBorderRight(borderStyle, region, sheet);
    }



}
