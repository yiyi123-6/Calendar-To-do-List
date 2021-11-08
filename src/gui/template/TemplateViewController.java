package gui.template;

import gui.ControllerBuilder;
import gui.MainMenuViewController;
import gui.UIControllerLoadable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import presenter.TemplatePresenter;
import presenter.TemplateView;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class responsible for controlling the TemplateView GUI
 */
public class TemplateViewController implements UIControllerLoadable, TemplateView {

    private ControllerBuilder builder;
    private Stage stage;
    private TemplatePresenter tp;

    private ObservableList<Map<String, String>> templates;
    @FXML
    private TableView<Map<String, String>> templateTableView;
    @FXML
    private TableColumn<Map, String> templateNameCol;
    @FXML
    private TableColumn<Map, String> templatePathCol;
    @FXML
    private Button editButton;
    @FXML
    private Button renameButton;

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * @param presenter template presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.builder = builder;
        this.stage = stage;
        this.tp = (TemplatePresenter) presenter;

        tp.showTemplateBrowser();
    }

    // ============== Update View =================

    /**
     * Update templates from the templateInfo Map
     * @param templateInfo a map stores the name and path of templates
     */
    @Override
    public void updateTemplateBrowser(Map<String, String> templateInfo) {
        templates.clear();

        for (String path : templateInfo.keySet()){
            Map<String, String> curr = new HashMap<>();
            curr.put("name", templateInfo.get(path));
            curr.put("path", path);
            templates.add(curr);
        }

        templateTableView.setItems(templates);
    }

    /**
     * Update the template view by presenting the response.
     * @param response .
     */
    @Override
    public void updateTemplateView(String[] response) {
        System.out.println(Arrays.toString(response));
    }

    /**
     * Show a confirmation after editing the template
     * @param response of template
     */
    @Override
    public void updateTemplateEditor(String response) {

        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(response);
        a.setTitle("Template Editing");

        if (response.equals("Template successfully edited")){
            a.setAlertType(Alert.AlertType.CONFIRMATION);
        }

        a.showAndWait();
    }

    protected void editCallback(String newPrompts, String path){
        tp.editTemplate(path, newPrompts, "2");
    }

    // ============== Button Action =================

    /**
     * Action when the user click back button.
     * Switch to the MainMenuView
     */
    public void onBackPress() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MainMenuView.fxml")
        );
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
     * Action when the user click Edit button.
     * Switch to the EditTemplateView
     */
    public void onEditPress() {
        if (templateTableView.getSelectionModel().getSelectedItem() == null){
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/EditTemplateView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        EditTemplateViewController controller = loader.getController();


        String path = templates.get(templateTableView.getSelectionModel().getSelectedIndex()).get("path");
        String[] prompts = tp.getInfo(path);
        controller.setPath(path);
        controller.setPrompts(prompts);

        builder.ConfigureUIController(controller, stage);
        stage.show();
    }

    /**
     * Action when the user click Rename button.
     * @param actionEvent of the TemplateView
     */

    public void onRenamePress(ActionEvent actionEvent) {
        if (templateTableView.getSelectionModel().getSelectedItem() == null){
            return;
        }

        TextInputDialog td = new TextInputDialog("");
        td.setHeaderText("Enter the new template name");
        td.setTitle("Rename template");
        Optional<String> res = td.showAndWait();

        if (res.isPresent()){
            String path = templates.get(templateTableView.getSelectionModel().getSelectedIndex()).get("path");
            tp.editTemplate(path, res.get(), "1");
        }

        tp.showTemplateBrowser();
    }

    // ============== Initializer =================

    /**
     * Initialize all the template and template table.
     */
    @FXML
    void initialize(){
        templateNameCol.setCellValueFactory(new MapValueFactory<>("name"));
        templatePathCol.setCellValueFactory(new MapValueFactory<>("path"));

        templates = FXCollections.observableArrayList();

        templateTableView.setOnMouseClicked(action -> {
            // If a row is selected, enable the edit and delete button
            if (action.getClickCount() == 2 && (templateTableView.getSelectionModel().getSelectedItem() != null) ) {
                String raw = templates.get(templateTableView.getSelectionModel().getSelectedIndex()).get("path");
                tp.showTemplate(raw);
            }

            if (templateTableView.getSelectionModel().getSelectedItem() != null) {
                editButton.setDisable(false);
                renameButton.setDisable(false);
            } else {
                editButton.setDisable(true);
                renameButton.setDisable(true);
            }
        });
    }
}
