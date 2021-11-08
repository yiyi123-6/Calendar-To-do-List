package presenter;

import controller.CreationControllable;
import controller.TemplateController;
import controller.UserActivityController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class responsible for presenting creation info
 */
public class CreationPresenter {

    private CreationView view;
    private CreationControllable cc;
    private UserActivityController uac;
    private TemplateController tc;

    /**
     * Initializes this creation presenter
     */
    public CreationPresenter(CreationControllable cc, UserActivityController uac, TemplateController tc, CreationView view){
        this.view = view;
        this.cc = cc;
        this.tc = tc;
        this.uac = uac;
    }

    /**
     * Makes a creation given an array of parameters, path and the creator UUID, and updates the corresponding view
     * @param params array containing creation info
     * @param path path to template used to make creation
     * @param creator UUID of the creator
     */
    public void makeCreation(String[] params, String path, UUID creator){
        try {
            cc.constructCreation(params, path, creator);
        } catch (ParseException e) {
            view.updateCreationConstructionView(e.getMessage());
        } catch (Exception e) {
            view.updateCreationConstructionView("Creation could not be made");
        }
    }

    /**
     * Edits the creation and updates its corresponding view
     * @param creationID UUID of the creation
     * @param action action user selected
     * @param input user input to the action
     */
    public void editCreation(UUID creationID, String action, String input){
        try {
            cc.editCreation(creationID, action, input);
        } catch (ParseException e) {
            view.updateCreationEditView(e.getMessage());
        } catch (Exception e) {
            view.updateCreationEditView("Creation could not be edited");
        }
    }

    /**
     * Show a singular creation
     * @param creationID UUID of the creation
     * @param viewer UUID of the viewer
     */
    public void showCreation(UUID creationID, UUID viewer){

        Map<String, String> res = new HashMap<>();

        String[] creationParams = cc.viewCreationSummary(creationID, viewer);
        Map<String, String> containedInfo = cc.viewCreation(creationID, viewer);

        res.put("name", creationParams[0]);
        res.put("type", creationParams[1]);
        res.put("author", creationParams[2]);

        view.updateCreationView(res, containedInfo, creationID);

    }

    /**
     * Collect info of all creations and display it to a browser view
     * @param viewer viewer's UUID
     * @param viewSelf true if the user is viewing their own creation
     */
    public void showCreationBrowser(UUID viewer, boolean viewSelf){
        Map<UUID, String[]> res = new HashMap<>();
        UUID[] creations;
        if (viewSelf){
            creations = cc.getOwnCreations(viewer);
        }
        else{
            creations = cc.getBrowsableCreations(viewer);
        }

        for (UUID cid : creations){
            res.put(cid, cc.viewCreationSummary(cid, viewer));
        }
        view.updateCreationBrowserView(res);
    }

    /**
     * get a Map of templates to be used to make a creation
     * @return Mapping of template paths to template info
     */
    public Map<String, String> getTemplateSelection(){
        return cc.getTemplateSelection();
    }

    /**
     * Shows the template prompts for a creation
     * @param path path to the template
     */
    public void showCreationFromTemplate(String path){
        view.showCreationFromTemplateView(tc.getTemplateInfo(path), path);
    }

    /**
     * Deletes the creation and notifies the view on the status
     * @param creation UUID of the creation to be deleted
     */
    public void deleteCreation(UUID creation){
        cc.deleteCreation(creation);
    }
}
