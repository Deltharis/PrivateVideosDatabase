package stja.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import stja.control.ManagerPresenter;
import stja.control.Permissions;
import stja.control.SearchTablePresenter;

public class LoginView extends VerticalLayout implements View {

    private FormLayout form = new FormLayout();

    private TextField username = new TextField("Username:");
    private PasswordField password = new PasswordField("Password:");
    private CheckBox checkbox = new CheckBox("Stay signed in");
    private Button button = new Button("Log in");
    private Label label = new Label("Log In:");

    private SearchTablePresenter searchTablePresenter;
    private ManagerPresenter managerPresenter;

    public LoginView() {
        initialize();
    }

    public LoginView(SearchTablePresenter searchTablePresenter, ManagerPresenter managerPresenter) {
        this();
        this.searchTablePresenter = searchTablePresenter;
        this.managerPresenter = managerPresenter;
    }

    public void initialize() {
        this.addComponent(form);
        form.addComponent(label);
        label.addStyleName("login-title");

        form.addComponent(username);
        form.addComponent(password);
        form.addComponent(checkbox);
        form.addComponent(button);

        form.setWidthUndefined();
        this.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
        this.setSizeFull();


        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Subject currentUser = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(
                        username.getValue(), password.getValue());

                token.setRememberMe(checkbox.getValue()); // if true user will not have to enter username/password in new browser session

                try {
                    currentUser.login(token); //tries to authenticate user
                    clear();

                    // if (currentUser.isAuthenticated())  - not necessary as try/catch statement will catch the unsuccessful login attempt

                    if (currentUser.isPermitted(Permissions.MANAGER)) { //authorization -- checking user permissions
                        getUI().getNavigator().addView("manager", new ManagerView(managerPresenter));
                    }
                    if (currentUser.isPermitted(Permissions.USER)) {
                        getUI().getNavigator().addView("main", new SearchPanelAndTable(searchTablePresenter));
                    }
                    navigateToCorrectThing(currentUser);
                } catch (Exception ex) { //if authentication is unsuccessful
                    clear();
                    ex.printStackTrace();
                    Notification.show("Login Error:", "Invalid username/password combination.", Type.ERROR_MESSAGE);
                }
            }

        });
    }

    private void navigateToCorrectThing(Subject subject) {
        Navigator navigator = getUI().getNavigator();
        if (subject.isPermitted(Permissions.MANAGER)) {
            navigator.navigateTo("manager");
        } else if (subject.isPermitted(Permissions.USER)) {
            navigator.navigateTo("main");
        } else if (subject.isPermitted(Permissions.FILM_MANAGER)) {
            throw new RuntimeException("WTF, not user but film manager");
        } else {
            throw new RuntimeException("NOPE, no permissions but logged in, bad user, bad " + subject.getPrincipal());
        }
    }

    public void clear() {
        username.clear();
        password.clear();
        checkbox.clear();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

}
