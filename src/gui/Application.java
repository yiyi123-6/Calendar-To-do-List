package gui;

import gui.user_activity.StartViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the application.
 */
public class Application extends javafx.application.Application {

    private ControllerBuilder builder;

    /**
     * Load the JavaFX and connect to the StartView (in phase 2 resources)
     * Create the StartView.
     * @param stage is the JavaFX container
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/StartView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        StartViewController controller = fxmlLoader.getController();
        builder.ConfigureUIController(controller, stage);

        stage.setTitle("0052 Group");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initialize Controller.
     */
    @Override
    public void init(){
        try {
            builder = new ControllerBuilder();
            builder.getControllerInitializer().loadControllers();

        } catch (ClassNotFoundException e) {
            System.out.println("Save files could not be read");
            System.exit(-1);
        }
    }

    // application entry point
    public static void main(String[] args) {
        launch();
    }
}