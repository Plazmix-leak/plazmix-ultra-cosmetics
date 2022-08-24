package net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffect;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.Particles;

/**
 * Represents an instance of ender aura particles summoned by a player.
 *
 * @author iSach
 * @since 12-23-2015
 */
public class ParticleEffectEnderAura extends ParticleEffect {

    public ParticleEffectEnderAura(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, owner, ParticleEffectType.valueOf("enderaura"));
    }

    @Override
    public void onUpdate() {
        Particles.PORTAL.display(0.35F, 0.05F, 0.35F, 0.1f, getModifiedAmount(5), getPlayer().getLocation().add(0, 1.2d, 0), 128);
        Particles.PORTAL.display(0.35F, 0.05F, 0.35F, 0.1f, getModifiedAmount(5), getPlayer().getLocation().add(0, 0.2d, 0), 128);
    }
}
