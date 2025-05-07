package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.model.object.user.UserObject;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.hiricus.dcs.generated.public_.tables.records.UserDisciplineRelationRecord;

import static com.hiricus.dcs.generated.public_.Tables.DISCIPLINE;
import static com.hiricus.dcs.generated.public_.Tables.USER_DISCIPLINE_RELATION;
import static com.hiricus.dcs.generated.public_.Tables.USERS;

@Repository
public class DisciplineRepository {
    private final DSLContext jooq;

    public DisciplineRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // CRUD methods
    public List<DisciplineObject> findAll() {
        return jooq.select(DISCIPLINE.asterisk())
                .from(DISCIPLINE)
                .fetch(DisciplineObject::new);
    }

    public Optional<DisciplineObject> findDisciplineById(int id) {
        return jooq.select(DISCIPLINE.asterisk())
                .from(DISCIPLINE)
                .where(DISCIPLINE.ID.eq(id))
                .fetchOptional(DisciplineObject::new);
    }

    public Optional<DisciplineObject> findDisciplineByName(String name) {
        return jooq.select(DISCIPLINE.asterisk())
                .from(DISCIPLINE)
                .where(DISCIPLINE.NAME.eq(name))
                .fetchOptional(DisciplineObject::new);
    }

    public Optional<Integer> createEmptyDiscipline(DisciplineObject discipline) {
        return jooq.insertInto(DISCIPLINE)
                .set(DISCIPLINE.NAME, discipline.getName())
                .returningResult(DISCIPLINE.ID)
                .fetchOptional(DISCIPLINE.ID);
    }

    public int updateDiscipline(DisciplineObject discipline) {
        return jooq.update(DISCIPLINE)
                .set(DISCIPLINE.NAME, discipline.getName())
                .where(DISCIPLINE.ID.eq(discipline.getId()))
                .returningResult(DISCIPLINE.ID)
                .execute();
    }

    public int deleteDisciplineById(int id) {
        return jooq.delete(DISCIPLINE)
                .where(DISCIPLINE.ID.eq(id))
                .execute();
    }

    public boolean isExistsById(int id) {
        return findDisciplineById(id).isPresent();
    }

    // Work with users
    public List<UserObject> getAddedUsers(int id) {
        return jooq.select(USERS.asterisk()).from(DISCIPLINE)
                .leftJoin(USER_DISCIPLINE_RELATION).on(DISCIPLINE.ID.eq(USER_DISCIPLINE_RELATION.DISCIPLINE_ID))
                .leftJoin(USERS).on(USER_DISCIPLINE_RELATION.USER_ID.eq(USERS.ID))
                .where(DISCIPLINE.ID.eq(id))
                .fetch(UserObject::new);
    }

    public int addUsers(int disciplineId, List<UserObject> users) {
        List<InsertSetMoreStep<UserDisciplineRelationRecord>> queries = users.stream()
                .map(user -> {
                    return jooq.insertInto(USER_DISCIPLINE_RELATION)
                            .set(USER_DISCIPLINE_RELATION.USER_ID, user.getId())
                            .set(USER_DISCIPLINE_RELATION.DISCIPLINE_ID, disciplineId);
                }).toList();
        return jooq.batch(queries).execute().length;
    }

    public int clearDiscipline(int id) {
        return jooq.delete(USER_DISCIPLINE_RELATION)
                .where(USER_DISCIPLINE_RELATION.DISCIPLINE_ID.eq(id))
                .execute();
    }

    // TODO Work with grades
}