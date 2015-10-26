package stja.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;
import stja.control.SearchTablePresenter;
import stja.data_access.TagRanking;
import stja.entities.video.Video;

/**
 * Created by Michal on 2015-10-17.
 */
public class SearchPanelAndTable extends Panel implements View {

    private SearchTablePresenter presenter;
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout crudLayout = new HorizontalLayout();
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private Button addTag;
    private FilterTable table;
    private Table table2;
    private BeanItemContainer<Video> container = new BeanItemContainer<Video>(Video.class);
    private BeanItemContainer<TagRanking> container2 = new BeanItemContainer<TagRanking>(TagRanking.class);

    public SearchPanelAndTable(SearchTablePresenter presenter) { //presenter should be an interface, but fuck it
        this.presenter = presenter;
        setCaption("VideoDatabase!");
        setSizeFull();
        initComponents();
        addListeners();
        setContent(mainLayout);
    }

    private void initComponents() {
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        addButton = new Button("Dodaj video");
        addButton.setImmediate(true);
        deleteButton = new Button("Usun video");
        deleteButton.setImmediate(true);
        editButton = new Button("Edytuj video");
        editButton.setImmediate(true);
        addTag = new Button("Dodaj Tag lub osobę");
        addTag.setImmediate(true);
        crudLayout.addComponent(addButton);
        crudLayout.addComponent(deleteButton);
        crudLayout.addComponent(editButton);
        crudLayout.addComponent(addTag);
        crudLayout.setSpacing(true);
        table = new FilterTable();
        table.setSizeFull();
        table.setFilterBarVisible(true);
        table.setContainerDataSource(container);
        table.setVisibleColumns("id","title", "description", "url", "tags", "video_date", "timestamp");
        table.setColumnHeaders(new String[] {"Id", "Tytuł", "Opis", "URL", "Tagi", "Data filmiku", "Data dodania"});
        table.setColumnCollapsingAllowed(true);
        table.setColumnCollapsed("id", true);
        table.setColumnCollapsed("timestamp", true);
        table.setImmediate(true);
        table.setSelectable(true);
        table.setMultiSelect(false);
        table.setSortEnabled(true);

        refreshTable();

        table2 = new Table();
        table2.setCaption("Najpopularniejsze tagi");
        table2.setContainerDataSource(container2);
        table2.setVisibleColumns("text", "countz");
        table2.setColumnHeaders(new String[] {"Tag", "Popularność"});
        table2.setImmediate(true);
        table2.setSelectable(false);

        refreshCount();
        mainLayout.addComponent(crudLayout);
        mainLayout.addComponent(table);
        mainLayout.addComponent(table2);
    }

    private void refreshCount() {
        container2.removeAllItems();
        container2.addAll(presenter.getRanking());
    }

    private void addListeners() {
        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                AddEditVideoWindow addWindow = new AddEditVideoWindow(presenter, new Video());
                addWindow.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        refreshTable();
                        refreshCount();
                    }
                });
                UI.getCurrent().addWindow(addWindow);
            }
        });
        deleteButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                Object object =  table.getValue();
                if (object != null){
                    Video video = (Video) object;
                    presenter.delete(video);
                    refreshTable();
                    Notification.show("Sukces", "Poprawnie usunieto", Notification.Type.TRAY_NOTIFICATION);
                }
                else{
                    Notification.show("WARNING", "Musisz najpierw wybrac video do usuniecia", Notification.Type.WARNING_MESSAGE);
                }
            }
        });
        editButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                Object object =  table.getValue();
                if (object != null){
                    try{
                        Video video = (Video) object;
                        AddEditVideoWindow addWindow = new AddEditVideoWindow(presenter, video);
                        addWindow.addCloseListener(new Window.CloseListener() {
                            @Override
                            public void windowClose(Window.CloseEvent e) {
                                refreshTable();
                                refreshCount();
                            }
                        });
                        UI.getCurrent().addWindow(addWindow);
                    }
                    catch(Throwable e){
                        Notification.show("WARNING", "Nope! " + e.getMessage() + " " + e.getCause().getMessage(), Notification.Type.WARNING_MESSAGE);
                    }
                }
                else{
                    Notification.show("WARNING", "Musisz najpierw wybrac video do edycji", Notification.Type.WARNING_MESSAGE);
                }
            }
        });
        addTag.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AddTagWindow addWindow = new AddTagWindow(presenter);
                addWindow.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        refreshCount();
                    }
                });
                UI.getCurrent().addWindow(addWindow);
            }
        });
    }

    private void refreshTable(){
        container.removeAllItems();
        container.addAll(presenter.getAllVideos());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
