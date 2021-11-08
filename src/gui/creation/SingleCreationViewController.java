package gui.creation;

import gui.ControllerBuilder;
import gui.UIControllerLoadable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.TemplateController;

import java.io.IOException;
import java.util.*;

/**
 * Class responsible for controlling the creation view
 */
public class SingleCreationViewController implements UIControllerLoadable {

    private Stage stage;
    private ControllerBuilder builder;
    private UUID creationID;
    private Map<Button, UUID> eventSelectorMap = new HashMap<>();

    // edited event : action
    private Map<UUID, String> editedEvents = new HashMap<>();

    // action : input
    private Map<String, String> editedCreationParams = new HashMap<>();
    //[[type of creation, response]]
    private List<String[]> addedEvents = new ArrayList<>();

    @FXML
    private Text creationAuthor;
    @FXML
    private Text creationType;
    @FXML
    private Text creationTitle;
    @FXML
    private VBox eventContainer;

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * @param presenter presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.stage = stage;
        this.builder = builder;
    }

    /**
     * Set the creation information from params to info.
     * @param params creation parameters of creation.
     * @param info information of creation that needs to be set.
     * @param cid unique id of creation.
     *
     */
    public void setCreationInfo(Map<String, String> params,Map<String, String> info, UUID cid){
        creationID = cid;
        creationAuthor.setText(params.get("author"));
        creationType.setText(params.get("type"));
        creationTitle.setText(params.get("name"));

        // display contained events as buttons
        for (String s : info.keySet()){
            eventContainer.getChildren().add(constructEventEntry(info.get(s), s));
        }
    }

    // ============== Button Action =================

    /**
     * Action when the user click Back button.
     * Switch to CreationView
     * @param actionEvent action event for SingleCreationView
     */
    @FXML
    private void onBack(ActionEvent actionEvent) {
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/CreationView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (IOException e) {
            return;
        }

        CreationViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);

        stage.show();
    }

    /**
     * Action when the user click Rename button.
     * Create a view of rename view
     * @param actionEvent action event for SingleCreationView
     */
    @FXML
    private void onRenameClick(ActionEvent actionEvent) {
        TextInputDialog td = new TextInputDialog("");
        td.setHeaderText("Enter new name of Creation");
        td.setTitle("Rename");
        Optional<String> res = td.showAndWait();

        if (res.isPresent()){
            creationTitle.setText(res.get());
            editedCreationParams.put("4", res.get());
        }
    }

    /**
     * Action when the user click Privacy button.
     * Create a view of select privacy
     * @param actionEvent action event for SingleCreationView
     */
    @FXML
    private void onPrivacyClick(ActionEvent actionEvent) {

        ButtonType privButton = new ButtonType("Private");
        ButtonType pubButton = new ButtonType("Public");
        Alert a = new Alert(Alert.AlertType.NONE, "Select privacy of creation", privButton, pubButton);
        a.setTitle("Privacy selection");

        a.showAndWait().ifPresent(response -> {
            if (response == privButton) {
                editedCreationParams.put("5", "true");
            } else if (response == pubButton) {
                editedCreationParams.put("5", "false");
            }
        });
    }

    /**
     * Action when the user click Add button.
     * Create a view of adding TodoList, Schedule or Tagged
     * @param actionEvent action event for SingleCreationView
     */
    @FXML
    private void onAddClick(ActionEvent actionEvent) {
        TemplateController tc = (TemplateController) builder.getControllers()[2];
        String path;
        if (creationType.getText().equals("Todo")){
            path = "todo placeholder";
        }
        else if (creationType.getText().equals("Schedule")){
            path = "schedule placeholder";
        }
        else if (creationType.getText().equals("Tagged")){
            path = "tagged placeholder";
        }
        else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Creation type not recognized");
            return;
        }
        String response = collectResponses(tc.getTemplateInfo(path));
        addedEvents.add(new String[]{creationType.getText(), response});
        eventContainer.getChildren().add(constructAddedEventEntry(response));
    }

    /**
     * Action when the user click Confirm button.
     * Switch to CreationView
     * @param actionEvent action event for SingleCreationView
     */
    @FXML
    private void onConfirmClick(ActionEvent actionEvent) {
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/CreationView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (IOException e) {
            return;
        }

        CreationViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        controller.editCreationCallback(creationID, addedEvents, editedCreationParams, editedEvents);
        stage.show();
    }

    // ============ Helper methods ============

    private String collectResponses(String[] prompts){
        String responses = "";

        for (int i = 2; i < prompts.length ; i++){
            TextInputDialog td = new TextInputDialog("");
            td.setHeaderText(prompts[i]);
            td.setTitle(prompts[0]);
            Optional<String> res = td.showAndWait();
            responses += res.get() + ",";

        }

        return responses.substring(0, responses.length() - 1);
    }

    private Button constructAddedEventEntry(String response){
        Button added = new Button("(To be added)" + response);
        added.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAddedEvent(response);
                eventContainer.getChildren().remove(added);
            }
        });
        return added;
    }

    private void removeAddedEvent(String response){
        // 1 = response
        for (String[] s : addedEvents){
            if (s[1].equals(response)){
                addedEvents.remove(s);
                return;
            }
        }
    }

    private Button constructEventEntry(String eventInfo, String eventID){
        Button eventSelector = new Button(eventInfo);
        eventSelectorMap.put(eventSelector, UUID.fromString(eventID));
        eventSelector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UUID eventID = eventSelectorMap.get(eventSelector);

                editedEvents.put(eventID, "3");
                eventContainer.getChildren().remove(eventSelector);
            }
        });

        return eventSelector;
    }
}
