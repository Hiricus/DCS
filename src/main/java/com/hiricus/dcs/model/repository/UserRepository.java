package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserObject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hiricus.dcs.generated.public_.Tables.USERS;
import static com.hiricus.dcs.generated.public_.Tables.USER_DATA;
import static com.hiricus.dcs.generated.public_.Tables.USER_DISCIPLINE_RELATION;
import static com.hiricus.dcs.generated.public_.Tables.DISCIPLINE;
import static com.hiricus.dcs.generated.public_.Tables.USER_GROUP_RELATION;
import static com.hiricus.dcs.generated.public_.Tables.STUDENT_GROUP;

@Repository
public class UserRepository {
    private final DSLContext jooq;

    public UserRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // CRUD methods
    public Optional<UserObject> findUserById(int id) {
        return jooq.select(USERS.asterisk())
                .from(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional(UserObject::new);
    }

    public Optional<UserObject> findUserByLogin(String login) {
        return jooq.select(USERS.asterisk())
                .from(USERS)
                .where(USERS.LOGIN.eq(login))
                .fetchOptional(UserObject::new);
    }

    public List<UserObject> findAll() {
        return jooq.select(USERS.asterisk())
                .from(USERS)
                .fetch(UserObject::new);
    }

    public Optional<Integer> createUser(UserObject user) {
        return jooq.insertInto(USERS)
                .set(USERS.LOGIN, user.getLogin())
                .set(USERS.EMAIL, user.getEmail())
                .set(USERS.PASSWORD, user.getPassword())
                .returningResult(USERS.ID)
                .fetchOptional(USERS.ID);
    }

    public int updateUser(UserObject user) {
        return jooq.update(USERS)
                .set(USERS.LOGIN, user.getLogin())
                .set(USERS.EMAIL, user.getEmail())
                .set(USERS.PASSWORD, user.getPassword())
                .set(USERS.LAST_LOGIN_TIME, user.getLastLoginTime())
                .where(USERS.ID.eq(user.getId()))
                .execute();
    }

    public int deleteUserById(int id) {
        return jooq.delete(USERS)
                .where(USERS.ID.eq(id))
                .execute();
    }

    public boolean isUserExistsById(int id) {
        return findUserById(id).isPresent();
    }

    public boolean isUserExistsByLogin(String login) {
        return findUserByLogin(login).isPresent();
    }

    // Work with groups
    public Optional<GroupObject> getUsersGroup(int userId) {
        return jooq.select(STUDENT_GROUP.asterisk()).from(STUDENT_GROUP)
                .leftJoin(USER_GROUP_RELATION).on(STUDENT_GROUP.ID.eq(USER_GROUP_RELATION.GROUP_ID))
                .leftJoin(USERS).on(USERS.ID.eq(USER_GROUP_RELATION.USER_ID))
                .where(USERS.ID.eq(userId))
                .fetchOptional(GroupObject::new);
    }

    // Work with disciplines
    public List<DisciplineObject> getUsersDisciplines(Integer userId) {
        return jooq.select(DISCIPLINE.asterisk()).from(USER_DISCIPLINE_RELATION)
                .leftJoin(DISCIPLINE).on(USER_DISCIPLINE_RELATION.DISCIPLINE_ID.eq(DISCIPLINE.ID))
                .where(USER_DISCIPLINE_RELATION.USER_ID.eq(userId))
                .fetch(DisciplineObject::new);
    }
}
