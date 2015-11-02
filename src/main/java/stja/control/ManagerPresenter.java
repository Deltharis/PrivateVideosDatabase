package stja.control;

import stja.data_access.PermissionDAO;
import stja.data_access.RoleDAO;
import stja.data_access.UserDAO;
import stja.entities.user.Permission;
import stja.entities.user.Role;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Created by Delth on 02.11.2015.
 */
@Stateless
public class ManagerPresenter {

    @Inject
    private UserDAO userDAO;

    @Inject
    private RoleDAO roleDAO;

    @Inject
    private PermissionDAO permissionDAO;


    public void createUser(String user, String password, Set<Role> roles) {
        userDAO.registerUser(user, password, roles);
    }

    public void createRola(Role rola) {
        roleDAO.persist(rola);
    }

    public void createPermission(Permission permission) {
        permissionDAO.persist(permission);
    }

    public List<Role> getRoles() {
        return roleDAO.getAll();
    }

    public List<Permission> getPermissions() {
        return permissionDAO.getAll();
    }
}
