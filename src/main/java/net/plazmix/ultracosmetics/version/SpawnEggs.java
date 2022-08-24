package net.plazmix.ultracosmetics.version;

import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.inventory.ItemStack;

/**
 * @author RadBuilder
 */
public class SpawnEggs {
    public static ItemStack getEggFromData(byte b) {
        return UCMaterial.PIG_SPAWN_EGG.parseItem();
    }
}
