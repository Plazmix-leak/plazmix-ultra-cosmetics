package net.plazmix.ultracosmetics.cosmetics.elements.mounts.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.Mount;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.MountType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.PlayerUtils;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.util.Vector;

/**
 * Represents an instance of a hype cart mount.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class MountHypeCart extends Mount<Minecart> {

    public MountHypeCart(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, MountType.valueOf("hypecart"), ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        if (entity.isOnGround()) {
            Vector vector = PlayerUtils.getHorizontalDirection(getPlayer(), 7.6);
            if(Math.abs(vector.getX()) > 4) {
                vector.setX(vector.getX() < 0 ? -4 : 4);
            }
            if(Math.abs(vector.getZ()) > 4) {
                vector.setZ(vector.getZ() < 0 ? -4 : 4);
            }
            entity.setVelocity(vector);
        }
        UltraCosmeticsData.get().getVersionManager().getEntityUtil().setClimb(entity);
    }

    @EventHandler
    public void onDestroy(VehicleDestroyEvent event) {
        if (event.getVehicle() == entity) {
            event.setCancelled(true);
        }
    }
}
