package com.hiricus.dcs.model.object.discipline;

import com.hiricus.dcs.model.object.user.UserObject;
import lombok.ToString;
import org.jooq.Record;

import java.util.ArrayList;
import java.util.List;

import static com.hiricus.dcs.generated.public_.Tables.DISCIPLINE;

@ToString
public class DisciplineObject {
    private int id;
    private String name;

    private List<UserObject> students = new ArrayList<>();
    private List<FinalGradeObject> grades = new ArrayList<>();

    public DisciplineObject(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public DisciplineObject(String name) {
        this.name = name;
    }
    public DisciplineObject() {}

    public DisciplineObject(Record record) {
        this.id = record.get(DISCIPLINE.ID);
        this.name = record.get(DISCIPLINE.NAME);
    }

    // getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<UserObject> getStudents() {
        return students;
    }
    public List<FinalGradeObject> getGrades() {
        return grades;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStudents(List<UserObject> students) {
        this.students = students;
    }
    public void setGrades(List<FinalGradeObject> grades) {
        this.grades = grades;
    }
}
