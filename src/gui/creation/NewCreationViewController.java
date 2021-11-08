package gui.creation;

import gui.ControllerBuilder;
import gui.UIControllerLoadable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for controlling the New Creation View of JavaFX.
 */
public class NewCreationViewController implements UIControllerLoadable {

    private Stage stage;
    private ControllerBuilder builder;
    private String path;
    private String[] prompts;
    private List<Object[]> eventFields = new ArrayList<>();

    @FXML
    private Text creationTypeTitle;
    @FXML
    private VBox eventContainer;
    @FXML
    private TextField creationNameField;
    @FXML
    public Text creationNameTitle;

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * @param presenter is the presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     *
     */

    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.stage = stage;
        this.builder = builder;
    }

    // ============== Button Action =================

    /**
     * Action when the user click Add Event button.
     */
    @FXML
    private void onEventAdd(){
        eventContainer.getChildren().add(constructEventFields());
    }

    /**
     * Action when the user click Confirm button.
     * Switch to CreationView
     */
    @FXML
    private void onEventConfirm(){
        List<String> res = new ArrayList<>();
        res.add(creationNameField.getText());

        for (Object[] o : eventFields){
            String currEvent = "";

            currEvent += ((TextField) o[0]).getText() + ",";
            currEvent += ((TextField) o[1]).getText() + ",";
            currEvent += ((CheckBox) o[2]).isSelected() ? "true" : "false";
            currEvent += ",";

            if (path.equals("schedule placeholder")){
                currEvent += ((DatePicker) o[3]).getValue().toString();
            }
            else{
                currEvent += ((TextField) o[3]).getText();
            }
            res.add(currEvent);
        }

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
        controller.addCreationCallback(res.toArray(new String[0]), path);
        stage.show();

    }

    /**
     * Action when the user click Back button.
     * Switch to CreationView
     */
    @FXML void onBackButtonClicked(){
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

    // ============== Setters =================

    /**
     * Sets a new path for the Creation
     *
     * @param creationPath new path to be set for this Creation
     */
    public void setCreationpath(String creationPath){
        this.path = creationPath;
    }

    /**
     * Sets a new Type for the Creation
     *
     * @param creationType new creation type to be set for this Creation
     */
    public void setCreationType(String creationType){
        creationTypeTitle.setText(creationType + " creation");
    }

    /**
     * Sets new prompts
     * Set the creation name title be the first element of prompts
     * @param prompts new list of prompts to be set
     */
    public void setPrompts(String[] prompts){
        this.prompts = prompts;
        creationNameTitle.setText(prompts[0]);
    }

    /**
     * Construct the Event Fields of the New Creation View
     */
    private HBox constructEventFields(){
        TextField name = new TextField(prompts[1]);
        TextField description = new TextField(prompts[2]);
        CheckBox privacy = new CheckBox(prompts[3]);

        HBox res = new HBox();
        res.setSpacing(10);

        res.getChildren().add(name);
        res.getChildren().add(description);
        res.getChildren().add(privacy);

        if (path.equals("schedule placeholder")){
            Text desc = new Text(prompts[4]);
            DatePicker dp = new DatePicker();
            res.getChildren().add(dp);
            res.getChildren().add(desc);
            eventFields.add(new Object[]{name, description, privacy, dp});
        }

        else{
            TextField eventUnique = new TextField(prompts[4]);
            res.getChildren().add(eventUnique);
            eventFields.add(new Object[]{name, description, privacy, eventUnique});
        }

        return res;
    }
}
