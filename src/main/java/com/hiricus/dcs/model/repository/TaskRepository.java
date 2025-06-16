package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.task.TaskObject;
import com.hiricus.dcs.model.object.user.UserDataObject;
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

    public List<TaskObject> findAllByAuthorId(int authorId) {
        return jooq.select(TASK.asterisk())
                .from(TASK)
                .where(TASK.TASK_AUTHOR_ID.eq(authorId))
                .fetch(TaskObject::new);
    }

    public List<TaskObject> findAllCompletedByAuthorId(int authorId) {
        return jooq.select(TASK.asterisk())
                .from(TASK)
                .where(TASK.TASK_AUTHOR_ID.eq(authorId))
                .and(TASK.IS_COMPLETED.eq(true))
                .fetch(TaskObject::new);
    }

    public List<TaskObject> findAllUncompletedByAuthorId(int authorId) {
        return jooq.select(TASK.asterisk())
                .from(TASK)
                .where(TASK.TASK_AUTHOR_ID.eq(authorId))
                .and(TASK.IS_COMPLETED.eq(false))
                .fetch(TaskObject::new);
    }

    public List<TaskObject> findAllUncompletedByGroupId(int groupId) {
        return jooq.select(TASK.asterisk())
                .from(TASK)
                .where(TASK.GROUP_ID.eq(groupId))
                .and(TASK.IS_COMPLETED.eq(false))
                .fetch(TaskObject::new);
    }

    public Optional<Integer> createTask(TaskObject task) {
        return jooq.insertInto(TASK)
                .set(TASK.TASK_NAME, task.getName())
                .set(TASK.TASK_TYPE, task.getType().name())
                .set(TASK.HAS_DOCUMENTS, task.isHasDocuments())
                .set(TASK.GROUP_ID, task.getRelatedGroupId())
                .returningResult(TASK.ID)
                .fetchOptional(TASK.ID);
    }

    public int updateTask(TaskObject task) {
        return jooq.update(TASK)
                .set(TASK.TASK_NAME, task.getName())
                .set(TASK.TASK_TYPE, task.getType().name())
//                .set(TASK.HAS_DOCUMENTS, task.isHasDocuments())
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

    public void setCompleted(int id, boolean completed) {
        jooq.update(TASK)
                .set(TASK.IS_COMPLETED, completed)
                .where(TASK.ID.eq(id))
                .execute();
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

    public int setRelatedGroup(int taskId, int groupId) {
        return jooq.update(TASK)
                .set(TASK.GROUP_ID, groupId)
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
    public List<UserDataObject> getAllTaskSubjects(int taskId) {
        return jooq.select(USER_DATA.asterisk()).from(TASK_USER_RELATION)
                .join(USER_DATA).on(TASK_USER_RELATION.USER_ID.eq(USER_DATA.USER_ID))
                .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                .fetch(UserDataObject::new);
    }
    public List<UserDataObject> getAllTaskSubjectsWithChecking(int taskId, boolean checkedStatus) {
        return jooq.select(USER_DATA.asterisk()).from(TASK_USER_RELATION)
                .join(USER_DATA).on(TASK_USER_RELATION.USER_ID.eq(USER_DATA.USER_ID))
                .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                .and(TASK_USER_RELATION.CHECKED.eq(checkedStatus))
                .fetch(UserDataObject::new);
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

    // TODO: проверить что нормально работает т.к. я это сам писал
    public int updateCheckedForTaskSubjects(int taskId, List<Integer> userIds, boolean checked) {
        return jooq.update(TASK_USER_RELATION)
                .set(TASK_USER_RELATION.CHECKED, checked)
                .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                .and(TASK_USER_RELATION.USER_ID.in(userIds))
                .execute();
    }

    public int removeTaskSubject(int taskId, int userId) {
        return jooq.delete(TASK_USER_RELATION)
                .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                .and(TASK_USER_RELATION.USER_ID.eq(userId))
                .execute();
    }

    public int removeTaskSubjects(int taskId, List<Integer> usersIds) {
        List<DeleteConditionStep<TaskUserRelationRecord>> queries = usersIds.stream()
                .map(userId -> {
                    return jooq.delete(TASK_USER_RELATION)
                            .where(TASK_USER_RELATION.TASK_ID.eq(taskId))
                            .and(TASK_USER_RELATION.USER_ID.eq(userId));
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
