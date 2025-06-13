package com.hiricus.dcs.model.object.user;

import com.hiricus.dcs.dto.UserDataDto;
import lombok.ToString;
import org.jooq.Record;

import java.time.LocalDateTime;

import static com.hiricus.dcs.generated.public_.Tables.USER_DATA;

@ToString
public class UserDataObject {
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDateTime birthDate;
    private String phoneNumber;
    private String passport;
    private String snils;

    public UserDataObject(int id, UserDataDto dataDto) {
        this.id = id;
        this.name = dataDto.getName();
        this.surname = dataDto.getSurname();
        this.patronymic = dataDto.getPatronymic();
        this.birthDate = dataDto.getBirthDate();
        this.phoneNumber = dataDto.getPhoneNumber();
        this.passport = dataDto.getPassport();
        this.snils = dataDto.getSnils();
    }
    public UserDataObject(Record record) {
        this.id = record.get(USER_DATA.USER_ID);
        this.name = record.get(USER_DATA.NAME);
        this.surname = record.get(USER_DATA.SURNAME);
        this.patronymic = record.get(USER_DATA.PATRONYMIC);
        this.birthDate = record.get(USER_DATA.BIRTH_DATE);
        this.phoneNumber = record.get(USER_DATA.PHONE_NUMBER);
        this.passport = record.get(USER_DATA.PASSPORT);
        this.snils = record.get(USER_DATA.SNILS);
    }
    public UserDataObject(int id, String name, String surname, String patronymic, LocalDateTime birthDate, String phoneNumber, String passport, String snils) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
        this.snils = snils;
    }
    public UserDataObject(String name, String surname, String patronymic, LocalDateTime birthDate, String phoneNumber, String passport, String snils) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
        this.snils = snils;
    }
    public UserDataObject(int id) {
        this.id = id;
    }
    public UserDataObject() {}

    // getters
    public Integer getId() {
        return this.id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public LocalDateTime getBirthDate() {
        return birthDate;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPassport() {
        return passport;
    }
    public String getSnils() {
        return snils;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setPassport(String passport) {
        this.passport = passport;
    }
    public void setSnils(String snils) {
        this.snils = snils;
    }
}
