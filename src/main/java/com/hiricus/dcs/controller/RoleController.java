package com.hiricus.dcs.controller;

import com.hiricus.dcs.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/{id}/sethead")
    public ResponseEntity<?> setHead(@PathVariable Integer id) {
        roleService.setHead(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/setcurator")
    public ResponseEntity<?> setCurator(@PathVariable Integer id) {
        roleService.setCurator(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/removerole")
    public ResponseEntity<?> removeRole(@PathVariable Integer id) {
        roleService.setStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
