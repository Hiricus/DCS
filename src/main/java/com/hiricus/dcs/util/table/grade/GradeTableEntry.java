package com.hiricus.dcs.util.table.grade;

import com.hiricus.dcs.model.object.user.UserDataObject;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GradeTableEntry {
    private String grade;
    private Integer userId;
    private String surname;
    private String name;
    private String patronymic;
    private String passport;
    private String disciplineName;

    public GradeTableEntry(UserDataObject userData, String disciplineName) {
        this.userId = userData.getId();
        this.name = userData.getName();
        this.surname = userData.getSurname();
        this.patronymic = userData.getPatronymic();
        this.passport = userData.getPassport();
        this.disciplineName = disciplineName;
    }

    public GradeTableEntry(List<String> values) {
        if (values.size() != 7) {
            throw new IllegalArgumentException("Cannot create GroupTableEntry from passed values: quantities don't match");
        }

        this.grade = values.get(0);
        this.userId = !values.get(1).isEmpty() ? Integer.valueOf(values.get(1)) : null;
        this.surname = values.get(2);
        this.name = values.get(3);
        this.patronymic = values.get(4);
        this.passport = values.get(5);
        this.disciplineName = values.get(6);
    }
}
