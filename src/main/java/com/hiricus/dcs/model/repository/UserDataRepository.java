package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.user.UserDataObject;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.jooq.Record;

import java.util.List;
import java.util.Optional;

import static com.hiricus.dcs.generated.public_.Tables.USER_DATA;

@Repository
public class UserDataRepository {
    private final DSLContext jooq;

    public UserDataRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    private UserDataObject mapToUserDataObject(Record record) {
        UserDataObject userData = new UserDataObject();
        userData.setId(record.get(USER_DATA.USER_ID));
        userData.setName(record.get(USER_DATA.NAME));
        userData.setSurname(record.get(USER_DATA.SURNAME));
        userData.setPatronymic(record.get(USER_DATA.PATRONYMIC));
        userData.setBirthDate(record.get(USER_DATA.BIRTH_DATE));
        userData.setPhoneNumber(record.get(USER_DATA.PHONE_NUMBER));
        userData.setPassport(record.get(USER_DATA.PASSPORT));
        userData.setSnils(record.get(USER_DATA.SNILS));

        return userData;
    }

    // CRUD methods
    public Optional<UserDataObject> findUserDataByUserId(int id) {
        return jooq.select(USER_DATA.asterisk())
                .from(USER_DATA)
                .where(USER_DATA.USER_ID.eq(id))
                .fetchOptional(this::mapToUserDataObject);
    }

    public Optional<UserDataObject> findUserDataByFullName(String surname, String name, String patronymic) {
        return jooq.select(USER_DATA.asterisk())
                .from(USER_DATA)
                .where(USER_DATA.SURNAME.eq(surname))
                .and(USER_DATA.NAME.eq(name))
                .and(USER_DATA.PATRONYMIC.eq(patronymic))
                .fetchOptional(this::mapToUserDataObject);
    }

    public List<UserDataObject> findAll() {
        return jooq.select(USER_DATA.asterisk())
                .from(USER_DATA)
                .fetch(this::mapToUserDataObject);
    }

    // TODO: сделать добавление пустой записи с id в USERS
    public Optional<Integer> createUserData(UserDataObject userData) {
        return jooq.insertInto(USER_DATA)
                .set(USER_DATA.USER_ID, userData.getId())
                .set(USER_DATA.NAME, userData.getName())
                .set(USER_DATA.SURNAME, userData.getSurname())
                .set(USER_DATA.PATRONYMIC, userData.getPatronymic())
                .set(USER_DATA.BIRTH_DATE, userData.getBirthDate())
                .set(USER_DATA.PHONE_NUMBER, userData.getPhoneNumber())
                .set(USER_DATA.PASSPORT, userData.getPassport())
                .set(USER_DATA.SNILS, userData.getSnils())
                .returningResult(USER_DATA.USER_ID)
                .fetchOptional(USER_DATA.USER_ID);
    }

    public int updateUserData(UserDataObject userData) {
        return jooq.update(USER_DATA)
                .set(USER_DATA.NAME, userData.getName())
                .set(USER_DATA.SURNAME, userData.getSurname())
                .set(USER_DATA.PATRONYMIC, userData.getPatronymic())
                .set(USER_DATA.BIRTH_DATE, userData.getBirthDate())
                .set(USER_DATA.PHONE_NUMBER, userData.getPhoneNumber())
                .set(USER_DATA.PASSPORT, userData.getPassport())
                .set(USER_DATA.SNILS, userData.getSnils())
                .where(USER_DATA.USER_ID.eq(userData.getId()))
                .execute();
    }

    public int deleteUserDataByUserId(int id) {
        return jooq.delete(USER_DATA)
                .where(USER_DATA.USER_ID.eq(id))
                .execute();
    }

    public boolean isUserDataExistsById(int id) {
        return findUserDataByUserId(id).isPresent();
    }
}