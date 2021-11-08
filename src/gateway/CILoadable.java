package gateway;

/**
 * An Interface where classes implementing it can be loaded with managers loaded from serialization files.
 */

public interface CILoadable {

    /**
     * Loads this CILoadable with loaded managers
     * @param params Array of managers
     */
    void load(Object[] params);
}
