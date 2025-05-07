package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.task.TaskObject;
import com.hiricus.dcs.model.object.user.UserObject;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.InsertSetMoreStep;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.hiricus.dcs.generated.public_.tables.records.TaskUserRelationRecord;

import static com.hiricus.dcs.generated.public_.Tables.*;

@Repository
public class TaskRepository {
    private final DSLContext jooq;

    public TaskRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // CRUD methods
    public List<TaskObject> findAll() {
        return jooq.select(TASK.asterisk())
                .from(TASK)
                .fetch(TaskObject::new);
    }

    public Optional<TaskObject> findTaskById(int id) {
        return jooq.select(TASK.asterisk())
                .from(TASK)
                .where(TASK.ID.eq(id))
                .fetchOptional(TaskObject::new);
    }

    public Optional<Integer> createEmptyTask(TaskObject task) {
        return jooq.insertInto(TASK)
                .set(TASK.TASK_NAME, task.getName())
                .set(TASK.TASK_TYPE, task.getType().name())
                .set(TASK.HAS_DOCUMENTS, task.isHasDocuments())
                .returningResult(TASK.ID)
                .fetchOptional(TASK.ID);
    }

    public int updateTask(TaskObject task) {
        return jooq.update(TASK)
                .set(TASK.TASK_NAME, task.getName())
                .set(TASK.TASK_TYPE, task.getType().name())
                .set(TASK.HAS_DOCUMENTS, task.isHasDocuments())
                .where(TASK.ID.eq(task.getId()))
                .execute();
    }

    public int deleteTaskById(int id) {
        return jooq.delete(TASK)
                .where(TASK.ID.eq(id))
                .execute();
    }

    public boolean isTaskExistsById(int id) {
        return findTaskById(id).isPresent();
    }

    // Work with author
    public Optional<UserObject> getTaskAuthor(int taskId) {
        return jooq.select(USERS.asterisk())
                .from(TASK)
                .join(USERS).on(TASK.TASK_AUTHOR_ID.eq(USERS.ID))
                .where(TASK.ID.eq(taskId))
                .fetchOptional(UserObject::new);
    }

    public int setAuthor(int taskId, UserObject user) {
        return jooq.update(TASK)
                .set(TASK.TASK_AUTHOR_ID, user.getId())
                .where(TASK.ID.eq(taskId))
                .execute();
    }

    // Work with group
    public Optional<GroupObject> getRelatedGroup(int taskId) {
        return jooq.select(STUDENT_GROUP.asterisk()).from(TASK)
                .join(STUDENT_GROUP).on(TASK.GROUP_ID.eq(STUDENT_GROUP.ID))
                .where(TASK.ID.eq(taskId))
                .fetchOptional(GroupObject::new);
    }

    public int setRelatedGroup(int taskId, GroupObject group) {
        return jooq.update(TASK)
                .set(TASK.GROUP_ID, group.getId())
                .where(TASK.ID.eq(taskId))
                .execute();
    }

    public int removeRelatedGroup(int taskId) {
        return jooq.update(TASK)
                .setNull(TASK.GROUP_ID)
                .where(TASK.ID.eq(taskId))
                .execute();
    }

    // Work with task subjects
    public List<UserObject> getTaskSubjects(int taskId) {
        return jooq.select(USERS.asterisk()).from(TASK_USER_RELATION)
                .join(USERS).on(TASK_USER_RELATION.USER_ID.eq(USERS.ID))
                .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                .fetch(UserObject::new);
    }

    public int addTaskSubjects(int taskId, List<UserObject> users) {
        List<InsertSetMoreStep<TaskUserRelationRecord>> queries = users.stream()
                .map(user -> {
                    return jooq.insertInto(TASK_USER_RELATION)
                            .set(TASK_USER_RELATION.TASK_ID, taskId)
                            .set(TASK_USER_RELATION.USER_ID, user.getId());
                }).toList();

        return jooq.batch(queries).execute().length;
    }

    public int removeTaskSubject(int taskId, UserObject user) {
        return jooq.delete(TASK_USER_RELATION)
                .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                .and(TASK_USER_RELATION.USER_ID.eq(user.getId()))
                .execute();
    }

    public int removeTaskSubjects(int taskId, List<UserObject> users) {
        List<DeleteConditionStep<TaskUserRelationRecord>> queries = users.stream()
                .map(user -> {
                    return jooq.delete(TASK_USER_RELATION)
                            .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                            .and(TASK_USER_RELATION.USER_ID.eq(user.getId()));
                }).toList();

        return jooq.batch(queries).execute().length;
    }

    public int removeAllTaskSubjects(int taskId) {
        return jooq.delete(TASK_USER_RELATION)
                .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                .execute();
    }

    // Work with linked documents
    // TODO Протестить хоть как то
    public boolean isDocumentLinked(int taskId, long documentId) {
        return jooq.select(TASK_DOCUMENT_RELATION.asterisk())
                .from(TASK_DOCUMENT_RELATION)
                .where(TASK_DOCUMENT_RELATION.TASK_ID.eq(taskId))
                .and(TASK_DOCUMENT_RELATION.DOCUMENT_ID.eq(documentId))
                .fetchOptional().isPresent();
    }

    public int linkDocument(int taskId, long documentId) {
        return jooq.insertInto(TASK_DOCUMENT_RELATION)
                .set(TASK_DOCUMENT_RELATION.DOCUMENT_ID, documentId)
                .set(TASK_DOCUMENT_RELATION.TASK_ID, taskId)
                .execute();
    }

    public int unlinkDocument(int taskId, long documentId) {
        return jooq.delete(TASK_DOCUMENT_RELATION)
                .where(TASK_DOCUMENT_RELATION.DOCUMENT_ID.eq(documentId))
                .and(TASK_DOCUMENT_RELATION.TASK_ID.eq(taskId))
                .execute();
    }
}
