package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.DisciplineDto;
import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.service.DisciplineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/disciplines")
public class DisciplineController {
    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @GetMapping
    public ResponseEntity<List<DisciplineDto>> getAllDisciplines() {
        List<DisciplineDto> response = disciplineService.getAllDisciplines()
                .stream()
                .map(DisciplineDto::new)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/groups/{group_id}")
    public ResponseEntity<List<DisciplineDto>> getDisciplinesOfGroup(@PathVariable("group_id") Integer id) {
        List<DisciplineObject> disciplines = disciplineService.getDisciplinesOfGroup(id);
        List<DisciplineDto> response = disciplines.stream().map(DisciplineDto::new).toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<List<DisciplineDto>> getDisciplinesOfUser(@PathVariable("user_id") Integer id) {
        List<DisciplineObject> disciplines = disciplineService.getDisciplinesOfUser(id);
        List<DisciplineDto> response = disciplines.stream().map(DisciplineDto::new).toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> createDiscipline(@RequestBody Map<String, Object> request) {
        String disciplineName = (String) request.get("name");
        Integer id = disciplineService.createDiscipline(disciplineName);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiscipline(@RequestBody Map<String, Object> request) {
        Integer disciplineId = (Integer) request.get("id");
        disciplineService.deleteDiscipline(disciplineId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
