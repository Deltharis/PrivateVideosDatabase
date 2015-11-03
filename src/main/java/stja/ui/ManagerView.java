package stja.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.shiro.SecurityUtils;
import stja.control.ManagerPresenter;
import stja.entities.user.Permission;
import stja.entities.user.Role;
import stja.entities.user.User;

import java.util.Set;

/**
 * Created by Delth on 02.11.2015.
 */
public class ManagerView extends VerticalLayout implements View {

    private ManagerPresenter presenter;
    private BeanItemContainer<Permission> permissionBeanItemContainer = new BeanItemContainer<Permission>(Permission.class);
    private BeanItemContainer<Role> roleBeanItemContainer = new BeanItemContainer<Role>(Role.class);
    private BeanItemContainer<User> userBeanItemContainer = new BeanItemContainer<User>(User.class);

    public ManagerView(final ManagerPresenter presenter) { //presenter should be an interface, but fuck it
        this.presenter = presenter;
        final Table table2 = new Table();
        table2.setCaption("Users");
        table2.setContainerDataSource(userBeanItemContainer);
        table2.setVisibleColumns(new String[]{"id", "username", "password", "salt", "roles"});
        table2.setImmediate(true);
        table2.setSelectable(true);
        table2.setSizeFull();
        final TextField newPassword = new TextField("New password");
        Button changePassword = new Button("Change password");
        changePassword.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String password = newPassword.getValue();
                if (password.trim().equals("")) {
                    Notification.show("Nope", "No empty passwords", Notification.Type.ERROR_MESSAGE);
                }
                User u = (User) table2.getValue();
                if (u == null) {
                    Notification.show("Nope", "Select something", Notification.Type.ERROR_MESSAGE);
                }
                presenter.changePassword(u, password);
            }
        });

        setCaption("Manager uzytkownika");
        setSpacing(true);
        setMargin(true);

        addComponent(newPassword);
        addComponent(changePassword);
        addComponent(table2);
        addComponent(new Label("<hr />", ContentMode.HTML));

        final TextField username = new TextField("Username:");
        final TextField password = new TextField("Password:");
        final ListSelect roles = new ListSelect("Roles", roleBeanItemContainer);
        roles.setMultiSelect(true);
        Button createUser = new Button("Create user");
        createUser.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                presenter.createUser(username.getValue(), password.getValue(), (Set<Role>) roles.getValue());
            }
        });

        addComponent(username);
        addComponent(password);
        addComponent(createUser);
        addComponent(roles);
        addComponent(new Label("<hr />", ContentMode.HTML));

        final TextField rola = new TextField("Role:");
        Button addRola = new Button("Create Role");
        final ListSelect permissions = new ListSelect("Permissions", permissionBeanItemContainer);
        permissions.setMultiSelect(true);
        addRola.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                presenter.createRola(new Role(rola.getValue(), (Set<Permission>) permissions.getValue()));
                refreshList();
            }
        });

        addComponent(rola);
        addComponent(addRola);
        addComponent(permissions);
        addComponent(new Label("<hr />", ContentMode.HTML));

        final TextField permission = new TextField("Permission:");
        Button addPermission = new Button("Create permission");
        addPermission.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                presenter.createPermission(new Permission(permission.getValue()));
                refreshList();
            }
        });
        addComponent(permission);
        addComponent(addPermission);
        addComponent(new Label("<hr />", ContentMode.HTML));
        Button navigator = new Button("Go to main");
        navigator.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getNavigator().navigateTo("main");
            }
        });
        addComponent(navigator);
        Button logout = new Button("Logout");
        logout.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                SecurityUtils.getSubject().logout();
                getUI().getNavigator().navigateTo("");
            }
        });
        addComponent(logout);
    }

    private void refreshList() {
        permissionBeanItemContainer.removeAllItems();
        permissionBeanItemContainer.addAll(presenter.getPermissions());
        roleBeanItemContainer.removeAllItems();
        roleBeanItemContainer.addAll(presenter.getRoles());
        userBeanItemContainer.removeAllItems();
        userBeanItemContainer.addAll(presenter.getUsers());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshList();
    }
}
