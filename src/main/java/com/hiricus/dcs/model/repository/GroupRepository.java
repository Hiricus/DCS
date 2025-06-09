package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.hiricus.dcs.generated.public_.tables.records.UserGroupRelationRecord;

import static com.hiricus.dcs.generated.public_.Tables.*;

@Repository
public class GroupRepository {
    private final DSLContext jooq;

    public GroupRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // CRUD methods
    public Optional<GroupObject> findGroupById(int id) {
        return jooq.select(STUDENT_GROUP.asterisk())
                .from(STUDENT_GROUP)
                .where(STUDENT_GROUP.ID.eq(id))
                .fetchOptional(GroupObject::new);
    }

    public Optional<GroupObject> findGroupByName(String name) {
        return jooq.select(STUDENT_GROUP.asterisk())
                .from(STUDENT_GROUP)
                .where(STUDENT_GROUP.GROUP_NAME.eq(name))
                .fetchOptional(GroupObject::new);
    }

    public List<GroupObject> findAll() {
        return jooq.select(STUDENT_GROUP.asterisk())
                .from(STUDENT_GROUP)
                .fetch(GroupObject::new);
    }

    public List<Integer> findAllGroupIds() {
        return jooq.select(STUDENT_GROUP.ID)
                .from(STUDENT_GROUP)
                .fetch(record -> record.get(STUDENT_GROUP.ID));
    }

    public List<Integer> findCuratedGroupIds(int curatorId) {
        return jooq.select(STUDENT_GROUP.ID)
                .from(STUDENT_GROUP)
                .where(STUDENT_GROUP.CURATOR_ID.eq(curatorId))
                .fetch(record -> record.get(STUDENT_GROUP.ID));
    }

    // Объекты куратора и старосты должны содержать id пользователя
    public Optional<Integer> createEmptyGroupWithStaff(GroupObject group) {
        return jooq.insertInto(STUDENT_GROUP)
                .set(STUDENT_GROUP.GROUP_NAME, group.getName())
                .set(STUDENT_GROUP.COURSE, group.getCourse())
                .set(STUDENT_GROUP.ENTRANCE_YEAR, group.getYear())
                .set(STUDENT_GROUP.CURATOR_ID, group.getCurator().getId())
                .set(STUDENT_GROUP.HEAD_ID, group.getHead().getId())
                .returningResult(STUDENT_GROUP.ID)
                .fetchOptional(STUDENT_GROUP.ID);
    }

    // Не меняет куратора и старосту
    public int updateGroupExcludingStaff(GroupObject group) {
        return jooq.update(STUDENT_GROUP)
                .set(STUDENT_GROUP.GROUP_NAME, group.getName())
                .set(STUDENT_GROUP.COURSE, group.getCourse())
                .set(STUDENT_GROUP.ENTRANCE_YEAR, group.getYear())
                .where(STUDENT_GROUP.ID.eq(group.getId()))
                .execute();
    }

    public int deleteGroupById(int id) {
        return jooq.delete(STUDENT_GROUP)
                .where(STUDENT_GROUP.ID.eq(id))
                .execute();
    }

    public boolean isGroupExistsById(int id) {
        return findGroupById(id).isPresent();
    }
    public boolean isGroupExistsByName(String name) {
        return findGroupByName(name).isPresent();
    }

    // More detailed work with staff
    public Optional<UserObject> findCurator(int groupId) {
        return jooq.select(USERS.asterisk())
                .from(STUDENT_GROUP)
                .join(USERS).on(STUDENT_GROUP.CURATOR_ID.eq(USERS.ID))
                .where(STUDENT_GROUP.ID.eq(groupId))
                .fetchOptional(UserObject::new);
    }
    public Optional<UserObject> findHead(int groupId) {
        return jooq.select(USERS.asterisk())
                .from(STUDENT_GROUP)
                .join(USERS).on(STUDENT_GROUP.HEAD_ID.eq(USERS.ID))
                .where(STUDENT_GROUP.ID.eq(groupId))
                .fetchOptional(UserObject::new);
    }

