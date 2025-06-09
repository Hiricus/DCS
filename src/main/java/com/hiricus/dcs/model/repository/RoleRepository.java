package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.user.RoleObject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hiricus.dcs.generated.public_.Tables.ROLES;
import static com.hiricus.dcs.generated.public_.Tables.USER_ROLE_RELATION;
import static com.hiricus.dcs.generated.public_.Tables.USERS;

@Repository
public class RoleRepository {
    private final DSLContext jooq;

    public RoleRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    public RoleObject mapToRoleObject(Record record) {
        RoleObject role = new RoleObject();
        role.setId(record.get(ROLES.ID));
        role.setRoleName(record.get(ROLES.ROLE_NAME));

        return role;
    }

    // CRUD methods
    public Optional<RoleObject> findRoleById(int id) {
        return jooq.select(ROLES.asterisk())
                .from(ROLES)
                .where(ROLES.ID.eq(id))
                .fetchOptional(this::mapToRoleObject);
    }
    public Optional<RoleObject> findRoleByName(String roleName) {
        return jooq.select(ROLES.asterisk())
                .from(ROLES)
                .where(ROLES.ROLE_NAME.eq(roleName))
                .fetchOptional(this::mapToRoleObject);
    }

    public List<RoleObject> findAll() {
        return jooq.select(ROLES.asterisk())
                .from(ROLES)
                .fetch(this::mapToRoleObject);
    }

    public Optional<Integer> createRole(RoleObject role) {
        return jooq.insertInto(ROLES)
                .set(ROLES.ROLE_NAME, role.getRoleName())
                .returningResult(ROLES.ID)
                .fetchOptional(ROLES.ID);
    }

    public int updateRole(RoleObject role) {
        return jooq.update(ROLES)
                .set(ROLES.ROLE_NAME, role.getRoleName())
                .where(ROLES.ID.eq(role.getId()))
                .execute();
    }

    public int deleteRoleById(int id) {
        return jooq.delete(ROLES)
                .where(ROLES.ID.eq(id))
                .execute();
    }

    public boolean isRoleExistsById(int id) {
        return findRoleById(id).isPresent();
    }

    // Work with users
    public List<RoleObject> getUsersRoles(int userId) {
        return jooq.select(ROLES.ID, ROLES.ROLE_NAME)
                .from(USERS)
                .leftJoin(USER_ROLE_RELATION).on(USERS.ID.eq(USER_ROLE_RELATION.USER_ID))
                .leftJoin(ROLES).on(USER_ROLE_RELATION.ROLE_ID.eq(ROLES.ID))
                .where(USERS.ID.eq(userId))
                .fetch(this::mapToRoleObject);
    }
    public boolean userHasRole(int userId, String role) {
        List<String> roleNames = getUsersRoles(userId).stream()
                .map(RoleObject::getRoleName)
                .toList();
        return roleNames.contains(role);
    }
    public int removeAllRolesFromUser(int userId) {
        return jooq.delete(USER_ROLE_RELATION)
                .where(USER_ROLE_RELATION.USER_ID.eq(userId))
                .execute();
    }
    public int addRoleToUser(int userId, int roleId) {
        return jooq.insertInto(USER_ROLE_RELATION)
                .set(USER_ROLE_RELATION.USER_ID, userId)
                .set(USER_ROLE_RELATION.ROLE_ID, roleId)
                .execute();
    }
}
