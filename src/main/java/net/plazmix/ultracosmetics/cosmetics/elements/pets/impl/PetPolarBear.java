package net.plazmix.ultracosmetics.cosmetics.elements.pets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.UCMaterial;

/**
 * Represents an instance of a polar bear pet summoned by a player.
 *
 * @author RadBuilder
 * @since 10-21-2017
 */
public class PetPolarBear extends Pet {
    public PetPolarBear(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, PetType.getByName("polarbear"), ItemFactory.create(UCMaterial.SNOW_BLOCK, UltraCosmeticsData.get().getItemNoPickupString()));
    }
}
