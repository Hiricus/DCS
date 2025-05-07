package com.hiricus.dcs.model.object.user;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class RoleObject {
    private int id;
    private String roleName;

    public RoleObject(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
    public RoleObject(String roleName) {
        this.roleName = roleName;
    }
    public RoleObject() {}

    // getters
    public int getId() {
        return id;
    }
    public String getRoleName() {
        return roleName;
    }
}
