package net.plazmix.ultracosmetics.cosmetics.elements.pets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import org.bukkit.entity.Wither;

/**
 * Represents an instance of a wither pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetWither extends Pet {

    public PetWither(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, PetType.getByName("wither"), null);
    }

    @Override
    public void onUpdate() {
        UltraCosmeticsData.get().getVersionManager().getEntityUtil().resetWitherSize((Wither) getEntity());
    }
}
