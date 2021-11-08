package gui.creation;

import controller.UserActivityController;
import gui.ControllerBuilder;
import gui.MainMenuViewController;
import gui.UIControllerLoadable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import presenter.CreationPresenter;
import presenter.CreationView;

import java.io.IOException;
import java.util.*;

/**
 * Class responsible for controlling the Creation View of JavaFX.
 */
public class CreationViewController implements UIControllerLoadable, CreationView {

    private Stage stage;
    private ControllerBuilder builder;
    private CreationPresenter cp;
    private UUID viewer;

    /**
     * Sets a new stage for the Creation View
     *
     * @param stage new stage to be set for this Creation View
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ObservableList<Map<String, String>> ownCreations;

    private ObservableList<Map<String, String>> browseCreations;

    @FXML
    private TabPane tabPane;
    @FXML
    private Button addCreation;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;
    @FXML
    private Tab ownTab;
    @FXML
    private TableView<Map<String, String>> ownTableView;
    @FXML
    private TableColumn<Map, String> ownNameCol;
    @FXML
    private TableColumn<Map, String> ownTypeCol;
    @FXML
    private TableColumn<Map, String> ownAuthorCol;
    @FXML
    protected Tab browseTab;
    @FXML
    private TableView<Map<String, String>> browseTableView;
    @FXML
    private TableColumn<Map, String> browseNameCol;
    @FXML
    private TableColumn<Map, String> browseTypeCol;
    @FXML
    private TableColumn<Map, String> browseAuthorCol;


    // ============== Button Action =================

    /**
     * Action when the user click Delete button.
     * Delete the creation
     */
    @FXML
    void onClickDeleteButton() {

        ObservableList<Map<String, String>> sel;
        TableView<Map<String, String>> t;
        UUID creation;

        if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Owned")) {
            sel = ownCreations;
            t = ownTableView;
        }
        else{
            sel = browseCreations;
            t = browseTableView;
        }
        if (sel.size() > 0){
            String raw = sel.get(t.getSelectionModel().getSelectedIndex()).get("UUID");
            creation = UUID.fromString(raw);
            cp.deleteCreation(creation);
            sel.remove(t.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Action when the user click Add Creation button.
     * Add a creation
     */
    @FXML
    void onClickAddCreation(){

        Map<String, String> templates = cp.getTemplateSelection();
        String[] keyset = templates.keySet().toArray(new String[0]);

        ArrayList<String> names = new ArrayList<>();
        for (String s : keyset){
            names.add(templates.get(s));
        }

        String[] newNames = names.toArray(new String[0]);
        ChoiceDialog<String> d = new ChoiceDialog<>(newNames[0], newNames);
        d.setHeaderText("Select a template");
        Optional<String> selection = d.showAndWait();

        if (!selection.isPresent()){
            return;
        }
        int index = names.indexOf(selection.get());
        if (index < 0){
            return;
        }

        cp.showCreationFromTemplate(keyset[index]);
    }

    /**
     * Action when the user click Back button.
     * Switch to the MainMenuView
     */
    @FXML
    void onClickBackButton(){
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MainMenuView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        MainMenuViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        stage.show();
    }

    /**
     * Initialize all the buttons and columns of Creation View.
     *
     */
    @FXML
    void initialize(){
        assert addCreation != null : "fx:id=\"addTodo\" was not injected: check your FXML file 'MainView.fxml'.";
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert backButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert ownTab != null : "fx:id=\"scheduleTab\" was not injected: check your FXML file 'MainView.fxml'.";
        assert ownNameCol != null : "fx:id=\"scheduleModuleNameCol\" was not injected: check your FXML file 'MainView.fxml'.";
        assert ownTypeCol != null : "fx:id=\"scheduleNameCol\" was not injected: check your FXML file 'MainView.fxml'.";
        assert ownAuthorCol != null : "fx:id=\"scheduleNoteCol\" was not injected: check your FXML file 'MainView.fxml'.";
        assert browseTab != null : "fx:id=\"todoTab\" was not injected: check your FXML file 'MainView.fxml'.";
        assert browseNameCol != null : "fx:id=\"todoModuleNameCol\" was not injected: check your FXML file 'MainView.fxml'.";
        assert browseTypeCol != null : "fx:id=\"todoNameCol\" was not injected: check your FXML file 'MainView.fxml'.";
        assert browseAuthorCol != null : "fx:id=\"todoNoteCol\" was not injected: check your FXML file 'MainView.fxml'.";

        // set cell factories for accessing creation info
        ownNameCol.setCellValueFactory(new MapValueFactory<>("name"));
        ownTypeCol.setCellValueFactory(new MapValueFactory<>("type"));
        ownAuthorCol.setCellValueFactory(new MapValueFactory<>("author"));

        browseNameCol.setCellValueFactory(new MapValueFactory<>("name"));
        browseTypeCol.setCellValueFactory(new MapValueFactory<>("type"));
        browseAuthorCol.setCellValueFactory(new MapValueFactory<>("author"));

        // setting items
        ownCreations = FXCollections.observableArrayList();
        browseCreations = FXCollections.observableArrayList();

        ownTableView.setItems(ownCreations);
        browseTableView.setItems(browseCreations);

        // set click listeners
        setTabClick(ownCreations, ownTableView);
        setTabClick(browseCreations, browseTableView);
        setTabSwitch();
    }

    /**
     * Edit creation callback after collecting info.
     * @param creation unique id of creations.
     * @param addedEvents list of events need to be added.
     * @param editedCreationParams creation params that are edited.
     * @param editedEvents mapping from the unique id to events.
     */
    public void editCreationCallback(UUID creation, List<String[]> addedEvents,
                                     Map<String, String> editedCreationParams, Map<UUID, String> editedEvents){
        for (String[] s : addedEvents){
            cp.editCreation(creation, s[0], s[1]);
        }

        for (String s : editedCreationParams.keySet()){
            cp.editCreation(creation, s, editedCreationParams.get(s));
        }

        for (UUID id : editedEvents.keySet()){
            cp.editCreation(creation, editedEvents.get(id), id.toString());
        }
        cp.showCreationBrowser(viewer, true);
    }

    /**
     * Callback when a creation link is clicked.
     * @param creationID UUID of the creation
     */
    public void linkCreationCallback(UUID creationID){
        cp.showCreationBrowser(viewer, true);
        tabPane.getSelectionModel().select(browseTab);
        cp.showCreationBrowser(viewer, false);

        tabPane.getSelectionModel().select(ownTab);
        if (!selectCreationRow(ownCreations, ownTableView, creationID)){
            tabPane.getSelectionModel().select(browseTab);
            selectCreationRow(browseCreations, browseTableView, creationID);
        }
    }

    /**
     * Add the Creation
     * @param res list of creations.
     * @param path new path that associated with the creations.
     */

    public void addCreationCallback(String[] res, String path){
        cp.makeCreation(res, path, viewer);
        cp.showCreationBrowser(viewer, true);
    }

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * @param presenter creation presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.stage = stage;
        this.cp = (CreationPresenter) presenter;
        this.builder = builder;

        this.viewer = ((UserActivityController)builder.getControllers()[0]).getLoggedInUser();
        cp.showCreationBrowser(viewer, true);
    }

    // ============== Update View =================

    /**
     * Update the creation view by adding the information of creation.
     * Switch to SingleCreationView
     * @param params parameters of creation .
     * @param creationInfo information of the creation.
     * @param creationID unique id of the creation.
     */
    @Override
    public void updateCreationView(Map<String, String> params, Map<String, String> creationInfo, UUID creationID) {
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/SingleCreationView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        SingleCreationViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        controller.setCreationInfo(params, creationInfo, creationID);

        stage.show();
    }

    /**
     * Update the creation construction view by showing the alert.
     * @param response the response of failing to construct the creation.
     */
    @Override
    public void updateCreationConstructionView(String response) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(response);
        a.setTitle("Creation construction failed");
        a.showAndWait();
    }

