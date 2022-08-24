package net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffect;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.SoundUtil;
import net.plazmix.ultracosmetics.util.Sounds;
import net.plazmix.ultracosmetics.util.UtilParticles;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Represents an instance of frost lord particles summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class ParticleEffectFrostLord extends ParticleEffect {

    int step = 0;
    float stepY = 0;
    float radius = 1.5f;

    public ParticleEffectFrostLord(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, owner, ParticleEffectType.valueOf("frostlord"));
    }

    @Override
    public void onUpdate() {
        for (int i = 0; i < 6; i++) {
            Location location = getPlayer().getLocation();
            double inc = (2 * Math.PI) / 100;
            double angle = step * inc + stepY + i;
            Vector v = new Vector();
            v.setX(Math.cos(angle) * radius);
            v.setZ(Math.sin(angle) * radius);
            UtilParticles.display(getType().getEffect(), location.add(v).add(0, stepY, 0));
            location.subtract(v).subtract(0, stepY, 0);
            if (stepY < 3) {
                radius -= 0.022;
                stepY += 0.045;
            } else {
                stepY = 0;
                step = 0;
                radius = 1.5f;
                SoundUtil.playSound(getPlayer(), Sounds.DIG_SNOW, .5f, 1.5f);
                UtilParticles.display(getType().getEffect(), location.clone().add(0, 3, 0), getModifiedAmount(48), 0.3f);
            }
        }
    }
}
