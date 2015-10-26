package stja.ui;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import stja.control.SearchTablePresenter;
import stja.entities.video.Tag;
import stja.entities.video.Video;

import java.util.Date;

/**
 * Created by Delth on 24.10.2015.
 */
public class AddEditVideoWindow extends Window {

    private SearchTablePresenter presenter;
    BeanItem<Video> video;

    public AddEditVideoWindow(final SearchTablePresenter presenter, Video vid){
        VerticalLayout mainLayout = new VerticalLayout();
        this.presenter = presenter;
        setModal(true);
        video = new BeanItem<>(vid);
        final FieldGroup fieldGroup = new FieldGroup(video);
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
        TwinColSelect mFeaturesTwinColSelect = new TwinColSelect();
        for(Tag tag : presenter.getAllTags()){
            mFeaturesTwinColSelect.addItem(tag);
        }

        mFeaturesTwinColSelect.setImmediate(true);
        mFeaturesTwinColSelect.setNullSelectionAllowed(true);
        mFeaturesTwinColSelect.setMultiSelect(true);
        mFeaturesTwinColSelect.setLeftColumnCaption("Available");
        mFeaturesTwinColSelect.setRightColumnCaption("Selected");
        fieldGroup.bind(mFeaturesTwinColSelect,"tags");
        for (final Object propertyId : fieldGroup.getUnboundPropertyIds()) {
            if(propertyId.equals("url")){
                Field f = fieldGroup.buildAndBind(propertyId);
                f.addValidator(new StringLengthValidator("Pole nie moze byc puste!", 1, 200, false));
                form2.addComponent(f, 1);
            }
            else if(propertyId.equals("title")){
                Field f = fieldGroup.buildAndBind(propertyId);
                f.addValidator(new StringLengthValidator("Pole nie moze byc puste!", 1, 50, false));
                form2.addComponent(f, 0);
            }
            else if(propertyId.equals("tags")){
                continue;
            }
            else if(!propertyId.equals("id") && !propertyId.equals("timestamp")) {
                form2.addComponent(fieldGroup.buildAndBind(propertyId));
            }
        }
        form2.addComponent(mFeaturesTwinColSelect);
        setContent(mainLayout);
        final Button saveButton = new Button("Save", new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                try {
                    fieldGroup.commit();
                    presenter.saveVideo(video.getBean());
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
                            .show("Other weird error, saveVideo failed " + e.getMessage(),
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
