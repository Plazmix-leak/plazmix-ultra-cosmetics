package net.plazmix.ultracosmetics.util;

import net.plazmix.ultracosmetics.config.SettingsManager;

/**
 * Package: be.isach.ultracosmetics.util
 * Created by: sachalewin
 * Date: 26/08/16
 * Project: UltraCosmetics
 */
public class ConfigUtils {

    public static int getGadgetSlot() {
        return SettingsManager.getConfig().getInt("Gadget-Slot");
    }

}
