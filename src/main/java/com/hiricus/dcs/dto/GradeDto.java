package com.hiricus.dcs.dto;

import lombok.Data;

@Data
public class GradeDto {
    private final long id;
    private final String grade;
    private final int disciplineId;

    private int userId;
    private String userName;
    private String userSurname;
    private String disciplineName;
}
