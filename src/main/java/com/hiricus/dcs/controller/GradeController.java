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
}