    public int setCurator(int groupId, int curatorId) {
        return jooq.update(STUDENT_GROUP)
                .set(STUDENT_GROUP.CURATOR_ID, curatorId)
                .where(STUDENT_GROUP.ID.eq(groupId))
                .execute();
    }
    public int setHead(int groupId, int headId) {
        return jooq.update(STUDENT_GROUP)
                .set(STUDENT_GROUP.HEAD_ID, headId)
                .where(STUDENT_GROUP.ID.eq(groupId))
                .execute();
    }

    public int removeCurator(int groupId) {
        return jooq.update(STUDENT_GROUP)
                .setNull(STUDENT_GROUP.CURATOR_ID)
                .where(STUDENT_GROUP.ID.eq(groupId))
                .execute();
    }
    public int removeHead(int groupId) {
        return jooq.update(STUDENT_GROUP)
                .setNull(STUDENT_GROUP.HEAD_ID)
                .where(STUDENT_GROUP.ID.eq(groupId))
                .execute();
    }

    public Optional<Integer> findGroupIdIfHead(int userId) {
        return jooq.select(STUDENT_GROUP.ID)
                .from(STUDENT_GROUP)
                .where(STUDENT_GROUP.HEAD_ID.eq(userId))
                .fetchOptional(
                        record -> record.get(STUDENT_GROUP.ID)
                );
    }

    // Work with group members
    public List<UserObject> getGroupMembers(int groupId) {
        return jooq.select(USERS.asterisk()).from(STUDENT_GROUP)
                .leftJoin(USER_GROUP_RELATION).on(STUDENT_GROUP.ID.eq(USER_GROUP_RELATION.GROUP_ID))
                .leftJoin(USERS).on(USER_GROUP_RELATION.USER_ID.eq(USERS.ID))
                .where(STUDENT_GROUP.ID.eq(groupId))
                .fetch(UserObject::new);
    }

    public List<UserDataObject> getGroupMembersInfo(int groupId) {
        return jooq.select(USER_DATA.asterisk()).from(STUDENT_GROUP)
                .leftJoin(USER_GROUP_RELATION).on(STUDENT_GROUP.ID.eq(USER_GROUP_RELATION.GROUP_ID))
                .leftJoin(USER_DATA).on(USER_GROUP_RELATION.USER_ID.eq(USER_DATA.USER_ID))
                .where(STUDENT_GROUP.ID.eq(groupId))
                .fetch(UserDataObject::new);
    }

    public int addGroupMembers(int groupId, List<UserObject> members) {
        List<InsertSetMoreStep<UserGroupRelationRecord>> queries = members.stream()
                .map(member -> {
                    return jooq.insertInto(USER_GROUP_RELATION)
                            .set(USER_GROUP_RELATION.USER_ID, member.getId())
                            .set(USER_GROUP_RELATION.GROUP_ID, groupId);
                })
                .toList();
        return jooq.batch(queries).execute().length;
    }

    // TODO: Сделать чтобы удаляло только если пользователь в группе с переданным id
    // Пока работает только если у каждого студента одна группа
    public int removeGroupMembers(int groupId, List<Integer> memberIds) {
        return jooq.deleteFrom(USER_GROUP_RELATION)
                .where(USER_GROUP_RELATION.USER_ID.in(memberIds))
                .execute();
    }

    public int clearGroup(int groupId) {
        return jooq.delete(USER_GROUP_RELATION)
                .where(USER_GROUP_RELATION.GROUP_ID.eq(groupId))
                .execute();
    }

    public boolean isUserInGroup(int groupId, int userId) {
        Optional<GroupObject> optionalGroup = jooq.select(STUDENT_GROUP.asterisk()).from(STUDENT_GROUP)
                .leftJoin(USER_GROUP_RELATION).on(STUDENT_GROUP.ID.eq(USER_GROUP_RELATION.GROUP_ID))
                .leftJoin(USERS).on(USERS.ID.eq(USER_GROUP_RELATION.USER_ID))
                .where(USERS.ID.eq(userId))
                .and(STUDENT_GROUP.ID.eq(groupId))
                .fetchOptional(GroupObject::new);
        return optionalGroup.isPresent();
    }
}
