/**
 * Package containing classes that help to read/write entities to files.
 */
package gateway;

import use_case.EventContainerManager;
import use_case.EventManager;
import use_case.MessageManager;
import use_case.TemplateManager;
import use_case.UserManager;

import java.io.*;

/**
 * Gateway class responsible for configuring all managers in this program.
 */
public class ManagerGateway implements Serializable{

    private String umSerPath = "phase2/serialized_um.ser";
    private String ecmSerPath = "phase2/serialized_ecm.ser";
    private String emSerPath = "phase2/serialized_em.ser";
    private String tmSerPath = "phase2/serialized_tm.ser";
    private String mmSerPath = "phase2/serialized_mm.ser";

    /**
     * Save all managers, in the order of, UserManager, EventContainerManager, EventManager, and TemplateManager,
     * to some .ser files.
     * @param managers array containing the managers to be saved.
     */
    public void saveManagers(Object[] managers) {
        saveToFile(managers[0], this.umSerPath);
        saveToFile(managers[1], this.ecmSerPath);
        saveToFile(managers[2], this.emSerPath);
        saveToFile(managers[3], this.tmSerPath);
        saveToFile(managers[4], this.mmSerPath);
    }

    /**
     * Return an array of managers, in the order of, UserManager, EventContainerManager, EventManager, and TemplateManager,
     * read from some .ser files. Constructs empty empty managers if .ser files are empty.
     *
     * @return An array of managers in order of, UserManager, EventContainerManager, EventManager, and TemplateManager.
     * @throws ClassNotFoundException when the serialization files could not be read.
     */
    public Object[] configureManagers() throws ClassNotFoundException{
        Object[] managers = new Object[5];
        managers[0] = readFromFile(umSerPath);
        managers[1] = readFromFile(ecmSerPath);
        managers[2] = readFromFile(emSerPath);
        managers[3] = readFromFile(tmSerPath);
        managers[4] = readFromFile(mmSerPath);

        if (managers[0] == null){
            managers[0] = new UserManager();
        } if (managers[1] == null) {
            managers[1] = new EventContainerManager();
        } if (managers[2] == null) {
            managers[2] = new EventManager();
        } if (managers[3] == null) {
            TemplateManager tm = new TemplateManager();
            managers[3] = tm;
        }if (managers[4] == null) {
            MessageManager mm = new MessageManager();
            managers[4] = mm;
        }
        return managers;
    }

    // =============== Helper methods ================
    private void saveToFile(Object o, String filePath) throws ClassCastException{
        try {
            OutputStream file = new FileOutputStream(filePath);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            // Serialize the object
            output.writeObject(o);
            output.close();
            file.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param path path of the file to be read
     * @return object that deserialized from the file, or null if file is empty or there is IOException caught
     * @throws ClassNotFoundException
     */
    private Object readFromFile(String path) throws ClassNotFoundException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            File file1 = new File(path);
            if (br.readLine() == null && file1.length() == 0) {
                return null;
            }

            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // Deserialize the object
            return input.readObject();
        } catch (IOException e) {
            // file can't be read
            return null;
        }
    }

}
