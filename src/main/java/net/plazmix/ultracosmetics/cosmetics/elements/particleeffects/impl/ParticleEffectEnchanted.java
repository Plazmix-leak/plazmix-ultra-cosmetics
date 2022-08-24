package net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffect;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.MathUtils;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.UtilParticles;

/**
 * Represents an instance of enchanted particles summoned by a player.
 *
 * @author iSach
 * @since 10-12-2015
 */
public class ParticleEffectEnchanted extends ParticleEffect {

    public ParticleEffectEnchanted(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, owner, ParticleEffectType.valueOf("enchanted"));
    }

    @Override
    public void onUpdate() {
        UtilParticles.display(Particles.ENCHANTMENT_TABLE, getPlayer().getLocation().add(0, MathUtils.randomDouble(0.1, 2), 0), getModifiedAmount(60), 8f);
    }
}