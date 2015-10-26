package stja.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by Delth on 26.10.2015.
 */
public class ErrorView extends VerticalLayout implements View {

    private Label label = new Label("Error 404 PAGE NOT FOUND");

    public ErrorView () {
        this.addComponent(label);
        //Page.getCurrent().reload();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

}
