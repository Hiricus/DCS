package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.DocumentDto;
import com.hiricus.dcs.dto.GroupDto;
import com.hiricus.dcs.dto.request.GroupCreationRequest;
import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.security.data.CustomUserDetails;
import com.hiricus.dcs.service.GroupService;
import com.hiricus.dcs.service.XmlTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: сделать проверки является ли пользователь куратором изменяемой группы
@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;
    private final XmlTableService tableService;

    public GroupController(GroupService groupService,
                           XmlTableService tableService) {
        this.groupService = groupService;
        this.tableService = tableService;
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

    // ----------------------- Работа с таблицами -----------------------
    @PostMapping("tables/upload")
    @PreAuthorize("hasRole('CURATOR')")
    public ResponseEntity<?> addUsersAndGroupsFromTable(@RequestBody MultipartFile file, Authentication authentication) {
        DocumentObject table;
        try {
            table = new DocumentObject(file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Integer curatorId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        tableService.handleGroupTable(table, curatorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tables/get/{group_id}")
    public ResponseEntity<DocumentDto> getGroupInfoTable(@PathVariable("group_id") Integer groupId) {
        DocumentObject table = tableService.getGroupInfoTable(groupId);
        DocumentDto response = new DocumentDto(table);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
