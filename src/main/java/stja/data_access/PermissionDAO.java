package stja.data_access;

import stja.entities.user.Permission;

import javax.ejb.Stateless;

/**
 * Created by Delth on 02.11.2015.
 */
@Stateless
public class PermissionDAO extends AbstractDAO<Permission> {
    @Override
    protected Class<Permission> getEntityClass() {
        return Permission.class;
    }
}
