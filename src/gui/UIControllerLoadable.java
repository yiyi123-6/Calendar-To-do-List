package gui;

import javafx.stage.Stage;

/**
 * An Interface for which the class implements it can be loaded to interact with the backend.
 */

public interface UIControllerLoadable {

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * @param presenter presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */
    void loadController(Object presenter, Stage stage, ControllerBuilder builder);
}
