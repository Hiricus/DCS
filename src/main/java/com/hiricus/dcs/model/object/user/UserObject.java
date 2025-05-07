package com.hiricus.dcs.model.object.user;

import lombok.Setter;
import lombok.ToString;
import org.jooq.Record;

import java.time.LocalDateTime;

import static com.hiricus.dcs.generated.public_.Tables.USERS;

@Setter
@ToString
public class UserObject {
    private int id;
    private String login;
    private String email;
    private String password;
    private LocalDateTime lastLoginTime;

    private UserDataObject userData;

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

    // setter
    public void setUserData(UserDataObject userData) {
        this.userData = userData;
    }
}
