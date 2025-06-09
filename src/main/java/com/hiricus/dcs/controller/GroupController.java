package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.GroupDto;
import com.hiricus.dcs.dto.request.GroupCreationRequest;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.security.data.CustomUserDetails;
import com.hiricus.dcs.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// TODO: сделать проверки является ли пользователь куратором изменяемой группы
@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupDto>> getAllGroups(@RequestHeader(name = "include_users", required = false, defaultValue = "false") boolean includeUsers,
                                       Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<GroupDto> response;
        // Для админа возвращаются все группы
        if (isAdmin) {
            List<GroupObject> groups = groupService.getAllGroups(includeUsers);
            response = groups.stream().map(GroupDto::new).toList();
        // Для куратора возвращаются только курируемые группы
        } else {
            Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
            List<GroupObject> groups = groupService.getCuratedGroups(userId, includeUsers);
            response = groups.stream().map(GroupDto::new).toList();
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@RequestHeader(name = "include_users", required = false, defaultValue = "false") boolean includeUsers,
                                                 @PathVariable("id") Integer id) {
        GroupObject group = groupService.getGroupById(id, includeUsers);
        GroupDto groupDto = new GroupDto(group);
        return new ResponseEntity<>(groupDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> createGroup(@RequestBody GroupCreationRequest request) {
        Integer groupId = groupService.createGroup(new GroupObject(request));
        return new ResponseEntity<>(groupId, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable("id") Integer id,
                                         @RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        Integer course = (Integer) request.get("course");
        Integer entranceYear = (Integer) request.get("entrance_year");

        GroupObject group = new GroupObject(id, name, course, entranceYear);
        groupService.updateGroupExcludingStaff(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable("id") Integer id) {
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{groupId}/users")
    public ResponseEntity<?> addUsersToGroup(@PathVariable("groupId") Integer groupId,
                                             @RequestBody List<Integer> userIds) {

        groupService.addUsersToGroup(groupId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/users")
    public ResponseEntity<?> removeUsersFromGroup(@PathVariable("groupId") Integer groupId,
                                                  @RequestBody List<Integer> userIds) {
        groupService.removeUsersFromGroup(groupId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
