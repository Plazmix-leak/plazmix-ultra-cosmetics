package net.plazmix.ultracosmetics;

import net.plazmix.ultracosmetics.cosmetics.elements.emotes.EmoteType;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.cosmetics.elements.hats.HatType;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.MountType;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;

public class CosmeticManager {

    private UltraCosmetics ultraCosmetics;

    public CosmeticManager(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
    }

    /**
     * Setup default Cosmetics ultraCosmetics.getConfig().
     */
    public void setupCosmeticsConfigs() {


        // CALL STATIC BLOCK.
        GadgetType.register();
        MountType.register();
        ParticleEffectType.register();
        PetType.register();
        HatType.register();
        SuitType.register();
        EmoteType.ANGRY.getSignature();

        GadgetType.checkEnabled();
        MountType.checkEnabled();
        ParticleEffectType.checkEnabled();
        PetType.checkEnabled();
        HatType.checkEnabled();
        SuitType.checkEnabled();
        EmoteType.checkEnabled();

    }

}
