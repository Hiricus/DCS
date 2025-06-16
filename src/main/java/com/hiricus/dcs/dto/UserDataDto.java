package com.hiricus.dcs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiricus.dcs.model.object.user.UserDataObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserDataDto {
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;

    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDateTime birthDate;

    @JsonProperty("phone_number")
    private String phoneNumber;
    private String passport;
    private String snils;

    public UserDataDto(UserDataObject userData) {
        this.id = userData.getId();
        this.name = userData.getName();
        this.surname = userData.getSurname();
        this.patronymic = userData.getPatronymic();
        this.birthDate = userData.getBirthDate();
        this.phoneNumber = userData.getPhoneNumber();
        this.passport = userData.getPassport();
        this.snils = userData.getSnils();
    }
}
