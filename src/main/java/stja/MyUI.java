package stja;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.server.VaadinCDIServlet;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import stja.control.SearchTablePresenter;
import stja.ui.SearchPanelAndTable;

import javax.annotation.PostConstruct;
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
    private VerticalLayout mainLayout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout = new VerticalLayout();
        setContent(mainLayout);

        initComponents();
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
