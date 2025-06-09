package com.hiricus.dcs.dto;

import lombok.Data;

@Data
public class GradeDto {
    private final long id;
    private final String grade;
    private int disciplineId;

    private int userId;
    private String userName;
    private String userSurname;
    private String disciplineName;

    public GradeDto(long id, String grade, int disciplineId, String disciplineName) {
        this.id = id;
        this.grade = grade;
        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
    }

    public GradeDto(long id, int userId, String userName, String grade) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.grade = grade;
    }
}
