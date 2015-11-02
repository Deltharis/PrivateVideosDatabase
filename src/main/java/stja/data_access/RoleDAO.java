package stja.data_access;

import stja.entities.user.Role;

/**
 * Created by Delth on 02.11.2015.
 */
public class RoleDAO extends AbstractDAO<Role> {
    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }
}
