package net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffect;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.UtilParticles;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Represents an instance of blood helix particles summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class ParticleEffectBloodHelix extends ParticleEffect {

    double i = 0;

    public ParticleEffectBloodHelix(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, owner, ParticleEffectType.valueOf("bloodhelix"));
    }

    @Override
    public void onUpdate() {
        Location location = getPlayer().getLocation();
        double radius = 1.1d;
        int steps = getModifiedAmount(100);
        double interval = (2 * Math.PI) / steps;
        for (int step = 0; step < steps; step += 4) {
            double angle = step * interval + i;
            Vector v1 = new Vector();
            Vector v2 = new Vector();
            v1.setX(Math.cos(angle) * radius);
            v1.setZ(Math.sin(angle) * radius);
            v2.setX(Math.cos(angle + 3.5) * radius);
            v2.setZ(Math.sin(angle + 3.5) * radius);
            UtilParticles.display(Particles.REDSTONE, location.clone().add(v1));
            UtilParticles.display(Particles.REDSTONE, location.clone().add(v2));
            location.add(0, 0.12d, 0);
            radius -= 4.4f / steps;
        }
        i += 0.05;
    }
}
