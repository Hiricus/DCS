package com.hiricus.dcs.dto.task;

import com.hiricus.dcs.model.object.user.UserDataObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskSubjectDto {
    private Integer id;
    private Boolean checked;
    private String name;
    private String surname;
    private String patronymic;
    private String passport;

    public TaskSubjectDto(UserDataObject userData, Boolean checked) {
        this.id = userData.getId();
        this.name = userData.getName();
        this.surname = userData.getSurname();
        this.patronymic = userData.getPatronymic();
        this.passport = userData.getPassport();

        this.checked = checked;
    }
}
