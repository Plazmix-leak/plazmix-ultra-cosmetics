package net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.MathUtils;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.UtilParticles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Represents an instance of an antigravity gadget summoned by a player.
 *
 * @author iSach
 * @since 08-10-2015
 */
public class GadgetAntiGravity extends Gadget {

    private ArmorStand as;
    private boolean running;

    public GadgetAntiGravity(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("antigravity"), ultraCosmetics);
    }

    @Override
    void onRightClick() {
        as = getPlayer().getWorld().spawn(getPlayer().getLocation(), ArmorStand.class);
        as.setMetadata("NO_INTER", new FixedMetadataValue(getUltraCosmetics(), ""));
        as.setGravity(false);
        as.setSmall(true);
        running = true;
        as.setVisible(false);
        as.setHelmet(new ItemStack(Material.SEA_LANTERN));
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> running = false, 240);
    }

    @Override
    void onLeftClick() {
    }

    @Override
    public void onUpdate() {
        if (as != null && as.isValid()) {
            if (running) {
                as.setHeadPose(as.getHeadPose().add(0, 0.1, 0));
                UtilParticles.display(Particles.PORTAL, 3f, 3f, 3f, as.getLocation(), 150);
                UtilParticles.display(Particles.SPELL_WITCH, .3f, .3f, .3f, as.getEyeLocation(), 5);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Entity ent : as.getNearbyEntities(3, 2, 3)) {
                            if (ent instanceof LivingEntity && !(ent instanceof ArmorStand)) {
                                MathUtils.applyVelocity(ent, new Vector(0, 0.05, 0));
                            }
                        }
                    }
                }.runTask(getUltraCosmetics());
            } else {
                as.remove();
                as = null;
            }
        }
    }

    // Find a fkn alternative to this shit :^)
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        try {
            if (running) {
                if (as != null && as.isValid() && event.getReason().contains("Fly")) {
                    if (as.getLocation().distance(event.getPlayer().getLocation()) < 8) {
                        event.setCancelled(true);
                        return;
                    }
                }
                event.setCancelled(true);
            }
        } catch (Exception exc) {
        }
    }

    @Override
    public void onClear() {
        if (as != null) {
            as.remove();
        }
        running = false;
    }
}
