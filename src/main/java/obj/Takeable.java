package obj;

/**
 * Definition of methods which should have every game object that can be taken.
 */
public interface Takeable {
    /**
     * Controls if the object can be taken.
     *
     * @return true if object can be taken, false otherwise.
     */
    boolean isTaken();

    /**
     * Processes effect from object taking.
     */
    void affect();
}
