package net.plazmix.ultracosmetics.cosmetics.elements.hats;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.suits.ArmorSlot;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an instance of a hat summoned by a player.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class Hat extends Cosmetic<HatType> {

    public Hat(UltraCosmetics ultraCosmetics, CosmeticPlayer owner, HatType type) {
        super(ultraCosmetics, Category.HATS, owner, type);
    }

    @Override
    protected void onEquip() {
        getOwner().removeHat();
        getOwner().removeEmote();
        getOwner().removeSuit(ArmorSlot.HELMET);

        if (getPlayer().getInventory().getHelmet() != null) {
            getPlayer().sendMessage(MessageManager.getMessage("Hats.Must-Remove-Hat"));
            return;
        }

        getPlayer().getInventory().setHelmet(getType().getItemStack());
        getOwner().setCurrentHat(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();
        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)
                && getPlayer() != null
                && player.equals(getPlayer())
                && current != null
                && current.hasItemMeta()
                && current.getItemMeta().hasDisplayName()
                && getType().getItemStack() != null
                && current.getItemMeta().getDisplayName().equals(getType().getItemStack().getItemMeta().getDisplayName())) {
            event.setCancelled(true);
            if (event.getAction().name().contains("DROP")
                    && SettingsManager.getConfig().getBoolean("Remove-Gadget-With-Drop")) {
                clear();
            }
            player.updateInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryCreativeEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();
        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)
                && getPlayer() != null
                && player.equals(getPlayer())
                && current != null
                && current.hasItemMeta()
                && current.getItemMeta().hasDisplayName()
                && getType().getItemStack() != null
                && current.getItemMeta().getDisplayName().equals(getType().getItemStack().getItemMeta().getDisplayName())) {
            event.setCancelled(true);
            if (event.getAction().name().contains("DROP")
                    && SettingsManager.getConfig().getBoolean("Remove-Gadget-With-Drop")) {
                clear();
                player.closeInventory(); // Close the inventory because clicking again results in the event being handled client side
                return;
            }
            player.closeInventory(); // Close the inventory because clicking again results in the event being handled client side
        }
    }

    @Override
    protected void onClear() {
        getPlayer().getInventory().setHelmet(null);
        getOwner().setCurrentHat(null);
    }

    public ItemStack getItemStack() {
        return getType().getItemStack();
    }
}
