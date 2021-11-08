package gui;

import controller.CreationControllable;
import controller.EventController;
import gateway.CILoadable;
import gateway.ControllerInitializer;
import gui.creation.CreationViewController;
import gui.mail.MailViewController;
import gui.template.TemplateViewController;
import gui.user_activity.StartViewController;
import gui.user_activity.UserProfileController;
import javafx.stage.Stage;
import controller.UserMessageController;
import presenter.*;
import controller.TemplateController;
import controller.UserActivityController;

/**
 * Class responsible for building and initializing GUI controllers with the appropriate presenters.
 */
public class ControllerBuilder {

    private UserActivityController uac;
    private CreationControllable cc;
    private TemplateController tc;
    private UserMessageController umc;

    private ControllerInitializer ci;

    /**
     * Initializes UserActivityController, CreationControllable, TemplateController, UserMessageController and
     * ControllerInitializer.
     * @throws ClassNotFoundException Thrown if the save file can not be read
     */
    public ControllerBuilder() throws ClassNotFoundException {
        this.uac = new UserActivityController();
        this.cc = new EventController();
        this.tc = new TemplateController();
        this.umc = new UserMessageController();

        ci = new ControllerInitializer(new CILoadable[]{uac, (CILoadable) cc, tc, umc});
    }

    // ============== Getters =================

    /**
     * Gets a list of controllers to interact with the program
     * @return a list of controllers
     */
    public Object[] getControllers(){
        return new Object[]{uac, cc, tc, umc};
    }

    /**
     * Gets the controller initializer
     * @return controller initializer
     */
    public ControllerInitializer getControllerInitializer(){
        return ci;
    }

    /**
     * Load the GUI controller based on which controller it is
     *
     * @param controller is the UIControllerloadable
     * @param stage the JavaFX container
     */
    public void ConfigureUIController(UIControllerLoadable controller, Stage stage){
        if (controller instanceof StartViewController){
            UserActivityPresenter uap = new UserActivityPresenter((UserStartView) controller, constructEmptyProfileView(), uac);
            controller.loadController(uap, stage, this);
        }

        else if (controller instanceof UserProfileController){
            UserActivityPresenter uap = new UserActivityPresenter(constructEmptyStartView(), (UserProfileView) controller, uac);
            controller.loadController(uap, stage, this);
        }

        else if (controller instanceof CreationViewController){
            CreationPresenter cp = new CreationPresenter(cc, uac, tc, (CreationView) controller);
            controller.loadController(cp, stage, this);
        }

        else if (controller instanceof TemplateViewController){
            TemplatePresenter tp = new TemplatePresenter(tc, (TemplateView) controller);
            controller.loadController(tp, stage, this);
        }

        else if (controller instanceof MailViewController){
            MailPresenter mp = new MailPresenter(umc, (MailView) controller);
            controller.loadController(mp, stage, this);
        }

        else{
            controller.loadController(null, stage, this);
        }
    }

    /**
     * Construct an empty profile view
     */
    private UserProfileView constructEmptyProfileView(){
        return new UserProfileView() {
            @Override
            public void updateBanView(String response) {}

            @Override
            public void updatePasswordChangeView(String response) {}

            @Override
            public void updateProfileView(String[] response) {}
        };
    }

    /**
     * Construct an empty start view
     */
    private UserStartView constructEmptyStartView(){
        return new UserStartView() {
            @Override
            public void updateSignUpView(String response) {}

            @Override
            public void updateLoginView(String response) {}

            @Override
            public void updateRecoveryView(String response) {}
        };
    }
}
