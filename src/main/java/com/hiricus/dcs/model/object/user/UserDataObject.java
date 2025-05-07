package com.hiricus.dcs.model.object.user;

import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class UserDataObject {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDateTime birthDate;
    private String phoneNumber;
    private String passport;
    private String snils;

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
    public UserDataObject() {}

    // getters
    public int getId() {
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
