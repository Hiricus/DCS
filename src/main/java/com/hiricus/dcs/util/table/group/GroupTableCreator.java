package com.hiricus.dcs.util.table.group;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class GroupTableCreator {
    public Workbook createGroupTable(List<GroupTableEntry> entries) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Group Table");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String[] headers = {
                "id", "фамилия", "имя", "отчество", "дата_рожд",
                "телефон", "паспорт", "снилс", "группа"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            ((Row) headerRow).createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (GroupTableEntry entry : entries) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(entry.getId() != null ? entry.getId() : 0);
            row.createCell(1).setCellValue(entry.getSurname());
            row.createCell(2).setCellValue(entry.getName());
            row.createCell(3).setCellValue(entry.getPatronymic());
            row.createCell(4).setCellValue(
                    entry.getBirthDate() != null ? entry.getBirthDate().format(dateFormatter) : ""
            );
            row.createCell(5).setCellValue(entry.getPhoneNumber());
            row.createCell(6).setCellValue(entry.getPassport());
            row.createCell(7).setCellValue(entry.getSnils());
            row.createCell(8).setCellValue(entry.getGroupName());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}
