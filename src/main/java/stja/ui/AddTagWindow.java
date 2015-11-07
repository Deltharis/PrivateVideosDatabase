package stja.ui;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import stja.control.SearchTablePresenter;
import stja.entities.tags.AbstractTag;
import stja.entities.tags.Person;
import stja.entities.tags.Tag;

/**
 * Created by Delth on 25.10.2015.
 */
public class AddTagWindow extends Window {

    private SearchTablePresenter presenter;

    public AddTagWindow(final SearchTablePresenter presenter){
        VerticalLayout mainLayout = new VerticalLayout();
        this.presenter = presenter;
        setModal(true);
        final FormLayout form = new FormLayout();
        TextField f = new TextField("Nazwa/Imie i nazwisko");
        f.addValidator(new StringLengthValidator("Pole nie moze byc puste!", 1, 25, false));
        form.addComponent(f, 0);
        final CheckBox checkBox = new CheckBox("Osoba");
        form.addComponent(checkBox, 1);
        setContent(mainLayout);
        final Button saveButton = new Button("Save", new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                try {
                    for (int i = 0; i < form.getComponentCount(); i++) {
                        try {
                            ((Field) form.getComponent(i)).validate();
                        } catch (Validator.InvalidValueException e) {
                            Notification
                                    .show("Commit failed: " + e.getCause().getMessage(),
                                            Notification.Type.ERROR_MESSAGE);
                        }
                    }
                    AbstractTag tag;
                    tag = checkBox.getValue() ? new Person() : new Tag();
                    presenter.saveTag(tag);
                    Notification.show("Changes committed!",
                            Notification.Type.TRAY_NOTIFICATION);
                    close();
                } catch (Exception e){
                    Notification
                            .show("Other weird error, saveTag failed " + e.getMessage(),
                                    Notification.Type.ERROR_MESSAGE);
                    close();
                }
            }
        });
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.addComponent(form);
        mainLayout.addComponent(saveButton);
    }
}
