package com.hiricus.dcs.dto;

import com.hiricus.dcs.model.object.user.UserDataObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserDataDto {
    private String name;
    private String surname;
    private String patronymic;
    private LocalDateTime birthDate;
    private String phoneNumber;
    private String passport;
    private String snils;

    public UserDataDto(UserDataObject userData) {
        this.name = userData.getName();
        this.surname = userData.getSurname();
        this.patronymic = userData.getPatronymic();
        this.birthDate = userData.getBirthDate();
        this.phoneNumber = userData.getPhoneNumber();
        this.passport = userData.getPassport();
        this.snils = userData.getSnils();
    }
}
