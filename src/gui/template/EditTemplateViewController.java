package gui.template;

import gui.ControllerBuilder;
import gui.UIControllerLoadable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for controlling the Edit Template View of JavaFX.
 */
public class EditTemplateViewController implements UIControllerLoadable {

    private Stage stage;
    private ControllerBuilder builder;
    private String path;
    private String[] prompts;
    private List<TextField> textFields = new ArrayList<>();

    @FXML
    private VBox promptContainer;
    @FXML
    private Text templateTitle;

    // ============== Setters =================

    /**
     * Sets a new path for this EditTemplateView
     *
     * @param path new path to be set for this EditTemplateView
     */
    public void setPath(String path){
        this.path = path;
    }

    /**
     * Sets a new prompts and template title for this EditTemplateView
     *
     * @param prompts new prompts to be set for this EditTemplateView
     */

    public void setPrompts(String[] prompts){
        this.prompts = prompts;
        templateTitle.setText(prompts[0]);

        addTextFields();
    }

    // ============== Button Action =================

    /**
     * Action when the user click Back button.
     * Switch to TemplateView
     */
    public void onBack() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TemplateView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        TemplateViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        stage.show();
    }

    /**
     * Action when the user click Confirm button.
     * Switch to TemplateView
     */
    public void onConfirm() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TemplateView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        TemplateViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        controller.editCallback(retrievePromptAnswers(), path);

        stage.show();
    }

    /**
     * Initialize the presenter, stage and ControllerBuilder.
     * Create the StartView.
     * @param presenter presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     *
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.builder = builder;
        this.stage = stage;
    }

    /**
     * Create an add text fields
     */
    private void addTextFields(){
        for (int i = 1; i< prompts.length; i++){
            TextField curr = new TextField(prompts[i]);
            textFields.add(curr);
            promptContainer.getChildren().add(curr);
        }
    }

    /**
     * Get the answer of prompts
     * @return the substring of prompts
     */
    private String retrievePromptAnswers(){
        String res = "";

        for (TextField t : textFields){
            res += t.getText() + "\n";
        }

        return res.substring(0, res.length() - 1);
    }
}
