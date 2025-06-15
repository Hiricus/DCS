package com.hiricus.dcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GradeDto {
    private final long id;
    private final String grade;
    @JsonProperty("discipline_id")
    private int disciplineId;

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("user_surname")
    private String userSurname;
    @JsonProperty("discipline_name")
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
