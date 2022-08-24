package net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.MathUtils;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Represents an instance of a thor hammer gadget summoned by a player.
 *
 * @author iSach
 * @since 08-08-2015
 */
public class GadgetThorHammer extends Gadget implements Listener {

    ArrayList<Item> hammer = new ArrayList<>();
    Vector v;

    public GadgetThorHammer(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("thorhammer"), ultraCosmetics);
    }

    @Override
    void onRightClick() {
        final Item i = getPlayer().getWorld().dropItem(getPlayer().getEyeLocation(), ItemFactory.create(UCMaterial.IRON_AXE, MessageManager.getMessage("Gadgets.ThorHammer.name")));
        i.setPickupDelay(0);
        i.setVelocity(getPlayer().getEyeLocation().getDirection().multiply(1.4));
        getPlayer().getInventory().setItem((int) SettingsManager.getConfig().get("Gadget-Slot"), null);
        hammer.add(i);
        v = getPlayer().getEyeLocation().getDirection().multiply(1.4).add(new Vector(0, 1, 0));
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            i.setVelocity(getPlayer().getEyeLocation().toVector().subtract(i.getLocation().toVector()).multiply(0.2).add(new Vector(0, 0, 0)));
            v = null;
            Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
                if (i.isValid()) {
                    ItemStack is;
                    if (UltraCosmeticsData.get().isAmmoEnabled()) {
                        is = ItemFactory.create(getType().getMaterial(), ChatColor.WHITE + "" + ChatColor.BOLD + getOwner().getAmmo(getType().toString().toLowerCase()) + " " + getType().getName(), ChatColor.BLUE + "Gadget");
                    } else {
                        is = ItemFactory.create(getType().getMaterial(), getType().getName(), MessageManager.getMessage("Gadgets.Lore"));
                    }
                    itemStack = is;
                    getPlayer().getInventory().setItem((int) SettingsManager.getConfig().get("Gadget-Slot"), is);
                    i.remove();
                }
            }, 40);
        }, 20);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (hammer.contains(event.getItem())) {
            event.setCancelled(true);
            if (event.getPlayer() == getPlayer()) {
                if (event.getItem().getTicksLived() > 5) {
                    ItemStack is;
                    if (UltraCosmeticsData.get().isAmmoEnabled()) {
                        is = ItemFactory.create(getType().getMaterial(), ChatColor.WHITE + "" + ChatColor.BOLD + getOwner().getAmmo(getType().toString().toLowerCase()) + " " + getType().getName(), ChatColor.BLUE + "Gadget");
                    } else {
                        is = ItemFactory.create(getType().getMaterial(), getType().getName(), MessageManager.getMessage("Gadgets.Lore"));
                    }
                    itemStack = is;
                    getPlayer().getInventory().setItem((int) SettingsManager.getConfig().get("Gadget-Slot"), is);
                    hammer.remove(event.getItem());
                    event.getItem().remove();
                }
            } else {
                if (v != null)
                    if (affectPlayers)
                        MathUtils.applyVelocity(event.getPlayer(), v);
            }
        }
    }

    @EventHandler
    public void onDamEnt(EntityDamageByEntityEvent event) {
        if (getOwner() != null
                && getPlayer() != null
                && event.getDamager() == getPlayer()
                && getPlayer().getItemInHand().equals(getItemStack())) {
            event.setCancelled(true);
        }
    }

    @Override
    void onLeftClick() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onClear() {
        for (Item i : hammer) {
            i.remove();
        }
        hammer.clear();
        v = null;
        HandlerList.unregisterAll(this);
    }
}
