package net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.SoundUtil;
import net.plazmix.ultracosmetics.util.Sounds;
import net.plazmix.ultracosmetics.util.UtilParticles;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a fungun gadget summoned by a player.
 *
 * @author iSach
 * @since 10-12-2015
 */
public class GadgetFunGun extends Gadget {

    private List<Projectile> projectiles = new ArrayList<>();

    public GadgetFunGun(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("fungun"), ultraCosmetics);
    }

    @Override
    void onRightClick() {
        for (int i = 0; i < 5; i++)
            projectiles.add(getPlayer().launchProjectile(Snowball.class));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (!projectiles.contains(projectile)) return;

        Location location = projectile.getLocation();

        for (Projectile snowball : projectiles)
            snowball.remove();

        UtilParticles.display(Particles.LAVA, 1.3f, 1f, 1.3f, location, 16);
        UtilParticles.display(Particles.HEART, 0.8f, 0.8f, 0.8f, location, 20);
        SoundUtil.playSound(getPlayer(), Sounds.CAT_PURREOW, 1.4f, 1.5f);
    }

    @Override
    void onLeftClick() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onClear() {
        HandlerList.unregisterAll(this);
    }
}
