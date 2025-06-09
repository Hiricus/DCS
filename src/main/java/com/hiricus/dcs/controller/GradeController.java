package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.GradeDto;
import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import com.hiricus.dcs.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/grades")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
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
}
