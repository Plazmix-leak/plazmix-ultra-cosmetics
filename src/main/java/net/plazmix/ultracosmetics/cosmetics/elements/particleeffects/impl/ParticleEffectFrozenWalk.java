package net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffect;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

/**
 * Represents an instance of frozen walk particles summoned by a player.
 *
 * @author iSach
 * @since 10-12-2015
 */
public class ParticleEffectFrozenWalk extends ParticleEffect {

    public ParticleEffectFrozenWalk(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, owner, ParticleEffectType.valueOf("frozenwalk"));
    }

    @Override
    public void onUpdate() {
        Vector vectorLeft = getLeftVector(getPlayer().getLocation()).normalize().multiply(0.15);
        Vector vectorRight = getRightVector(getPlayer().getLocation()).normalize().multiply(0.15);
        Location locationLeft = getPlayer().getLocation().add(vectorLeft);
        Location locationRight = getPlayer().getLocation().add(vectorRight);
        locationLeft.setY(getPlayer().getLocation().getY());
        locationRight.setY(getPlayer().getLocation().getY());
        if (UltraCosmeticsData.get().getServerVersion().compareTo(ServerVersion.v1_14_R1) >= 0) {
            locationLeft.getWorld().spawnParticle(Particle.ITEM_CRACK, locationLeft, 0, 0, 0, 0, 0, UCMaterial.SNOW.parseItem());
            locationLeft.getWorld().spawnParticle(Particle.ITEM_CRACK, locationRight, 0, 0, 0, 0, 0, UCMaterial.SNOW.parseItem());
        } else {
            Particles.ITEM_CRACK.display(new Particles.ItemData(Material.SNOW, (byte) 0), 0, 0, 0, 0f, 0, locationLeft, 32);
            Particles.ITEM_CRACK.display(new Particles.ItemData(Material.SNOW, (byte) 0), 0, 0, 0, 0f, 0, locationRight, 32);
        }
    }

    public static Vector getLeftVector(Location loc) {
        final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 0))));
        final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 0))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }

    public static Vector getRightVector(Location loc) {
        final float newX = (float) (loc.getX() + (-1 * Math.cos(Math.toRadians(loc.getYaw() + 0))));
        final float newZ = (float) (loc.getZ() + (-1 * Math.sin(Math.toRadians(loc.getYaw() + 0))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }
}
