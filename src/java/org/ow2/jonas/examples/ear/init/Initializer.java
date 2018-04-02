package org.ow2.jonas.examples.ear.init;

/**
 * The {@link Initializer} Bean initialize the entities data.
 * @author Guillaume Sauthier
 */
public interface Initializer {

    /**
     * Initialize the minimal set of entities needed by the sample.
     */
    void initializeEntities();

}