    /**
     * Update the creation edit view by showing the alert.
     * @param response the response of failing to edit the creation.
     */
    @Override
    public void updateCreationEditView(String response) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(response);
        a.setTitle("Edit failed");
        a.showAndWait();
    }

    /**
     * Update the creation browser view by updating the table
     * @param creations the new creations need to be added to CreationBrowserView.
     */
    @Override
    public void updateCreationBrowserView(Map<UUID, String[]> creations) {
        ObservableList<Map<String, String>> sel;
        TableView<Map<String, String>> t;

        if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Owned")){
            sel = ownCreations;
            t = ownTableView;
        }
        else{
            sel = browseCreations;
            t = browseTableView;
        }
        sel.clear();
        for (UUID u : creations.keySet()){
            Map<String, String> res = new HashMap<>();
            res.put("name", creations.get(u)[0]);
            res.put("type", creations.get(u)[1]);
            res.put("author", creations.get(u)[2]);
            res.put("UUID", u.toString());

            sel.add(res);
        }
        t.setItems(sel);
    }

    /**
     * Show the creation from TemplateView
     * Switch to NewCreationView
     * @param response the response of the creation.
     * @param path of the creation
     */
    @Override
    public void showCreationFromTemplateView(String[] response, String path) {

        ArrayList<String> prompts = new ArrayList<>();

        for (int i = 1; i < response.length; i++){
            prompts.add(response[i]);
        }
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/NewCreationView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 830, 600));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        NewCreationViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);

        controller.setCreationType(response[0]);
        controller.setPrompts(prompts.toArray(new String[0]));
        controller.setCreationpath(path);

        stage.show();
    }

    // ================ Helper Methods ==============

    // set click listeners for row selection
    private void setTabClick(ObservableList<Map<String, String>> sel, TableView<Map<String, String>> t){
        t.setOnMouseClicked(action -> {
            if (action.getClickCount() == 2 && (t.getSelectionModel().getSelectedItem() != null) ) {
                String raw = sel.get(t.getSelectionModel().getSelectedIndex()).get("UUID");
                cp.showCreation(UUID.fromString(raw), viewer);
            }
            if (t.getSelectionModel().getSelectedItem() != null) {
                deleteButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
            }
        });
    }

    // set click listeners for tab switching
    private void setTabSwitch(){
        tabPane.setOnMouseClicked(event -> {
            if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Owned")){
                cp.showCreationBrowser(viewer, true);
            }
            else{
                cp.showCreationBrowser(viewer, false);
            }
        });
    }


    // select a row corresponding to creation, return true if selected
    private boolean selectCreationRow(ObservableList<Map<String, String>> s, TableView<Map<String, String>> t, UUID creationID){
        for (Map<String, String> m : s){
            if (UUID.fromString(m.get("UUID")).equals(creationID)){
                t.requestFocus();
                t.getSelectionModel().select(m);
                t.getFocusModel().focus(t.getSelectionModel().getSelectedIndex());
                return true;
            }
        }
        return false;
    }

}
