package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hiricus.dcs.generated.public_.Tables.FINAL_GRADE;
import static com.hiricus.dcs.generated.public_.Tables.USERS;
import static com.hiricus.dcs.generated.public_.Tables.DISCIPLINE;

@Repository
public class GradeRepository {
    private final DSLContext jooq;

    public GradeRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // CRUD methods
    public Optional<FinalGradeObject> findGradeById(long id) {
        return jooq.select(FINAL_GRADE.asterisk())
                .from(FINAL_GRADE)
                .where(FINAL_GRADE.ID.eq(id))
                .fetchOptional(FinalGradeObject::new);
    }

    public List<FinalGradeObject> findGradesByUserId(int userId) {
        return jooq.select(FINAL_GRADE.asterisk())
                .from(FINAL_GRADE)
                .where(FINAL_GRADE.USER_ID.eq(userId))
                .fetch(FinalGradeObject::new);
    }

    public List<FinalGradeObject> findGradesByDisciplineId(int disciplineId) {
        return jooq.select(FINAL_GRADE.asterisk())
                .from(FINAL_GRADE)
                .where(FINAL_GRADE.DISCIPLINE_ID.eq(disciplineId))
                .fetch(FinalGradeObject::new);
    }

    public Optional<Long> createGrade(FinalGradeObject grade) {
        return jooq.insertInto(FINAL_GRADE)
                .set(FINAL_GRADE.GRADE, grade.getGrade())
                .set(FINAL_GRADE.ATTESTATION_PERIOD, grade.getAttestationPeriod())
                .set(FINAL_GRADE.USER_ID, grade.getUserId())
                .set(FINAL_GRADE.DISCIPLINE_ID, grade.getDisciplineId())
                .returningResult(FINAL_GRADE.ID)
                .fetchOptional(FINAL_GRADE.ID);
    }

    public int updateGradeValue(Long gradeId, String updatedGrade) {
        return jooq.update(FINAL_GRADE)
                .set(FINAL_GRADE.GRADE, updatedGrade)
                .where(FINAL_GRADE.ID.eq(gradeId))
                .execute();
    }

    public int updateGrade(FinalGradeObject grade) {
        return jooq.update(FINAL_GRADE)
                .set(FINAL_GRADE.GRADE, grade.getGrade())
                .set(FINAL_GRADE.ATTESTATION_PERIOD, grade.getAttestationPeriod())
                .set(FINAL_GRADE.USER_ID, grade.getUserId())
                .set(FINAL_GRADE.DISCIPLINE_ID, grade.getDisciplineId())
                .where(FINAL_GRADE.ID.eq(grade.getId()))
                .execute();
    }

    public int deleteGradeById(long id) {
        return jooq.delete(FINAL_GRADE)
                .where(FINAL_GRADE.ID.eq(id))
                .execute();
    }

    public boolean isGradeExistsById(long id) {
        return findGradeById(id).isPresent();
    }
}
