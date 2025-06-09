package com.hiricus.dcs.dto;

import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserDataObject;

import java.util.ArrayList;
import java.util.List;

public class GroupDto {
    private Integer id;
    private String name;
    private Integer course;
    private Integer entranceYear;

    private UserDto curator;
    private UserDto head;

    private List<UserDto> includedUsers = new ArrayList<>();

    public GroupDto(GroupObject groupObject) {
        this.id = groupObject.getId();
        this.name = groupObject.getName();
        this.course = groupObject.getCourse();
        this.entranceYear = groupObject.getYear();

        this.curator = groupObject.getCurator() != null ? new UserDto(groupObject.getCurator()) : null;
        this.head = groupObject.getHead() != null ? new UserDto(groupObject.getHead()) : null;


        List<UserDataObject> userDataObjects = groupObject.getMembers();
        if (userDataObjects != null) {
            for (UserDataObject userDataObject : userDataObjects) {
                this.includedUsers.add(new UserDto(userDataObject));
            }
        }
    }
}
