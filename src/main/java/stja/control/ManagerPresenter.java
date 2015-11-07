package stja.control;

import stja.data_access.security.PermissionDAO;
import stja.data_access.security.RoleDAO;
import stja.data_access.security.UserDAO;
import stja.entities.user.Permission;
import stja.entities.user.Role;
import stja.entities.user.User;

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

    public void changePassword(User user, String password) {
        userDAO.changePassword(user, password);
    }

    public void createRola(Role rola) {
        roleDAO.merge(rola);
    }

    public void createPermission(Permission permission) {
        permissionDAO.merge(permission);
    }

    public List<User> getUsers() {
        return userDAO.getAll();
    }

    public List<Role> getRoles() {
        return roleDAO.getAll();
    }

    public List<Permission> getPermissions() {
        return permissionDAO.getAll();
    }
}
