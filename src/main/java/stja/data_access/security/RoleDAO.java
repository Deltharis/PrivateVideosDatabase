package stja.data_access.security;

import stja.data_access.AbstractDAO;
import stja.entities.user.Role;

import javax.ejb.Stateless;

/**
 * Created by Delth on 02.11.2015.
 */
@Stateless
public class RoleDAO extends AbstractDAO<Role> {
    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }
}
