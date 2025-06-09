package com.hiricus.dcs.model.object.group;

import com.hiricus.dcs.dto.request.GroupCreationRequest;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import lombok.ToString;
import org.jooq.Record;

import java.util.ArrayList;
import java.util.List;

import static com.hiricus.dcs.generated.public_.Tables.STUDENT_GROUP;

@ToString
public class GroupObject {
    private int id;
    private String name;
    private int course;
    private int year;

    private UserObject curator;
    private UserObject head;

    private List<UserDataObject> members = new ArrayList<>();

    // constructors
    public GroupObject(GroupCreationRequest request) {
        this.name = request.getName();
        this.course = request.getCourse();
        this.year = request.getEntranceYear();
        this.curator = new UserObject(request.getCuratorId());
        this.head = new UserObject(request.getHeadId());
    }
    public GroupObject(int id, String name, int course, int year) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.year = year;
    }
    public GroupObject() {}

    public GroupObject(Record record) {
        this.id = record.get(STUDENT_GROUP.ID);
        this.name = record.get(STUDENT_GROUP.GROUP_NAME);
        this.course = record.get(STUDENT_GROUP.COURSE);
        this.year = record.get(STUDENT_GROUP.ENTRANCE_YEAR);
    }

    // getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getCourse() {
        return course;
    }
    public int getYear() {
        return year;
    }
    public UserObject getCurator() {
        return curator;
    }
    public UserObject getHead() {
        return head;
    }
    public List<UserDataObject> getMembers() {
        return members;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCourse(int course) {
        this.course = course;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setCurator(UserObject curator) {
        this.curator = curator;
    }
    public void setHead(UserObject head) {
        this.head = head;
    }
    public void setMembers(List<UserDataObject> members) {
        this.members = members;
    }
}