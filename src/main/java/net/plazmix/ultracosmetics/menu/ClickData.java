package net.plazmix.ultracosmetics.menu;

import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Inventory click data.
 *
 * @author iSach
 * @since 08-09-2016
 */
public class ClickData {

    private Inventory inventory;
    private CosmeticPlayer clicker;
    private InventoryAction action;
    private ItemStack clicked;
    private int slot;

    public ClickData(Inventory inventory, CosmeticPlayer clicker, InventoryAction action, ItemStack clicked, int slot) {
        this.inventory = inventory;
        this.clicker = clicker;
        this.action = action;
        this.clicked = clicked;
        this.slot = slot;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ItemStack getClicked() {
        return clicked;
    }

    public int getSlot() {
        return slot;
    }

    public InventoryAction getAction() {
        return action;
    }

    public CosmeticPlayer getClicker() {
        return clicker;
    }
}
