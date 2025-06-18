package com.hiricus.dcs.util.table.grade;

import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GradeTableCreator {
    public Workbook writeGradeTable(List<GradeTableEntry> entries) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Grades");

        // Создаём заголовки
        String[] headers = {"оценка", "user_id", "фамилия", "имя", "отчество", "пасспорт", "название_дисциплины"};

        // Создаём первую строку с заголовками
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Заполяем строки данными
        int rowNum = 1;
        for (GradeTableEntry entry : entries) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(entry.getGrade());
            row.createCell(1).setCellValue(entry.getUserId() != null ? entry.getUserId().toString() : "");
            row.createCell(2).setCellValue(entry.getSurname());
            row.createCell(3).setCellValue(entry.getName());
            row.createCell(4).setCellValue(entry.getPatronymic());
            row.createCell(5).setCellValue(entry.getPassport());
            row.createCell(6).setCellValue(entry.getDisciplineName());
        }

        // Автоподгон ширины колонок
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    public Workbook writeGradeReportOnDisciplineAndGroup(List<FinalGradeObject> entries, String disciplineName, String groupName) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Отчёт об оценках по дисциплине");

        // Создаём заголовки
        String[] headers = {"Фио студента", "Оценка"};

        // Создаём строку с названием дисциплины
        Row nameRow = sheet.createRow(0);
        Cell nameCell = nameRow.createCell(0);
        nameCell.setCellValue("Итоговые оценки: " + disciplineName + " " + groupName);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // Создаём строку с заголовками
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Заполняем строку данными
        int rowNum = 2;
        for (FinalGradeObject entry : entries) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(entry.getUserName());
            row.createCell(1).setCellValue(entry.getGrade());
        }

        // Автоподгон ширины колонок
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    public Workbook writePersonalGradeReport(List<FinalGradeObject> entries, String studentFullName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Персональный отчёт об оценках");

        // Создаём заголовки
        String[] headers = {"Название предмета", "Оценка"};

        // Создаём строку с фио студня
        Row nameRow = sheet.createRow(0);
        Cell nameCell = nameRow.createCell(0);
        nameCell.setCellValue(studentFullName);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // Создаём строку с заголовками
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Заполняем строки данными
        int rowNum = 2;
        for (FinalGradeObject entry : entries) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(entry.getDisciplineName());
            row.createCell(1).setCellValue(entry.getGrade());
        }

        // Автоподгон ширины колонок
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}
