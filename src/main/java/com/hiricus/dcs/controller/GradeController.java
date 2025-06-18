package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.DocumentDto;
import com.hiricus.dcs.dto.GradeDto;
import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.service.GradeService;
import com.hiricus.dcs.service.XmlTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/grades")
public class GradeController {
    private final XmlTableService tableService;
    private final GradeService gradeService;

    public GradeController(XmlTableService tableService,
                           GradeService gradeService) {
        this.tableService = tableService;
        this.gradeService = gradeService;
    }

    // ----------------------- Работа с таблицами -----------------------
    @PostMapping
    public ResponseEntity<Long> createGrade(@RequestBody Map<String, Object> request) {
        String grade = (String) request.get("grade");
        Integer userId = (Integer) request.get("user_id");
        Integer disciplineId = (Integer) request.get("discipline_id");

        FinalGradeObject gradeObject = new FinalGradeObject(grade, LocalDate.now(), userId, disciplineId);
        Long id = gradeService.createGrade(gradeObject);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable("id") Long gradeId, @RequestParam("new_grade") String updatedGrade) {
        gradeService.updateGradeValue(gradeId, updatedGrade);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable("id") Long gradeId) {
        gradeService.deleteGrade(gradeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ----------------------- Работа с большими объёмами данных -----------------------
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<GradeDto>> getUsersGrades(@PathVariable("userId") Integer userId) {
        List<FinalGradeObject> grades = gradeService.getUsersGrades(userId);

        List<GradeDto> response = new ArrayList<>();
        grades.stream().forEach(grade -> {
            GradeDto gradeDto = new GradeDto(grade.getId(), grade.getGrade(), grade.getDisciplineId(), grade.getDisciplineName());
            response.add(gradeDto);
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/byGroup/byDiscipline")
    public ResponseEntity<List<GradeDto>> getGradesByGroupAndDiscipline(@RequestBody Map<String, Object> request) {
        Integer groupId = (Integer) request.get("group_id");
        Integer disciplineId = (Integer) request.get("discipline_id");

        List<FinalGradeObject> grades = gradeService.getGradesByGroupAndDiscipline(groupId, disciplineId);

        List<GradeDto> response = new ArrayList<>();
        grades.stream().forEach(grade -> {
            GradeDto gradeDto = new GradeDto(grade.getId(), grade.getUserId(), grade.getUserName(), grade.getGrade());
            response.add(gradeDto);
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ----------------------- Работа с таблицами -----------------------
    @PostMapping("/getFillingTemplate")
    public ResponseEntity<DocumentDto> getFillingTemplate(@RequestBody Map<String, Object> request) {
        Integer groupId = (int) request.get("group_id");
        Integer disciplineId = (int) request.get("discipline_id");

        DocumentObject table = tableService.getFillingTemplateForGrades(groupId, disciplineId);
        return new ResponseEntity<>(new DocumentDto(table), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadGradesOfDiscipline(@RequestParam("file") MultipartFile file) {
        DocumentObject table;
        try {
            table = new DocumentObject(file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        tableService.handleGradeTable(table);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/personalGradeReport/{id}")
    public ResponseEntity<DocumentDto> getPersonalGradeReport(@PathVariable("id") Integer userId) {
        DocumentObject table = tableService.getPersonalGradeReport(userId);

        return new ResponseEntity<>(new DocumentDto(table), HttpStatus.OK);
    }

    @PostMapping("/gradeReportOnGroupAndDiscipline/get")
    public ResponseEntity<DocumentDto> getGradeReportOnGroupAndDiscipline(@RequestBody Map<String, Object> request) {
        Integer groupId = (int) request.get("group_id");
        Integer disciplineId = (int) request.get("discipline_id");

        DocumentObject table = tableService.getGradeReportOnDiscipline(disciplineId, groupId);
        return new ResponseEntity<>(new DocumentDto(table), HttpStatus.OK);
    }
}
