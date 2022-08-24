package net.plazmix.ultracosmetics.listeners;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.elements.suits.ArmorSlot;
import net.plazmix.ultracosmetics.menu.CosmeticsInventoryHolder;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.player.profile.CosmeticsProfile;
import net.plazmix.ultracosmetics.player.profile.CosmeticsProfileManager;
import net.plazmix.ultracosmetics.run.FallDamageManager;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

/**
 * Player listeners.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class PlayerListener implements Listener {

    private UltraCosmetics ultraCosmetics;

    public PlayerListener(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent event) {
        CosmeticPlayer cp = ultraCosmetics.getPlayerManager().create(event.getPlayer());
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (SettingsManager.getConfig().getBoolean("Menu-Item.Give-On-Join") && event.getPlayer().hasPermission("ultracosmetics.receivechest") && SettingsManager.getConfig().getStringList("Enabled-Worlds").contains(event.getPlayer().getWorld().getName())) {
                    Bukkit.getScheduler().runTaskLater(ultraCosmetics, () -> {
                        if (cp != null && event.getPlayer() != null)
                            cp.giveMenuItem();
                    }, 5);
                }


                if (SettingsManager.getConfig().getStringList("Enabled-Worlds").contains(event.getPlayer().getWorld().getName())
                        && UltraCosmeticsData.get().areCosmeticsProfilesEnabled()) {
                    // Cosmetics profile. TODO Add option to disable!!
                    CosmeticsProfileManager cosmeticsProfileManager = ultraCosmetics.getCosmeticsProfileManager();
                    if (cosmeticsProfileManager.getProfile(event.getPlayer().getUniqueId()) == null) {
                        // ultraCosmetics.getSmartLogger().write("Creating cosmetics profile for " + event.getPlayer().getName());
                        cosmeticsProfileManager.initForPlayer(cp);
                    } else {
                        //    ultraCosmetics.getSmartLogger().write("Loading cosmetics profile for " + event.getPlayer().getName());
                        CosmeticsProfile cosmeticsProfile = cosmeticsProfileManager.getProfile(event.getPlayer().getUniqueId());
                        cp.setCosmeticsProfile(cosmeticsProfile);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cosmeticsProfile.loadToPlayer(cp);
                            }
                        }.runTask(ultraCosmetics);
                    }
                }
            }
        };
        bukkitRunnable.runTaskAsynchronously(ultraCosmetics);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        CosmeticPlayer cosmeticPlayer = ultraCosmetics.getPlayerManager().getUltraPlayer(event.getPlayer());
        if (SettingsManager.getConfig().getStringList("Enabled-Worlds").contains(event.getPlayer().getWorld().getName())) {
            if (SettingsManager.getConfig().getBoolean("Menu-Item.Give-On-Join") && event.getPlayer().hasPermission("ultracosmetics.receivechest")) {
                ultraCosmetics.getPlayerManager().getUltraPlayer(event.getPlayer()).giveMenuItem();
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (UltraCosmeticsData.get().areCosmeticsProfilesEnabled()) {

                        CosmeticsProfile cp = ultraCosmetics.getCosmeticsProfileManager().getProfile(event.getPlayer().getUniqueId());
                        if (cp == null) {
                            ultraCosmetics.getCosmeticsProfileManager().initForPlayer(cosmeticPlayer);
                        } else {
                            cp.loadToPlayer();
                        }
                    }
                }
            }.runTaskLater(ultraCosmetics, 5);
        } else { // Disable cosmetics when joining a bad world.
            cosmeticPlayer.removeMenuItem();
            cosmeticPlayer.setQuitting(true);
            if (cosmeticPlayer.clear())
                cosmeticPlayer.getBukkitPlayer().sendMessage(MessageManager.getMessage("World-Disabled"));
            cosmeticPlayer.setQuitting(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
        if (isMenuItem(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getItemDrop().remove();
            ItemStack chest = event.getPlayer().getItemInHand().clone();
            chest.setAmount(1);
            event.getPlayer().setItemInHand(chest);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        CosmeticPlayer cosmeticPlayer = ultraCosmetics.getPlayerManager().getUltraPlayer(event.getPlayer());
        // Avoid triggering this when clicking in the inventory
        InventoryType t = event.getPlayer().getOpenInventory().getType();
        if (t != InventoryType.CRAFTING
                && t != InventoryType.CREATIVE) {
            return;
        }

        if (isMenuItem(event.getItem())) {
            event.setCancelled(true);
            ultraCosmetics.getMenus().getMainMenu().open(cosmeticPlayer);
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!SettingsManager.getConfig().getStringList("Enabled-Worlds").contains(player.getWorld().getName())) return;
        if (event.getView().getTopInventory().getHolder() instanceof CosmeticsInventoryHolder
                || isMenuItem(event.getCurrentItem())
                || (event.getClick() == ClickType.NUMBER_KEY && isMenuItem(player.getInventory().getItem(event.getHotbarButton())))) {
            event.setCancelled(true);
            player.updateInventory();
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelMove(InventoryCreativeEvent event) {
        Player player = (Player) event.getWhoClicked();
        if ((SettingsManager.getConfig().getStringList("Enabled-Worlds")).contains(player.getWorld().getName())) {
            if (isMenuItem(event.getCurrentItem())) {
                event.setCancelled(true);
                player.closeInventory(); // Close the inventory because clicking again results in the event being handled client side
            }
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelMove(InventoryDragEvent event) {
        for (ItemStack item : event.getNewItems().values()) {
            if (isMenuItem(item)) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).updateInventory();
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if ((boolean) SettingsManager.getConfig().get("Menu-Item.Give-On-Respawn") && ((List<String>) SettingsManager.getConfig().get("Enabled-Worlds")).contains(event.getPlayer().getWorld().getName())) {
            int slot = SettingsManager.getConfig().getInt("Menu-Item.Slot");
            if (event.getPlayer().getInventory().getItem(slot) != null) {
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), event.getPlayer().getInventory().getItem(slot));
                event.getPlayer().getInventory().setItem(slot, null);
            }
            String name = ChatColor.translateAlternateColorCodes('&', String.valueOf(SettingsManager.getConfig().get("Menu-Item.Displayname")));
            UCMaterial material = UCMaterial.matchUCMaterial((String) SettingsManager.getConfig().get("Menu-Item.Type"));
            // byte data = Byte.valueOf(String.valueOf(SettingsManager.getConfig().get("Menu-Item.Data"))); TODO
            event.getPlayer().getInventory().setItem(slot, ItemFactory.create(material, name));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        // TODO: Do anything with cosmetics profile?
        CosmeticPlayer up = ultraCosmetics.getPlayerManager().getUltraPlayer(event.getPlayer());
        up.setQuitting(true);
        up.clear();
        up.removeMenuItem();
        if (UltraCosmeticsData.get().areCosmeticsProfilesEnabled())
            ultraCosmetics.getCosmeticsProfileManager().clearPlayerFromProfile(up);
        ultraCosmetics.getPlayerManager().remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        int slot = SettingsManager.getConfig().getInt("Menu-Item.Slot");
        if (isMenuItem(event.getEntity().getInventory().getItem(slot))) {
            event.getDrops().remove(event.getEntity().getInventory().getItem(slot));
            event.getEntity().getInventory().setItem(slot, null);
        }
        CosmeticPlayer cosmeticPlayer = ultraCosmetics.getPlayerManager().getUltraPlayer(event.getEntity());
        if (cosmeticPlayer.getCurrentGadget() != null)
            event.getDrops().remove(event.getEntity().getInventory().getItem((Integer) SettingsManager.getConfig().get("Gadget-Slot")));
        if (cosmeticPlayer.getCurrentHat() != null)
            event.getDrops().remove(cosmeticPlayer.getCurrentHat().getItemStack());
        Arrays.asList(ArmorSlot.values()).forEach(armorSlot -> {
            if (cosmeticPlayer.getSuit(armorSlot) != null) {
                event.getDrops().remove(cosmeticPlayer.getSuit(armorSlot).getItemStack());
            }
        });
        if (cosmeticPlayer.getCurrentEmote() != null)
            event.getDrops().remove(cosmeticPlayer.getCurrentEmote().getItemStack());
        cosmeticPlayer.clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                && FallDamageManager.shouldBeProtected(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickUpItem(PlayerPickupItemEvent event) {
        if (isMenuItem(event.getItem().getItemStack())) {
            event.setCancelled(true);
            event.getItem().remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractGhost(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() != null
                && event.getRightClicked().hasMetadata("C_AD_ArmorStand"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (SettingsManager.getConfig().getList("Disabled-Commands").contains(event.getMessage().split(" ")[0].replace("/", "").toLowerCase())) {
            CosmeticPlayer player = ultraCosmetics.getPlayerManager().getUltraPlayer(event.getPlayer());
            if (player.getCurrentEmote() != null || player.getCurrentHat() != null || player.hasSuitOn()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MessageManager.getMessage("Disabled-Command-Wearing-Message"));
            } else if (player.getCurrentGadget() != null && player.getCurrentGadget().getItemStack().equals(event.getPlayer().getItemInHand())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MessageManager.getMessage("Disabled-Command-Holding-Message"));
            }
        }
    }

    private boolean isMenuItem(ItemStack item) {
        return item != null
                && item.hasItemMeta()
                && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', String.valueOf(SettingsManager.getConfig().get("Menu-Item.Displayname"))));
    }
}
