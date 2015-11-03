package stja;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.server.VaadinCDIServlet;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import stja.control.ManagerPresenter;
import stja.control.Permissions;
import stja.control.SearchTablePresenter;
import stja.ui.ErrorView;
import stja.ui.LoginView;
import stja.ui.ManagerView;
import stja.ui.SearchPanelAndTable;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

/**
 *
 */
@CDIUI("")
@Theme("mytheme")
@Widgetset("stja.MyAppWidgetset")
public class MyUI extends UI {

    public static final String MAINVIEW = "main";
    Navigator navigator;
    @Inject
    private SearchTablePresenter searchTablePresenter;
    @Inject
    private ManagerPresenter managerPresenter;
    private VerticalLayout mainLayout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout = new VerticalLayout();
        setContent(mainLayout);

        initComponents();
        navigator = new Navigator(this, this);

        navigator.setErrorView(new ErrorView());

        /*managerPresenter.createUser(
                "admin",
                "admin",
                Sets.newHashSet
                        (new Role
                                        ("UserManager",
                                                Sets.newHashSet(
                                                        new Permission(
                                                                Permissions.MANAGER
                                                        ))
                                        ),
                                new Role(
                                        "SiteAdmin", Sets.newHashSet(
                                        new Permission(
                                                Permissions.FILM_MANAGER
                                        ),
                                        new Permission(Permissions.USER)
                                ))
                        )
        );*/
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() || currentUser.isRemembered()) {
            if (currentUser.isPermitted(Permissions.MANAGER)) {
                navigator.addView("manager", new ManagerView(managerPresenter));
            }
            if (currentUser.isPermitted(Permissions.USER)) {
                navigator.addView("main", new SearchPanelAndTable(searchTablePresenter));
            }
            navigateToCorrectThing(currentUser);
        } else {
            navigator.addView("", new LoginView(searchTablePresenter, managerPresenter));
            navigator.navigateTo("");
        }
    }

    private void navigateToCorrectThing(Subject subject) {
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

    private void initComponents() {
        Panel panel = new SearchPanelAndTable(searchTablePresenter);
        mainLayout.addComponent(panel);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(widgetset = "stja.MyAppWidgetset", ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinCDIServlet {
    }
}
