package com.hiricus.dcs.util.table;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class TableParser {
    public List<List<String>> parseTable(Workbook workbook, int rowAmount) throws IOException  {
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> dataList = new ArrayList<>();

        Iterator<Row> rowIterator = sheet.iterator();

        // Пропуск заголовков
        if (rowIterator.hasNext()) rowIterator.next();

        // Парсим содержимое
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            List<String> rowData = new ArrayList<>();

            for (int i = 0; i < rowAmount; i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                rowData.add(getCellValueAsString(cell));
            }
            dataList.add(rowData);
        }

        workbook.close();

        return dataList;
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case CellType.STRING: return cell.getStringCellValue();
            case CellType.NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date javaDate = cell.getDateCellValue();

                    // Преобразуем в строку нужного формата
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    return sdf.format(javaDate);
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case CellType.BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case CellType.FORMULA: return cell.getCellFormula();
            case CellType.BLANK: return "";
            default: return "";
        }
    }
}