package stja.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.tepi.filtertable.FilterTable;
import stja.control.Permissions;
import stja.control.SearchTablePresenter;
import stja.data_access.tags.TagRanking;
import stja.entities.video.Video;

/**
 * Created by Delth on 17.10.2015.
 */
public class SearchPanelAndTable extends Panel implements View {

    private SearchTablePresenter presenter;
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout crudLayout = new HorizontalLayout();
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private Button addTag;
    private Button deleteTags;
    private FilterTable table;
    private Table tagRankingTable;
    private Table personRankingTable;
    private BeanItemContainer<Video> videoItemContainer = new BeanItemContainer<Video>(Video.class);
    private BeanItemContainer<TagRanking> tagRankingItemContainer = new BeanItemContainer<TagRanking>(TagRanking.class);
    private BeanItemContainer<TagRanking> personRankingItemContainer = new BeanItemContainer<TagRanking>(TagRanking.class);

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
        Button logoutButton = new Button("Log out");
        logoutButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                SecurityUtils.getSubject().logout();
                getUI().getNavigator().navigateTo("");
            }
        });
        crudLayout.addComponent(addButton);
        crudLayout.addComponent(deleteButton);
        crudLayout.addComponent(editButton);
        crudLayout.addComponent(addTag);
        crudLayout.addComponent(logoutButton);
        deleteTags = new Button("Delete tags");
        crudLayout.addComponent(deleteTags);
        deleteTags.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                presenter.deleteTags();
                refreshCount();
                refreshTable();
            }
        });
        deleteTags.setVisible(false);
        crudLayout.setSpacing(true);
        table = new FilterTable();
        table.setSizeFull();
        table.setFilterBarVisible(true);
        table.setContainerDataSource(videoItemContainer);
        table.addGeneratedColumn("UrlButton", new CustomTable.ColumnGenerator() {
            @Override
            public Object generateCell(CustomTable customTable, Object itemId, Object columnId) {
                Video video = (Video) itemId;
                final Button b = new Button(video.getUrl());
                b.setStyleName(BaseTheme.BUTTON_LINK);
                b.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        getUI().getPage().open(b.getCaption(), "_blank");
                    }
                });
                return b;
            }
        });
        table.setVisibleColumns("id", "title", "description", "UrlButton", "tags", "people", "video_date", "timestamp", "core", "url");
        table.setColumnHeaders(new String[]{"Id", "Tytuł", "Opis", "URL", "Tagi", "Tancerze", "Data filmiku", "Data dodania", "Repertuar", "URLtext"});
        table.setColumnCollapsingAllowed(true);
        table.setColumnCollapsed("id", true);
        table.setColumnCollapsed("timestamp", true);
        table.setColumnCollapsed("core", true);
        table.setColumnCollapsed("url", true);

        table.setImmediate(true);
        table.setSelectable(true);
        table.setMultiSelect(false);
        table.setSortEnabled(true);
        table.setCellStyleGenerator(new CustomTable.CellStyleGenerator() {
            @Override
            public String getStyle(CustomTable customTable, Object itemId, Object propertyId) {
                // Row style setting, not relevant in this example.
                if (propertyId == null) {
                    Video video = (Video) itemId;
                    if (video.isCore()) {
                        return "highlight";
                    } else {
                        return null;
                    }
                } else return null;
            }
        });

        refreshTable();

        tagRankingTable = new Table();
        tagRankingTable.setCaption("Najpopularniejsze tagi");
        tagRankingTable.setContainerDataSource(tagRankingItemContainer);
        tagRankingTable.setVisibleColumns("text", "countz");
        tagRankingTable.setColumnHeaders(new String[]{"Tag", "Popularność"});
        tagRankingTable.setImmediate(true);
        tagRankingTable.setSelectable(false);

        personRankingTable = new Table();
        personRankingTable.setCaption("Najaktywniejsi tancerze");
        personRankingTable.setContainerDataSource(personRankingItemContainer);
        personRankingTable.setVisibleColumns("text", "countz");
        personRankingTable.setColumnHeaders(new String[]{"Osoba", "Aktywność"});
        personRankingTable.setImmediate(true);
        personRankingTable.setSelectable(false);

        refreshCount();
        mainLayout.addComponent(crudLayout);
        mainLayout.addComponent(table);
        HorizontalLayout rankings = new HorizontalLayout();
        rankings.setSpacing(true);
        rankings.addComponent(tagRankingTable);
        rankings.addComponent(personRankingTable);
        mainLayout.addComponent(rankings);
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
                        e.printStackTrace();
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
        videoItemContainer.removeAllItems();
        videoItemContainer.addAll(presenter.getAllVideos());
    }

    private void refreshCount() {
        tagRankingItemContainer.removeAllItems();
        tagRankingItemContainer.addAll(presenter.getTagRanking());
        personRankingItemContainer.removeAllItems();
        personRankingItemContainer.addAll(presenter.getPersonRanking());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isPermitted(Permissions.MANAGER)) {
            deleteTags.setVisible(true);
        } else {
            deleteTags.setVisible(false);
        }
        if (currentUser.isPermitted(Permissions.FILM_MANAGER)) {
            addButton.setVisible(true);
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            addTag.setVisible(true);
        } else {
            addButton.setVisible(false);
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            addTag.setVisible(false);
        }
        refreshTable();
        refreshCount();

    }
}
