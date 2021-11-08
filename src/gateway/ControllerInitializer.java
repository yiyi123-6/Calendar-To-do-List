package gateway;

/**
 * Class responsible for configuring and saving the controllers of this system.
 */
public class ControllerInitializer {

    private CILoadable[] controllers;
    private ManagerGateway mg = new ManagerGateway();
    private Object[] managers;

    /**
     * Initializes a gateway.ControllerInitializer given an array of controllers.
     * @param controllers array containing controllers implementing CILoadable
     * @throws ClassNotFoundException Thrown if the save file can not be read
     */
    public ControllerInitializer(CILoadable[] controllers) throws ClassNotFoundException {
        this.controllers = controllers;
        managers = mg.configureManagers();
    }

    public void loadControllers() throws ClassNotFoundException {
        managers = mg.configureManagers();
        for (CILoadable l : controllers){
            l.load(managers);
        }
    }

    public void saveControllers(){
        mg.saveManagers(managers);
    }
}
