package com.hiricus.dcs.dto;

import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Data
public class UserDto {
    private final int id;
    private String login;
    private String name;
    private String surname;
    private String patronymic;

    public UserDto(UserDataObject userData) {
        this.id = userData.getId();
        this.name = userData.getName();
        this.surname = userData.getSurname();
        this.patronymic = userData.getPatronymic();
    }

    public UserDto(UserObject userObject) {
        this.id = userObject.getId();
        this.login = userObject.getLogin();
    }

    public UserDto(int id, String login) {
        this.id = id;
        this.login = login;
    }
}
