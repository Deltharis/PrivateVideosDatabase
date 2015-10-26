package stja.ui;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import stja.control.SearchTablePresenter;
import stja.entities.video.Tag;

import java.util.Date;

/**
 * Created by Delth on 25.10.2015.
 */
public class AddTagWindow extends Window {

    private SearchTablePresenter presenter;
    BeanItem<Tag> tag = new BeanItem<Tag>(new Tag());

    public AddTagWindow(final SearchTablePresenter presenter){
        VerticalLayout mainLayout = new VerticalLayout();
        this.presenter = presenter;
        setModal(true);
        final FieldGroup fieldGroup = new FieldGroup(tag);
        fieldGroup.setFieldFactory(new DefaultFieldGroupFieldFactory() {
            @Override
            public <T extends Field> T createField(final Class<?> type,
                                                   final Class<T> fieldType) {
                T field;
                if (Date.class == type) {
                    field = (T) new PopupDateField();
                }
                else {
                    field = super.createField(type, fieldType);
                }
                field.setWidth(100.0f, Unit.PERCENTAGE);
                return field;
            }
        });
        FormLayout form2 = new FormLayout();
        for (final Object propertyId : fieldGroup.getUnboundPropertyIds()) {
            if(propertyId.equals("text")){
                Field f = fieldGroup.buildAndBind(propertyId);
                f.addValidator(new StringLengthValidator("Pole nie moze byc puste!", 1, 25, false));
                form2.addComponent(f, 0);
            }
            else if(propertyId.equals("videos") || propertyId.equals("id")){
                continue;
            }
        }
        setContent(mainLayout);
        final Button saveButton = new Button("Save", new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                try {
                    fieldGroup.commit();
                    presenter.saveTag(tag.getBean());
                    Notification.show("Changes committed!",
                            Notification.Type.TRAY_NOTIFICATION);
                    close();
                } catch (final FieldGroup.CommitException e) {
                    Notification
                            .show("Commit failed: " + e.getCause().getMessage(),
                                    Notification.Type.ERROR_MESSAGE);
                    //normal error, don't close, fix
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
        mainLayout.addComponent(form2);
        mainLayout.addComponent(saveButton);
    }
}
