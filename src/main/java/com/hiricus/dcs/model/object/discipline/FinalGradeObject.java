package com.hiricus.dcs.model.object.discipline;

import lombok.Setter;
import lombok.ToString;
import org.jooq.Record;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.hiricus.dcs.generated.public_.Tables.FINAL_GRADE;

@ToString
@Setter
public class FinalGradeObject {
    private long id;
    private String grade;
    private LocalDate attestationPeriod;
    private int userId;
    private int disciplineId;

    public FinalGradeObject() {}
    public FinalGradeObject(String grade, LocalDate attestationPeriod) {
        this.grade = grade;
        this.attestationPeriod = attestationPeriod;
    }
    public FinalGradeObject(String grade, LocalDate attestationPeriod, int userId, int disciplineId) {
        this.grade = grade;
        this.attestationPeriod = attestationPeriod;
        this.userId = userId;
        this.disciplineId = disciplineId;
    }
    public FinalGradeObject(long id, String grade, LocalDate attestationPeriod, int userId, int disciplineId) {
        this(grade, attestationPeriod, userId, disciplineId);
        this.id = id;
    }

    public FinalGradeObject(Record record) {
        this.id = record.get(FINAL_GRADE.ID);
        this.grade = record.get(FINAL_GRADE.GRADE);
        this.attestationPeriod = record.get(FINAL_GRADE.ATTESTATION_PERIOD);
        this.userId = record.get(FINAL_GRADE.USER_ID);
        this.disciplineId = record.get(FINAL_GRADE.DISCIPLINE_ID);
    }

    // getter
    public long getId() {
        return id;
    }
    public String getGrade() {
        return grade;
    }
    public LocalDate getAttestationPeriod() {
        return attestationPeriod;
    }
    public int getUserId() {
        return userId;
    }
    public int getDisciplineId() {
        return disciplineId;
    }
}
