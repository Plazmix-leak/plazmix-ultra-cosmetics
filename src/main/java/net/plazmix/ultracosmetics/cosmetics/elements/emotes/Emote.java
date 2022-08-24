package net.plazmix.ultracosmetics.cosmetics.elements.emotes;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an instance of an emote summoned by a player.
 *
 * @author iSach
 * @since 06-17-2016
 */
public class Emote extends Cosmetic<EmoteType> {

    private EmoteAnimation animation;
    private ItemStack itemStack;

    public Emote(CosmeticPlayer owner, final EmoteType emoteType, UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.EMOTES, owner, emoteType);

        this.animation = new EmoteAnimation(getType().getTicksPerFrame(), this);

        owner.setCurrentEmote(this);
    }

    @Override
    protected void onEquip() {
        animation.start();
    }

    @Override
    protected synchronized void onClear() {
        animation.stop();
        getPlayer().getBukkitPlayer().getInventory().setHelmet(null);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    protected void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().equals(itemStack)) {
            event.setCancelled(true);
        }
        if (event.getCursor() != null && event.getCursor().equals(itemStack)) {
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryCreativeEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().equals(itemStack)) {
            event.setCancelled(true);
            getPlayer().getBukkitPlayer().closeInventory(); // Close the inventory because clicking again results in the event being handled client side
        }
        if (event.getCursor() != null && event.getCursor().equals(itemStack)) {
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            event.setCancelled(true);
            getPlayer().getBukkitPlayer().closeInventory(); // Close the inventory because clicking again results in the event being handled client side
        }
    }
}
