package net.plazmix.ultracosmetics.cosmetics.elements.particleeffects;

import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.impl.*;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticMatType;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticType;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Particle effect types.
 *
 * @author iSach
 * @since 12-18-2015
 */
public class ParticleEffectType extends CosmeticMatType<ParticleEffect> {

    private final static List<ParticleEffectType> ENABLED = new ArrayList<>();
    private final static List<ParticleEffectType> VALUES = new ArrayList<>();

    public static List<ParticleEffectType> enabled() {
        return ENABLED;
    }

    public static List<ParticleEffectType> values() {
        return VALUES;
    }

    public static ParticleEffectType valueOf(String s) {
        for (ParticleEffectType particleEffectType : VALUES) {
            if (particleEffectType.getSignature().equalsIgnoreCase(s)) return particleEffectType;
        }
        return null;
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(CosmeticType::isEnabled).collect(Collectors.toList()));
    }

    private Particles effect;
    private int repeatDelay;
    private double particleMultiplier;

    private ParticleEffectType(String signature, int repeatDelay, Particles effect, UCMaterial material, Class<? extends ParticleEffect> clazz,ServerVersion baseVersion, boolean supportsParticleMultiplier, Boolean isEnable) {
        super(Category.EFFECTS, signature, material, clazz, baseVersion, isEnable);
        this.repeatDelay = repeatDelay;
        this.effect = effect;

        if (supportsParticleMultiplier) {
            String path = getCategory().getConfigPath() + "." + signature + ".Particle-Multiplier";

            if (!SettingsManager.getConfig().isDouble(path)) {
                particleMultiplier = 1;
                SettingsManager.getConfig().set(getCategory().getConfigPath() + "." + signature + ".Particle-Multiplier", 1.0, "A multiplier applied to the number", "of particles displayed. 1.0 is 100%");

            } else {
                particleMultiplier = SettingsManager.getConfig().getDouble(path);
            }
        }

        VALUES.add(this);
    }

    public Particles getEffect() {
        return effect;
    }

    public int getRepeatDelay() {
        return repeatDelay;
    }
    
    public double getParticleMultiplier() {
        return particleMultiplier;
    }

    public static void register() {
        new ParticleEffectType("SnowCloud", 1, Particles.SNOW_SHOVEL, UCMaterial.SNOWBALL, ParticleEffectSnowCloud.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("BloodHelix", 1, Particles.REDSTONE, UCMaterial.REDSTONE, ParticleEffectBloodHelix.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("FrostLord", 1, Particles.SNOW_SHOVEL, UCMaterial.PACKED_ICE, ParticleEffectFrostLord.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("FlameRings", 1, Particles.FLAME, UCMaterial.BLAZE_POWDER, ParticleEffectFlameRings.class, ServerVersion.v1_8_R1, false, true);
        new ParticleEffectType("GreenSparks", 1, Particles.VILLAGER_HAPPY, UCMaterial.EMERALD, ParticleEffectGreenSparks.class, ServerVersion.v1_8_R1, false, true);
        new ParticleEffectType("FrozenWalk", 1, Particles.SNOW_SHOVEL, UCMaterial.SNOWBALL, ParticleEffectFrozenWalk.class, ServerVersion.v1_8_R1, false, true);
        new ParticleEffectType("Music", 4, Particles.FLAME, UCMaterial.MUSIC_DISC_MALL, ParticleEffectMusic.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("Enchanted", 1, Particles.ENCHANTMENT_TABLE, UCMaterial.BOOK, ParticleEffectEnchanted.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("Inferno", 1, Particles.FLAME, UCMaterial.NETHER_WART, ParticleEffectInferno.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("AngelWings", 2, Particles.REDSTONE, UCMaterial.FEATHER, ParticleEffectAngelWings.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("SuperHero", 2, Particles.REDSTONE, UCMaterial.GLOWSTONE_DUST, ParticleEffectSuperHero.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("SantaHat", 2, Particles.REDSTONE, UCMaterial.BEACON, ParticleEffectSantaHat.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("EnderAura", 1, Particles.PORTAL, UCMaterial.ENDER_EYE, ParticleEffectEnderAura.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("FlameFairy", 1, Particles.FLAME, UCMaterial.BLAZE_POWDER, ParticleEffectFlameFairy.class, ServerVersion.v1_8_R1 ,false, true);
        new ParticleEffectType("RainCloud", 1, Particles.DRIP_WATER, UCMaterial.LAPIS_LAZULI, ParticleEffectRainCloud.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("CrushedCandyCane", 1, Particles.ITEM_CRACK, UCMaterial.WHITE_DYE, ParticleEffectCrushedCandyCane.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("InLove", 1, Particles.HEART, UCMaterial.RED_DYE, ParticleEffectInLove.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("MagicalRods", 3, Particles.REDSTONE, UCMaterial.BLAZE_ROD, ParticleEffectMagicalRods.class, ServerVersion.v1_8_R1, true, true);
        new ParticleEffectType("FireWaves", 4, Particles.FLAME, UCMaterial.GOLD_NUGGET, ParticleEffectFireWaves.class, ServerVersion.v1_8_R1, true, true);
    }
}
