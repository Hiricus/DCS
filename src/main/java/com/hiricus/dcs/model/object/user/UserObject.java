package com.hiricus.dcs.model.object.user;

import lombok.Data;
import org.jooq.Record;

import java.time.LocalDateTime;
import java.util.List;

import static com.hiricus.dcs.generated.public_.Tables.USERS;

@Data
public class UserObject {
    private int id;
    private String login;
    private String email;
    private String password;
    private LocalDateTime lastLoginTime;

    private UserDataObject userData;

    private List<String> userRoles;

    public UserObject(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public UserObject(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }
    public UserObject(int id, String login, String email, String password, LocalDateTime lastLoginTime) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.lastLoginTime = lastLoginTime;
    }
    public UserObject(int id, String login, String email, String password, LocalDateTime lastLoginTime, UserDataObject userData) {
        this(id, login, email, password, lastLoginTime);
        this.userData = userData;
    }
    public UserObject(int id) {
        this.id = id;
    }
    public UserObject() {}

    public UserObject(Record record) {
        this.id = record.get(USERS.ID);
        this.login = record.get(USERS.LOGIN);
        this.email = record.get(USERS.EMAIL);
        this.password = record.get(USERS.PASSWORD);
        this.lastLoginTime = record.get(USERS.LAST_LOGIN_TIME);
    }

    // getter
    public int getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }
    public UserDataObject getUserData() {
        return userData;
    }
    public List<String> getUserRoles() {
        return userRoles;
    }

    // setter
    public void setUserData(UserDataObject userData) {
        this.userData = userData;
    }
}
