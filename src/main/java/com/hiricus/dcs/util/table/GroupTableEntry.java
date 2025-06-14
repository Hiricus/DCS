package com.hiricus.dcs.util.table;

import com.hiricus.dcs.model.object.user.UserDataObject;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GroupTableEntry {
    private Integer id;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDateTime birthDate;
    private String phoneNumber;
    private String passport;
    private String snils;
    private String groupName;

    public GroupTableEntry(List<String> values) {
        if (values.size() != 9) {
            throw new IllegalArgumentException("Cannot create GroupTableEntry from passed values: quantities don't match");
        }

        this.id = !values.get(0).isEmpty() ? Integer.valueOf(values.get(0)) : null;
        this.surname = values.get(1);
        this.name = values.get(2);
        this.patronymic = values.get(3);
        this.birthDate = !values.get(4).isEmpty() ? dateFromString(values.get(4)) : null;
        this.phoneNumber = values.get(5);
        this.passport = values.get(6);
        this.snils = values.get(7);
        this.groupName = values.get(8);
    }

    public GroupTableEntry(UserDataObject userData, String groupName) {
        this.id = userData.getId();
        this.surname = userData.getSurname();
        this.name = userData.getName();
        this.patronymic = userData.getPatronymic();
        this.birthDate = userData.getBirthDate();
        this.phoneNumber = userData.getPhoneNumber();
        this.passport = userData.getPassport();
        this.snils = userData.getSnils();
        this.groupName = groupName;
    }

    private LocalDateTime dateFromString(String dateTimeStr) {
        dateTimeStr += " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        return dateTime;
    }
}