package com.hiricus.dcs;

import com.hiricus.dcs.dto.request.TaskCreationRequest;
import com.hiricus.dcs.dto.task.TaskDto;
import com.hiricus.dcs.dto.task.TaskSubjectDto;
import com.hiricus.dcs.model.object.task.TaskObject;
import com.hiricus.dcs.security.data.CustomUserDetails;
import com.hiricus.dcs.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAll(@RequestHeader(name = "include_completed", defaultValue = "false") Boolean includeCompleted, Authentication authentication) {
        Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        List<TaskDto> response = taskService.getAllOwnedTasks(userId, includeCompleted)
                .stream()
                .map(TaskDto::new)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/assignedToHead")
    public ResponseEntity<List<TaskDto>> getAssignedToHead(Authentication authentication) {
        Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        List<TaskDto> response = taskService.getAllAssignedToHead(userId)
                .stream()
                .map(TaskDto::new)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> createTask(@RequestBody TaskCreationRequest request, Authentication authentication) {
        Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        return new ResponseEntity<>(taskService.createTask(request, userId), HttpStatus.CREATED);
    }

    @PutMapping("/setCompleted/{id}")
    public ResponseEntity<?> setCompleted(@PathVariable("id") Integer taskId, @RequestParam Boolean completed) {
        taskService.setCompleted(taskId, completed);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Integer id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<TaskSubjectDto>> getTaskSubjects(@PathVariable("id") Integer taskId) {
        List<TaskSubjectDto> response = taskService.getTaskSubjects(taskId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/subjects")
    public ResponseEntity<?> updateSubjectsCompletionStatus(@PathVariable("id") Integer taskId, @RequestBody List<TaskSubjectDto> subjects) {
        taskService.updateSubjectsCompletionStatus(taskId, subjects);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
