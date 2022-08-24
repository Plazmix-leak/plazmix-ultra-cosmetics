package net.plazmix.ultracosmetics.v1_12_R1;

import net.plazmix.ultracosmetics.v1_12_R1.customentities.CustomEntities;
import net.plazmix.ultracosmetics.version.IModule;

/**
 * @author RadBuilder
 */
public class Module implements IModule {


    @Override
    public void enable() {
        CustomEntities.registerEntities();
    }

    @Override
    public void disable() {
        CustomEntities.unregisterEntities();
    }
}
